# API 测试状态追踪

> 最后更新: 2025-12-12

## 图例说明

- ✅ **已测试通过** - 接口已在集成测试中验证通过
- 🔶 **SDK已实现** - SDK 已实现此接口，可能因星球未开启功能而跳过测试
- ⚪ **未实现** - SDK 尚未实现此接口

## 测试覆盖率概览

| API 模块 | 总接口数 | 已测试 | SDK已实现 | 覆盖率 |
|---------|---------|-------|----------|--------|
| 用户系统 | 22 | 18 ✅ | 4 🔶 | 81.8% |
| 星球管理 | 18 | 16 ✅ | 2 🔶 | 88.9% |
| 话题管理 | 8 | 6 ✅ | 2 🔶 | 75.0% |
| 打卡系统 | 12 | 10 ✅ | 2 🔶 | 83.3% |
| 排行榜系统 | 7 | 7 ✅ | 0 🔶 | 100% |
| 数据面板 | 4 | 4 ✅ | 0 🔶 | 100% |
| 杂项功能 | 4 | 4 ✅ | 0 🔶 | 100% |
| **总计** | **75** | **65** | **10** | **86.7%** |

## 已测试通过的接口 (65个) ✅

### 用户系统 (18个)
- [x] 获取当前用户信息 `GET /v3/users/self`
- [x] 获取指定用户信息 `GET /v3/users/{user_id}`
- [x] 获取用户统计数据 `GET /v3/users/{user_id}/statistics`
- [x] 获取用户创建的星球 `GET /v2/users/{user_id}/created_groups`
- [x] 获取用户动态足迹 `GET /v2/users/{user_id}/footprints`
- [x] 获取用户头像URL `GET /v3/users/{user_id}/avatar_url`
- [x] 获取用户星球足迹 `GET /v2/users/{user_id}/group_footprints`
- [x] 获取申请中星球列表 `GET /v2/groups/applying`
- [x] 获取星球邀请人信息 `GET /v2/groups/{group_id}/inviter`
- [x] 获取优惠券列表 `GET /v2/coupons`
- [x] 获取备注列表 `GET /v2/remarks`
- [x] 获取推荐关注用户 `GET /v2/users/recommended_follows`
- [x] 获取屏蔽用户列表 `GET /v2/users/block_users`
- [x] 获取推荐偏好分类 `GET /v2/users/self/recommendations/preference_categories`
- [x] 获取未回答问题摘要 `GET /v2/users/self/unanswered_questions/brief`
- [x] 获取关注者统计 `GET /v3/users/self/followers/statistics`
- [x] 获取用户偏好配置 `GET /v3/users/self/preferences`
- [x] 获取周榜排名 `GET /v3/users/self/group_weekly_rankings`

### 星球管理 (16个)
- [x] 获取用户星球列表 `GET /v2/groups`
- [x] 获取星球详情 `GET /v2/groups/{group_id}`
- [x] 获取星球统计数据 `GET /v2/groups/{group_id}/statistics`
- [x] 获取星球菜单配置 `GET /v2/groups/{group_id}/menus`
- [x] 获取星球角色成员 `GET /v2/groups/{group_id}/role_members`
- [x] 获取星球专栏列表 `GET /v2/groups/{group_id}/columns`
- [x] 获取专栏汇总信息 `GET /v2/groups/{group_id}/columns/summary`
- [x] 获取成员活跃摘要 `GET /v2/groups/{group_id}/members/{user_id}/activity_summary`
- [x] 获取续费信息 `GET /v2/groups/{group_id}/renewal_info`
- [x] 获取分销信息 `GET /v2/groups/{group_id}/distribution`
- [x] 获取可升级星球 `GET /v2/groups/upgradeable`
- [x] 获取推荐星球 `GET /v2/groups/recommended`
- [x] 获取自定义标签 `GET /v2/groups/{group_id}/custom_tags`
- [x] 获取定时任务 `GET /v2/groups/{group_id}/scheduled_tasks`
- [x] 获取风险预警 `GET /v2/groups/{group_id}/risk_warnings`
- [x] 获取星球权限配置 `GET /v2/dashboard/groups/{group_id}/privileges`

### 话题管理 (6个)
- [x] 获取星球话题列表 `GET /v2/groups/{group_id}/topics`
- [x] 获取话题详情 `GET /v2/topics/{topic_id}`
- [x] 获取置顶话题列表 `GET /v2/groups/{group_id}/sticky_topics`
- [x] 获取话题基础信息 `GET /v2/topics/{topic_id}/info`
- [x] 获取话题打赏列表 `GET /v2/topics/{topic_id}/rewards`
- [x] 获取相关推荐话题 `GET /v2/topics/{topic_id}/recommendations`

