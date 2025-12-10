package model

// GroupType 星球类型
type GroupType string

const (
	GroupTypeFree GroupType = "free"
	GroupTypePay  GroupType = "pay"
)

// Group 星球模型
type Group struct {
	GroupID       int64     `json:"group_id"`
	Number        int       `json:"number,omitempty"`
	Name          string    `json:"name"`
	Description   string    `json:"description"`
	BackgroundURL string    `json:"background_url"`
	Type          GroupType `json:"type"`
	MemberCount   *int      `json:"member_count,omitempty"`
	Owner         *User     `json:"owner,omitempty"`
	CreateTime    string    `json:"create_time"`
	RiskLevel     string    `json:"risk_level,omitempty"`
	PartnerIDs    []int64   `json:"partner_ids,omitempty"`
	AdminIDs      []int64   `json:"admin_ids,omitempty"`
}
