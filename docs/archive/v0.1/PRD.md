# 知识星球API封装服务 PRD

> 基于HAR抓包数据分析，整理知识星球原生API接口，作为项目开发参考

## 1. 项目概述

### 1.1 项目目标

封装知识星球原生API，提供标准化RESTful服务，支持：
- 星球管理（获取星球信息、成员、统计等）
- 话题管理（获取话题列表、详情、评论、点赞等）
- 用户管理（用户信息、关注、足迹等）
- 打卡项目（打卡列表、统计、排行榜等）
- Dashboard运营数据（收入、积分榜、权限等）
- 标签/专栏管理

### 1.2 技术栈

- NestJS + TypeORM + PostgreSQL + Redis
- 基于 `https://api.zsxq.com` 原生API封装

---

## 2. API接口清单

### 2.1 用户模块 (User)

#### V2接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/v2/users/self/groups` | 获取当前用户加入的星球列表 |
| GET | `/v2/users/self/groups/applied_groups` | 获取已申请的星球 |
| GET | `/v2/users/self/merchant_coupons` | 获取商家优惠券 |
| GET | `/v2/users/self/recommendations/users` | 获取推荐用户 |
| GET | `/v2/users/self/groups/{group_id}/inviter` | 获取邀请人信息 |
| GET | `/v2/users/self/groups/{group_id}/checkins/{checkin_id}/checkined_dates` | 获取打卡日期 |
| GET | `/v2/users/self/groups/{group_id}/checkins/{checkin_id}/statistics` | 获取个人打卡统计 |
| GET | `/v2/users/self/groups/{group_id}/checkins/{checkin_id}/topics` | 获取个人打卡话题 |
| GET | `/v2/users/{user_id}/created_groups` | 获取用户创建的星球 |
| GET | `/v2/users/{user_id}/footprints` | 获取用户足迹 |
| GET | `/v2/users/{user_id}/footprints/groups` | 获取用户在星球的足迹 |

#### V3接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/v3/users/self` | 获取当前用户信息 |
| GET | `/v3/users/self/remarks` | 获取用户备注 |
| GET | `/v3/users/{user_id}` | 获取指定用户信息 |
| GET | `/v3/users/{user_id}/statistics` | 获取用户统计数据 |
| GET | `/v3/users/{user_id}/avatar_url` | 获取用户头像URL |

---

### 2.2 星球模块 (Group/Planet)

#### 星球基础信息

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/v2/groups` | 获取用户加入的所有星球列表 |
| GET | `/v2/groups/{group_id}` | 获取星球详情 |
| GET | `/v2/groups/unread_topics_count` | 获取未读话题数 |
| GET | `/v2/groups/upgradable_groups` | 获取可升级的星球 |
| GET | `/v2/groups/recommendations` | 获取推荐星球 |
| GET | `/v2/pk_groups/{group_id}` | 获取PK星球信息 |
| GET | `/v2/pk_groups/{group_id}/records` | 获取PK记录 |

**参数说明**:
- `count`: 返回数量（默认3）
- `index`: 分页索引
- `source`: 来源（如 `GroupListView`）

#### 星球排行榜 (V3)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/v3/groups/ranking_list` | 获取星球排行榜 |
| GET | `/v3/groups/{group_id}/ranking_list/statistics` | 获取星球排行统计 |

**type参数值**:
- `group_fortune_list`: 财富榜
- `paid_group_active_list`: 付费活跃榜
- `new_star_list`: 新星榜
- `group_sales_list`: 销量榜

#### 星球成员

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/v2/groups/{group_id}/members/{user_id}` | 获取成员信息 |
| GET | `/v2/groups/{group_id}/members/{user_id}/summary` | 获取成员概要 |
| GET | `/v2/groups/{group_id}/role_members` | 获取角色成员列表 |

**role参数**:
- `owner`: 星主
- `partners`: 合伙人
- `guests`: 嘉宾

#### 星球统计

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/v2/groups/{group_id}/statistics` | 获取星球统计 |
| GET | `/v2/groups/{group_id}/distribution` | 获取分销信息 |
| GET | `/v2/groups/{group_id}/renewal` | 获取续费信息 |

