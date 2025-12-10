"""HTTP客户端测试"""

import pytest
import httpx
from unittest.mock import AsyncMock, MagicMock, patch
from zsxq.http import HttpClient, HttpConfig
from zsxq.exception import NetworkException, TimeoutException


def test_http_config_default_device_id():
    """测试HTTP配置默认生成设备ID"""
    config = HttpConfig(token="test-token")

    assert config.device_id != ""
    assert len(config.device_id) > 0


def test_http_config_custom_device_id():
    """测试HTTP配置自定义设备ID"""
    config = HttpConfig(token="test-token", device_id="custom-device")

    assert config.device_id == "custom-device"


def test_generate_signature():
    """测试签名生成"""
    config = HttpConfig(token="test-token")
    client = HttpClient(config)

    timestamp = "1234567890"
    method = "GET"
    path = "/v2/groups"

    signature = client._generate_signature(timestamp, method, path)

    assert len(signature) == 40  # HMAC-SHA1 hex = 40 chars
    assert all(c in "0123456789abcdef" for c in signature)


def test_generate_signature_with_body():
    """测试带请求体的签名生成"""
    config = HttpConfig(token="test-token")
    client = HttpClient(config)

    timestamp = "1234567890"
    method = "POST"
    path = "/v2/topics"
    body = '{"text":"test"}'

    signature = client._generate_signature(timestamp, method, path, body)

    assert len(signature) == 40
    assert all(c in "0123456789abcdef" for c in signature)


def test_build_headers():
    """测试请求头构建"""
    config = HttpConfig(
        token="test-token",
        device_id="device-123",
        app_version="5.60.0",
    )
    client = HttpClient(config)

    headers, request_id = client._build_headers("GET", "/v2/groups")

    assert headers["authorization"] == "test-token"
    assert headers["x-aduid"] == "device-123"
    assert "x-timestamp" in headers
    assert "x-signature" in headers
    assert "x-request-id" in headers
    assert headers["x-request-id"] == request_id
    assert len(headers["x-signature"]) == 40


def test_build_headers_with_body():
    """测试带请求体的请求头构建"""
    config = HttpConfig(token="test-token")
    client = HttpClient(config)

    body = '{"text":"test"}'
    headers, request_id = client._build_headers("POST", "/v2/topics", body)

    assert "x-signature" in headers
    assert len(headers["x-signature"]) == 40
    assert request_id != ""


@pytest.mark.asyncio
async def test_get_client_creation():
    """测试HTTP客户端创建"""
    config = HttpConfig(token="test-token", base_url="https://api.test.com")
    client = HttpClient(config)

    http_client = await client._get_client()

    assert isinstance(http_client, httpx.AsyncClient)
    assert str(http_client.base_url) == "https://api.test.com"

    await client.close()


@pytest.mark.asyncio
async def test_close_client():
    """测试关闭HTTP客户端"""
    config = HttpConfig(token="test-token")
    client = HttpClient(config)

    # 创建客户端
    await client._get_client()
    assert client._client is not None

    # 关闭客户端
    await client.close()
    assert client._client is None


@pytest.mark.asyncio
async def test_get_request_success():
    """测试成功的GET请求"""
    config = HttpConfig(token="test-token")
    client = HttpClient(config)

    mock_response = MagicMock()
    mock_response.json.return_value = {
        "succeeded": True,
        "resp_data": {"test": "data"},
    }
    mock_response.raise_for_status = MagicMock()

    with patch.object(client, "_get_client") as mock_get_client:
        mock_http_client = AsyncMock()
        mock_http_client.request = AsyncMock(return_value=mock_response)
        mock_get_client.return_value = mock_http_client

        response, request_id = await client._do_request("GET", "/v2/groups")

        assert response["succeeded"] is True
        assert response["resp_data"]["test"] == "data"
        assert request_id != ""

    await client.close()


