# 用户 API

> UserRequest 类的 API 文档

## 概述

`UserRequest` 提供用户相关的所有操作。

```typescript
// 通过客户端访问
const users = client.users;
```

---

## 方法列表

### getSelf()

获取当前登录用户信息。

**签名**:
```typescript
getSelf(): Promise<SelfUser>
```

**返回值**:
```typescript
interface SelfUser {
  user: User;
  accounts: {
    phone?: {
      country_code: string;
      phone_number: string;
    };
    wechat?: {
      name: string;
      avatar_url: string;
    };
  };
  subscriptions: {
    subscribed_rangefinderinsight: boolean;
    subscribed_xiaomiquanvip: boolean;
    subscribed_xingqiusvip: boolean;
  };
  identity_status: string;
  associated_enterprise: boolean;
  associated_ecommerce: boolean;
}
```

**示例**:
```typescript
const me = await client.users.getSelf();
console.log(`欢迎, ${me.user.name}`);
```

---

### get(userId)

获取指定用户信息。

**签名**:
```typescript
get(userId: string | number): Promise<UserInfo>
```

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| userId | string \| number | 是 | 用户 ID |

**返回值**:
```typescript
interface UserInfo {
  user: User;
  user_specific?: {
    followed: boolean;
  };
  associated_ecommerce: boolean;
}
```

---

### getStatistics(userId)

获取用户统计数据。

**签名**:
```typescript
getStatistics(userId: string | number): Promise<UserStatistics>
```

**返回值**:
```typescript
interface UserStatistics {
  followees_count: number;  // 关注数
  followers_count: number;  // 粉丝数
  topics_count: number;     // 发帖数
  notes_count: number;      // 笔记数
}
```

---

### getAvatar(userId)

获取用户大尺寸头像 URL。

**签名**:
```typescript
getAvatar(userId: string | number): Promise<string>
```

---

### getFootprints(userId, options?)

获取用户动态/足迹。

**签名**:
```typescript
getFootprints(userId: string | number, options?: FootprintOptions): Promise<Footprint[]>
```

**选项**:
```typescript
interface FootprintOptions {
  count?: number;              // 返回数量
  group_id?: string | number;  // 筛选特定星球
  filter?: 'group';            // 过滤类型
  filter_group_id?: string | number;
}
```

**示例**:
```typescript
// 获取用户在特定星球的动态
const footprints = await client.users.getFootprints(userId, {
  count: 20,
  group_id: groupId,
  filter: 'group',
  filter_group_id: groupId
});
```

---

### getCreatedGroups(userId, count?)

获取用户创建的星球列表。

**签名**:
```typescript
getCreatedGroups(userId: string | number, count?: number): Promise<Group[]>
```

---

### getRemarks()

获取当前用户的备注列表。

**签名**:
```typescript
getRemarks(beginTime?: string): Promise<Remark[]>
```

---

### getCoupons()

获取当前用户的优惠券列表。

**签名**:
```typescript
getCoupons(): Promise<Coupon[]>
```

---

### getAppliedGroups()

获取当前用户申请中的星球。

**签名**:
```typescript
getAppliedGroups(): Promise<Group[]>
```

---

## 数据模型

### User

```typescript
interface User {
  user_id: number;
  uid?: string;
  name: string;
  avatar_url: string;
  location?: string;
  introduction?: string;
  unique_id?: string;
  user_sid?: string;
  grade?: string;
  verified?: boolean;
}
```

### Footprint

```typescript
interface Footprint {
  type: 'topic' | 'comment' | 'like';
  topic?: Topic;
  comment?: Comment;
  create_time: string;
}
```

---

## 相关文档

- [API 映射](../design/api-mapping.md#用户相关-userrequest)
- [认证指南](../guides/authentication.md)