#### 星球菜单

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/v2/groups/{group_id}/menus` | 获取星球菜单 |
| GET | `/v2/groups/{group_id}/menus/last_read_time` | 获取最后阅读时间 |
| PUT | `/v2/groups/{group_id}/menus/last_read_time` | 更新最后阅读时间 |

---

### 2.3 话题模块 (Topic)

#### 话题列表

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/v2/groups/{group_id}/topics` | 获取星球话题列表 |

**参数说明**:
| 参数 | 类型 | 说明 |
|------|------|------|
| count | integer | 返回数量 |
| begin_time | string | 开始时间（ISO8601，需URL编码） |
| end_time | string | 结束时间（ISO8601，需URL编码） |
| scope | string | 范围：all/digests/by_owner |
| direction | string | 方向：backward/forward |
| with_invisibles | boolean | 是否包含不可见内容 |

#### 话题详情

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/v2/topics/{topic_id}` | 获取话题详情 |
| GET | `/v2/topics/{topic_id}/info` | 获取话题基础信息 |
| GET | `/v2/topics/{topic_id}/comments` | 获取话题评论 |
| GET | `/v2/topics/{topic_id}/rewards` | 获取话题打赏 |
| GET | `/v2/topics/{topic_id}/recommendations` | 获取相关推荐 |

**评论参数**:
| 参数 | 类型 | 说明 |
|------|------|------|
| with_sticky | boolean | 是否包含置顶评论 |
| sort | string | 排序：asc/desc |
| count | integer | 返回数量 |
| sort_type | string | 排序类型：by_create_time |

---

### 2.4 标签模块 (Hashtag)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/v2/groups/{group_id}/hashtags` | 获取星球标签列表 |
| GET | `/v2/hashtags/{hashtag_id}/topics` | 获取标签下的话题 |

**参数说明**:
| 参数 | 类型 | 说明 |
|------|------|------|
| count | integer | 返回数量（默认10） |

---

### 2.5 专栏模块 (Column)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/v2/groups/{group_id}/columns` | 获取专栏列表 |
| GET | `/v2/groups/{group_id}/columns/summary` | 获取专栏概要 |
| GET | `/v2/groups/{group_id}/columns/{column_id}/topics` | 获取专栏话题 |

---

### 2.6 打卡模块 (Checkin)

#### 打卡项目

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/v2/groups/{group_id}/checkins` | 获取打卡项目列表 |
| GET | `/v2/groups/{group_id}/checkins/{checkin_id}` | 获取打卡项目详情 |

**scope参数**:
- `ongoing`: 进行中
- `closed`: 已关闭
- `over`: 已结束

#### 打卡统计

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/v2/groups/{group_id}/checkins/{checkin_id}/statistics` | 获取打卡统计 |
| GET | `/v2/groups/{group_id}/checkins/{checkin_id}/statistics/daily` | 获取每日统计 |
| GET | `/v2/groups/{group_id}/checkins/{checkin_id}/topics` | 获取打卡话题 |

#### 打卡排行榜

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/v2/groups/{group_id}/checkins/{checkin_id}/ranking_list` | 获取排行榜 |
| GET | `/v2/groups/{group_id}/checkins/{checkin_id}/joined_users` | 获取参与用户 |

**排行榜type参数**:
- `continuous`: 连续打卡天数
- `accumulated`: 累计打卡次数

**参与用户filter参数**:
- `checkined`: 已打卡
- `uncheckined`: 未打卡

---

### 2.7 Dashboard模块（星主专用）

#### 运营概览

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/v2/dashboard/groups/{group_id}/overview` | 获取运营概览 |
| GET | `/v2/dashboard/groups/{group_id}/incomes/overview` | 获取收入概览 |
| GET | `/v2/dashboard/groups/{group_id}/privileges` | 获取权限设置 |

