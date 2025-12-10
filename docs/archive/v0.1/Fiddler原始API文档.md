# 知识星球 API 文档
**生成时间**: 2025-12-09 16:56:15
**接口总数**: 188
**数据来源**: Fiddler Everywhere 抓包分析
---
## 📚 目录

### 第一部分：知识星球原生 API（抓包分析）

| 编号 | 模块 | 子模块 | 说明 |
|------|------|--------|------|
| 1.1 | [其他](#1-其他) | [未分类](#11-未分类) | 基础接口、话题标签等 |
| 2.1 | [内容系统](#2-内容系统) | [话题管理](#21-话题管理) | 星球话题、专栏、打卡话题 |
| 3.1 | [排行榜系统](#3-排行榜系统) | [星球排行](#31-星球排行) | 星球排行、打卡排行榜 |
| 4.1 | [数据分析](#4-数据分析) | [行为追踪](#41-行为追踪) | 用户行为上报 |
| 5.1 | [星球系统](#5-星球系统) | [升级管理](#51-升级管理) | 星球升级相关 |
| 5.2 | | [星球信息](#52-星球信息) | 星球详情、成员、菜单 |
| 5.3 | | [星球推荐](#53-星球推荐) | 推荐星球列表 |
| 5.4 | | [未读消息](#54-未读消息) | 未读消息数量 |
| 6.1 | [用户系统](#6-用户系统) | [优惠券](#61-优惠券) | 用户优惠券 |
| 6.2 | | [备注管理](#62-备注管理) | 用户备注 |
| 6.3 | | [推荐系统](#63-推荐系统) | 推荐内容 |
| 6.4 | | [用户信息](#64-用户信息) | 用户资料、动态 |
| 6.5 | | [用户星球管理](#65-用户星球管理) | 打卡记录、打卡项目 |
| 7.1 | [监控系统](#7-监控系统) | [性能监控](#71-性能监控) | 性能数据上报 |
| 7.2 | | [错误上报](#72-错误上报) | 错误日志上报 |
| 8.1 | [阅读追踪](#8-阅读追踪) | [阅读进度](#81-阅读进度) | 阅读记录 |

### 第二部分：SDK 封装接口设计

| 编号 | 模块 | 说明 |
|------|------|------|
| 2.1 | [认证接口](#21-认证接口) | 用户登录认证 |
| 2.2 | [用户接口](#22-用户接口) | 用户信息获取 |
| 2.3 | [星球接口](#23-星球接口) | 星球列表、详情、话题 |
| 2.4 | [话题接口](#24-话题接口) | 话题详情、评论 |
| 2.5 | [打卡项目接口](#25-打卡项目接口) | 打卡列表、统计、排行榜 |
| 2.6 | [训练营接口](#26-训练营接口) | 训练营管理 |
| 2.7 | [星主专用接口](#27-星主专用接口) | 成员管理、内容管理 |
| 2.8 | [API 映射表](#28-api-映射表) | SDK 与原生 API 对照 |
| 2.9 | [错误代码](#29-错误代码) | 错误码说明 |

---
## 🔐 认证机制
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
| `content-type` | 内容类型 | `application/json; charset=utf-8` |

---
## 🌐 基础 URL

```
https://api.zsxq.com
```

---

## 1. 其他

### 1.1 未分类

**接口数量**: 35

#### `GET` /v2/groups

**接口名称**: 获取用户星球列表

**功能说明**: 获取当前登录用户已加入的所有星球列表，包含星球基本信息、最近更新时间、成员角色等

**完整 URL**:
```
https://api.zsxq.com/v2/groups
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268187` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `dd7b51bee0c20ebf21c1c97f983febb6cc8ebc5b` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'groups': [{'group_id': 88885121521552, 'name': '易安AI编程·出海赚钱', 'privilege_user_last_topic_create_time': '2025-12-08T18:09:50.408+0800', 'background_url': 'https://images.zsxq.com/FprqWRYy8OAu6_Y4T4uOIQZSBbE6?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:NzzZ020dO7CxwhmzvThPB5NAskE=', 'type': 'free', 'risk_level': 'normal', 'partner_ids': [51544811224184, 51418248822184, 455451411288, 184444848828412, 184444148525542, 244418184485821], 'admin_ids': ...
```

**平均响应时间**: 227ms


---

#### `GET` /v2/pk_groups/{pk_group_id}

**接口名称**: 获取PK群组详情

**功能说明**: 获取指定PK群组的详细信息，包括群组名称、背景图、战力值、防守成功次数等

**完整 URL**:
```
https://api.zsxq.com/v2/pk_groups/15555411412112
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268191` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `5bb2bd7c29c81228dffdfa8437e231ffd317e3f7` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "group": {
      "group_id": 15555411412112,
      "name": "AI私域赚钱",
      "background_url": "https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8=",
      "power": 0,
      "defensive_success_count": 0,
      "previous_ranking_power": 7825,
      "user_specific": {
        "is_privileged_member": true
      }
    }
  }
}
```

**平均响应时间**: 73ms


---

#### `GET` /v2/pk_groups/{pk_group_id}/records

**接口名称**: 获取PK群组对战记录

**功能说明**: 获取指定PK群组的对战记录列表，包括攻击方、防守方、对战结果等

**完整 URL**:
```
https://api.zsxq.com/v2/pk_groups/15555411412112/records?count=1
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `1` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268192` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `3b3707395f5335c139d90d651993f7f47b85232e` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "groups": []
  }
}
```

**平均响应时间**: 59ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/28844882581111/topics?count=1
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `1` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268192` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `02cc335a167e741d72f36e3b5d4724838d520a63` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 82811825281411142, 'topic_uid': '82811825281411142', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 51118424254414, 'name': '岸芷汀兰', 'avatar_url': 'https://images.zsxq.com/Ftpe2Fl5Ubay2gVmaH9Lmg1AUc3j?imageMog...
```

**平均响应时间**: 179ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/15514284488142/topics?count=1
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `1` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268192` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `bd15c4f370e46b3cb8b49ecd1067eeaa249f11c9` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 82811825841211442, 'topic_uid': '82811825841211442', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 582884445452854, 'name': '深圳大冲', 'avatar_url': 'https://images.zsxq.com/FvMsMu9H2_vt7RZ3ZmeiSRAE-5Zk?imageMo...
```

**平均响应时间**: 193ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/51144121845184/topics?count=1
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `1` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268193` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `d4143b2d0e4a3a9a8e38b851c48f6cd9e6a95d6b` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 45811854885441458, 'topic_uid': '45811854885441458', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 8442455112, 'name': '英男', 'avatar_url': 'https://images.zsxq.com/FiCL-2eyOJd-lJbDxefn8OJSX4m3?imageMogr2/aut...
```

**平均响应时间**: 117ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/51145125424814/topics?count=1
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `1` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268193` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `71fd2d3afed8ebcc9b74e0d4a2b531252d4f6298` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 22811851848118141, 'topic_uid': '22811851848118141', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 812858181514282, 'name': '平老板', 'avatar_url': 'https://images.zsxq.com/FnvL5BJE_ZSAVyPVlPr_hvOSgXY7?imageMog...
```

**平均响应时间**: 227ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/15425825555222/topics?count=1
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `1` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268193` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `614d2829d571af3a02f67b5a0f4de57559c894f7` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 55188118821885814, 'topic_uid': '55188118821885814', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 585582848845524, 'name': '张铖', 'avatar_url': 'https://images.zsxq.com/FsEA3CRumbupAAbz55eP77brOqWQ?imageMogr...
```

**平均响应时间**: 168ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/51521485524884/topics?count=1
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `1` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268193` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `a255d395500462bdface704253b9e91bca1d9c74` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 55188118845855824, 'topic_uid': '55188118845855824', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'task', 'task': {'owner': {'user_id': 582884445452854, 'name': '深圳大冲', 'avatar_url': 'https://images.zsxq.com/FvMsMu9H2_vt7RZ3ZmeiSRAE-5Zk?imageMo...
```

**平均响应时间**: 131ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/51184585284414/topics?count=1
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `1` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268193` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `6171a2b2048321d24f0d2f683764d8ff0ca5ff66` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 14588542541554542, 'topic_uid': '14588542541554542', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 241888888148421, 'name': '无岸', 'avatar_url': 'https://images.zsxq.com/FvseKqtKMNLtLE1fmRYExEOsrlOY?imageMogr...
```

**平均响应时间**: 104ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/28812242884811/topics?count=1
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `1` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268193` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `60f99fa09047cb32e5b301730e4c578356e4b42c` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 14588542545411422, 'topic_uid': '14588542545411422', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 585125485845184, 'name': '维度哥', 'avatar_url': 'https://images.zsxq.com/Fp2MzGf6um9-v-8EbZ4sXvdjgLr8?imageMog...
```

**平均响应时间**: 95ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/48818152154548/topics?count=1
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `1` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268194` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `4b3ee452641a1aae8656524bd65230f425b42ae9` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 45811414222482148, 'topic_uid': '45811414222482148', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 582884445452854, 'name': '深圳大冲', 'avatar_url': 'https://images.zsxq.com/FvMsMu9H2_vt7RZ3ZmeiSRAE-5Zk?imageMo...
```

**平均响应时间**: 278ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/48548284252828/topics?count=1
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `1` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268194` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `4cd3ca325509d6ebaef6d0e97dbfa5859a2dd197` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 14588552155851222, 'topic_uid': '14588552155851222', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 582885851288184, 'name': '正能量者', 'avatar_url': 'https://images.zsxq.com/FiguBZnZfRFRxu-8yGeyO6-d7s9S?imageMo...
```

**平均响应时间**: 285ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/88258485424282/topics?count=1
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `1` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268194` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `3d5e79f2e7aac42da26d31476849e7d4090b53fa` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 55188124185152254, 'topic_uid': '55188124185152254', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 815815145851582, 'name': '三横王者', 'avatar_url': 'https://images.zsxq.com/FqhLCVwfN-BhVm_oKRLVJu8QfKOX?imageMo...
```

**平均响应时间**: 185ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/51521412454144/topics?count=1
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `1` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268195` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `5e7233a6c0a18f7d6c54c25408618aaac2c09da0` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 82811885454852552, 'topic_uid': '82811885454852552', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 181281551422882, 'name': '顾北', 'avatar_url': 'https://images.zsxq.com/FkZPZx9n5upu_sS96uE2dc97Z39m?imageMogr...
```

**平均响应时间**: 177ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/28844882581111/topics?count=10
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `10` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268214` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `fa111ef75dee8eb97bbc0b0895e46b70e3d632b9` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 82811825281411142, 'topic_uid': '82811825281411142', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 51118424254414, 'name': '岸芷汀兰', 'avatar_url': 'https://images.zsxq.com/Ftpe2Fl5Ubay2gVmaH9Lmg1AUc3j?imageMog...
```

**平均响应时间**: 282ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/15514284488142/topics?count=10
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `10` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268216` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `03f128974b175ca23a4b0ec120893a38bb0f13a5` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 82811825841211442, 'topic_uid': '82811825841211442', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 582884445452854, 'name': '深圳大冲', 'avatar_url': 'https://images.zsxq.com/FvMsMu9H2_vt7RZ3ZmeiSRAE-5Zk?imageMo...
```

**平均响应时间**: 190ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/51144121845184/topics?count=10
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `10` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268218` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `06ad526fdd744f9251b2c31ef81bdeea1886d3a0` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 45811854885441458, 'topic_uid': '45811854885441458', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 8442455112, 'name': '英男', 'avatar_url': 'https://images.zsxq.com/FiCL-2eyOJd-lJbDxefn8OJSX4m3?imageMogr2/aut...
```

**平均响应时间**: 161ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/51145125424814/topics?count=10
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `10` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268219` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `d7cf0a3d898cd4ee1a41b5ac5efe67d76dc774f2` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 22811851848118141, 'topic_uid': '22811851848118141', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 812858181514282, 'name': '平老板', 'avatar_url': 'https://images.zsxq.com/FnvL5BJE_ZSAVyPVlPr_hvOSgXY7?imageMog...
```

**平均响应时间**: 195ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/15425825555222/topics?count=10
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `10` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268220` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `7f657d418923696035f3b7bd1d9c031f76a20535` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 55188118821885814, 'topic_uid': '55188118821885814', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 585582848845524, 'name': '张铖', 'avatar_url': 'https://images.zsxq.com/FsEA3CRumbupAAbz55eP77brOqWQ?imageMogr...
```

**平均响应时间**: 306ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/51521485524884/topics?count=10
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `10` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268221` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `b97d690fbab5aa410a53288936e9754f3f80dad5` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 55188118845855824, 'topic_uid': '55188118845855824', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'task', 'task': {'owner': {'user_id': 582884445452854, 'name': '深圳大冲', 'avatar_url': 'https://images.zsxq.com/FvMsMu9H2_vt7RZ3ZmeiSRAE-5Zk?imageMo...
```

**平均响应时间**: 131ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/88258185254482/topics?count=10
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `10` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268222` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `b65562cef60e2ac0e22ea9e3fb2edb49fe134815` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 45811414222482148, 'topic_uid': '45811414222482148', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 582884445452854, 'name': '深圳大冲', 'avatar_url': 'https://images.zsxq.com/FvMsMu9H2_vt7RZ3ZmeiSRAE-5Zk?imageMo...
```

**平均响应时间**: 121ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/51184585284414/topics?count=10
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `10` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268223` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `7fcf6c9f57ca825bce044f9f3b3f279b78317cb7` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 14588542541554542, 'topic_uid': '14588542541554542', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 241888888148421, 'name': '无岸', 'avatar_url': 'https://images.zsxq.com/FvseKqtKMNLtLE1fmRYExEOsrlOY?imageMogr...
```

**平均响应时间**: 190ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/28812242884811/topics?count=10
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `10` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268224` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `06ff22a750c197bb4f809f2901b55e85fa7aa71e` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 14588542545411422, 'topic_uid': '14588542545411422', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 585125485845184, 'name': '维度哥', 'avatar_url': 'https://images.zsxq.com/Fp2MzGf6um9-v-8EbZ4sXvdjgLr8?imageMog...
```

**平均响应时间**: 300ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/88285218512422/topics?count=10
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `10` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268227` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `2d7f414f9097091718b995233f1c37acb76c3c9e` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 45811881252542118, 'topic_uid': '45811881252542118', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 582884445452854, 'name': '深圳大冲', 'avatar_url': 'https://images.zsxq.com/FvMsMu9H2_vt7RZ3ZmeiSRAE-5Zk?imageMo...
```

**平均响应时间**: 203ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/88258185254482/topics?count=1
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `1` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268236` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `73918f8ba3cf8824f40dfc7a9ae64b9b4ae6eea4` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 45811414222482148, 'topic_uid': '45811414222482148', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 582884445452854, 'name': '深圳大冲', 'avatar_url': 'https://images.zsxq.com/FvMsMu9H2_vt7RZ3ZmeiSRAE-5Zk?imageMo...
```

**平均响应时间**: 189ms


---

#### `GET` /v2/hashtags/{hashtag_id}/topics

**接口名称**: 获取标签下的话题列表

**功能说明**: 获取指定标签下的话题列表，支持分页查询和按时间排序

**完整 URL**:
```
https://api.zsxq.com/v2/hashtags/88285218512422/topics?count=1
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `1` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268237` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `7c7378ace4067670b9720ab54692a68e00519106` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 45811881252542118, 'topic_uid': '45811881252542118', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 582884445452854, 'name': '深圳大冲', 'avatar_url': 'https://images.zsxq.com/FvMsMu9H2_vt7RZ3ZmeiSRAE-5Zk?imageMo...
```

**平均响应时间**: 119ms


---

#### `GET` /v2/url_details

**接口名称**: 获取URL详情

**功能说明**: 解析指定URL的详细信息，获取链接的元数据（标题、描述、图标等）

**完整 URL**:
```
https://api.zsxq.com/v2/url_details?url=https%3A%2F%2Fwx.zsxq.com%2Fmweb%2Fviews%2Foperational%2Fgroup_data.html%3Fgroup_id%3D15555411412112
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `url` | `https://wx.zsxq.com/mweb/views/operational/group_data.html?group_id=15555411412112` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268240` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `eda8519004013cd44693045606169bb078d90619` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "type": "unknown"
  }
}
```

**平均响应时间**: 62ms


---

#### `GET` /v2/topics/{topic_id}/info

**接口名称**: 获取话题基础信息

**功能说明**: 获取指定话题的基础信息，不包含评论和点赞详情

**完整 URL**:
```
https://api.zsxq.com/v2/topics/5125585155255524/info
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268255` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `e35d3f17f97f5a4ca1f6cf23eb134481535ca899` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topic': {'topic_id': 5125585155255524, 'topic_uid': '5125585155255524', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 582884445452854, 'name': '深圳大冲', 'avatar_url': 'https://images.zsxq.com/FvMsMu9H2_vt7RZ3ZmeiSRAE-5Zk?imageMogr2/...
```

**平均响应时间**: 135ms


---

#### `GET` /v2/topics/{topic_id}

**接口名称**: 获取话题详情

**功能说明**: 获取指定话题的完整详情，包括话题内容、作者信息、点赞数、评论数等

**完整 URL**:
```
https://api.zsxq.com/v2/topics/5125585155255524
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268255` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `033fa29cda86bd6389ad4aa305f399992ba04349` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topic': {'topic_id': 5125585155255524, 'topic_uid': '5125585155255524', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 582884445452854, 'name': '深圳大冲', 'avatar_url': 'https://images.zsxq.com/FvMsMu9H2_vt7RZ3ZmeiSRAE-5Zk?imageMogr2/...
```

**平均响应时间**: 176ms


---

#### `GET` /v2/topics/{topic_id}/recommendations

**接口名称**: 获取话题推荐列表

**功能说明**: 获取与指定话题相关的推荐话题列表

**完整 URL**:
```
https://api.zsxq.com/v2/topics/5125585155255524/recommendations
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268255` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `fe4db4dca56a88783a62472d71a770f96f1fe796` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "topics": []
  }
}
```

**平均响应时间**: 84ms


---

#### `GET` /v2/topics/{topic_id}/comments

**接口名称**: 获取话题评论列表

**功能说明**: 获取指定话题的评论列表，支持分页查询和排序

**完整 URL**:
```
https://api.zsxq.com/v2/topics/5125585155255524/comments?with_sticky=true&sort=asc&count=30&sort_type=by_create_time
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `with_sticky` | `true` |  |
| `sort` | `asc` |  |
| `count` | `30` |  |
| `sort_type` | `by_create_time` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268255` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `fc6973485a1f57be2596fdda68cb71a7e11afc17` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "comments": [],
    "index": ""
  }
}
```

**平均响应时间**: 109ms


---

#### `GET` /v2/topics/{topic_id}/rewards

**接口名称**: 获取话题打赏列表

**功能说明**: 获取指定话题的打赏记录列表

**完整 URL**:
```
https://api.zsxq.com/v2/topics/5125585155255524/rewards
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268255` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `69982f3990be18ae5cbcf99cc363d116593b3f65` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "rewards": []
  }
}
```

**平均响应时间**: 61ms


---

#### `POST` /secret/user/

**接口名称**: 用户密钥相关操作

**功能说明**: 与用户密钥或加密相关的操作接口

**完整 URL**:
```
https://user.jpush.cn/secret/user/
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-jg-version` | `1.0` |
| `authorization` | `Basic ZmYz...RxWTJhYz0=` |
| `x-jg-timestamp` | `1765268316931` |
| `x-jg-sign` | `ede1236a1d661604cb32b75aa091ef422517de9ff9e1e6e393ef311e7d529487` |

**响应状态码**: `200`

**响应示例**:

```json
Badge API success
```

**平均响应时间**: 203ms


---

#### `POST` /v3/report

**接口名称**: 数据上报

**功能说明**: 上报用户行为数据或统计信息

**完整 URL**:
```
https://stats.jpush.cn/v3/report
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-app-key` | `ff39e7046256c8960074d921` |
| `authorization` | `Basic ODQy...Y2NTM1YQ==` |

**请求体**:

```json
��/�R�'����1�o�a�|R�����`R|�c[Wyd]�6�����?PL��Mh&�%�u�nu��pWMHX�X�<�<x)D�bT�Q�$O���} �g,L��1m�"�m*é�#����T����y�L��u�*ڔ��9;�yA^T�c��ղ�P�g��42�D��jf�d��3tG9�L.�]���n:xkZ�/��M��*�aR...
```

**响应状态码**: `200`

**响应示例**:

```json
{
  "code": 200,
  "desc": "success"
}
```

**平均响应时间**: 281ms


---


## 2. 内容系统

### 2.1 话题管理

**接口数量**: 13

#### `GET` /v2/groups/{group_id}/topics

**接口名称**: 获取星球话题列表

**功能说明**: 获取指定星球的话题列表，支持分页查询、时间范围筛选和多种排序方式

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/topics?count=1&begin_time=2025-12-04T01%3A47%3A34.978%2B0800&scope=digests&with_invisibles=false
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `1` |  |
| `begin_time` | `2025-12-04T01:47:34.978+0800` |  |
| `scope` | `digests` |  |
| `with_invisibles` | `false` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268192` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `51bfe9db53bcdd5567ecda73c0f842acf37f33f7` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 82811825841211442, 'topic_uid': '82811825841211442', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 582884445452854, 'name': '深圳大冲', 'avatar_url': 'https://images.zsxq.com/FvMsMu9H2_vt7RZ3ZmeiSRAE-5Zk?imageMo...
```

**平均响应时间**: 106ms


---

#### `GET` /v2/groups/{group_id}/topics

**接口名称**: 获取星球话题列表

**功能说明**: 获取指定星球的话题列表，支持分页查询、时间范围筛选和多种排序方式

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/topics?count=1&begin_time=2025-12-09T15%3A49%3A39.928%2B0800&scope=all&with_invisibles=false
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `1` |  |
| `begin_time` | `2025-12-09T15:49:39.928+0800` |  |
| `scope` | `all` |  |
| `with_invisibles` | `false` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268192` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `82eab577219b552230d75713f6eb36dcd876d29c` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "topics": []
  }
}
```

**平均响应时间**: 101ms


---

#### `GET` /v2/groups/{group_id}/topics

**接口名称**: 获取星球话题列表

**功能说明**: 获取指定星球的话题列表，支持分页查询、时间范围筛选和多种排序方式

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/topics?count=1&begin_time=2025-12-01T00%3A05%3A46.684%2B0800&scope=by_owner&with_invisibles=false
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `1` |  |
| `begin_time` | `2025-12-01T00:05:46.684+0800` |  |
| `scope` | `by_owner` |  |
| `with_invisibles` | `false` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268192` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `01d30e946834c184eb23522f0bb5862d6476635e` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 82811825841211442, 'topic_uid': '82811825841211442', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 582884445452854, 'name': '深圳大冲', 'avatar_url': 'https://images.zsxq.com/FvMsMu9H2_vt7RZ3ZmeiSRAE-5Zk?imageMo...
```

**平均响应时间**: 115ms


---

#### `GET` /v2/groups/{group_id}/topics

**接口名称**: 获取星球话题列表

**功能说明**: 获取指定星球的话题列表，支持分页查询、时间范围筛选和多种排序方式

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/topics?count=10&end_time=2025-12-08T19%3A33%3A19.110%2B0800&scope=all&direction=backward
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `10` |  |
| `end_time` | `2025-12-08T19:33:19.110+0800` |  |
| `scope` | `all` |  |
| `direction` | `backward` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268200` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `590c7d71ea83f76ea57e809d89083321d3f0028b` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 14588542541554542, 'topic_uid': '14588542541554542', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 241888888148421, 'name': '无岸', 'avatar_url': 'https://images.zsxq.com/FvseKqtKMNLtLE1fmRYExEOsrlOY?imageMogr...
```

**平均响应时间**: 311ms


---

#### `GET` /v2/groups/{group_id}/topics

**接口名称**: 获取星球话题列表

**功能说明**: 获取指定星球的话题列表，支持分页查询、时间范围筛选和多种排序方式

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/topics?count=10&scope=by_owner&direction=backward
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `10` |  |
| `scope` | `by_owner` |  |
| `direction` | `backward` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268211` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `ac1b8b8d4c97e42032c98cb216948bde97f95072` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 82811825841211442, 'topic_uid': '82811825841211442', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 582884445452854, 'name': '深圳大冲', 'avatar_url': 'https://images.zsxq.com/FvMsMu9H2_vt7RZ3ZmeiSRAE-5Zk?imageMo...
```

**平均响应时间**: 188ms


---

#### `GET` /v2/groups/{group_id}/topics

**接口名称**: 获取星球话题列表

**功能说明**: 获取指定星球的话题列表，支持分页查询、时间范围筛选和多种排序方式

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/topics?count=1&begin_time=2025-12-09T16%3A17%3A12.958%2B0800&scope=digests&with_invisibles=false
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `1` |  |
| `begin_time` | `2025-12-09T16:17:12.958+0800` |  |
| `scope` | `digests` |  |
| `with_invisibles` | `false` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268235` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `d9f927b5d6bec7ec82afc62fa98a78e61f9654c9` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "topics": []
  }
}
```

**平均响应时间**: 64ms


---

#### `GET` /v2/groups/{group_id}/topics

**接口名称**: 获取星球话题列表

**功能说明**: 获取指定星球的话题列表，支持分页查询、时间范围筛选和多种排序方式

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/topics?count=1&begin_time=2025-12-09T16%3A16%3A53.201%2B0800&scope=all&with_invisibles=false
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `1` |  |
| `begin_time` | `2025-12-09T16:16:53.201+0800` |  |
| `scope` | `all` |  |
| `with_invisibles` | `false` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268235` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `edd2aa8c166f725b2f978d52ad1ec742d433b338` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "topics": []
  }
}
```

**平均响应时间**: 90ms


---

#### `GET` /v2/groups/{group_id}/topics

**接口名称**: 获取星球话题列表

**功能说明**: 获取指定星球的话题列表，支持分页查询、时间范围筛选和多种排序方式

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/topics?count=1&begin_time=2025-12-09T16%3A16%3A54.004%2B0800&scope=by_owner&with_invisibles=false
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `1` |  |
| `begin_time` | `2025-12-09T16:16:54.004+0800` |  |
| `scope` | `by_owner` |  |
| `with_invisibles` | `false` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268235` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `59cc0732675926179c9e35fa3f18be67296383ce` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "topics": []
  }
}
```

**平均响应时间**: 68ms


---

#### `GET` /v2/groups/{group_id}/columns/{column_id}/topics

**接口名称**: 获取专栏话题列表

**功能说明**: 获取指定星球下特定专栏的话题列表，支持分页查询

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/columns/518528184554/topics?count=100
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `100` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268248` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `b4048bf6ab6404dac6eceba841f8ee78d7ab5ff5` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 5125585155255524, 'topic_uid': '5125585155255524', 'title': '✅ 快闪项目汇总', 'text': '你好呀，项目全部分享给你，快闪项目汇总：\n\n项目介绍：❤️项目介绍：什么是闲鱼无货源...\n\n已经在星球配套群的，搜索：', 'create_time': '2025-05-02T10:42:13.963+0800', 'attached_to_column_time': '2025-07-21T11:36:00.553+0800'}, {'topic_id': 1524188582228482, 'topic_uid': '1524188582228482', 'title': '新手如何制作一张属于自己的海报', 'text': '新手如何制作一张属于自己的海报\n\n大家应该发现，我们星球每次活动的海报，都是蛮不错的，学会制作海报，是一个副业入门的必备', 'create_time': '2025-...
```

**平均响应时间**: 84ms


---

#### `GET` /v2/groups/{group_id}/columns/{column_id}/topics

**接口名称**: 获取专栏话题列表

**功能说明**: 获取指定星球下特定专栏的话题列表，支持分页查询

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/columns/518528184554/topics?count=20
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `20` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268248` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `57b58257f264b034f31a0911542d1ab872e10c3e` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 5125585155255524, 'topic_uid': '5125585155255524', 'title': '✅ 快闪项目汇总', 'text': '你好呀，项目全部分享给你，快闪项目汇总：\n\n项目介绍：❤️项目介绍：什么是闲鱼无货源...\n\n已经在星球配套群的，搜索：', 'create_time': '2025-05-02T10:42:13.963+0800', 'attached_to_column_time': '2025-07-21T11:36:00.553+0800'}, {'topic_id': 1524188582228482, 'topic_uid': '1524188582228482', 'title': '新手如何制作一张属于自己的海报', 'text': '新手如何制作一张属于自己的海报\n\n大家应该发现，我们星球每次活动的海报，都是蛮不错的，学会制作海报，是一个副业入门的必备', 'create_time': '2025-...
```

**平均响应时间**: 82ms


---

#### `GET` /v2/groups/{group_id}/checkins/{checkin_id}/topics

**接口名称**: 获取打卡话题列表

**功能说明**: 获取指定星球下特定打卡活动的相关话题列表

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins/2424141521/topics?count=20
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `20` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268265` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `46511f2e23b86ab3aaa4c51eb4be22444e1e4709` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 45811854524448528, 'topic_uid': '45811854524448528', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 815114424224452, 'name': '深圳小江', 'avatar_url': 'https://images.zsxq.com/Frqh2a41m5bYxWU5TV1CEr-bA_8z?imageMo...
```

**平均响应时间**: 336ms


---

#### `GET` /v2/groups/{group_id}/checkins/{checkin_id}/topics

**接口名称**: 获取打卡话题列表

**功能说明**: 获取指定星球下特定打卡活动的相关话题列表

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins/5454855814/topics?count=20
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `20` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268272` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `41627bda2477ac9bc5e141c18321286af6612c64` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 45811884445588148, 'topic_uid': '45811884445588148', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 212544188448281, 'name': '大地', 'avatar_url': 'https://images.zsxq.com/FukP0hBPkZtgVMWzGu91cMMqhNtK?imageMogr...
```

**平均响应时间**: 312ms


---

#### `GET` /v2/groups/{group_id}/checkins/{checkin_id}/topics

**接口名称**: 获取打卡话题列表

**功能说明**: 获取指定星球下特定打卡活动的相关话题列表

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins/1141425812/topics?count=20
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `20` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268297` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `8cf5fa6394f5f7fe194d54095c59e9fea06c4b26` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'topics': [{'topic_id': 4842452528182518, 'topic_uid': '4842452528182518', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 548122428284524, 'name': 'Echo', 'avatar_url': 'https://images.zsxq.com/Fq60wZcJWxrSk7d6RNBV9-ZtCait?imageMogr...
```

**平均响应时间**: 251ms


---


## 3. 排行榜系统

### 3.1 星球排行

**接口数量**: 10

#### `GET` /v3/groups/ranking_list

**接口名称**: 获取星球排行榜

**功能说明**: 获取星球排行榜列表，支持按不同维度排名

**完整 URL**:
```
https://api.zsxq.com/v3/groups/ranking_list?type=group_fortune_list&count=3
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `type` | `group_fortune_list` |  |
| `count` | `3` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268187` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `ef33298c65cfba393d3550340858c3674b1f5285` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `3.9.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'period': {'begin_time': '2025-12-08T00:00:00.000+0800', 'end_time': '2025-12-14T23:59:59.999+0800'}, 'groups': [{'uid': '552521181154', 'name': '齐俊杰的粉丝群', 'background_url': 'https://images.zsxq.com/FkGWEV8Qr7CWKnz1ZNyPjeM5msxJ?e=1780368555&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:8KGtfECsI35F89WyxFUadmIRa6w=', 'description': '1，投资不是打板算卦的择时撞大运！而是应对之道！是资金和仓位管理！\n2，预测的胜率不超过60%！策略才能改变赔率！让你对的时候多赚，错的时候少赔。\n3，不分析个股！！不指导个股操作！！问个股勿扰\n4，所有回复都是个人观点不作为投资建议！\n5，团队证券基金...
```

**平均响应时间**: 76ms


---

#### `GET` /v3/groups/ranking_list

**接口名称**: 获取星球排行榜

**功能说明**: 获取星球排行榜列表，支持按不同维度排名

**完整 URL**:
```
https://api.zsxq.com/v3/groups/ranking_list?type=paid_group_active_list&count=3
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `type` | `paid_group_active_list` |  |
| `count` | `3` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268187` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `886397c777c1316d9059e92c6a1e3a7ef426db01` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `3.9.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'period': {'begin_time': '2025-12-09T10:00:00.000+0800', 'end_time': '2025-12-09T16:00:00.000+0800'}, 'groups': [{'uid': '48884542242158', 'name': '粥左罗•终身写作社群', 'background_url': 'https://images.zsxq.com/FjwWIl3s7hVspBBxR9tm0Sfw2QLM?e=1780368555&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:LIzuUxxq3HEGKNfdQSXkKD_ZDyY=', 'description': '原价 699，优惠价 529 元！！\n\n【👇点“点击展开”，看完整介绍】\n\n‼️为啥一定要跟粥老师学写作？\n\n粥左罗写作10年，教写作7年：\n2018.3创立公众号，110万粉，视频号20万\n2019.4 创立写作营，连开7年，超过10...
```

**平均响应时间**: 76ms


---

#### `GET` /v3/groups/ranking_list

**接口名称**: 获取星球排行榜

**功能说明**: 获取星球排行榜列表，支持按不同维度排名

**完整 URL**:
```
https://api.zsxq.com/v3/groups/ranking_list?type=new_star_list&count=3
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `type` | `new_star_list` |  |
| `count` | `3` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268187` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `7c47894b8d64c0bb3125b31419a73cf1ad891c2e` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `3.9.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'period': {'begin_time': '2025-12-01T00:00:00.000+0800', 'end_time': '2025-12-31T23:59:59.999+0800'}, 'groups': [{'uid': '48885522454248', 'name': '大耳哥讲酒旅', 'background_url': 'https://images.zsxq.com/Fn6AuZuF1DcJAlbtDUOJGDZu-5Rd?e=1780368555&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:pq9YrYA8wYNDRQwvlleyr0o8BRU=', 'description': '抖音头部酒旅团购MCN+服务商，视频号首批酒旅合作机构🎙️拥有抖音酒旅头部IP“大耳哥”，旗下签约学员400+🤵🏻专注抖音·视频号-酒店团购直播。致力于打造1万名具备“文化力”的酒旅主播🌇', 'create_time': '2025-11-30T16:16:...
```

**平均响应时间**: 112ms


---

#### `GET` /v3/groups/ranking_list

**接口名称**: 获取星球排行榜

**功能说明**: 获取星球排行榜列表，支持按不同维度排名

**完整 URL**:
```
https://api.zsxq.com/v3/groups/ranking_list?type=group_sales_list&count=3
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `type` | `group_sales_list` |  |
| `count` | `3` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268187` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `eed709564947e17de6ca3196fee1f28cd950bffa` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `3.9.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'period': {'begin_time': '2025-12-08T00:00:00.000+0800', 'end_time': '2025-12-14T23:59:59.999+0800'}, 'groups': [{'uid': '88885185281182', 'name': '粥左罗每日思考·365日更群', 'background_url': 'https://images.zsxq.com/Fk_wTIwyRYgma_hsbKm2VDlEF8KH?e=1780368555&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:PeLw3VW6AfqADGKMMrAAJexs9oI=', 'description': '【现在请勿付费加入】🌹【本期已到期】【12月10日社群升级迭代，新一期发售】请关注粥左罗直播间、朋友圈、公众号动态。想邀请朋友加入的，12.10 开始。升级后，定价 365 元，12.10 开放早鸟价。\n不是垂直社群\n不是单讲写作或自媒体或...
```

**平均响应时间**: 112ms


---

#### `GET` /v3/groups/{group_id}/ranking_list/statistics

**接口名称**: 获取星球排行榜统计

**功能说明**: 获取指定星球的排行榜统计信息

**完整 URL**:
```
https://api.zsxq.com/v3/groups/15555411412112/ranking_list/statistics
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268192` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `afe351f2da9989344f31a29ecb364002ce83078b` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `3.9.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "highest_ranking_type": "paid_group_active_list",
    "highest_ranking": 3
  }
}
```

**平均响应时间**: 57ms


---

#### `GET` /v2/dashboard/groups/{group_id}/scoreboard/ranking_list

**接口名称**: 获取星球数据面板积分排行榜

**功能说明**: 获取指定星球的数据面板积分排行榜，包含用户积分排名信息

**完整 URL**:
```
https://api.zsxq.com/v2/dashboard/groups/15555411412112/scoreboard/ranking_list?type=last_month&count=10
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `type` | `last_month` |  |
| `count` | `10` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268192` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `ca750e8d7cb10a35d1406daf8a9bfb61d1494d4e` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'period': {'begin_time': '2025-11-01T00:00:00.000+0800', 'end_time': '2025-11-30T23:59:59.999+0800'}, 'ranking_list': [{'rank': 1, 'member': {'user_id': 582884445452854, 'name': '深圳大冲', 'alias': '', 'avatar_url': 'https://images.zsxq.com/FvMsMu9H2_vt7RZ3ZmeiSRAE-5Zk?e=1780368681&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:z4MNjKataNTK4rxNa_WUgfXCrzY='}, 'score': 1198.9, 'archived_time': '2025-11-30T23:44:51.240+0800'}, {'rank': 2, 'member': {'user_id': 212212...
```

**平均响应时间**: 93ms


---

#### `GET` /v2/groups/{group_id}/checkins/{checkin_id}/ranking_list

**接口名称**: 获取打卡排行榜

**功能说明**: 获取指定打卡活动的用户排行榜，支持按打卡次数或积分排名

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins/5454855814/ranking_list?type=continuous&index=0
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `type` | `continuous` |  |
| `index` | `0` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268274` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `f140564796617558049573d7249f42ced696dafe` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'ranking_list': [{'user': {'user_id': 585581528521884, 'name': '爱玮', 'alias': '', 'avatar_url': 'https://images.zsxq.com/Fh6BmMzP8QWqtyfA1gStqUvnrw0E?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1x0/quality/75/ignore-error/1&e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:s0GfEh58IT1iq336TX-0SXfRcrY='}, 'rankings': 1, 'checkined_days': 10}, {'user': {'user_id': 48551442254588, 'name': 'Sherry', 'alias': '', 'avatar_url': 'https://images.zsxq...
```

**平均响应时间**: 74ms


---

#### `GET` /v2/groups/{group_id}/checkins/{checkin_id}/ranking_list

**接口名称**: 获取打卡排行榜

**功能说明**: 获取指定打卡活动的用户排行榜，支持按打卡次数或积分排名

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins/5454855814/ranking_list?type=accumulated&index=0
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `type` | `accumulated` |  |
| `index` | `0` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268274` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `3c3a2795353d2b078b64d6e30b1dfcce5c36aa8d` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'ranking_list': [{'user': {'user_id': 585581528521884, 'name': '爱玮', 'alias': '', 'avatar_url': 'https://images.zsxq.com/Fh6BmMzP8QWqtyfA1gStqUvnrw0E?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1x0/quality/75/ignore-error/1&e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:s0GfEh58IT1iq336TX-0SXfRcrY='}, 'rankings': 1, 'checkined_days': 10}, {'user': {'user_id': 48551442254588, 'name': 'Sherry', 'alias': '', 'avatar_url': 'https://images.zsxq...
```

**平均响应时间**: 110ms


---

#### `GET` /v2/groups/{group_id}/checkins/{checkin_id}/ranking_list

**接口名称**: 获取打卡排行榜

**功能说明**: 获取指定打卡活动的用户排行榜，支持按打卡次数或积分排名

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins/5454855814/ranking_list?type=continuous&index=21
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `type` | `continuous` |  |
| `index` | `21` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268275` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `8bd9e1fb66bcd8d42307f0ac18b30f5e4d1120ea` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'ranking_list': [{'user': {'user_id': 184558881148412, 'name': '涤生', 'alias': '', 'avatar_url': 'https://images.zsxq.com/Fu7QOlljnMcKoxfZA3BnPqtFwplu?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1x0/quality/75/ignore-error/1&e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:YznUaw8pMpN7qeald7Ssp1hUZVc='}, 'rankings': 22, 'checkined_days': 8}, {'user': {'user_id': 212251422881241, 'name': '刘秀莲', 'alias': '', 'avatar_url': 'https://images.zsxq.c...
```

**平均响应时间**: 90ms


---

#### `GET` /v2/groups/{group_id}/checkins/{checkin_id}/ranking_list

**接口名称**: 获取打卡排行榜

**功能说明**: 获取指定打卡活动的用户排行榜，支持按打卡次数或积分排名

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins/5454855814/ranking_list?type=accumulated&index=21
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `type` | `accumulated` |  |
| `index` | `21` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268278` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `852bb4992c423fd4fcdf7a38a73c2ba74624f1aa` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'ranking_list': [{'user': {'user_id': 422125848454118, 'name': 'Max', 'alias': '', 'avatar_url': 'https://images.zsxq.com/FgWlaqGYdlSgsQ5HC-XUPQkvwyf9?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1x0/quality/75/ignore-error/1&e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:dGyL_8l6N7HqYyGA-bvXaXd_PKk='}, 'rankings': 22, 'checkined_days': 9}, {'user': {'user_id': 212544188448281, 'name': '大地', 'alias': '', 'avatar_url': 'https://images.zsxq.c...
```

**平均响应时间**: 73ms


---


## 4. 数据分析

### 4.1 行为追踪

**接口数量**: 1

#### `POST` /sa

**接口名称**: 数据埋点上报

**功能说明**: 神策数据埋点上报接口，用于用户行为追踪和数据分析

**完整 URL**:
```
https://sa.zsxq.com/sa?project=production
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `project` | `production` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `cookie` | `SERVER_ID=...c69e21811b` |

**响应状态码**: `200`

**平均响应时间**: 154ms


---


## 5. 星球系统

### 5.1 升级管理

**接口数量**: 1

#### `GET` /v2/groups/upgradable_groups

**接口名称**: 获取可升级星球列表

**功能说明**: 获取当前用户可以升级的星球列表

**完整 URL**:
```
https://api.zsxq.com/v2/groups/upgradable_groups
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268188` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `3cffc79fbf7f0c9cf6d28d2dbb2ad1aa430960da` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "group_ids": [],
    "guest_ids": []
  }
}
```

**平均响应时间**: 67ms


---

### 5.2 星球信息

**接口数量**: 31

#### `GET` /v2/groups/{group_id}

**接口名称**: 获取星球详情

**功能说明**: 获取指定星球的详细信息，包括名称、简介、成员数、话题数等

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268191` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `ec43e413080a517c6284ab05d9c81ee4975ca68c` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'group': {'group_id': 15555411412112, 'number': 50620566, 'name': 'AI私域赚钱', 'description': '领大额优惠券，请拖到下方，截图海报上的二维码，领取优惠券，或加大冲微信：95017333 或 330517251 领取。\n\n我是深圳大冲，在职场玩副业，项目型 IP 孵化教练，大冲十年退休发起人，大冲从副业小白，微信 0 好友， 一年内从私域运营起步，通过 1500 好友赚到 7 位数，一年带领 500 位副业小白通过 AI 私域赚钱变现，辅导大冲私教 360+ 人。\n\n这里分享大冲的私域运营经验，私域高客单成交秘籍，项目型 IP 打造经验。\n\n本星球免费解答项目型 IP 和 副业赚钱问题，免费参加副业小项目快闪实战，免费诊断个人 IP 账号，分享大冲副业百万实战经验，更新能落地，易理解，可商业化的案例，应用，风险，避坑。\n\n适合人群：\n1、副业刚开始起步的小白\n2、想打造个人IP的初学者\n3、想成为超级个体的探索者...
```

**平均响应时间**: 97ms


---

#### `GET` /v2/groups/{group_id}/hashtags

**接口名称**: 获取星球标签列表

**功能说明**: 获取指定星球的所有标签（话题分类）列表

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/hashtags
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268191` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `a44447339af1947f3aa038914000b76d411a41f7` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'hashtags': [{'hashtag_id': 15514284488142, 'title': '#小项目#', 'owner': {'user_id': 582884445452854}, 'properties': {'show_on_timeline': True, 'privileged': True}, 'topics_count': 1633}, {'hashtag_id': 28844882581111, 'title': '#复盘#', 'owner': {'user_id': 582884445452854}, 'properties': {'show_on_timeline': True, 'privileged': False}, 'topics_count': 1096}, {'hashtag_id': 51185581188214, 'title': '#积小胜#', 'owner': {'user_id': 582884445452854}, 'properties': {'sho...
```

**平均响应时间**: 72ms


---

#### `GET` /v2/groups/{group_id}/menus

**接口名称**: 获取星球菜单列表

**功能说明**: 获取指定星球的自定义菜单配置

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/menus?with_optional_menus=false
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `with_optional_menus` | `false` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268191` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `925944c97d992a68b561c42474709e36dd11f1fe` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'menus': [{'menu_id': 51285448425514, 'title': '精华', 'preset': True, 'preset_type': 'digests', 'latest_topic_create_time': '2025-12-08T23:30:09.201+0800'}, {'menu_id': 15284118124422, 'title': '最新', 'preset': True, 'preset_type': 'all'}, {'menu_id': 15284118124442, 'title': '只看星主', 'preset': True, 'preset_type': 'by_owner'}, {'menu_id': 28844882581111, 'title': '#复盘#', 'hashtag': {'hashtag_id': 28844882581111}}, {'menu_id': 15514284488142, 'title': '#小项目#', 'has...
```

**平均响应时间**: 127ms


---

#### `GET` /v2/groups/{group_id}/role_members

**接口名称**: 获取星球角色成员列表

**功能说明**: 获取指定星球的特定角色成员列表，如管理员、嘉宾等

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/role_members?role=owner,partners,guests
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `role` | `owner,partners,guests` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268191` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `dc49231a65829c680c8caf7ecfe58b26e2b53283` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'owner': {'group_id': 15555411412112, 'user_id': 582884445452854, 'name': '深圳大冲', 'number': 1, 'avatar_url': 'https://images.zsxq.com/FvMsMu9H2_vt7RZ3ZmeiSRAE-5Zk?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1x0/quality/75/ignore-error/1&e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:iTz-sF6KYHl60VBMT77uU4SLcUc=', 'description': 'AI私域赚钱星球主理人，项目型 IP 孵化教练，主业架构师、副业销冠操盘手、副业首年收入超百万，一人企业私域社群商业化探索者、大冲十年退休计划发起人。', 'join_time': '2024-09-03T15:17:27....
```

**平均响应时间**: 120ms


---

#### `GET` /v2/groups/{group_id}/renewal

**接口名称**: 获取星球续费信息

**功能说明**: 获取指定星球的续费相关信息，包括续费价格、到期时间等

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/renewal
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268192` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `d798c87a57d4bb3967601dd3f213f13e6e99de5c` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'amount': 4080, 'original_amount': 6800, 'discounted_percentage': 60, 'begin_time': '2286-11-21T01:46:39.999+0800', 'mode': 'period', 'duration': '1Y', 'guidance': '你好，我是深圳大冲，AI私域赚钱星球已经到了第 2 年，感谢支持。\n\nAI私域赚钱星球，一年以来累计举办小项目实战 60 次以上，很多人赚到了 4 位数以上，星球长期排在活跃榜前 3，畅销榜前 3，项目实战口碑也非常好，也得到了众多大佬的极力推荐。\n\n为了回报大家，我把续期价格全部下调，现在只要原价的 5 折，可能现在是咱们星球最便宜的一次了，8月份开始，星球价格会持续上调，并逐步取消续费折扣和优惠券。\n\n最后，再次感谢你的信任与陪伴，还有问题可加大冲微信：95017333 或 330517251 咨询。\n\n如果有无法续期的星友，麻烦关注【知识星球公众号】，在公众号内进行付款。'...
```

**平均响应时间**: 81ms


---

#### `GET` /v2/dashboard/groups/{group_id}/privileges

**接口名称**: 获取星球权限信息

**功能说明**: 获取当前用户在指定星球的管理权限信息

**完整 URL**:
```
https://api.zsxq.com/v2/dashboard/groups/15555411412112/privileges
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268192` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `28629cd938d8f31ffc30bed067d1e241bd848bfe` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "privileges": {
      "access_group_data": "owner,admins,partners",
      "access_incomes_data": "owner,partners",
      "access_weekly_reports": "owner,admins,partners",
      "access_accounts": "owner,partners"
    }
  }
}
```

**平均响应时间**: 65ms


---

#### `GET` /v2/dashboard/groups/{group_id}/scoreboard/settings

**接口名称**: 获取积分榜设置

**功能说明**: 获取指定星球的积分榜配置信息，包括积分规则等

**完整 URL**:
```
https://api.zsxq.com/v2/dashboard/groups/15555411412112/scoreboard/settings
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268192` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `edde48e8abd7da453ed4ba12ed4f872b42d70c9e` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "enabled_honor": true,
    "enabled": true,
    "rules_update_time": "2025-10-28T11:56:39.836+0800",
    "score_rules": {
      "read_topic": 0.2,
      "shared_group": 0.5,
      "shared_topic": 0.5,
      "new_member_invited": 5,
      "topic_digested": 5,
      "question_answered": 3,
      "answer_published": 3,
      "topic_or_comment_rewarded": 2,
      "topic_published": 2,
      "topic_or_comment_liked": 0.1,
      "topic_or_comment_replied": 0.1,
      "comment_published": 0.6,
      "topic_or_comment_like_published": 0.1,
      "profile_submitted": 0
    },
    "exclude_privilege_user": false,
    "description": ""
  }
}
```

**平均响应时间**: 68ms


---

#### `GET` /v2/dashboard/groups/{group_id}/overview

**接口名称**: 获取星球数据面板概览

**功能说明**: 获取指定星球的数据面板概览信息，包括成员数、话题数、活跃度等统计数据

**完整 URL**:
```
https://api.zsxq.com/v2/dashboard/groups/15555411412112/overview
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268192` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `2d4ba198f6940d64c2c5af645caef7c80bdb3376` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "daily": {
      "member_increase_count": 2,
      "member_active_count": 532
    },
    "statistics": {
      "yesterday": {
        "member_increase_count": 2,
        "member_renew_count": 0,
        "member_active_count": 532,
        "member_read_count": 16169,
        "member_like_count": 113,
        "member_comment_count": 32,
        "member_task_count": 1,
        "member_checkin_count": 111
      },
      "within_30days_average": {
        "member_increase_count": 18.5,
        "member_renew_count": 0.6,
        "member_active_count": 581.9,
        "member_read_count": 27494.7,
        "member_like_count": 199.1,
        "member_comment_count": 31.3,
        "member_task_count": 0,
        "member_checkin_count": 255.1
      },
      "future_90days_expired": {
        "total_count": 237,
        "un_renewed_count": 219,
        "renewed_count": 18
      }
    }
  }
}
```

**平均响应时间**: 106ms


---

#### `GET` /v2/dashboard/groups/{group_id}/scoreboard/statistics/self

**接口名称**: 获取个人积分统计

**功能说明**: 获取当前用户在指定星球的个人积分统计信息

**完整 URL**:
```
https://api.zsxq.com/v2/dashboard/groups/15555411412112/scoreboard/statistics/self
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268192` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `9cffa1f42b32905048c4d7858448d25641960ca9` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "statistics": {
      "yesterday_increase_score": 0,
      "this_month_increase_score": 0.9,
      "this_week_rank": 435,
      "this_month_rank": 389
    }
  }
}
```

**平均响应时间**: 69ms


---

#### `GET` /v2/groups/{group_id}/columns/summary

**接口名称**: 获取专栏摘要

**功能说明**: 获取指定星球的专栏摘要信息

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/columns/summary
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268192` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `5fd126bee52ac9bb5e1cd19c870c20f371fd51b1` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "has_columns": true,
    "title": "精品专栏加微330517251"
  }
}
```

**平均响应时间**: 92ms


---

#### `GET` /v2/groups/{group_id}/statistics

**接口名称**: 获取星球统计信息

**功能说明**: 获取指定星球的统计数据，包括成员数、话题数、评论数等

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/statistics
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268192` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `242f1383acb765b5c1228e95e6545719d5bea5b3` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "topics_count": 64066,
    "digests_count": 454,
    "answers_count": 24,
    "privileged": {
      "isolates_count": 15,
      "expired_count": 346,
      "trials_count": 0
    },
    "tasks_count": 3
  }
}
```

**平均响应时间**: 71ms


---

#### `GET` /v2/groups/{group_id}/checkins

**接口名称**: 获取星球打卡项目列表

**功能说明**: 获取指定星球的打卡项目列表，支持按状态筛选

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins?scope=ongoing&count=30
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `scope` | `ongoing` |  |
| `count` | `30` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268192` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `b2256c79aa2c9d8f623771f0ef086f3f535387c7` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'checkins': [{'checkin_id': 2424141521, 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'owner': {'user_id': 582884445452854, 'name': '深圳大冲', 'avatar_url': 'https://images.zsxq.com/FvMsMu9H2_vt7RZ3ZmeiSRAE-5Zk?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1x0/quality/75/ignore-error/1&...
```

**平均响应时间**: 82ms


---

#### `GET` /v2/groups/{group_id}/columns

**接口名称**: 获取星球专栏列表

**功能说明**: 获取指定星球的所有专栏列表

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/columns
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268192` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `f84fe3ce2f2ae8fff3524eefa4b60665ebe26354` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'columns': [{'column_id': 518528184554, 'name': '新手入门指南', 'cover_url': 'https://file.zsxq.com/column_cover.png', 'statistics': {'topics_count': 7}, 'create_time': '2025-02-02T22:23:19.070+0800', 'last_topic_attach_time': '2025-07-21T11:36:00.555+0800'}, {'column_id': 281444244211, 'name': '大冲每周复盘点评', 'cover_url': 'https://file.zsxq.com/column_cover.png', 'statistics': {'topics_count': 8}, 'create_time': '2025-08-29T17:33:27.243+0800', 'last_topic_attach_time': '...
```

**平均响应时间**: 65ms


---

#### `GET` /v2/dashboard/groups/{group_id}/incomes/overview

**接口名称**: 获取星球收入概览

**功能说明**: 获取指定星球的收入概览数据，包括总收入、各类型收入等

**完整 URL**:
```
https://api.zsxq.com/v2/dashboard/groups/15555411412112/incomes/overview
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268241` |
| `authorization` | `` |
| `x-signature` | `98cd349b74a96f5b2dfca940b4cde4edad3cc9e1` |
| `x-aduid` | `8343c2ef-5662-e4f6-1aef-00a67186c76d` |
| `x-version` | `2.85.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "overview": {
      "members": {
        "total": {
          "total_count": 5139,
          "yesterday_increase_count": 2
        },
        "paid_members": {
          "total_count": 4725,
          "yesterday_increase_count": 2
        },
        "renewed_members": {
          "yesterday_increase_count": 0,
          "this_month_increase_count": 1,
          "last_month_increase_count": 24
        }
      }
    }
  }
}
```

**平均响应时间**: 117ms


---

#### `GET` /v2/groups/{group_id}/distribution

**接口名称**: 获取星球分销信息

**功能说明**: 获取指定星球的分销配置和数据信息

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/distribution?self_inviter=true
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `self_inviter` | `true` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268255` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `37293442e9d5f5cbb9f32120d69b63b9eb1372e3` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "distribution": {
      "enabled": true,
      "percentage": 19,
      "commission_percentage": 100,
      "commission_fee": 1292,
      "returned_fee": 0
    },
    "inviter": {
      "name": "易安",
      "avatar_url": "https://images.zsxq.com/FriZRDmr30xyldqJusczqW_LdCt_?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1x0/quality/75/ignore-error/1&e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:U34XseaDWcLZjBFMSBdIoPV989E=",
      "role": "member"
    }
  }
}
```

**平均响应时间**: 72ms


---

#### `GET` /v2/groups/{group_id}/checkins

**接口名称**: 获取星球打卡项目列表

**功能说明**: 获取指定星球的打卡项目列表，支持按状态筛选

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins?scope=closed&count=30
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `scope` | `closed` |  |
| `count` | `30` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268261` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `73d71eb9af3c43a7528712ff43e2826e59e0aeb0` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'checkins': [{'checkin_id': 8424124482, 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'owner': {'user_id': 585241288554484, 'name': '阳阳', 'alias': '阳阳', 'avatar_url': 'https://images.zsxq.com/Fid7grNDgA3YpV5ag4vtRs5BgevU?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1x0/quality/75/ig...
```

**平均响应时间**: 220ms


---

#### `GET` /v2/groups/{group_id}/checkins

**接口名称**: 获取星球打卡项目列表

**功能说明**: 获取指定星球的打卡项目列表，支持按状态筛选

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins?scope=over&count=30
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `scope` | `over` |  |
| `count` | `30` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268261` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `09a47e5eb7ae46af85c2fe1f42c30562aa5b2010` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'checkins': [{'checkin_id': 5454855814, 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'owner': {'user_id': 15412512522222, 'name': '百灵', 'avatar_url': 'https://images.zsxq.com/FvK7w4lWDQDD-KcsiCUY0-Koaznk?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1x0/quality/75/ignore-error/1&e=1...
```

**平均响应时间**: 142ms


---

#### `GET` /v2/groups/{group_id}/checkins

**接口名称**: 获取星球打卡项目列表

**功能说明**: 获取指定星球的打卡项目列表，支持按状态筛选

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins?scope=ongoing&count=100
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `scope` | `ongoing` |  |
| `count` | `100` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268261` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `c63386ed20fc3f2d0c42ded3318cd856e58c8da6` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'checkins': [{'checkin_id': 2424141521, 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'owner': {'user_id': 582884445452854, 'name': '深圳大冲', 'avatar_url': 'https://images.zsxq.com/FvMsMu9H2_vt7RZ3ZmeiSRAE-5Zk?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1x0/quality/75/ignore-error/1&...
```

**平均响应时间**: 92ms


---

#### `GET` /v2/groups/{group_id}/checkins/{checkin_id}

**接口名称**: 获取打卡项目详情

**功能说明**: 获取指定打卡项目的详细信息

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins/2424141521
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268265` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `2a21286e917fb3ea80c7cd139514550a4be486d5` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'is_valid_member': True, 'checkin': {'checkin_id': 2424141521, 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'owner': {'user_id': 582884445452854, 'name': '深圳大冲', 'avatar_url': 'https://images.zsxq.com/FvMsMu9H2_vt7RZ3ZmeiSRAE-5Zk?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1x0/qua...
```

**平均响应时间**: 240ms


---

#### `GET` /v2/groups/{group_id}/checkins/{checkin_id}/statistics

**接口名称**: 获取打卡项目统计

**功能说明**: 获取指定打卡项目的统计信息，包括参与人数、打卡次数等

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins/2424141521/statistics
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268265` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `97dc156c4f32817aed52a8e875b979627adfad7a` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'statistics': {'joined_count': 29, 'completed_count': 0, 'checkined_count': 24, 'ranking_list': [{'avatar_url': 'https://images.zsxq.com/FhC1oNRym12tcqiVpdgRI_LXavLb?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1x0/quality/75/ignore-error/1&e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:vhB2GI_f56lpQwdC7ZSTKn4rM5Y='}, {'avatar_url': 'https://images.zsxq.com/FulenSryfaTFBFpj9rW3aMEM5Wxr?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1...
```

**平均响应时间**: 241ms


---

#### `GET` /v2/groups/{group_id}/checkins/{checkin_id}/statistics/daily

**接口名称**: 获取打卡项目每日统计

**功能说明**: 获取指定打卡项目的每日统计数据，按日期展示打卡情况

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins/2424141521/statistics/daily?date=2025-12-09T16%3A17%3A45.319%2B0800
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `date` | `2025-12-09T16:17:45.319+0800` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268265` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `9bebaef4fb84505077da78c165ef18354d90696a` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "checkined_count": 9
  }
}
```

**平均响应时间**: 241ms


---

#### `GET` /v2/groups/{group_id}/checkins/{checkin_id}

**接口名称**: 获取打卡项目详情

**功能说明**: 获取指定打卡项目的详细信息

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins/5454855814
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268272` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `7055424cafd6957e4da91faa2690929971eb1c89` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'is_valid_member': True, 'checkin': {'checkin_id': 5454855814, 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'owner': {'user_id': 15412512522222, 'name': '百灵', 'avatar_url': 'https://images.zsxq.com/FvK7w4lWDQDD-KcsiCUY0-Koaznk?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1x0/qualit...
```

**平均响应时间**: 78ms


---

#### `GET` /v2/groups/{group_id}/checkins/{checkin_id}/statistics

**接口名称**: 获取打卡项目统计

**功能说明**: 获取指定打卡项目的统计信息，包括参与人数、打卡次数等

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins/5454855814/statistics
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268272` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `ffbcc5105aa8a04630f230b29e48b50b33fb333d` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'statistics': {'joined_count': 118, 'completed_count': 23, 'checkined_count': 756, 'ranking_list': [{'avatar_url': 'https://images.zsxq.com/Fh6BmMzP8QWqtyfA1gStqUvnrw0E?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1x0/quality/75/ignore-error/1&e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:s0GfEh58IT1iq336TX-0SXfRcrY='}, {'avatar_url': 'https://images.zsxq.com/FhKwHYV1HXFGX20wlWsj9asWYaQw?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blu...
```

**平均响应时间**: 78ms


---

#### `GET` /v2/groups/{group_id}/checkins/{checkin_id}/statistics/daily

**接口名称**: 获取打卡项目每日统计

**功能说明**: 获取指定打卡项目的每日统计数据，按日期展示打卡情况

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins/5454855814/statistics/daily?date=2025-12-09T16%3A17%3A52.272%2B0800
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `date` | `2025-12-09T16:17:52.272+0800` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268272` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `c9265b03e57a7538240bd12369e1dbc3dc138de2` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "checkined_count": 0
  }
}
```

**平均响应时间**: 124ms


---

#### `GET` /v2/groups/{group_id}/members/{member_id}

**接口名称**: 获取成员详情

**功能说明**: 获取指定星球成员的详细信息

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/members/184444848828412
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268274` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `fd8c0d51e4463a89d24938784f0ee12871d6f1e2` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "member": {
      "group_id": 15555411412112,
      "user_id": 184444848828412,
      "name": "易安",
      "number": 4805,
      "avatar_url": "https://images.zsxq.com/FriZRDmr30xyldqJusczqW_LdCt_?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1x0/quality/75/ignore-error/1&e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:U34XseaDWcLZjBFMSBdIoPV989E=",
      "join_time": "2025-10-18T22:22:59.374+0800",
      "update_time": "2025-10-18T22:26:57.566+0800",
      "login_time": "2025-12-09T15:30:17.511+0800",
      "expired_time": "2286-11-21T01:46:39.999+0800",
      "isolated": false,
      "status": "joined"
    }
  }
}
```

**平均响应时间**: 73ms


---

#### `GET` /v2/groups/{group_id}/members/{member_id}/summary

**接口名称**: 获取成员摘要信息

**功能说明**: 获取指定星球成员的摘要信息，包括发帖数、评论数等

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/members/844415445224112/summary
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268288` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `1d9319b2b1764f522a1ea0b89ce3a736046df5d1` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "joined_days": 124,
    "topics_count": 33,
    "liked_count": 63,
    "renewal_count": 0,
    "invitees_count": 0,
    "paid": false,
    "member": {
      "user_id": 844415445224112,
      "name": "熙雾AI",
      "avatar_url": "https://images.zsxq.com/FjmBf5kkQwAuU69Z8yDVUt3GE-h2?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1x0/quality/75/ignore-error/1&e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:W5q6rIoUQEQvJgDIJBId0mytlNk=",
      "number": 3810,
      "join_time": "2025-08-08T14:35:38.461+0800",
      "status": "joined",
      "expired_time": "2026-08-08T14:35:38.461+0800",
      "introduction": "+v：ftl862941815\n公众号：熙雾AI视界"
    }
  }
}
```

**平均响应时间**: 75ms


---

#### `GET` /v2/groups/{group_id}/checkins/{checkin_id}

**接口名称**: 获取打卡项目详情

**功能说明**: 获取指定打卡项目的详细信息

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins/1141425812
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268297` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `8f37556cc3f6b87814627f0314e02af0d3f471a9` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'is_valid_member': True, 'checkin': {'checkin_id': 1141425812, 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'owner': {'user_id': 28248815485121, 'name': '深圳老易', 'alias': '深圳老易', 'avatar_url': 'https://images.zsxq.com/FmxHUIwoaOBEYL-RPqv36m4rpi0u?imageMogr2/auto-orient/thumbnail/150x/format/...
```

**平均响应时间**: 119ms


---

#### `GET` /v2/groups/{group_id}/checkins/{checkin_id}/statistics

**接口名称**: 获取打卡项目统计

**功能说明**: 获取指定打卡项目的统计信息，包括参与人数、打卡次数等

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins/1141425812/statistics
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268297` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `7797b90e1ab9884fc801d6788a65d38712d36829` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'statistics': {'joined_count': 229, 'completed_count': 2, 'checkined_count': 2107, 'ranking_list': [{'avatar_url': 'https://images.zsxq.com/FiWFBdSd57Sd6Yd_p_FEtdOQP8-h?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1x0/quality/75/ignore-error/1&e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:2M61re1ZJu4m8oGWI3KhUbU0weQ='}, {'avatar_url': 'https://images.zsxq.com/Fv7jryQQcsnaFdosEbZcvi31pqpu?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blu...
```

**平均响应时间**: 72ms


---

#### `GET` /v2/groups/{group_id}/checkins/{checkin_id}/statistics/daily

**接口名称**: 获取打卡项目每日统计

**功能说明**: 获取指定打卡项目的每日统计数据，按日期展示打卡情况

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins/1141425812/statistics/daily?date=2025-12-09T16%3A18%3A17.922%2B0800
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `date` | `2025-12-09T16:18:17.922+0800` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268297` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `bab38436f1035e864fdc7b90c0a08f2e21b4fe07` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "checkined_count": 0
  }
}
```

**平均响应时间**: 70ms


---

#### `GET` /v2/groups/{group_id}/checkins/{checkin_id}/joined_users

**接口名称**: 获取打卡项目参与用户列表

**功能说明**: 获取参与指定打卡项目的用户列表

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins/1141425812/joined_users?filter=uncheckined&count=10
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `filter` | `uncheckined` |  |
| `count` | `10` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268299` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `6d47046d29fcca63499ebf1cde0b8b67903fbc3b` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'joined_users': [{'member': {'user_id': 144151582812, 'name': '大风', 'avatar_url': 'https://images.zsxq.com/FkojEV_eWaWJe45riK4zjVlXVsOC?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1x0/quality/75/ignore-error/1&e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:kTrzCvdRFBH9bufMhtrUDlYQdeI='}, 'join_time': '2025-09-21T23:22:21.722+0800'}, {'member': {'user_id': 481128551858, 'name': 'Liulx', 'avatar_url': 'https://images.zsxq.com/FnAiHiCIkPZuG2j...
```

**平均响应时间**: 76ms


---

#### `GET` /v2/groups/{group_id}/checkins/{checkin_id}/joined_users

**接口名称**: 获取打卡项目参与用户列表

**功能说明**: 获取参与指定打卡项目的用户列表

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins/1141425812/joined_users?filter=checkined&count=10
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `filter` | `checkined` |  |
| `count` | `10` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268299` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `feb84f51d77ad7326dcffa11b5356821273f1276` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "joined_users": []
  }
}
```

**平均响应时间**: 70ms


---

#### `POST` /v2/groups/{group_id}/checkins

**接口名称**: 创建打卡项目

**功能说明**: 在指定星球创建新的打卡项目，设置打卡规则、开始结束时间等

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins
```

**功能说明**: 创建打卡项目/训练营

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765344634` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `a1b2c3d4e5f6g7h8i9j0...` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**请求体**:

```json
{
  "req_data": {
    "checkin_days": 7,
    "type": "accumulated",
    "show_topics_on_timeline": false,
    "title": "测试打卡流程",
    "text": "打卡10天，要求完成7天",
    "validity": {
      "long_period": false,
      "expiration_time": "2025-12-17T23:59:59.594+0800"
    }
  }
}
```

**请求参数说明**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| `checkin_days` | integer | 是 | 需要打卡的天数 |
| `type` | string | 是 | 打卡类型: `accumulated`(累计) / `continuous`(连续) |
| `show_topics_on_timeline` | boolean | 否 | 是否在时间线显示打卡话题 |
| `title` | string | 是 | 打卡项目标题 |
| `text` | string | 否 | 打卡项目描述 |
| `validity.long_period` | boolean | 是 | 是否长期有效 |
| `validity.expiration_time` | string | 否 | 截止时间（ISO8601格式），`long_period=false`时必填 |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "checkin": {
      "checkin_id": 2424141411,
      "group": {
        "group_id": 15555411412112,
        "name": "AI私域赚钱",
        "background_url": "https://images.zsxq.com/..."
      },
      "owner": {
        "user_id": 184444848828412,
        "name": "易安",
        "avatar_url": "https://images.zsxq.com/..."
      },
      "title": "测试打卡流程",
      "text": "打卡10天，要求完成7天",
      "checkin_days": 7,
      "validity": {
        "long_period": false,
        "expiration_time": "2025-12-17T23:59:59.594+0800"
      },
      "show_topics_on_timeline": false,
      "create_time": "2025-12-10T16:30:34.085+0800",
      "status": "ongoing",
      "type": "accumulated",
      "joined_count": 0,
      "statistics": {
        "joined_count": 0,
        "completed_count": 0,
        "checkined_count": 0
      },
      "joined_users": [],
      "user_specific": {
        "joined": false
      },
      "min_words_count": 0
    }
  }
}
```

**平均响应时间**: 150ms


---

#### `PUT` /v2/groups/{group_id}/checkins/{checkin_id}

**接口名称**: 更新打卡项目

**功能说明**: 更新指定打卡项目的配置信息，如名称、规则、时间等

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins/2424141411
```

**功能说明**: 修改打卡项目（标题/描述）

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765344650` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `b2c3d4e5f6g7h8i9j0k1...` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**请求体**:

```json
{
  "req_data": {
    "title": "测试打卡流程",
    "text": "打卡10天，要求完成7天，修改"
  }
}
```

**请求参数说明**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| `title` | string | 否 | 打卡项目新标题 |
| `text` | string | 否 | 打卡项目新描述 |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {}
}
```

**平均响应时间**: 80ms


---

#### `PUT` /v2/groups/{group_id}/checkins/{checkin_id} (关闭项目)

**接口名称**: 关闭打卡项目

**功能说明**: 关闭指定的打卡项目，停止接受新的打卡记录

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/checkins/2424141411
```

**功能说明**: 关闭打卡项目

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765344680` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `c3d4e5f6g7h8i9j0k1l2...` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**请求体**:

```json
{
  "req_data": {
    "status": "closed"
  }
}
```

**请求参数说明**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| `status` | string | 是 | 状态值: `closed`(关闭项目) |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {}
}
```

**平均响应时间**: 75ms


---

### 5.3 星球推荐

**接口数量**: 1

#### `GET` /v2/groups/recommendations

**接口名称**: 获取推荐星球列表

**功能说明**: 获取系统推荐的星球列表

**完整 URL**:
```
https://api.zsxq.com/v2/groups/recommendations?count=3&index=3&source=GroupListView
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `3` |  |
| `index` | `3` |  |
| `source` | `GroupListView` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268187` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `b7d36b833b742efbb96fe9ab86b71eb1cce0b9c3` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'groups': [{'group_id': 15281855242282, 'name': '亚楠高阶启蒙星球', 'background_url': 'https://images.zsxq.com/Fh9Ms7pKCrOzwSvE9XCNhmlUrBAC?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:fJwshcj3XXgN5PBnb6yn2iEcnVw=', 'statistics': {'members': {'count': 1491}, 'topics': {'topics_count': 17952, 'answers_count': 1214, 'digests_count': 5241}}, 'type': 'pay', 'owner': {'name': '亚楠'}, 'policies': {'payment': {'amount': 5000}, 'enable_iap_join_group': True}, 'des...
```

**平均响应时间**: 122ms


---

### 5.4 未读消息

**接口数量**: 1

#### `GET` /v2/groups/unread_topics_count

**接口名称**: 获取星球未读话题数

**功能说明**: 获取各星球的未读话题数量统计

**完整 URL**:
```
https://api.zsxq.com/v2/groups/unread_topics_count
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268187` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `ab119c975ebe171d98511d377d510fc05e185ca4` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "groups": [
      {
        "group_id": 88885121521552,
        "count": 14
      },
      {
        "group_id": 48888152588828,
        "count": 15
      },
      {
        "group_id": 48888828515858,
        "count": 94
      },
      {
        "group_id": 28888881514141,
        "count": 282
      },
      {
        "group_id": 51115214421144,
        "count": 0
      },
      {
        "group_id": 15555411412112,
        "count": 0
      },
      {
        "group_id": 88851415151812,
        "count": 229
      }
    ]
  }
}
```

**平均响应时间**: 69ms


---


## 6. 用户系统

### 6.1 优惠券

**接口数量**: 1

#### `GET` /v2/users/self/merchant_coupons

**接口名称**: 获取用户优惠券列表

**功能说明**: 获取当前用户拥有的商家优惠券列表

**完整 URL**:
```
https://api.zsxq.com/v2/users/self/merchant_coupons
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268188` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `3ab06096583c808fe90592ed7a5b67afa397efa5` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "coupons": []
  }
}
```

**平均响应时间**: 201ms


---

### 6.2 备注管理

**接口数量**: 1

#### `GET` /v3/users/self/remarks

**接口名称**: 获取用户备注列表

**功能说明**: 获取当前用户设置的好友备注信息列表

**完整 URL**:
```
https://api.zsxq.com/v3/users/self/remarks?begin_time=1970-01-01T08%3A00%3A00.001%2B0800
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `begin_time` | `1970-01-01T08:00:00.001+0800` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268187` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `c85399dfc088bc5f7f463b099b274401ed8bcf9e` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `3.9.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "remarks": []
  }
}
```

**平均响应时间**: 64ms


---

### 6.3 推荐系统

**接口数量**: 1

#### `GET` /v2/users/self/recommendations/users

**接口名称**: 获取推荐用户列表

**功能说明**: 获取系统为当前用户推荐的可能认识的用户列表

**完整 URL**:
```
https://api.zsxq.com/v2/users/self/recommendations/users?type=unfollowed_group_owner&count=9
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `type` | `unfollowed_group_owner` |  |
| `count` | `9` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268187` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `679f74c607553e84fea32abcde951ccca8b60261` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "recommended_users": []
  }
}
```

**平均响应时间**: 69ms


---

### 6.4 用户信息

**接口数量**: 5

#### `GET` /v3/users/self

**接口名称**: 获取当前用户信息

**功能说明**: 获取当前登录用户的详细信息，包括用户ID、昵称、头像等

**完整 URL**:
```
https://api.zsxq.com/v3/users/self
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268242` |
| `x-signature` | `59a08935a84e0d6e683d7ddc5efb96b1993ccc60` |
| `x-aduid` | `e3b4c837-09a6-f7c3-a0ab-2a115d3d99a3` |
| `x-version` | `3.11.0` |
| `x-request-id` | `3717a4a4-aad9-cbf9-09b1-e483a8dd8775` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "accounts": {
      "phone": {
        "country_code": "86",
        "phone_number": "15899883113"
      },
      "wechat": {
        "avatar_url": "https://images.zsxq.com/FriZRDmr30xyldqJusczqW_LdCt_?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1x0/quality/75/ignore-error/1&e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:U34XseaDWcLZjBFMSBdIoPV989E=",
        "name": "易安"
      }
    },
    "associated_enterprise": false,
    "associated_ecommerce": false,
    "identity_status": "unknown",
    "subscriptions": {
      "subscribed_rangefinderinsight": false,
      "subscribed_xiaomiquanvip": false,
      "subscribed_xingqiusvip": false
    },
    "user": {
      "uid": "184444848828412",
      "avatar_url": "https://images.zsxq.com/FriZRDmr30xyldqJusczqW_LdCt_?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1x0/quality/75/ignore-error/1&e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:U34XseaDWcLZjBFMSBdIoPV989E=",
      "name": "易安",
      "location": "广东",
      "unique_id": "20133213",
      "user_sid": "k49k5odke4",
      "grade": "general"
    }
  }
}
```

**平均响应时间**: 72ms


---

#### `GET` /v3/users/{user_id}

**接口名称**: 获取指定用户信息

**功能说明**: 获取指定用户的详细信息

**完整 URL**:
```
https://api.zsxq.com/v3/users/844415445224112
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268288` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `12751b2c84a7aa9054120b3e19cd4cd34e646fb3` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `3.9.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "associated_ecommerce": false,
    "user": {
      "uid": "844415445224112",
      "avatar_url": "https://images.zsxq.com/FjmBf5kkQwAuU69Z8yDVUt3GE-h2?imageMogr2/auto-orient/thumbnail/150x/format/jpg/blur/1x0/quality/75/ignore-error/1&e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:W5q6rIoUQEQvJgDIJBId0mytlNk=",
      "name": "熙雾AI",
      "location": "广东",
      "introduction": "+v：ftl862941815\n公众号：熙雾AI视界",
      "verified": true
    },
    "user_specific": {
      "followed": false
    }
  }
}
```

**平均响应时间**: 74ms


---

#### `GET` /v2/users/{user_id}/footprints

**接口名称**: 获取用户足迹

**功能说明**: 获取指定用户的活动足迹记录

**完整 URL**:
```
https://api.zsxq.com/v2/users/844415445224112/footprints?count=20&group_id=15555411412112&filter=group&filter_group_id=15555411412112
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `20` |  |
| `group_id` | `15555411412112` |  |
| `filter` | `group` |  |
| `filter_group_id` | `15555411412112` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268291` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `f7ba81a190a8b7df7c415c2c6905218c7a6e09f0` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'footprints': [{'type': 'topic', 'topic': {'topic_id': 55188112221855554, 'topic_uid': '55188112221855554', 'group': {'group_id': 15555411412112, 'name': 'AI私域赚钱', 'type': 'pay', 'background_url': 'https://images.zsxq.com/FuYZ7BCEf_2tZwHtWQaZjn6UWlzs?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:H2TzyuKGxYWwT2qU3lLyzwENe_8='}, 'type': 'talk', 'talk': {'owner': {'user_id': 844415445224112, 'name': '熙雾AI', 'avatar_url': 'https://images.zsxq.com/FjmBf...
```

**平均响应时间**: 169ms


---

#### `GET` /v3/users/{user_id}/statistics

**接口名称**: 获取用户统计信息

**功能说明**: 获取指定用户的统计数据，包括加入星球数、发帖数等

**完整 URL**:
```
https://api.zsxq.com/v3/users/844415445224112/statistics
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268291` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `fc9f6d255d318a4e258d1e62523807dc81488c84` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `3.9.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "followees_count": 35,
    "followers_count": 13,
    "notes_count": 0,
    "topics_count": 95
  }
}
```

**平均响应时间**: 90ms


---

#### `GET` /v3/users/{user_id}/avatar_url

**接口名称**: 获取用户头像URL

**功能说明**: 获取指定用户的头像图片URL

**完整 URL**:
```
https://api.zsxq.com/v3/users/844415445224112/avatar_url
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268291` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `b9d430c25fc566eda14d2335ac408c5a5ced48fe` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `3.9.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "large_avatar_url": "https://images.zsxq.com/FjmBf5kkQwAuU69Z8yDVUt3GE-h2?imageMogr2/auto-orient/thumbnail/640x/format/jpg/blur/1x0/quality/75/ignore-error/1&e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:eUx_E8I83SUXOrOtc50If7EWvVk="
  }
}
```

**平均响应时间**: 58ms


---

### 6.5 用户星球管理

**接口数量**: 8

#### `GET` /v2/users/self/groups/applied_groups

**接口名称**: 获取已申请加入的星球列表

**功能说明**: 获取当前用户已申请但尚未通过的星球列表

**完整 URL**:
```
https://api.zsxq.com/v2/users/self/groups/applied_groups
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268187` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `3c85535285ef464d125a3a08454e8b9be2276d16` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "groups": []
  }
}
```

**平均响应时间**: 67ms


---

#### `GET` /v2/users/self/groups/{group_id}/inviter

**接口名称**: 获取星球邀请人信息

**功能说明**: 获取邀请当前用户加入指定星球的邀请人信息

**完整 URL**:
```
https://api.zsxq.com/v2/users/self/groups/15555411412112/inviter
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268192` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `5dc3fc6626645401648f51cdfc1221f13ea36f23` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {}
}
```

**平均响应时间**: 64ms


---

#### `GET` /v2/users/self/groups/{group_id}/checkins/{checkin_id}/topics

**接口名称**: 获取我在打卡项目中的话题

**功能说明**: 获取当前用户在指定打卡项目中发布的话题列表

**完整 URL**:
```
https://api.zsxq.com/v2/users/self/groups/15555411412112/checkins/5454855814/topics?date=2025-12-09T00%3A00%3A00.000%2B0800
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `date` | `2025-12-09T00:00:00.000+0800` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268282` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `47475e5534ae4c104f91d77503f0f2cede124159` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": false,
  "code": 52010,
  "info": "",
  "resp_data": {},
  "error": "未报名参加该打卡任务"
}
```

**平均响应时间**: 173ms


---

#### `GET` /v2/users/self/groups/{group_id}/checkins/{checkin_id}/checkined_dates

**接口名称**: 获取我的打卡日期列表

**功能说明**: 获取当前用户在指定打卡项目中已打卡的日期列表

**完整 URL**:
```
https://api.zsxq.com/v2/users/self/groups/15555411412112/checkins/5454855814/checkined_dates?begin_time=2025-12-01T00%3A00%3A00.000%2B0800&end_time=2025-12-31T23%3A59%3A59.900%2B0800
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `begin_time` | `2025-12-01T00:00:00.000+0800` |  |
| `end_time` | `2025-12-31T23:59:59.900+0800` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268282` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `d9117e5b872a4429b124199607c485ff371c22ce` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": false,
  "code": 52010,
  "info": "",
  "resp_data": {},
  "error": "未报名参加该打卡任务"
}
```

**平均响应时间**: 211ms


---

#### `GET` /v2/users/self/groups/{group_id}/checkins/{checkin_id}/checkined_dates

**接口名称**: 获取我的打卡日期列表

**功能说明**: 获取当前用户在指定打卡项目中已打卡的日期列表

**完整 URL**:
```
https://api.zsxq.com/v2/users/self/groups/15555411412112/checkins/5454855814/checkined_dates?begin_time=2026-01-01T00%3A00%3A00.000%2B0800&end_time=2026-01-04T23%3A59%3A59.900%2B0800
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `begin_time` | `2026-01-01T00:00:00.000+0800` |  |
| `end_time` | `2026-01-04T23:59:59.900+0800` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268282` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `76fba8d783f7da5b9bebfbdf2f09b389ef3139a2` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": false,
  "code": 52010,
  "info": "",
  "resp_data": {},
  "error": "未报名参加该打卡任务"
}
```

**平均响应时间**: 211ms


---

#### `GET` /v2/users/self/groups/{group_id}/checkins/{checkin_id}/statistics

**接口名称**: 获取我的打卡统计

**功能说明**: 获取当前用户在指定打卡项目中的个人打卡统计数据

**完整 URL**:
```
https://api.zsxq.com/v2/users/self/groups/15555411412112/checkins/5454855814/statistics
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268282` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `a2ec78f6a61edb2989d9eaa0a8f6c93f3945cdd2` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": false,
  "code": 52010,
  "info": "",
  "resp_data": {},
  "error": "未报名参加该打卡任务"
}
```

**平均响应时间**: 196ms


---

#### `GET` /v2/users/{user_id}/created_groups

**接口名称**: 获取用户创建的星球列表

**功能说明**: 获取指定用户创建的星球列表

**完整 URL**:
```
https://api.zsxq.com/v2/users/844415445224112/created_groups?count=200
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `count` | `200` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268291` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `280f433b18616db1060783cf3212c64366274741` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {
    "groups": []
  }
}
```

**平均响应时间**: 62ms


---

#### `GET` /v2/users/{user_id}/footprints/groups

**接口名称**: 获取用户活跃的星球列表

**功能说明**: 获取指定用户有活动足迹的星球列表

**完整 URL**:
```
https://api.zsxq.com/v2/users/844415445224112/footprints/groups?group_id=15555411412112
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `group_id` | `15555411412112` |  |

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268291` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `b0d8a81be06a8b70e816cfd0b00f0561361af7a5` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'groups': [{'group_id': 48884125881548, 'name': '江工·DeepSeek·AIP·十年退休', 'background_url': 'https://images.zsxq.com/FnTqb3A-RtGq6sFZNoqNvQ6Nw4J3?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:_73LRri3EMe-2zu1wfbwKV9T6Iw=', 'member_topics_count': 0}, {'group_id': 15552545485212, 'name': 'AI破局俱乐部', 'background_url': 'https://images.zsxq.com/FvLDlIM-8yYeC17o1sLs5R3yn6J8?e=1769875199&token=kIxbL07-8jAj8w1n4s9zv64FuZZNEATmlU_Vm6zD:vd-Mg1ivLm4D6PvaoENoBLZa...
```

**平均响应时间**: 78ms


---


## 7. 监控系统

### 7.1 性能监控

**接口数量**: 1

#### `POST` /api/{api_id}/envelope/

**接口名称**: 极光推送信封接口

**功能说明**: 极光推送服务的信封API，用于消息推送

**完整 URL**:
```
https://client-report.zsxq.com/api/5/envelope/?sentry_version=7&sentry_key=eb3ab2f2d9cc499e95d1c8ff86a0637a&sentry_client=sentry.javascript.vue%2F9.0.1
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `sentry_version` | `7` |  |
| `sentry_key` | `eb3ab2f2d9cc499e95d1c8ff86a0637a` |  |
| `sentry_client` | `sentry.javascript.vue/9.0.1` |  |

**请求体**:

```json
{"sent_at":"2025-12-09T08:17:21.820Z","sdk":{"name":"sentry.javascript.vue","version":"9.0.1"}}
{"type":"session"}
{"sid":"b680d2a8ebd04ad193f426c9738afff5","init":true,"started":"2025-12-09T08:17:21....
```

**响应状态码**: `200`

**平均响应时间**: 145ms


---

### 7.2 错误上报

**接口数量**: 6

#### `POST` /rqd/sync

**接口名称**: 腾讯质量监控数据同步

**功能说明**: 腾讯移动质量监控（RQD）的数据同步接口，用于上报崩溃和性能数据

**完整 URL**:
```
https://ios.bugly.qq.com/rqd/sync?aid=FB8E26AF-5DF6-4BED-86E0-D431C39B29FF
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `aid` | `FB8E26AF-5DF6-4BED-86E0-D431C39B29FF` |  |

**响应状态码**: `200`

**平均响应时间**: 73ms


---

#### `POST` /rqd/sync

**接口名称**: 腾讯质量监控数据同步

**功能说明**: 腾讯移动质量监控（RQD）的数据同步接口，用于上报崩溃和性能数据

**完整 URL**:
```
https://ios.bugly.qq.com/rqd/sync?aid=03C24FBA-0CA5-4C07-BE56-2D34AF6A3516
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `aid` | `03C24FBA-0CA5-4C07-BE56-2D34AF6A3516` |  |

**响应状态码**: `200`

**平均响应时间**: 70ms


---

#### `POST` /rqd/sync

**接口名称**: 腾讯质量监控数据同步

**功能说明**: 腾讯移动质量监控（RQD）的数据同步接口，用于上报崩溃和性能数据

**完整 URL**:
```
https://ios.bugly.qq.com/rqd/sync?aid=2A442AE6-F690-423E-AEBE-1E462752D0B9
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `aid` | `2A442AE6-F690-423E-AEBE-1E462752D0B9` |  |

**响应状态码**: `200`

**平均响应时间**: 64ms


---

#### `POST` /rqd/sync

**接口名称**: 腾讯质量监控数据同步

**功能说明**: 腾讯移动质量监控（RQD）的数据同步接口，用于上报崩溃和性能数据

**完整 URL**:
```
https://ios.bugly.qq.com/rqd/sync?aid=63F67FBB-732B-4B66-A267-E4F1CEED712B
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `aid` | `63F67FBB-732B-4B66-A267-E4F1CEED712B` |  |

**响应状态码**: `200`

**平均响应时间**: 303ms


---

#### `POST` /rqd/sync

**接口名称**: 腾讯质量监控数据同步

**功能说明**: 腾讯移动质量监控（RQD）的数据同步接口，用于上报崩溃和性能数据

**完整 URL**:
```
https://ios.bugly.qq.com/rqd/sync?aid=533EE2B7-A226-4CC6-8888-EE3E739DDA59
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `aid` | `533EE2B7-A226-4CC6-8888-EE3E739DDA59` |  |

**响应状态码**: `200`

**平均响应时间**: 71ms


---

#### `POST` /rqd/sync

**接口名称**: 腾讯质量监控数据同步

**功能说明**: 腾讯移动质量监控（RQD）的数据同步接口，用于上报崩溃和性能数据

**完整 URL**:
```
https://ios.bugly.qq.com/rqd/sync?aid=A2E994F3-E510-47A4-AA8B-D82A2EECEED7
```

**查询参数**:

| 参数名 | 值 | 说明 |
|--------|----|----- |
| `aid` | `A2E994F3-E510-47A4-AA8B-D82A2EECEED7` |  |

**响应状态码**: `200`

**平均响应时间**: 77ms


---


## 8. 阅读追踪

### 8.1 阅读进度

**接口数量**: 2

#### `GET` /v2/groups/{group_id}/menus/last_read_time

**接口名称**: 获取菜单最后阅读时间

**功能说明**: 获取当前用户最后阅读星球菜单的时间

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/menus/last_read_time
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268192` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-signature` | `4fca33c489fa9ed2c938d9cecd7dd8dfcf5f48aa` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-version` | `2.83.0` |

**响应状态码**: `200`

**响应示例**:

```json
{'succeeded': True, 'resp_data': {'menus': [{'menu_id': 51285448425514, 'last_read_time': '2025-12-04T01:47:34.977+0800'}, {'menu_id': 15284118124422, 'last_read_time': '2025-12-09T15:49:39.927+0800'}, {'menu_id': 28844882581111, 'last_read_time': '2025-12-09T15:49:39.927+0800'}, {'menu_id': 15514284488142, 'last_read_time': '2025-12-09T15:49:39.927+0800'}, {'menu_id': 51144121845184, 'last_read_time': '2025-12-09T15:49:39.927+0800'}, {'menu_id': 51145125424814, 'last_read_time': '2025-12-09T15:...
```

**平均响应时间**: 68ms


---

#### `PUT` /v2/groups/{group_id}/menus/last_read_time

**接口名称**: 更新菜单最后阅读时间

**功能说明**: 更新当前用户最后阅读星球菜单的时间

**完整 URL**:
```
https://api.zsxq.com/v2/groups/15555411412112/menus/last_read_time
```

**特殊请求头**:

| 请求头 | 值 |
|--------|----|
| `x-timestamp` | `1765268192` |
| `authorization` | `D047A423-A...169922C77C` |
| `x-aduid` | `d75d966c-ed30-4fe8-b0f9-f030eb39d9be` |
| `x-signature` | `c206ddedd385b5ffe08302354ddc81c99a0c5e78` |
| `x-version` | `2.83.0` |

**请求体**:

```json
{
  "req_data": {
    "menus": [
      {
        "last_read_time": "2025-12-09T16:16:32.244+0800",
        "menu_id": 15284118124422
      },
      {
        "last_read_time": "2025-12-09T16:16:32.244+0800",
        "menu_id": 28844882581111
      },
      {
        "last_read_time": "2025-12-09T16:16:32.244+0800",
        "menu_id": 15514284488142
      },
      {
        "last_read_time": "2025-12-09T16:16:32.244+0800",
        "menu_id": 51144121845184
      },
      {
        "last_read_time": "2025-12-09T16:16:32.244+0800",
        "menu_id": 51145125424814
      },
      {
        "last_read_time": "2025-12-09T16:16:32.244+0800",
        "menu_id": 15425825555222
      },
      {
        "last_read_time": "2025-12-09T16:16:32.244+0800",
        "menu_id": 51521485524884
      },
      {
        "last_read_time": "2025-12-09T16:16:32.244+0800",
        "menu_id": 28812242884811
      },
      {
        "last_read_time": "2025-12-09T16:16:32.244+0800",
        "menu_id": 48818152154548
      },
      {
        "last_read_time": "2025-12-09T16:16:32.244+0800",
        "menu_id": 48548284252828
      },
      {
        "last_read_time": "2025-12-09T16:16:32.244+0800",
        "menu_id": 88258485424282
      },
      {
        "last_read_time": "2025-12-09T16:16:32.244+0800",
        "menu_id": 51521412454144
      }
    ]
  }
}
```

**响应状态码**: `200`

**响应示例**:

```json
{
  "succeeded": true,
  "resp_data": {}
}
```

**平均响应时间**: 82ms


---


## 📊 文档统计

- **接口总数**: 118 个（去重后）
- **原始请求**: 188 个
- **业务模块**: 8 个
- **子分类**: 16 个

## 🎯 使用说明

### 认证方式

所有接口都需要在请求头中包含认证信息，核心参数包括：

1. **authorization**: 用户认证 Token
2. **x-signature**: 基于时间戳和请求内容的签名（SHA1）
3. **x-timestamp**: Unix 时间戳
4. **x-aduid**: 设备唯一标识

### 签名算法（推测）

```python
import hmac
import hashlib
import time

def generate_signature(timestamp, method, path, body=None, secret_key="UNKNOWN"):
    """
    生成请求签名
    注意：secret_key 需要通过逆向工程获取
    """
    sign_data = f"{timestamp}\n{method}\n{path}"
    if body:
        sign_data += f"\n{body}"
    
    signature = hmac.new(
        secret_key.encode(),
        sign_data.encode(),
        hashlib.sha1
    ).hexdigest()
    
    return signature
```

## 🔧 逆向工程提示

1. **iOS App 脱壳**: 使用 frida-ios-dump 或 Clutch
2. **找到加密密钥**: 使用 Hopper/IDA 反汇编，搜索 "x-signature" 相关代码
3. **动态调试**: 使用 Frida hook 签名函数，获取实际的签名算法和密钥

---

# SDK 封装接口设计

> 以下是基于知识星球原生 API 封装的 SDK 接口设计，适用于 TypeScript、Java、Go、Python 等多语言 SDK 实现

## 基础信息

- **认证方式**: Token 认证（通过 `authorization` 请求头）
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

---

## 2.1 认证接口

### 用户登录

**接口**: `POST /auth/login`

**对应原生API**: 无（本地认证）

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

---

## 2.2 用户接口

### 获取当前用户信息

**接口**: `GET /users/me`

**对应原生API**: `GET /v3/users/self`

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

---

## 2.3 星球接口

### 获取星球列表

**接口**: `GET /planets`

**对应原生API**: `GET /v2/groups`

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

### 获取星球详情

**接口**: `GET /planets/:id`

**对应原生API**: `GET /v2/groups/{group_id}`

**Headers**: `Authorization: Bearer <token>`

**路径参数**:
- `id`: 星球ID

### 获取星球话题列表

**接口**: `GET /planets/:id/topics`

**对应原生API**: `GET /v2/groups/{group_id}/topics`

**Headers**: `Authorization: Bearer <token>`

**查询参数**:
- `page` (可选): 页码，默认1
- `pageSize` (可选): 每页数量，默认20
- `scope` (可选): 范围 all/digests/by_owner

---

## 2.4 话题接口

### 获取话题详情

**接口**: `GET /topics/:id`

**对应原生API**: `GET /v2/topics/{topic_id}`

**Headers**: `Authorization: Bearer <token>`

### 获取话题评论

**接口**: `GET /topics/:id/comments`

**对应原生API**: `GET /v2/topics/{topic_id}/comments`

**查询参数**:
- `sort` (可选): asc/desc
- `count` (可选): 返回数量

---

## 2.5 打卡项目接口

> 基于知识星球原生打卡功能封装

### 获取打卡项目列表

**接口**: `GET /planets/:planetId/checkins`

**对应原生API**: `GET /v2/groups/{group_id}/checkins`

**Headers**: `Authorization: Bearer <token>`

**查询参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| scope | string | 否 | ongoing | 项目范围: ongoing/closed/over |

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
  }
}
```

**缓存策略**: TTL=7200秒(2小时)

### 获取打卡项目详情

**接口**: `GET /checkins/:checkinId`

**对应原生API**: `GET /v2/groups/{group_id}/checkins/{checkin_id}`

### 获取打卡项目统计

**接口**: `GET /checkins/:checkinId/stats`

**对应原生API**: `GET /v2/groups/{group_id}/checkins/{checkin_id}/statistics`

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
  }
}
```

**缓存策略**: TTL=3600秒(1小时)

### 获取打卡每日统计

**接口**: `GET /checkins/:checkinId/daily-stats`

**对应原生API**: `GET /v2/groups/{group_id}/checkins/{checkin_id}/statistics/daily`

**查询参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| date | string | 否 | 今天 | 查询日期，格式: YYYY-MM-DD |

**缓存策略**: TTL=1800秒(30分钟)

### 获取打卡排行榜

**接口**: `GET /checkins/:checkinId/leaderboard`

**对应原生API**: `GET /v2/groups/{group_id}/checkins/{checkin_id}/ranking_list`

**查询参数**:
| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| type | string | 否 | continuous | 排行榜类型: continuous/accumulated |
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
      }
    ],
    "total": 150,
    "userRank": {
      "rank": 6,
      "days": 20
    },
    "cachedAt": "2025-01-15T10:30:00.000+08:00"
  }
}
```

**缓存策略**: TTL=3600秒(1小时)

### 获取打卡话题列表

**接口**: `GET /checkins/:checkinId/topics`

**对应原生API**: `GET /v2/groups/{group_id}/checkins/{checkin_id}/topics`

**缓存策略**: TTL=600秒(10分钟)

---

## 2.6 训练营接口

> 训练营是打卡项目的别称，以下接口与打卡项目接口功能相同

### 获取训练营列表

**接口**: `GET /training-camps`

**查询参数**:
- `planetId` (必填): 星球ID
- `scope` (可选): 项目范围，默认 ongoing

### 获取训练营详情

**接口**: `GET /training-camps/:id`

### 获取训练营打卡记录

**接口**: `GET /training-camps/:id/checkins`

### 获取训练营排行榜

**接口**: `GET /training-camps/:id/ranking`

---

## 2.7 星主专用接口

> 以下接口需要星主权限 (role: 'owner')

### 获取星球成员列表

**接口**: `GET /owner/planets/:planetId/members`

**对应原生API**: `GET /v2/groups/{group_id}/role_members`

### 创建训练营

**接口**: `POST /owner/training-camps`

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

### 创建话题

**接口**: `POST /owner/topics`

### 更新话题

**接口**: `PUT /owner/topics/:id`

### 删除话题

**接口**: `DELETE /owner/topics/:id`

---

## 2.8 API 映射表

| 服务端接口 | 原生 API | 说明 |
|-----------|---------|------|
| `GET /users/me` | `GET /v3/users/self` | 获取当前用户 |
| `GET /planets` | `GET /v2/groups` | 获取星球列表 |
| `GET /planets/:id` | `GET /v2/groups/{group_id}` | 获取星球详情 |
| `GET /planets/:id/topics` | `GET /v2/groups/{group_id}/topics` | 获取话题列表 |
| `GET /topics/:id` | `GET /v2/topics/{topic_id}` | 获取话题详情 |
| `GET /topics/:id/comments` | `GET /v2/topics/{topic_id}/comments` | 获取评论 |
| `GET /planets/:id/checkins` | `GET /v2/groups/{group_id}/checkins` | 获取打卡项目 |
| `GET /checkins/:id` | `GET /v2/groups/{group_id}/checkins/{checkin_id}` | 打卡详情 |
| `GET /checkins/:id/stats` | `GET /v2/groups/{group_id}/checkins/{checkin_id}/statistics` | 打卡统计 |
| `GET /checkins/:id/leaderboard` | `GET /v2/groups/{group_id}/checkins/{checkin_id}/ranking_list` | 排行榜 |
| `POST /planets/:id/checkins` | `POST /v2/groups/{group_id}/checkins` | 创建打卡项目 |
| `PUT /checkins/:id` | `PUT /v2/groups/{group_id}/checkins/{checkin_id}` | 修改打卡项目 |
| `PUT /checkins/:id/close` | `PUT /v2/groups/{group_id}/checkins/{checkin_id}` | 关闭打卡项目 |

---

## 2.9 错误代码

| 错误代码 | 说明 |
|---------|------|
| `UNAUTHORIZED` | 未授权，需要登录 |
| `FORBIDDEN` | 权限不足 |
| `NOT_FOUND` | 资源不存在 |
| `PLANET_NOT_FOUND` | 星球不存在 |
| `TOPIC_NOT_FOUND` | 话题不存在 |
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

---

## 📝 更新日志

- **2025-12-09**: 初始版本，包含 118 个唯一接口
- **2025-12-09**: 整合 SDK 封装接口设计文档

---

**生成工具**: analyze_har.py + generate_api_docs.py
**数据来源**: Fiddler Everywhere 抓包
**App 版本**: 知识星球 iOS 5.29.1
