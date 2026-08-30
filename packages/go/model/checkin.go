package model

// CheckinStatus 打卡状态
type CheckinStatus string

const (
	CheckinStatusOngoing CheckinStatus = "ongoing"
	CheckinStatusClosed  CheckinStatus = "closed"
	CheckinStatusOver    CheckinStatus = "over"
)

// Checkin 打卡项目模型
type Checkin struct {
	CheckinID            int64         `json:"checkin_id"`
	Group                *Group        `json:"group,omitempty"`
	Owner                *User         `json:"owner,omitempty"`
	Name                 string        `json:"name"`
	Description          string        `json:"description"`
	CoverURL             string        `json:"cover_url,omitempty"`
	Status               CheckinStatus `json:"status"`
	CreateTime           string        `json:"create_time"`
	BeginTime            string        `json:"begin_time"`
	EndTime              string        `json:"end_time"`
	Title                string        `json:"title,omitempty"`
	Text                 string        `json:"text,omitempty"`
	Type                 string        `json:"type,omitempty"`
	CheckinDays          int           `json:"checkin_days,omitempty"`
	ShowTopicsOnTimeline bool          `json:"show_topics_on_timeline,omitempty"`
	// MinWordsCount 最低字数，0 表示无限制
	MinWordsCount int `json:"min_words_count,omitempty"`
}

// CheckinStatistics 打卡统计模型
type CheckinStatistics struct {
	JoinedCount    int `json:"joined_count"`
	CompletedCount int `json:"completed_count"`
	CheckinedCount int `json:"checkined_count"`
}

// RankingItem 排行榜项目模型
type RankingItem struct {
	User            *User `json:"user"`
	Rank            int   `json:"rank"`
	Count           int   `json:"count"`
	ContinuousCount int   `json:"continuous_count,omitempty"`
}
