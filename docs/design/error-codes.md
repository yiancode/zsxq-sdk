# 错误码定义

> 本文档是 SDK 错误码的唯一权威来源（SSOT）

## 错误码结构

SDK 使用统一的错误响应格式：

```typescript
interface ZsxqError {
  code: number;       // 错误码
  message: string;    // 错误消息
  info?: string;      // 附加信息
}
```

---

## 知识星球原生错误码

### 认证相关 (1xxxx)

| 错误码 | 常量 | 说明 | 处理建议 |
|-------|------|------|---------|
| 10001 | `AUTH_TOKEN_INVALID` | Token 无效 | 重新获取 Token |
| 10002 | `AUTH_TOKEN_EXPIRED` | Token 已过期 | 刷新或重新登录 |
| 10003 | `AUTH_SIGNATURE_INVALID` | 签名验证失败 | 检查签名算法 |
| 10004 | `AUTH_DEVICE_INVALID` | 设备标识无效 | 重新生成设备 ID |

### 权限相关 (2xxxx)

| 错误码 | 常量 | 说明 | 处理建议 |
|-------|------|------|---------|
| 20001 | `PERMISSION_DENIED` | 权限不足 | 检查用户角色 |
| 20002 | `NOT_MEMBER` | 非星球成员 | 需要加入星球 |
| 20003 | `MEMBER_EXPIRED` | 成员已过期 | 需要续费 |
| 20004 | `NOT_OWNER` | 非星主 | 需要星主权限 |

### 资源相关 (3xxxx)

| 错误码 | 常量 | 说明 | 处理建议 |
|-------|------|------|---------|
| 30001 | `GROUP_NOT_FOUND` | 星球不存在 | 检查星球 ID |
| 30002 | `TOPIC_NOT_FOUND` | 话题不存在 | 检查话题 ID |
| 30003 | `USER_NOT_FOUND` | 用户不存在 | 检查用户 ID |
| 30004 | `CHECKIN_NOT_FOUND` | 打卡项目不存在 | 检查打卡 ID |

### 业务相关 (5xxxx)

| 错误码 | 常量 | 说明 | 处理建议 |
|-------|------|------|---------|
| 52010 | `NOT_JOINED_CHECKIN` | 未报名参加打卡 | 需要先报名 |
| 52011 | `CHECKIN_CLOSED` | 打卡已关闭 | 无法继续打卡 |
| 52012 | `ALREADY_CHECKINED` | 今日已打卡 | 无需重复打卡 |

### 限流相关 (4xxxx)

| 错误码 | 常量 | 说明 | 处理建议 |
|-------|------|------|---------|
| 40001 | `RATE_LIMIT_EXCEEDED` | 请求频率超限 | 等待后重试 |
| 40002 | `TOO_MANY_REQUESTS` | 请求过于频繁 | 降低请求频率 |

---

## SDK 自定义错误码

### 客户端错误 (6xxxx)

| 错误码 | 常量 | 说明 | 处理建议 |
|-------|------|------|---------|
| 60001 | `CONFIG_INVALID` | 配置无效 | 检查 SDK 配置 |
| 60002 | `TOKEN_MISSING` | Token 未设置 | 设置认证 Token |
| 60003 | `PARAM_INVALID` | 参数无效 | 检查方法参数 |
| 60004 | `PARAM_REQUIRED` | 缺少必填参数 | 补充必填参数 |

### 网络错误 (7xxxx)

| 错误码 | 常量 | 说明 | 处理建议 |
|-------|------|------|---------|
| 70001 | `NETWORK_ERROR` | 网络连接失败 | 检查网络连接 |
| 70002 | `TIMEOUT` | 请求超时 | 增加超时时间或重试 |
| 70003 | `SERVER_ERROR` | 服务器错误 | 稍后重试 |
| 70004 | `PARSE_ERROR` | 响应解析失败 | 联系开发者 |

### 重试相关 (8xxxx)

