# 快速开始

> 5 分钟上手 zsxq-sdk

## 安装

```bash
npm install zsxq-sdk
# 或
yarn add zsxq-sdk
# 或
pnpm add zsxq-sdk
```

---

## 基础使用

### 1. 获取 Token

首先需要获取知识星球的认证 Token。可以通过以下方式获取：

- 浏览器开发者工具抓包
- 手机抓包工具（如 Charles、Fiddler）

Token 格式示例：`D047A423-A...169922C77C`

### 2. 初始化客户端

```typescript
import { ZsxqClientBuilder } from 'zsxq-sdk';

const client = new ZsxqClientBuilder()
  .setToken('your-token-here')
  .build();
```

### 3. 调用 API

```typescript
// 获取我的星球列表
const groups = await client.groups.list();
console.log(groups);

// 获取星球话题
const topics = await client.topics.list(groups[0].group_id);
console.log(topics);
```

---

## 完整示例

```typescript
import { ZsxqClientBuilder, ZsxqException } from 'zsxq-sdk';

async function main() {
  // 初始化客户端
  const client = new ZsxqClientBuilder()
    .setToken('your-token')
    .setTimeout(5000)
    .setRetry(3)
    .build();

  try {
    // 获取当前用户
    const me = await client.users.getSelf();
    console.log(`欢迎, ${me.user.name}`);

    // 获取我的星球
    const groups = await client.groups.list();
    console.log(`你加入了 ${groups.length} 个星球`);

    // 获取第一个星球的最新话题
    if (groups.length > 0) {
      const topics = await client.topics.list(groups[0].group_id, {
        count: 10,
        scope: 'all'
      });

      topics.forEach(topic => {
        console.log(`- ${topic.talk?.text?.slice(0, 50)}...`);
      });
    }
  } catch (error) {
    if (error instanceof ZsxqException) {
      console.error(`SDK 错误: ${error.code} - ${error.message}`);
    } else {
      throw error;
    }
  }
}

main();
```

---

## 配置选项

```typescript
const client = new ZsxqClientBuilder()
  // 必填：认证 Token
  .setToken('your-token')

  // 可选：API 基础 URL（默认: https://api.zsxq.com）
  .setBaseUrl('https://api.zsxq.com')

  // 可选：请求超时（默认: 10000ms）
  .setTimeout(5000)

  // 可选：重试次数（默认: 3）
  .setRetry(3)

  // 可选：设备 ID
  .setDeviceId('your-device-id')

  // 可选：App 版本（默认: 2.83.0）
  .setAppVersion('2.83.0')

  .build();
```

---

## 常用操作

### 获取星球信息

```typescript
// 获取星球详情
const group = await client.groups.get(groupId);
console.log(group.name, group.description);

// 获取星球统计
const stats = await client.groups.getStatistics(groupId);
console.log(`话题数: ${stats.topics_count}`);
```

### 获取话题

```typescript
// 获取最新话题
const topics = await client.topics.list(groupId, { count: 20 });

// 获取精华话题
const digests = await client.topics.list(groupId, { scope: 'digests' });

// 只看星主
const ownerTopics = await client.topics.list(groupId, { scope: 'by_owner' });
```

### 获取打卡项目

```typescript
// 获取进行中的打卡
const checkins = await client.checkins.list(groupId, 'ongoing');

// 获取打卡统计
const stats = await client.checkins.getStatistics(groupId, checkinId);
console.log(`参与人数: ${stats.joined_count}`);

// 获取排行榜
const ranking = await client.checkins.getRankingList(groupId, checkinId, 'continuous');
```

---

## 错误处理

```typescript
import {
  ZsxqException,
  TokenExpiredException,
  RateLimitException
} from 'zsxq-sdk';

try {
  const groups = await client.groups.list();
} catch (error) {
  if (error instanceof TokenExpiredException) {
    // Token 过期，需要重新获取
    console.log('Token 已过期');
  } else if (error instanceof RateLimitException) {
    // 请求过于频繁
    console.log('请求频率过高，请稍后重试');
  } else if (error instanceof ZsxqException) {
    // 其他 SDK 错误
    console.error(`错误码: ${error.code}, 消息: ${error.message}`);
  } else {
    throw error;
  }
}
```

---

## TypeScript 支持

SDK 提供完整的 TypeScript 类型定义：

```typescript
import {
  ZsxqClient,
  ZsxqConfig,
  Group,
  Topic,
  User,
  Checkin
} from 'zsxq-sdk';

// 类型安全的 API 调用
const groups: Group[] = await client.groups.list();
const topic: Topic = await client.topics.get(topicId);
```

---

## 下一步

- [认证指南](authentication.md) - 了解更多认证细节
- [客户端 API](../api/client.md) - 完整 API 文档
- [错误处理](error-handling.md) - 错误处理最佳实践
- [NestJS 集成](nestjs-integration.md) - 在 NestJS 中使用
