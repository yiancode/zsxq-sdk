# 知识星球原生API参考

> 项目逆向分析整理，供开发参考

## 概述

知识星球APP通过 `https://api.zsxq.com` 提供API服务。本文档整理了已验证可用的API端点。

## 认证机制

### Token格式

```
Authorization: UUID_HASH
```

- 格式示例: `5a8c9b2f-1234-5678-9abc-def012345678_abc123def456789`
- 获取方式: 使用Charles/Fiddler抓包，从请求Header中获取

### 请求头要求

```http
Authorization: {token}
User-Agent: xiaomiquan/5.28.1 (iPhone; iOS 14.7.1; Scale/3.00)
Content-Type: application/json
Accept: application/json, text/plain, */*
x-request-id: {random-uuid}
```

**重要**: User-Agent必须伪装为知识星球APP客户端，否则可能被拒绝访问。

## 响应格式

### 成功响应

```json
{
  "succeeded": true,
  "resp_data": {
    // 实际数据
  }
}
```

### 错误响应

```json
{
  "succeeded": false,
  "error": {
    "code": 10001,
    "message": "错误描述"
  }
}
```

### HTTP状态码

| 状态码 | 含义 | 处理方式 |
|-------|------|---------|
| 200 | 成功 | 检查 `succeeded` 字段 |
| 401 | Token失效 | 重新获取Token |
| 429 | 请求频率过高 | 降低请求频率 |
| 500+ | 服务器错误 | 重试或稍后再试 |

---

## 打卡项目API

### 1. 获取打卡项目列表

**端点**: `GET /v2/groups/{group_id}/checkins`

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| scope | string | 否 | 项目范围: ongoing(进行中) / closed(已关闭) / over(已结束) |
| count | integer | 否 | 返回数量，最大100 |

**示例请求**:
```bash
GET https://api.zsxq.com/v2/groups/123456/checkins?scope=ongoing&count=100
```

**响应数据**:
```json
{
  "succeeded": true,
  "resp_data": {
    "checkins": [
      {
        "checkin_id": 1141152412,
        "name": "2025年打卡挑战",
        "description": "每日打卡记录",
        "start_time": "2025-01-01T00:00:00.000+0800",
        "end_time": "2025-12-31T23:59:59.000+0800",
        "background": {
          "image_url": "https://images.zsxq.com/..."
        },
        "users_count": 150,
        "checkined_count": 3500,
        "current_continuous_days": 10,
        "rules": "每天必须打卡",
        "create_time": "2025-01-01T00:00:00.000+0800"
      }
    ]
  }
}
```

---

### 2. 获取打卡项目详情

**端点**: `GET /v2/groups/{group_id}/checkins/{checkin_id}`

**路径参数**:
| 参数 | 说明 |
|------|------|
| group_id | 星球ID |
| checkin_id | 打卡项目ID |

**响应数据**: 同上，返回单个项目详情

---

### 3. 获取项目统计数据

**端点**: `GET /v2/groups/{group_id}/checkins/{checkin_id}/statistics`

**响应数据**:
```json
{
  "succeeded": true,
  "resp_data": {
    "users_count": 150,
    "checkined_count": 3500,
    "today_count": 120,
    "continuous_rate": 0.85
  }
}
```

**字段说明**:
- `users_count`: 参与总人数
- `checkined_count`: 累计打卡总数
- `today_count`: 今日打卡数
- `continuous_rate`: 连续打卡率

---

### 4. 获取每日统计

**端点**: `GET /v2/groups/{group_id}/checkins/{checkin_id}/statistics/daily`

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| date | string | 否 | ISO8601格式日期，需URL编码 |

**示例请求**:
```bash
GET /v2/groups/123456/checkins/789/statistics/daily?date=2025-01-15T10%3A30%3A00.000%2B0800
```

**响应数据**:
```json
{
  "succeeded": true,
  "resp_data": {
    "date": "2025-01-15",
    "count": 120,
    "new_users": 5,
    "active_users": 115
  }
}
```

---

### 5. 获取排行榜

**端点**: `GET /v2/groups/{group_id}/checkins/{checkin_id}/ranking_list`

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| type | string | 否 | continuous(连续天数) / accumulated(累计次数) |
| index | integer | 否 | 分页索引，从0开始 |

**响应数据**:
```json
{
  "succeeded": true,
  "resp_data": {
    "ranking_list": [
      {
        "rankings": 1,
        "user": {
          "user_id": 585221282158424,
          "name": "张三",
          "alias": "",
          "avatar_url": "https://images.zsxq.com/..."
        },
        "checkined_days": 45
      }
    ],
    "user_specific": {
      "rankings": 6,
      "checkined_days": 20
    }
  }
}
```

**字段说明**:
- `ranking_list`: 排行榜列表
- `rankings`: 排名（从1开始）
- `checkined_days`: 打卡天数（连续或累计，取决于type参数）
- `user_specific`: 当前用户的排名信息

