package model

// PkGroup PK 群组
type PkGroup struct {
	PkGroupID   int64  `json:"pk_group_id"`
	Name        string `json:"name"`
	Description string `json:"description,omitempty"`
	Status      string `json:"status"`
	CreateTime  string `json:"create_time"`
}

// PkBattle PK 对战记录
type PkBattle struct {
	BattleID   int64  `json:"battle_id"`
	GroupA     *Group `json:"group_a"`
	GroupB     *Group `json:"group_b"`
	ScoreA     int    `json:"score_a"`
	ScoreB     int    `json:"score_b"`
	StartTime  string `json:"start_time"`
	EndTime    string `json:"end_time,omitempty"`
	Status     string `json:"status"`
}

// UrlDetail URL 详情
type UrlDetail struct {
	URL         string `json:"url"`
	Title       string `json:"title,omitempty"`
	Description string `json:"description,omitempty"`
	ImageURL    string `json:"image_url,omitempty"`
}

// GlobalConfig 全局配置
type GlobalConfig struct {
	Version     string            `json:"version,omitempty"`
	Features    map[string]bool   `json:"features,omitempty"`
	Settings    map[string]string `json:"settings,omitempty"`
}

// Activity 动态
type Activity struct {
	ActivityID   int64  `json:"activity_id"`
	Type         string `json:"type"`
	Content      string `json:"content,omitempty"`
	Actor        *User  `json:"actor,omitempty"`
	Target       interface{} `json:"target,omitempty"`
	CreateTime   string `json:"create_time"`
}

// DailyStatistics 每日统计
type DailyStatistics struct {
	Date         string `json:"date"`
	CheckinCount int    `json:"checkin_count"`
	UserCount    int    `json:"user_count,omitempty"`
}

// MyCheckinStatistics 我的打卡统计
type MyCheckinStatistics struct {
	TotalDays       int    `json:"total_days"`
	ContinuousDays  int    `json:"continuous_days"`
	LastCheckinDate string `json:"last_checkin_date,omitempty"`
	MyRank          int    `json:"my_rank,omitempty"`
}
