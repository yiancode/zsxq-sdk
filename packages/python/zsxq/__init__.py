"""
知识星球 Python SDK

使用示例:
    from zsxq import ZsxqClientBuilder

    client = ZsxqClientBuilder() \\
        .set_token("your-token") \\
        .set_timeout(10) \\
        .build()

    # 获取星球列表
    groups = await client.groups.list()

    # 获取话题
    topics = await client.topics.list(group_id, count=20)

    # 获取当前用户
    user = await client.users.self()
"""

from zsxq.client import ZsxqClient, ZsxqClientBuilder, ZsxqConfig
from zsxq.model import (
    User,
    Group,
    Topic,
    TalkContent,
    TaskContent,
    QuestionContent,
    SolutionContent,
    Image,
    ImageSize,
    FileAttachment,
    Article,
    Checkin,
    CheckinStatistics,
    RankingItem,
    Comment,
    Hashtag,
)
from zsxq.exception import (
    ZsxqException,
    AuthException,
    TokenInvalidException,
    TokenExpiredException,
    SignatureInvalidException,
    PermissionException,
    NotMemberException,
    NotOwnerException,
    ResourceNotFoundException,
    GroupNotFoundException,
    TopicNotFoundException,
    RateLimitException,
    BusinessException,
    NotJoinedCheckinException,
    NetworkException,
    TimeoutException,
)

__version__ = "1.0.0"
__all__ = [
    # Client
    "ZsxqClient",
    "ZsxqClientBuilder",
    "ZsxqConfig",
    # Models
    "User",
    "Group",
    "Topic",
    "TalkContent",
    "TaskContent",
    "QuestionContent",
    "SolutionContent",
    "Image",
    "ImageSize",
    "FileAttachment",
    "Article",
    "Checkin",
    "CheckinStatistics",
    "RankingItem",
    "Comment",
    "Hashtag",
    # Exceptions
    "ZsxqException",
    "AuthException",
    "TokenInvalidException",
    "TokenExpiredException",
    "SignatureInvalidException",
    "PermissionException",
    "NotMemberException",
    "NotOwnerException",
    "ResourceNotFoundException",
    "GroupNotFoundException",
    "TopicNotFoundException",
    "RateLimitException",
    "BusinessException",
    "NotJoinedCheckinException",
    "NetworkException",
    "TimeoutException",
]
