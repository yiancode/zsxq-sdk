# SDK 统一架构设计

## 概述

所有语言的 SDK 实现必须遵循本文档定义的架构设计，以确保跨语言的一致性和可预测性。

## 核心组件

```
┌─────────────────────────────────────────────────────────┐
│                     ZsxqClient                          │
│  (门面类，聚合所有 Request 模块)                         │
├─────────────────────────────────────────────────────────┤
│  groups: GroupsRequest                                  │
│  topics: TopicsRequest                                  │
│  users: UsersRequest                                    │
│  checkins: CheckinsRequest                              │
│  dashboard: DashboardRequest                            │
└─────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────┐
│                    HttpClient                           │
│  (底层 HTTP 客户端，处理请求、签名、重试)                 │
└─────────────────────────────────────────────────────────┘
```

## 1. Builder 模式

所有语言必须使用 Builder 模式初始化客户端。

### 必需配置

| 配置项 | 类型 | 说明 |
|--------|------|------|
| `token` | string | 用户认证 Token（必需） |

### 可选配置

| 配置项 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `baseUrl` | string | `https://api.zsxq.com` | API 基础 URL |
| `timeout` | number | `10000` | 请求超时（毫秒） |
| `retryCount` | number | `3` | 重试次数 |
| `retryDelay` | number | `1000` | 重试间隔（毫秒） |
| `deviceId` | string | 自动生成 | 设备 ID |
| `appVersion` | string | `2.83.0` | App 版本号 |

### 各语言示例

**TypeScript**:
```typescript
const client = new ZsxqClientBuilder()
  .setToken('your-token')
  .setTimeout(10000)
  .setRetry(3)
  .build();
```

**Java**:
```java
ZsxqClient client = new ZsxqClientBuilder()
    .token("your-token")
    .timeout(10000)
    .retry(3)
    .build();
```

**Python**:
```python
client = (ZsxqClientBuilder()
    .token("your-token")
    .timeout(10000)
    .retry(3)
    .build())
```

**Go**:
```go
client, err := zsxq.NewClient("your-token",
    zsxq.WithTimeout(10*time.Second),
    zsxq.WithRetry(3),
)
```

## 2. Request 模块设计

### 模块划分

| 模块 | 职责 | 主要方法 |
|------|------|----------|
| `GroupsRequest` | 星球管理 | list, get, getStatistics, getMembers |
| `TopicsRequest` | 话题管理 | list, get, getComments |
| `UsersRequest` | 用户管理 | self, get, getStatistics |
| `CheckinsRequest` | 打卡管理 | list, get, getStatistics, getRankingList |
| `DashboardRequest` | 数据面板 | getOverview, getIncomes |

### 基类设计

所有 Request 模块继承自 `BaseRequest`：

```typescript
abstract class BaseRequest {
  protected httpClient: HttpClient;

  constructor(httpClient: HttpClient) {
    this.httpClient = httpClient;
  }

  protected async get<T>(path: string, params?: object): Promise<T>;
  protected async post<T>(path: string, data?: object): Promise<T>;
  protected async put<T>(path: string, data?: object): Promise<T>;
  protected async delete<T>(path: string): Promise<T>;
}
```

## 3. 方法命名规范

### 命名约定

| 操作类型 | 前缀 | 示例 |
|----------|------|------|
| 获取单个 | `get` | `getGroup(groupId)` |
| 获取列表 | `list` | `listGroups()` |
| 创建 | `create` | `createTopic(data)` |
| 更新 | `update` | `updateTopic(topicId, data)` |
| 删除 | `delete` | `deleteTopic(topicId)` |

### 参数命名

- 使用 camelCase（驼峰命名）
- ID 参数使用 `xxxId` 格式：`groupId`, `topicId`, `userId`
- 查询参数使用 Options 对象：`ListTopicsOptions`

## 4. 返回值设计

### 统一响应处理

SDK 内部处理原始响应，只返回业务数据：

```typescript
// 原始 API 响应
{
  "succeeded": true,
  "resp_data": {
    "groups": [...]
  }
}

// SDK 返回值
const groups: Group[] = await client.groups.list();
// 直接返回 groups 数组，不含外层包装
```

### 分页处理

对于分页 API，提供两种调用方式：

```typescript
// 方式 1：获取单页
const page = await client.topics.list(groupId, { count: 20 });

// 方式 2：迭代器（可选实现）
for await (const topic of client.topics.iterate(groupId)) {
  console.log(topic);
}
```

## 5. 异常处理

### 异常抛出时机

- API 返回 `succeeded: false` 时抛出对应业务异常
- 网络错误时抛出 `NetworkException`
- 超时时抛出 `TimeoutException`

### 异常信息

所有异常必须包含：

| 属性 | 类型 | 说明 |
|------|------|------|
| `code` | number | 错误码 |
| `message` | string | 错误信息 |
| `requestId` | string | 请求 ID（用于追踪） |

### 使用示例

```typescript
try {
  const groups = await client.groups.list();
} catch (error) {
  if (error instanceof TokenExpiredException) {
    // 处理 Token 过期
    await refreshToken();
  } else if (error instanceof RateLimitException) {
    // 处理限流
    await sleep(error.retryAfter);
  } else if (error instanceof ZsxqException) {
    // 处理其他 SDK 异常
    console.error(`Error ${error.code}: ${error.message}`);
  }
}
```

## 6. HTTP 客户端设计

### 职责

- 请求签名（x-signature）
- 请求头管理
- 重试逻辑
- 超时处理
- 响应解析

### 签名算法

```typescript
function sign(timestamp: string, method: string, path: string, body?: string): string {
  const signData = body
    ? `${timestamp}\n${method}\n${path}\n${body}`
    : `${timestamp}\n${method}\n${path}`;

  return crypto
    .createHmac('sha1', SECRET_KEY)
    .update(signData)
    .digest('hex');
}
```

### 重试策略

- 仅对网络错误和 5xx 错误重试
- 使用指数退避：`delay * 2^attempt`
- 最大重试次数由配置决定

## 7. 日志规范

### 日志级别

| 级别 | 用途 |
|------|------|
| DEBUG | 请求/响应详情 |
| INFO | 关键操作（初始化、重试） |
| WARN | 可恢复的错误（重试中） |
| ERROR | 不可恢复的错误 |

### 日志格式

```
[ZSXQ-SDK] [LEVEL] [REQUEST-ID] Message
```

## 8. 线程安全

- `ZsxqClient` 实例必须是线程安全的
- 可在多线程/协程环境中共享使用
- 内部状态使用适当的同步机制保护
