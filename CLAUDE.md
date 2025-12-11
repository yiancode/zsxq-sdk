# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目简介

zsxq-sdk - 知识星球多语言 SDK Monorepo，提供类型安全的 API 封装。

## 常用命令

### TypeScript SDK

```bash
cd packages/typescript
npm install                           # 安装依赖
npm run build                         # 构建
npm run test                          # 运行所有测试
npm run test -- --testPathPattern="GroupsRequest"  # 运行单个测试
npm run lint                          # 代码检查
```

### Java SDK

```bash
cd packages/java
mvn compile                           # 编译
mvn test                              # 运行所有测试
mvn test -Dtest=GroupsRequestTest     # 运行单个测试类
mvn test -Dtest=GroupsRequestTest#testList  # 运行单个测试方法
mvn package                           # 打包

# 集成测试（需要真实 Token）
ZSXQ_TOKEN="your-token" ZSXQ_GROUP_ID="group-id" mvn test -Dtest=IntegrationTest
```

### Go SDK

```bash
cd packages/go
go mod tidy                           # 整理依赖
go build ./...                        # 构建
go test ./...                         # 运行所有测试
go test ./client -run TestNewClient   # 运行单个测试
go test ./request -v                  # 详细输出运行 request 包测试

# 集成测试
ZSXQ_TOKEN="your-token" ZSXQ_GROUP_ID="group-id" go test ./... -v
```

### Python SDK

```bash
cd packages/python
pip install -e ".[dev]"               # 安装开发依赖
pytest                                # 运行所有测试
pytest tests/test_client.py -k test_list  # 运行单个测试
black zsxq                            # 格式化
mypy zsxq                             # 类型检查
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

### 各语言包结构对应

| 组件 | TypeScript | Java | Go | Python |
|------|-----------|------|-----|--------|
| 入口 | `src/index.ts` | `com.zsxq.sdk.client` | `zsxq.go` | `zsxq/__init__.py` |
| 客户端 | `src/client/` | `com.zsxq.sdk.client/` | `client/` | `zsxq/client.py` |
| 请求模块 | `src/request/` | `com.zsxq.sdk.request/` | `request/` | `zsxq/request.py` |
| 模型 | `src/model/` | `com.zsxq.sdk.model/` | `model/` | `zsxq/model.py` |
| 异常 | `src/exception/` | `com.zsxq.sdk.exception/` | `exception/` | `zsxq/exception.py` |
| HTTP | `src/http/` | `com.zsxq.sdk.http/` | `http/` | `zsxq/http.py` |

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

**Go**:
```go
client := zsxq.NewClientBuilder().
    SetToken(token).
    SetTimeout(10 * time.Second).
    MustBuild()

groups, err := client.Groups().List(ctx)
```

**Python**:
```python
client = ZsxqClientBuilder() \
    .set_token(token) \
    .set_timeout(10) \
    .build()

groups = await client.groups.list()
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
- **签名头**: `x-timestamp`, `x-signature`（HMAC-SHA1 签名）
- 详细规范见 `spec/openapi.yaml`

## 工具脚本

`tools/` 目录包含开发辅助脚本：

- `analyze_har.py` - 分析 HAR 抓包文件，提取 API 信息
- `generate_api_docs.py` - 从分析报告生成 Markdown API 文档
- `fix_api_docs.py` - 修复和格式化 API 文档

## 自定义命令

- `/commit` - 智能分析变更并自动生成 Git 提交（使用 Conventional Commits 格式，自动推送）

## API 测试状态

查看 `docs/TESTING_STATUS.md` 了解各接口的测试覆盖情况。当前覆盖率约 14%，已测试 7 个核心接口。

## 跨语言开发注意事项

- 新增 API 时，需同步更新所有语言 SDK 实现
- 方法命名保持一致：`list()`, `get()`, `create()`, `update()`, `delete()`
- 各语言 ID 参数统一用 `groupId`, `topicId` 等驼峰式命名
- 异常类型需与 `spec/errors/error-codes.yaml` 定义一致
