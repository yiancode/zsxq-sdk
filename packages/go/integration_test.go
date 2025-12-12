// 集成测试 - 需要真实 Token 和 Group ID
//
// 运行方式:
// ZSXQ_TOKEN="xxx" ZSXQ_GROUP_ID="xxx" go test -v -run Integration
//
// 注意: 部分测试可能会因星球未开启相关功能而跳过（如打卡、排行榜等）

package zsxq_test

import (
	"context"
	"os"
	"strconv"
	"testing"

	"github.com/zsxq-sdk/zsxq-sdk-go/client"
	"github.com/zsxq-sdk/zsxq-sdk-go/request"
)

var (
	testClient *client.Client
	testGroupID int64
	skipTests   bool
)

func TestMain(m *testing.M) {
	token := os.Getenv("ZSXQ_TOKEN")
	groupIDStr := os.Getenv("ZSXQ_GROUP_ID")

	if token == "" || groupIDStr == "" {
		skipTests = true
		os.Exit(0)
	}

	var err error
	testGroupID, err = strconv.ParseInt(groupIDStr, 10, 64)
	if err != nil {
		skipTests = true
		os.Exit(0)
	}

	testClient = client.NewBuilder().
		SetToken(token).
		MustBuild()

	os.Exit(m.Run())
}

func skipIfNeeded(t *testing.T) {
	if skipTests {
		t.Skip("跳过集成测试：未设置 ZSXQ_TOKEN 或 ZSXQ_GROUP_ID 环境变量")
	}
}

// ========== 核心 API 测试 ==========

func TestIntegration_GetCurrentUser(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	user, err := testClient.Users().Self(ctx)
	if err != nil {
		t.Fatalf("获取当前用户失败: %v", err)
	}

	if user.UserID == "" {
		t.Error("用户ID为空")
	}
	if user.Name == "" {
		t.Error("用户名为空")
	}
	t.Logf("✅ 当前用户: %s (ID: %s)", user.Name, user.UserID)
}

func TestIntegration_GetGroups(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	groups, err := testClient.Groups().List(ctx)
	if err != nil {
		t.Fatalf("获取星球列表失败: %v", err)
	}

	if len(groups) == 0 {
		t.Error("星球列表为空")
	}
	t.Logf("✅ 已加入 %d 个星球", len(groups))
	for i, g := range groups {
		if i >= 3 {
			break
		}
		t.Logf("   - %s (ID: %d)", g.Name, g.GroupID)
	}
}

func TestIntegration_GetGroupDetail(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	group, err := testClient.Groups().Get(ctx, testGroupID)
	if err != nil {
		t.Fatalf("获取星球详情失败: %v", err)
	}

	if group.Name == "" {
		t.Error("星球名称为空")
	}
	t.Logf("✅ 星球详情: %s", group.Name)
}

func TestIntegration_GetGroupStatistics(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	stats, err := testClient.Groups().GetStatistics(ctx, testGroupID)
	if err != nil {
		t.Logf("⚠️ 获取星球统计失败: %v", err)
		return
	}
	t.Logf("✅ 星球统计: %v", stats)
}

func TestIntegration_GetTopics(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	topics, err := testClient.Topics().List(ctx, testGroupID, nil)
	if err != nil {
		t.Logf("⚠️ 获取话题列表失败: %v", err)
		return
	}

	t.Logf("✅ 获取到 %d 个话题", len(topics))
	if len(topics) > 0 {
		t.Logf("   - 第一个话题ID: %d", topics[0].TopicID)
	}
}

func TestIntegration_GetUserStatistics(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	user, err := testClient.Users().Self(ctx)
	if err != nil {
		t.Fatalf("获取当前用户失败: %v", err)
	}

	stats, err := testClient.Users().GetStatistics(ctx, user.UserID)
	if err != nil {
		t.Fatalf("获取用户统计失败: %v", err)
	}
	t.Logf("✅ 用户统计: %v", stats)
}

// ========== 星球功能测试 ==========

func TestIntegration_GetGroupMenus(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	menus, err := testClient.Groups().GetMenus(ctx, testGroupID)
	if err != nil {
		t.Logf("⚠️ 获取星球菜单配置失败: %v", err)
		return
	}
	t.Logf("✅ 获取到 %d 个菜单配置", len(menus))
}

