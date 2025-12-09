"""异常类定义"""

from typing import Optional


class ZsxqException(Exception):
    """知识星球 SDK 基础异常类"""

    def __init__(self, code: int, message: str, request_id: Optional[str] = None):
        self.code = code
        self.message = message
        self.request_id = request_id
        super().__init__(self._format_message())

    def _format_message(self) -> str:
        if self.request_id:
            return f"[{self.code}] {self.message} (request_id: {self.request_id})"
        return f"[{self.code}] {self.message}"


class AuthException(ZsxqException):
    """认证异常基类"""
    pass


class TokenInvalidException(AuthException):
    """Token 无效异常 (10001)"""

    def __init__(self, message: str = "Token 无效", request_id: Optional[str] = None):
        super().__init__(10001, message, request_id)


class TokenExpiredException(AuthException):
    """Token 过期异常 (10002)"""

    def __init__(self, message: str = "Token 已过期", request_id: Optional[str] = None):
        super().__init__(10002, message, request_id)


class SignatureInvalidException(AuthException):
    """签名验证失败异常 (10003)"""

    def __init__(self, message: str = "签名验证失败", request_id: Optional[str] = None):
        super().__init__(10003, message, request_id)


class PermissionException(ZsxqException):
    """权限异常"""
    pass


class NotMemberException(PermissionException):
    """非成员异常 (20002)"""

    def __init__(self, message: str = "非星球成员", request_id: Optional[str] = None):
        super().__init__(20002, message, request_id)


class NotOwnerException(PermissionException):
    """非星主异常 (20003)"""

    def __init__(self, message: str = "非星主", request_id: Optional[str] = None):
        super().__init__(20003, message, request_id)


class ResourceNotFoundException(ZsxqException):
    """资源不存在异常基类"""
    pass


class GroupNotFoundException(ResourceNotFoundException):
    """星球不存在异常 (30001)"""

    def __init__(self, message: str = "星球不存在", request_id: Optional[str] = None):
        super().__init__(30001, message, request_id)


class TopicNotFoundException(ResourceNotFoundException):
    """话题不存在异常 (30002)"""

    def __init__(self, message: str = "话题不存在", request_id: Optional[str] = None):
        super().__init__(30002, message, request_id)


class UserNotFoundException(ResourceNotFoundException):
    """用户不存在异常 (30003)"""

    def __init__(self, message: str = "用户不存在", request_id: Optional[str] = None):
        super().__init__(30003, message, request_id)


class CheckinNotFoundException(ResourceNotFoundException):
    """打卡项目不存在异常 (30004)"""

    def __init__(self, message: str = "打卡项目不存在", request_id: Optional[str] = None):
        super().__init__(30004, message, request_id)


class RateLimitException(ZsxqException):
    """限流异常 (40001)"""

    def __init__(
        self,
        message: str = "请求过于频繁",
        request_id: Optional[str] = None,
        retry_after: Optional[int] = None
    ):
        super().__init__(40001, message, request_id)
        self.retry_after = retry_after


class BusinessException(ZsxqException):
    """业务异常基类"""
    pass


class AlreadyMemberException(BusinessException):
    """已是成员异常 (50001)"""

    def __init__(self, message: str = "已是星球成员", request_id: Optional[str] = None):
        super().__init__(50001, message, request_id)


class NotJoinedCheckinException(BusinessException):
    """未参与打卡异常 (52010)"""

    def __init__(self, message: str = "未参与打卡项目", request_id: Optional[str] = None):
        super().__init__(52010, message, request_id)


class AlreadyCheckedInException(BusinessException):
    """已打卡异常 (52011)"""

    def __init__(self, message: str = "今日已打卡", request_id: Optional[str] = None):
        super().__init__(52011, message, request_id)


class CheckinClosedException(BusinessException):
    """打卡已关闭异常 (52012)"""

    def __init__(self, message: str = "打卡项目已关闭", request_id: Optional[str] = None):
        super().__init__(52012, message, request_id)


class ValidationException(ZsxqException):
    """参数验证异常"""
    pass


class InvalidParameterException(ValidationException):
    """参数错误异常 (60001)"""

    def __init__(self, message: str = "参数错误", request_id: Optional[str] = None):
        super().__init__(60001, message, request_id)


class MissingParameterException(ValidationException):
    """缺少参数异常 (60002)"""

    def __init__(self, message: str = "缺少必要参数", request_id: Optional[str] = None):
        super().__init__(60002, message, request_id)


class NetworkException(ZsxqException):
    """网络异常"""

    def __init__(
        self,
        message: str,
        request_id: Optional[str] = None,
        cause: Optional[Exception] = None
    ):
        super().__init__(70001, message, request_id)
        self.__cause__ = cause


class TimeoutException(NetworkException):
    """超时异常 (70002)"""

    def __init__(
        self,
        message: str = "请求超时",
        request_id: Optional[str] = None,
        cause: Optional[Exception] = None
    ):
        super().__init__(message, request_id, cause)
        self.code = 70002


class ServerErrorException(NetworkException):
    """服务器错误异常 (70003)"""

    def __init__(
        self,
        message: str = "服务器错误",
        request_id: Optional[str] = None,
        cause: Optional[Exception] = None
    ):
        super().__init__(message, request_id, cause)
        self.code = 70003


def create_exception(code: int, message: str, request_id: Optional[str] = None) -> ZsxqException:
    """根据错误码创建对应异常"""
    exception_map = {
        10001: TokenInvalidException,
        10002: TokenExpiredException,
        10003: SignatureInvalidException,
        20002: NotMemberException,
        20003: NotOwnerException,
        30001: GroupNotFoundException,
        30002: TopicNotFoundException,
        30003: UserNotFoundException,
        30004: CheckinNotFoundException,
        40001: RateLimitException,
        50001: AlreadyMemberException,
        52010: NotJoinedCheckinException,
        52011: AlreadyCheckedInException,
        52012: CheckinClosedException,
        60001: InvalidParameterException,
        60002: MissingParameterException,
    }

    exception_class = exception_map.get(code)
    if exception_class:
        return exception_class(message, request_id)

    # 按错误码范围判断
    if 10000 <= code < 20000:
        return AuthException(code, message, request_id)
    elif 20000 <= code < 30000:
        return PermissionException(code, message, request_id)
    elif 30000 <= code < 40000:
        return ResourceNotFoundException(code, message, request_id)
    elif 40000 <= code < 50000:
        return RateLimitException(message, request_id)
    elif 50000 <= code < 60000:
        return BusinessException(code, message, request_id)
    elif 60000 <= code < 70000:
        return ValidationException(code, message, request_id)
    elif 70000 <= code < 80000:
        return NetworkException(message, request_id)

    return ZsxqException(code, message, request_id)
