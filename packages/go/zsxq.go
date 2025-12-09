// Package zsxq 提供知识星球 API 的 Go SDK
//
// 使用示例:
//
//	client := zsxq.NewClientBuilder().
//		SetToken("your-token").
//		SetTimeout(10 * time.Second).
//		Build()
//
//	// 获取星球列表
//	groups, err := client.Groups().List(ctx)
//
//	// 获取话题
//	topics, err := client.Topics().List(ctx, groupId, nil)
//
//	// 获取当前用户
//	user, err := client.Users().Self(ctx)
package zsxq

import (
	"github.com/zsxq-sdk/zsxq-sdk-go/client"
)

// NewClientBuilder 创建客户端构建器
func NewClientBuilder() *client.Builder {
	return client.NewBuilder()
}

// Client 是 ZsxqClient 的别名
type Client = client.Client
