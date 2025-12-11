# zsxq-sdk AI 敏捷开发计划

> 最后更新: 2025-12-12
> 目标: 将 API 覆盖率从 14% 提升至 100%

---

## 项目概览

| 指标 | 当前状态 | 目标状态 |
|------|---------|---------|
| 总接口数 | 85 | 85 |
| 已实现 | 65 | 85 |
| 覆盖率 | 76.5% | 100% |
| SDK 语言 | Java (主力) | Java, TypeScript, Go, Python |

---

## 当前实现状态

### Java SDK 已实现方法

| 模块 | 已实现方法 | 数量 |
|------|-----------|------|
| **GroupsRequest** | `list`, `get`, `getStatistics`, `getMember`, `getHashtags`, `getUnreadCount`, `getMenus`, `getRoleMembers`, `getColumns`, `getColumnsSummary`, `getMemberActivitySummary`, `getRenewalInfo`, `getDistribution`, `getUpgradeableGroups`, `getRecommendedGroups`, `getCustomTags`, `getScheduledTasks`, `getRiskWarnings` | 18 |
| **TopicsRequest** | `list`, `get`, `getComments`, `listByHashtag`, `listByColumn`, `getInfo`, `getRewards`, `getRecommendations`, `listSticky` | 9 |
| **UsersRequest** | `self`, `get`, `getStatistics`, `getCreatedGroups`, `getFootprints`, `getAvatarUrl`, `getGroupFootprints`, `getApplyingGroups`, `getInviter`, `getCoupons`, `getRemarks`, `getRecommendedFollows`, `getBlockedUsers`, `reportPushChannel`, `getPreferenceCategories`, `getUnansweredQuestionsSummary`, `getFollowerStats`, `getContributions`, `getContributionStats`, `getAchievementsSummary`, `getWeeklyRanking`, `getPreferences` | 22 |
| **CheckinsRequest** | `list`, `get`, `getStatistics`, `getRankingList`, `getTopics`, `getDailyStatistics`, `getJoinedUsers`, `getMyCheckins`, `getMyCheckinDays`, `getMyStatistics`, `create`, `update` | 12 |
| **DashboardRequest** | `getOverview`, `getIncomes`, `getScoreboardRanking`, `getPrivileges`, `getInvoiceStats` | 5 |
| **RankingRequest** | `getGroupRanking`, `getGroupRankingStats`, `getScoreRanking`, `getMyScoreStats`, `getScoreboardSettings`, `getInvitationRanking`, `getContributionRanking` | 7 |
| **MiscRequest** | `getPkGroup`, `getPkBattles`, `parseUrl`, `getGlobalConfig`, `getActivities` | 5 |

### 已有模型

`User`, `Group`, `Topic`, `TalkContent`, `Image`, `Comment`, `Hashtag`, `Checkin`, `CheckinStatistics`, `RankingItem`, `Reward`, `Menu`, `Column`, `RoleMembers`, `ActivitySummary`, `RankingStatistics`, `ScoreboardSettings`, `Coupon`, `Remark`, `Inviter`, `DailyStatistics`, `MyCheckinStatistics`, `Privilege`, `InvoiceStats`, `PkGroup`, `PkBattle`, `UrlDetail`, `GlobalConfig`, `Activity`, `Contribution`, `ContributionStatistics`, `AchievementSummary`, `WeeklyRanking`, `PreferenceCategory`, `FollowerStatistics`, `UnansweredQuestionsSummary`, `CustomTag`, `ScheduledJob`, `RenewalInfo`, `DistributionInfo`, `GroupWarning`

---

## Sprint 1: 核心功能完善 (预计 3-5 个接口/天)

**目标**: 覆盖率 → 40%
**状态**: [x] ✅ Completed (2025-12-11)

### 1.1 话题管理扩展 (TopicsRequest)

