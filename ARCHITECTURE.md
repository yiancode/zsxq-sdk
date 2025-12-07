# 知识星球API项目架构设计

## 1. 项目概述

本项目旨在通过Charles监测和封装知识星球的API接口，为不同角色的用户提供标准化的RESTful API服务。

### 1.1 核心目标
- 封装知识星球原生API，提供统一的接口规范
- 支持多角色权限管理（普通用户、星主）
- 提供稳定、高性能的API服务
- 良好的文档和可维护性

## 2. 技术选型

### 2.1 后端技术栈
- **运行时**: Node.js 18+ (LTS)
- **语言**: TypeScript 5+
- **框架**: NestJS
  - 原因：企业级框架、依赖注入、装饰器语法、内置支持多种功能
- **数据库**:
  - PostgreSQL (主数据库 - 用户、权限等结构化数据)
  - Redis (缓存、会话管理、限流)
- **ORM**: TypeORM
- **API文档**: Swagger/OpenAPI
- **验证**: class-validator + class-transformer
- **HTTP客户端**: Axios
- **日志**: Winston
- **测试**: Jest

### 2.2 技术选型理由

#### 为什么选择TypeScript/Node.js而非Go？
1. **生态优势**: npm生态系统庞大，HTTP请求、JSON处理库成熟
2. **开发效率**: TypeScript提供强类型检查，减少运行时错误
3. **维护成本**: JavaScript/TypeScript开发者基数大，易于团队扩展
4. **异步处理**: Node.js天生异步非阻塞，适合I/O密集型API代理服务

#### 为什么选择NestJS？
1. **模块化架构**: 清晰的分层结构，便于大型项目管理
2. **依赖注入**: 提高代码可测试性和可维护性
3. **装饰器支持**: 优雅的路由、验证、权限控制
4. **内置功能**: Guards(权限)、Interceptors(拦截器)、Pipes(数据转换)
5. **TypeScript原生**: 完整的类型支持

## 3. 系统架构

### 3.1 整体架构图

```
┌─────────────────────────────────────────────────────────────┐
│                         客户端层                              │
│                  (Web/Mobile/Third-party)                   │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      │ HTTPS/REST API
                      ▼
┌─────────────────────────────────────────────────────────────┐
│                      API Gateway                             │
│              (认证、限流、日志、路由)                         │
└─────────────────────┬───────────────────────────────────────┘
                      │
        ┌─────────────┼─────────────┐
        ▼             ▼             ▼
┌──────────────┐ ┌──────────┐ ┌──────────────┐
│  Controller  │ │Controller│ │  Controller  │
│   (路由层)   │ │  (路由层) │ │   (路由层)   │
└──────┬───────┘ └────┬─────┘ └──────┬───────┘
       │              │               │
       ▼              ▼               ▼
┌──────────────┐ ┌──────────┐ ┌──────────────┐
│   Service    │ │ Service  │ │   Service    │
│  (业务层)    │ │ (业务层)  │ │  (业务层)    │
└──────┬───────┘ └────┬─────┘ └──────┬───────┘
       │              │               │
       └──────────────┼───────────────┘
                      │
        ┌─────────────┼─────────────┐
        ▼             ▼             ▼
┌──────────────┐ ┌──────────┐ ┌──────────────┐
│ Repository   │ │  Cache   │ │   ZSXQ API   │
│  (数据层)    │ │  Layer   │ │   Client     │
└──────┬───────┘ └────┬─────┘ └──────┬───────┘
       │              │               │
       ▼              ▼               ▼
┌──────────────┐ ┌──────────┐ ┌──────────────┐
│  PostgreSQL  │ │  Redis   │ │知识星球原始API│
└──────────────┘ └──────────┘ └──────────────┘
```

### 3.2 分层架构详解

#### Controller层 (控制器层)
- 职责：接收HTTP请求、参数验证、调用Service、返回响应
- 特点：薄控制器，只做路由和数据转换

