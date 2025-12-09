# zsxq-sdk

> 知识星球 TypeScript SDK - 类型安全、开箱即用

[![Node.js Version](https://img.shields.io/badge/node-%3E%3D18.0.0-brightgreen)](https://nodejs.org/)
[![TypeScript](https://img.shields.io/badge/typescript-%5E5.3.3-blue)](https://www.typescriptlang.org/)
[![License](https://img.shields.io/badge/license-MIT-green)](LICENSE)

## 特性

- **类型安全** - 完整的 TypeScript 类型定义
- **Builder 模式** - 灵活的客户端配置
- **模块化设计** - 按需使用各功能模块
- **自动重试** - 内置网络错误重试机制
- **NestJS 集成** - 可选的 NestJS 模块支持

## 安装

```bash
npm install zsxq-sdk
```

## 快速开始

```typescript
import { ZsxqClientBuilder } from 'zsxq-sdk';

// 创建客户端
const client = new ZsxqClientBuilder()
  .setToken(process.env.ZSXQ_TOKEN!)
  .setTimeout(10000)
  .setRetry(3)
  .build();

// 获取我的星球列表
const groups = await client.groups.list();

// 获取星球话题
const topics = await client.topics.list(groupId, { count: 20 });

// 获取当前用户信息
const me = await client.users.getSelf();
```

## 核心功能

### 星球管理

```typescript
// 获取星球列表
const groups = await client.groups.list();

// 获取星球详情
const group = await client.groups.get(groupId);

// 获取星球统计
const stats = await client.groups.getStatistics(groupId);
```

### 话题操作

```typescript
// 获取话题列表
const topics = await client.topics.list(groupId, {
  count: 20,
  scope: 'all', // 'all' | 'digests' | 'by_owner'
});

// 获取话题详情
const topic = await client.topics.get(topicId);

// 获取话题评论
const comments = await client.topics.getComments(topicId);
```

### 打卡功能

```typescript
// 获取打卡项目列表
const checkins = await client.checkins.list(groupId, { scope: 'ongoing' });

// 获取打卡统计
const stats = await client.checkins.getStatistics(groupId, checkinId);

// 获取排行榜
const ranking = await client.checkins.getRankingList(groupId, checkinId, {
  type: 'continuous',
});
```

## 错误处理

```typescript
import {
  ZsxqException,
  TokenExpiredException,
  RateLimitException,
} from 'zsxq-sdk';

try {
  const groups = await client.groups.list();
} catch (error) {
  if (error instanceof TokenExpiredException) {
    // Token 过期，需要重新获取
    console.log('Token 已过期');
  } else if (error instanceof RateLimitException) {
    // 请求频率限制
    await sleep(error.retryAfter || 60000);
  } else if (error instanceof ZsxqException) {
    // 其他 API 错误
    console.error(`错误码: ${error.code}, 信息: ${error.message}`);
  }
}
```

## NestJS 集成

```typescript
import { Module } from '@nestjs/common';
import { ZsxqModule } from 'zsxq-sdk/nestjs';

@Module({
  imports: [
    ZsxqModule.forRoot({
      token: process.env.ZSXQ_TOKEN!,
      timeout: 10000,
      retry: 3,
    }),
  ],
})
export class AppModule {}
```

在服务中注入：

```typescript
import { Injectable } from '@nestjs/common';
import { ZsxqClient } from 'zsxq-sdk';
import { InjectZsxqClient } from 'zsxq-sdk/nestjs';

@Injectable()
export class PlanetService {
  constructor(
    @InjectZsxqClient()
    private readonly zsxq: ZsxqClient,
  ) {}

  async getMyPlanets() {
    return this.zsxq.groups.list();
  }
}
```

## 文档

详细文档请查看 [docs/](docs/README.md)：

- [快速开始](docs/guides/quick-start.md) - 入门教程
- [认证指南](docs/guides/authentication.md) - Token 获取与管理
- [API 参考](docs/api/client.md) - 完整 API 文档
- [错误处理](docs/guides/error-handling.md) - 错误处理最佳实践
- [NestJS 集成](docs/guides/nestjs-integration.md) - NestJS 模块使用
- [原生 API](docs/reference/native-api.md) - 知识星球原生 API 参考

## Token 获取

通过浏览器开发者工具获取 Token：

1. 打开 [知识星球网页版](https://wx.zsxq.com)
2. 登录你的账号
3. 按 `F12` 打开开发者工具
4. 切换到 `Network` 选项卡
5. 刷新页面，找到 `api.zsxq.com` 请求
6. 在 Cookie 中提取 `zsxq_access_token` 的值

详细说明请查看 [认证指南](docs/guides/authentication.md)

## 开发

```bash
# 安装依赖
npm install

# 开发模式
npm run start:dev

# 构建
npm run build

# 测试
npm run test

# 代码检查
npm run lint
```

## 许可证

MIT License - 详见 [LICENSE](LICENSE) 文件

---

**注意**: 本项目仅供学习和研究使用，请遵守知识星球的服务条款。