| 错误码 | 常量 | 说明 | 处理建议 |
|-------|------|------|---------|
| 80001 | `RETRY_EXHAUSTED` | 重试次数耗尽 | 检查服务状态 |
| 80002 | `RETRY_ABORTED` | 重试被中断 | 检查请求 |

---

## 异常类层次

```
ZsxqException (基础异常)
    ├── ZsxqAuthException (认证异常)
    │   ├── TokenInvalidException
    │   ├── TokenExpiredException
    │   └── SignatureInvalidException
    ├── ZsxqPermissionException (权限异常)
    │   ├── NotMemberException
    │   └── NotOwnerException
    ├── ZsxqNotFoundExeption (资源不存在)
    │   ├── GroupNotFoundException
    │   ├── TopicNotFoundException
    │   └── CheckinNotFoundException
    ├── ZsxqRateLimitException (限流异常)
    └── ZsxqNetworkException (网络异常)
        ├── TimeoutException
        └── ServerErrorException
```

---

## 使用示例

### 捕获特定异常

```typescript
import {
  ZsxqException,
  ZsxqAuthException,
  TokenExpiredException
} from 'zsxq-sdk';

try {
  const groups = await client.groups.list();
} catch (error) {
  if (error instanceof TokenExpiredException) {
    // Token 过期，刷新 Token
    await refreshToken();
    return retry();
  }

  if (error instanceof ZsxqAuthException) {
    // 其他认证错误
    redirectToLogin();
    return;
  }

  if (error instanceof ZsxqException) {
    // 其他 SDK 错误
    console.error(`错误码: ${error.code}, 消息: ${error.message}`);
  }

  throw error;
}
```

### 错误码判断

```typescript
import { ErrorCode } from 'zsxq-sdk';

try {
  await client.checkins.get(groupId, checkinId);
} catch (error) {
  if (error.code === ErrorCode.NOT_JOINED_CHECKIN) {
    // 未报名，引导用户报名
    showJoinCheckinDialog();
  }
}
```

---

## 错误码枚举

```typescript
// src/enums/error-code.enum.ts

export enum ErrorCode {
  // 认证相关
  AUTH_TOKEN_INVALID = 10001,
  AUTH_TOKEN_EXPIRED = 10002,
  AUTH_SIGNATURE_INVALID = 10003,
  AUTH_DEVICE_INVALID = 10004,

  // 权限相关
  PERMISSION_DENIED = 20001,
  NOT_MEMBER = 20002,
  MEMBER_EXPIRED = 20003,
  NOT_OWNER = 20004,

  // 资源相关
  GROUP_NOT_FOUND = 30001,
  TOPIC_NOT_FOUND = 30002,
  USER_NOT_FOUND = 30003,
  CHECKIN_NOT_FOUND = 30004,

  // 限流相关
  RATE_LIMIT_EXCEEDED = 40001,
  TOO_MANY_REQUESTS = 40002,

  // 业务相关
  NOT_JOINED_CHECKIN = 52010,
  CHECKIN_CLOSED = 52011,
  ALREADY_CHECKINED = 52012,

  // 客户端错误
  CONFIG_INVALID = 60001,
  TOKEN_MISSING = 60002,
  PARAM_INVALID = 60003,
  PARAM_REQUIRED = 60004,

  // 网络错误
  NETWORK_ERROR = 70001,
  TIMEOUT = 70002,
  SERVER_ERROR = 70003,
  PARSE_ERROR = 70004,

  // 重试相关
  RETRY_EXHAUSTED = 80001,
  RETRY_ABORTED = 80002,
}
```

---

## HTTP 状态码映射

| HTTP 状态码 | 对应错误码范围 | 说明 |
|------------|---------------|------|
| 401 | 1xxxx | 认证失败 |
| 403 | 2xxxx | 权限不足 |
| 404 | 3xxxx | 资源不存在 |
| 429 | 4xxxx | 请求限流 |
| 500+ | 7xxxx | 服务器错误 |

---

## 版本说明

- **SDK 版本**: 0.1.0
- **最后更新**: 2025-12-09
