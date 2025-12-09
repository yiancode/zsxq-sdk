# zsxq-sdk 文档

> 知识星球 TypeScript SDK 官方文档

## 文档结构

本文档遵循 **SSOT (Single Source of Truth)** 原则，确保每个概念只有一个权威定义。

### 设计文档 (`design/`)

系统设计与核心定义，是其他文档引用的权威来源。

| 文档 | 说明 |
|------|------|
| [architecture.md](design/architecture.md) | SDK 架构设计 |
| [api-mapping.md](design/api-mapping.md) | API 映射关系（SSOT） |
| [error-codes.md](design/error-codes.md) | 错误码定义（SSOT） |

### API 参考 (`api/`)

SDK 公共 API 详细文档。

| 文档 | 说明 |
|------|------|
| [client.md](api/client.md) | 客户端 API |
| [group.md](api/group.md) | 星球 API |
| [topic.md](api/topic.md) | 话题 API |
| [user.md](api/user.md) | 用户 API |
| [checkin.md](api/checkin.md) | 打卡 API |

### 使用指南 (`guides/`)

使用场景和最佳实践。

| 文档 | 说明 |
|------|------|
| [quick-start.md](guides/quick-start.md) | 快速开始 |
| [authentication.md](guides/authentication.md) | 认证指南 |
| [nestjs-integration.md](guides/nestjs-integration.md) | NestJS 集成 |
| [error-handling.md](guides/error-handling.md) | 错误处理 |

### 原生 API 参考 (`reference/`)

知识星球原生 API 文档，供深度开发参考。

| 文档 | 说明 |
|------|------|
| [native-api.md](reference/native-api.md) | 原生 API 完整文档 |

### 归档文档 (`archive/`)

历史版本文档。

---

## SSOT 原则说明

以下内容在本文档中只有一个权威定义位置：

- **API 端点映射**: [design/api-mapping.md](design/api-mapping.md)
- **错误码定义**: [design/error-codes.md](design/error-codes.md)
- **数据模型**: [design/architecture.md](design/architecture.md)

其他文档中如需引用，应使用链接而非复制内容。

---

## 快速导航

- **新手入门**: [快速开始指南](guides/quick-start.md)
- **了解架构**: [架构设计文档](design/architecture.md)
- **API 参考**: [客户端 API](api/client.md)
- **问题排查**: [错误处理指南](guides/error-handling.md)
