# SDK 异常层次结构

## 概述

所有语言的 SDK 必须实现一致的异常层次结构，以确保跨语言的错误处理体验一致。

## 异常层次图

```
ZsxqException (基类)
│
├── AuthException (认证异常)
│   ├── TokenInvalidException      # 10001: Token 无效
│   ├── TokenExpiredException      # 10002: Token 过期
│   └── SignatureInvalidException  # 10003: 签名验证失败
│
├── PermissionException (权限异常)
│   ├── NotMemberException         # 20002: 非星球成员
│   └── NotOwnerException          # 20003: 非星主
│
├── ResourceNotFoundException (资源不存在)
│   ├── GroupNotFoundException     # 30001: 星球不存在
│   ├── TopicNotFoundException     # 30002: 话题不存在
│   ├── UserNotFoundException      # 30003: 用户不存在
│   └── CheckinNotFoundException   # 30004: 打卡项目不存在
│
├── RateLimitException (限流异常)  # 40001: 请求过于频繁
│   └── DailyLimitException        # 40002: 每日限制
│
├── BusinessException (业务异常)
│   ├── AlreadyMemberException     # 50001: 已是成员
│   ├── NotJoinedCheckinException  # 52010: 未参与打卡
│   └── AlreadyCheckedInException  # 52011: 今日已打卡
│
├── ValidationException (参数异常)
│   ├── InvalidParameterException  # 60001: 参数错误
│   └── MissingParameterException  # 60002: 缺少参数
│
└── NetworkException (网络异常)
    ├── TimeoutException           # 70002: 请求超时
    └── ServerErrorException       # 70003: 服务器错误
```

## 基类定义

### TypeScript

```typescript
export class ZsxqException extends Error {
  readonly code: number;
  readonly requestId?: string;

  constructor(code: number, message: string, requestId?: string) {
    super(message);
    this.name = 'ZsxqException';
    this.code = code;
    this.requestId = requestId;
  }
}
```

### Java

```java
public class ZsxqException extends RuntimeException {
    private final int code;
    private final String requestId;

    public ZsxqException(int code, String message, String requestId) {
        super(message);
        this.code = code;
        this.requestId = requestId;
    }

    public int getCode() { return code; }
    public String getRequestId() { return requestId; }
}
```

### Python

```python
class ZsxqException(Exception):
    def __init__(self, code: int, message: str, request_id: str = None):
        super().__init__(message)
        self.code = code
        self.request_id = request_id
```

### Go

```go
type ZsxqError struct {
    Code      int
    Message   string
    RequestID string
}

func (e *ZsxqError) Error() string {
    return fmt.Sprintf("[%d] %s", e.Code, e.Message)
}
```

## 异常属性

| 属性 | 类型 | 必需 | 说明 |
|------|------|------|------|
| `code` | int | 是 | 错误码 |
| `message` | string | 是 | 错误描述 |
| `requestId` | string | 否 | 请求追踪 ID |
| `cause` | Exception | 否 | 原始异常（网络错误时） |

## 特殊异常属性

### RateLimitException

```typescript
export class RateLimitException extends ZsxqException {
  readonly retryAfter?: number; // 建议重试等待时间（秒）
}
```

### NetworkException

```typescript
export class NetworkException extends ZsxqException {
  readonly cause?: Error; // 原始网络错误
}
```

## 错误码映射

SDK 内部根据 API 返回的 `code` 字段创建对应异常：

```typescript
function createException(code: number, message: string, requestId?: string): ZsxqException {
  switch (code) {
    case 10001:
      return new TokenInvalidException(code, message, requestId);
    case 10002:
      return new TokenExpiredException(code, message, requestId);
    case 20002:
      return new NotMemberException(code, message, requestId);
    case 30001:
      return new GroupNotFoundException(code, message, requestId);
    case 40001:
      return new RateLimitException(code, message, requestId);
    case 52010:
      return new NotJoinedCheckinException(code, message, requestId);
    // ... 其他映射
    default:
      return new ZsxqException(code, message, requestId);
  }
}
```

## 使用示例

### 精确捕获

```typescript
try {
  await client.groups.list();
} catch (error) {
  if (error instanceof TokenExpiredException) {
    // Token 过期，刷新 Token
    await refreshToken();
    await client.groups.list(); // 重试
  } else if (error instanceof NotMemberException) {
    // 非成员，提示加入
    showJoinPrompt();
  } else if (error instanceof RateLimitException) {
    // 限流，等待后重试
    await sleep(error.retryAfter || 60);
  }
}
```

### 分类捕获

```typescript
try {
  await client.groups.list();
} catch (error) {
  if (error instanceof AuthException) {
    // 所有认证相关错误
    redirectToLogin();
  } else if (error instanceof ResourceNotFoundException) {
    // 所有资源不存在错误
    show404Page();
  } else if (error instanceof NetworkException) {
    // 所有网络错误
    showNetworkError();
  }
}
```

## 最佳实践

1. **优先捕获具体异常** - 先处理最具体的异常类型
2. **提供回退处理** - 最后捕��� `ZsxqException` 处理未知错误
3. **记录 requestId** - 用于问题追踪和调试
4. **适当重试** - 对 `NetworkException` 和 `RateLimitException` 可以重试
