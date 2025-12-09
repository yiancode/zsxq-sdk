# API 映射关系

> 本文档是 SDK 方法与知识星球原生 API 的唯一权威映射来源（SSOT）

## 映射规则

SDK 采用语义化方法命名，对应知识星球原生 API 端点。

---

## 星球相关 (GroupRequest)

| SDK 方法 | HTTP 方法 | 原生 API 端点 | 说明 |
|---------|----------|--------------|------|
| `groups.list()` | GET | `/v2/groups` | 获取用户加入的星球列表 |
| `groups.get(groupId)` | GET | `/v2/groups/{group_id}` | 获取星球详情 |
| `groups.getHashtags(groupId)` | GET | `/v2/groups/{group_id}/hashtags` | 获取星球标签 |
| `groups.getMenus(groupId)` | GET | `/v2/groups/{group_id}/menus` | 获取星球菜单 |
| `groups.getStatistics(groupId)` | GET | `/v2/groups/{group_id}/statistics` | 获取星球统计 |
| `groups.getColumns(groupId)` | GET | `/v2/groups/{group_id}/columns` | 获取星球专栏 |
| `groups.getRoleMembers(groupId, role)` | GET | `/v2/groups/{group_id}/role_members` | 获取星球成员（按角色） |
| `groups.getUnreadCount()` | GET | `/v2/groups/unread_topics_count` | 获取未读话题数 |
| `groups.getRecommendations(count)` | GET | `/v2/groups/recommendations` | 获取推荐星球 |

---

## 话题相关 (TopicRequest)

| SDK 方法 | HTTP 方法 | 原生 API 端点 | 说明 |
|---------|----------|--------------|------|
| `topics.list(groupId, options)` | GET | `/v2/groups/{group_id}/topics` | 获取话题列表 |
| `topics.get(topicId)` | GET | `/v2/topics/{topic_id}` | 获取话题详情 |
| `topics.getInfo(topicId)` | GET | `/v2/topics/{topic_id}/info` | 获取话题信息 |
| `topics.getComments(topicId, options)` | GET | `/v2/topics/{topic_id}/comments` | 获取话题评论 |
| `topics.getRewards(topicId)` | GET | `/v2/topics/{topic_id}/rewards` | 获取话题打赏 |
| `topics.getRecommendations(topicId)` | GET | `/v2/topics/{topic_id}/recommendations` | 获取相关推荐 |
| `topics.getByHashtag(hashtagId, options)` | GET | `/v2/hashtags/{hashtag_id}/topics` | 按标签获取话题 |
| `topics.getByColumn(groupId, columnId)` | GET | `/v2/groups/{group_id}/columns/{column_id}/topics` | 按专栏获取话题 |

### 话题列表参数 (options)

```typescript
interface TopicListOptions {
  count?: number;        // 返回数量，默认 20
  scope?: 'all' | 'digests' | 'by_owner';  // 范围
  direction?: 'forward' | 'backward';       // 方向
  begin_time?: string;   // 开始时间
  end_time?: string;     // 结束时间
  with_invisibles?: boolean;  // 是否包含隐藏
}
```

---

## 用户相关 (UserRequest)

| SDK 方法 | HTTP 方法 | 原生 API 端点 | 说明 |
|---------|----------|--------------|------|
| `users.getSelf()` | GET | `/v3/users/self` | 获取当前用户 |
| `users.get(userId)` | GET | `/v3/users/{user_id}` | 获取用户信息 |
| `users.getStatistics(userId)` | GET | `/v3/users/{user_id}/statistics` | 获取用户统计 |
| `users.getAvatar(userId)` | GET | `/v3/users/{user_id}/avatar_url` | 获取用户头像 |
| `users.getFootprints(userId, options)` | GET | `/v2/users/{user_id}/footprints` | 获取用户足迹 |
| `users.getCreatedGroups(userId)` | GET | `/v2/users/{user_id}/created_groups` | 获取用户创建的星球 |
| `users.getRemarks()` | GET | `/v3/users/self/remarks` | 获取用户备注 |
| `users.getCoupons()` | GET | `/v2/users/self/merchant_coupons` | 获取优惠券 |
| `users.getAppliedGroups()` | GET | `/v2/users/self/groups/applied_groups` | 获取申请中的星球 |

---

## 打卡相关 (CheckinRequest)

