/**
 * 知识星球 SDK 基础异常类
 */
export class ZsxqException extends Error {
  readonly code: number;
  readonly requestId?: string;

  constructor(code: number, message: string, requestId?: string) {
    super(message);
    this.name = 'ZsxqException';
    this.code = code;
    this.requestId = requestId;
    Object.setPrototypeOf(this, ZsxqException.prototype);
  }
}

/**
 * 认证异常基类
 */
export class AuthException extends ZsxqException {
  constructor(code: number, message: string, requestId?: string) {
    super(code, message, requestId);
    this.name = 'AuthException';
    Object.setPrototypeOf(this, AuthException.prototype);
  }
}

/**
 * Token 无效异常 (10001)
 */
export class TokenInvalidException extends AuthException {
  constructor(message = 'Token 无效', requestId?: string) {
    super(10001, message, requestId);
    this.name = 'TokenInvalidException';
    Object.setPrototypeOf(this, TokenInvalidException.prototype);
  }
}

/**
 * Token 过期异常 (10002)
 */
export class TokenExpiredException extends AuthException {
  constructor(message = 'Token 已过期', requestId?: string) {
    super(10002, message, requestId);
    this.name = 'TokenExpiredException';
    Object.setPrototypeOf(this, TokenExpiredException.prototype);
  }
}

/**
 * 签名验证失败异常 (10003)
 */
export class SignatureInvalidException extends AuthException {
  constructor(message = '签名验证失败', requestId?: string) {
    super(10003, message, requestId);
    this.name = 'SignatureInvalidException';
    Object.setPrototypeOf(this, SignatureInvalidException.prototype);
  }
}

/**
 * 权限异常
 */
export class PermissionException extends ZsxqException {
  constructor(code: number, message: string, requestId?: string) {
    super(code, message, requestId);
    this.name = 'PermissionException';
    Object.setPrototypeOf(this, PermissionException.prototype);
  }
}

/**
 * 非成员异常 (20002)
 */
export class NotMemberException extends PermissionException {
  constructor(message = '非星球成员', requestId?: string) {
    super(20002, message, requestId);
    this.name = 'NotMemberException';
    Object.setPrototypeOf(this, NotMemberException.prototype);
  }
}

/**
 * 非星主异常 (20003)
 */
export class NotOwnerException extends PermissionException {
  constructor(message = '非星主', requestId?: string) {
    super(20003, message, requestId);
    this.name = 'NotOwnerException';
    Object.setPrototypeOf(this, NotOwnerException.prototype);
  }
}

/**
 * 资源不存在异常基类
 */
export class ResourceNotFoundException extends ZsxqException {
  constructor(code: number, message: string, requestId?: string) {
    super(code, message, requestId);
    this.name = 'ResourceNotFoundException';
    Object.setPrototypeOf(this, ResourceNotFoundException.prototype);
  }
}

/**
 * 星球不存在异常 (30001)
 */
export class GroupNotFoundException extends ResourceNotFoundException {
  constructor(message = '星球不存在', requestId?: string) {
    super(30001, message, requestId);
    this.name = 'GroupNotFoundException';
    Object.setPrototypeOf(this, GroupNotFoundException.prototype);
  }
}

/**
 * 话题不存在异常 (30002)
 */
export class TopicNotFoundException extends ResourceNotFoundException {
  constructor(message = '话题不存在', requestId?: string) {
    super(30002, message, requestId);
    this.name = 'TopicNotFoundException';
    Object.setPrototypeOf(this, TopicNotFoundException.prototype);
  }
}

/**
 * 限流异常 (40001)
 */
export class RateLimitException extends ZsxqException {
  readonly retryAfter?: number;

  constructor(message = '请求过于频繁', requestId?: string, retryAfter?: number) {
    super(40001, message, requestId);
    this.name = 'RateLimitException';
    this.retryAfter = retryAfter;
    Object.setPrototypeOf(this, RateLimitException.prototype);
  }
}

/**
 * 业务异常基类
 */
export class BusinessException extends ZsxqException {
  constructor(code: number, message: string, requestId?: string) {
    super(code, message, requestId);
    this.name = 'BusinessException';
    Object.setPrototypeOf(this, BusinessException.prototype);
  }
}

/**
 * 未参与打卡异常 (52010)
 */
export class NotJoinedCheckinException extends BusinessException {
  constructor(message = '未参与打卡项目', requestId?: string) {
    super(52010, message, requestId);
    this.name = 'NotJoinedCheckinException';
    Object.setPrototypeOf(this, NotJoinedCheckinException.prototype);
  }
}

/**
 * 网络异常
 */
export class NetworkException extends ZsxqException {
  readonly cause?: Error;

  constructor(message: string, cause?: Error, requestId?: string) {
    super(70001, message, requestId);
    this.name = 'NetworkException';
    this.cause = cause;
    Object.setPrototypeOf(this, NetworkException.prototype);
  }
}

/**
 * 超时异常 (70002)
 */
export class TimeoutException extends NetworkException {
  constructor(message = '请求超时', cause?: Error, requestId?: string) {
    super(message, cause, requestId);
    this.name = 'TimeoutException';
    Object.setPrototypeOf(this, TimeoutException.prototype);
  }
}

/**
 * 根据错误码创建对应异常
 */
export function createException(
  code: number,
  message: string,
  requestId?: string,
): ZsxqException {
  switch (code) {
    case 10001:
      return new TokenInvalidException(message, requestId);
    case 10002:
      return new TokenExpiredException(message, requestId);
    case 10003:
      return new SignatureInvalidException(message, requestId);
    case 20001:
      return new PermissionException(code, message, requestId);
    case 20002:
      return new NotMemberException(message, requestId);
    case 20003:
      return new NotOwnerException(message, requestId);
    case 30001:
      return new GroupNotFoundException(message, requestId);
    case 30002:
      return new TopicNotFoundException(message, requestId);
    case 40001:
      return new RateLimitException(message, requestId);
    case 52010:
      return new NotJoinedCheckinException(message, requestId);
    default:
      return new ZsxqException(code, message, requestId);
  }
}
