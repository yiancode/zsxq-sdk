package http

import (
	"context"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"net/url"
	"testing"
	"time"
)

func TestNewClient(t *testing.T) {
	config := Config{
		Token:      "test-token",
		BaseURL:    "https://api.test.com",
		Timeout:    5 * time.Second,
		RetryCount: 3,
		RetryDelay: time.Second,
		DeviceID:   "device-123",
		AppVersion: "1.0.0",
	}

	client := NewClient(config)

	if client == nil {
		t.Fatal("Client should not be nil")
	}

	if client.config.Token != config.Token {
		t.Errorf("Expected token %s, got %s", config.Token, client.config.Token)
	}
}

func TestGenerateSignature(t *testing.T) {
	config := DefaultConfig
	config.Token = "test-token"
	client := NewClient(config)

	timestamp := "1234567890"
	method := "GET"
	path := "/v2/groups"

	signature := client.generateSignature(timestamp, method, path, "")

	if len(signature) != 40 { // HMAC-SHA1 hex = 40 chars
		t.Errorf("Expected signature length 40, got %d", len(signature))
	}

	// 验证是否为有效的 hex 字符串
	for _, c := range signature {
		if !((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f')) {
			t.Errorf("Signature contains invalid hex character: %c", c)
		}
	}
}

func TestGenerateSignatureWithBody(t *testing.T) {
	config := DefaultConfig
	config.Token = "test-token"
	client := NewClient(config)

	timestamp := "1234567890"
	method := "POST"
	path := "/v2/topics"
	body := `{"text":"test"}`

	signature := client.generateSignature(timestamp, method, path, body)

	if len(signature) != 40 {
		t.Errorf("Expected signature length 40, got %d", len(signature))
	}
}

func TestBuildHeaders(t *testing.T) {
	config := DefaultConfig
	config.Token = "test-token"
	config.DeviceID = "device-123"
	config.AppVersion = "5.60.0"
	client := NewClient(config)

	headers, requestID := client.buildHeaders("GET", "/v2/groups", "")

	if headers.Get("authorization") != "test-token" {
		t.Errorf("Expected authorization header 'test-token', got '%s'", headers.Get("authorization"))
	}

	if headers.Get("x-aduid") != "device-123" {
		t.Errorf("Expected x-aduid header 'device-123', got '%s'", headers.Get("x-aduid"))
	}

	if headers.Get("x-timestamp") == "" {
		t.Error("x-timestamp header should not be empty")
	}

	if headers.Get("x-signature") == "" {
		t.Error("x-signature header should not be empty")
	}

	if requestID == "" {
		t.Error("requestID should not be empty")
	}

	if headers.Get("x-request-id") != requestID {
		t.Error("x-request-id header should match returned requestID")
	}
}

func TestGetRequestSuccess(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if r.Method != "GET" {
			t.Errorf("Expected GET request, got %s", r.Method)
		}

		if r.URL.Path != "/v2/groups" {
			t.Errorf("Expected path /v2/groups, got %s", r.URL.Path)
		}

		// 验证请求头
		if r.Header.Get("authorization") == "" {
			t.Error("authorization header is missing")
		}

		response := Response{
			Succeeded: true,
			RespData:  json.RawMessage(`{"test":"data"}`),
		}

		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(response)
	}))
	defer server.Close()

	config := DefaultConfig
	config.Token = "test-token"
	config.BaseURL = server.URL
	client := NewClient(config)

	var result map[string]interface{}
	err := client.Get(context.Background(), "/v2/groups", nil, &result)

	if err != nil {
		t.Errorf("Get request should succeed: %v", err)
	}

	if result["test"] != "data" {
		t.Errorf("Expected result data, got %v", result)
	}
}

func TestGetRequestWithParams(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		params := r.URL.Query()
		if params.Get("count") != "10" {
			t.Errorf("Expected count=10, got %s", params.Get("count"))
		}

		if params.Get("scope") != "all" {
			t.Errorf("Expected scope=all, got %s", params.Get("scope"))
		}

		response := Response{
			Succeeded: true,
			RespData:  json.RawMessage(`{}`),
		}
		json.NewEncoder(w).Encode(response)
	}))
	defer server.Close()

	config := DefaultConfig
	config.Token = "test-token"
	config.BaseURL = server.URL
	client := NewClient(config)

	params := url.Values{}
	params.Set("count", "10")
	params.Set("scope", "all")

	var result map[string]interface{}
	err := client.Get(context.Background(), "/v2/topics", params, &result)

	if err != nil {
		t.Errorf("Get request should succeed: %v", err)
	}
}