@pytest.mark.asyncio
async def test_post_request_success():
    """测试成功的POST请求"""
    config = HttpConfig(token="test-token")
    client = HttpClient(config)

    mock_response = MagicMock()
    mock_response.json.return_value = {
        "succeeded": True,
        "resp_data": {"id": 123},
    }
    mock_response.raise_for_status = MagicMock()

    with patch.object(client, "_get_client") as mock_get_client:
        mock_http_client = AsyncMock()
        mock_http_client.request = AsyncMock(return_value=mock_response)
        mock_get_client.return_value = mock_http_client

        json_data = {"text": "test"}
        response, request_id = await client._do_request("POST", "/v2/topics", json_data=json_data)

        assert response["resp_data"]["id"] == 123
        assert request_id != ""

    await client.close()


@pytest.mark.asyncio
async def test_timeout_exception():
    """测试超时异常"""
    config = HttpConfig(token="test-token", timeout=0.001)
    client = HttpClient(config)

    with patch.object(client, "_get_client") as mock_get_client:
        mock_http_client = AsyncMock()
        mock_http_client.request = AsyncMock(side_effect=httpx.TimeoutException("Timeout"))
        mock_get_client.return_value = mock_http_client

        with pytest.raises(TimeoutException) as exc_info:
            await client._do_request("GET", "/v2/groups")

        assert "请求超时" in str(exc_info.value)

    await client.close()


@pytest.mark.asyncio
async def test_network_exception():
    """测试网络异常"""
    config = HttpConfig(token="test-token")
    client = HttpClient(config)

    with patch.object(client, "_get_client") as mock_get_client:
        mock_http_client = AsyncMock()
        mock_http_client.request = AsyncMock(side_effect=httpx.HTTPError("Network Error"))
        mock_get_client.return_value = mock_http_client

        with pytest.raises(NetworkException) as exc_info:
            await client._do_request("GET", "/v2/groups")

        assert "网络错误" in str(exc_info.value)

    await client.close()


@pytest.mark.asyncio
async def test_retry_on_network_error():
    """测试网络错误时重试"""
    config = HttpConfig(token="test-token", retry_count=2, retry_delay=0.01)
    client = HttpClient(config)

    call_count = 0

    async def mock_request(*args, **kwargs):
        nonlocal call_count
        call_count += 1

        if call_count < 3:
            raise httpx.HTTPError("Network Error")

        # 第三次成功
        response = MagicMock()
        response.json.return_value = {"succeeded": True, "resp_data": {}}
        response.raise_for_status = MagicMock()
        return response

    with patch.object(client, "_get_client") as mock_get_client:
        mock_http_client = AsyncMock()
        mock_http_client.request = mock_request
        mock_get_client.return_value = mock_http_client

        response, _ = await client._execute_with_retry("GET", "/v2/groups")

        assert response["succeeded"] is True
        assert call_count == 3  # 1次原始请求 + 2次重试

    await client.close()


@pytest.mark.asyncio
async def test_max_retry_reached():
    """测试达到最大重试次数"""
    config = HttpConfig(token="test-token", retry_count=2, retry_delay=0.01)
    client = HttpClient(config)

    with patch.object(client, "_get_client") as mock_get_client:
        mock_http_client = AsyncMock()
        mock_http_client.request = AsyncMock(side_effect=httpx.HTTPError("Network Error"))
        mock_get_client.return_value = mock_http_client

        with pytest.raises(NetworkException):
            await client._execute_with_retry("GET", "/v2/groups")

    await client.close()


@pytest.mark.asyncio
async def test_get_with_params():
    """测试带参数的GET请求"""
    config = HttpConfig(token="test-token")
    client = HttpClient(config)

    mock_response = MagicMock()
    mock_response.json.return_value = {
        "succeeded": True,
        "resp_data": [],
    }
    mock_response.raise_for_status = MagicMock()

    with patch.object(client, "_get_client") as mock_get_client:
        mock_http_client = AsyncMock()
        mock_http_client.request = AsyncMock(return_value=mock_response)
        mock_get_client.return_value = mock_http_client

        params = {"count": 10, "scope": "all"}
        await client._do_request("GET", "/v2/topics", params=params)

        # 验证参数被传递
        call_args = mock_http_client.request.call_args
        assert call_args[1]["params"] == params

    await client.close()
