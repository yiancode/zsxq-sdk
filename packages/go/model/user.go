package model

import (
	"encoding/json"
	"fmt"
)

// User 用户模型
//
// 注意：API 返回的数据中，用户ID可能以 uid 或 user_id 形式出现
// 实现自定义 UnmarshalJSON 来处理字段兼容性
type User struct {
	UserID       string `json:"user_id,omitempty"`
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

// UnmarshalJSON 自定义 JSON 反序列化，处理 uid 和 user_id 字段的兼容性
// 同时处理 user_id 可能是数字或字符串的情况
func (u *User) UnmarshalJSON(data []byte) error {
	// 使用 map 先解析，以便灵活处理不同类型
	var raw map[string]interface{}
	if err := json.Unmarshal(data, &raw); err != nil {
		return err
	}

	// 处理 user_id 字段（可能是数字或字符串）
	if userID, ok := raw["user_id"]; ok {
		switch v := userID.(type) {
		case string:
			u.UserID = v
			u.UID = v
		case float64:
			u.UserID = fmt.Sprintf("%.0f", v)
			u.UID = u.UserID
		}
		delete(raw, "user_id")
	}

	// 处理 uid 字段
	if uid, ok := raw["uid"]; ok {
		if u.UserID == "" {
			if uidStr, ok := uid.(string); ok {
				u.UserID = uidStr
				u.UID = uidStr
			}
		} else if u.UID == "" {
			if uidStr, ok := uid.(string); ok {
				u.UID = uidStr
			}
		}
		delete(raw, "uid")
	}

	// 使用临时结构处理其他字段
	type Alias User
	aux := &struct {
		*Alias
		UserID interface{} `json:"user_id,omitempty"`
		UID    interface{} `json:"uid,omitempty"`
	}{
		Alias: (*Alias)(u),
	}

	// 重新marshal并unmarshal其他字段
	remaining, _ := json.Marshal(raw)
	if err := json.Unmarshal(remaining, aux); err != nil {
		return err
	}

	return nil
}