func TestPostRequestSuccess(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if r.Method != "POST" {
			t.Errorf("Expected POST request, got %s", r.Method)
		}

		response := Response{
			Succeeded: true,
			RespData:  json.RawMessage(`{"id":123}`),
		}
		json.NewEncoder(w).Encode(response)
	}))
	defer server.Close()

	config := DefaultConfig
	config.Token = "test-token"
	config.BaseURL = server.URL
	client := NewClient(config)

	postData := map[string]string{"text": "test"}
	var result map[string]interface{}
	err := client.Post(context.Background(), "/v2/topics", postData, &result)

	if err != nil {
		t.Errorf("Post request should succeed: %v", err)
	}

	if result["id"].(float64) != 123 {
		t.Errorf("Expected id=123, got %v", result["id"])
	}
}

func TestBusinessErrorResponse(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		response := Response{
			Succeeded: false,
			Code:      10001,
			Error:     "Token 无效",
		}
		json.NewEncoder(w).Encode(response)
	}))
	defer server.Close()

	config := DefaultConfig
	config.Token = "test-token"
	config.BaseURL = server.URL
	client := NewClient(config)

	var result map[string]interface{}
	err := client.Get(context.Background(), "/v2/groups", nil, &result)

	if err == nil {
		t.Error("Expected error for failed response")
	}
}

func TestNetworkError(t *testing.T) {
	config := DefaultConfig
	config.Token = "test-token"
	config.BaseURL = "http://invalid-host-that-does-not-exist.local"
	config.Timeout = 100 * time.Millisecond
	client := NewClient(config)

	var result map[string]interface{}
	err := client.Get(context.Background(), "/v2/groups", nil, &result)

	if err == nil {
		t.Error("Expected network error")
	}
}

func TestRetryOnNetworkError(t *testing.T) {
	requestCount := 0
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		requestCount++
		if requestCount < 3 {
			// 前两次返回错误
			w.WriteHeader(http.StatusInternalServerError)
			return
		}

		// 第三次成功
		response := Response{
			Succeeded: true,
			RespData:  json.RawMessage(`{"success":true}`),
		}
		json.NewEncoder(w).Encode(response)
	}))
	defer server.Close()

	config := DefaultConfig
	config.Token = "test-token"
	config.BaseURL = server.URL
	config.RetryCount = 2
	config.RetryDelay = 10 * time.Millisecond
	client := NewClient(config)

	var result map[string]interface{}
	err := client.Get(context.Background(), "/v2/groups", nil, &result)

	if err != nil {
		t.Errorf("Request should succeed after retries: %v", err)
	}

	if requestCount != 3 {
		t.Errorf("Expected 3 requests (1 original + 2 retries), got %d", requestCount)
	}
}

func TestMaxRetryReached(t *testing.T) {
	requestCount := 0
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		requestCount++
		w.WriteHeader(http.StatusInternalServerError)
	}))
	defer server.Close()

	config := DefaultConfig
	config.Token = "test-token"
	config.BaseURL = server.URL
	config.RetryCount = 2
	config.RetryDelay = 10 * time.Millisecond
	client := NewClient(config)

	var result map[string]interface{}
	err := client.Get(context.Background(), "/v2/groups", nil, &result)

	if err == nil {
		t.Error("Expected error after max retries")
	}

	if requestCount != 3 {
		t.Errorf("Expected 3 requests, got %d", requestCount)
	}
}

func TestNoRetryOnBusinessError(t *testing.T) {
	requestCount := 0
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		requestCount++
		response := Response{
			Succeeded: false,
			Code:      10001,
			Error:     "Token 无效",
		}
		json.NewEncoder(w).Encode(response)
	}))
	defer server.Close()

	config := DefaultConfig
	config.Token = "test-token"
	config.BaseURL = server.URL
	config.RetryCount = 2
	client := NewClient(config)

	var result map[string]interface{}
	err := client.Get(context.Background(), "/v2/groups", nil, &result)

	if err == nil {
		t.Error("Expected error")
	}

	if requestCount != 1 {
		t.Errorf("Should not retry on business error, expected 1 request, got %d", requestCount)
	}
}

func TestContextTimeout(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		time.Sleep(200 * time.Millisecond)
		response := Response{
			Succeeded: true,
			RespData:  json.RawMessage(`{}`),
		}
		json.NewEncoder(w).Encode(response)
	}))
	defer server.Close()

	config := DefaultConfig
	config.Token = "test-token"
	config.BaseURL = server.URL
	client := NewClient(config)

	ctx, cancel := context.WithTimeout(context.Background(), 50*time.Millisecond)
	defer cancel()

	var result map[string]interface{}
	err := client.Get(ctx, "/v2/groups", nil, &result)

	if err == nil {
		t.Error("Expected timeout error")
	}
}
