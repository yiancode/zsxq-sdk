# zsxq-sdk-go

> 知识星球 Go SDK - 轻量级、类型安全的 API 客户端

[![Go Version](https://img.shields.io/badge/go-%3E%3D1.21-00ADD8)](https://go.dev/)
[![License](https://img.shields.io/badge/license-MIT-green)](LICENSE)

## 特性

- **类型安全** - 完整的 Go 类型定义
- **Builder 模式** - 灵活的客户端配置
- **模块化设计** - 按需使用各功能模块
- **自动重试** - 内置网络错误重试机制
- **Context 支持** - 支持 context 取消和超时

## 安装

```bash
go get github.com/zsxq-sdk/zsxq-sdk-go
```

## 快速开始

```go
package main

import (
    "context"
    "fmt"
    "os"

    zsxq "github.com/zsxq-sdk/zsxq-sdk-go/client"
)

func main() {
    // 创建客户端
    client := zsxq.NewClientBuilder().
        SetToken(os.Getenv("ZSXQ_TOKEN")).
        SetTimeout(10 * time.Second).
        SetRetryCount(3).
        MustBuild()

    ctx := context.Background()

    // 获取我的星球列表
    groups, err := client.Groups().List(ctx)
    if err != nil {
        panic(err)
    }

    for _, g := range groups {
        fmt.Printf("星球: %s\n", g.Name)
    }

    // 获取当前用户信息
    me, err := client.Users().Self(ctx)
    if err != nil {
        panic(err)
    }
    fmt.Printf("用户: %s\n", me.Name)
}
```

## 核心功能

### 星球管理

```go
// 获取星球列表
groups, err := client.Groups().List(ctx)

// 获取星球详情
group, err := client.Groups().Get(ctx, groupID)

// 获取星球统计
stats, err := client.Groups().GetStatistics(ctx, groupID)

// 获取星球标签
hashtags, err := client.Groups().GetHashtags(ctx, groupID)
```

### 话题操作

```go
// 获取话题列表
topics, err := client.Topics().List(ctx, groupID, nil)

// 带参数的话题列表
options := &request.ListTopicsOptions{
    Count: 20,
    Scope: "all",
}
topics, err := client.Topics().List(ctx, groupID, options)

// 获取话题详情
topic, err := client.Topics().Get(ctx, topicID)

// 获取话题评论
comments, err := client.Topics().GetComments(ctx, topicID, nil)
```

### 打卡管理

```go
// 获取打卡项目列表
checkins, err := client.Checkins().List(ctx, groupID, nil)

// 获取打卡统计
stats, err := client.Checkins().GetStatistics(ctx, groupID, checkinID)

// 获取打卡排行榜
ranking, err := client.Checkins().GetRankingList(ctx, groupID, checkinID, nil)
```

### 训练营（打卡）

```go
import "github.com/zsxq-sdk/zsxq-sdk-go/request"

// 创建训练营（有截止时间）
params := request.CreateCheckinParams{
    Title:                "7天打卡挑战",           // 训练营标题
    Text:                 "每天完成一个任务",        // 训练营描述
    CheckinDays:          7,                     // 打卡天数
    Type:                 "accumulated",         // 打卡类型: accumulated(累计) / continuous(连续)
    ShowTopicsOnTimeline: false,                 // 是否在时间线展示
    Validity: &request.CheckinValidity{
        LongPeriod:     false,
        ExpirationTime: "2025-12-31T23:59:59.000+0800",  // 截止时间
    },
}
checkin, err := client.Checkins().Create(ctx, groupID, params)
if err != nil {
    panic(err)
}
fmt.Printf("创建成功: %d\n", checkin.CheckinID)

// 创建长期有效的训练营
longTermParams := request.CreateCheckinParams{
    Title:       "每日学习打卡",
    Text:        "持续学习，每天进步",
    CheckinDays: 21,
    Type:        "accumulated",
    Validity:    &request.CheckinValidity{LongPeriod: true},  // 长期有效
}
longTermCheckin, err := client.Checkins().Create(ctx, groupID, longTermParams)

// 更新训练营
updateParams := request.UpdateCheckinParams{
    Title: "新标题",
    Text:  "更新后的描述",
}
updated, err := client.Checkins().Update(ctx, groupID, checkinID, updateParams)
```

## 错误处理

```go
import "github.com/zsxq-sdk/zsxq-sdk-go/exception"

groups, err := client.Groups().List(ctx)
if err != nil {
    switch e := err.(type) {
    case *exception.TokenExpiredError:
        // Token 过期，需要重新获取
        fmt.Println("Token 已过期")
    case *exception.RateLimitError:
        // 触发限流
        fmt.Println("请求过于频繁")
    case *exception.ZsxqError:
        // 其他 API 错误
        fmt.Printf("错误码: %d, 信息: %s\n", e.Code, e.Message)
    default:
        // 网络或其他错误
        fmt.Printf("错误: %v\n", err)
    }
}
```

## 开发

```bash
# 整理依赖
go mod tidy

# 构建
go build ./...

# 运行测试
go test ./...

# 带详细输出运行测试
go test ./... -v

# 运行单个包测试
go test ./client -v
```

## 许可证

MIT License

---

**注意**: 本项目仅供学习和研究使用，请遵守知识星球的服务条款。
