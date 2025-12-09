# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目简介

zsxq-sdk - 知识星球多语言 SDK Monorepo，提供类型安全的 API 封装。

## 项目结构

```
zsxq-sdk/
├── spec/                        # API 规范（SSOT）
│   ├── openapi.yaml             # OpenAPI 3.0 规范
│   ├── errors/error-codes.yaml  # 错误码定义
│   └── sdk-design/              # SDK 设计规范
├── packages/
│   ├── typescript/              # TypeScript SDK
│   │   ├── src/
│   │   ├── package.json
│   │   └── tsconfig.json
│   └── java/                    # Java SDK
│       ├── src/main/java/
│       └── pom.xml
└── docs/                        # 通用文档
```

## 常用命令

### TypeScript SDK

```bash
cd packages/typescript
npm install               # 安装依赖
npm run build             # 构建
npm run test              # 测试
npm run lint              # 代码检查
```

### Java SDK

```bash
cd packages/java
mvn compile               # 编译
mvn test                  # 测试
mvn package               # 打包
mvn install               # 安装到本地仓库
```

## SDK 统一架构

所有语言 SDK 遵循相同设计：

```
ZsxqClient (门面)
├── groups      → 星球管理 (/v2/groups/*)
├── topics      → 话题管理 (/v2/topics/*)
├── users       → 用户管理 (/v3/users/*)
├── checkins    → 打卡管理 (/v2/groups/*/checkins/*)
└── dashboard   → 数据面板 (/v2/dashboard/*)
```

### 核心组件

- **ZsxqClient** - SDK 门面类，聚合所有 Request 模块
- **ZsxqClientBuilder** - Builder 模式构建客户端
- **HttpClient** - 底层 HTTP 客户端（重试、签名）
- **Request 模块** - 各功能域的 API 封装

### 异常层次

```
ZsxqException (基类)
├── AuthException           # 认证错误 (1xxxx)
│   ├── TokenInvalidException
│   └── TokenExpiredException
├── PermissionException     # 权限错误 (2xxxx)
├── ResourceNotFoundException  # 资源不存在 (3xxxx)
├── RateLimitException      # 限流 (4xxxx)
└── NetworkException        # 网络错误 (7xxxx)
```

## SSOT 原则

- **API 规范**: `spec/openapi.yaml`
- **错误码**: `spec/errors/error-codes.yaml`
- **SDK 设计**: `spec/sdk-design/`
- 各语言 SDK 实现必须遵循规范

## 开发规范

### 代码风格

**TypeScript**:
```typescript
const client = new ZsxqClientBuilder()
  .setToken(token)
  .setTimeout(10000)
  .build();

const groups = await client.groups.list();
```

**Java**:
```java
ZsxqClient client = new ZsxqClientBuilder()
    .token(token)
    .timeout(10000)
    .build();

List<Group> groups = client.groups().list();
```

### 新增 API 流程

1. 更新 `spec/openapi.yaml` 添加端点定义
2. 更新 `spec/errors/error-codes.yaml`（如有新错误码）
3. 在各语言 SDK 中实现对应方法
4. 保持方法命名一致

## 环境变量

```bash
ZSXQ_TOKEN=xxx              # 知识星球 Token
ZSXQ_TIMEOUT=10000          # 请求超时（毫秒）
ZSXQ_RETRY_COUNT=3          # 重试次数
```

## 知识星球 API

- **基础 URL**: `https://api.zsxq.com`
- **认证头**: `authorization: <token>`
- **签名头**: `x-timestamp`, `x-signature`
- 详细规范见 `spec/openapi.yaml`