func TestIntegration_GetGroupRoleMembers(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	roleMembers, err := testClient.Groups().GetRoleMembers(ctx, testGroupID)
	if err != nil {
		t.Logf("⚠️ 获取角色成员失败: %v", err)
		return
	}
	if roleMembers.Owner != nil {
		t.Logf("✅ 星主: %s", roleMembers.Owner.Name)
	}
}

func TestIntegration_GetGroupColumns(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	columns, err := testClient.Groups().GetColumns(ctx, testGroupID)
	if err != nil {
		t.Fatalf("获取专栏列表失败: %v", err)
	}
	t.Logf("✅ 获取到 %d 个专栏", len(columns))
}

func TestIntegration_GetColumnsSummary(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	summary, err := testClient.Groups().GetColumnsSummary(ctx, testGroupID)
	if err != nil {
		t.Fatalf("获取专栏汇总失败: %v", err)
	}
	t.Logf("✅ 专栏汇总: %v", summary)
}

func TestIntegration_GetMemberActivitySummary(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	user, err := testClient.Users().Self(ctx)
	if err != nil {
		t.Fatalf("获取当前用户失败: %v", err)
	}

	userID, _ := strconv.ParseInt(user.UserID, 10, 64)
	summary, err := testClient.Groups().GetMemberActivitySummary(ctx, testGroupID, userID)
	if err != nil {
		t.Logf("⚠️ 获取成员活跃摘要失败: %v", err)
		return
	}
	t.Logf("✅ 成员活跃摘要: 话题数=%d", summary.TopicsCount)
}

func TestIntegration_GetStickyTopics(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	topics, err := testClient.Topics().ListSticky(ctx, testGroupID)
	if err != nil {
		t.Logf("⚠️ 获取置顶话题失败: %v", err)
		return
	}
	t.Logf("✅ 获取到 %d 个置顶话题", len(topics))
}

func TestIntegration_GetTopicInfo(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	topics, err := testClient.Topics().List(ctx, testGroupID, nil)
	if err != nil || len(topics) == 0 {
		t.Skip("没有话题可测试")
	}

	info, err := testClient.Topics().GetInfo(ctx, topics[0].TopicID)
	if err != nil {
		t.Logf("⚠️ 获取话题基础信息失败: %v", err)
		return
	}
	t.Logf("✅ 话题基础信息: ID=%d", info.TopicID)
}

func TestIntegration_GetTopicRewards(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	topics, err := testClient.Topics().List(ctx, testGroupID, nil)
	if err != nil || len(topics) == 0 {
		t.Skip("没有话题可测试")
	}

	rewards, err := testClient.Topics().GetRewards(ctx, topics[0].TopicID)
	if err != nil {
		t.Logf("⚠️ 获取话题打赏失败: %v", err)
		return
	}
	t.Logf("✅ 话题打赏数: %d", len(rewards))
}

func TestIntegration_GetTopicRecommendations(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	topics, err := testClient.Topics().List(ctx, testGroupID, nil)
	if err != nil || len(topics) == 0 {
		t.Skip("没有话题可测试")
	}

	recommendations, err := testClient.Topics().GetRecommendations(ctx, topics[0].TopicID)
	if err != nil {
		t.Logf("⚠️ 获取相关推荐话题失败: %v", err)
		return
	}
	t.Logf("✅ 相关推荐话题数: %d", len(recommendations))
}

// ========== 排行榜功能测试 ==========

func TestIntegration_GetGroupRanking(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	ranking, err := testClient.Ranking().GetGroupRanking(ctx, testGroupID, nil)
	if err != nil {
		t.Logf("⚠️ 星球排行榜功能未开启: %v", err)
		return
	}
	t.Logf("✅ 获取到 %d 条排行记录", len(ranking))
}

func TestIntegration_GetGroupRankingStats(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	stats, err := testClient.Ranking().GetGroupRankingStats(ctx, testGroupID)
	if err != nil {
		t.Logf("⚠️ 排行统计功能未开启: %v", err)
		return
	}
	t.Logf("✅ 排行统计: %+v", stats)
}

