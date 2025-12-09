# 缓存设计方案

> 为 zsxq-api 设计的 Redis 缓存方案

## 设计目标

1. **减少API调用** - 降低对知识星球原生API的请求频率
2. **提升响应速度** - 缓存命中时响应时间 ≤ 100ms
3. **容错降级** - Redis故障时自动降级为无缓存模式
4. **数据新鲜度** - 通过定时刷新保证数据时效性

## 缓存架构

```
┌─────────────────────────────────────────────────────────────┐
│                       请求处理流程                          │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   请求 ──▶ Service ──▶ CacheService ──▶ Redis              │
│                │              │                             │
│                │              ├─ 命中 ──▶ 返回缓存数据      │
│                │              │                             │
│                │              └─ 未命中                      │
│                │                   │                        │
│                └──────────────────▶│                        │
│                                    ▼                        │
│                            ZsxqClientService                │
│                                    │                        │
│                                    ▼                        │
│                          知识星球原生API                    │
│                                    │                        │
│                                    ▼                        │
│                         写入Redis缓存                       │
│                                    │                        │
│                                    ▼                        │
│                              返回数据                       │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

## 缓存键设计

### 键命名规范

```
{prefix}:{domain}:{resource_id}:{sub_resource}
```

- `prefix`: 应用前缀，默认 `zsxq`
- `domain`: 业务域，如 `planet`, `topic`, `checkin`
- `resource_id`: 资源ID
- `sub_resource`: 子资源类型

### 缓存键定义

```typescript
// src/common/constants/cache-keys.ts
export const CacheKeys = {
  // ==================== 星球相关 ====================

  /** 星球列表 */
  planetsList: () => 'zsxq:planets:list',

  /** 星球详情 */
  planetInfo: (planetId: string) => `zsxq:planet:${planetId}:info`,

  /** 星球话题列表 */
  planetTopics: (planetId: string, page: number) =>
    `zsxq:planet:${planetId}:topics:page:${page}`,

  /** 星球成员列表 */
  planetMembers: (planetId: string, page: number) =>
    `zsxq:planet:${planetId}:members:page:${page}`,

  // ==================== 打卡项目相关 ====================

  /** 打卡项目列表 */
  checkinsList: (planetId: string, scope: string) =>
    `zsxq:planet:${planetId}:checkins:${scope}`,

  /** 打卡项目详情 */
  checkinInfo: (checkinId: string) => `zsxq:checkin:${checkinId}:info`,

  /** 打卡项目统计 */
  checkinStats: (checkinId: string) => `zsxq:checkin:${checkinId}:stats`,

  /** 打卡每日统计 */
  checkinDailyStats: (checkinId: string, date: string) =>
    `zsxq:checkin:${checkinId}:daily:${date}`,

  /** 打卡排行榜 */
  checkinLeaderboard: (checkinId: string, type: string) =>
    `zsxq:checkin:${checkinId}:leaderboard:${type}`,

  /** 打卡话题列表 */
  checkinTopics: (checkinId: string) => `zsxq:checkin:${checkinId}:topics`,

  // ==================== 话题相关 ====================

  /** 话题详情 */
  topicInfo: (topicId: string) => `zsxq:topic:${topicId}:info`,

  // ==================== 用户相关 ====================

  /** 用户权限缓存 */
  userPermissions: (userId: string) => `zsxq:user:${userId}:permissions`,

  // ==================== 通配符模式 ====================

  /** 星球下所有缓存 */
  planetAll: (planetId: string) => `zsxq:planet:${planetId}:*`,

  /** 打卡项目下所有缓存 */
  checkinAll: (checkinId: string) => `zsxq:checkin:${checkinId}:*`,
};
```

## TTL策略

### 分层TTL设计

| 数据类型 | TTL | 定时刷新间隔 | 说明 |
|---------|-----|-------------|------|
| 星球列表 | 2小时 | 1小时 | 变化频率低 |
| 星球详情 | 2小时 | 1小时 | 基本不变 |
| 打卡项目列表 | 2小时 | 1小时 | 变化频率低 |
| 打卡项目统计 | 1小时 | 30分钟 | 需要相对准确 |
| 排行榜 | 1小时 | 30分钟 | 用户关注度高 |
| 每日统计 | 30分钟 | 15分钟 | 实时性要求较高 |
| 话题列表 | 10分钟 | 5分钟 | 更新频繁 |
| 用户权限 | 30分钟 | - | 登录时刷新 |

### TTL配置

```typescript
// src/config/cache.config.ts
export const CacheTTL = {
  // 星球相关
  PLANETS_LIST: 7200,      // 2小时
  PLANET_INFO: 7200,       // 2小时
  PLANET_TOPICS: 600,      // 10分钟
  PLANET_MEMBERS: 1800,    // 30分钟

  // 打卡项目相关
  CHECKINS_LIST: 7200,     // 2小时
  CHECKIN_INFO: 7200,      // 2小时
  CHECKIN_STATS: 3600,     // 1小时
  CHECKIN_DAILY_STATS: 1800, // 30分钟
  CHECKIN_LEADERBOARD: 3600, // 1小时
  CHECKIN_TOPICS: 600,     // 10分钟

  // 话题相关
  TOPIC_INFO: 3600,        // 1小时

  // 用户相关
  USER_PERMISSIONS: 1800,  // 30分钟

  // 默认值
  DEFAULT: 3600,           // 1小时
};
```

## CacheService 实现

### 接口定义

```typescript
// src/common/services/cache.service.ts
import { Injectable, OnModuleInit, Logger } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import Redis from 'ioredis';

