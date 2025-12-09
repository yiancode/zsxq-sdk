# 星球 API

> GroupRequest 类的 API 文档

## 概述

`GroupRequest` 提供星球（知识星球社群）相关的所有操作。

```typescript
// 通过客户端访问
const groups = client.groups;
```

---

## 方法列表

### list()

获取当前用户加入的所有星球列表。

**签名**:
```typescript
list(): Promise<Group[]>
```

**返回值**: 星球数组

**示例**:
```typescript
const groups = await client.groups.list();
groups.forEach(group => {
  console.log(`${group.name} - ${group.member_count} 成员`);
});
```

---

### get(groupId)

获取指定星球的详细信息。

**签名**:
```typescript
get(groupId: string | number): Promise<Group>
```

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| groupId | string \| number | 是 | 星球 ID |

**返回值**: 星球详情

**示例**:
```typescript
const group = await client.groups.get('15555411412112');
console.log(group.description);
```

---

### getHashtags(groupId)

获取星球的所有标签。

**签名**:
```typescript
getHashtags(groupId: string | number): Promise<Hashtag[]>
```

**返回值**: 标签数组

**示例**:
```typescript
const hashtags = await client.groups.getHashtags(groupId);
hashtags.forEach(tag => {
  console.log(`#${tag.title}# - ${tag.topics_count} 篇`);
});
```

---

### getMenus(groupId, options?)

获取星球的菜单配置。

**签名**:
```typescript
getMenus(groupId: string | number, options?: {
  with_optional_menus?: boolean;
}): Promise<Menu[]>
```

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| groupId | string \| number | 是 | 星球 ID |
| with_optional_menus | boolean | 否 | 是否包含可选菜单 |

---

### getStatistics(groupId)

获取星球统计数据。

**签名**:
```typescript
getStatistics(groupId: string | number): Promise<GroupStatistics>
```

**返回值**:
```typescript
interface GroupStatistics {
  topics_count: number;      // 话题总数
  digests_count: number;     // 精华数
  answers_count: number;     // 问答数
  tasks_count: number;       // 作业数
  privileged: {
    isolates_count: number;  // 隔离人数
    expired_count: number;   // 过期人数
    trials_count: number;    // 试用人数
  };
}
```

---

### getColumns(groupId)

获取星球的专栏列表。

**签名**:
```typescript
getColumns(groupId: string | number): Promise<Column[]>
```

**返回值**: 专栏数组

---

### getRoleMembers(groupId, role)

按角色获取星球成员。

**签名**:
```typescript
getRoleMembers(groupId: string | number, role: string): Promise<RoleMembers>
```

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| groupId | string \| number | 是 | 星球 ID |
| role | string | 是 | 角色：owner,partners,guests |

**示例**:
```typescript
const members = await client.groups.getRoleMembers(groupId, 'owner,partners,guests');
console.log(`星主: ${members.owner.name}`);
console.log(`合伙人: ${members.partners.length} 人`);
```

---

### getCheckins(groupId, scope?)

获取星球的打卡项目列表。

**签名**:
```typescript
getCheckins(groupId: string | number, scope?: CheckinScope): Promise<Checkin[]>
```

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| groupId | string \| number | 是 | 星球 ID |
| scope | CheckinScope | 否 | 范围：ongoing/closed/over |

---

### getUnreadCount()

获取所有星球的未读话题数。

**签名**:
```typescript
getUnreadCount(): Promise<UnreadCount[]>
```

**返回值**:
```typescript
interface UnreadCount {
  group_id: number;
  count: number;
}
```

---

### getRecommendations(count?, index?)

获取推荐星球列表。

**签名**:
```typescript
getRecommendations(count?: number, index?: number): Promise<Group[]>
```

---

## 数据模型

### Group

```typescript
interface Group {
  group_id: number;
  name: string;
  description: string;
  background_url: string;
  type: 'free' | 'pay';
  member_count?: number;
  owner?: User;
  create_time: string;
  // ...
}
```

### Hashtag

```typescript
interface Hashtag {
  hashtag_id: number;
  title: string;
  topics_count: number;
  owner: User;
  properties: {
    show_on_timeline: boolean;
    privileged: boolean;
  };
}
```

### Column

```typescript
interface Column {
  column_id: number;
  name: string;
  cover_url: string;
  statistics: {
    topics_count: number;
  };
  create_time: string;
  last_topic_attach_time: string;
}
```

---

## 相关文档

- [API 映射](../design/api-mapping.md#星球相关-grouprequest)
- [话题 API](topic.md)
- [打卡 API](checkin.md)