#### 积分榜

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/v2/dashboard/groups/{group_id}/scoreboard/settings` | 获取积分榜设置 |
| GET | `/v2/dashboard/groups/{group_id}/scoreboard/statistics/self` | 获取个人积分统计 |
| GET | `/v2/dashboard/groups/{group_id}/scoreboard/ranking_list` | 获取积分排行榜 |

**ranking_list type参数**:
- `last_month`: 上月排行
- `this_month`: 本月排行
- `all_time`: 总排行

---

### 2.8 其他接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/v2/url_details` | 获取URL详情 |

---

## 3. 数据模型

### 3.1 星球 (Group)

```typescript
interface Group {
  group_id: number;           // 星球ID
  number: number;             // 星球编号
  name: string;               // 星球名称
  description: string;        // 星球描述
  background_url: string;     // 背景图URL
  type: 'free' | 'pay';       // 星球类型
  risk_level: string;         // 风险等级
  partner_ids: number[];      // 合伙人ID列表
  admin_ids: number[];        // 管理员ID列表
  guest_ids: number[];        // 嘉宾ID列表
  owner: User;                // 星主信息
  policies: GroupPolicies;    // 策略设置
  statistics: GroupStats;     // 统计数据
  user_specific?: UserGroupInfo; // 用户特定信息
}

interface GroupPolicies {
  need_examine: boolean;      // 是否需要审核
  allow_join: boolean;        // 是否允许加入
  enable_iap_renew_group: boolean; // 是否开启IAP续费
  renewal: {
    advance_discounted_percentage: number;  // 提前续费折扣
    grace_discounted_percentage: number;    // 宽限期折扣
    discounted_percentage: number;          // 常规折扣
  };
  payment: {
    amount: number;           // 价格（单位：分）
    duration: string;         // 时长：1Y/infinite
    mode: 'deadline' | 'period'; // 模式
    begin_time?: string;
    end_time?: string;
  };
}

interface GroupStats {
  members: { count: number };
}
```

### 3.2 用户 (User)

```typescript
interface User {
  user_id: number;            // 用户ID
  name: string;               // 用户名
  alias?: string;             // 别名
  avatar_url: string;         // 头像URL
  description?: string;       // 个人简介
  location?: string;          // 位置
  number?: number;            // 用户编号
}
```

### 3.3 话题 (Topic)

```typescript
interface Topic {
  topic_id: number;           // 话题ID
  topic_uid: string;          // 话题UID
  group: GroupBrief;          // 所属星球
  type: 'talk' | 'q&a' | 'task'; // 话题类型
  talk?: TalkContent;         // 普通话题内容
  question?: QuestionContent; // 问答内容
  create_time: string;        // 创建时间
  latest_likes: Like[];       // 最新点赞
  likes_count?: number;
  comments_count?: number;
  rewards_count?: number;
  reading_count?: number;
  readers_count?: number;
  digested?: boolean;         // 是否精华
  sticky?: boolean;           // 是否置顶
}

interface TalkContent {
  owner: User;                // 作者
  text: string;               // 文本内容（含特殊标记）
  images?: Image[];           // 图片列表
  files?: File[];             // 文件列表
  links?: Link[];             // 链接列表
}

interface Image {
  image_id: number;
  type: 'jpg' | 'png' | 'gif';
  thumbnail: ImageVariant;
  large: ImageVariant;
  original: ImageVariant;
}

interface ImageVariant {
  url: string;
  width: number;
  height: number;
  size?: number;
}
```

### 3.4 打卡项目 (Checkin)