---

### 6. 获取打卡话题列表

**端点**: `GET /v2/groups/{group_id}/checkins/{checkin_id}/topics`

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| count | integer | 否 | 返回数量，默认20 |

**响应数据**:
```json
{
  "succeeded": true,
  "resp_data": {
    "topics": [
      {
        "topic": {
          "topic_id": 123456789,
          "title": "今日打卡",
          "text": "今天学习了Flask框架...",
          "create_time": "2025-01-15T08:30:00.000+0800",
          "user": {
            "user_id": 585221282158424,
            "name": "张三",
            "avatar_url": "https://images.zsxq.com/..."
          }
        }
      }
    ]
  }
}
```

---

## 星球API

### 1. 获取星球信息

**端点**: `GET /v2/groups/{group_id}`

**响应数据**:
```json
{
  "succeeded": true,
  "resp_data": {
    "group": {
      "group_id": 123456,
      "name": "技术交流圈",
      "description": "分享技术心得",
      "avatar_url": "https://images.zsxq.com/...",
      "members_count": 1000,
      "topics_count": 5000,
      "owner": {
        "user_id": 585221282158424,
        "name": "星主名称",
        "avatar_url": "https://images.zsxq.com/..."
      },
      "create_time": "2020-01-01T00:00:00.000+0800"
    }
  }
}
```

---

### 2. 获取星球话题列表

**端点**: `GET /v2/groups/{group_id}/topics`

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| count | integer | 否 | 返回数量 |
| end_time | string | 否 | 分页游标（上一页最后一条的create_time） |

**响应数据**:
```json
{
  "succeeded": true,
  "resp_data": {
    "topics": [
      {
        "topic_id": 123456789,
        "type": "talk",
        "title": "话题标题",
        "text": "话题内容...",
        "create_time": "2025-01-15T08:30:00.000+0800",
        "likes_count": 50,
        "comments_count": 10,
        "user": {
          "user_id": 585221282158424,
          "name": "张三",
          "avatar_url": "https://images.zsxq.com/..."
        }
      }
    ]
  }
}
```

---

### 3. 获取话题详情

**端点**: `GET /v2/topics/{topic_id}`

---

### 4. 获取星球成员列表

**端点**: `GET /v2/groups/{group_id}/members`

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| count | integer | 否 | 返回数量 |
| end_time | string | 否 | 分页游标 |

---

## 用户API

### 1. 获取当前用户信息

**端点**: `GET /v2/users/self`

**响应数据**:
```json
{
  "succeeded": true,
  "resp_data": {
    "user": {
      "user_id": 585221282158424,
      "name": "用户名",
      "avatar_url": "https://images.zsxq.com/...",
      "description": "个人简介"
    }
  }
}
```

---

## 最佳实践

### 1. 请求频率控制

- 建议每分钟不超过60次请求
- 遇到429错误时，暂停1-5分钟后重试
- 使用缓存减少重复请求

### 2. Token管理

- Token有效期未知，建议定期更新
- 不要在代码中硬编码Token
- 使用配置文件或环境变量管理

### 3. 错误处理

```typescript
// 推荐的错误处理模式
if (response.status === 401) {
  throw new ZsxqTokenExpiredError('Token已失效');
}
if (response.status === 429) {
  throw new ZsxqRateLimitError('请求过于频繁');
}
if (!response.data.succeeded) {
  throw new ZsxqApiError(response.data.error?.message || '未知错误');
}
```

### 4. 数据格式化

原生API返回的字段命名使用下划线风格，建议在业务层转换为驼峰命名：

```typescript
// 原始数据
{ user_id: 123, avatar_url: 'https://...' }

// 格式化后
{ userId: 123, avatarUrl: 'https://...' }
```

---

## 附录：API端点汇总

| 功能 | 方法 | 端点 |
|------|------|------|
| 获取打卡项目列表 | GET | `/v2/groups/{group_id}/checkins` |
| 获取打卡项目详情 | GET | `/v2/groups/{group_id}/checkins/{checkin_id}` |
| 获取项目统计 | GET | `/v2/groups/{group_id}/checkins/{checkin_id}/statistics` |
| 获取每日统计 | GET | `/v2/groups/{group_id}/checkins/{checkin_id}/statistics/daily` |
| 获取排行榜 | GET | `/v2/groups/{group_id}/checkins/{checkin_id}/ranking_list` |
| 获取打卡话题 | GET | `/v2/groups/{group_id}/checkins/{checkin_id}/topics` |
| 获取星球信息 | GET | `/v2/groups/{group_id}` |
| 获取星球话题 | GET | `/v2/groups/{group_id}/topics` |
| 获取话题详情 | GET | `/v2/topics/{topic_id}` |
| 获取星球成员 | GET | `/v2/groups/{group_id}/members` |
| 获取当前用户 | GET | `/v2/users/self` |
