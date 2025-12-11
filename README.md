# zsxq-sdk

> 知识星球多语言 SDK Monorepo - 类型安全、开箱即用

[![License](https://img.shields.io/badge/license-MIT-green)](LICENSE)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.0+-blue)](packages/typescript)
[![Java](https://img.shields.io/badge/Java-17+-orange)](packages/java)
[![Go](https://img.shields.io/badge/Go-1.21+-00ADD8)](packages/go)
[![Python](https://img.shields.io/badge/Python-3.10+-yellow)](packages/python)

## 概述

本仓库是知识星球 SDK 的 **Monorepo**，包含多语言实现和统一的 API 规范。用户只需安装对应语言的包，无需关心其他语言的实现。

## 支持的 API 接口

### 星球管理 (Groups)

| 接口 | 说明 |
|------|------|
| 获取用户星球列表 | 获取当前登录用户已加入的所有星球 |
| 获取星球详情 | 获取指定星球的详细信息 |
| 获取星球标签列表 | 获取星球的所有标签 |
| 获取星球菜单配置 | 获取星球的菜单配置信息 |
| 获取星球角色成员 | 获取星主、合伙人、管理员列表 |
| 获取星球统计数据 | 获取星球的统计数据 |
| 获取星球成员详情 | 获取指定星球成员的详细信息 |
| 获取成员活跃摘要 | 获取成员在星球的活跃数据摘要 |
| 获取星球专栏列表 | 获取星球的专栏列表 |
| 获取专栏汇总信息 | 获取星球专栏的汇总信息 |
| 获取星球续费信息 | 获取星球的续费配置信息 |
| 获取星球分销信息 | 获取星球的分销配置信息 |
| 获取可升级星球列表 | 获取当前用户可升级的星球列表 |
| 获取推荐星球列表 | 获取系统推荐的星球列表 |
| 获取未读话题数量 | 获取所有已加入星球的未读话题数量 |

### 话题管理 (Topics)

| 接口 | 说明 |
|------|------|
| 获取星球话题列表 | 支持分页和筛选（全部/精华/问答） |
| 获取专栏话题列表 | 获取指定专栏的话题列表 |
| 获取话题详情 | 获取话题的完整详情信息 |
| 获取话题基础信息 | 轻量版话题信息（不含评论详情） |
| 获取话题评论列表 | 支持正序/倒序、置顶评论 |
| 获取话题打赏列表 | 获取话题的打赏记录列表 |
| 获取相关推荐话题 | 获取与当前话题相关的推荐话题 |

### 标签系统 (Hashtags)

| 接口 | 说明 |
|------|------|
| 获取标签话题列表 | 获取指定标签下的话题列表 |

### 打卡系统 (Checkins)

| 接口 | 说明 |
|------|------|
| 获取打卡项目列表 | 支持筛选（全部/进行中/已结束） |
| 获取打卡项目详情 | 获取指定打卡项目的详细信息 |
| 获取打卡项目统计 | 获取打卡项目的统计数据 |
| 获取打卡每日统计 | 获取打卡项目的每日统计数据 |
| 获取打卡话题列表 | 获取打卡项目的话题列表 |
| 获取打卡排行榜 | 获取打卡项目的排行榜 |
| 获取打卡参与用户 | 获取打卡项目的参与用户列表 |
| 创建打卡项目 | 星主/管理员权限 |
| 更新打卡项目 | 星主/管理员权限 |
| 获取我的打卡记录 | 获取当前用户在打卡项目的打卡记录 |
| 获取我的打卡日期 | 获取当前用户的打卡日期列表 |
| 获取我的打卡统计 | 获取当前用户在打卡项目的统计数据 |

### 排行榜系统 (Ranking)

| 接口 | 说明 |
|------|------|
| 获取星球排行榜 | 获取星球排行榜列表 |
| 获取星球排行统计 | 获取星球在排行榜的统计数据 |
| 获取积分排行榜 | 获取星球的积分排行榜 |
| 获取我的积分统计 | 获取当前用户在星球的积分统计 |
| 获取积分榜设置 | 获取星球积分榜的配置设置 |

### 用户系统 (Users)

| 接口 | 说明 |
|------|------|
| 获取当前用户信息 | 获取当前登录用户的详细信息 |
| 获取指定用户信息 | 获取指定用户的详细信息 |
| 获取用户统计数据 | 获取用户的统计数据 |
| 获取用户头像URL | 获取用户的头像 URL |
| 获取用户动态足迹 | 获取用户的动态足迹列表 |
| 获取用户星球足迹 | 获取用户的星球足迹列表 |
| 获取用户创建的星球 | 获取用户创建的星球列表 |
| 获取申请中的星球 | 获取当前用户申请中的星球列表 |
| 获取邀请人信息 | 获取邀请当前用户加入星球的邀请人信息 |
| 获取我的优惠券 | 获取当前用户的优惠券列表 |
| 获取我的备注列表 | 获取当前用户的备注列表 |
| 获取推荐关注用户 | 获取系统推荐关注的用户列表 |

### 数据面板 (Dashboard)

| 接口 | 说明 |
|------|------|
| 获取星球数据概览 | 星主专用：成员数、话题数、活跃度等 |
| 获取星球收入概览 | 星主专用：总收入、月收入、日收入 |
| 获取星球权限配置 | 获取星球的权限配置信息 |

### 其他接口

| 接口 | 说明 |
|------|------|
| 获取PK群组详情 | 获取 PK 群组的详细信息 |
| 获取PK对战记录 | 获取 PK 群组的对战记录列表 |
| 解析URL详情 | 解析 URL 获取详情信息 |
| 获取菜单阅读时间 | 获取星球菜单的最后阅读时间 |
| 更新菜单阅读时间 | 更新星球菜单的最后阅读时间 |

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

### Go

```bash
go get github.com/stinglong/zsxq-sdk/packages/go
```

```go
client := zsxq.NewClientBuilder().
    SetToken("your-token").
    MustBuild()

groups, err := client.Groups().List(ctx)
```

详细文档：[packages/go/README.md](packages/go/README.md)

### Python

```bash
pip install zsxq-sdk
```

```python
client = ZsxqClientBuilder() \
    .set_token("your-token") \
    .build()

groups = await client.groups.list()
```

详细文档：[packages/python/README.md](packages/python/README.md)

## 功能模块

所有语言 SDK 提供统一的 API 结构：

| 模块 | 说明 | 主要方法 |
|------|------|----------|
| `groups` | 星球管理 | `list()`, `get()`, `getStatistics()`, `getHashtags()` |
| `topics` | 话题管理 | `list()`, `get()`, `getComments()`, `getRewards()` |
| `users` | 用户管理 | `self()`, `get()`, `getStatistics()` |
| `checkins` | 打卡管理 | `list()`, `get()`, `getRankingList()`, `getStatistics()` |
| `dashboard` | 数据面板 | `getOverview()`, `getIncomes()`, `getPrivileges()` |

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
│   ├── typescript/          # TypeScript SDK
│   ├── java/                # Java SDK
│   ├── go/                  # Go SDK
│   └── python/              # Python SDK
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

# Go SDK
cd packages/go
go build ./... && go test ./...

# Python SDK
cd packages/python
pip install -e ".[dev]" && pytest
```

## 许可证

MIT License

---

**声明**: 本项目为非官方 SDK，仅供学习和研究使用，请遵守知识星球的服务条款。