func TestIntegration_GetScoreRanking(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	ranking, err := testClient.Ranking().GetScoreRanking(ctx, testGroupID, nil)
	if err != nil {
		t.Logf("⚠️ 积分排行功能未开启: %v", err)
		return
	}
	t.Logf("✅ 获取到 %d 条积分排行", len(ranking))
}

func TestIntegration_GetMyScoreStats(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	stats, err := testClient.Ranking().GetMyScoreStats(ctx, testGroupID)
	if err != nil {
		t.Logf("⚠️ 我的积分统计功能未开启: %v", err)
		return
	}
	t.Logf("✅ 我的积分统计: %v", stats)
}

func TestIntegration_GetScoreboardSettings(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	settings, err := testClient.Ranking().GetScoreboardSettings(ctx, testGroupID)
	if err != nil {
		t.Logf("⚠️ 积分榜设置功能未开启: %v", err)
		return
	}
	t.Logf("✅ 积分榜设置: 启用=%v", settings.Enabled)
}

func TestIntegration_GetInvitationRanking(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	ranking, err := testClient.Ranking().GetInvitationRanking(ctx, testGroupID, nil)
	if err != nil {
		t.Logf("⚠️ 邀请排行功能未开启: %v", err)
		return
	}
	t.Logf("✅ 获取到 %d 条邀请排行", len(ranking))
}

func TestIntegration_GetContributionRanking(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	ranking, err := testClient.Ranking().GetContributionRanking(ctx, testGroupID, nil)
	if err != nil {
		t.Logf("⚠️ 贡献排行功能未开启: %v", err)
		return
	}
	t.Logf("✅ 获取到 %d 条贡献排行", len(ranking))
}

func TestIntegration_GetGlobalRanking(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	ranking, err := testClient.Ranking().GetGlobalRanking(ctx, "group_sales_list", 10)
	if err != nil {
		t.Logf("⚠️ 全局星球排行榜功能未开启: %v", err)
		return
	}
	t.Logf("✅ 全局星球排行榜数据: %v", ranking)
}

// ========== 用户功能测试 ==========

func TestIntegration_GetAvatarURL(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	user, err := testClient.Users().Self(ctx)
	if err != nil {
		t.Fatalf("获取当前用户失败: %v", err)
	}

	avatarURL, err := testClient.Users().GetAvatarURL(ctx, user.UserID)
	if err != nil {
		t.Logf("⚠️ 获取头像URL失败: %v", err)
		return
	}
	if len(avatarURL) > 50 {
		t.Logf("✅ 用户头像URL: %s...", avatarURL[:50])
	} else {
		t.Logf("✅ 用户头像URL: %s", avatarURL)
	}
}

func TestIntegration_GetGroupFootprints(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	user, err := testClient.Users().Self(ctx)
	if err != nil {
		t.Fatalf("获取当前用户失败: %v", err)
	}

	footprints, err := testClient.Users().GetGroupFootprints(ctx, user.UserID)
	if err != nil {
		t.Logf("⚠️ 获取星球足迹失败: %v", err)
		return
	}
	t.Logf("✅ 获取到 %d 条星球足迹", len(footprints))
}

func TestIntegration_GetApplyingGroups(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	groups, err := testClient.Users().GetApplyingGroups(ctx)
	if err != nil {
		t.Logf("⚠️ 获取申请中星球失败: %v", err)
		return
	}
	t.Logf("✅ 申请中星球数: %d", len(groups))
}

func TestIntegration_GetInviter(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	inviter, err := testClient.Users().GetInviter(ctx, testGroupID)
	if err != nil {
		t.Logf("⚠️ 获取邀请人失败: %v", err)
		return
	}
	if inviter.User != nil {
		t.Logf("✅ 邀请人: %s", inviter.User.Name)
	} else {
		t.Log("⚠️ 无邀请人信息")
	}
}

func TestIntegration_GetCoupons(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	coupons, err := testClient.Users().GetCoupons(ctx)
	if err != nil {
		t.Logf("⚠️ 获取优惠券失败: %v", err)
		return
	}
	t.Logf("✅ 获取到 %d 张优惠券", len(coupons))
}