| 优先级 | 接口 | 方法名 | 端点 | 新增模型 | 状态 |
|--------|------|--------|------|---------|------|
| P0 | 获取话题基础信息 | `getInfo(topicId)` | `GET /v2/topics/{topic_id}/info` | - | [x] ✅ |
| P0 | 获取话题打赏列表 | `getRewards(topicId)` | `GET /v2/topics/{topic_id}/rewards` | `Reward` | [x] ✅ |
| P1 | 获取相关推荐话题 | `getRecommendations(topicId)` | `GET /v2/topics/{topic_id}/recommendations` | - | [x] ✅ |
| P1 | 获取置顶话题列表 | `listSticky(groupId)` | `GET /v2/groups/{group_id}/topics/sticky` | - | [x] ✅ |

**实现步骤**:
1. [x] 创建 `Reward` 模型 (`packages/java/src/main/java/com/zsxq/sdk/model/Reward.java`)
2. [x] 在 `TopicsRequest` 添加 `getInfo()` 方法
3. [x] 在 `TopicsRequest` 添加 `getRewards()` 方法
4. [x] 在 `TopicsRequest` 添加 `getRecommendations()` 方法
5. [x] 在 `TopicsRequest` 添加 `listSticky()` 方法
6. [x] 编写单元测试
7. [x] 集成测试验证

### 1.2 星球管理扩展 (GroupsRequest)

| 优先级 | 接口 | 方法名 | 端点 | 新增模型 | 状态 |
|--------|------|--------|------|---------|------|
| P0 | 获取菜单配置 | `getMenus(groupId)` | `GET /v2/groups/{group_id}/menus` | `Menu` | [x] ✅ |
| P0 | 获取角色成员 | `getRoleMembers(groupId)` | `GET /v2/groups/{group_id}/role_members` | `RoleMembers` | [x] ✅ |
| P0 | 获取专栏列表 | `getColumns(groupId)` | `GET /v2/groups/{group_id}/columns` | `Column` | [x] ✅ |
| P1 | 获取专栏汇总 | `getColumnsSummary(groupId)` | `GET /v2/groups/{group_id}/columns/summary` | - | [x] ✅ |
| P1 | 获取成员活跃摘要 | `getMemberActivitySummary(groupId, memberId)` | `GET /v2/groups/{group_id}/members/{member_id}/summary` | `ActivitySummary` | [x] ✅ |

**实现步骤**:
1. [x] 创建 `Menu` 模型
2. [x] 创建 `Column` 模型
3. [x] 创建 `RoleMembers` 模型
4. [x] 创建 `ActivitySummary` 模型
5. [x] 在 `GroupsRequest` 添加以上方法
6. [x] 编写单元测试
7. [x] 集成测试验证

### Sprint 1 验收标准

- [x] 9 个新接口实现完成
- [x] 5 个新模型创建完成
- [x] 编译通过
- [x] 集成测试代码编写完成
- [ ] 更新 `docs/TESTING_STATUS.md`（需要真实 Token 验证后更新）

---

## Sprint 2: 排行榜模块 + 用户扩展 (预计 3-5 个接口/天)

**目标**: 覆盖率 → 60%
**状态**: [x] ✅ Completed (2025-12-11)

### 2.1 新增 RankingRequest 模块

| 优先级 | 接口 | 方法名 | 端点 | 状态 |
|--------|------|--------|------|------|
| P0 | 获取星球排行榜 | `getGroupRanking(groupId)` | `GET /v2/groups/{group_id}/ranking_list` | [x] ✅ |
| P0 | 获取星球排行统计 | `getGroupRankingStats(groupId)` | `GET /v2/groups/{group_id}/ranking_list/statistics` | [x] ✅ |
| P0 | 获取积分排行榜 | `getScoreRanking(groupId)` | `GET /v2/groups/{group_id}/scoreboard/ranking_list` | [x] ✅ |
| P1 | 获取我的积分统计 | `getMyScoreStats(groupId)` | `GET /v2/groups/{group_id}/scoreboard/my_statistics` | [x] ✅ |
| P1 | 获取积分榜设置 | `getScoreboardSettings(groupId)` | `GET /v2/groups/{group_id}/scoreboard/settings` | [x] ✅ |
| P2 | 获取邀请排行榜 | `getInvitationRanking(groupId)` | `GET /v2/groups/{group_id}/invitation_ranking_list` | [x] ✅ |
| P2 | 获取贡献排行榜 | `getContributionRanking(groupId)` | `GET /v2/groups/{group_id}/contribution_ranking_list` | [x] ✅ |

