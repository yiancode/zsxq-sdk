package request

import (
	"context"
	"fmt"
	"net/url"
	"strconv"

	"github.com/zsxq-sdk/zsxq-sdk-go/http"
	"github.com/zsxq-sdk/zsxq-sdk-go/model"
)

// GroupsRequest 星球请求模块
type GroupsRequest struct {
	client *http.Client
}

// NewGroupsRequest 创建星球请求模块
func NewGroupsRequest(client *http.Client) *GroupsRequest {
	return &GroupsRequest{client: client}
}

// groupsResponse 星球列表响应
type groupsResponse struct {
	Groups []model.Group `json:"groups"`
}

// groupResponse 星球详情响应
type groupResponse struct {
	Group model.Group `json:"group"`
}

// hashtagsResponse 标签列表响应
type hashtagsResponse struct {
	Hashtags []model.Hashtag `json:"hashtags"`
}

// userResponse 用户响应
type userResponse struct {
	User model.User `json:"user"`
}

// List 获取我的星球列表
func (r *GroupsRequest) List(ctx context.Context) ([]model.Group, error) {
	var resp groupsResponse
	if err := r.client.Get(ctx, "/v2/groups", nil, &resp); err != nil {
		return nil, err
	}
	return resp.Groups, nil
}

// Get 获取星球详情
func (r *GroupsRequest) Get(ctx context.Context, groupID int64) (*model.Group, error) {
	var resp groupResponse
	path := fmt.Sprintf("/v2/groups/%d", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return &resp.Group, nil
}

// GetHashtags 获取星球标签
func (r *GroupsRequest) GetHashtags(ctx context.Context, groupID int64) ([]model.Hashtag, error) {
	var resp hashtagsResponse
	path := fmt.Sprintf("/v2/groups/%d/hashtags", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return resp.Hashtags, nil
}

// GetStatistics 获取星球统计
func (r *GroupsRequest) GetStatistics(ctx context.Context, groupID int64) (map[string]interface{}, error) {
	var resp map[string]interface{}
	path := fmt.Sprintf("/v2/groups/%d/statistics", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return resp, nil
}

// GetMember 获取成员信息
func (r *GroupsRequest) GetMember(ctx context.Context, groupID, memberID int64) (*model.User, error) {
	var resp userResponse
	path := fmt.Sprintf("/v2/groups/%d/members/%d", groupID, memberID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return &resp.User, nil
}

// GetUnreadCount 获取未读话题数
func (r *GroupsRequest) GetUnreadCount(ctx context.Context) (map[string]int, error) {
	var resp map[string]int
	if err := r.client.Get(ctx, "/v2/groups/unread_topics_count", nil, &resp); err != nil {
		return nil, err
	}
	return resp, nil
}

// menusResponse 菜单列表响应
type menusResponse struct {
	Menus []model.Menu `json:"menus"`
}

// GetMenus 获取星球菜单配置
func (r *GroupsRequest) GetMenus(ctx context.Context, groupID int64) ([]model.Menu, error) {
	var resp menusResponse
	path := fmt.Sprintf("/v2/groups/%d/menus", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return resp.Menus, nil
}

// roleMembersResponse 角色成员响应
type roleMembersResponse struct {
	model.RoleMembers
}

// GetRoleMembers 获取星球角色成员（星主、合伙人、管理员）
func (r *GroupsRequest) GetRoleMembers(ctx context.Context, groupID int64) (*model.RoleMembers, error) {
	var resp roleMembersResponse
	path := fmt.Sprintf("/v2/groups/%d/role_members", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return &resp.RoleMembers, nil
}

// columnsResponse 专栏列表响应
type columnsResponse struct {
	Columns []model.Column `json:"columns"`
}

// GetColumns 获取星球专栏列表
func (r *GroupsRequest) GetColumns(ctx context.Context, groupID int64) ([]model.Column, error) {
	var resp columnsResponse
	path := fmt.Sprintf("/v2/groups/%d/columns", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return resp.Columns, nil
}

// GetColumnsSummary 获取专栏汇总信息
func (r *GroupsRequest) GetColumnsSummary(ctx context.Context, groupID int64) (map[string]interface{}, error) {
	var resp map[string]interface{}
	path := fmt.Sprintf("/v2/groups/%d/columns/summary", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return resp, nil
}

// activitySummaryResponse 活跃摘要响应
type activitySummaryResponse struct {
	Summary model.ActivitySummary `json:"summary"`
}

// GetMemberActivitySummary 获取成员活跃摘要
func (r *GroupsRequest) GetMemberActivitySummary(ctx context.Context, groupID, memberID int64) (*model.ActivitySummary, error) {
	var resp activitySummaryResponse
	path := fmt.Sprintf("/v2/groups/%d/members/%d/summary", groupID, memberID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return &resp.Summary, nil
}

// renewalInfoResponse 续费信息响应
type renewalInfoResponse struct {
	Renewal model.RenewalInfo `json:"renewal"`
}

// GetRenewalInfo 获取星球续费信息
func (r *GroupsRequest) GetRenewalInfo(ctx context.Context, groupID int64) (*model.RenewalInfo, error) {
	var resp renewalInfoResponse
	path := fmt.Sprintf("/v2/groups/%d/renewal", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return &resp.Renewal, nil
}

// distributionInfoResponse 分销信息响应
type distributionInfoResponse struct {
	Distribution model.DistributionInfo `json:"distribution"`
}

// GetDistribution 获取星球分销信息
func (r *GroupsRequest) GetDistribution(ctx context.Context, groupID int64) (*model.DistributionInfo, error) {
	var resp distributionInfoResponse
	path := fmt.Sprintf("/v2/groups/%d/distribution", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return &resp.Distribution, nil
}

// GetUpgradeableGroups 获取可升级星球列表
func (r *GroupsRequest) GetUpgradeableGroups(ctx context.Context) ([]model.Group, error) {
	var resp groupsResponse
	if err := r.client.Get(ctx, "/v2/groups/upgradable_groups", nil, &resp); err != nil {
		return nil, err
	}
	return resp.Groups, nil
}

// GetRecommendedGroups 获取推荐星球列表
func (r *GroupsRequest) GetRecommendedGroups(ctx context.Context) ([]model.Group, error) {
	var resp groupsResponse
	if err := r.client.Get(ctx, "/v2/groups/recommendations", nil, &resp); err != nil {
		return nil, err
	}
	return resp.Groups, nil
}

// customTagsResponse 自定义标签响应
type customTagsResponse struct {
	Labels []model.CustomTag `json:"labels"`
}

// GetCustomTags 获取星球自定义标签
func (r *GroupsRequest) GetCustomTags(ctx context.Context, groupID int64) ([]model.CustomTag, error) {
	var resp customTagsResponse
	path := fmt.Sprintf("/v2/groups/%d/labels", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return resp.Labels, nil
}

// scheduledJobsResponse 定时任务响应
type scheduledJobsResponse struct {
	Jobs []model.ScheduledJob `json:"jobs"`
}

// GetScheduledTasks 获取星球定时任务
func (r *GroupsRequest) GetScheduledTasks(ctx context.Context, groupID int64) ([]model.ScheduledJob, error) {
	var resp scheduledJobsResponse
	path := fmt.Sprintf("/v2/groups/%d/scheduled_jobs", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return resp.Jobs, nil
}

// groupWarningResponse 风险预警响应
type groupWarningResponse struct {
	Warning model.GroupWarning `json:"warning"`
}

// GetRiskWarnings 获取星球风险预警
func (r *GroupsRequest) GetRiskWarnings(ctx context.Context, groupID int64) (*model.GroupWarning, error) {
	var resp groupWarningResponse
	path := fmt.Sprintf("/v3/groups/%d/group_warning", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return &resp.Warning, nil
}

// TopicsRequest 话题请求模块
type TopicsRequest struct {
	client *http.Client
}

// NewTopicsRequest 创建话题请求模块
func NewTopicsRequest(client *http.Client) *TopicsRequest {
	return &TopicsRequest{client: client}
}

// ListTopicsOptions 话题列表查询参数
type ListTopicsOptions struct {
	Count          int    // 返回数量
	Scope          string // 查询范围: all/digests/by_owner
	Direction      string // 分页方向: forward/backward
	BeginTime      string // 开始时间 (ISO 8601)
	EndTime        string // 结束时间 (ISO 8601)
	WithInvisibles bool   // 是否包含隐藏话题
}

// topicsResponse 话题列表响应
type topicsResponse struct {
	Topics []model.Topic `json:"topics"`
}

// topicResponse 话题详情响应
type topicResponse struct {
	Topic model.Topic `json:"topic"`
}

// commentsResponse 评论列表响应
type commentsResponse struct {
	Comments []model.Comment `json:"comments"`
}

// List 获取话题列表
func (r *TopicsRequest) List(ctx context.Context, groupID int64, opts *ListTopicsOptions) ([]model.Topic, error) {
	params := url.Values{}
	if opts != nil {
		if opts.Count > 0 {
			params.Set("count", strconv.Itoa(opts.Count))
		}
		if opts.Scope != "" {
			params.Set("scope", opts.Scope)
		}
		if opts.Direction != "" {
			params.Set("direction", opts.Direction)
		}
		if opts.BeginTime != "" {
			params.Set("begin_time", opts.BeginTime)
		}
		if opts.EndTime != "" {
			params.Set("end_time", opts.EndTime)
		}
		if opts.WithInvisibles {
			params.Set("with_invisibles", "true")
		}
	}

	var resp topicsResponse
	path := fmt.Sprintf("/v2/groups/%d/topics", groupID)
	if err := r.client.Get(ctx, path, params, &resp); err != nil {
		return nil, err
	}
	return resp.Topics, nil
}

// Get 获取话题详情
func (r *TopicsRequest) Get(ctx context.Context, topicID int64) (*model.Topic, error) {
	var resp topicResponse
	path := fmt.Sprintf("/v2/topics/%d", topicID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return &resp.Topic, nil
}

// ListCommentsOptions 评论列表查询参数
type ListCommentsOptions struct {
	Count      int    // 返回数量
	Sort       string // 排序: asc/desc
	WithSticky bool   // 是否包含置顶
}

// GetComments 获取话题评论
func (r *TopicsRequest) GetComments(ctx context.Context, topicID int64, opts *ListCommentsOptions) ([]model.Comment, error) {
	params := url.Values{}
	if opts != nil {
		if opts.Count > 0 {
			params.Set("count", strconv.Itoa(opts.Count))
		}
		if opts.Sort != "" {
			params.Set("sort", opts.Sort)
		}
		if opts.WithSticky {
			params.Set("with_sticky", "true")
		}
	}

	var resp commentsResponse
	path := fmt.Sprintf("/v2/topics/%d/comments", topicID)
	if err := r.client.Get(ctx, path, params, &resp); err != nil {
		return nil, err
	}
	return resp.Comments, nil
}

// ListByHashtag 按标签获取话题
func (r *TopicsRequest) ListByHashtag(ctx context.Context, hashtagID int64, opts *ListTopicsOptions) ([]model.Topic, error) {
	params := url.Values{}
	if opts != nil {
		if opts.Count > 0 {
			params.Set("count", strconv.Itoa(opts.Count))
		}
		if opts.Scope != "" {
			params.Set("scope", opts.Scope)
		}
	}

	var resp topicsResponse
	path := fmt.Sprintf("/v2/hashtags/%d/topics", hashtagID)
	if err := r.client.Get(ctx, path, params, &resp); err != nil {
		return nil, err
	}
	return resp.Topics, nil
}

// ListByColumn 按专栏获取话题
func (r *TopicsRequest) ListByColumn(ctx context.Context, groupID, columnID int64, opts *ListTopicsOptions) ([]model.Topic, error) {
	params := url.Values{}
	if opts != nil {
		if opts.Count > 0 {
			params.Set("count", strconv.Itoa(opts.Count))
		}
	}

	var resp topicsResponse
	path := fmt.Sprintf("/v2/groups/%d/columns/%d/topics", groupID, columnID)
	if err := r.client.Get(ctx, path, params, &resp); err != nil {
		return nil, err
	}
	return resp.Topics, nil
}

// GetInfo 获取话题基础信息（轻量级）
func (r *TopicsRequest) GetInfo(ctx context.Context, topicID int64) (*model.Topic, error) {
	var resp topicResponse
	path := fmt.Sprintf("/v2/topics/%d/info", topicID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return &resp.Topic, nil
}

// rewardsResponse 打赏列表响应
type rewardsResponse struct {
	Rewards []model.Reward `json:"rewards"`
}

// GetRewards 获取话题打赏列表
func (r *TopicsRequest) GetRewards(ctx context.Context, topicID int64) ([]model.Reward, error) {
	var resp rewardsResponse
	path := fmt.Sprintf("/v2/topics/%d/rewards", topicID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return resp.Rewards, nil
}

// GetRecommendations 获取相关推荐话题
func (r *TopicsRequest) GetRecommendations(ctx context.Context, topicID int64) ([]model.Topic, error) {
	var resp topicsResponse
	path := fmt.Sprintf("/v2/topics/%d/recommendations", topicID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return resp.Topics, nil
}

// ListSticky 获取置顶话题列表
func (r *TopicsRequest) ListSticky(ctx context.Context, groupID int64) ([]model.Topic, error) {
	var resp topicsResponse
	path := fmt.Sprintf("/v2/groups/%d/topics/sticky", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return resp.Topics, nil
}

// UsersRequest 用户请求模块
type UsersRequest struct {
	client *http.Client
}

// NewUsersRequest 创建用户请求模块
func NewUsersRequest(client *http.Client) *UsersRequest {
	return &UsersRequest{client: client}
}

// Self 获取当前用户信息
func (r *UsersRequest) Self(ctx context.Context) (*model.User, error) {
	var resp userResponse
	if err := r.client.Get(ctx, "/v3/users/self", nil, &resp); err != nil {
		return nil, err
	}
	return &resp.User, nil
}

// Get 获取指定用户信息
func (r *UsersRequest) Get(ctx context.Context, userID string) (*model.User, error) {
	var resp userResponse
	path := fmt.Sprintf("/v3/users/%s", userID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return &resp.User, nil
}

// GetStatistics 获取用户统计
func (r *UsersRequest) GetStatistics(ctx context.Context, userID string) (map[string]interface{}, error) {
	var resp map[string]interface{}
	path := fmt.Sprintf("/v3/users/%s/statistics", userID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return resp, nil
}

// GetFootprints 获取用户动态
func (r *UsersRequest) GetFootprints(ctx context.Context, userID string) ([]model.Topic, error) {
	var resp topicsResponse
	path := fmt.Sprintf("/v2/users/%s/footprints", userID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return resp.Topics, nil
}

// GetCreatedGroups 获取用户创建的星球
func (r *UsersRequest) GetCreatedGroups(ctx context.Context, userID string) ([]model.Group, error) {
	var resp groupsResponse
	path := fmt.Sprintf("/v2/users/%s/created_groups", userID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return resp.Groups, nil
}

// GetAvatarURL 获取用户大尺寸头像URL
func (r *UsersRequest) GetAvatarURL(ctx context.Context, userID string) (string, error) {
	var resp struct {
		AvatarURL string `json:"avatar_url"`
	}
	path := fmt.Sprintf("/v3/users/%s/avatar_url", userID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return "", err
	}
	return resp.AvatarURL, nil
}

// GetGroupFootprints 获取用户星球足迹
func (r *UsersRequest) GetGroupFootprints(ctx context.Context, userID string, groupID ...int64) ([]model.Group, error) {
	path := fmt.Sprintf("/v2/users/%s/footprints/groups", userID)
	params := url.Values{}
	if len(groupID) > 0 && groupID[0] > 0 {
		params.Set("group_id", strconv.FormatInt(groupID[0], 10))
	}

	var resp groupsResponse
	if err := r.client.Get(ctx, path, params, &resp); err != nil {
		return nil, err
	}
	return resp.Groups, nil
}

// GetApplyingGroups 获取申请中的星球列表
func (r *UsersRequest) GetApplyingGroups(ctx context.Context) ([]model.Group, error) {
	var resp groupsResponse
	if err := r.client.Get(ctx, "/v2/groups/applying", nil, &resp); err != nil {
		return nil, err
	}
	return resp.Groups, nil
}

// inviterResponse 邀请人响应
type inviterResponse struct {
	Inviter model.Inviter `json:"inviter"`
}

// GetInviter 获取星球邀请人信息
func (r *UsersRequest) GetInviter(ctx context.Context, groupID int64) (*model.Inviter, error) {
	var resp inviterResponse
	path := fmt.Sprintf("/v2/users/self/groups/%d/inviter", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return &resp.Inviter, nil
}

// couponsResponse 优惠券列表响应
type couponsResponse struct {
	Coupons []model.Coupon `json:"coupons"`
}

// GetCoupons 获取我的优惠券列表
func (r *UsersRequest) GetCoupons(ctx context.Context) ([]model.Coupon, error) {
	var resp couponsResponse
	if err := r.client.Get(ctx, "/v2/users/self/merchant_coupons", nil, &resp); err != nil {
		return nil, err
	}
	return resp.Coupons, nil
}

// remarksResponse 备注列表响应
type remarksResponse struct {
	Remarks []model.Remark `json:"remarks"`
}

// GetRemarks 获取我的备注列表
func (r *UsersRequest) GetRemarks(ctx context.Context, beginTime ...string) ([]model.Remark, error) {
	path := "/v3/users/self/remarks"
	if len(beginTime) > 0 && beginTime[0] != "" {
		path = path + "?begin_time=" + url.QueryEscape(beginTime[0])
	} else {
		// 默认从1970年开始获取所有备注
		path = path + "?begin_time=1970-01-01T08:00:00.001%2B0800"
	}
	var resp remarksResponse
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return resp.Remarks, nil
}

// usersResponse 用户列表响应
type usersResponse struct {
	Users []*model.User `json:"users"`
}

// GetRecommendedFollows 获取推荐关注用户列表
func (r *UsersRequest) GetRecommendedFollows(ctx context.Context) ([]*model.User, error) {
	var resp usersResponse
	if err := r.client.Get(ctx, "/v2/users/recommended_follows", nil, &resp); err != nil {
		return nil, err
	}
	return resp.Users, nil
}

// GetBlockedUsers 获取屏蔽用户列表
func (r *UsersRequest) GetBlockedUsers(ctx context.Context) ([]*model.User, error) {
	var resp usersResponse
	if err := r.client.Get(ctx, "/v2/users/block_users", nil, &resp); err != nil {
		return nil, err
	}
	return resp.Users, nil
}

// ReportPushChannel 上报推送通道
func (r *UsersRequest) ReportPushChannel(ctx context.Context, channel, deviceToken string) error {
	body := map[string]interface{}{
		"channel":      channel,
		"device_token": deviceToken,
	}
	var resp map[string]interface{}
	return r.client.Post(ctx, "/v2/users/self/push_channel", body, &resp)
}

// preferenceCategoriesResponse 推荐偏好分类响应
type preferenceCategoriesResponse struct {
	Categories []model.PreferenceCategory `json:"categories"`
}

// GetPreferenceCategories 获取推荐偏好分类
func (r *UsersRequest) GetPreferenceCategories(ctx context.Context) ([]model.PreferenceCategory, error) {
	var resp preferenceCategoriesResponse
	if err := r.client.Get(ctx, "/v2/users/self/recommendations/preference_categories", nil, &resp); err != nil {
		return nil, err
	}
	return resp.Categories, nil
}

// GetUnansweredQuestionsSummary 获取未回答问题摘要
func (r *UsersRequest) GetUnansweredQuestionsSummary(ctx context.Context) (*model.UnansweredQuestionsSummary, error) {
	var resp model.UnansweredQuestionsSummary
	if err := r.client.Get(ctx, "/v2/users/self/unanswered_questions/brief", nil, &resp); err != nil {
		return nil, err
	}
	return &resp, nil
}

// GetFollowerStats 获取关注者统计
func (r *UsersRequest) GetFollowerStats(ctx context.Context, beginTime ...string) (*model.FollowerStatistics, error) {
	path := "/v3/users/self/followers/statistics"
	if len(beginTime) > 0 && beginTime[0] != "" {
		path = path + "?begin_time=" + url.QueryEscape(beginTime[0])
	}
	var resp model.FollowerStatistics
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return &resp, nil
}

// contributionsResponse 贡献记录响应
type contributionsResponse struct {
	Contributions []model.Contribution `json:"contributions"`
}

// GetContributions 获取贡献记录
func (r *UsersRequest) GetContributions(ctx context.Context, beginTime, endTime string) ([]model.Contribution, error) {
	path := "/v3/users/self/contributions"
	if beginTime != "" && endTime != "" {
		path = path + "?begin_time=" + url.QueryEscape(beginTime) + "&end_time=" + url.QueryEscape(endTime)
	}
	var resp contributionsResponse
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return resp.Contributions, nil
}

// contributionStatsResponse 贡献统计响应
type contributionStatsResponse struct {
	Statistics model.ContributionStatistics `json:"statistics"`
}

// GetContributionStats 获取贡献统计
func (r *UsersRequest) GetContributionStats(ctx context.Context) (*model.ContributionStatistics, error) {
	var resp contributionStatsResponse
	if err := r.client.Get(ctx, "/v3/users/self/contributions/statistics", nil, &resp); err != nil {
		return nil, err
	}
	return &resp.Statistics, nil
}

// achievementSummariesResponse 成就摘要列表响应
type achievementSummariesResponse struct {
	Summaries []model.AchievementSummary `json:"summaries"`
}

// GetAchievementsSummary 获取成就摘要列表
func (r *UsersRequest) GetAchievementsSummary(ctx context.Context) ([]model.AchievementSummary, error) {
	var resp achievementSummariesResponse
	if err := r.client.Get(ctx, "/v3/users/self/achievements/summaries", nil, &resp); err != nil {
		return nil, err
	}
	return resp.Summaries, nil
}

// GetWeeklyRanking 获取星球周榜排名
func (r *UsersRequest) GetWeeklyRanking(ctx context.Context, groupID int64) (*model.WeeklyRanking, error) {
	var resp model.WeeklyRanking
	path := fmt.Sprintf("/v3/users/self/group_weekly_rankings?group_id=%d", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return &resp, nil
}

// GetPreferences 获取用户偏好配置
func (r *UsersRequest) GetPreferences(ctx context.Context) (map[string]interface{}, error) {
	var resp map[string]interface{}
	if err := r.client.Get(ctx, "/v3/users/self/preferences", nil, &resp); err != nil {
		return nil, err
	}
	return resp, nil
}

// CheckinsRequest 打卡请求模块
type CheckinsRequest struct {
	client *http.Client
}

// NewCheckinsRequest 创建打卡请求模块
func NewCheckinsRequest(client *http.Client) *CheckinsRequest {
	return &CheckinsRequest{client: client}
}

// ListCheckinsOptions 打卡列表查询参数
type ListCheckinsOptions struct {
	Type  string // 类型: all/ongoing/closed
	Count int    // 返回数量
}

// checkinsResponse 打卡列表响应
type checkinsResponse struct {
	Checkins []model.Checkin `json:"checkins"`
}

// checkinResponse 打卡详情响应
type checkinResponse struct {
	Checkin model.Checkin `json:"checkin"`
}

// rankingResponse 排行榜响应
type rankingResponse struct {
	RankingList []model.RankingItem `json:"ranking_list"`
}

// List 获取打卡项目列表
func (r *CheckinsRequest) List(ctx context.Context, groupID int64, opts *ListCheckinsOptions) ([]model.Checkin, error) {
	params := url.Values{}
	if opts != nil {
		if opts.Type != "" {
			params.Set("type", opts.Type)
		}
		if opts.Count > 0 {
			params.Set("count", strconv.Itoa(opts.Count))
		}
	}

	var resp checkinsResponse
	path := fmt.Sprintf("/v2/groups/%d/checkins", groupID)
	if err := r.client.Get(ctx, path, params, &resp); err != nil {
		return nil, err
	}
	return resp.Checkins, nil
}

// Get 获取打卡项目详情
func (r *CheckinsRequest) Get(ctx context.Context, groupID, checkinID int64) (*model.Checkin, error) {
	var resp checkinResponse
	path := fmt.Sprintf("/v2/groups/%d/checkins/%d", groupID, checkinID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return &resp.Checkin, nil
}

// GetStatistics 获取打卡统计
func (r *CheckinsRequest) GetStatistics(ctx context.Context, groupID, checkinID int64) (*model.CheckinStatistics, error) {
	var resp model.CheckinStatistics
	path := fmt.Sprintf("/v2/groups/%d/checkins/%d/statistics", groupID, checkinID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return &resp, nil
}

// ListRankingOptions 排行榜查询参数
type ListRankingOptions struct {
	Type  string // 类型: continuous/accumulated
	Index int    // 分页索引
}

// GetRankingList 获取打卡排行榜
func (r *CheckinsRequest) GetRankingList(ctx context.Context, groupID, checkinID int64, opts *ListRankingOptions) ([]model.RankingItem, error) {
	params := url.Values{}
	if opts != nil {
		if opts.Type != "" {
			params.Set("type", opts.Type)
		}
		if opts.Index > 0 {
			params.Set("index", strconv.Itoa(opts.Index))
		}
	}

	var resp rankingResponse
	path := fmt.Sprintf("/v2/groups/%d/checkins/%d/ranking_list", groupID, checkinID)
	if err := r.client.Get(ctx, path, params, &resp); err != nil {
		return nil, err
	}
	return resp.RankingList, nil
}

// GetTopics 获取打卡话题
func (r *CheckinsRequest) GetTopics(ctx context.Context, groupID, checkinID int64, opts *ListTopicsOptions) ([]model.Topic, error) {
	params := url.Values{}
	if opts != nil {
		if opts.Count > 0 {
			params.Set("count", strconv.Itoa(opts.Count))
		}
	}

	var resp topicsResponse
	path := fmt.Sprintf("/v2/groups/%d/checkins/%d/topics", groupID, checkinID)
	if err := r.client.Get(ctx, path, params, &resp); err != nil {
		return nil, err
	}
	return resp.Topics, nil
}

// dailyStatisticsResponse 每日统计响应
type dailyStatisticsResponse struct {
	DailyStatistics []model.DailyStatistics `json:"daily_statistics"`
}

// GetDailyStatistics 获取打卡每日统计
func (r *CheckinsRequest) GetDailyStatistics(ctx context.Context, groupID, checkinID int64) ([]model.DailyStatistics, error) {
	var resp dailyStatisticsResponse
	path := fmt.Sprintf("/v2/groups/%d/checkins/%d/statistics/daily", groupID, checkinID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return resp.DailyStatistics, nil
}

// JoinedUsersOptions 参与用户查询参数
type JoinedUsersOptions struct {
	Count   int    // 返回数量
	EndTime string // 结束时间
}

// GetJoinedUsers 获取打卡参与用户列表
func (r *CheckinsRequest) GetJoinedUsers(ctx context.Context, groupID, checkinID int64, opts *JoinedUsersOptions) ([]*model.User, error) {
	params := url.Values{}
	if opts != nil {
		if opts.Count > 0 {
			params.Set("count", strconv.Itoa(opts.Count))
		}
		if opts.EndTime != "" {
			params.Set("end_time", opts.EndTime)
		}
	}

	var resp usersResponse
	path := fmt.Sprintf("/v2/groups/%d/checkins/%d/joined_users", groupID, checkinID)
	if err := r.client.Get(ctx, path, params, &resp); err != nil {
		return nil, err
	}
	return resp.Users, nil
}

// MyCheckinsOptions 我的打卡记录查询参数
type MyCheckinsOptions struct {
	Count   int    // 返回数量
	EndTime string // 结束时间
}

// GetMyCheckins 获取我的打卡记录
func (r *CheckinsRequest) GetMyCheckins(ctx context.Context, groupID, checkinID int64, opts *MyCheckinsOptions) ([]model.Topic, error) {
	params := url.Values{}
	if opts != nil {
		if opts.Count > 0 {
			params.Set("count", strconv.Itoa(opts.Count))
		}
		if opts.EndTime != "" {
			params.Set("end_time", opts.EndTime)
		}
	}

	var resp topicsResponse
	path := fmt.Sprintf("/v2/users/self/groups/%d/checkins/%d/topics", groupID, checkinID)
	if err := r.client.Get(ctx, path, params, &resp); err != nil {
		return nil, err
	}
	return resp.Topics, nil
}

// checkinDatesResponse 打卡日期列表响应
type checkinDatesResponse struct {
	Dates []string `json:"dates"`
}

// GetMyCheckinDays 获取我的打卡日期列表
func (r *CheckinsRequest) GetMyCheckinDays(ctx context.Context, groupID, checkinID int64) ([]string, error) {
	var resp checkinDatesResponse
	path := fmt.Sprintf("/v2/users/self/groups/%d/checkins/%d/checkined_dates", groupID, checkinID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return resp.Dates, nil
}

// myCheckinStatsResponse 我的打卡统计响应
type myCheckinStatsResponse struct {
	Statistics model.MyCheckinStatistics `json:"statistics"`
}

// GetMyStatistics 获取我的打卡统计
func (r *CheckinsRequest) GetMyStatistics(ctx context.Context, groupID, checkinID int64) (*model.MyCheckinStatistics, error) {
	var resp myCheckinStatsResponse
	path := fmt.Sprintf("/v2/users/self/groups/%d/checkins/%d/statistics", groupID, checkinID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return &resp.Statistics, nil
}

// CheckinValidity 打卡项目有效期配置
type CheckinValidity struct {
	// LongPeriod 是否长期有效
	LongPeriod bool `json:"long_period,omitempty"`
	// ExpirationTime 截止时间 (ISO 8601 格式，如 "2025-12-31T23:59:59.000+0800")
	ExpirationTime string `json:"expiration_time,omitempty"`
}

// CreateCheckinParams 创建打卡项目参数
//
// 基于实际 API 结构:
//
//	{
//	  "req_data": {
//	    "title": "训练营标题",
//	    "text": "训练营描述",
//	    "checkin_days": 7,
//	    "type": "accumulated",
//	    "show_topics_on_timeline": false,
//	    "validity": {
//	      "long_period": false,
//	      "expiration_time": "2025-12-24T23:59:59.798+0800"
//	    }
//	  }
//	}
type CreateCheckinParams struct {
	// Title 训练营标题
	Title string `json:"title"`
	// Text 训练营描述
	Text string `json:"text,omitempty"`
	// CheckinDays 打卡天数
	CheckinDays int `json:"checkin_days"`
	// Type 打卡类型: accumulated(累计打卡) / continuous(连续打卡)
	Type string `json:"type"`
	// ShowTopicsOnTimeline 是否在时间线展示
	ShowTopicsOnTimeline bool `json:"show_topics_on_timeline,omitempty"`
	// Validity 有效期配置
	Validity *CheckinValidity `json:"validity,omitempty"`
}

// Create 创建打卡项目（训练营）
//
// 示例 - 创建有截止时间的训练营:
//
//	params := request.CreateCheckinParams{
//	    Title:                "7天打卡挑战",
//	    Text:                 "每天完成一个任务",
//	    CheckinDays:          7,
//	    Type:                 "accumulated",
//	    ShowTopicsOnTimeline: false,
//	    Validity: &request.CheckinValidity{
//	        LongPeriod:     false,
//	        ExpirationTime: "2025-12-31T23:59:59.000+0800",
//	    },
//	}
//	checkin, err := client.Checkins().Create(ctx, groupID, params)
//
// 示例 - 创建长期有效的训练营:
//
//	params := request.CreateCheckinParams{
//	    Title:       "每日学习打卡",
//	    Text:        "持续学习，每天进步",
//	    CheckinDays: 21,
//	    Type:        "accumulated",
//	    Validity:    &request.CheckinValidity{LongPeriod: true},
//	}
//	checkin, err := client.Checkins().Create(ctx, groupID, params)
func (r *CheckinsRequest) Create(ctx context.Context, groupID int64, params CreateCheckinParams) (*model.Checkin, error) {
	body := map[string]interface{}{
		"req_data": params,
	}
	var resp checkinResponse
	path := fmt.Sprintf("/v2/groups/%d/checkins", groupID)
	if err := r.client.Post(ctx, path, body, &resp); err != nil {
		return nil, err
	}
	return &resp.Checkin, nil
}

// UpdateCheckinParams 更新打卡项目参数
type UpdateCheckinParams struct {
	// Title 训练营标题
	Title string `json:"title,omitempty"`
	// Text 训练营描述
	Text string `json:"text,omitempty"`
	// CheckinDays 打卡天数
	CheckinDays int `json:"checkin_days,omitempty"`
	// Type 打卡类型
	Type string `json:"type,omitempty"`
	// ShowTopicsOnTimeline 是否在时间线展示
	ShowTopicsOnTimeline *bool `json:"show_topics_on_timeline,omitempty"`
	// Validity 有效期配置
	Validity *CheckinValidity `json:"validity,omitempty"`
	// Status 打卡状态
	Status string `json:"status,omitempty"`
}

// Update 更新打卡项目
func (r *CheckinsRequest) Update(ctx context.Context, groupID, checkinID int64, params UpdateCheckinParams) (*model.Checkin, error) {
	body := map[string]interface{}{
		"req_data": params,
	}
	var resp checkinResponse
	path := fmt.Sprintf("/v2/groups/%d/checkins/%d", groupID, checkinID)
	if err := r.client.Put(ctx, path, body, &resp); err != nil {
		return nil, err
	}
	return &resp.Checkin, nil
}

// DashboardRequest Dashboard 请求模块
type DashboardRequest struct {
	client *http.Client
}

// NewDashboardRequest 创建 Dashboard 请求模块
func NewDashboardRequest(client *http.Client) *DashboardRequest {
	return &DashboardRequest{client: client}
}

// GetOverview 获取星球概览
func (r *DashboardRequest) GetOverview(ctx context.Context, groupID int64) (map[string]interface{}, error) {
	var resp map[string]interface{}
	path := fmt.Sprintf("/v2/dashboard/groups/%d/overview", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return resp, nil
}

// GetIncomes 获取收入概览
func (r *DashboardRequest) GetIncomes(ctx context.Context, groupID int64) (map[string]interface{}, error) {
	var resp map[string]interface{}
	path := fmt.Sprintf("/v2/dashboard/groups/%d/incomes/overview", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return resp, nil
}

// GetScoreboardRanking 获取积分排行
func (r *DashboardRequest) GetScoreboardRanking(ctx context.Context, groupID int64, opts *ListRankingOptions) ([]model.RankingItem, error) {
	params := url.Values{}
	if opts != nil {
		if opts.Type != "" {
			params.Set("type", opts.Type)
		}
		if opts.Index > 0 {
			params.Set("index", strconv.Itoa(opts.Index))
		}
	}

	var resp rankingResponse
	path := fmt.Sprintf("/v2/dashboard/groups/%d/scoreboard/ranking_list", groupID)
	if err := r.client.Get(ctx, path, params, &resp); err != nil {
		return nil, err
	}
	return resp.RankingList, nil
}

// privilegesResponse 权限配置响应
type privilegesResponse struct {
	Privileges map[string]interface{} `json:"privileges"`
}

// GetPrivileges 获取星球权限配置
func (r *DashboardRequest) GetPrivileges(ctx context.Context, groupID int64) (map[string]interface{}, error) {
	var resp privilegesResponse
	path := fmt.Sprintf("/v2/dashboard/groups/%d/privileges", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return resp.Privileges, nil
}

// invoiceStatsResponse 发票统计响应
type invoiceStatsResponse struct {
	TotalAmount   int `json:"total_amount"`
	TotalCount    int `json:"total_count"`
	PendingAmount int `json:"pending_amount"`
	PendingCount  int `json:"pending_count"`
}

// GetInvoiceStats 获取发票统计
func (r *DashboardRequest) GetInvoiceStats(ctx context.Context) (*model.InvoiceStats, error) {
	var resp invoiceStatsResponse
	if err := r.client.Get(ctx, "/v3/invoices/statistics", nil, &resp); err != nil {
		return nil, err
	}
	return &model.InvoiceStats{
		TotalAmount:   resp.TotalAmount,
		TotalCount:    resp.TotalCount,
		PendingAmount: resp.PendingAmount,
		PendingCount:  resp.PendingCount,
	}, nil
}

// RankingRequest 排行榜请求模块
type RankingRequest struct {
	client *http.Client
}

// NewRankingRequest 创建排行榜请求模块
func NewRankingRequest(client *http.Client) *RankingRequest {
	return &RankingRequest{client: client}
}

// rankingStatsResponse 排行统计响应
type rankingStatsResponse struct {
	Statistics model.RankingStatistics `json:"statistics"`
}

// GetGroupRanking 获取星球排行榜
func (r *RankingRequest) GetGroupRanking(ctx context.Context, groupID int64, opts *ListRankingOptions) ([]model.RankingItem, error) {
	params := url.Values{}
	if opts != nil {
		if opts.Type != "" {
			params.Set("type", opts.Type)
		}
		if opts.Index > 0 {
			params.Set("index", strconv.Itoa(opts.Index))
		}
	}

	var resp rankingResponse
	path := fmt.Sprintf("/v2/groups/%d/ranking_list", groupID)
	if err := r.client.Get(ctx, path, params, &resp); err != nil {
		return nil, err
	}
	return resp.RankingList, nil
}

// GetGroupRankingStats 获取排行统计
func (r *RankingRequest) GetGroupRankingStats(ctx context.Context, groupID int64) (*model.RankingStatistics, error) {
	var resp rankingStatsResponse
	path := fmt.Sprintf("/v3/groups/%d/ranking_list/statistics", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return &resp.Statistics, nil
}

// GetScoreRanking 获取积分排行榜
func (r *RankingRequest) GetScoreRanking(ctx context.Context, groupID int64, opts *ListRankingOptions) ([]model.RankingItem, error) {
	params := url.Values{}
	if opts != nil {
		if opts.Type != "" {
			params.Set("type", opts.Type)
		}
		if opts.Index > 0 {
			params.Set("index", strconv.Itoa(opts.Index))
		}
	}

	var resp rankingResponse
	path := fmt.Sprintf("/v2/dashboard/groups/%d/scoreboard/ranking_list", groupID)
	if err := r.client.Get(ctx, path, params, &resp); err != nil {
		return nil, err
	}
	return resp.RankingList, nil
}

// GetMyScoreStats 获取我的积分统计
func (r *RankingRequest) GetMyScoreStats(ctx context.Context, groupID int64) (map[string]interface{}, error) {
	var resp struct {
		Statistics map[string]interface{} `json:"statistics"`
	}
	path := fmt.Sprintf("/v2/dashboard/groups/%d/scoreboard/statistics/self", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	if resp.Statistics == nil {
		return make(map[string]interface{}), nil
	}
	return resp.Statistics, nil
}

// scoreboardSettingsResponse 积分榜设置响应
type scoreboardSettingsResponse struct {
	Settings model.ScoreboardSettings `json:"settings"`
}

// GetScoreboardSettings 获取积分榜设置
func (r *RankingRequest) GetScoreboardSettings(ctx context.Context, groupID int64) (*model.ScoreboardSettings, error) {
	var resp model.ScoreboardSettings
	path := fmt.Sprintf("/v2/dashboard/groups/%d/scoreboard/settings", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return &resp, nil
}

// GetInvitationRanking 获取邀请排行榜
func (r *RankingRequest) GetInvitationRanking(ctx context.Context, groupID int64, opts *ListRankingOptions) ([]model.RankingItem, error) {
	params := url.Values{}
	if opts != nil {
		if opts.Type != "" {
			params.Set("type", opts.Type)
		}
		if opts.Index > 0 {
			params.Set("index", strconv.Itoa(opts.Index))
		}
	}

	var resp rankingResponse
	path := fmt.Sprintf("/v2/groups/%d/invitations/ranking_list", groupID)
	if err := r.client.Get(ctx, path, params, &resp); err != nil {
		return nil, err
	}
	return resp.RankingList, nil
}

// GetContributionRanking 获取贡献排行榜
func (r *RankingRequest) GetContributionRanking(ctx context.Context, groupID int64, opts *ListRankingOptions) ([]model.RankingItem, error) {
	params := url.Values{}
	if opts != nil {
		if opts.Type != "" {
			params.Set("type", opts.Type)
		}
		if opts.Index > 0 {
			params.Set("index", strconv.Itoa(opts.Index))
		}
	}

	var resp rankingResponse
	path := fmt.Sprintf("/v2/groups/%d/contribution_ranking_list", groupID)
	if err := r.client.Get(ctx, path, params, &resp); err != nil {
		return nil, err
	}
	return resp.RankingList, nil
}

// GetGlobalRanking 获取全局星球排行榜（v3接口）
// type: group_sales_list(畅销榜), new_star_list(新星榜), paid_group_active_list(活跃榜), group_fortune_list(财富榜)
func (r *RankingRequest) GetGlobalRanking(ctx context.Context, rankType string, count int) (map[string]interface{}, error) {
	params := url.Values{}
	params.Set("type", rankType)
	params.Set("count", strconv.Itoa(count))

	var resp map[string]interface{}
	if err := r.client.Get(ctx, "/v3/groups/ranking_list", params, &resp); err != nil {
		return nil, err
	}
	return resp, nil
}

// MiscRequest 杂项请求模块
type MiscRequest struct {
	client *http.Client
}

// NewMiscRequest 创建杂项请求模块
func NewMiscRequest(client *http.Client) *MiscRequest {
	return &MiscRequest{client: client}
}

// globalConfigResponse 全局配置响应
type globalConfigResponse struct {
	Config model.GlobalConfig `json:"config"`
}

// GetGlobalConfig 获取全局配置
func (r *MiscRequest) GetGlobalConfig(ctx context.Context) (*model.GlobalConfig, error) {
	var resp globalConfigResponse
	if err := r.client.Get(ctx, "/v2/global/config", nil, &resp); err != nil {
		return nil, err
	}
	return &resp.Config, nil
}

// activitiesResponse 动态列表响应
type activitiesResponse struct {
	Activities []model.Activity `json:"activities"`
}

// ListActivitiesOptions 动态列表查询参数
type ListActivitiesOptions struct {
	Count   int    // 返回数量
	EndTime string // 结束时间
}

// GetActivities 获取用户动态
func (r *MiscRequest) GetActivities(ctx context.Context, opts *ListActivitiesOptions) ([]model.Activity, error) {
	params := url.Values{}
	if opts != nil {
		if opts.Count > 0 {
			params.Set("count", strconv.Itoa(opts.Count))
		}
		if opts.EndTime != "" {
			params.Set("end_time", opts.EndTime)
		}
	}

	var resp activitiesResponse
	if err := r.client.Get(ctx, "/v2/activities", params, &resp); err != nil {
		return nil, err
	}
	return resp.Activities, nil
}

// pkGroupResponse PK群组响应
type pkGroupResponse struct {
	PkGroup model.PkGroup `json:"pk_group"`
}

// GetPkGroup 获取 PK 群组信息
func (r *MiscRequest) GetPkGroup(ctx context.Context, groupID int64) (*model.PkGroup, error) {
	var resp pkGroupResponse
	path := fmt.Sprintf("/v2/pk/groups/%d", groupID)
	if err := r.client.Get(ctx, path, nil, &resp); err != nil {
		return nil, err
	}
	return &resp.PkGroup, nil
}
