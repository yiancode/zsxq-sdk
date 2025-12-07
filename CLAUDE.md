# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目简介

知识星球API封装服务 - 通过封装知识星球原生API，提供标准化的RESTful API服务。基于NestJS框架构建，支持多角色权限管理（普通用户、星主）。

## 常用命令

```bash
# 安装依赖
npm install

# 开发模式（热重载）
npm run start:dev

# 构建
npm run build

# 生产模式
npm run start:prod

# 代码检查与格式化
npm run lint
npm run format

# 测试
npm run test              # 单元测试
npm run test:watch        # 监听模式
npm run test:cov          # 覆盖率
npm run test:e2e          # E2E测试

# 数据库迁移
npm run migration:run     # 运行迁移
npm run migration:revert  # 回滚迁移
npm run migration:generate # 生成迁移
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

- **zsxq-client/** - 知识星球API客户端封装，负责与知识星球原生API通信
- **modules/auth/** - JWT认证、Passport策略、权限守卫
- **modules/user/** - 用户实体、角色枚举（USER/PLANET_OWNER/ADMIN）
- **modules/planet/** - 星球管理
- **modules/topic/** - 话题/帖子管理
- **modules/training-camp/** - 训练营功能
- **modules/member/** - 星主专用的成员管理

### 公共组件 (common/)

- `filters/http-exception.filter.ts` - 全局异常过滤器，统一错误响应格式
- `interceptors/transform.interceptor.ts` - 响应转换拦截器，统一成功响应格式
- `interceptors/logging.interceptor.ts` - 请求日志拦截器
- `decorators/roles.decorator.ts` - 角色装饰器
- `decorators/current-user.decorator.ts` - 获取当前用户装饰器

### 统一响应格式

```typescript
// 成功
{ success: true, data: {...}, message: "...", timestamp: "..." }

// 错误
{ success: false, error: { code: "...", message: [...] }, timestamp: "...", path: "..." }
```

## 开发规范

### 路径别名

使用 `@/` 别名指向 `src/` 目录：
```typescript
import { UserModule } from '@/modules/user/user.module';
```

### 角色权限

```typescript
enum UserRole {
  USER = 'user',
  PLANET_OWNER = 'owner',
  ADMIN = 'admin'
}

// 使用装饰器控制权限
@Roles(UserRole.PLANET_OWNER)
```

### API路由约定

- 普通用户: `/api/v1/planets`, `/api/v1/topics`, `/api/v1/training-camps`
- 星主专用: `/api/v1/owner/*`

## 环境配置

复制 `.env.example` 为 `.env` 并配置：
- 数据库: PostgreSQL (DB_*)
- 缓存: Redis (REDIS_*)
- JWT密钥: JWT_SECRET, JWT_REFRESH_SECRET
- 知识星球API: ZSXQ_API_* (基础URL、超时、重试配置)

## 当前开发状态

查看 `IMPLEMENTATION_PLAN.md` 了解详细进度。当前处于 Stage 1（基础设施搭建）阶段。