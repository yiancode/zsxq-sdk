# 知识星球 API 路径修复与废弃说明

本文档记录了知识星球 SDK 中的 API 路径修复和真正已废弃的 API。

## ✅ 已修复的 API 路径 (2025-12-13)

以下 API 之前使用了错误的路径，现已修复：

| 功能 | ❌ 旧路径(错误) | ✅ 新路径(正确) | 状态 |
|------|--------------|---------------|------|
| 获取备注列表 | `GET /v2/remarks` | `GET /v3/users/self/remarks?begin_time=...` | ✅ 已修复 |
| 获取优惠券 | `GET /v2/coupons` | `GET /v2/users/self/merchant_coupons` | ✅ 已修复 |
| 获取邀请人 | `GET /v2/groups/{group_id}/inviter` | `GET /v2/users/self/groups/{group_id}/inviter` | ✅ 已修复 |
| 获取用户足迹 | `GET /v2/users/{user_id}/group_footprints` | `GET /v2/users/{user_id}/footprints/groups?group_id=...` | ✅ 已修复 |

**测试结果**:
- ✅ GetRemarks: 成功获取备注 (2025-12-13 测试)
- ✅ GetCoupons: 成功获取优惠券 (2025-12-13 测试)
- ✅ GetInviter: API 调用成功 (2025-12-13 测试)
- ✅ GetGroupFootprints: 成功获取 189 条星球足迹 (2025-12-13 测试)

## ❌ 确认已废弃的 API

以下 API 端点已被知识星球官方废弃，返回 HTTP 404 错误：

### 用户系统 (2个已废弃)
| API 端点 | SDK 方法 | 废弃时间 |
|---------|---------|---------|
| `GET /v2/groups/applying` | `Users().GetApplyingGroups()` | 未知 |
| `GET /v2/users/recommended_follows` | `Users().GetRecommendedFollows()` | 未知 |

### 排行榜系统 (2个已废弃)
| API 端点 | SDK 方法 | 废弃时间 | 替代方案 |
|---------|---------|---------|---------|
| `GET /v2/groups/{groupId}/ranking_list` | `Ranking().GetGroupRanking()` | 未知 | 使用 `GetGroupRankingStats()` |
| `GET /v2/groups/{groupId}/contribution_ranking_list` | `Ranking().GetContributionRanking()` | 未知 | 暂无 |

### 杂项功能 (3个已废弃)
| API 端点 | SDK 方法 | 废弃时间 |
|---------|---------|---------|
| `GET /v2/global/config` | `Misc().GetGlobalConfig()` | 未知 |
| `GET /v2/activities` | `Misc().GetActivities()` | 未知 |
| `GET /v2/pk/groups/{groupId}` | `Misc().GetPkGroup()` | 未知 |

## 错误信息

调用已废弃的 API 时，SDK 会返回以下错误：

```go
[70001] API 端点不存在或已废弃 (HTTP 404): /v2/...
```

## SDK 策略

对于已废弃的 API，SDK 将:
1. 保留方法定义以保持向后兼容性
2. 返回明确的错误信息说明 API 已废弃
3. 在文档中标注为 `@deprecated`

## 更新记录

- **2025-12-13**:
  - 修复 4 个错误的 API 路径
  - 确认 7 个 API 真正已废弃
  - 添加测试验证
