# 知识星球原生 API 参考

> 通过抓包分析获取的知识星球原生 API 文档

## 概述

- **基础 URL**: `https://api.zsxq.com`
- **App 版本**: 2.83.0 / 2.85.0
- **接口总数**: 118 个（去重后）
- **数据来源**: Fiddler Everywhere 抓包分析

---

## 认证机制

### 请求头

所有 API 请求都需要以下请求头：

| 请求头 | 说明 | 示例 |
|--------|------|------|
| `authorization` | 认证 Token | `D047A423-A9E1-...` |
| `x-timestamp` | Unix 时间戳 | `1765268187` |
| `x-signature` | 请求签名 (SHA1) | `dd7b51bee...` |
| `x-aduid` | 设备唯一标识 | `d75d966c-ed30...` |
| `x-version` | App 版本 | `2.83.0` |
| `x-request-id` | 请求追踪 ID (UUID) | `9af8e4c1...` |
| `user-agent` | 用户代理 | `xiaomiquan/5.29.1 iOS/phone/26.1` |
| `content-type` | 内容类型 | `application/json; charset=utf-8` |

### 签名算法（推测）

```typescript
import * as crypto from 'crypto';

function generateSignature(
  timestamp: string,
  method: string,
  path: string,
  body?: string,
  secretKey: string = 'UNKNOWN'
): string {
  let signData = `${timestamp}\n${method}\n${path}`;
  if (body) {
    signData += `\n${body}`;
  }

  return crypto
    .createHmac('sha1', secretKey)
    .update(signData)
    .digest('hex');
}
```

---

## API 端点列表

### 用户相关

| 方法 | 端点 | 说明 |
|------|------|------|
| `GET` | `/v3/users/self` | 获取当前用户信息 |
| `GET` | `/v3/users/{user_id}` | 获取指定用户信息 |
| `GET` | `/v3/users/{user_id}/statistics` | 获取用户统计数据 |
| `GET` | `/v3/users/{user_id}/avatar_url` | 获取用户大尺寸头像 |
| `GET` | `/v2/users/{user_id}/footprints` | 获取用户动态/足迹 |
| `GET` | `/v2/users/{user_id}/created_groups` | 获取用户创建的星球 |
| `GET` | `/v3/users/self/remarks` | 获取用户备注列表 |
| `GET` | `/v2/users/self/merchant_coupons` | 获取优惠券列表 |
| `GET` | `/v2/users/self/groups/applied_groups` | 获取申请中的星球 |

### 星球相关

| 方法 | 端点 | 说明 |
|------|------|------|
| `GET` | `/v2/groups` | 获取我的星球列表 |
| `GET` | `/v2/groups/{group_id}` | 获取星球详情 |
| `GET` | `/v2/groups/{group_id}/hashtags` | 获取星球标签 |
| `GET` | `/v2/groups/{group_id}/menus` | 获取星球菜单配置 |
| `GET` | `/v2/groups/{group_id}/statistics` | 获取星球统计 |
| `GET` | `/v2/groups/{group_id}/columns` | 获取星球专栏 |
| `GET` | `/v2/groups/{group_id}/columns/summary` | 获取专栏概览 |
| `GET` | `/v2/groups/{group_id}/role_members` | 获取角色成员 |
| `GET` | `/v2/groups/{group_id}/renewal` | 获取续费信息 |
| `GET` | `/v2/groups/{group_id}/distribution` | 获取分销信息 |
| `GET` | `/v2/groups/{group_id}/members/{member_id}` | 获取成员信息 |
| `GET` | `/v2/groups/{group_id}/members/{member_id}/summary` | 获取成员概览 |
| `GET` | `/v2/groups/unread_topics_count` | 获取未读话题数 |
| `GET` | `/v2/groups/recommendations` | 获取推荐星球 |
| `GET` | `/v2/groups/upgradable_groups` | 获取可升级星球 |

### 话题相关

| 方法 | 端点 | 说明 |
|------|------|------|
| `GET` | `/v2/groups/{group_id}/topics` | 获取话题列表 |
| `GET` | `/v2/topics/{topic_id}` | 获取话题详情 |
| `GET` | `/v2/topics/{topic_id}/info` | 获取话题信息（轻量） |
| `GET` | `/v2/topics/{topic_id}/comments` | 获取话题评论 |
| `GET` | `/v2/topics/{topic_id}/rewards` | 获取话题打赏 |
| `GET` | `/v2/topics/{topic_id}/recommendations` | 获取相关推荐 |
| `GET` | `/v2/hashtags/{hashtag_id}/topics` | 按标签获取话题 |
| `GET` | `/v2/groups/{group_id}/columns/{column_id}/topics` | 按专栏获取话题 |

### 打卡相关

| 方法 | 端点 | 说明 |
|------|------|------|
| `GET` | `/v2/groups/{group_id}/checkins` | 获取打卡项目列表 |
| `GET` | `/v2/groups/{group_id}/checkins/{checkin_id}` | 获取打卡项目详情 |
| `GET` | `/v2/groups/{group_id}/checkins/{checkin_id}/statistics` | 获取打卡统计 |
| `GET` | `/v2/groups/{group_id}/checkins/{checkin_id}/statistics/daily` | 获取每日统计 |
| `GET` | `/v2/groups/{group_id}/checkins/{checkin_id}/ranking_list` | 获取打卡排行榜 |
| `GET` | `/v2/groups/{group_id}/checkins/{checkin_id}/topics` | 获取打卡话题 |
| `GET` | `/v2/groups/{group_id}/checkins/{checkin_id}/joined_users` | 获取参与用户 |

