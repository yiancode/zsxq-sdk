# 话题 API

> TopicRequest 类的 API 文档

## 概述

`TopicRequest` 提供话题（帖子/内容）相关的所有操作。

```typescript
// 通过客户端访问
const topics = client.topics;
```

---

## 方法列表

### list(groupId, options?)

获取星球的话题列表。

**签名**:
```typescript
list(groupId: string | number, options?: TopicListOptions): Promise<Topic[]>
```

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| groupId | string \| number | 是 | 星球 ID |
| options | TopicListOptions | 否 | 查询选项 |

**选项**:
```typescript
interface TopicListOptions {
  count?: number;         // 返回数量，默认 20
  scope?: 'all' | 'digests' | 'by_owner';  // 范围
  direction?: 'forward' | 'backward';       // 分页方向
  begin_time?: string;    // 开始时间（ISO 8601）
  end_time?: string;      // 结束时间（ISO 8601）
  with_invisibles?: boolean;  // 是否包含隐藏话题
}
```

**示例**:
```typescript
// 获取最新 10 条话题
const topics = await client.topics.list(groupId, { count: 10 });

// 获取精华话题
const digests = await client.topics.list(groupId, { scope: 'digests' });

// 只看星主
const ownerTopics = await client.topics.list(groupId, { scope: 'by_owner' });
```

---

### get(topicId)

获取话题详情。

**签名**:
```typescript
get(topicId: string | number): Promise<Topic>
```

**参数**:
| 参数 | 类型 | 必填 | 说明 |
|-----|------|------|------|
| topicId | string \| number | 是 | 话题 ID |

**示例**:
```typescript
const topic = await client.topics.get('5125585155255524');
console.log(topic.talk?.text);
```

---

### getInfo(topicId)

获取话题信息（轻量级）。

**签名**:
```typescript
getInfo(topicId: string | number): Promise<TopicInfo>
```

---

### getComments(topicId, options?)

获取话题评论列表。

**签名**:
```typescript
getComments(topicId: string | number, options?: CommentOptions): Promise<Comment[]>
```

**选项**:
```typescript
interface CommentOptions {
  count?: number;         // 返回数量，默认 30
  sort?: 'asc' | 'desc';  // 排序方式
  sort_type?: 'by_create_time';  // 排序字段
  with_sticky?: boolean;  // 是否包含置顶评论
}
```

**示例**:
```typescript
const comments = await client.topics.getComments(topicId, {
  sort: 'asc',
  count: 30,
  with_sticky: true
});
```

---

### getRewards(topicId)

获取话题打赏记录。

**签名**:
```typescript
getRewards(topicId: string | number): Promise<Reward[]>
```

---

### getRecommendations(topicId)

获取相关推荐话题。

**签名**:
```typescript
getRecommendations(topicId: string | number): Promise<Topic[]>
```

---

### getByHashtag(hashtagId, options?)

按标签获取话题列表。

**签名**:
```typescript
getByHashtag(hashtagId: string | number, options?: {
  count?: number;
}): Promise<Topic[]>
```

**示例**:
```typescript
const topics = await client.topics.getByHashtag('28844882581111', { count: 10 });
```

---

### getByColumn(groupId, columnId, options?)

按专栏获取话题列表。

**签名**:
```typescript
getByColumn(groupId: string | number, columnId: string | number, options?: {
  count?: number;
}): Promise<Topic[]>
```

**示例**:
```typescript
const topics = await client.topics.getByColumn(groupId, columnId, { count: 100 });
```

---

## 数据模型

### Topic

```typescript
interface Topic {
  topic_id: number;
  topic_uid: string;
  group: Group;
  type: 'talk' | 'task' | 'q&a' | 'solution';
  create_time: string;

  // 类型特定内容
  talk?: TalkContent;
  task?: TaskContent;
  question?: QuestionContent;
  solution?: SolutionContent;

  // 统计信息
  likes_count?: number;
  comments_count?: number;
  rewards_count?: number;
  reading_count?: number;

  // 状态
  digested?: boolean;
  sticky?: boolean;
}
```

### TalkContent

```typescript
interface TalkContent {
  owner: User;
  text?: string;
  images?: Image[];
  files?: File[];
  article?: Article;
}
```

### Comment

```typescript
interface Comment {
  comment_id: number;
  owner: User;
  text: string;
  create_time: string;
  likes_count: number;
  repliee?: User;
  sticky?: boolean;
}
```

---

## 话题类型

| 类型 | 说明 | 内容字段 |
|-----|------|---------|
| `talk` | 发言/帖子 | `talk` |
| `task` | 作业 | `task` |
| `q&a` | 提问 | `question` |
| `solution` | 问答回复 | `solution` |

---

## 相关文档

- [API 映射](../design/api-mapping.md#话题相关-topicrequest)
- [星球 API](group.md)
- [用户 API](user.md)