**实现步骤**:
1. [x] 创建 `RankingRequest.java` 模块
2. [x] 创建 `RankingStatistics` 模型
3. [x] 创建 `ScoreboardSettings` 模型
4. [x] 在 `ZsxqClient` 中注册 `ranking()` 方法
5. [x] 实现 7 个排行榜相关方法
6. [x] 编写单元测试
7. [x] 集成测试验证

### 2.2 用户模块扩展 (UsersRequest)

| 优先级 | 接口 | 方法名 | 端点 | 状态 |
|--------|------|--------|------|------|
| P0 | 获取用户头像 | `getAvatarUrl(userId)` | `GET /v3/users/{user_id}/avatar_url` | [x] ✅ |
| P1 | 获取星球足迹 | `getGroupFootprints(userId)` | `GET /v2/users/{user_id}/group_footprints` | [x] ✅ |
| P1 | 获取申请中星球 | `getApplyingGroups()` | `GET /v2/groups/applying` | [x] ✅ |
| P1 | 获取邀请人信息 | `getInviter(groupId)` | `GET /v2/groups/{group_id}/inviter` | [x] ✅ |
| P2 | 获取我的优惠券 | `getCoupons()` | `GET /v2/coupons` | [x] ✅ |
| P2 | 获取我的备注 | `getRemarks()` | `GET /v2/remarks` | [x] ✅ |
| P2 | 获取推荐关注 | `getRecommendedFollows()` | `GET /v2/users/recommended_follows` | [x] ✅ |

**新增模型**:
- [x] `Coupon` - 优惠券
- [x] `Remark` - 备注
- [x] `Inviter` - 邀请人信息

### Sprint 2 验收标准

- [x] 14 个新接口实现完成
- [x] `RankingRequest` 模块创建完成
- [x] 5 个新模型创建完成 (RankingStatistics, ScoreboardSettings, Coupon, Remark, Inviter)
- [x] 编译通过
- [x] 集成测试代码编写完成

---

## Sprint 3: 打卡完善 + 辅助功能 (预计 3-5 个接口/天)

**目标**: 覆盖率 → 80%
**状态**: [x] ✅ Completed (2025-12-12)

### 3.1 打卡模块完善 (CheckinsRequest)

| 优先级 | 接口 | 方法名 | 端点 | 状态 |
|--------|------|--------|------|------|
| P0 | 获取每日统计 | `getDailyStatistics(groupId, checkinId)` | `GET /v2/groups/{group_id}/checkins/{checkin_id}/statistics/daily` | [x] ✅ |
| P1 | 获取参与用户 | `getJoinedUsers(groupId, checkinId)` | `GET /v2/groups/{group_id}/checkins/{checkin_id}/joined_users` | [x] ✅ |
| P1 | 获取我的打卡记录 | `getMyCheckins(groupId, checkinId)` | `GET /v2/users/self/groups/{group_id}/checkins/{checkin_id}/topics` | [x] ✅ |
| P1 | 获取我的打卡日期 | `getMyCheckinDays(groupId, checkinId)` | `GET /v2/users/self/groups/{group_id}/checkins/{checkin_id}/checkined_dates` | [x] ✅ |
| P1 | 获取我的打卡统计 | `getMyStatistics(groupId, checkinId)` | `GET /v2/users/self/groups/{group_id}/checkins/{checkin_id}/statistics` | [x] ✅ |
| P2 | 创建打卡项目 | `create(groupId, params)` | `POST /v2/groups/{group_id}/checkins` | [x] ✅ |
| P2 | 更新打卡项目 | `update(groupId, checkinId, params)` | `PUT /v2/groups/{group_id}/checkins/{checkin_id}` | [x] ✅ |

**新增模型**:
- [x] `DailyStatistics` - 每日统计
- [x] `MyCheckinStatistics` - 我的打卡统计

### 3.2 数据面板扩展 (DashboardRequest)

| 优先级 | 接口 | 方法名 | 端点 | 状态 |
|--------|------|--------|------|------|
| P1 | 获取权限配置 | `getPrivileges(groupId)` | `GET /v2/dashboard/groups/{group_id}/privileges` | [x] ✅ |
| P2 | 获取发票统计 | `getInvoiceStats()` | `GET /v3/invoices/statistics` | [x] ✅ |

