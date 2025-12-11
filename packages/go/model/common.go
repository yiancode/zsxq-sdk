package model

// Menu 星球菜单配置模型
type Menu struct {
	MenuID int64  `json:"menu_id"`
	Name   string `json:"name"`
	Type   string `json:"type"`
}

// Column 星球专栏模型
type Column struct {
	ColumnID    int64  `json:"column_id"`
	Name        string `json:"name"`
	TopicsCount int    `json:"topics_count"`
	Description string `json:"description"`
	CoverURL    string `json:"cover_url"`
}

// RoleMembers 星球角色成员模型（包含星主、合伙人、管理员）
type RoleMembers struct {
	Owner    *User   `json:"owner"`
	Partners []*User `json:"partners"`
	Admins   []*User `json:"admins"`
}

// ActivitySummary 成员活跃摘要模型
type ActivitySummary struct {
	TopicsCount     int `json:"topics_count"`
	CommentsCount   int `json:"comments_count"`
	LikesReceived   int `json:"likes_received"`
	LikesGiven      int `json:"likes_given"`
	RewardsReceived int `json:"rewards_received"`
	RewardsGiven    int `json:"rewards_given"`
}

// Reward 打赏模型
type Reward struct {
	User       *User  `json:"user"`
	Amount     int    `json:"amount"`
	CreateTime string `json:"create_time"`
}

// RenewalInfo 星球续费信息
type RenewalInfo struct {
	RenewalID     int64  `json:"renewal_id,omitempty"`
	Price         int    `json:"price"`
	OriginalPrice int    `json:"original_price"`
	StartTime     string `json:"start_time,omitempty"`
	EndTime       string `json:"end_time,omitempty"`
	Status        string `json:"status,omitempty"`
}

// DistributionInfo 星球分销信息
type DistributionInfo struct {
	Enabled        bool   `json:"enabled"`
	CommissionRate int    `json:"commission_rate"`
	JoinURL        string `json:"join_url,omitempty"`
}

// CustomTag 自定义标签
type CustomTag struct {
	LabelID int64  `json:"label_id"`
	Name    string `json:"name"`
	Color   string `json:"color,omitempty"`
}

// ScheduledJob 定时任务
type ScheduledJob struct {
	JobID      int64  `json:"job_id"`
	Name       string `json:"name"`
	Type       string `json:"type"`
	Status     string `json:"status"`
	CreateTime string `json:"create_time"`
}

// GroupWarning 星球风险预警
type GroupWarning struct {
	WarningType string `json:"warning_type,omitempty"`
	Level       string `json:"level,omitempty"`
	Message     string `json:"message,omitempty"`
}

// Inviter 邀请人信息
type Inviter struct {
	User *User `json:"user"`
}

// Coupon 优惠券
type Coupon struct {
	CouponID   int64  `json:"coupon_id"`
	Name       string `json:"name"`
	Amount     int    `json:"amount"`
	ValidUntil string `json:"valid_until,omitempty"`
	Status     string `json:"status"`
}

// Remark 备注
type Remark struct {
	RemarkID   int64  `json:"remark_id"`
	TargetID   int64  `json:"target_id"`
	TargetType string `json:"target_type"`
	Content    string `json:"content"`
}

// PreferenceCategory 推荐偏好分类
type PreferenceCategory struct {
	CategoryID int64  `json:"category_id"`
	Name       string `json:"name"`
	Selected   bool   `json:"selected"`
}

// UnansweredQuestionsSummary 未回答问题摘要
type UnansweredQuestionsSummary struct {
	Count      int    `json:"count"`
	LatestTime string `json:"latest_time,omitempty"`
}

// FollowerStatistics 关注者统计
type FollowerStatistics struct {
	FollowersCount   int `json:"followers_count"`
	FollowingCount   int `json:"following_count"`
	NewFollowers     int `json:"new_followers,omitempty"`
	UnfollowersCount int `json:"unfollowers_count,omitempty"`
}

// Contribution 贡献记录
type Contribution struct {
	Date   string `json:"date"`
	Type   string `json:"type"`
	Points int    `json:"points"`
}

// ContributionStatistics 贡献统计
type ContributionStatistics struct {
	TotalPoints     int `json:"total_points"`
	TopicsCount     int `json:"topics_count"`
	CommentsCount   int `json:"comments_count"`
	LikesGivenCount int `json:"likes_given_count"`
}

// AchievementSummary 成就摘要
type AchievementSummary struct {
	AchievementID int64  `json:"achievement_id"`
	Name          string `json:"name"`
	Description   string `json:"description,omitempty"`
	Unlocked      bool   `json:"unlocked"`
}

// WeeklyRanking 星球周榜排名
type WeeklyRanking struct {
	Rank       int `json:"rank"`
	Points     int `json:"points"`
	TotalUsers int `json:"total_users"`
}
