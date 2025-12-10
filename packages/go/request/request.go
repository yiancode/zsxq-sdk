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
	Scope string // 状态: ongoing/closed/over
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
		if opts.Scope != "" {
			params.Set("scope", opts.Scope)
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