| SDK 方法 | HTTP 方法 | 原生 API 端点 | 说明 |
|---------|----------|--------------|------|
| `checkins.list(groupId, scope)` | GET | `/v2/groups/{group_id}/checkins` | 获取打卡项目列表 |
| `checkins.get(groupId, checkinId)` | GET | `/v2/groups/{group_id}/checkins/{checkin_id}` | 获取打卡详情 |
| `checkins.getStatistics(groupId, checkinId)` | GET | `/v2/groups/{group_id}/checkins/{checkin_id}/statistics` | 获取打卡统计 |
| `checkins.getDailyStats(groupId, checkinId, date)` | GET | `/v2/groups/{group_id}/checkins/{checkin_id}/statistics/daily` | 获取每日统计 |
| `checkins.getRankingList(groupId, checkinId, type)` | GET | `/v2/groups/{group_id}/checkins/{checkin_id}/ranking_list` | 获取排行榜 |
| `checkins.getTopics(groupId, checkinId)` | GET | `/v2/groups/{group_id}/checkins/{checkin_id}/topics` | 获取打卡话题 |
| `checkins.getJoinedUsers(groupId, checkinId, filter)` | GET | `/v2/groups/{group_id}/checkins/{checkin_id}/joined_users` | 获取参与用户 |

### 打卡范围 (scope)

```typescript
type CheckinScope = 'ongoing' | 'closed' | 'over';
```

### 排行榜类型 (type)

```typescript
type RankingType = 'continuous' | 'accumulated';
```

---

## Dashboard 相关 (DashboardRequest)

> 星主/管理员专用接口

| SDK 方法 | HTTP 方法 | 原生 API 端点 | 说明 |
|---------|----------|--------------|------|
| `dashboard.getOverview(groupId)` | GET | `/v2/dashboard/groups/{group_id}/overview` | 获取星球概览 |
| `dashboard.getPrivileges(groupId)` | GET | `/v2/dashboard/groups/{group_id}/privileges` | 获取权限设置 |
| `dashboard.getIncomesOverview(groupId)` | GET | `/v2/dashboard/groups/{group_id}/incomes/overview` | 获取收入概览 |
| `dashboard.getScoreboardSettings(groupId)` | GET | `/v2/dashboard/groups/{group_id}/scoreboard/settings` | 获取积分设置 |
| `dashboard.getScoreboardRanking(groupId, type)` | GET | `/v2/dashboard/groups/{group_id}/scoreboard/ranking_list` | 获取积分排行 |
| `dashboard.getSelfScore(groupId)` | GET | `/v2/dashboard/groups/{group_id}/scoreboard/statistics/self` | 获取自己积分 |

---

## 排行榜相关

| SDK 方法 | HTTP 方法 | 原生 API 端点 | 说明 |
|---------|----------|--------------|------|
| `rankings.getGroupRanking(type)` | GET | `/v3/groups/ranking_list` | 获取星球排行榜 |
| `rankings.getGroupStats(groupId)` | GET | `/v3/groups/{group_id}/ranking_list/statistics` | 获取星球排名统计 |

### 排行榜类型

```typescript
type GroupRankingType =
  | 'group_fortune_list'    // 财富榜
  | 'paid_group_active_list' // 付费活跃榜
  | 'new_star_list'          // 新星榜
  | 'group_sales_list';      // 畅销榜
```

---

## 成员相关

| SDK 方法 | HTTP 方法 | 原生 API 端点 | 说明 |
|---------|----------|--------------|------|
| `members.get(groupId, memberId)` | GET | `/v2/groups/{group_id}/members/{member_id}` | 获取成员信息 |
| `members.getSummary(groupId, memberId)` | GET | `/v2/groups/{group_id}/members/{member_id}/summary` | 获取成员概要 |

---

## 其他接口

| SDK 方法 | HTTP 方法 | 原生 API 端点 | 说明 |
|---------|----------|--------------|------|
| `misc.getUrlDetails(url)` | GET | `/v2/url_details` | 解析 URL 详情 |
| `misc.getRenewal(groupId)` | GET | `/v2/groups/{group_id}/renewal` | 获取续费信息 |
| `misc.getDistribution(groupId)` | GET | `/v2/groups/{group_id}/distribution` | 获取分销信息 |
| `misc.getUpgradableGroups()` | GET | `/v2/groups/upgradable_groups` | 获取可升级星球 |

---

## 请求头映射

SDK 自动处理以下请求头：

| SDK 配置 | 请求头 | 说明 |
|---------|--------|------|
| `token` | `authorization` | 认证 Token |
| `deviceId` | `x-aduid` | 设备唯一标识 |
| `appVersion` | `x-version` | App 版本号 |
| - | `x-timestamp` | 自动生成时间戳 |
| - | `x-signature` | 自动计算签名 |
| - | `x-request-id` | 自动生成请求 ID |

---

## 分页约定

大多数列表接口支持以下分页参数：

| 参数 | 类型 | 说明 |
|-----|------|------|
| `count` | number | 每页数量（默认 20，最大 100） |
| `begin_time` | string | 开始时间（ISO 8601） |
| `end_time` | string | 结束时间（ISO 8601） |
| `direction` | string | 分页方向 forward/backward |

---

## 版本说明

- **API 版本**: v2/v3（知识星球原生 API）
- **SDK 版本**: 0.1.0
- **最后更新**: 2025-12-09
