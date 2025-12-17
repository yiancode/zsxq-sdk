# zsxq-sdk-typescript

> 知识星球 TypeScript SDK - 轻量级、类型安全的 API 客户端

[![TypeScript Version](https://img.shields.io/badge/typescript-%3E%3D5.0-blue)](https://www.typescriptlang.org/)
[![License](https://img.shields.io/badge/license-MIT-green)](LICENSE)

## 特性

- **类型安全** - 完整的 TypeScript 类型定义
- **Builder 模式** - 灵活的客户端配置
- **模块化设计** - 按需使用各功能模块
- **自动重试** - 内置网络错误重试机制
- **零依赖** - 仅使用原生 fetch API

## 安装

```bash
npm install zsxq-sdk
# 或
yarn add zsxq-sdk
# 或
pnpm add zsxq-sdk
```

## 快速开始

```typescript
import { ZsxqClientBuilder } from 'zsxq-sdk';

// 创建客户端
const client = new ZsxqClientBuilder()
  .setToken(process.env.ZSXQ_TOKEN!)
  .setTimeout(10000)
  .setRetryCount(3)
  .build();

// 获取我的星球列表
const groups = await client.groups.list();
groups.forEach(g => console.log(`星球: ${g.name}`));

// 获取当前用户信息
const me = await client.users.self();
console.log(`用户: ${me.name}`);
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

// 获取星球标签
const hashtags = await client.groups.getHashtags(groupId);
```

### 话题操作

```typescript
// 获取话题列表
const topics = await client.topics.list(groupId);

// 带参数的话题列表
const topics = await client.topics.list(groupId, {
  count: 20,
  scope: 'all',
});

// 获取话题详情
const topic = await client.topics.get(topicId);

// 获取话题评论
const comments = await client.topics.getComments(topicId);
```

### 打卡管理

```typescript
// 获取打卡项目列表
const checkins = await client.checkins.list(groupId);

// 获取打卡统计
const stats = await client.checkins.getStatistics(groupId, checkinId);

// 获取打卡排行榜
const ranking = await client.checkins.getRankingList(groupId, checkinId);
```

### 训练营（打卡）

```typescript
// 创建训练营（有截止时间）
const checkin = await client.checkins.create(groupId, {
  title: '7天打卡挑战',           // 训练营标题
  text: '每天完成一个任务',        // 训练营描述
  checkin_days: 7,               // 打卡天数
  type: 'accumulated',           // 打卡类型: accumulated(累计) / continuous(连续)
  show_topics_on_timeline: false, // 是否在时间线展示
  validity: {
    long_period: false,
    expiration_time: '2025-12-31T23:59:59.000+0800'  // 截止时间
  }
});
console.log('创建成功:', checkin.checkin_id);

// 创建长期有效的训练营
const longTermCheckin = await client.checkins.create(groupId, {
  title: '每日学习打卡',
  text: '持续学习，每天进步',
  checkin_days: 21,
  type: 'accumulated',
  validity: { long_period: true }  // 长期有效
});

// 更新训练营
const updated = await client.checkins.update(groupId, checkinId, {
  title: '新标题',
  text: '更新后的描述'
});
```

### 排行榜

```typescript
// 获取星球排行榜
const ranking = await client.ranking.getGroupRanking(groupId);

// 获取积分排行榜
const scoreRanking = await client.ranking.getScoreRanking(groupId);

// 获取我的积分统计
const myStats = await client.ranking.getMyScoreStats(groupId);
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
    // 触发限流
    console.log('请求过于频繁');
  } else if (error instanceof ZsxqException) {
    // 其他 API 错误
    console.log(`错误码: ${error.code}, 信息: ${error.message}`);
  }
}
```

## 开发

```bash
# 安装依赖
npm install

# 构建
npm run build

# 运行测试
npm test

# 运行单个测试文件
npm test -- --testPathPattern="GroupsRequest"

# 代码检查
npm run lint
```

## 许可证

MIT License

---

**注意**: 本项目仅供学习和研究使用，请遵守知识星球的服务条款。