func TestIntegration_GetRemarks(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	remarks, err := testClient.Users().GetRemarks(ctx)
	if err != nil {
		t.Logf("⚠️ 获取备注失败: %v", err)
		return
	}
	t.Logf("✅ 获取到 %d 条备注", len(remarks))
}

func TestIntegration_GetRecommendedFollows(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	users, err := testClient.Users().GetRecommendedFollows(ctx)
	if err != nil {
		t.Logf("⚠️ 获取推荐关注失败: %v", err)
		return
	}
	t.Logf("✅ 推荐关注用户数: %d", len(users))
}

func TestIntegration_GetBlockedUsers(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	users, err := testClient.Users().GetBlockedUsers(ctx)
	if err != nil {
		t.Logf("⚠️ 获取屏蔽用户失败: %v", err)
		return
	}
	t.Logf("✅ 屏蔽用户数: %d", len(users))
}

func TestIntegration_GetPreferenceCategories(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	categories, err := testClient.Users().GetPreferenceCategories(ctx)
	if err != nil {
		t.Logf("⚠️ 获取推荐偏好分类失败: %v", err)
		return
	}
	t.Logf("✅ 获取到 %d 个推荐偏好分类", len(categories))
}

func TestIntegration_GetUnansweredQuestionsSummary(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	summary, err := testClient.Users().GetUnansweredQuestionsSummary(ctx)
	if err != nil {
		t.Logf("⚠️ 获取未回答问题摘要失败: %v", err)
		return
	}
	t.Logf("✅ 未回答问题数: %d", summary.Count)
}

func TestIntegration_GetFollowerStats(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	stats, err := testClient.Users().GetFollowerStats(ctx)
	if err != nil {
		t.Logf("⚠️ 获取关注者统计失败: %v", err)
		return
	}
	t.Logf("✅ 关注者数: %d", stats.FollowersCount)
}

func TestIntegration_GetUserPreferences(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	preferences, err := testClient.Users().GetPreferences(ctx)
	if err != nil {
		t.Logf("⚠️ 获取用户偏好配置失败: %v", err)
		return
	}
	t.Logf("✅ 用户偏好配置键数: %d", len(preferences))
}

func TestIntegration_GetWeeklyRanking(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	ranking, err := testClient.Users().GetWeeklyRanking(ctx, testGroupID)
	if err != nil {
		t.Logf("⚠️ 获取周榜排名失败: %v", err)
		return
	}
	t.Logf("✅ 周榜排名: %+v", ranking)
}

// ========== 打卡功能测试 ==========

func TestIntegration_GetCheckinList(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	checkins, err := testClient.Checkins().List(ctx, testGroupID, &request.ListCheckinsOptions{
		Type:  "all",
		Count: 20,
	})
	if err != nil {
		t.Logf("⚠️ 打卡功能未开启: %v", err)
		return
	}
	t.Logf("✅ 获取到 %d 个打卡项目", len(checkins))
}

func TestIntegration_GetCheckinDetail(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	checkins, err := testClient.Checkins().List(ctx, testGroupID, &request.ListCheckinsOptions{
		Type:  "all",
		Count: 20,
	})
	if err != nil || len(checkins) == 0 {
		t.Skip("没有打卡项目可测试")
	}

	checkin, err := testClient.Checkins().Get(ctx, testGroupID, checkins[0].CheckinID)
	if err != nil {
		t.Logf("⚠️ 获取打卡详情失败: %v", err)
		return
	}
	t.Logf("✅ 打卡项目: %s", checkin.Name)
}

func TestIntegration_GetCheckinStatistics(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	checkins, err := testClient.Checkins().List(ctx, testGroupID, &request.ListCheckinsOptions{
		Type:  "all",
		Count: 20,
	})
	if err != nil || len(checkins) == 0 {
		t.Skip("没有打卡项目可测试")
	}

	stats, err := testClient.Checkins().GetStatistics(ctx, testGroupID, checkins[0].CheckinID)
	if err != nil {
		t.Logf("⚠️ 获取打卡统计失败: %v", err)
		return
	}
	t.Logf("✅ 打卡统计: %+v", stats)
}

