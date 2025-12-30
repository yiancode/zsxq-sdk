package request_test

import (
	"context"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"
	"time"

	httpclient "github.com/zsxq-sdk/zsxq-sdk-go/http"
	"github.com/zsxq-sdk/zsxq-sdk-go/request"
)

func TestGroupsRequest_List(t *testing.T) {
	// 创建测试服务器
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if r.URL.Path != "/v2/groups" {
			t.Errorf("Expected path /v2/groups, got %s", r.URL.Path)
		}

		response := map[string]interface{}{
			"succeeded": true,
			"resp_data": map[string]interface{}{
				"groups": []map[string]interface{}{
					{"group_id": 123, "name": "测试星球1", "type": "free"},
					{"group_id": 456, "name": "测试星球2", "type": "pay"},
				},
			},
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}))
	defer server.Close()

	// 创建客户端
	client := httpclient.NewClient(httpclient.Config{
		BaseURL: server.URL,
		Token:   "test-token",
		Timeout: 5 * time.Second,
	})

	groupsReq := request.NewGroupsRequest(client)

	// 测试
	ctx := context.Background()
	groups, err := groupsReq.List(ctx)

	if err != nil {
		t.Fatalf("Expected no error, got %v", err)
	}

	if len(groups) != 2 {
		t.Fatalf("Expected 2 groups, got %d", len(groups))
	}

	if groups[0].Name != "测试星球1" {
		t.Errorf("Expected group name '测试星球1', got '%s'", groups[0].Name)
	}
}

func TestGroupsRequest_Get(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if r.URL.Path != "/v2/groups/123" {
			t.Errorf("Expected path /v2/groups/123, got %s", r.URL.Path)
		}

		response := map[string]interface{}{
			"succeeded": true,
			"resp_data": map[string]interface{}{
				"group": map[string]interface{}{
					"group_id": 123,
					"name":     "测试星球",
					"type":     "free",
				},
			},
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}))
	defer server.Close()

	client := httpclient.NewClient(httpclient.Config{
		BaseURL: server.URL,
		Token:   "test-token",
		Timeout: 5 * time.Second,
	})

	groupsReq := request.NewGroupsRequest(client)

	ctx := context.Background()
	group, err := groupsReq.Get(ctx, 123)

	if err != nil {
		t.Fatalf("Expected no error, got %v", err)
	}

	if group.Name != "测试星球" {
		t.Errorf("Expected group name '测试星球', got '%s'", group.Name)
	}

	if group.GroupID != 123 {
		t.Errorf("Expected group ID 123, got %d", group.GroupID)
	}
}

func TestGroupsRequest_GetStatistics(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		response := map[string]interface{}{
			"succeeded": true,
			"resp_data": map[string]interface{}{
				"members_count": 100,
				"topics_count":  50,
			},
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}))
	defer server.Close()

	client := httpclient.NewClient(httpclient.Config{
		BaseURL: server.URL,
		Token:   "test-token",
		Timeout: 5 * time.Second,
	})

	groupsReq := request.NewGroupsRequest(client)

	ctx := context.Background()
	stats, err := groupsReq.GetStatistics(ctx, 123)

	if err != nil {
		t.Fatalf("Expected no error, got %v", err)
	}

	if stats["members_count"] != float64(100) {
		t.Errorf("Expected members_count 100, got %v", stats["members_count"])
	}

	if stats["topics_count"] != float64(50) {
		t.Errorf("Expected topics_count 50, got %v", stats["topics_count"])
	}
}

func TestGroupsRequest_GetMember(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if r.URL.Path != "/v2/groups/123/members/999" {
			t.Errorf("Expected path /v2/groups/123/members/999, got %s", r.URL.Path)
		}

		response := map[string]interface{}{
			"succeeded": true,
			"resp_data": map[string]interface{}{
				"user": map[string]interface{}{
					"user_id": 999,
					"name":    "测试用户",
				},
			},
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}))
	defer server.Close()

	client := httpclient.NewClient(httpclient.Config{
		BaseURL: server.URL,
		Token:   "test-token",
		Timeout: 5 * time.Second,
	})

	groupsReq := request.NewGroupsRequest(client)

	ctx := context.Background()
	user, err := groupsReq.GetMember(ctx, 123, 999)

	if err != nil {
		t.Fatalf("Expected no error, got %v", err)
	}

	if user.Name != "测试用户" {
		t.Errorf("Expected user name '测试用户', got '%s'", user.Name)
	}

	if user.UserID != "999" {
		t.Errorf("Expected user ID '999', got '%s'", user.UserID)
	}
}

func TestGroupsRequest_GetHashtags(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		response := map[string]interface{}{
			"succeeded": true,
			"resp_data": map[string]interface{}{
				"hashtags": []map[string]interface{}{
					{"hashtag_id": 1, "name": "标签1"},
					{"hashtag_id": 2, "name": "标签2"},
				},
			},
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}))
	defer server.Close()

	client := httpclient.NewClient(httpclient.Config{
		BaseURL: server.URL,
		Token:   "test-token",
		Timeout: 5 * time.Second,
	})

	groupsReq := request.NewGroupsRequest(client)

	ctx := context.Background()
	hashtags, err := groupsReq.GetHashtags(ctx, 123)

	if err != nil {
		t.Fatalf("Expected no error, got %v", err)
	}

	if len(hashtags) != 2 {
		t.Fatalf("Expected 2 hashtags, got %d", len(hashtags))
	}

	if hashtags[0].Name != "标签1" {
		t.Errorf("Expected hashtag name '标签1', got '%s'", hashtags[0].Name)
	}
}

