# 知识星球API封装服务

> 通过封装知识星球原生API，提供标准化的RESTful API服务

[![Node.js Version](https://img.shields.io/badge/node-%3E%3D18.0.0-brightgreen)](https://nodejs.org/)
[![TypeScript](https://img.shields.io/badge/typescript-%5E5.3.3-blue)](https://www.typescriptlang.org/)
[![NestJS](https://img.shields.io/badge/nestjs-%5E10.3.0-red)](https://nestjs.com/)
[![License](https://img.shields.io/badge/license-MIT-green)](LICENSE)

## 📋 项目简介

本项目旨在通过Charles监测和封装知识星球的API接口，为不同角色的用户提供统一、稳定的RESTful API服务。

### 核心功能

#### 普通用户功能
- ✅ 获取知识星球列表
- ✅ 获取星球详细信息
- ✅ 浏览星球话题/帖子
- ✅ 查看训练营信息
- ✅ 获取训练营打卡记录
- ✅ 查看训练营排行榜

#### 星主功能
- ✅ 管理星球成员
- ✅ 创建和管理训练营
- ✅ 发布、编辑、删除话题
- ✅ 查看成员打卡详情
- ✅ 数据统计分析

## 🏗️ 技术架构

### 技术栈
- **运行时**: Node.js 18+
- **语言**: TypeScript 5+
- **框架**: NestJS 10+
- **数据库**: PostgreSQL + Redis
- **ORM**: TypeORM
- **API文档**: Swagger/OpenAPI
- **测试**: Jest

### 架构设计

详细架构设计请查看 [ARCHITECTURE.md](./ARCHITECTURE.md)

```
Controller → Service → Repository → Database
                ↓
         ZSXQ API Client → 知识星球API
```

## 🚀 快速开始

### 环境要求

- Node.js >= 18.0.0
- PostgreSQL >= 14
- Redis >= 6.0
- npm >= 9.0.0

### 安装

```bash
# 克隆项目
git clone https://github.com/yourusername/zsxq-api.git
cd zsxq-api

# 安装依赖
npm install

# 配置环境变量
cp .env.example .env
# 编辑 .env 文件，填入你的配置
```

### 数据库初始化

```bash
# 创建数据库
createdb zsxq_api

# 运行迁移
npm run migration:run
```

### 启动服务

```bash
# 开发模式
npm run start:dev

# 生产模式
npm run build
npm run start:prod
```

服务默认运行在 `http://localhost:3000`

### 访问API文档

启动服务后，访问 Swagger文档：
```
http://localhost:3000/api-docs
```

## 📖 使用指南

### API认证

所有API请求需要在Header中携带JWT Token：

```bash
Authorization: Bearer <your-jwt-token>
```

### 获取Token

```bash
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "your-username",
  "password": "your-password"
}
```

### 示例请求

#### 获取星球列表

```bash
GET /api/v1/planets
Authorization: Bearer <token>
```

#### 创建话题（星主权限）

```bash
POST /api/v1/owner/topics
Authorization: Bearer <token>
Content-Type: application/json

{
  "planetId": "123456",
  "title": "话题标题",
  "content": "话题内容"
}
```

## 🔧 开发指南

### 项目结构

```
zsxq-api/
├── src/
│   ├── modules/           # 业务模块
│   │   ├── auth/         # 认证模块
│   │   ├── user/         # 用户模块
│   │   ├── planet/       # 星球模块
│   │   ├── topic/        # 话题模块
│   │   ├── training-camp/# 训练营模块
│   │   └── member/       # 成员管理模块
│   ├── common/           # 公共模块
│   ├── config/           # 配置
│   ├── zsxq-client/      # 知识星球API客户端
│   └── main.ts           # 入口文件
├── test/                 # 测试文件
├── migrations/           # 数据库迁移文件
└── logs/                 # 日志文件
```

### 开发规范

- 遵循 [Conventional Commits](https://www.conventionalcommits.org/) 提交规范
- 代码格式化：`npm run format`
- 代码检查：`npm run lint`
- 运行测试：`npm run test`

### 测试

```bash
# 单元测试
npm run test

# 测试覆盖率
npm run test:cov

# E2E测试
npm run test:e2e

# 监听模式
npm run test:watch
```

## 📝 API文档

详细的API接口文档请查看：
- Swagger UI: `http://localhost:3000/api-docs`
- [API文档](./docs/api.md)（待完善）

## 🔐 安全性

- JWT Token认证
- API限流（100-200请求/分钟）
- SQL注入防护
- XSS防护
- HTTPS传输（生产环境）
- 敏感数据加密

## 🚦 限流策略

- **普通用户**: 100请求/分钟
- **星主**: 200请求/分钟
- 超出限制将返回 `429 Too Many Requests`

## 📊 监控与日志

### 日志位置
- 开发环境：控制台输出
- 生产环境：`logs/` 目录

### 日志级别
- ERROR: 错误日志
- WARN: 警告日志
- INFO: 重要信息
- DEBUG: 调试信息（仅开发环境）

## 🤝 贡献指南

1. Fork本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 🙏 致谢

- [NestJS](https://nestjs.com/) - 渐进式Node.js框架
- [TypeORM](https://typeorm.io/) - ORM框架
- [知识星球](https://www.zsxq.com/) - 提供API服务

## 📮 联系方式

如有问题或建议，请通过以下方式联系：

- 提交 [Issue](https://github.com/yourusername/zsxq-api/issues)
- 发送邮件至：your-email@example.com

## 🗺️ 路线图

- [x] 架构设计
- [x] 项目初始化
- [ ] 核心模块开发
  - [ ] 认证模块
  - [ ] 星球模块
  - [ ] 话题模块
  - [ ] 训练营模块
- [ ] API文档完善
- [ ] 单元测试
- [ ] E2E测试
- [ ] 性能优化
- [ ] 部署文档
- [ ] Docker支持

---

**注意**: 本项目仅供学习和研究使用，请遵守知识星球的服务条款。