**新增模型**:
- [x] `Privilege` - 权限配置
- [x] `InvoiceStats` - 发票统计

### 3.3 新增 MiscRequest 模块

| 优先级 | 接口 | 方法名 | 端点 | 状态 |
|--------|------|--------|------|------|
| P1 | 获取PK群组详情 | `getPkGroup(pkGroupId)` | `GET /v2/pk_groups/{pk_group_id}` | [x] ✅ |
| P1 | 获取PK对战记录 | `getPkBattles(pkGroupId)` | `GET /v2/pk_groups/{pk_group_id}/records` | [x] ✅ |
| P2 | 解析URL详情 | `parseUrl(url)` | `GET /v2/url_details?url=xxx` | [x] ✅ |
| P2 | 获取全局配置 | `getGlobalConfig()` | `GET /v2/settings` | [x] ✅ |
| P2 | 获取动态列表 | `getActivities()` | `GET /v2/dynamics` | [x] ✅ |

**新增模型**:
- [x] `PkGroup` - PK群组
- [x] `PkBattle` - PK对战
- [x] `UrlDetail` - URL解析结果
- [x] `GlobalConfig` - 全局配置
- [x] `Activity` - 动态

### Sprint 3 验收标准

- [x] 14 个新接口实现完成
- [x] `MiscRequest` 模块创建完成
- [x] 9 个新模型创建完成
- [x] 所有代码编译通过
- [x] 集成测试代码编写完成

---

## Sprint 4: 高级功能 + 多语言同步

**目标**: 覆盖率 → 100%
**状态**: [x] ✅ Sprint 4.1/4.2 Completed (2025-12-12)

### 4.1 用户高级功能

| 接口 | 方法名 | 端点 | 状态 |
|------|--------|------|------|
| 获取屏蔽用户列表 | `getBlockedUsers()` | `GET /v2/users/block_users` | [x] ✅ |
| 上报推送通道 | `reportPushChannel()` | `POST /v2/users/self/push_channel` | [x] ✅ |
| 获取推荐偏好分类 | `getPreferenceCategories()` | `GET /v2/users/self/recommendations/preference_categories` | [x] ✅ |
| 获取未回答问题摘要 | `getUnansweredQuestionsSummary()` | `GET /v2/users/self/unanswered_questions/brief` | [x] ✅ |
| 获取关注者统计 | `getFollowerStats()` | `GET /v3/users/self/followers/statistics` | [x] ✅ |
| 获取贡献记录 | `getContributions()` | `GET /v3/users/self/contributions` | [x] ✅ |
| 获取贡献统计 | `getContributionStats()` | `GET /v3/users/self/contributions/statistics` | [x] ✅ |
| 获取成就摘要 | `getAchievementsSummary()` | `GET /v3/users/self/achievements/summaries` | [x] ✅ |
| 获取星球周榜排名 | `getWeeklyRanking(groupId)` | `GET /v3/users/self/group_weekly_rankings` | [x] ✅ |
| 获取用户偏好配置 | `getPreferences()` | `GET /v3/users/self/preferences` | [x] ✅ |

### 4.2 星球高级功能

| 接口 | 方法名 | 端点 | 状态 |
|------|--------|------|------|
| 获取续费信息 | `getRenewalInfo(groupId)` | `GET /v2/groups/{group_id}/renewal` | [x] ✅ |
| 获取分销信息 | `getDistribution(groupId)` | `GET /v2/groups/{group_id}/distribution` | [x] ✅ |
| 获取可升级星球列表 | `getUpgradeableGroups()` | `GET /v2/groups/upgradable_groups` | [x] ✅ |
| 获取推荐星球列表 | `getRecommendedGroups()` | `GET /v2/groups/recommendations` | [x] ✅ |
| 获取自定义标签 | `getCustomTags(groupId)` | `GET /v2/groups/{group_id}/labels` | [x] ✅ |
| 获取定时任务 | `getScheduledTasks(groupId)` | `GET /v2/groups/{group_id}/scheduled_jobs` | [x] ✅ |
| 获取风险预警 | `getRiskWarnings(groupId)` | `GET /v3/groups/{group_id}/group_warning` | [x] ✅ |

