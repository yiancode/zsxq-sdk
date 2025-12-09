# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目简介

知识星球API封装服务 - 封装知识星球原生API，提供标准化RESTful服务。基于NestJS + TypeORM + PostgreSQL + Redis构建。

## 常用命令

```bash
# 开发
npm run start:dev         # 开发模式（热重载）
npm run build && npm run start:prod  # 生产模式

# 代码质量
npm run lint              # ESLint检查并自动修复
npm run format            # Prettier格式化

# 测试
npm run test              # 单元测试
npm run test -- --testPathPattern="user"  # 运行单个模块测试
npm run test:watch        # 监听模式
npm run test:cov          # 覆盖率报告
npm run test:e2e          # E2E测试

# 数据库迁移
npm run migration:run     # 运行迁移
npm run migration:revert  # 回滚迁移
npm run migration:generate -- src/migrations/MigrationName  # 生成迁移
```

## 架构概览

```
Controller → Service → Repository → PostgreSQL
                ↓
         ZsxqClientService → 知识星球原生API
                ↓
              Redis (缓存)
```

### 核心模块

- **zsxq-client/** - 知识星球API客户端，封装原生API调用（重试、错误处理）
- **modules/auth/** - JWT认证、Passport策略、权限守卫
- **modules/user/** - 用户实体、角色枚举
- **modules/planet/** - 星球管理
- **modules/topic/** - 话题/帖子管理
- **modules/training-camp/** - 训练营功能
- **modules/member/** - 星主专用的成员管理

### 公共组件 (common/)

- `filters/http-exception.filter.ts` - 全局异常过滤器
- `interceptors/transform.interceptor.ts` - 统一响应格式转换
- `interceptors/logging.interceptor.ts` - 请求日志
- `decorators/roles.decorator.ts` - 角色装饰器
- `decorators/current-user.decorator.ts` - 获取当前用户

### 统一响应格式

```typescript
// 成功: { success: true, data: {...}, message: "...", timestamp: "..." }
// 错误: { success: false, error: { code: "...", message: [...] }, timestamp: "...", path: "..." }
```

## 开发规范

### 路径别名

```typescript
import { UserModule } from '@/modules/user/user.module';     // src/*
import { UserModule } from '@modules/user/user.module';      // src/modules/*
import { LoggingInterceptor } from '@common/interceptors/logging.interceptor';  // src/common/*
import { ZsxqClientService } from '@zsxq-client/zsxq-client.service';  // src/zsxq-client/*
```

### 角色与权限

```typescript
enum UserRole { USER = 'user', PLANET_OWNER = 'owner', ADMIN = 'admin' }

// 使用装饰器控制权限
@Roles(UserRole.PLANET_OWNER)
```

### API路由约定

- 普通用户: `/api/v1/planets`, `/api/v1/topics`, `/api/v1/training-camps`
- 星主专用: `/api/v1/owner/*`
- Swagger文档: `http://localhost:3000/api-docs`

## 环境配置

复制 `.env.example` 为 `.env` 并配置：
- 数据库: PostgreSQL (DB_*)
- 缓存: Redis (REDIS_*)
- JWT密钥: JWT_SECRET, JWT_REFRESH_SECRET
- 知识星球API: ZSXQ_API_* (基础URL、超时、重试配置)