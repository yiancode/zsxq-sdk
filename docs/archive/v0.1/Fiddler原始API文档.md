# 知识星球 API 文档

**生成时间**: 2025-12-09 16:56:15
**数据来源**: Fiddler Everywhere 抓包分析
**文档优化**: 2025-12-11（去重 + JSON 格式优化）

---

## 目录

- [认证机制](#认证机制)
- [1. 星球管理](#1-星球管理)
  - [获取用户星球列表](#11-获取用户星球列表)
  - [获取星球详情](#12-获取星球详情)
  - [获取星球标签列表](#13-获取星球标签列表)
  - [获取星球菜单配置](#14-获取星球菜单配置)
  - [获取星球角色成员](#15-获取星球角色成员)
  - [获取星球统计数据](#16-获取星球统计数据)
  - [获取星球成员详情](#17-获取星球成员详情)
  - [获取成员活跃摘要](#18-获取成员活跃摘要)
  - [获取星球专栏列表](#19-获取星球专栏列表)
  - [获取专栏汇总信息](#110-获取专栏汇总信息)
  - [获取星球续费信息](#111-获取星球续费信息)
  - [获取星球分销信息](#112-获取星球分销信息)
  - [获取可升级星球列表](#113-获取可升级星球列表)
  - [获取推荐星球列表](#114-获取推荐星球列表)
  - [获取未读话题数量](#115-获取未读话题数量)
- [2. 话题管理](#2-话题管理)
  - [获取星球话题列表](#21-获取星球话题列表)
  - [获取专栏话题列表](#22-获取专栏话题列表)
  - [获取话题详情](#23-获取话题详情)
  - [获取话题基础信息](#24-获取话题基础信息)
  - [获取话题评论列表](#25-获取话题评论列表)
  - [获取话题打赏列表](#26-获取话题打赏列表)
  - [获取相关推荐话题](#27-获取相关推荐话题)
- [3. 标签系统](#3-标签系统)
  - [获取标签话题列表](#31-获取标签话题列表)
- [4. 打卡系统](#4-打卡系统)
  - [获取打卡项目列表](#41-获取打卡项目列表)
  - [获取打卡项目详情](#42-获取打卡项目详情)
  - [获取打卡项目统计](#43-获取打卡项目统计)
  - [获取打卡每日统计](#44-获取打卡每日统计)
  - [获取打卡话题列表](#45-获取打卡话题列表)
  - [获取打卡排行榜](#46-获取打卡排行榜)
  - [获取打卡参与用户](#47-获取打卡参与用户)
  - [创建打卡项目](#48-创建打卡项目)
  - [更新打卡项目](#49-更新打卡项目)
  - [获取我的打卡记录](#410-获取我的打卡记录)
  - [获取我的打卡日期](#411-获取我的打卡日期)
  - [获取我的打卡统计](#412-获取我的打卡统计)
- [5. 排行榜系统](#5-排行榜系统)
  - [获取星球排行榜](#51-获取星球排行榜)
  - [获取星球排行统计](#52-获取星球排行统计)
  - [获取积分排行榜](#53-获取积分排行榜)
  - [获取我的积分统计](#54-获取我的积分统计)
  - [获取积分榜设置](#55-获取积分榜设置)
- [6. 用户系统](#6-用户系统)
  - [获取当前用户信息](#61-获取当前用户信息)
  - [获取指定用户信息](#62-获取指定用户信息)
  - [获取用户统计数据](#63-获取用户统计数据)
  - [获取用户头像URL](#64-获取用户头像url)
  - [获取用户动态足迹](#65-获取用户动态足迹)
  - [获取用户星球足迹](#66-获取用户星球足迹)
  - [获取用户创建的星球](#67-获取用户创建的星球)
  - [获取申请中的星球](#68-获取申请中的星球)
  - [获取邀请人信息](#69-获取邀请人信息)
  - [获取我的优惠券](#610-获取我的优惠券)
  - [获取我的备注列表](#611-获取我的备注列表)
  - [获取推荐关注用户](#612-获取推荐关注用户)
- [7. 数据面板](#7-数据面板)
  - [获取星球数据概览](#71-获取星球数据概览)
  - [获取星球收入概览](#72-获取星球收入概览)
  - [获取星球权限配置](#73-获取星球权限配置)
- [8. 其他接口](#8-其他接口)
  - [获取PK群组详情](#81-获取pk群组详情)
  - [获取PK对战记录](#82-获取pk对战记录)
  - [解析URL详情](#83-解析url详情)
  - [获取菜单阅读时间](#84-获取菜单阅读时间)
  - [更新菜单阅读时间](#85-更新菜单阅读时间)
- [错误码说明](#错误码说明)

---

## 认证机制

所有 API 请求都需要以下请求头：

| 请求头 | 说明 | 示例 |
|--------|------|------|
| `authorization` | 认证 Token | `D047A423-A...` |
| `x-timestamp` | Unix 时间戳 | `1765268187` |
| `x-signature` | 请求签名 (SHA1) | `dd7b51bee...` |
| `x-aduid` | 设备唯一标识 | `d75d966c-ed30...` |
| `x-version` | App 版本 | `2.83.0` |
| `x-request-id` | 请求追踪 ID (UUID) | `9af8e4c1...` |
| `user-agent` | 用户代理 | `xiaomiquan/5.29.1 iOS/phone/26.1` |

**基础 URL**: `https://api.zsxq.com`

---

## 1. 星球管理

### 1.1 获取用户星球列表

`GET /v2/groups`

获取当前登录用户已加入的所有星球列表。

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "groups": [
      {
        "group_id": 88885121521552,
        "name": "易安AI编程·出海赚钱",
        "type": "free",
        "background_url": "https://images.zsxq.com/...",
        "privilege_user_last_topic_create_time": "2025-12-08T18:09:50.408+0800",
        "risk_level": "normal",
        "partner_ids": [51544811224184],
        "admin_ids": [],
        "user_specific": {
          "role": "member"
        }
      }
    ]
  }
}
```

---

### 1.2 获取星球详情

`GET /v2/groups/{group_id}`

获取指定星球的详细信息。

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| `group_id` | number | 星球 ID |

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "group": {
      "group_id": 15555411412112,
      "name": "AI私域赚钱",
      "description": "星球简介...",
      "type": "pay",
      "background_url": "https://images.zsxq.com/...",
      "member_count": 1234,
      "topics_count": 567,
      "owner": {
        "user_id": 582884445452854,
        "name": "星主名称",
        "avatar_url": "https://images.zsxq.com/..."
      }
    }
  }
}
```

---

### 1.3 获取星球标签列表

`GET /v2/groups/{group_id}/hashtags`

获取星球的所有标签。

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "hashtags": [
      {
        "hashtag_id": 28844882581111,
        "name": "AI工具",
        "topics_count": 42
      }
    ]
  }
}
```

---

### 1.4 获取星球菜单配置

`GET /v2/groups/{group_id}/menus`

获取星球的菜单配置信息。

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "menus": [
      {
        "menu_id": 123456,
        "name": "精华",
        "type": "digests"
      }
    ]
  }
}
```

---

### 1.5 获取星球角色成员

`GET /v2/groups/{group_id}/role_members`

获取星球的星主、合伙人、管理员列表。

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "owner": {
      "user_id": 582884445452854,
      "name": "星主"
    },
    "partners": [],
    "admins": []
  }
}
```

---

### 1.6 获取星球统计数据

`GET /v2/groups/{group_id}/statistics`

获取星球的统计数据。

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "statistics": {
      "topics_count": 1234,
      "members_count": 567,
      "questions_count": 89,
      "answers_count": 76
    }
  }
}
```

---

### 1.7 获取星球成员详情

`GET /v2/groups/{group_id}/members/{member_id}`

获取指定星球成员的详细信息。

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| `group_id` | number | 星球 ID |
| `member_id` | number | 成员 ID |

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "member": {
      "user_id": 51118424254414,
      "name": "用户名",
      "avatar_url": "https://images.zsxq.com/...",
      "role": "member",
      "joined_time": "2024-01-15T10:30:00.000+0800"
    }
  }
}
```

---

### 1.8 获取成员活跃摘要

`GET /v2/groups/{group_id}/members/{member_id}/summary`

获取成员在星球的活跃数据摘要。

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "summary": {
      "topics_count": 15,
      "comments_count": 42,
      "likes_received": 128
    }
  }
}
```

---

### 1.9 获取星球专栏列表

`GET /v2/groups/{group_id}/columns`

获取星球的专栏列表。

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "columns": [
      {
        "column_id": 123456789,
        "name": "专栏名称",
        "topics_count": 20
      }
    ]
  }
}
```

---

### 1.10 获取专栏汇总信息

`GET /v2/groups/{group_id}/columns/summary`

获取星球专栏的汇总信息。

---

### 1.11 获取星球续费信息

`GET /v2/groups/{group_id}/renewal`

获取星球的续费配置信息。

---

### 1.12 获取星球分销信息

`GET /v2/groups/{group_id}/distribution`

获取星球的分销配置信息。

---

### 1.13 获取可升级星球列表

`GET /v2/groups/upgradable_groups`

获取当前用户可升级的星球列表。

---

### 1.14 获取推荐星球列表

`GET /v2/groups/recommendations`

获取系统推荐的星球列表。

---

### 1.15 获取未读话题数量

`GET /v2/groups/unread_topics_count`

获取所有已加入星球的未读话题数量。

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "groups": [
      {
        "group_id": 15555411412112,
        "unread_topics_count": 5
      }
    ]
  }
}
```

---

## 2. 话题管理

### 2.1 获取星球话题列表

`GET /v2/groups/{group_id}/topics`

获取指定星球的话题列表，支持分页和筛选。

**查询参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| `count` | number | 每页数量，默认 20 |
| `scope` | string | 范围：`all`(全部)、`digests`(精华)、`questions`(问答) |
| `begin_time` | string | 起始时间（ISO8601 格式） |
| `end_time` | string | 结束时间 |
| `with_invisibles` | boolean | 是否包含隐藏话题 |

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "topics": [
      {
        "topic_id": 82811825281411142,
        "topic_uid": "82811825281411142",
        "type": "talk",
        "group": {
          "group_id": 15555411412112,
          "name": "AI私域赚钱"
        },
        "talk": {
          "owner": {
            "user_id": 51118424254414,
            "name": "作者名称",
            "avatar_url": "https://images.zsxq.com/..."
          },
          "text": "话题内容...",
          "images": [],
          "files": []
        },
        "likes_count": 10,
        "comments_count": 5,
        "rewards_count": 2,
        "reading_count": 100,
        "digested": true,
        "create_time": "2025-12-08T10:30:00.000+0800"
      }
    ]
  }
}
```

**话题类型 (type)**:
- `talk` - 分享
- `question` - 提问
- `task` - 作业
- `solution` - 回答

---

### 2.2 获取专栏话题列表

`GET /v2/groups/{group_id}/columns/{column_id}/topics`

获取指定专栏的话题列表。

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| `group_id` | number | 星球 ID |
| `column_id` | number | 专栏 ID |

---

### 2.3 获取话题详情

`GET /v2/topics/{topic_id}`

获取话题的完整详情信息。

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "topic": {
      "topic_id": 5125585155255524,
      "topic_uid": "5125585155255524",
      "type": "talk",
      "group": {
        "group_id": 15555411412112,
        "name": "AI私域赚钱",
        "type": "pay"
      },
      "talk": {
        "owner": {
          "user_id": 582884445452854,
          "name": "深圳大冲",
          "avatar_url": "https://images.zsxq.com/..."
        },
        "text": "完整话题内容..."
      },
      "likes_count": 25,
      "comments_count": 12,
      "rewards_count": 3,
      "reading_count": 256,
      "create_time": "2025-12-07T15:20:00.000+0800"
    }
  }
}
```

---

### 2.4 获取话题基础信息

`GET /v2/topics/{topic_id}/info`

获取话题的基础信息（轻量版，不含评论详情）。

---

### 2.5 获取话题评论列表

`GET /v2/topics/{topic_id}/comments`

获取话题的评论列表。

**查询参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| `count` | number | 每页数量 |
| `sort` | string | 排序：`asc`(正序)、`desc`(倒序) |
| `sort_type` | string | 排序类型：`by_create_time` |
| `with_sticky` | boolean | 是否包含置顶评论 |

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "comments": [
      {
        "comment_id": 123456789,
        "owner": {
          "user_id": 51118424254414,
          "name": "评论者",
          "avatar_url": "https://images.zsxq.com/..."
        },
        "text": "评论内容",
        "likes_count": 5,
        "create_time": "2025-12-08T11:00:00.000+0800",
        "sticky": false,
        "repliee": null
      }
    ],
    "index": ""
  }
}
```

---

### 2.6 获取话题打赏列表

`GET /v2/topics/{topic_id}/rewards`

获取话题的打赏记录列表。

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "rewards": [
      {
        "user": {
          "user_id": 51118424254414,
          "name": "打赏者"
        },
        "amount": 10,
        "create_time": "2025-12-08T12:00:00.000+0800"
      }
    ]
  }
}
```

---

### 2.7 获取相关推荐话题

`GET /v2/topics/{topic_id}/recommendations`

获取与当前话题相关的推荐话题。

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "topics": []
  }
}
```

---

## 3. 标签系统

### 3.1 获取标签话题列表

`GET /v2/hashtags/{hashtag_id}/topics`

获取指定标签下的话题列表。

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| `hashtag_id` | number | 标签 ID |

**查询参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| `count` | number | 每页数量 |

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "topics": [
      {
        "topic_id": 82811825281411142,
        "topic_uid": "82811825281411142",
        "group": {
          "group_id": 15555411412112,
          "name": "AI私域赚钱",
          "type": "pay"
        },
        "type": "talk",
        "talk": {
          "owner": {
            "user_id": 51118424254414,
            "name": "作者"
          },
          "text": "话题内容..."
        },
        "create_time": "2025-12-08T10:00:00.000+0800"
      }
    ]
  }
}
```

---

## 4. 打卡系统

### 4.1 获取打卡项目列表

`GET /v2/groups/{group_id}/checkins`

获取星球的打卡项目列表。

**查询参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| `scope` | string | 范围：`all`(全部)、`ongoing`(进行中)、`ended`(已结束) |

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "checkins": [
      {
        "checkin_id": 123456789,
        "name": "21天打卡挑战",
        "description": "每天坚持学习",
        "status": "ongoing",
        "joined_users_count": 50,
        "create_time": "2025-12-01T00:00:00.000+0800",
        "end_time": "2025-12-21T23:59:59.000+0800"
      }
    ]
  }
}
```

---

### 4.2 获取打卡项目详情

`GET /v2/groups/{group_id}/checkins/{checkin_id}`

获取指定打卡项目的详细信息。

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "checkin": {
      "checkin_id": 123456789,
      "name": "21天打卡挑战",
      "description": "每天坚持学习",
      "status": "ongoing",
      "joined_users_count": 50,
      "rules": "打卡规则说明..."
    }
  }
}
```

---

### 4.3 获取打卡项目统计

`GET /v2/groups/{group_id}/checkins/{checkin_id}/statistics`

获取打卡项目的统计数据。

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "statistics": {
      "total_checkin_count": 500,
      "today_checkin_count": 30,
      "joined_users_count": 50
    }
  }
}
```

---

### 4.4 获取打卡每日统计

`GET /v2/groups/{group_id}/checkins/{checkin_id}/statistics/daily`

获取打卡项目的每日统计数据。

---

### 4.5 获取打卡话题列表

`GET /v2/groups/{group_id}/checkins/{checkin_id}/topics`

获取打卡项目的话题列表（打卡内容）。

**查询参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| `count` | number | 每页数量 |

---

### 4.6 获取打卡排行榜

`GET /v2/groups/{group_id}/checkins/{checkin_id}/ranking_list`

获取打卡项目的排行榜。

**查询参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| `count` | number | 返回数量 |

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "ranking_list": [
      {
        "rank": 1,
        "user": {
          "user_id": 51118424254414,
          "name": "用户名",
          "avatar_url": "https://images.zsxq.com/..."
        },
        "checkin_count": 21,
        "continuous_days": 21
      }
    ]
  }
}
```

---

### 4.7 获取打卡参与用户

`GET /v2/groups/{group_id}/checkins/{checkin_id}/joined_users`

获取打卡项目的参与用户列表。

---

### 4.8 创建打卡项目

`POST /v2/groups/{group_id}/checkins`

创建新的打卡项目（星主/管理员权限）。

**请求体**:
```json
{
  "checkin": {
    "name": "打卡项目名称",
    "description": "项目说明",
    "end_time": "2025-12-31T23:59:59.000+0800",
    "rules": "打卡规则"
  }
}
```

---

### 4.9 更新打卡项目

`PUT /v2/groups/{group_id}/checkins/{checkin_id}`

更新打卡项目信息（星主/管理员权限）。

---

### 4.10 获取我的打卡记录

`GET /v2/users/self/groups/{group_id}/checkins/{checkin_id}/topics`

获取当前用户在打卡项目的打卡记录。

---

### 4.11 获取我的打卡日期

`GET /v2/users/self/groups/{group_id}/checkins/{checkin_id}/checkined_dates`

获取当前用户的打卡日期列表。

---

### 4.12 获取我的打卡统计

`GET /v2/users/self/groups/{group_id}/checkins/{checkin_id}/statistics`

获取当前用户在打卡项目的统计数据。

---

## 5. 排行榜系统

### 5.1 获取星球排行榜

`GET /v3/groups/ranking_list`

获取星球排行榜列表。

**查询参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| `count` | number | 返回数量 |
| `type` | string | 排行类型 |

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "ranking_list": [
      {
        "rank": 1,
        "group": {
          "group_id": 15555411412112,
          "name": "AI私域赚钱"
        },
        "score": 9999
      }
    ]
  }
}
```

---

### 5.2 获取星球排行统计

`GET /v3/groups/{group_id}/ranking_list/statistics`

获取星球在排行榜的统计数据。

---

### 5.3 获取积分排行榜

`GET /v2/dashboard/groups/{group_id}/scoreboard/ranking_list`

获取星球的积分排行榜。

---

### 5.4 获取我的积分统计

`GET /v2/dashboard/groups/{group_id}/scoreboard/statistics/self`

获取当前用户在星球的积分统计。

---

### 5.5 获取积分榜设置

`GET /v2/dashboard/groups/{group_id}/scoreboard/settings`

获取星球积分榜的配置设置。

---

## 6. 用户系统

### 6.1 获取当前用户信息

`GET /v3/users/self`

获取当前登录用户的详细信息。

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "user": {
      "user_id": 51118424254414,
      "name": "用户名",
      "alias": "昵称",
      "avatar_url": "https://images.zsxq.com/...",
      "description": "个人简介",
      "location": "深圳",
      "industry": "互联网"
    }
  }
}
```

---

### 6.2 获取指定用户信息

`GET /v3/users/{user_id}`

获取指定用户的详细信息。

---

### 6.3 获取用户统计数据

`GET /v3/users/{user_id}/statistics`

获取用户的统计数据。

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "statistics": {
      "topics_count": 100,
      "comments_count": 500,
      "likes_count": 1200,
      "followers_count": 50
    }
  }
}
```

---

### 6.4 获取用户头像URL

`GET /v3/users/{user_id}/avatar_url`

获取用户的头像 URL。

---

### 6.5 获取用户动态足迹

`GET /v2/users/{user_id}/footprints`

获取用户的动态足迹列表。

---

### 6.6 获取用户星球足迹

`GET /v2/users/{user_id}/footprints/groups`

获取用户的星球足迹列表。

---

### 6.7 获取用户创建的星球

`GET /v2/users/{user_id}/created_groups`

获取用户创建的星球列表。

---

### 6.8 获取申请中的星球

`GET /v2/users/self/groups/applied_groups`

获取当前用户申请中的星球列表。

---

### 6.9 获取邀请人信息

`GET /v2/users/self/groups/{group_id}/inviter`

获取邀请当前用户加入星球的邀请人信息。

---

### 6.10 获取我的优惠券

`GET /v2/users/self/merchant_coupons`

获取当前用户的优惠券列表。

---

### 6.11 获取我的备注列表

`GET /v3/users/self/remarks`

获取当前用户的备注列表。

---

### 6.12 获取推荐关注用户

`GET /v2/users/self/recommendations/users`

获取系统推荐关注的用户列表。

---

## 7. 数据面板

### 7.1 获取星球数据概览

`GET /v2/dashboard/groups/{group_id}/overview`

获取星球的数据概览（星主专用）。

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "overview": {
      "members_count": 1234,
      "new_members_count": 50,
      "topics_count": 567,
      "new_topics_count": 20,
      "comments_count": 2345,
      "active_members_count": 300
    }
  }
}
```

---

### 7.2 获取星球收入概览

`GET /v2/dashboard/groups/{group_id}/incomes/overview`

获取星球的收入概览（星主专用）。

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "overview": {
      "total_income": 50000,
      "month_income": 5000,
      "today_income": 200
    }
  }
}
```

---

### 7.3 获取星球权限配置

`GET /v2/dashboard/groups/{group_id}/privileges`

获取星球的权限配置信息。

---

## 8. 其他接口

### 8.1 获取PK群组详情

`GET /v2/pk_groups/{pk_group_id}`

获取 PK 群组的详细信息。

**响应示例**:
```json
{
  "succeeded": true,
  "resp_data": {
    "group": {
      "group_id": 15555411412112,
      "name": "AI私域赚钱",
      "background_url": "https://images.zsxq.com/...",
      "power": 7825,
      "defensive_success_count": 10,
      "previous_ranking_power": 7500,
      "user_specific": {
        "is_privileged_member": true
      }
    }
  }
}
```

---

### 8.2 获取PK对战记录

`GET /v2/pk_groups/{pk_group_id}/records`

获取 PK 群组的对战记录列表。

**查询参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| `count` | number | 返回数量 |

---

### 8.3 解析URL详情

`GET /v2/url_details`

解析 URL 获取详情信息。

---

### 8.4 获取菜单阅读时间

`GET /v2/groups/{group_id}/menus/last_read_time`

获取星球菜单的最后阅读时间。

---

### 8.5 更新菜单阅读时间

`PUT /v2/groups/{group_id}/menus/last_read_time`

更新星球菜单的最后阅读时间。

---

## 第三方服务接口

以下接口为知识星球 APP 使用的第三方服务，非核心业务 API：

| 接口 | 服务商 | 说明 |
|------|--------|------|
| `POST /sa` | 神策 | 用户行为数据上报 |
| `POST /rqd/sync` | 腾讯 | 错误日志上报 |
| `POST /api/*/envelope/` | Sentry | 性能监控数据上报 |
| `POST /secret/user/` | 极光 | 推送服务 |
| `POST /v3/report` | 极光 | 推送统计上报 |

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 10001 | Token 无效 |
| 10002 | Token 已过期 |
| 10003 | 签名验证失败 |
| 20001 | 权限不足 |
| 20002 | 非星球成员 |
| 20003 | 非星主 |
| 30001 | 星球不存在 |
| 30002 | 话题不存在 |
| 40001 | 请求过于频繁 |
| 52010 | 未参与打卡项目 |

---

## 统计信息

| 项目 | 数量 |
|------|------|
| 原始接口总数 | 188 |
| 去重后接口数 | 约 50 |
| 核心业务接口 | 约 45 |
| 第三方服务接口 | 约 5 |

**主要去重内容**:
- `/v2/hashtags/{hashtag_id}/topics` - 原 24 条合并为 1 条
- `/v2/groups/{group_id}/topics` - 原 8 条合并为 1 条
- `/v2/groups/{group_id}/checkins` - 原 4 条合并为 1 条
- `/v3/groups/ranking_list` - 原 4 条合并为 1 条
- `/v2/groups/{group_id}/checkins/{checkin_id}/ranking_list` - 原 4 条合并为 1 条
- `/rqd/sync` - 原 6 条合并为 1 条