### 排行榜相关

| 方法 | 端点 | 说明 |
|------|------|------|
| `GET` | `/v3/groups/ranking_list` | 获取星球排行榜 |
| `GET` | `/v3/groups/{group_id}/ranking_list/statistics` | 获取排行统计 |
| `GET` | `/v2/dashboard/groups/{group_id}/scoreboard/ranking_list` | 获取积分排行 |

### 数据面板（Dashboard）

| 方法 | 端点 | 说明 |
|------|------|------|
| `GET` | `/v2/dashboard/groups/{group_id}/overview` | 获取星球概览 |
| `GET` | `/v2/dashboard/groups/{group_id}/privileges` | 获取权限配置 |
| `GET` | `/v2/dashboard/groups/{group_id}/scoreboard/settings` | 获取积分设置 |
| `GET` | `/v2/dashboard/groups/{group_id}/scoreboard/statistics/self` | 获取个人积分 |
| `GET` | `/v2/dashboard/groups/{group_id}/incomes/overview` | 获取收入概览 |

### 阅读追踪

| 方法 | 端点 | 说明 |
|------|------|------|
| `GET` | `/v2/groups/{group_id}/menus/last_read_time` | 获取阅读进度 |
| `PUT` | `/v2/groups/{group_id}/menus/last_read_time` | 更新阅读进度 |

---

## 查询参数说明

### 话题列表参数

| 参数 | 类型 | 说明 |
|------|------|------|
| `count` | number | 返回数量，默认 20 |
| `scope` | string | 范围：`all`/`digests`/`by_owner` |
| `direction` | string | 分页方向：`forward`/`backward` |
| `begin_time` | string | 开始时间（ISO 8601） |
| `end_time` | string | 结束时间（ISO 8601） |
| `with_invisibles` | boolean | 是否包含隐藏话题 |

### 打卡列表参数

| 参数 | 类型 | 说明 |
|------|------|------|
| `scope` | string | 状态：`ongoing`/`closed`/`over` |
| `count` | number | 返回数量 |

### 排行榜参数

| 参数 | 类型 | 说明 |
|------|------|------|
| `type` | string | 类型：`continuous`/`accumulated` |
| `index` | number | 分页索引 |

### 评论参数

| 参数 | 类型 | 说明 |
|------|------|------|
| `sort` | string | 排序：`asc`/`desc` |
| `sort_type` | string | 排序字段：`by_create_time` |
| `count` | number | 返回数量 |
| `with_sticky` | boolean | 是否包含置顶 |

---

## 响应格式

### 成功响应

```json
{
  "succeeded": true,
  "resp_data": {
    // 响应数据
  }
}
```

### 错误响应

```json
{
  "succeeded": false,
  "code": 52010,
  "info": "",
  "resp_data": {},
  "error": "未报名参加该打卡任务"
}
```

---

## 常见错误码

| 错误码 | 说明 |
|--------|------|
| `10001` | Token 无效 |
| `10002` | Token 过期 |
| `52010` | 未参与打卡项目 |

> 完整错误码列表参见 [错误码定义](../design/error-codes.md)

---

## 数据模型

### User

```typescript
interface User {
  user_id: number;
  uid?: string;
  name: string;
  avatar_url: string;
  location?: string;
  introduction?: string;
  unique_id?: string;
  user_sid?: string;
  grade?: string;
  verified?: boolean;
}
```

### Group

```typescript
interface Group {
  group_id: number;
  number?: number;
  name: string;
  description: string;
  background_url: string;
  type: 'free' | 'pay';
  member_count?: number;
  owner?: User;
  create_time: string;
  risk_level?: string;
  partner_ids?: number[];
  admin_ids?: number[];
}
```

### Topic

```typescript
interface Topic {
  topic_id: number;
  topic_uid: string;
  group: Group;
  type: 'talk' | 'task' | 'q&a' | 'solution';
  create_time: string;
  talk?: TalkContent;
  task?: TaskContent;
  question?: QuestionContent;
  solution?: SolutionContent;
  likes_count?: number;
  comments_count?: number;
  rewards_count?: number;
  reading_count?: number;
  digested?: boolean;
  sticky?: boolean;
}

interface TalkContent {
  owner: User;
  text?: string;
  images?: Image[];
  files?: File[];
  article?: Article;
}
```

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
}

interface CheckinStatistics {
  joined_count: number;
  completed_count: number;
  checkined_count: number;
  ranking_list: RankingItem[];
}
```

---

## 使用示例

### 获取话题列表

```bash
curl -X GET 'https://api.zsxq.com/v2/groups/15555411412112/topics?count=10&scope=all' \
  -H 'authorization: YOUR_TOKEN' \
  -H 'x-timestamp: 1765268192' \
  -H 'x-signature: YOUR_SIGNATURE' \
  -H 'x-version: 2.83.0'
```

### 获取打卡排行榜

```bash
curl -X GET 'https://api.zsxq.com/v2/groups/15555411412112/checkins/5454855814/ranking_list?type=continuous&index=0' \
  -H 'authorization: YOUR_TOKEN' \
  -H 'x-timestamp: 1765268274' \
  -H 'x-signature: YOUR_SIGNATURE' \
  -H 'x-version: 2.83.0'
```

---

## 相关文档

- [API 映射](../design/api-mapping.md) - SDK 方法与原生 API 映射
- [错误码定义](../design/error-codes.md) - 完整错误码列表
- [认证指南](../guides/authentication.md) - Token 获取与使用
