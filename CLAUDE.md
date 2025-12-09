# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目简介

zsxq-sdk - 知识星球 TypeScript SDK，提供类型安全的 API 封装。

## 项目定位

这是一个 **SDK 项目**，不是 REST API 服务。核心目标是：
- 封装知识星球原生 API
- 提供类型安全的 TypeScript 接口
- 支持 NestJS 依赖注入（可选）

## 常用命令

```bash
# 开发
npm run start:dev         # 开发模式（热重载）
npm run build             # 构建

# 代码质量
npm run lint              # ESLint 检查
npm run format            # Prettier 格式化

# 测试
npm run test              # 单元测试
npm run test:watch        # 监听模式
npm run test:cov          # 覆盖率报告
```

## SDK 架构

```
ZsxqClient (门面)
├── GroupsRequest      → /v2/groups/*
├── TopicsRequest      → /v2/topics/*
├── UsersRequest       → /v3/users/*
├── CheckinsRequest    → /v2/groups/*/checkins/*
└── HttpClient         → 底层 HTTP 请求
```

### 核心组件

- **ZsxqClient** - SDK 门面类，聚合所有 Request 模块
- **ZsxqClientBuilder** - Builder 模式构建客户端
- **HttpClient** - 底层 HTTP 客户端（重试、错误处理）
- **Request 模块** - 各功能域的 API 封装
- **NestJS 模块** - 可选的依赖注入支持

### 异常层次

```
ZsxqException (基类)
├── NetworkException        # 网络错误
├── AuthException          # 认证错误
│   ├── TokenInvalidException
│   └── TokenExpiredException
├── RateLimitException     # 频率限制
├── PermissionException    # 权限不足
└── ResourceNotFoundException  # 资源不存在
```

## 文档结构

```
docs/
├── README.md              # 文档首页
├── design/                # 设计文档 (SSOT)
│   ├── api-mapping.md     # SDK → API 映射（权威来源）
│   ├── architecture.md    # SDK 架构设计
│   └── error-codes.md     # 错误码定义（权威来源）
├── api/                   # API 参考文档
│   ├── client.md          # 客户端 API
│   ├── group.md           # 星球 API
│   ├── topic.md           # 话题 API
│   ├── user.md            # 用户 API
│   └── checkin.md         # 打卡 API
├── guides/                # 使用指南
│   ├── quick-start.md     # 快速开始
│   ├── authentication.md  # 认证指南
│   ├── error-handling.md  # 错误处理
│   └── nestjs-integration.md # NestJS 集成
├── reference/             # 参考文档
│   └── native-api.md      # 原生 API 参考（118 个端点）
└── archive/               # 归档文档
    └── v0.1/              # 旧版文档
```

### SSOT 原则

- **API 映射**: 只在 `design/api-mapping.md` 定义
- **错误码**: 只在 `design/error-codes.md` 定义
- 其他文档通过链接引用，不复制内容

## 开发规范

### 代码风格

```typescript
// 使用 Builder 模式
const client = new ZsxqClientBuilder()
  .setToken(token)
  .setTimeout(10000)
  .build();

// 类型安全的 API 调用
const groups: Group[] = await client.groups.list();
const topic: Topic = await client.topics.get(topicId);
```

### 错误处理

```typescript
try {
  const data = await client.groups.list();
} catch (error) {
  if (error instanceof TokenExpiredException) {
    // 处理 Token 过期
  } else if (error instanceof ZsxqException) {
    // 处理其他 SDK 错误
  }
}
```

### 测试

- 单元测试使用 Jest
- Mock 知识星球 API 响应
- 测试文件与源文件同目录，命名为 `*.spec.ts`

## 环境变量

```bash
ZSXQ_TOKEN=xxx           # 知识星球 Token
ZSXQ_TIMEOUT=10000       # 请求超时（毫秒）
ZSXQ_RETRY=3             # 重试次数
```

## 知识星球 API

- **基础 URL**: `https://api.zsxq.com`
- **认证头**: `authorization: <token>`
- **签名头**: `x-timestamp`, `x-signature`
- 详细 API 列表见 `docs/reference/native-api.md`