#### Service层 (业务逻辑层)
- 职责：核心业务逻辑、事务管理、异常处理
- 特点：可复用的业务组件

#### Repository层 (数据访问层)
- 职责：数据库操作、数据持久化
- 特点：隔离数据访问细节

#### ZSXQ Client层 (知识星球API客户端)
- 职责：封装知识星球原始API调用
- 特点：统一的HTTP请求处理、错误重试、响应格式化

## 4. 核心模块设计

### 4.1 模块划分

```
src/
├── modules/
│   ├── auth/                 # 认证模块
│   │   ├── guards/          # 权限守卫
│   │   ├── strategies/      # 认证策略(JWT等)
│   │   └── decorators/      # 自定义装饰器
│   ├── user/                # 用户模块
│   │   ├── entities/        # 用户实体
│   │   ├── dto/            # 数据传输对象
│   │   └── services/       # 用户服务
│   ├── planet/              # 星球模块
│   │   ├── entities/
│   │   ├── dto/
│   │   └── services/
│   ├── topic/               # 话题/帖子模块
│   │   ├── entities/
│   │   ├── dto/
│   │   └── services/
│   ├── training-camp/       # 训练营模块
│   │   ├── entities/
│   │   ├── dto/
│   │   └── services/
│   └── member/              # 成员管理模块(星主专用)
│       ├── entities/
│       ├── dto/
│       └── services/
├── common/                  # 公共模块
│   ├── decorators/         # 装饰器
│   ├── filters/            # 异常过滤器
│   ├── guards/             # 守卫
│   ├── interceptors/       # 拦截器
│   ├── pipes/              # 管道
│   └── utils/              # 工具类
├── config/                  # 配置
│   ├── database.config.ts
│   ├── redis.config.ts
│   └── app.config.ts
└── zsxq-client/            # 知识星球API客户端
    ├── client.ts           # HTTP客户端
    ├── endpoints/          # API端点定义
    ├── types/              # 类型定义
    └── interceptors/       # 请求/响应拦截器
```

### 4.2 角色权限设计

```typescript
enum UserRole {
  USER = 'user',           // 普通用户
  PLANET_OWNER = 'owner',  // 星主
  ADMIN = 'admin'          // 系统管理员
}

// 权限装饰器使用示例
@Controller('planets')
export class PlanetController {
  // 所有用户都可访问
  @Get(':id/topics')
  @Roles(UserRole.USER, UserRole.PLANET_OWNER)
  getTopics() {}

  // 仅星主可访问
  @Post(':id/topics')
  @Roles(UserRole.PLANET_OWNER)
  createTopic() {}
}
```

## 5. API设计规范

### 5.1 RESTful接口设计

#### 普通用户API
```
GET    /api/v1/planets                      # 获取星球列表
GET    /api/v1/planets/:id                  # 获取星球详情
GET    /api/v1/planets/:id/topics           # 获取星球话题列表
GET    /api/v1/topics/:id                   # 获取话题详情
GET    /api/v1/training-camps/:id           # 获取训练营信息
GET    /api/v1/training-camps/:id/checkins  # 获取训练营打卡记录
GET    /api/v1/training-camps/:id/ranking   # 获取训练营排行榜
```

#### 星主API
```
GET    /api/v1/owner/planets/:id/members    # 获取星球成员列表
POST   /api/v1/owner/training-camps         # 创建训练营
GET    /api/v1/owner/training-camps/:id/checkins  # 获取训练营打卡详情
POST   /api/v1/owner/topics                 # 创建话题
PUT    /api/v1/owner/topics/:id             # 更新话题
DELETE /api/v1/owner/topics/:id             # 删除话题
```

### 5.2 统一响应格式

```typescript
// 成功响应
{
  "success": true,
  "data": { ... },
  "message": "操作成功",
  "timestamp": "2025-12-07T12:00:00Z"
}

// 错误响应
{
  "success": false,
  "error": {
    "code": "PLANET_NOT_FOUND",
    "message": "星球不存在",
    "details": { ... }
  },
  "timestamp": "2025-12-07T12:00:00Z"
}
```

