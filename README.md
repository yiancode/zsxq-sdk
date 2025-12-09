# zsxq-sdk

> 知识星球多语言 SDK Monorepo - 类型安全、开箱即用

[![License](https://img.shields.io/badge/license-MIT-green)](LICENSE)

## 概述

本仓库是知识星球 SDK 的 **Monorepo**，包含多语言实现和统一的 API 规范。用户只需安装对应语言的包，无需关心其他语言的实现。

## SDK 安装

### TypeScript/JavaScript

```bash
npm install zsxq-sdk
```

```typescript
import { ZsxqClientBuilder } from 'zsxq-sdk';

const client = new ZsxqClientBuilder()
  .setToken('your-token')
  .build();

const groups = await client.groups.list();
```

详细文档：[packages/typescript/README.md](packages/typescript/README.md)

### Java

```xml
<dependency>
    <groupId>com.zsxq</groupId>
    <artifactId>zsxq-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

```java
ZsxqClient client = new ZsxqClientBuilder()
    .token("your-token")
    .build();

List<Group> groups = client.groups().list();
```

详细文档：[packages/java/README.md](packages/java/README.md)

## 功能模块

所有语言 SDK 提供统一的 API 结构：

| 模块 | 说明 | 主要方法 |
|------|------|----------|
| `groups` | 星球管理 | `list()`, `get()`, `getStatistics()` |
| `topics` | 话题管理 | `list()`, `get()`, `getComments()` |
| `users` | 用户管理 | `self()`, `get()` |
| `checkins` | 打卡管理 | `list()`, `getRankingList()` |
| `dashboard` | 数据面板 | `getOverview()`, `getIncomes()` |

## Token 获取

1. 打开 [知识星球网页版](https://wx.zsxq.com) 并登录
2. 按 `F12` 打开开发者工具 → Network
3. 刷新页面，找到 `api.zsxq.com` 请求
4. 在请求头中提取 `authorization` 值

## 仓库结构

```
zsxq-sdk/
├── spec/                    # API 规范（SSOT）
│   ├── openapi.yaml         # OpenAPI 3.0 规范
│   ├── errors/              # 错误码定义
│   └── sdk-design/          # SDK 设计规范
├── packages/
│   ├── typescript/          # TypeScript SDK（独立发布到 npm）
│   └── java/                # Java SDK（独立发布到 Maven）
└── docs/                    # 详细文档
```

## 文档索引

| 文档 | 说明 |
|------|------|
| [spec/openapi.yaml](spec/openapi.yaml) | API 接口定义 |
| [spec/errors/error-codes.yaml](spec/errors/error-codes.yaml) | 统一错误码 |
| [spec/sdk-design/architecture.md](spec/sdk-design/architecture.md) | SDK 架构设计 |
| [docs/guides/quick-start.md](docs/guides/quick-start.md) | 快速入门 |
| [docs/guides/error-handling.md](docs/guides/error-handling.md) | 错误处理 |

## 贡献开发

```bash
# TypeScript SDK
cd packages/typescript
npm install && npm run build && npm test

# Java SDK
cd packages/java
mvn compile && mvn test
```

## 许可证

MIT License

---

**声明**: 本项目为非官方 SDK，仅供学习和研究使用，请遵守知识星球的服务条款。