```typescript
interface Checkin {
  checkin_id: number;         // 打卡项目ID
  name: string;               // 项目名称
  description?: string;       // 项目描述
  start_time: string;         // 开始时间
  end_time?: string;          // 结束时间
  background?: {
    image_url: string;
  };
  users_count: number;        // 参与人数
  checkined_count: number;    // 打卡总次数
  current_continuous_days?: number; // 当前连续天数
  rules?: string;             // 打卡规则
  create_time: string;
}

interface CheckinRanking {
  rankings: number;           // 排名
  user: User;
  checkined_days: number;     // 打卡天数
}

interface CheckinStatistics {
  users_count: number;
  checkined_count: number;
  today_count: number;
  continuous_rate: number;
}
```

### 3.5 标签 (Hashtag)

```typescript
interface Hashtag {
  hashtag_id: number;
  name: string;
  topics_count?: number;
}
```

### 3.6 专栏 (Column)

```typescript
interface Column {
  column_id: number;
  name: string;
  description?: string;
  topics_count: number;
  cover_url?: string;
}
```

---

## 4. 响应格式

### 4.1 成功响应

```json
{
  "succeeded": true,
  "resp_data": {
    // 实际数据
  }
}
```

### 4.2 错误响应

```json
{
  "succeeded": false,
  "error": {
    "code": 10001,
    "message": "错误描述"
  }
}
```

### 4.3 常见错误码

| 错误码 | 说明 |
|--------|------|
| 10001 | 通用错误 |
| 10002 | 参数错误 |
| 20001 | Token无效 |
| 20002 | Token过期 |
| 30001 | 权限不足 |
| 40001 | 资源不存在 |

---

## 5. 认证机制

### 5.1 Token格式

```
Authorization: UUID_HASH
```

示例: `5a8c9b2f-1234-5678-9abc-def012345678_abc123def456789`

### 5.2 请求头要求

```http
Authorization: {token}
User-Agent: xiaomiquan/5.28.1 (iPhone; iOS 14.7.1; Scale/3.00)
Content-Type: application/json
Accept: application/json, text/plain, */*
x-request-id: {random-uuid}
```

---

## 6. 分页机制

### 6.1 时间戳分页

大部分列表接口使用时间戳进行分页：

- `begin_time`: 获取此时间之后的数据
- `end_time`: 获取此时间之前的数据
- `direction`: backward（向前翻页）/ forward（向后翻页）

时间格式示例: `2025-12-09T16:17:52.272+0800`（需URL编码）

### 6.2 索引分页

部分接口使用索引分页：

- `index`: 起始索引（从0开始）
- `count`: 每页数量

---

## 7. 特殊字段说明

### 7.1 话题文本中的特殊标记

话题文本 `text` 字段中可能包含特殊标记：

```xml
<!-- 链接 -->
<e type="web" href="{url_encoded}" title="{title_encoded}" />

<!-- 标签 -->
<e type="hashtag" hid="{hashtag_id}" title="{title_encoded}" />

<!-- @用户 -->
<e type="mention" uid="{user_id}" title="{name}" />
```

### 7.2 图片URL Token

图片URL包含临时访问Token，有过期时间：

```
https://images.zsxq.com/{key}?e={expire_timestamp}&token={access_token}
```

---

## 8. 优先级规划

### P0 - 核心功能

1. 用户认证与信息获取
2. 星球列表与详情
3. 话题列表与详情
4. 话题评论

### P1 - 重要功能

1. 打卡项目管理
2. 打卡统计与排行榜
3. 标签与专栏
4. 成员管理

### P2 - 扩展功能

1. Dashboard运营数据
2. 积分榜管理
3. 星球推荐
4. 用户足迹

---

## 9. 实现建议

### 9.1 请求频率控制

- 每分钟不超过60次请求
- 遇到429错误暂停1-5分钟
- 使用Redis缓存减少重复请求

### 9.2 Token管理

- Token有效期未知，建议定期更新
- 使用环境变量管理Token
- 检测401错误自动刷新

### 9.3 错误处理

```typescript
if (response.status === 401) {
  throw new ZsxqTokenExpiredError('Token已失效');
}
if (response.status === 429) {
  throw new ZsxqRateLimitError('请求过于频繁');
}
if (!response.data.succeeded) {
  throw new ZsxqApiError(response.data.error?.message || '未知错误');
}
```

