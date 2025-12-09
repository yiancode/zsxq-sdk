package model

// User 用户模型
type User struct {
	UserID       int64  `json:"user_id"`
	UID          string `json:"uid,omitempty"`
	Name         string `json:"name"`
	AvatarURL    string `json:"avatar_url"`
	Location     string `json:"location,omitempty"`
	Introduction string `json:"introduction,omitempty"`
	UniqueID     string `json:"unique_id,omitempty"`
	UserSID      string `json:"user_sid,omitempty"`
	Grade        string `json:"grade,omitempty"`
	Verified     bool   `json:"verified,omitempty"`
}
