# zsxq-sdk 重构实施计划

> 将 zsxq-api 重构为符合 SDK 标准的 zsxq-sdk

## 重构目标

1. **项目重命名**: zsxq-api → zsxq-sdk
2. **架构重构**: 参考 FastAuth SDK 设计模式，适配 TypeScript
3. **文档重构**: 遵循 SSOT 原则，参考 AutoDepositRefundAgent 文档架构

---

## Stage 1: 项目重命名

**Goal**: 完成项目基础信息重命名

**Success Criteria**:
- [x] package.json name 改为 zsxq-sdk
- [x] 所有相关描述更新
- [x] Git 仓库信息不变（保留提交历史）

**Status**: ✅ Complete

---

## Stage 2: 文档重构（SSOT）

**Goal**: 按 SSOT 原则重构文档目录

**新文档结构**:

```
docs/
├── README.md                   # 文档索引
├── design/                     # 设计文档
│   ├── architecture.md         # 架构设计
│   ├── api-mapping.md          # API 映射关系（SSOT）
│   └── error-codes.md          # 错误码定义（SSOT）
├── api/                        # API 参考
│   ├── client.md               # 客户端 API
│   ├── group.md                # 星球 API
│   ├── topic.md                # 话题 API
│   ├── user.md                 # 用户 API
│   └── checkin.md              # 打卡 API
├── guides/                     # 使用指南
│   ├── quick-start.md          # 快速开始
│   ├── authentication.md       # 认证指南
│   ├── nestjs-integration.md   # NestJS 集成
│   └── error-handling.md       # 错误处理
├── reference/                  # 原生 API 参考
│   └── native-api.md           # 原生 API 文档（合并）
└── archive/                    # 归档文档
    └── v0.1/                   # 旧版本文档
```

**SSOT 核心文件**:
- `design/api-mapping.md`: API 映射的唯一来源
- `design/error-codes.md`: 错误码的唯一来源
- 其他文档通过链接引用这些 SSOT 文件

**Status**: Not Started

---

## Stage 3: SDK 核心架构重构

**Goal**: 按 FastAuth 设计模式重构代码结构

**设计对比**:

| FastAuth (Java) | zsxq-sdk (TypeScript) | 说明 |
|-----------------|----------------------|------|
| AuthRequestBuilder | ZsxqClientBuilder | Builder 入口类 |
| AuthDefaultRequest | ZsxqRequest | 请求基类 |
| AuthSource | ZsxqEndpoint | API 端点定义 |
| AuthConfig | ZsxqConfig | 客户端配置 |
| AuthUser/AuthToken | ZsxqUser/ZsxqToken | 数据模型 |

**新目录结构**:

```
src/
├── index.ts                    # SDK 主入口（导出所有公共 API）
├── client/                     # 客户端核心（原 zsxq-client）
│   ├── zsxq-client.ts          # 主客户端类
│   ├── zsxq-client.builder.ts  # Builder 模式构建器
│   └── zsxq-client.config.ts   # 配置类
├── request/                    # API 请求实现
│   ├── base.request.ts         # 请求基类
│   ├── group.request.ts        # 星球相关请求
│   ├── topic.request.ts        # 话题相关请求
│   ├── user.request.ts         # 用户相关请求
│   ├── checkin.request.ts      # 打卡相关请求
│   └── dashboard.request.ts    # Dashboard 请求
├── endpoint/                   # API 端点定义
│   ├── index.ts                # 端点汇总
│   ├── group.endpoint.ts       # 星球端点
│   ├── topic.endpoint.ts       # 话题端点
│   ├── user.endpoint.ts        # 用户端点
│   └── checkin.endpoint.ts     # 打卡端点
├── model/                      # 数据模型
│   ├── group.model.ts          # 星球模型
│   ├── topic.model.ts          # 话题模型
│   ├── user.model.ts           # 用户模型
│   ├── checkin.model.ts        # 打卡模型
│   └── response.model.ts       # 响应模型
├── enums/                      # 枚举定义
│   ├── scope.enum.ts           # API scope 枚举
│   └── error-code.enum.ts      # 错误码枚举
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

**Success Criteria**:
- 核心 SDK 可独立使用（不依赖 NestJS）
- NestJS 集成作为可选模块
- 遵循 Builder 模式
- 完整的 TypeScript 类型支持

**Status**: Not Started

---

## Stage 4: 更新项目入口文件

**Goal**: 更新 README.md 和 CLAUDE.md

**README.md 结构**（参考 FastAuth）:
1. 项目徽章
2. 项目简介
3. 核心特性
4. 快速开始
5. 使用示例
6. 文档链接
7. 更新日志
8. 贡献指南
9. 许可证

**CLAUDE.md 结构**:
1. 项目概述
2. 常用命令
3. 核心架构
4. 包结构
5. 核心类
6. 扩展指南
7. 依赖说明
8. 测试说明

**Status**: Not Started

---

## 执行顺序

1. ✅ 分析参考项目（已完成）
2. ⏳ Stage 1: 项目重命名（当前）
3. ⏳ Stage 2: 文档重构（先整理文档，明确 API 结构）
4. ⏳ Stage 3: SDK 核心架构重构
5. ⏳ Stage 4: 更新项目入口文件

---

## 注意事项

- 保留 Git 提交历史
- 每个阶段可独立编译和测试
- NestJS 功能保留为可选集成
- 现有功能不受影响

---

**最后更新**: 2025-12-09
**当前进度**: Stage 1 - In Progress