### 9.4 数据格式化

原生API使用下划线命名，建议转换为驼峰命名：

```typescript
// 原始: { user_id: 123, avatar_url: '...' }
// 转换: { userId: 123, avatarUrl: '...' }
```

---

## 10. 附录

### 10.1 完整API端点列表

```
# 用户模块
GET /v2/users/self/groups
GET /v2/users/self/groups/applied_groups
GET /v2/users/self/merchant_coupons
GET /v2/users/self/recommendations/users
GET /v2/users/self/groups/{group_id}/inviter
GET /v2/users/self/groups/{group_id}/checkins/{checkin_id}/checkined_dates
GET /v2/users/self/groups/{group_id}/checkins/{checkin_id}/statistics
GET /v2/users/self/groups/{group_id}/checkins/{checkin_id}/topics
GET /v2/users/{user_id}/created_groups
GET /v2/users/{user_id}/footprints
GET /v2/users/{user_id}/footprints/groups
GET /v3/users/self
GET /v3/users/self/remarks
GET /v3/users/{user_id}
GET /v3/users/{user_id}/statistics
GET /v3/users/{user_id}/avatar_url

# 星球模块
GET /v2/groups
GET /v2/groups/{group_id}
GET /v2/groups/unread_topics_count
GET /v2/groups/upgradable_groups
GET /v2/groups/recommendations
GET /v2/pk_groups/{group_id}
GET /v2/pk_groups/{group_id}/records
GET /v3/groups/ranking_list
GET /v3/groups/{group_id}/ranking_list/statistics
GET /v2/groups/{group_id}/members/{user_id}
GET /v2/groups/{group_id}/members/{user_id}/summary
GET /v2/groups/{group_id}/role_members
GET /v2/groups/{group_id}/statistics
GET /v2/groups/{group_id}/distribution
GET /v2/groups/{group_id}/renewal
GET /v2/groups/{group_id}/menus
GET /v2/groups/{group_id}/menus/last_read_time
PUT /v2/groups/{group_id}/menus/last_read_time

# 话题模块
GET /v2/groups/{group_id}/topics
GET /v2/topics/{topic_id}
GET /v2/topics/{topic_id}/info
GET /v2/topics/{topic_id}/comments
GET /v2/topics/{topic_id}/rewards
GET /v2/topics/{topic_id}/recommendations

# 标签模块
GET /v2/groups/{group_id}/hashtags
GET /v2/hashtags/{hashtag_id}/topics

# 专栏模块
GET /v2/groups/{group_id}/columns
GET /v2/groups/{group_id}/columns/summary
GET /v2/groups/{group_id}/columns/{column_id}/topics

# 打卡模块
GET /v2/groups/{group_id}/checkins
GET /v2/groups/{group_id}/checkins/{checkin_id}
GET /v2/groups/{group_id}/checkins/{checkin_id}/statistics
GET /v2/groups/{group_id}/checkins/{checkin_id}/statistics/daily
GET /v2/groups/{group_id}/checkins/{checkin_id}/topics
GET /v2/groups/{group_id}/checkins/{checkin_id}/ranking_list
GET /v2/groups/{group_id}/checkins/{checkin_id}/joined_users

# Dashboard模块
GET /v2/dashboard/groups/{group_id}/overview
GET /v2/dashboard/groups/{group_id}/incomes/overview
GET /v2/dashboard/groups/{group_id}/privileges
GET /v2/dashboard/groups/{group_id}/scoreboard/settings
GET /v2/dashboard/groups/{group_id}/scoreboard/statistics/self
GET /v2/dashboard/groups/{group_id}/scoreboard/ranking_list

# 其他
GET /v2/url_details
```

### 10.2 参考资源

- HAR抓包文件: `/Users/stinglong/code/aisy.har`
- 原有API文档: `docs/zsxq-native-api-reference.md`