### 4.3 多语言 SDK 同步

| 语言 | 同步范围 | 状态 |
|------|---------|------|
| TypeScript | 所有新增接口 | [ ] |
| Go | 所有新增接口 | [ ] |
| Python | 所有新增接口 | [ ] |

### Sprint 4 验收标准

- [x] Java SDK Sprint 4.1/4.2 接口覆盖（新增 17 个接口）
- [x] 新增 12 个模型
- [x] 所有代码编译通过
- [x] 集成测试代码编写完成
- [ ] TypeScript SDK 同步完成
- [ ] Go SDK 同步完成
- [ ] Python SDK 同步完成
- [ ] 更新所有文档

---

## 测试策略

### 单元测试

每个新方法需要对应的单元测试:

```java
// 示例: TopicsRequestTest.java
@Test
void testGetInfo() {
    // Mock HTTP 响应
    // 调用方法
    // 验证返回结果
}
```

### 集成测试

需要真实 Token 和 Group ID:

```bash
# 运行集成测试
ZSXQ_TOKEN="xxx" ZSXQ_GROUP_ID="xxx" mvn test -Dtest=IntegrationTest
```

### 测试环境变量

| 变量 | 说明 | 必需 |
|------|------|------|
| `ZSXQ_TOKEN` | 认证 Token | ✅ |
| `ZSXQ_GROUP_ID` | 测试星球 ID | ✅ |
| `ZSXQ_TOPIC_ID` | 测试话题 ID | 可选 |
| `ZSXQ_CHECKIN_ID` | 测试打卡项目 ID | 可选 |
| `ZSXQ_USER_ID` | 测试用户 ID | 可选 |

---

## 开发规范

### 代码模板

**新增 Request 方法模板**:

```java
/**
 * 获取XXX
 *
 * @param groupId 星球ID
 * @return XXX对象
 */
public XXX getXxx(long groupId) {
    return getXxx(String.valueOf(groupId));
}

public XXX getXxx(String groupId) {
    Map<String, Object> data = httpClient.get(
            "/v2/groups/" + groupId + "/xxx",
            new TypeToken<Map<String, Object>>() {}.getType());
    Object obj = data.get("xxx");
    if (obj == null) return null;
    String json = gson.toJson(obj);
    return gson.fromJson(json, XXX.class);
}
```

**新增 Model 模板**:

```java
package com.zsxq.sdk.model;

/**
 * XXX模型
 */
public class Xxx {
    private Long xxxId;
    private String name;
    private String description;
    // ... 其他字段

    // Getters and Setters
}
```

### 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 获取单个 | `get` + 名词 | `getMenu()`, `getColumn()` |
| 获取列表 | `get` + 名词复数 | `getMenus()`, `getColumns()` |
| 获取统计 | `get` + 名词 + `Stats`/`Statistics` | `getScoreStats()` |
| 列表(分页) | `list` + 名词 | `listTopics()`, `listByHashtag()` |
| 创建 | `create` + 名词 | `createCheckin()` |
| 更新 | `update` + 名词 | `updateCheckin()` |
| 删除 | `delete` + 名词 | `deleteCheckin()` |

---

## 风险与应对

| 风险 | 影响 | 应对措施 |
|------|------|---------|
| API 变更 | 方法失效 | 定期抓包验证，版本兼容 |
| Token 失效 | 集成测试失败 | 提供刷新 Token 指南 |
| 限流 | 测试被阻断 | 添加重试机制，降低测试频率 |
| 响应结构变化 | 解析失败 | 使用宽松解析，记录 warning |

---

## 参考文档

- [API 完整文档](archive/v0.1/Fiddler原始API文档.md)
- [测试状态追踪](TESTING_STATUS.md)
- [SDK 架构设计](../spec/sdk-design/architecture.md)
- [错误码定义](../spec/errors/error-codes.yaml)

---

## 更新日志

| 日期 | 版本 | 更新内容 |
|------|------|---------|
| 2025-12-11 | v1.0 | 初始计划创建 |
| 2025-12-12 | v1.1 | Sprint 4.1/4.2 完成 - Java SDK 新增 17 个接口和 12 个模型 |
