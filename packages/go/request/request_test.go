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