func TestIntegration_GetCheckinRankingList(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	checkins, err := testClient.Checkins().List(ctx, testGroupID, &request.ListCheckinsOptions{
		Type:  "all",
		Count: 20,
	})
	if err != nil || len(checkins) == 0 {
		t.Skip("没有打卡项目可测试")
	}

	ranking, err := testClient.Checkins().GetRankingList(ctx, testGroupID, checkins[0].CheckinID, nil)
	if err != nil {
		t.Logf("⚠️ 获取打卡排行榜失败: %v", err)
		return
	}
	t.Logf("✅ 打卡排行记录数: %d", len(ranking))
}

func TestIntegration_GetCheckinDailyStatistics(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	checkins, err := testClient.Checkins().List(ctx, testGroupID, &request.ListCheckinsOptions{
		Type:  "all",
		Count: 20,
	})
	if err != nil || len(checkins) == 0 {
		t.Skip("没有打卡项目可测试")
	}

	stats, err := testClient.Checkins().GetDailyStatistics(ctx, testGroupID, checkins[0].CheckinID)
	if err != nil {
		t.Logf("⚠️ 获取打卡每日统计失败: %v", err)
		return
	}
	t.Logf("✅ 打卡每日统计记录数: %d", len(stats))
}

func TestIntegration_GetCheckinJoinedUsers(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	checkins, err := testClient.Checkins().List(ctx, testGroupID, &request.ListCheckinsOptions{
		Type:  "all",
		Count: 20,
	})
	if err != nil || len(checkins) == 0 {
		t.Skip("没有打卡项目可测试")
	}

	users, err := testClient.Checkins().GetJoinedUsers(ctx, testGroupID, checkins[0].CheckinID, nil)
	if err != nil {
		t.Logf("⚠️ 获取打卡参与用户失败: %v", err)
		return
	}
	t.Logf("✅ 打卡参与用户数: %d", len(users))
}

func TestIntegration_GetMyCheckins(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	checkins, err := testClient.Checkins().List(ctx, testGroupID, &request.ListCheckinsOptions{
		Type:  "all",
		Count: 20,
	})
	if err != nil || len(checkins) == 0 {
		t.Skip("没有打卡项目可测试")
	}

	myCheckins, err := testClient.Checkins().GetMyCheckins(ctx, testGroupID, checkins[0].CheckinID, nil)
	if err != nil {
		t.Logf("⚠️ 获取我的打卡记录失败: %v", err)
		return
	}
	t.Logf("✅ 我的打卡记录数: %d", len(myCheckins))
}

func TestIntegration_GetMyCheckinDays(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	checkins, err := testClient.Checkins().List(ctx, testGroupID, &request.ListCheckinsOptions{
		Type:  "all",
		Count: 20,
	})
	if err != nil || len(checkins) == 0 {
		t.Skip("没有打卡项目可测试")
	}

	days, err := testClient.Checkins().GetMyCheckinDays(ctx, testGroupID, checkins[0].CheckinID)
	if err != nil {
		t.Logf("⚠️ 获取我的打卡日期失败: %v", err)
		return
	}
	t.Logf("✅ 我的打卡日期数: %d", len(days))
}

func TestIntegration_GetMyCheckinStatistics(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	checkins, err := testClient.Checkins().List(ctx, testGroupID, &request.ListCheckinsOptions{
		Type:  "all",
		Count: 20,
	})
	if err != nil || len(checkins) == 0 {
		t.Skip("没有打卡项目可测试")
	}

	stats, err := testClient.Checkins().GetMyStatistics(ctx, testGroupID, checkins[0].CheckinID)
	if err != nil {
		t.Logf("⚠️ 获取我的打卡统计失败: %v", err)
		return
	}
	t.Logf("✅ 我的打卡统计: %+v", stats)
}

// ========== 数据面板测试 ==========

func TestIntegration_GetDashboardOverview(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	overview, err := testClient.Dashboard().GetOverview(ctx, testGroupID)
	if err != nil {
		t.Logf("⚠️ 获取星球数据概览失败: %v", err)
		return
	}
	t.Logf("✅ 星球数据概览: %v", overview)
}