@Injectable()
export class CacheService implements OnModuleInit {
  private readonly logger = new Logger(CacheService.name);
  private client: Redis | null = null;
  private enabled = true;

  constructor(private configService: ConfigService) {}

  async onModuleInit() {
    await this.initRedis();
  }

  private async initRedis() {
    try {
      this.client = new Redis({
        host: this.configService.get('REDIS_HOST', 'localhost'),
        port: this.configService.get('REDIS_PORT', 6379),
        password: this.configService.get('REDIS_PASSWORD', ''),
        db: this.configService.get('REDIS_DB', 0),
        connectTimeout: 5000,
        maxRetriesPerRequest: 3,
      });

      await this.client.ping();
      this.logger.log('Redis连接成功');
    } catch (error) {
      this.logger.warn(`Redis连接失败: ${error.message}, 将使用降级模式`);
      this.client = null;
      this.enabled = false;
    }
  }

  /**
   * 检查缓存是否可用
   */
  isEnabled(): boolean {
    return this.enabled && this.client !== null;
  }

  /**
   * 获取缓存
   */
  async get<T>(key: string): Promise<T | null> {
    if (!this.isEnabled()) return null;

    try {
      const value = await this.client.get(key);
      if (value) {
        this.logger.debug(`缓存命中: ${key}`);
        return JSON.parse(value);
      }
      this.logger.debug(`缓存未命中: ${key}`);
      return null;
    } catch (error) {
      this.logger.error(`获取缓存失败 ${key}: ${error.message}`);
      return null;
    }
  }

  /**
   * 设置缓存
   */
  async set(key: string, value: any, ttl?: number): Promise<boolean> {
    if (!this.isEnabled()) return false;

    try {
      const serialized = JSON.stringify(value);
      const expireTime = ttl || this.configService.get('CACHE_DEFAULT_TTL', 3600);
      await this.client.setex(key, expireTime, serialized);
      this.logger.debug(`缓存写入: ${key}, TTL: ${expireTime}s`);
      return true;
    } catch (error) {
      this.logger.error(`设置缓存失败 ${key}: ${error.message}`);
      return false;
    }
  }

  /**
   * 删除缓存
   */
  async delete(key: string): Promise<boolean> {
    if (!this.isEnabled()) return false;

    try {
      await this.client.del(key);
      return true;
    } catch (error) {
      this.logger.error(`删除缓存失败 ${key}: ${error.message}`);
      return false;
    }
  }

