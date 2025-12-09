# 客户端 API

> SDK 客户端核心 API 文档

## 快速开始

```typescript
import { ZsxqClientBuilder } from 'zsxq-sdk';

const client = new ZsxqClientBuilder()
  .setToken('your-token')
  .build();

// 获取星球列表
const groups = await client.groups.list();
```

---

## ZsxqClientBuilder

客户端构建器，使用 Builder 模式配置 SDK。

### 方法

#### `setToken(token: string)`

设置认证 Token（必填）。

```typescript
builder.setToken('D047A423-A...169922C77C');
```

#### `setBaseUrl(url: string)`

设置 API 基础 URL，默认 `https://api.zsxq.com`。

```typescript
builder.setBaseUrl('https://api.zsxq.com');
```

#### `setTimeout(ms: number)`

设置请求超时时间（毫秒），默认 10000。

```typescript
builder.setTimeout(5000);
```

#### `setRetry(count: number)`

设置重试次数，默认 3。

```typescript
builder.setRetry(3);
```

#### `setDeviceId(deviceId: string)`

设置设备唯一标识。

```typescript
builder.setDeviceId('d75d966c-ed30-4fe8-b0f9-f030eb39d9be');
```

#### `setAppVersion(version: string)`

设置 App 版本号，默认 `2.83.0`。

```typescript
builder.setAppVersion('2.83.0');
```

#### `build()`

构建并返回客户端实例。

```typescript
const client = builder.build();
```

---

## ZsxqClient

SDK 主客户端类，提供所有 API 访问入口。

### 属性

| 属性 | 类型 | 说明 |
|-----|------|------|
| `groups` | GroupRequest | 星球相关请求 |
| `topics` | TopicRequest | 话题相关请求 |
| `users` | UserRequest | 用户相关请求 |
| `checkins` | CheckinRequest | 打卡相关请求 |
| `dashboard` | DashboardRequest | Dashboard 请求（星主专用） |

### 使用示例

```typescript
// 星球操作
const groups = await client.groups.list();
const group = await client.groups.get(groupId);

// 话题操作
const topics = await client.topics.list(groupId);
const topic = await client.topics.get(topicId);

// 用户操作
const me = await client.users.getSelf();
const user = await client.users.get(userId);

// 打卡操作
const checkins = await client.checkins.list(groupId);
const stats = await client.checkins.getStatistics(groupId, checkinId);

// Dashboard（星主专用）
const overview = await client.dashboard.getOverview(groupId);
```

---

## ZsxqConfig

客户端配置接口。

```typescript
interface ZsxqConfig {
  // 必填：认证 Token
  token: string;

  // 可选：API 基础 URL
  baseUrl?: string;  // 默认: 'https://api.zsxq.com'

  // 可选：超时时间（毫秒）
  timeout?: number;  // 默认: 10000

  // 可选：重试次数
  retryCount?: number;  // 默认: 3

  // 可选：设备标识
  deviceId?: string;

  // 可选：App 版本
  appVersion?: string;  // 默认: '2.83.0'
}
```

---

## 响应格式

所有 API 响应遵循统一格式：

```typescript
interface ZsxqResponse<T = any> {
  succeeded: boolean;
  code?: number;
  message?: string;
  resp_data: T;
}
```

### 成功响应

```typescript
{
  succeeded: true,
  resp_data: {
    groups: [...],
    // ...
  }
}
```

### 错误响应

```typescript
{
  succeeded: false,
  code: 52010,
  error: "未报名参加该打卡任务",
  resp_data: {}
}
```

---

## 错误处理

详见 [错误码定义](../design/error-codes.md)

```typescript
import { ZsxqException, TokenExpiredException } from 'zsxq-sdk';

try {
  const groups = await client.groups.list();
} catch (error) {
  if (error instanceof TokenExpiredException) {
    // 处理 Token 过期
  } else if (error instanceof ZsxqException) {
    console.error(`错误码: ${error.code}`);
  }
}
```

---

## 相关文档

- [架构设计](../design/architecture.md)
- [API 映射](../design/api-mapping.md)
- [错误码定义](../design/error-codes.md)
- [快速开始](../guides/quick-start.md)
