"""HTTP 客户端"""

import hmac
import hashlib
import time
import uuid
from typing import Any, Dict, Optional, TypeVar, Type
from dataclasses import dataclass

import httpx
from pydantic import BaseModel

from zsxq.exception import (
    ZsxqException,
    NetworkException,
    TimeoutException,
    create_exception,
)


@dataclass
class HttpConfig:
    """HTTP 客户端配置"""
    token: str
    base_url: str = "https://api.zsxq.com"
    timeout: float = 10.0
    retry_count: int = 3
    retry_delay: float = 1.0
    device_id: str = ""
    app_version: str = "2.83.0"

    def __post_init__(self):
        if not self.device_id:
            self.device_id = str(uuid.uuid4())


T = TypeVar("T", bound=BaseModel)


class HttpClient:
    """HTTP 客户端 - 处理请求、签名、重试"""

    def __init__(self, config: HttpConfig):
        self.config = config
        self._client: Optional[httpx.AsyncClient] = None

    async def _get_client(self) -> httpx.AsyncClient:
        """获取或创建 HTTP 客户端"""
        if self._client is None or self._client.is_closed:
            self._client = httpx.AsyncClient(
                base_url=self.config.base_url,
                timeout=self.config.timeout,
                headers={
                    "Content-Type": "application/json; charset=utf-8",
                    "User-Agent": f"xiaomiquan/{self.config.app_version} SDK/1.0.0",
                    "x-version": self.config.app_version,
                },
            )
        return self._client

    async def close(self):
        """关闭 HTTP 客户端"""
        if self._client and not self._client.is_closed:
            await self._client.aclose()
            self._client = None

    def _generate_signature(
        self, timestamp: str, method: str, path: str, body: Optional[str] = None
    ) -> str:
        """生成请求签名"""
        secret_key = "zsxq-sdk-secret"
        sign_data = f"{timestamp}\n{method.upper()}\n{path}"
        if body:
            sign_data += f"\n{body}"

        return hmac.new(
            secret_key.encode(),
            sign_data.encode(),
            hashlib.sha1
        ).hexdigest()

    def _build_headers(
        self, method: str, path: str, body: Optional[str] = None
    ) -> tuple[Dict[str, str], str]:
        """构建请求头"""
        timestamp = str(int(time.time()))
        request_id = str(uuid.uuid4())
        signature = self._generate_signature(timestamp, method, path, body)

        headers = {
            "authorization": self.config.token,
            "x-timestamp": timestamp,
            "x-signature": signature,
            "x-request-id": request_id,
            "x-aduid": self.config.device_id,
        }

        return headers, request_id

    async def _do_request(
        self,
        method: str,
        path: str,
        params: Optional[Dict[str, Any]] = None,
        json_data: Optional[Dict[str, Any]] = None,
    ) -> tuple[Dict[str, Any], str]:
        """执行请求"""
        body_str = None
        if json_data:
            import json
            body_str = json.dumps(json_data)

        headers, request_id = self._build_headers(method, path, body_str)
        client = await self._get_client()

        try:
            response = await client.request(
                method=method,
                url=path,
                params=params,
                json=json_data,
                headers=headers,
            )
            response.raise_for_status()
            return response.json(), request_id
        except httpx.TimeoutException as e:
            raise TimeoutException("请求超时", request_id, e)
        except httpx.HTTPError as e:
            raise NetworkException(f"网络错误: {e}", request_id, e)

    async def _execute_with_retry(
        self,
        method: str,
        path: str,
        params: Optional[Dict[str, Any]] = None,
        json_data: Optional[Dict[str, Any]] = None,
    ) -> tuple[Dict[str, Any], str]:
        """执行请求（带重试）"""
        import asyncio

        last_error: Optional[Exception] = None
        request_id = ""

        for i in range(self.config.retry_count + 1):
            try:
                return await self._do_request(method, path, params, json_data)
            except (NetworkException, TimeoutException) as e:
                last_error = e
                request_id = getattr(e, "request_id", "")

                if i < self.config.retry_count:
                    delay = self.config.retry_delay * (2 ** i)
                    await asyncio.sleep(delay)
            except Exception as e:
                raise e

        if last_error:
            raise last_error
        raise NetworkException("请求失败", request_id)

    def _handle_response(
        self, data: Dict[str, Any], request_id: str
    ) -> Dict[str, Any]:
        """处理响应"""
        if data.get("succeeded"):
            return data.get("resp_data", {})

        code = data.get("code", 0)
        message = data.get("error") or data.get("info") or "未知错误"
        raise create_exception(code, message, request_id)

    async def get(
        self,
        path: str,
        params: Optional[Dict[str, Any]] = None,
    ) -> Dict[str, Any]:
        """GET 请求"""
        response, request_id = await self._execute_with_retry("GET", path, params=params)
        return self._handle_response(response, request_id)

    async def post(
        self,
        path: str,
        data: Optional[Dict[str, Any]] = None,
    ) -> Dict[str, Any]:
        """POST 请求"""
        response, request_id = await self._execute_with_retry("POST", path, json_data=data)
        return self._handle_response(response, request_id)

    async def put(
        self,
        path: str,
        data: Optional[Dict[str, Any]] = None,
    ) -> Dict[str, Any]:
        """PUT 请求"""
        response, request_id = await self._execute_with_retry("PUT", path, json_data=data)
        return self._handle_response(response, request_id)

    async def delete(self, path: str) -> Dict[str, Any]:
        """DELETE 请求"""
        response, request_id = await self._execute_with_retry("DELETE", path)
        return self._handle_response(response, request_id)
