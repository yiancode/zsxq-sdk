package exception

import "fmt"

// ZsxqError 知识星球 SDK 基础异常
type ZsxqError struct {
	Code      int
	Message   string
	RequestID string
}

func (e *ZsxqError) Error() string {
	if e.RequestID != "" {
		return fmt.Sprintf("[%d] %s (request_id: %s)", e.Code, e.Message, e.RequestID)
	}
	return fmt.Sprintf("[%d] %s", e.Code, e.Message)
}

// NewZsxqError 创建基础异常
func NewZsxqError(code int, message, requestID string) *ZsxqError {
	return &ZsxqError{Code: code, Message: message, RequestID: requestID}
}

// AuthError 认证异常
type AuthError struct {
	*ZsxqError
}

// NewAuthError 创建认证异常
func NewAuthError(code int, message, requestID string) *AuthError {
	return &AuthError{ZsxqError: NewZsxqError(code, message, requestID)}
}

// TokenInvalidError Token 无效异常 (10001)
type TokenInvalidError struct {
	*AuthError
}

// NewTokenInvalidError 创建 Token 无效异常
func NewTokenInvalidError(message, requestID string) *TokenInvalidError {
	if message == "" {
		message = "Token 无效"
	}
	return &TokenInvalidError{AuthError: NewAuthError(10001, message, requestID)}
}

// TokenExpiredError Token 过期异常 (10002)
type TokenExpiredError struct {
	*AuthError
}

// NewTokenExpiredError 创建 Token 过期异常
func NewTokenExpiredError(message, requestID string) *TokenExpiredError {
	if message == "" {
		message = "Token 已过期"
	}
	return &TokenExpiredError{AuthError: NewAuthError(10002, message, requestID)}
}

// SignatureInvalidError 签名验证失败异常 (10003)
type SignatureInvalidError struct {
	*AuthError
}

// NewSignatureInvalidError 创建签名验证失败异常
func NewSignatureInvalidError(message, requestID string) *SignatureInvalidError {
	if message == "" {
		message = "签名验证失败"
	}
	return &SignatureInvalidError{AuthError: NewAuthError(10003, message, requestID)}
}

// PermissionError 权限异常
type PermissionError struct {
	*ZsxqError
}

// NewPermissionError 创建权限异常
func NewPermissionError(code int, message, requestID string) *PermissionError {
	return &PermissionError{ZsxqError: NewZsxqError(code, message, requestID)}
}

// NotMemberError 非成员异常 (20002)
type NotMemberError struct {
	*PermissionError
}

// NewNotMemberError 创建非成员异常
func NewNotMemberError(message, requestID string) *NotMemberError {
	if message == "" {
		message = "非星球成员"
	}
	return &NotMemberError{PermissionError: NewPermissionError(20002, message, requestID)}
}

// NotOwnerError 非星主异常 (20003)
type NotOwnerError struct {
	*PermissionError
}

// NewNotOwnerError 创建非星主异常
func NewNotOwnerError(message, requestID string) *NotOwnerError {
	if message == "" {
		message = "非星主"
	}
	return &NotOwnerError{PermissionError: NewPermissionError(20003, message, requestID)}
}

// ResourceNotFoundError 资源不存在异常
type ResourceNotFoundError struct {
	*ZsxqError
}

// NewResourceNotFoundError 创建资源不存在异常
func NewResourceNotFoundError(code int, message, requestID string) *ResourceNotFoundError {
	return &ResourceNotFoundError{ZsxqError: NewZsxqError(code, message, requestID)}
}

// GroupNotFoundError 星球不存在异常 (30001)
type GroupNotFoundError struct {
	*ResourceNotFoundError
}

// NewGroupNotFoundError 创建星球不存在异常
func NewGroupNotFoundError(message, requestID string) *GroupNotFoundError {
	if message == "" {
		message = "星球不存在"
	}
	return &GroupNotFoundError{ResourceNotFoundError: NewResourceNotFoundError(30001, message, requestID)}
}

// TopicNotFoundError 话题不存在异常 (30002)
type TopicNotFoundError struct {
	*ResourceNotFoundError
}

// NewTopicNotFoundError 创建话题不存在异常
func NewTopicNotFoundError(message, requestID string) *TopicNotFoundError {
	if message == "" {
		message = "话题不存在"
	}
	return &TopicNotFoundError{ResourceNotFoundError: NewResourceNotFoundError(30002, message, requestID)}
}

// RateLimitError 限流异常 (40001)
type RateLimitError struct {
	*ZsxqError
	RetryAfter int
}

// NewRateLimitError 创建限流异常
func NewRateLimitError(message, requestID string, retryAfter int) *RateLimitError {
	if message == "" {
		message = "请求过于频繁"
	}
	return &RateLimitError{
		ZsxqError:  NewZsxqError(40001, message, requestID),
		RetryAfter: retryAfter,
	}
}

// BusinessError 业务异常
type BusinessError struct {
	*ZsxqError
}

// NewBusinessError 创建业务异常
func NewBusinessError(code int, message, requestID string) *BusinessError {
	return &BusinessError{ZsxqError: NewZsxqError(code, message, requestID)}
}

// NotJoinedCheckinError 未参与打卡异常 (52010)
type NotJoinedCheckinError struct {
	*BusinessError
}

// NewNotJoinedCheckinError 创建未参与打卡异常
func NewNotJoinedCheckinError(message, requestID string) *NotJoinedCheckinError {
	if message == "" {
		message = "未参与打卡项目"
	}
	return &NotJoinedCheckinError{BusinessError: NewBusinessError(52010, message, requestID)}
}

// NetworkError 网络异常
type NetworkError struct {
	*ZsxqError
	Cause error
}

// NewNetworkError 创建网络异常
func NewNetworkError(message, requestID string, cause error) *NetworkError {
	return &NetworkError{
		ZsxqError: NewZsxqError(70001, message, requestID),
		Cause:     cause,
	}
}

func (e *NetworkError) Unwrap() error {
	return e.Cause
}

// TimeoutError 超时异常 (70002)
type TimeoutError struct {
	*NetworkError
}

// NewTimeoutError 创建超时异常
func NewTimeoutError(message, requestID string, cause error) *TimeoutError {
	if message == "" {
		message = "请求超时"
	}
	return &TimeoutError{
		NetworkError: &NetworkError{
			ZsxqError: NewZsxqError(70002, message, requestID),
			Cause:     cause,
		},
	}
}

// CreateError 根据错误码创建对应异常
func CreateError(code int, message, requestID string) error {
	switch code {
	case 10001:
		return NewTokenInvalidError(message, requestID)
	case 10002:
		return NewTokenExpiredError(message, requestID)
	case 10003:
		return NewSignatureInvalidError(message, requestID)
	case 20001:
		return NewPermissionError(code, message, requestID)
	case 20002:
		return NewNotMemberError(message, requestID)
	case 20003:
		return NewNotOwnerError(message, requestID)
	case 30001:
		return NewGroupNotFoundError(message, requestID)
	case 30002:
		return NewTopicNotFoundError(message, requestID)
	case 40001:
		return NewRateLimitError(message, requestID, 0)
	case 52010:
		return NewNotJoinedCheckinError(message, requestID)
	default:
		return NewZsxqError(code, message, requestID)
	}
}