func TestGroupsRequest_GetUnreadCount(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		response := map[string]interface{}{
			"succeeded": true,
			"resp_data": map[string]interface{}{
				"123": 5,
				"456": 10,
			},
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}))
	defer server.Close()

	client := httpclient.NewClient(httpclient.Config{
		BaseURL: server.URL,
		Token:   "test-token",
		Timeout: 5 * time.Second,
	})

	groupsReq := request.NewGroupsRequest(client)

	ctx := context.Background()
	counts, err := groupsReq.GetUnreadCount(ctx)

	if err != nil {
		t.Fatalf("Expected no error, got %v", err)
	}

	if counts["123"] != 5 {
		t.Errorf("Expected count 5 for group 123, got %d", counts["123"])
	}

	if counts["456"] != 10 {
		t.Errorf("Expected count 10 for group 456, got %d", counts["456"])
	}
}

func TestMiscRequest_GetPkBattles(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if r.URL.Path != "/v2/pk_groups/123/records" {
			t.Errorf("Expected path /v2/pk_groups/123/records, got %s", r.URL.Path)
		}

		// 验证查询参数
		count := r.URL.Query().Get("count")
		if count != "10" {
			t.Errorf("Expected count=10, got count=%s", count)
		}

		response := map[string]interface{}{
			"succeeded": true,
			"resp_data": map[string]interface{}{
				"records": []map[string]interface{}{
					{
						"battle_id":  1,
						"group_a":    map[string]interface{}{"pk_group_id": 123, "name": "群组A", "status": "active", "create_time": "2024-01-01T00:00:00Z"},
						"group_b":    map[string]interface{}{"pk_group_id": 456, "name": "群组B", "status": "active", "create_time": "2024-01-01T00:00:00Z"},
						"score_a":    100,
						"score_b":    80,
						"start_time": "2024-01-01T00:00:00Z",
						"status":     "finished",
					},
					{
						"battle_id":  2,
						"group_a":    map[string]interface{}{"pk_group_id": 123, "name": "群组A", "status": "active", "create_time": "2024-01-01T00:00:00Z"},
						"group_b":    map[string]interface{}{"pk_group_id": 789, "name": "群组C", "status": "active", "create_time": "2024-01-01T00:00:00Z"},
						"score_a":    50,
						"score_b":    60,
						"start_time": "2024-01-02T00:00:00Z",
						"status":     "ongoing",
					},
				},
			},
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}))
	defer server.Close()

	client := httpclient.NewClient(httpclient.Config{
		BaseURL: server.URL,
		Token:   "test-token",
		Timeout: 5 * time.Second,
	})

	miscReq := request.NewMiscRequest(client)

	ctx := context.Background()
	opts := &request.ListPkBattlesOptions{Count: 10}
	battles, err := miscReq.GetPkBattles(ctx, 123, opts)

	if err != nil {
		t.Fatalf("Expected no error, got %v", err)
	}

	if len(battles) != 2 {
		t.Fatalf("Expected 2 battles, got %d", len(battles))
	}

	if battles[0].BattleID != 1 {
		t.Errorf("Expected battle ID 1, got %d", battles[0].BattleID)
	}

	if battles[0].Status != "finished" {
		t.Errorf("Expected battle status 'finished', got '%s'", battles[0].Status)
	}
}

func TestMiscRequest_ParseUrl(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if r.URL.Path != "/v2/url_details" {
			t.Errorf("Expected path /v2/url_details, got %s", r.URL.Path)
		}

		// 验证查询参数
		url := r.URL.Query().Get("url")
		if url != "https://example.com" {
			t.Errorf("Expected url=https://example.com, got url=%s", url)
		}

		response := map[string]interface{}{
			"succeeded": true,
			"resp_data": map[string]interface{}{
				"url_detail": map[string]interface{}{
					"url":         "https://example.com",
					"title":       "Example Domain",
					"description": "Example description",
					"site_name":   "Example",
				},
			},
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}))
	defer server.Close()

	client := httpclient.NewClient(httpclient.Config{
		BaseURL: server.URL,
		Token:   "test-token",
		Timeout: 5 * time.Second,
	})

	miscReq := request.NewMiscRequest(client)

	ctx := context.Background()
	detail, err := miscReq.ParseUrl(ctx, "https://example.com")

	if err != nil {
		t.Fatalf("Expected no error, got %v", err)
	}

	if detail.URL != "https://example.com" {
		t.Errorf("Expected URL 'https://example.com', got '%s'", detail.URL)
	}

	if detail.Title != "Example Domain" {
		t.Errorf("Expected title 'Example Domain', got '%s'", detail.Title)
	}
}
