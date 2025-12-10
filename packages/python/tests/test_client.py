"""客户端测试"""

import pytest
from zsxq.client import ZsxqClient, ZsxqClientBuilder, ZsxqConfig


def test_builder_set_token():
    """测试设置Token"""
    builder = ZsxqClientBuilder()
    result = builder.set_token("test-token")

    assert result is builder  # 链式调用
    assert builder._token == "test-token"


def test_builder_set_base_url():
    """测试设置基础URL"""
    builder = ZsxqClientBuilder()
    builder.set_base_url("https://custom.api.com")

    assert builder._base_url == "https://custom.api.com"


def test_builder_set_timeout():
    """测试设置超时时间"""
    builder = ZsxqClientBuilder()
    builder.set_timeout(5.0)

    assert builder._timeout == 5.0


def test_builder_set_retry_count():
    """测试设置重试次数"""
    builder = ZsxqClientBuilder()
    builder.set_retry_count(5)

    assert builder._retry_count == 5


def test_builder_set_retry_delay():
    """测试设置重试延迟"""
    builder = ZsxqClientBuilder()
    builder.set_retry_delay(2.0)

    assert builder._retry_delay == 2.0


def test_builder_set_retry():
    """测试设置重试配置（快捷方法）"""
    builder = ZsxqClientBuilder()
    builder.set_retry(5, 2.0)

    assert builder._retry_count == 5
    assert builder._retry_delay == 2.0


def test_builder_set_retry_without_delay():
    """测试设置重试次数（不设置延迟）"""
    builder = ZsxqClientBuilder()
    old_delay = builder._retry_delay
    builder.set_retry(5)

    assert builder._retry_count == 5
    assert builder._retry_delay == old_delay  # 延迟未改变


def test_builder_set_device_id():
    """测试设置设备ID"""
    builder = ZsxqClientBuilder()
    builder.set_device_id("device-123")

    assert builder._device_id == "device-123"


def test_builder_set_app_version():
    """测试设置应用版本"""
    builder = ZsxqClientBuilder()
    builder.set_app_version("5.60.0")

    assert builder._app_version == "5.60.0"


def test_builder_build_without_token():
    """测试没有设置Token时构建失败"""
    builder = ZsxqClientBuilder()

    with pytest.raises(ValueError) as exc_info:
        builder.build()

    assert "Token is required" in str(exc_info.value)


def test_builder_build_with_token():
    """测试设置Token后成功构建"""
    builder = ZsxqClientBuilder()
    client = builder.set_token("test-token").build()

    assert isinstance(client, ZsxqClient)
    assert client.groups is not None
    assert client.topics is not None
    assert client.users is not None
    assert client.checkins is not None
    assert client.dashboard is not None


def test_builder_build_with_empty_token():
    """测试空Token时构建失败"""
    builder = ZsxqClientBuilder()
    builder.set_token("")

    with pytest.raises(ValueError) as exc_info:
        builder.build()

    assert "Token is required" in str(exc_info.value)


def test_builder_chaining():
    """测试链式调用"""
    builder = ZsxqClientBuilder()

    result = (
        builder.set_token("test-token")
        .set_base_url("https://custom.api.com")
        .set_timeout(5.0)
        .set_retry_count(3)
        .set_retry_delay(1.0)
        .set_device_id("device-123")
        .set_app_version("5.60.0")
    )

    assert result is builder
    client = builder.build()
    assert isinstance(client, ZsxqClient)


def test_builder_with_all_configs():
    """测试设置所有配置项"""
    client = (
        ZsxqClientBuilder()
        .set_token("test-token")
        .set_base_url("https://custom.api.com")
        .set_timeout(8.0)
        .set_retry_count(3)
        .set_retry_delay(1.5)
        .set_device_id("my-device")
        .set_app_version("5.60.0")
        .build()
    )

    assert isinstance(client, ZsxqClient)


def test_builder_default_values():
    """测试默认值"""
    builder = ZsxqClientBuilder()

    assert builder._base_url == "https://api.zsxq.com"
    assert builder._timeout == 10.0
    assert builder._retry_count == 3
    assert builder._retry_delay == 1.0
    assert builder._app_version == "2.83.0"
    assert builder._device_id != ""  # 应该有默认生成的UUID


def test_client_modules_initialization():
    """测试客户端模块初始化"""
    client = ZsxqClientBuilder().set_token("test-token").build()

    assert client.groups is not None
    assert client.topics is not None
    assert client.users is not None
    assert client.checkins is not None
    assert client.dashboard is not None


def test_config_dataclass():
    """测试配置dataclass"""
    config = ZsxqConfig(
        token="test-token",
        base_url="https://custom.api.com",
        timeout=5.0,
        retry_count=3,
        retry_delay=1.0,
        device_id="device-123",
        app_version="5.60.0",
    )

    assert config.token == "test-token"
    assert config.base_url == "https://custom.api.com"
    assert config.timeout == 5.0
    assert config.retry_count == 3
    assert config.retry_delay == 1.0
    assert config.device_id == "device-123"
    assert config.app_version == "5.60.0"


def test_config_default_values():
    """测试配置默认值"""
    config = ZsxqConfig(token="test-token")

    assert config.base_url == "https://api.zsxq.com"
    assert config.timeout == 10.0
    assert config.retry_count == 3
    assert config.retry_delay == 1.0
    assert config.app_version == "2.83.0"
    assert config.device_id != ""  # 应该有默认生成的UUID


@pytest.mark.asyncio
async def test_client_context_manager():
    """测试客户端上下文管理器"""
    async with ZsxqClientBuilder().set_token("test-token").build() as client:
        assert isinstance(client, ZsxqClient)

    # 客户端应该已关闭


@pytest.mark.asyncio
async def test_client_close():
    """测试手动关闭客户端"""
    client = ZsxqClientBuilder().set_token("test-token").build()

    await client.close()
    # 关闭后不应抛出异常