  /**
   * 批量删除（支持通配符）
   */
  async deletePattern(pattern: string): Promise<number> {
    if (!this.isEnabled()) return 0;

    try {
      const keys = await this.client.keys(pattern);
      if (keys.length > 0) {
        return await this.client.del(...keys);
      }
      return 0;
    } catch (error) {
      this.logger.error(`批量删除缓存失败 ${pattern}: ${error.message}`);
      return 0;
    }
  }

  /**
   * 检查键是否存在
   */
  async exists(key: string): Promise<boolean> {
    if (!this.isEnabled()) return false;

    try {
      return (await this.client.exists(key)) > 0;
    } catch (error) {
      this.logger.error(`检查缓存存在性失败 ${key}: ${error.message}`);
      return false;
    }
  }

  /**
   * 获取剩余TTL
   */
  async getTTL(key: string): Promise<number> {
    if (!this.isEnabled()) return -2;

    try {
      return await this.client.ttl(key);
    } catch (error) {
      this.logger.error(`获取TTL失败 ${key}: ${error.message}`);
      return -2;
    }
  }
}
```

### 带缓存的数据获取装饰器

```typescript
// src/common/decorators/cacheable.decorator.ts
import { CacheService } from '../services/cache.service';
import { CacheTTL } from '../../config/cache.config';

interface CacheableOptions {
  keyGenerator: (...args: any[]) => string;
  ttl?: number;
}

export function Cacheable(options: CacheableOptions) {
  return function (
    target: any,
    propertyKey: string,
    descriptor: PropertyDescriptor,
  ) {
    const originalMethod = descriptor.value;

    descriptor.value = async function (...args: any[]) {
      const cacheService = this.cacheService as CacheService;
      const cacheKey = options.keyGenerator(...args);

      // 尝试从缓存获取
      if (cacheService?.isEnabled()) {
        const cached = await cacheService.get(cacheKey);
        if (cached) {
          return cached;
        }
      }

      // 调用原方法
      const result = await originalMethod.apply(this, args);

      // 添加缓存时间戳
      if (result && typeof result === 'object') {
        result.cachedAt = new Date().toISOString();
      }

      // 写入缓存
      if (cacheService?.isEnabled() && result) {
        await cacheService.set(cacheKey, result, options.ttl);
      }

      return result;
    };

    return descriptor;
  };
}
```

## 定时刷新机制

### 使用 @nestjs/schedule

```typescript
// src/common/services/cache-scheduler.service.ts
import { Injectable, Logger } from '@nestjs/common';
import { Cron, CronExpression } from '@nestjs/schedule';
import { CacheService } from './cache.service';
import { ZsxqClientService } from '@/zsxq-client/zsxq-client.service';
import { CacheKeys } from '../constants/cache-keys';

@Injectable()
export class CacheSchedulerService {
  private readonly logger = new Logger(CacheSchedulerService.name);

  constructor(
    private cacheService: CacheService,
    private zsxqClient: ZsxqClientService,
  ) {}

  /**
   * 每小时刷新星球列表
   */
  @Cron(CronExpression.EVERY_HOUR)
  async refreshPlanetsList() {
    this.logger.log('开始刷新星球列表缓存');
    try {
      // 实现刷新逻辑
      this.logger.log('星球列表缓存刷新完成');
    } catch (error) {
      this.logger.error(`星球列表缓存刷新失败: ${error.message}`);
    }
  }

  /**
   * 每30分钟刷新排行榜
   */
  @Cron('0 */30 * * * *')
  async refreshLeaderboards() {
    this.logger.log('开始刷新排行榜缓存');
    try {
      // 实现刷新逻辑
      this.logger.log('排行榜缓存刷新完成');
    } catch (error) {
      this.logger.error(`排行榜缓存刷新失败: ${error.message}`);
    }
  }

  /**
   * 每5分钟刷新话题列表
   */
  @Cron('0 */5 * * * *')
  async refreshTopics() {
    this.logger.log('开始刷新话题列表缓存');
    try {
      // 实现刷新逻辑
      this.logger.log('话题列表缓存刷新完成');
    } catch (error) {
      this.logger.error(`话题列表缓存刷新失败: ${error.message}`);
    }
  }
}
```

## 缓存使用示例

### Service层使用

```typescript
// src/modules/checkin/checkin.service.ts
@Injectable()
export class CheckinService {
  constructor(
    private cacheService: CacheService,
    private zsxqClient: ZsxqClientService,
  ) {}

