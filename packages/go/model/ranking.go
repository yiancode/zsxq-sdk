package model

// RankingStatistics 排行统计
type RankingStatistics struct {
	TotalUsers     int `json:"total_users"`
	MyRank         int `json:"my_rank,omitempty"`
	MyPoints       int `json:"my_points,omitempty"`
	TopUserPoints  int `json:"top_user_points,omitempty"`
}

// ScoreboardSettings 积分榜设置
type ScoreboardSettings struct {
	Enabled     bool              `json:"enabled"`
	Rules       string            `json:"rules,omitempty"`
	PointsRules map[string]int    `json:"points_rules,omitempty"`
}

// InvoiceStats 发票统���
type InvoiceStats struct {
	TotalAmount      int `json:"total_amount"`
	TotalCount       int `json:"total_count"`
	PendingAmount    int `json:"pending_amount,omitempty"`
	PendingCount     int `json:"pending_count,omitempty"`
}
