"""客户端模块"""

from dataclasses import dataclass, field
from typing import Optional
import uuid

from zsxq.http import HttpClient, HttpConfig
from zsxq.request import (
    GroupsRequest,
    TopicsRequest,
    UsersRequest,
    CheckinsRequest,
    DashboardRequest,
    RankingRequest,
    MiscRequest,
)


@dataclass
class ZsxqConfig:
    """SDK 配置"""
    token: str
    base_url: str = "https://api.zsxq.com"
    timeout: float = 10.0
    retry_count: int = 3
    retry_delay: float = 1.0
    device_id: str = field(default_factory=lambda: str(uuid.uuid4()))
    app_version: str = "2.83.0"


class ZsxqClient:
    """知识星球 SDK 主客户端

    使用 ZsxqClientBuilder 构建实例:
        client = ZsxqClientBuilder() \\
            .set_token("your-token") \\
            .build()

        # 获取星球列表
        groups = await client.groups.list()

        # 获取话题
        topics = await client.topics.list(group_id)
    """

    def __init__(self, config: ZsxqConfig):
        http_config = HttpConfig(
            token=config.token,
            base_url=config.base_url,
            timeout=config.timeout,
            retry_count=config.retry_count,
            retry_delay=config.retry_delay,
            device_id=config.device_id,
            app_version=config.app_version,
        )
        self._http_client = HttpClient(http_config)

        # 初始化各功能模块
        self.groups = GroupsRequest(self._http_client)
        self.topics = TopicsRequest(self._http_client)
        self.users = UsersRequest(self._http_client)
        self.checkins = CheckinsRequest(self._http_client)
        self.dashboard = DashboardRequest(self._http_client)
        self.ranking = RankingRequest(self._http_client)
        self.misc = MiscRequest(self._http_client)

    async def close(self):
        """关闭客户端"""
        await self._http_client.close()

    async def __aenter__(self):
        return self

    async def __aexit__(self, exc_type, exc_val, exc_tb):
        await self.close()


class ZsxqClientBuilder:
    """ZsxqClient 构建器

    使用示例:
        client = ZsxqClientBuilder() \\
            .set_token("your-token") \\
            .set_timeout(10) \\
            .set_retry(3) \\
            .build()
    """

    def __init__(self):
        self._token: Optional[str] = None
        self._base_url: str = "https://api.zsxq.com"
        self._timeout: float = 10.0
        self._retry_count: int = 3
        self._retry_delay: float = 1.0
        self._device_id: str = str(uuid.uuid4())
        self._app_version: str = "2.83.0"

    def set_token(self, token: str) -> "ZsxqClientBuilder":
        """设置认证 Token（必需）"""
        self._token = token
        return self

    def set_base_url(self, base_url: str) -> "ZsxqClientBuilder":
        """设置 API 基础 URL"""
        self._base_url = base_url
        return self

    def set_timeout(self, timeout: float) -> "ZsxqClientBuilder":
        """设置请求超时（秒）"""
        self._timeout = timeout
        return self

    def set_retry_count(self, count: int) -> "ZsxqClientBuilder":
        """设置重试次数"""
        self._retry_count = count
        return self

    def set_retry_delay(self, delay: float) -> "ZsxqClientBuilder":
        """设置重试间隔（秒）"""
        self._retry_delay = delay
        return self

    def set_retry(self, count: int, delay: Optional[float] = None) -> "ZsxqClientBuilder":
        """设置重试配置"""
        self._retry_count = count
        if delay is not None:
            self._retry_delay = delay
        return self

    def set_device_id(self, device_id: str) -> "ZsxqClientBuilder":
        """设置设备 ID"""
        self._device_id = device_id
        return self

    def set_app_version(self, version: str) -> "ZsxqClientBuilder":
        """设置 App 版本号"""
        self._app_version = version
        return self

    def build(self) -> ZsxqClient:
        """构建 ZsxqClient 实例"""
        if not self._token:
            raise ValueError("Token is required. Use set_token() to set it.")

        config = ZsxqConfig(
            token=self._token,
            base_url=self._base_url,
            timeout=self._timeout,
            retry_count=self._retry_count,
            retry_delay=self._retry_delay,
            device_id=self._device_id,
            app_version=self._app_version,
        )

        return ZsxqClient(config)
