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

## 训练营接口

### 1. 获取训练营列表

**接口**: `GET /training-camps`

**Headers**: `Authorization: Bearer <token>`

**查询参数**:
- `planetId` (必填): 星球ID
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
- `limit` (可选): 返回数量，默认100

**响应**:
```json
{
  "success": true,
  "data": [
    {
      "rank": 1,
      "userId": "uuid",
      "username": "张三",
      "avatar": "https://...",
      "checkinCount": 21,
      "continuousCount": 21
    }
  ]
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
| `VALIDATION_ERROR` | 参数验证错误 |
| `INTERNAL_SERVER_ERROR` | 服务器内部错误 |
| `RATE_LIMIT_EXCEEDED` | 请求频率超限 |
| `ZSXQ_API_ERROR` | 知识星球API调用失败 |

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
