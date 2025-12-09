package http

import (
	"bytes"
	"context"
	"crypto/hmac"
	"crypto/sha1"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"io"
	"math"
	"net/http"
	"net/url"
	"strings"
	"time"

	"github.com/google/uuid"
	"github.com/zsxq-sdk/zsxq-sdk-go/exception"
)

// Config HTTP 客户端配置
type Config struct {
	Token      string
	BaseURL    string
	Timeout    time.Duration
	RetryCount int
	RetryDelay time.Duration
	DeviceID   string
	AppVersion string
}

// DefaultConfig 默认配置
var DefaultConfig = Config{
	BaseURL:    "https://api.zsxq.com",
	Timeout:    10 * time.Second,
	RetryCount: 3,
	RetryDelay: time.Second,
	DeviceID:   uuid.New().String(),
	AppVersion: "2.83.0",
}

// Response 知识星球 API 响应格式
type Response struct {
	Succeeded bool            `json:"succeeded"`
	Code      int             `json:"code,omitempty"`
	Error     string          `json:"error,omitempty"`
	Info      string          `json:"info,omitempty"`
	RespData  json.RawMessage `json:"resp_data"`
}

// Client HTTP 客户端
type Client struct {
	httpClient *http.Client
	config     Config
}

// NewClient 创建 HTTP 客户端
func NewClient(config Config) *Client {
	return &Client{
		httpClient: &http.Client{
			Timeout: config.Timeout,
		},
		config: config,
	}
}

// generateSignature 生成请求签名
func (c *Client) generateSignature(timestamp, method, path, body string) string {
	secretKey := "zsxq-sdk-secret"
	signData := fmt.Sprintf("%s\n%s\n%s", timestamp, strings.ToUpper(method), path)
	if body != "" {
		signData += "\n" + body
	}

	h := hmac.New(sha1.New, []byte(secretKey))
	h.Write([]byte(signData))
	return hex.EncodeToString(h.Sum(nil))
}

// buildHeaders 构建请求头
func (c *Client) buildHeaders(method, path, body string) (http.Header, string) {
	timestamp := fmt.Sprintf("%d", time.Now().Unix())
	requestID := uuid.New().String()
	signature := c.generateSignature(timestamp, method, path, body)

	headers := http.Header{}
	headers.Set("Content-Type", "application/json; charset=utf-8")
	headers.Set("User-Agent", fmt.Sprintf("xiaomiquan/%s SDK/1.0.0", c.config.AppVersion))
	headers.Set("authorization", c.config.Token)
	headers.Set("x-timestamp", timestamp)
	headers.Set("x-signature", signature)
	headers.Set("x-request-id", requestID)
	headers.Set("x-version", c.config.AppVersion)
	headers.Set("x-aduid", c.config.DeviceID)

	return headers, requestID
}

// doRequest 执行请求
func (c *Client) doRequest(ctx context.Context, method, path string, params url.Values, body []byte) (*Response, string, error) {
	fullURL := c.config.BaseURL + path
	if len(params) > 0 {
		fullURL += "?" + params.Encode()
	}

	var bodyReader io.Reader
	var bodyStr string
	if len(body) > 0 {
		bodyReader = bytes.NewReader(body)
		bodyStr = string(body)
	}

	req, err := http.NewRequestWithContext(ctx, method, fullURL, bodyReader)
	if err != nil {
		return nil, "", err
	}

	headers, requestID := c.buildHeaders(method, path, bodyStr)
	req.Header = headers

	resp, err := c.httpClient.Do(req)
	if err != nil {
		if ctx.Err() != nil {
			return nil, requestID, exception.NewTimeoutError("请求超时", requestID, err)
		}
		return nil, requestID, exception.NewNetworkError("网络错误", requestID, err)
	}
	defer resp.Body.Close()

	respBody, err := io.ReadAll(resp.Body)
	if err != nil {
		return nil, requestID, exception.NewNetworkError("读取响应失败", requestID, err)
	}

	var response Response
	if err := json.Unmarshal(respBody, &response); err != nil {
		return nil, requestID, exception.NewNetworkError("解析响应失败", requestID, err)
	}

	return &response, requestID, nil
}

// executeWithRetry 执行请求（带重试）
func (c *Client) executeWithRetry(ctx context.Context, method, path string, params url.Values, body []byte) (*Response, string, error) {
	var lastErr error
	var requestID string

	for i := 0; i <= c.config.RetryCount; i++ {
		response, reqID, err := c.doRequest(ctx, method, path, params, body)
		requestID = reqID

		if err == nil {
			return response, requestID, nil
		}

		lastErr = err

		// 只对网络错误和超时错误进行重试
		_, isNetwork := err.(*exception.NetworkError)
		_, isTimeout := err.(*exception.TimeoutError)
		if !isNetwork && !isTimeout {
			return nil, requestID, err
		}

		if i < c.config.RetryCount {
			delay := time.Duration(math.Pow(2, float64(i))) * c.config.RetryDelay
			select {
			case <-ctx.Done():
				return nil, requestID, ctx.Err()
			case <-time.After(delay):
			}
		}
	}

	return nil, requestID, lastErr
}

// handleResponse 处理响应
func (c *Client) handleResponse(response *Response, requestID string, result interface{}) error {
	if !response.Succeeded {
		code := response.Code
		message := response.Error
		if message == "" {
			message = response.Info
		}
		if message == "" {
			message = "未知错误"
		}
		return exception.CreateError(code, message, requestID)
	}

	if result != nil && len(response.RespData) > 0 {
		if err := json.Unmarshal(response.RespData, result); err != nil {
			return exception.NewNetworkError("解析数据失败", requestID, err)
		}
	}

	return nil
}

// Get GET 请求
func (c *Client) Get(ctx context.Context, path string, params url.Values, result interface{}) error {
	response, requestID, err := c.executeWithRetry(ctx, http.MethodGet, path, params, nil)
	if err != nil {
		return err
	}
	return c.handleResponse(response, requestID, result)
}

// Post POST 请求
func (c *Client) Post(ctx context.Context, path string, data interface{}, result interface{}) error {
	var body []byte
	var err error
	if data != nil {
		body, err = json.Marshal(data)
		if err != nil {
			return exception.NewNetworkError("序列化请求数据失败", "", err)
		}
	}

	response, requestID, err := c.executeWithRetry(ctx, http.MethodPost, path, nil, body)
	if err != nil {
		return err
	}
	return c.handleResponse(response, requestID, result)
}

// Put PUT 请求
func (c *Client) Put(ctx context.Context, path string, data interface{}, result interface{}) error {
	var body []byte
	var err error
	if data != nil {
		body, err = json.Marshal(data)
		if err != nil {
			return exception.NewNetworkError("序列化请求数据失败", "", err)
		}
	}

	response, requestID, err := c.executeWithRetry(ctx, http.MethodPut, path, nil, body)
	if err != nil {
		return err
	}
	return c.handleResponse(response, requestID, result)
}

// Delete DELETE 请求
func (c *Client) Delete(ctx context.Context, path string, result interface{}) error {
	response, requestID, err := c.executeWithRetry(ctx, http.MethodDelete, path, nil, nil)
	if err != nil {
		return err
	}
	return c.handleResponse(response, requestID, result)
}
