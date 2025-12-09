# 打卡 API

> CheckinRequest 类的 API 文档

## 概述

`CheckinRequest` 提供打卡项目（训练营）相关的所有操作。

```typescript
// 通过客户端访问
const checkins = client.checkins;
```

---

## 方法列表

### list(groupId, scope?)

获取星球的打卡项目列表。

**签名**:
```typescript
list(groupId: string | number, scope?: CheckinScope): Promise<Checkin[]>
```

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| groupId | string \| number | 是 | 星球 ID |
| scope | CheckinScope | 否 | 项目状态范围 |

**范围类型**:
```typescript
type CheckinScope = 'ongoing' | 'closed' | 'over';
// ongoing: 进行中
// closed: 已关闭
// over: 已结束
```

**示例**:
```typescript
// 获取进行中的打卡项目
const ongoingCheckins = await client.checkins.list(groupId, 'ongoing');

// 获取所有已结束的项目
const overCheckins = await client.checkins.list(groupId, 'over');
```

---

### get(groupId, checkinId)

获取打卡项目详情。

**签名**:
```typescript
get(groupId: string | number, checkinId: string | number): Promise<CheckinDetail>
```

**返回值**:
```typescript
interface CheckinDetail {
  is_valid_member: boolean;  // 是否有效成员
  checkin: Checkin;
}
```

---

### getStatistics(groupId, checkinId)

获取打卡项目统计数据。

**签名**:
```typescript
getStatistics(groupId: string | number, checkinId: string | number): Promise<CheckinStatistics>
```

**返回值**:
```typescript
interface CheckinStatistics {
  joined_count: number;      // 参与人数
  completed_count: number;   // 完成人数
  checkined_count: number;   // 打卡次数
  ranking_list: RankingItem[]; // 排行预览
}
```

**示例**:
```typescript
const stats = await client.checkins.getStatistics(groupId, checkinId);
console.log(`参与人数: ${stats.joined_count}`);
console.log(`打卡次数: ${stats.checkined_count}`);
```

---

### getDailyStats(groupId, checkinId, date?)

获取打卡每日统计。

**签名**:
```typescript
getDailyStats(groupId: string | number, checkinId: string | number, date?: string): Promise<DailyStats>
```

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| date | string | 否 | 日期（ISO 8601），默认今天 |

**返回值**:
```typescript
interface DailyStats {
  checkined_count: number;  // 当日打卡人数
}
```

---

### getRankingList(groupId, checkinId, type?, index?)

获取打卡排行榜。

**签名**:
```typescript
getRankingList(groupId: string | number, checkinId: string | number, type?: RankingType, index?: number): Promise<RankingList>
```

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| type | RankingType | 否 | 排行榜类型 |
| index | number | 否 | 分页索引 |

**排行榜类型**:
```typescript
type RankingType = 'continuous' | 'accumulated';
// continuous: 连续打卡天数
// accumulated: 累计打卡天数
```

**返回值**:
```typescript
interface RankingList {
  ranking_list: RankingItem[];
}

interface RankingItem {
  user: User;
  rankings: number;      // 排名
  checkined_days: number; // 打卡天数
}
```

**示例**:
```typescript
// 连续打卡排行
const continuous = await client.checkins.getRankingList(groupId, checkinId, 'continuous');

// 累计打卡排行
const accumulated = await client.checkins.getRankingList(groupId, checkinId, 'accumulated');
```

---

### getTopics(groupId, checkinId, count?)

获取打卡话题列表。

**签名**:
```typescript
getTopics(groupId: string | number, checkinId: string | number, count?: number): Promise<Topic[]>
```

---

### getJoinedUsers(groupId, checkinId, filter?, count?)

获取参与打卡的用户列表。

**签名**:
```typescript
getJoinedUsers(groupId: string | number, checkinId: string | number, filter?: JoinedUserFilter, count?: number): Promise<JoinedUser[]>
```

**过滤类型**:
```typescript
type JoinedUserFilter = 'checkined' | 'uncheckined';
// checkined: 今日已打卡
// uncheckined: 今日未打卡
```

---

## 数据模型

### Checkin

```typescript
interface Checkin {
  checkin_id: number;
  group: Group;
  owner: User;
  name: string;
  description: string;
  cover_url?: string;
  status: 'ongoing' | 'closed' | 'over';
  create_time: string;
  begin_time: string;
  end_time: string;

  // 规则设置
  rules: {
    minimum_text_length?: number;
    require_image?: boolean;
    daily_checkin_limit?: number;
  };

  // 统计预览
  statistics?: {
    joined_count: number;
    checkined_count: number;
  };
}
```

### JoinedUser

```typescript
interface JoinedUser {
  member: User;
  join_time: string;
  last_checkin_time?: string;
  continuous_days?: number;
  accumulated_days?: number;
}
```

---

## 使用场景

### 获取打卡项目概览

```typescript
async function getCheckinOverview(groupId: string, checkinId: string) {
  const [detail, stats, ranking] = await Promise.all([
    client.checkins.get(groupId, checkinId),
    client.checkins.getStatistics(groupId, checkinId),
    client.checkins.getRankingList(groupId, checkinId, 'continuous')
  ]);

  return {
    name: detail.checkin.name,
    participants: stats.joined_count,
    totalCheckins: stats.checkined_count,
    topRankers: ranking.ranking_list.slice(0, 3)
  };
}
```

### 获取今日未打卡用户

```typescript
async function getTodayUncheckinedUsers(groupId: string, checkinId: string) {
  const users = await client.checkins.getJoinedUsers(
    groupId,
    checkinId,
    'uncheckined',
    100
  );
  return users;
}
```

---

## 相关文档

- [API 映射](../design/api-mapping.md#打卡相关-checkinrequest)
- [星球 API](group.md)
- [话题 API](topic.md)
