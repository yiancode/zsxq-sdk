# 归档文档 (v0.1)

此目录包含项目早期版本的文档，已被新版文档取代。

## 归档原因

这些文档是项目初期开发时创建的，现已整合到新的 SSOT (Single Source of Truth) 文档结构中。

## 归档文件

| 文件 | 原用途 | 替代文档 |
|------|--------|----------|
| `抓包API文档.md` | HAR 抓包分析结果 | `../reference/native-api.md` |
| `PRD.md` | 产品需求文档 | `../design/api-mapping.md` |
| `cache-design.md` | 缓存设计 | 待整合 |
| `zsxq-native-api-reference.md` | 原生 API 参考 | `../reference/native-api.md` |
| `zsxq-client-enhancement.md` | 客户端增强 | `../api/client.md` |

## 新文档结构

新的文档结构遵循 SSOT 原则：

```
docs/
├── README.md              # 文档首页
├── design/                # 设计文档 (SSOT)
│   ├── api-mapping.md     # SDK 方法 → 原生 API 映射
│   ├── architecture.md    # SDK 架构设计
│   └── error-codes.md     # 错误码定义
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
│   └── native-api.md      # 原生 API 参考
└── archive/               # 归档文档
    └── v0.1/              # 本目录
```

## 注意事项

- 这些文档仅供历史参考
- 如需查阅最新信息，请使用新文档结构中的对应文档
- 归档文档可能包含过时或不准确的信息