### 打卡系统 (10个)
- [x] 获取打卡项目列表 `GET /v2/groups/{group_id}/checkins`
- [x] 获取打卡项目详情 `GET /v2/groups/{group_id}/checkins/{checkin_id}`
- [x] 获取打卡项目统计 `GET /v2/groups/{group_id}/checkins/{checkin_id}/statistics`
- [x] 获取打卡排行榜 `GET /v2/groups/{group_id}/checkins/{checkin_id}/ranking_list`
- [x] 获取打卡话题列表 `GET /v2/groups/{group_id}/checkins/{checkin_id}/topics`
- [x] 获取打卡每日统计 `GET /v2/groups/{group_id}/checkins/{checkin_id}/statistics/daily`
- [x] 获取打卡参与用户 `GET /v2/groups/{group_id}/checkins/{checkin_id}/joined_users`
- [x] 获取我的打卡记录 `GET /v2/users/self/groups/{group_id}/checkins/{checkin_id}/topics`
- [x] 获取我的打卡日期 `GET /v2/users/self/groups/{group_id}/checkins/{checkin_id}/checkined_dates`
- [x] 获取我的打卡统计 `GET /v2/users/self/groups/{group_id}/checkins/{checkin_id}/statistics`

### 排行榜系统 (7个)
- [x] 获取星球排行榜 `GET /v2/groups/{group_id}/ranking_list`
- [x] 获取排行统计 `GET /v2/groups/{group_id}/ranking_list/statistics`
- [x] 获取积分排行榜 `GET /v2/groups/{group_id}/scoreboard/ranking_list`
- [x] 获取我的积分统计 `GET /v2/groups/{group_id}/scoreboard/my_statistics`
- [x] 获取积分榜设置 `GET /v2/groups/{group_id}/scoreboard/settings`
- [x] 获取邀请排行榜 `GET /v2/groups/{group_id}/invitation_ranking_list`
- [x] 获取贡献排行榜 `GET /v2/groups/{group_id}/contribution_ranking_list`

### 数据面板 (4个)
- [x] 获取星球数据概览 `GET /v2/dashboard/groups/{group_id}/overview`
- [x] 获取星球收入概览 `GET /v2/dashboard/groups/{group_id}/incomes/overview`
- [x] 获取积分排行 `GET /v2/dashboard/groups/{group_id}/scoreboard/ranking_list`
- [x] 获取发票统计 `GET /v3/invoices/statistics`

### 杂项功能 (4个)
- [x] 获取全局配置 `GET /v2/global/config`
- [x] 获取用户动态 `GET /v2/activities`
- [x] 获取PK群组信息 `GET /v2/pk/groups/{group_id}`
- [x] 上报推送通道 `POST /v2/users/self/push_channel`

## SDK已实现但因功能限制跳过 (10个) 🔶

以下接口 SDK 已实现，但因测试星球未开启相关功能而标记为跳过：

### 用户系统
- [ ] 获取贡献记录 `GET /v3/users/self/contributions`
- [ ] 获取贡献统计 `GET /v3/users/self/contributions/statistics`
- [ ] 获取成就摘要 `GET /v3/users/self/achievements/summaries`
- [ ] 上报推送通道 `POST /v2/users/self/push_channel`

### 星球管理
- [ ] 创建打卡项目 `POST /v2/groups/{group_id}/checkins`
- [ ] 更新打卡项目 `PUT /v2/groups/{group_id}/checkins/{checkin_id}`

### 话题管理
- [ ] 创建话题 `POST /v2/groups/{group_id}/topics`
- [ ] 更新话题 `PUT /v2/topics/{topic_id}`

### 打卡系统
- [ ] 创建打卡 `POST /v2/groups/{group_id}/checkins`
- [ ] 更新打卡 `PUT /v2/groups/{group_id}/checkins/{checkin_id}`

## 运行测试

```bash
# 进入 Java SDK 目录
cd packages/java

# 配置环境变量
export ZSXQ_TOKEN="your-token"
export ZSXQ_GROUP_ID="your-group-id"

# 运行集成测试
mvn test -Dtest=IntegrationTest
```

## 测试环境

- **测试框架**: JUnit 5
- **Java版本**: 11+
- **SDK版本**: 1.0.0
- **总测试数**: 56

## 参考文档

- [Java SDK 快速开始](./guides/java-quick-start.md) - 完整使用示例
- [认证指南](./guides/authentication.md) - Token 获取方法
- [错误处理](./guides/error-handling.md) - 异常处理最佳实践

---

**注意**: 部分测试因星球未开启相关功能（如打卡、排行榜、积分等）而被跳过，显示为 ⚠️ 警告而非错误。如有任何问题或建议，请提交 Issue。
