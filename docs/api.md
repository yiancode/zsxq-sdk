# API 接口文档

## 基础信息

- **Base URL**: `http://localhost:3000/api/v1`
- **认证方式**: Bearer Token (JWT)
- **响应格式**: JSON

## 通用响应格式

### 成功响应

```json
{
  "success": true,
  "data": { ... },
  "message": "操作成功",
  "timestamp": "2025-12-07T12:00:00.000Z"
}
```

### 错误响应

```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": ["错误信息"],
    "details": {}
  },
  "timestamp": "2025-12-07T12:00:00.000Z",
  "path": "/api/v1/planets"
}
```

### 分页响应

```json
{
  "success": true,
  "data": {
    "items": [...],
    "total": 100,
    "page": 1,
    "pageSize": 20,
    "totalPages": 5
  },
  "message": "获取成功",
  "timestamp": "2025-12-07T12:00:00.000Z"
}
```

## 认证接口

### 1. 用户登录

**接口**: `POST /auth/login`

**请求体**:
```json
{
  "username": "string",
  "password": "string"
}
```

**响应**:
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "user": {
      "id": "uuid",
      "username": "张三",
      "role": "user"
    }
  }
}
```

## 用户接口

### 1. 获取当前用户信息

**接口**: `GET /users/me`

**Headers**: `Authorization: Bearer <token>`

**响应**:
```json
{
  "success": true,
  "data": {
    "id": "uuid",
    "username": "张三",
    "avatar": "https://...",
    "role": "user",
    "createdAt": "2025-12-07T12:00:00.000Z"
  }
}
```

## 星球接口

### 1. 获取星球列表

**接口**: `GET /planets`

**Headers**: `Authorization: Bearer <token>`

**查询参数**:
- `page` (可选): 页码，默认1
- `pageSize` (可选): 每页数量，默认20

**响应**:
```json
{
  "success": true,
  "data": [
    {
      "id": "uuid",
      "zsxqPlanetId": "123456",
      "name": "技术交流圈",
      "description": "分享技术心得",
      "avatar": "https://...",
      "memberCount": 1000,
      "owner": {
        "id": "uuid",
        "username": "星主名称",
        "avatar": "https://..."
      },
      "createdAt": "2025-12-07T12:00:00.000Z"
    }
  ]
}
```

### 2. 获取星球详情

**接口**: `GET /planets/:id`

**Headers**: `Authorization: Bearer <token>`

**路径参数**:
- `id`: 星球ID

**响应**:
```json
{
  "success": true,
  "data": {
    "id": "uuid",
    "zsxqPlanetId": "123456",
    "name": "技术交流圈",
    "description": "分享技术心得",
    "avatar": "https://...",
    "memberCount": 1000,
    "owner": {
      "id": "uuid",
      "username": "星主名称",
      "avatar": "https://..."
    },
    "createdAt": "2025-12-07T12:00:00.000Z"
  }
}
```

### 3. 获取星球话题列表

**接口**: `GET /planets/:id/topics`

**Headers**: `Authorization: Bearer <token>`

**路径参数**:
- `id`: 星球ID

**查询参数**:
- `page` (可选): 页码，默认1
- `pageSize` (可选): 每页数量，默认20

**响应**:
```json
{
  "success": true,
  "data": {
    "items": [
      {
        "id": "uuid",
        "zsxqTopicId": "789",
        "title": "如何学习TypeScript",
        "content": "分享一些学习经验...",
        "author": {
          "id": "uuid",
          "username": "张三",
          "avatar": "https://..."
        },
        "likesCount": 50,
        "commentsCount": 10,
        "createdAt": "2025-12-07T12:00:00.000Z"
      }
    ],
    "total": 100,
    "page": 1,
    "pageSize": 20,
    "totalPages": 5
  }
}
```

## 话题接口

### 1. 获取话题详情

**接口**: `GET /topics/:id`

**Headers**: `Authorization: Bearer <token>`

**路径参数**:
- `id`: 话题ID

**响应**:
```json
{
  "success": true,
  "data": {
    "id": "uuid",
    "zsxqTopicId": "789",
    "title": "如何学习TypeScript",
    "content": "分享一些学习经验...",
    "planetId": "uuid",
    "author": {
      "id": "uuid",
      "username": "张三",
      "avatar": "https://..."
    },
    "likesCount": 50,
    "commentsCount": 10,
    "createdAt": "2025-12-07T12:00:00.000Z",
    "updatedAt": "2025-12-07T12:00:00.000Z"
  }
}
```

## 打卡项目接口

> 基于知识星球原生打卡功能封装，参考 ZSXQCheckIn 项目设计

### 1. 获取打卡项目列表

**接口**: `GET /planets/:planetId/checkins`

**Headers**: `Authorization: Bearer <token>`

**路径参数**:
- `planetId`: 星球ID

**查询参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| scope | string | 否 | ongoing | 项目范围: ongoing(进行中) / closed(已关闭) / over(已结束) |

**响应**:
```json
{
  "success": true,
  "data": {
    "items": [
      {
        "projectId": "1141152412",
        "title": "2025年打卡挑战",
        "description": "每日打卡记录",
        "status": "ongoing",
        "startDate": "2025-01-01T00:00:00.000+08:00",
        "endDate": "2025-12-31T23:59:59.000+08:00",
        "coverImage": "https://images.zsxq.com/...",
        "totalMembers": 150,
        "totalCheckins": 3500,
        "cachedAt": "2025-01-15T10:30:00.000+08:00"
      }
    ],
    "total": 5
  },
  "timestamp": "2025-12-07T12:00:00.000Z"
}
```

**缓存策略**: TTL=7200秒(2小时)

---

### 2. 获取打卡项目详情

**接口**: `GET /checkins/:checkinId`

**Headers**: `Authorization: Bearer <token>`

**路径参数**:
- `checkinId`: 打卡项目ID

**响应**:
```json
{
  "success": true,
  "data": {
    "projectId": "1141152412",
    "title": "2025年打卡挑战",
    "description": "每日打卡记录",
    "status": "ongoing",
    "startDate": "2025-01-01T00:00:00.000+08:00",
    "endDate": "2025-12-31T23:59:59.000+08:00",
    "coverImage": "https://images.zsxq.com/...",
    "totalMembers": 150,
    "totalCheckins": 3500,
    "continuousDays": 10,
    "rules": "每天必须打卡",
    "createdAt": "2025-01-01T00:00:00.000+08:00",
    "cachedAt": "2025-01-15T10:30:00.000+08:00"
  },
  "timestamp": "2025-12-07T12:00:00.000Z"
}
```

---

### 3. 获取打卡项目统计

**接口**: `GET /checkins/:checkinId/stats`

**Headers**: `Authorization: Bearer <token>`

**路径参数**:
- `checkinId`: 打卡项目ID

**响应**:
```json
{
  "success": true,
  "data": {
    "totalMembers": 150,
    "totalCheckins": 3500,
    "todayCheckins": 120,
    "continuousRate": 0.85,
    "avgCheckinsPerMember": 23.33,
    "cachedAt": "2025-01-15T10:30:00.000+08:00"
  },
  "timestamp": "2025-12-07T12:00:00.000Z"
}
```

**字段说明**:
- `totalMembers`: 项目参与总人数
- `totalCheckins`: 累计打卡总数
- `todayCheckins`: 今日打卡数
- `continuousRate`: 连续打卡率（0-1之间的小数）
- `avgCheckinsPerMember`: 人均打卡次数

**缓存策略**: TTL=3600秒(1小时)

---

### 4. 获取打卡每日统计

**接口**: `GET /checkins/:checkinId/daily-stats`

**Headers**: `Authorization: Bearer <token>`

**路径参数**:
- `checkinId`: 打卡项目ID

**查询参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| date | string | 否 | 今天 | 查询日期，格式: YYYY-MM-DD |

**响应**:
```json
{
  "success": true,
  "data": {
    "date": "2025-01-15",
    "totalCheckins": 120,
    "newMembers": 5,
    "activeMembers": 115,
    "cachedAt": "2025-01-15T10:30:00.000+08:00"
  },
  "timestamp": "2025-12-07T12:00:00.000Z"
}
```

**缓存策略**: TTL=1800秒(30分钟)

---

### 5. 获取打卡排行榜

**接口**: `GET /checkins/:checkinId/leaderboard`

**Headers**: `Authorization: Bearer <token>`

**路径参数**:
- `checkinId`: 打卡项目ID

**查询参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| type | string | 否 | continuous | 排行榜类型: continuous(连续天数) / accumulated(累计次数) |
| limit | integer | 否 | 10 | 返回数量(1-100) |

**响应**:
```json
{
  "success": true,
  "data": {
    "type": "continuous",
    "rankings": [
      {
        "rank": 1,
        "user": {
          "userId": "585221282158424",
          "name": "张三",
          "alias": "",
          "avatar": "https://images.zsxq.com/..."
        },
        "days": 45
      },
      {
        "rank": 2,
        "user": {
          "userId": "585221282158425",
          "name": "李四",
          "alias": "小李",
          "avatar": "https://images.zsxq.com/..."
        },
        "days": 42
      }
    ],
    "total": 150,
    "userRank": {
      "rank": 6,
      "days": 20
    },
    "cachedAt": "2025-01-15T10:30:00.000+08:00"
  },
  "timestamp": "2025-12-07T12:00:00.000Z"
}
```

**字段说明**:
- `type`: 排行榜类型
- `rankings`: 排行榜列表
  - `rank`: 排名（从1开始）
  - `user`: 用户信息
  - `days`: 打卡天数（连续或累计，取决于type）
- `total`: 该项目的总参与人数
- `userRank`: 当前用户的排名信息（如果有）

**缓存策略**: TTL=3600秒(1小时)

---

### 6. 获取打卡话题列表

**接口**: `GET /checkins/:checkinId/topics`

**Headers**: `Authorization: Bearer <token>`

**路径参数**:
- `checkinId`: 打卡项目ID

**查询参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| count | integer | 否 | 20 | 返回数量(1-100) |

**响应**:
```json
{
  "success": true,
  "data": {
    "items": [
      {
        "topicId": "123456789",
        "title": "今日打卡",
        "content": "今天学习了NestJS框架...",
        "createTime": "2025-01-15T08:30:00.000+08:00",
        "user": {
          "userId": "585221282158424",
          "name": "张三",
          "avatar": "https://images.zsxq.com/..."
        }
      }
    ],
    "total": 100,
    "cachedAt": "2025-01-15T10:30:00.000+08:00"
  },
  "timestamp": "2025-12-07T12:00:00.000Z"
}
```

**缓存策略**: TTL=600秒(10分钟)

---

## 训练营接口

> 训练营是打卡项目的别称，以下接口与打卡项目接口功能相同

### 1. 获取训练营列表

**接口**: `GET /training-camps`

**Headers**: `Authorization: Bearer <token>`

**查询参数**:
- `planetId` (必填): 星球ID
- `scope` (可选): 项目范围，默认 ongoing
- `page` (可选): 页码，默认1
- `pageSize` (可选): 每页数量，默认20

**响应**:
```json
{
  "success": true,
  "data": {
    "items": [
      {
        "id": "uuid",
        "zsxqCampId": "456",
        "name": "21天打卡挑战",
        "description": "坚持21天打卡",
        "planetId": "uuid",
        "startDate": "2025-12-01",
        "endDate": "2025-12-21",
        "participantsCount": 500,
        "createdAt": "2025-12-07T12:00:00.000Z"
      }
    ],
    "total": 10,
    "page": 1,
    "pageSize": 20,
    "totalPages": 1
  }
}
```

### 2. 获取训练营详情

**接口**: `GET /training-camps/:id`

**Headers**: `Authorization: Bearer <token>`

**路径参数**:
- `id`: 训练营ID

**响应**:
```json
{
  "success": true,
  "data": {
    "id": "uuid",
    "zsxqCampId": "456",
    "name": "21天打卡挑战",
    "description": "坚持21天打卡",
    "planetId": "uuid",
    "startDate": "2025-12-01",
    "endDate": "2025-12-21",
    "participantsCount": 500,
    "createdAt": "2025-12-07T12:00:00.000Z"
  }
}
```

### 3. 获取训练营打卡记录

**接口**: `GET /training-camps/:id/checkins`

**Headers**: `Authorization: Bearer <token>`

**路径参数**:
- `id`: 训练营ID

**查询参数**:
- `page` (可选): 页码，默认1
- `pageSize` (可选): 每页数量，默认20

**响应**:
```json
{
  "success": true,
  "data": {
    "items": [
      {
        "id": "uuid",
        "userId": "uuid",
        "username": "张三",
        "avatar": "https://...",
        "content": "今天打卡内容",
        "checkinDate": "2025-12-07",
        "createdAt": "2025-12-07T12:00:00.000Z"
      }
    ],
    "total": 200,
    "page": 1,
    "pageSize": 20,
    "totalPages": 10
  }
}
```

### 4. 获取训练营排行榜

**接口**: `GET /training-camps/:id/ranking`

**Headers**: `Authorization: Bearer <token>`

**路径参数**:
- `id`: 训练营ID

**查询参数**:
- `type` (可选): 排行榜类型，continuous(连续) / accumulated(累计)，默认continuous
- `limit` (可选): 返回数量，默认100

**响应**:
```json
{
  "success": true,
  "data": {
    "type": "continuous",
    "rankings": [
      {
        "rank": 1,
        "user": {
          "userId": "uuid",
          "name": "张三",
          "avatar": "https://..."
        },
        "days": 21
      }
    ],
    "total": 500,
    "userRank": {
      "rank": 10,
      "days": 15
    }
  }
}
```

## 星主专用接口

> 以下接口需要星主权限 (role: 'owner')

### 1. 获取星球成员列表

**接口**: `GET /owner/planets/:planetId/members`

**Headers**: `Authorization: Bearer <token>`

**路径参数**:
- `planetId`: 星球ID

**查询参数**:
- `page` (可选): 页码，默认1
- `pageSize` (可选): 每页数量，默认20
- `keyword` (可选): 搜索关键词

**响应**:
```json
{
  "success": true,
  "data": {
    "items": [
      {
        "id": "uuid",
        "username": "张三",
        "avatar": "https://...",
        "joinedAt": "2025-12-07T12:00:00.000Z"
      }
    ],
    "total": 1000,
    "page": 1,
    "pageSize": 20,
    "totalPages": 50
  }
}
```

### 2. 创建训练营

**接口**: `POST /owner/training-camps`

**Headers**: `Authorization: Bearer <token>`

**请求体**:
```json
{
  "planetId": "uuid",
  "name": "21天打卡挑战",
  "description": "坚持21天打卡",
  "startDate": "2025-12-01",
  "endDate": "2025-12-21"
}
```

**响应**:
```json
{
  "success": true,
  "data": {
    "id": "uuid",
    "zsxqCampId": "456",
    "name": "21天打卡挑战",
    "description": "坚持21天打卡",
    "planetId": "uuid",
    "startDate": "2025-12-01",
    "endDate": "2025-12-21",
    "participantsCount": 0,
    "createdAt": "2025-12-07T12:00:00.000Z"
  },
  "message": "训练营创建成功"
}
```

### 3. 创建话题

**接口**: `POST /owner/topics`

**Headers**: `Authorization: Bearer <token>`

**请求体**:
```json
{
  "planetId": "uuid",
  "title": "如何学习TypeScript",
  "content": "分享一些学习经验..."
}
```

**响应**:
```json
{
  "success": true,
  "data": {
    "id": "uuid",
    "zsxqTopicId": "789",
    "title": "如何学习TypeScript",
    "content": "分享一些学习经验...",
    "planetId": "uuid",
    "authorId": "uuid",
    "likesCount": 0,
    "commentsCount": 0,
    "createdAt": "2025-12-07T12:00:00.000Z"
  },
  "message": "话题创建成功"
}
```

### 4. 更新话题

**接口**: `PUT /owner/topics/:id`

**Headers**: `Authorization: Bearer <token>`

**路径参数**:
- `id`: 话题ID

**请求体**:
```json
{
  "title": "如何学习TypeScript（更新）",
  "content": "更新后的内容..."
}
```

**响应**:
```json
{
  "success": true,
  "data": {
    "id": "uuid",
    "zsxqTopicId": "789",
    "title": "如何学习TypeScript（更新）",
    "content": "更新后的内容...",
    "updatedAt": "2025-12-07T12:00:00.000Z"
  },
  "message": "话题更新成功"
}
```

### 5. 删除话题

**接口**: `DELETE /owner/topics/:id`

**Headers**: `Authorization: Bearer <token>`

**路径参数**:
- `id`: 话题ID

**响应**:
```json
{
  "success": true,
  "data": null,
  "message": "话题删除成功"
}
```

## 错误代码

| 错误代码 | 说明 |
|---------|------|
| `UNAUTHORIZED` | 未授权，需要登录 |
| `FORBIDDEN` | 权限不足 |
| `NOT_FOUND` | 资源不存在 |
| `PLANET_NOT_FOUND` | 星球不存在 |
| `TOPIC_NOT_FOUND` | 话题不存在 |
| `CAMP_NOT_FOUND` | 训练营不存在 |
| `CHECKIN_NOT_FOUND` | 打卡项目不存在 |
| `VALIDATION_ERROR` | 参数验证错误 |
| `INTERNAL_SERVER_ERROR` | 服务器内部错误 |
| `RATE_LIMIT_EXCEEDED` | 请求频率超限 |
| `ZSXQ_API_ERROR` | 知识星球API调用失败 |
| `ZSXQ_TOKEN_EXPIRED` | 知识星球Token已失效 |
| `ZSXQ_RATE_LIMIT` | 知识星球API限流 |

## 限流规则

- **普通用户**: 100请求/分钟
- **星主**: 200请求/分钟
- 超限返回 `429 Too Many Requests`

## 注意事项

1. 所有时间字段使用ISO 8601格式（UTC时区）
2. 所有ID字段使用UUID格式
3. 分页参数：page从1开始，pageSize最大100
4. 请求Header需包含 `Authorization: Bearer <token>`
5. 响应中的 `zsxq*` 字段为知识星球原始ID
6. 带有 `cachedAt` 字段的响应表示数据来自缓存

## 相关文档

- [知识星球原生API参考](./zsxq-native-api-reference.md) - 原生API端点和响应格式
- [缓存设计方案](./cache-design.md) - Redis缓存策略和实现
- [ZsxqClient增强方案](./zsxq-client-enhancement.md) - API客户端增强设计
