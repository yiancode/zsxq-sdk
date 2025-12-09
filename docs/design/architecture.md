# zsxq-sdk 架构设计

> 本文档定义 SDK 的核心架构，是所有实现的权威参考

## 设计目标

1. **轻量级**: 核心 SDK 无外部框架依赖，可独立使用
2. **开箱即用**: 提供 Builder 模式，简化配置
3. **可扩展**: 支持 NestJS 等框架集成
4. **类型安全**: 完整的 TypeScript 类型支持

---

## 核心架构

### 类关系图

```
ZsxqClientBuilder (构建器入口)
    │
    ▼
ZsxqClient (主客户端)
    │
    ├── ZsxqConfig (配置)
    │
    ├── Request (请求层)
    │   ├── GroupRequest
    │   ├── TopicRequest
    │   ├── UserRequest
    │   ├── CheckinRequest
    │   └── DashboardRequest
    │
    ├── Endpoint (端点定义)
    │   └── ZSXQ_ENDPOINTS
    │
    └── Cache (缓存层)
        └── TokenCache
```

### 请求流程

```
用户调用 → ZsxqClient → Request → HTTP请求 → 知识星球API
                ↓
            Response解析 → Model转换 → 返回用户
```

---

## 包结构

```
src/
├── index.ts                    # SDK 主入口
├── client/                     # 客户端核心
│   ├── zsxq-client.ts          # 主客户端类
│   ├── zsxq-client.builder.ts  # Builder 构建器
│   └── zsxq-client.config.ts   # 配置定义
├── request/                    # API 请求实现
│   ├── base.request.ts         # 请求基类
│   ├── group.request.ts        # 星球请求
│   ├── topic.request.ts        # 话题请求
│   ├── user.request.ts         # 用户请求
│   ├── checkin.request.ts      # 打卡请求
│   └── dashboard.request.ts    # Dashboard 请求
├── endpoint/                   # API 端点定义
│   └── index.ts                # 端点常量
├── model/                      # 数据模型
│   ├── group.model.ts          # 星球模型
│   ├── topic.model.ts          # 话题模型
│   ├── user.model.ts           # 用户模型
│   ├── checkin.model.ts        # 打卡模型
│   └── response.model.ts       # 响应模型
├── enums/                      # 枚举定义
│   ├── scope.enum.ts           # API scope
│   └── error-code.enum.ts      # 错误码
├── exception/                  # 异常定义
│   ├── zsxq.exception.ts       # 基础异常
│   ├── auth.exception.ts       # 认证异常
│   └── rate-limit.exception.ts # 限流异常
├── cache/                      # 缓存实现
│   └── token.cache.ts          # Token 缓存
├── utils/                      # 工具类
│   ├── http.util.ts            # HTTP 工具
│   ├── signature.util.ts       # 签名工具
│   └── transform.util.ts       # 数据转换
└── nestjs/                     # NestJS 集成（可选）
    ├── zsxq.module.ts          # NestJS 模块
    └── zsxq.service.ts         # NestJS 服务
```

---

## 核心类设计

### ZsxqClientBuilder

构建器模式入口，参考 FastAuth 的 `AuthRequestBuilder`。

```typescript
const client = new ZsxqClientBuilder()
  .setToken('your-token')
  .setTimeout(5000)
  .setRetry(3)
  .build();
```

### ZsxqClient

主客户端类，提供所有 API 访问入口。

```typescript
interface ZsxqClient {
  // 星球相关
  groups: GroupRequest;

  // 话题相关
  topics: TopicRequest;

  // 用户相关
  users: UserRequest;

  // 打卡相关
  checkins: CheckinRequest;

  // Dashboard（星主专用）
  dashboard: DashboardRequest;
}
```

### ZsxqConfig

客户端配置，参考 FastAuth 的 `AuthConfig`。

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

## 数据模型

### 响应模型

所有 API 响应遵循统一格式：

```typescript
interface ZsxqResponse<T = any> {
  succeeded: boolean;
  code?: number;
  message?: string;
  resp_data: T;
}
```

### 核心实体模型

详见各模型文件，主要包括：

- **Group**: 星球信息
- **Topic**: 话题信息
- **User**: 用户信息
- **Checkin**: 打卡项目
- **Comment**: 评论
- **Hashtag**: 标签

---

## 异常处理

### 异常层次

```
ZsxqException (基础异常)
    ├── ZsxqAuthException (认证异常)
    │   └── TokenExpiredException
    ├── ZsxqRateLimitException (限流异常)
    └── ZsxqApiException (API 异常)
```

### 错误码

详见 [error-codes.md](error-codes.md)

---

## 设计决策

### 为什么使用 Builder 模式？

1. **参考 FastAuth**: 经过验证的成熟模式
2. **配置灵活**: 支持可选参数的链式配置
3. **类型安全**: 编译时检查必填参数

### 为什么分离 NestJS 集成？

1. **核心 SDK 独立**: 不依赖特定框架
2. **可选引入**: 按需使用 NestJS 模块
3. **减少体积**: 仅核心功能时无额外依赖

---

## 参考资料

- FastAuth SDK 设计: [CLAUDE.md](../../reference/FastAuth-CLAUDE.md)
- 知识星球原生 API: [native-api.md](../reference/native-api.md)
- API 映射关系: [api-mapping.md](api-mapping.md)
