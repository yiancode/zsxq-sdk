package client

import (
	"time"

	"github.com/google/uuid"
	"github.com/zsxq-sdk/zsxq-sdk-go/http"
	"github.com/zsxq-sdk/zsxq-sdk-go/request"
)

// Config SDK 配置
type Config struct {
	Token      string        // 用户认证 Token（必需）
	BaseURL    string        // API 基础 URL
	Timeout    time.Duration // 请求超时
	RetryCount int           // 重试次数
	RetryDelay time.Duration // 重试间隔
	DeviceID   string        // 设备 ID
	AppVersion string        // App 版本号
}

// DefaultConfig 默认配置
var DefaultConfig = Config{
	BaseURL:    "https://api.zsxq.com",
	Timeout:    10 * time.Second,
	RetryCount: 3,
	RetryDelay: time.Second,
	AppVersion: "2.83.0",
}

// Client 知识星球 SDK 主客户端
type Client struct {
	groups    *request.GroupsRequest
	topics    *request.TopicsRequest
	users     *request.UsersRequest
	checkins  *request.CheckinsRequest
	dashboard *request.DashboardRequest
	ranking   *request.RankingRequest
	misc      *request.MiscRequest
}

// NewClient 创建客户端
func NewClient(config Config) *Client {
	httpConfig := http.Config{
		Token:      config.Token,
		BaseURL:    config.BaseURL,
		Timeout:    config.Timeout,
		RetryCount: config.RetryCount,
		RetryDelay: config.RetryDelay,
		DeviceID:   config.DeviceID,
		AppVersion: config.AppVersion,
	}

	httpClient := http.NewClient(httpConfig)

	return &Client{
		groups:    request.NewGroupsRequest(httpClient),
		topics:    request.NewTopicsRequest(httpClient),
		users:     request.NewUsersRequest(httpClient),
		checkins:  request.NewCheckinsRequest(httpClient),
		dashboard: request.NewDashboardRequest(httpClient),
		ranking:   request.NewRankingRequest(httpClient),
		misc:      request.NewMiscRequest(httpClient),
	}
}

// Groups 星球管理
func (c *Client) Groups() *request.GroupsRequest {
	return c.groups
}

// Topics 话题管理
func (c *Client) Topics() *request.TopicsRequest {
	return c.topics
}

// Users 用户管理
func (c *Client) Users() *request.UsersRequest {
	return c.users
}

// Checkins 打卡管理
func (c *Client) Checkins() *request.CheckinsRequest {
	return c.checkins
}

// Dashboard 数据面板
func (c *Client) Dashboard() *request.DashboardRequest {
	return c.dashboard
}

// Ranking 排行榜
func (c *Client) Ranking() *request.RankingRequest {
	return c.ranking
}

// Misc 杂项功能
func (c *Client) Misc() *request.MiscRequest {
	return c.misc
}

// Builder 客户端构建器
type Builder struct {
	config Config
}

// NewBuilder 创建构建器
func NewBuilder() *Builder {
	return &Builder{
		config: Config{
			BaseURL:    DefaultConfig.BaseURL,
			Timeout:    DefaultConfig.Timeout,
			RetryCount: DefaultConfig.RetryCount,
			RetryDelay: DefaultConfig.RetryDelay,
			DeviceID:   uuid.New().String(),
			AppVersion: DefaultConfig.AppVersion,
		},
	}
}

// SetToken 设置认证 Token（必需）
func (b *Builder) SetToken(token string) *Builder {
	b.config.Token = token
	return b
}

// SetBaseURL 设置 API 基础 URL
func (b *Builder) SetBaseURL(baseURL string) *Builder {
	b.config.BaseURL = baseURL
	return b
}

// SetTimeout 设置请求超时
func (b *Builder) SetTimeout(timeout time.Duration) *Builder {
	b.config.Timeout = timeout
	return b
}

// SetRetryCount 设置重试次数
func (b *Builder) SetRetryCount(count int) *Builder {
	b.config.RetryCount = count
	return b
}

// SetRetryDelay 设置重试间隔
func (b *Builder) SetRetryDelay(delay time.Duration) *Builder {
	b.config.RetryDelay = delay
	return b
}

// SetDeviceID 设置设备 ID
func (b *Builder) SetDeviceID(deviceID string) *Builder {
	b.config.DeviceID = deviceID
	return b
}

// SetAppVersion 设置 App 版本号
func (b *Builder) SetAppVersion(version string) *Builder {
	b.config.AppVersion = version
	return b
}

// Build 构建客户端
func (b *Builder) Build() (*Client, error) {
	if b.config.Token == "" {
		return nil, &BuildError{Message: "Token is required. Use SetToken() to set it."}
	}
	return NewClient(b.config), nil
}

// MustBuild 构建客户端，失败时 panic
func (b *Builder) MustBuild() *Client {
	client, err := b.Build()
	if err != nil {
		panic(err)
	}
	return client
}

// BuildError 构建错误
type BuildError struct {
	Message string
}

func (e *BuildError) Error() string {
	return e.Message
}