func TestIntegration_GetDashboardIncomes(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	incomes, err := testClient.Dashboard().GetIncomes(ctx, testGroupID)
	if err != nil {
		t.Logf("⚠️ 获取星球收入概览失败: %v", err)
		return
	}
	t.Logf("✅ 星球收入概览: %v", incomes)
}

func TestIntegration_GetDashboardPrivileges(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	privileges, err := testClient.Dashboard().GetPrivileges(ctx, testGroupID)
	if err != nil {
		t.Logf("⚠️ 获取星球权限配置失败: %v", err)
		return
	}
	t.Logf("✅ 星球权限配置数: %d", len(privileges))
}

func TestIntegration_GetInvoiceStats(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	stats, err := testClient.Dashboard().GetInvoiceStats(ctx)
	if err != nil {
		t.Logf("⚠️ 获取发票统计失败: %v", err)
		return
	}
	t.Logf("✅ 发票统计: %+v", stats)
}

// ========== 杂项功能测试 ==========

func TestIntegration_GetGlobalConfig(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	config, err := testClient.Misc().GetGlobalConfig(ctx)
	if err != nil {
		t.Logf("⚠️ 获取全局配置失败: %v", err)
		return
	}
	t.Logf("✅ 全局配置: %+v", config)
}

func TestIntegration_GetActivities(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	activities, err := testClient.Misc().GetActivities(ctx, nil)
	if err != nil {
		t.Logf("⚠️ 获取用户动态失败: %v", err)
		return
	}
	t.Logf("✅ 获取到 %d 条动态", len(activities))
}

func TestIntegration_GetPkGroup(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	pkGroup, err := testClient.Misc().GetPkGroup(ctx, testGroupID)
	if err != nil {
		t.Logf("⚠️ 获取PK群组信息失败: %v", err)
		return
	}
	t.Logf("✅ PK群组: %+v", pkGroup)
}

// ========== 星球高级功能测试 ==========

func TestIntegration_GetRenewalInfo(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	renewal, err := testClient.Groups().GetRenewalInfo(ctx, testGroupID)
	if err != nil {
		t.Logf("⚠️ 获取续费信息失败: %v", err)
		return
	}
	t.Logf("✅ 续费信息: %+v", renewal)
}

func TestIntegration_GetDistribution(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	distribution, err := testClient.Groups().GetDistribution(ctx, testGroupID)
	if err != nil {
		t.Logf("⚠️ 获取分销信息失败: %v", err)
		return
	}
	t.Logf("✅ 分销信息: %+v", distribution)
}

func TestIntegration_GetUpgradeableGroups(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	groups, err := testClient.Groups().GetUpgradeableGroups(ctx)
	if err != nil {
		t.Logf("⚠️ 获取可升级星球失败: %v", err)
		return
	}
	t.Logf("✅ 可升级星球数: %d", len(groups))
}

func TestIntegration_GetRecommendedGroups(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	groups, err := testClient.Groups().GetRecommendedGroups(ctx)
	if err != nil {
		t.Logf("⚠️ 获取推荐星球失败: %v", err)
		return
	}
	t.Logf("✅ 推荐星球数: %d", len(groups))
}

func TestIntegration_GetCustomTags(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	tags, err := testClient.Groups().GetCustomTags(ctx, testGroupID)
	if err != nil {
		t.Logf("⚠️ 获取自定义标签失败: %v", err)
		return
	}
	t.Logf("✅ 自定义标签数: %d", len(tags))
}

func TestIntegration_GetScheduledTasks(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	tasks, err := testClient.Groups().GetScheduledTasks(ctx, testGroupID)
	if err != nil {
		t.Logf("⚠️ 获取定时任务失败: %v", err)
		return
	}
	t.Logf("✅ 定时任务数: %d", len(tasks))
}

func TestIntegration_GetRiskWarnings(t *testing.T) {
	skipIfNeeded(t)
	ctx := context.Background()

	warning, err := testClient.Groups().GetRiskWarnings(ctx, testGroupID)
	if err != nil {
		t.Logf("⚠️ 获取风险预警失败: %v", err)
		return
	}
	if warning.WarningType != "" {
		t.Logf("✅ 风险预警: %+v", warning)
	} else {
		t.Log("✅ 无风险预警")
	}
}