## 6. 数据库设计

### 6.1 核心表结构

```sql
-- 用户表
CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  zsxq_user_id VARCHAR(100) UNIQUE,
  username VARCHAR(100),
  role VARCHAR(20) DEFAULT 'user',
  created_at TIMESTAMP DEFAULT NOW(),
  updated_at TIMESTAMP DEFAULT NOW()
);

-- 星球表
CREATE TABLE planets (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  zsxq_planet_id VARCHAR(100) UNIQUE,
  name VARCHAR(200),
  description TEXT,
  owner_id UUID REFERENCES users(id),
  created_at TIMESTAMP DEFAULT NOW(),
  updated_at TIMESTAMP DEFAULT NOW()
);

-- 用户-星球关系表
CREATE TABLE user_planets (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID REFERENCES users(id),
  planet_id UUID REFERENCES planets(id),
  joined_at TIMESTAMP DEFAULT NOW(),
  UNIQUE(user_id, planet_id)
);
```

## 7. 缓存策略

### 7.1 Redis缓存设计

```
# 星球信息缓存 (TTL: 1小时)
planet:{planet_id}

# 话题列表缓存 (TTL: 5分钟)
planet:{planet_id}:topics:page:{page}

# 用户权限缓存 (TTL: 30分钟)
user:{user_id}:permissions

# 限流 (TTL: 1分钟)
ratelimit:{user_id}:{endpoint}
```

## 8. 安全性设计

### 8.1 认证方式
- JWT Token认证
- Token有效期：7天
- Refresh Token机制

### 8.2 API限流
- 普通用户：100请求/分钟
- 星主：200请求/分钟
- 使用Redis实现滑动窗口限流

### 8.3 数据安全
- 敏感数据加密存储
- HTTPS传输
- SQL注入防护(使用ORM参数化查询)
- XSS防护(响应头设置)

## 9. 监控与日志

### 9.1 日志级别
- ERROR: 错误日志(需要告警)
- WARN: 警告日志
- INFO: 重要信息(API调用、业务操作)
- DEBUG: 调试信息(开发环境)

### 9.2 监控指标
- API响应时间
- 错误率
- 知识星球API调用成功率
- 缓存命中率
- 数据库连接池状态

## 10. 部署架构

```
┌─────────────────────────────────────────┐
│              负载均衡 (Nginx)            │
└─────────┬───────────────────────────────┘
          │
    ┌─────┴─────┬─────────┐
    ▼           ▼         ▼
┌────────┐ ┌────────┐ ┌────────┐
│ API实例1│ │ API实例2│ │ API实例3│
└────┬───┘ └────┬───┘ └────┬───┘
     │          │          │
     └──────────┼──────────┘
                │
        ┌───────┴────────┐
        ▼                ▼
  ┌──────────┐    ┌──────────┐
  │PostgreSQL│    │  Redis   │
  │ (主从)   │    │ (哨兵)   │
  └──────────┘    └──────────┘
```

## 11. 扩展性设计

### 11.1 水平扩展
- 无状态API设计
- 使用Redis共享会话
- 数据库读写分离

### 11.2 插件化设计
- 支持自定义中间件
- 事件驱动架构(EventEmitter)
- Webhook支持

## 12. 开发规范

### 12.1 代码规范
- ESLint + Prettier
- Commit规范：Conventional Commits
- 代码审查流程

### 12.2 测试策略
- 单元测试覆盖率 > 80%
- 集成测试覆盖核心流程
- E2E测试覆盖关键用户场景

## 13. 技术债务管理

### 13.1 优先级
1. 安全问题：立即修复
2. 性能问题：高优先级
3. 代码质量：中优先级
4. 功能优化：低优先级

### 13.2 重构原则
- 小步迭代
- 保证测试通过
- 向后兼容
- 文档同步更新