  async getLeaderboard(
    checkinId: string,
    type: 'continuous' | 'accumulated' = 'continuous',
    limit: number = 10,
  ) {
    const cacheKey = CacheKeys.checkinLeaderboard(checkinId, type);

    // 尝试从缓存获取
    const cached = await this.cacheService.get(cacheKey);
    if (cached) {
      return this.formatLeaderboard(cached, limit);
    }

    // 调用知识星球API
    const rawData = await this.zsxqClient.get(
      `/v2/groups/${this.groupId}/checkins/${checkinId}/ranking_list`,
      { params: { type, index: 0 } },
    );

    // 格式化数据
    const formatted = this.formatLeaderboard(rawData.resp_data, limit);
    formatted.cachedAt = new Date().toISOString();

    // 写入缓存
    await this.cacheService.set(cacheKey, formatted, CacheTTL.CHECKIN_LEADERBOARD);

    return formatted;
  }

  private formatLeaderboard(rawData: any, limit: number) {
    const rankings = rawData.ranking_list?.slice(0, limit) || [];
    return {
      type: rawData.type,
      rankings: rankings.map((item: any) => ({
        rank: item.rankings,
        user: {
          userId: item.user.user_id,
          name: item.user.name,
          avatar: item.user.avatar_url,
        },
        days: item.checkined_days,
      })),
      total: rawData.ranking_list?.length || 0,
      userRank: rawData.user_specific ? {
        rank: rawData.user_specific.rankings,
        days: rawData.user_specific.checkined_days,
      } : null,
    };
  }
}
```

## 容错与降级

### 降级策略

```typescript
// 缓存不可用时的处理
async getWithFallback<T>(
  cacheKey: string,
  fetchFn: () => Promise<T>,
  ttl?: number,
): Promise<T> {
  // 尝试从缓存获取
  if (this.cacheService.isEnabled()) {
    const cached = await this.cacheService.get<T>(cacheKey);
    if (cached) {
      return cached;
    }
  }

  // 缓存未命中或不可用，直接调用API
  const data = await fetchFn();

  // 尝试写入缓存（失败也不影响返回）
  if (this.cacheService.isEnabled()) {
    this.cacheService.set(cacheKey, data, ttl).catch((err) => {
      this.logger.warn(`缓存写入失败: ${err.message}`);
    });
  }

  return data;
}
```

### 健康检查

```typescript
// src/common/controllers/health.controller.ts
@Get('cache')
async cacheHealth() {
  return {
    redis: {
      enabled: this.cacheService.isEnabled(),
      status: this.cacheService.isEnabled() ? 'connected' : 'disconnected',
    },
  };
}
```

## 监控指标

### 关键指标

- **缓存命中率**: `cache_hits / (cache_hits + cache_misses)`
- **平均响应时间**: 缓存命中 vs 缓存未命中
- **Redis内存使用**: 监控缓存大小
- **错误率**: 缓存操作失败次数

### 日志规范

```typescript
// DEBUG级别：缓存命中/未命中
this.logger.debug(`缓存命中: ${key}`);
this.logger.debug(`缓存未命中: ${key}`);

// INFO级别：定时刷新
this.logger.log('开始刷新排行榜缓存');
this.logger.log('排行榜缓存刷新完成');

// WARN级别：Redis连接问题
this.logger.warn('Redis连接失败，使用降级模式');

// ERROR级别：缓存操作异常
this.logger.error(`设置缓存失败: ${error.message}`);
```

## 配置示例

### 环境变量

```bash
# .env
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=
REDIS_DB=0
CACHE_DEFAULT_TTL=3600
CACHE_ENABLED=true
```

### NestJS模块配置

```typescript
// src/common/common.module.ts
@Module({
  imports: [
    ScheduleModule.forRoot(),
  ],
  providers: [
    CacheService,
    CacheSchedulerService,
  ],
  exports: [CacheService],
})
export class CommonModule {}
```
