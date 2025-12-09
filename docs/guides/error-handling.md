# 错误处理

> zsxq-sdk 错误处理最佳实践

## 概述

SDK 提供了完整的错误处理机制，包括：
- 类型化的异常类
- 原生 API 错误码映射
- 自动重试机制
- 详细的错误信息

---

## 异常类型

### 异常层次结构

```
ZsxqException (基类)
├── NetworkException        # 网络错误
├── AuthException          # 认证错误
│   ├── TokenInvalidException
│   └── TokenExpiredException
├── RateLimitException     # 频率限制
├── PermissionException    # 权限不足
├── ResourceNotFoundException  # 资源不存在
└── BusinessException      # 业务逻辑错误
```

### 导入异常类

```typescript
import {
  ZsxqException,
  NetworkException,
  AuthException,
  TokenInvalidException,
  TokenExpiredException,
  RateLimitException,
  PermissionException,
  ResourceNotFoundException,
  BusinessException
} from 'zsxq-sdk';
```

---

## 基础错误处理

### 简单 try-catch

```typescript
try {
  const groups = await client.groups.list();
} catch (error) {
  if (error instanceof ZsxqException) {
    console.error(`SDK 错误: ${error.code} - ${error.message}`);
  } else {
    throw error;
  }
}
```

### 分类处理

```typescript
try {
  const topic = await client.topics.get(topicId);
} catch (error) {
  if (error instanceof TokenExpiredException) {
    // Token 过期，需要重新获取
    await refreshToken();
    return retry();
  }

  if (error instanceof RateLimitException) {
    // 请求过于频繁，等待后重试
    await sleep(error.retryAfter || 60000);
    return retry();
  }

  if (error instanceof ResourceNotFoundException) {
    // 资源不存在
    return null;
  }

  if (error instanceof PermissionException) {
    // 权限不足
    throw new Error('无权访问此资源');
  }

  if (error instanceof NetworkException) {
    // 网络错误
    throw new Error('网络连接失败，请检查网络');
  }

  throw error;
}
```

---

## 常见错误场景

### 认证错误

```typescript
import { TokenInvalidException, TokenExpiredException } from 'zsxq-sdk';

async function safeApiCall<T>(fn: () => Promise<T>): Promise<T> {
  try {
    return await fn();
  } catch (error) {
    if (error instanceof TokenInvalidException) {
      console.error('Token 无效，请检查 Token 格式');
      throw error;
    }

    if (error instanceof TokenExpiredException) {
      console.error('Token 已过期，请重新获取');
      // 可以在这里触发重新登录流程
      throw error;
    }

    throw error;
  }
}
```

### 频率限制

```typescript
import { RateLimitException } from 'zsxq-sdk';

async function withRateLimitRetry<T>(
  fn: () => Promise<T>,
  maxRetries = 3
): Promise<T> {
  for (let i = 0; i < maxRetries; i++) {
    try {
      return await fn();
    } catch (error) {
      if (error instanceof RateLimitException) {
        const waitTime = error.retryAfter || (i + 1) * 60000;
        console.log(`触发频率限制，等待 ${waitTime}ms 后重试...`);
        await new Promise(resolve => setTimeout(resolve, waitTime));
        continue;
      }
      throw error;
    }
  }
  throw new Error('超过最大重试次数');
}
```

### 资源不存在

```typescript
import { ResourceNotFoundException } from 'zsxq-sdk';

async function getTopic(topicId: string) {
  try {
    return await client.topics.get(topicId);
  } catch (error) {
    if (error instanceof ResourceNotFoundException) {
      console.log('话题不存在或已被删除');
      return null;
    }
    throw error;
  }
}
```

---

## 错误码处理

### 使用错误码

```typescript
import { ZsxqException, ErrorCode } from 'zsxq-sdk';

try {
  const stats = await client.checkins.getStatistics(groupId, checkinId);
} catch (error) {
  if (error instanceof ZsxqException) {
    switch (error.code) {
      case ErrorCode.NOT_JOINED_CHECKIN:
        console.log('用户未参与此打卡项目');
        break;
      case ErrorCode.CHECKIN_NOT_STARTED:
        console.log('打卡项目未开始');
        break;
      case ErrorCode.CHECKIN_ENDED:
        console.log('打卡项目已结束');
        break;
      default:
        console.error(`未知错误: ${error.code}`);
    }
  }
}
```

### 错误码常量

参考 [错误码定义](../design/error-codes.md) 获取完整错误码列表。

---

## 全局错误处理

### 封装统一处理器

```typescript
type ErrorHandler = (error: Error) => void;

const errorHandlers: Map<string, ErrorHandler> = new Map([
  ['TokenExpiredException', () => {
    // 跳转到登录页
    window.location.href = '/login';
  }],
  ['RateLimitException', (error) => {
    // 显示提示
    showToast('请求过于频繁，请稍后再试');
  }],
  ['NetworkException', () => {
    showToast('网络连接失败');
  }],
]);

function handleError(error: Error): void {
  const handler = errorHandlers.get(error.constructor.name);
  if (handler) {
    handler(error);
  } else {
    console.error('未处理的错误:', error);
    showToast('操作失败，请重试');
  }
}
```

### 包装 API 调用

```typescript
class ApiWrapper {
  constructor(private client: ZsxqClient) {}

  async call<T>(fn: (client: ZsxqClient) => Promise<T>): Promise<T | null> {
    try {
      return await fn(this.client);
    } catch (error) {
      handleError(error as Error);
      return null;
    }
  }
}

// 使用
const api = new ApiWrapper(client);
const groups = await api.call(c => c.groups.list());
```

---

## 自动重试配置

### 配置重试策略

```typescript
const client = new ZsxqClientBuilder()
  .setToken(token)
  .setRetry(3)              // 最大重试次数
  .setRetryDelay(1000)      // 重试间隔（毫秒）
  .setRetryCondition((error) => {
    // 自定义重试条件
    return error instanceof NetworkException;
  })
  .build();
```

### 指数退避

```typescript
async function exponentialBackoff<T>(
  fn: () => Promise<T>,
  maxRetries = 5,
  baseDelay = 1000
): Promise<T> {
  for (let i = 0; i < maxRetries; i++) {
    try {
      return await fn();
    } catch (error) {
      if (i === maxRetries - 1) throw error;

      const delay = baseDelay * Math.pow(2, i);
      const jitter = Math.random() * 1000;
      await new Promise(r => setTimeout(r, delay + jitter));
    }
  }
  throw new Error('Unreachable');
}
```

---

## 日志记录

### 结构化日志

```typescript
interface ErrorLog {
  timestamp: string;
  errorType: string;
  errorCode?: number;
  message: string;
  context?: Record<string, unknown>;
  stack?: string;
}

function logError(error: Error, context?: Record<string, unknown>): void {
  const log: ErrorLog = {
    timestamp: new Date().toISOString(),
    errorType: error.constructor.name,
    message: error.message,
    context,
    stack: error.stack,
  };

  if (error instanceof ZsxqException) {
    log.errorCode = error.code;
  }

  console.error(JSON.stringify(log, null, 2));
  // 或发送到日志服务
  // await logService.send(log);
}
```

---

## 最佳实践

### 1. 始终使用类型化异常

```typescript
// 推荐
if (error instanceof TokenExpiredException) { ... }

// 不推荐
if (error.message.includes('token')) { ... }
```

### 2. 提供有意义的用户反馈

```typescript
const errorMessages: Record<string, string> = {
  TokenExpiredException: '登录已过期，请重新登录',
  RateLimitException: '操作太频繁，请稍后再试',
  PermissionException: '您没有权限执行此操作',
  NetworkException: '网络连接失败，请检查网络设置',
};
```

### 3. 不要吞掉未知错误

```typescript
try {
  await someOperation();
} catch (error) {
  if (error instanceof ZsxqException) {
    handleKnownError(error);
  } else {
    // 记录并重新抛出未知错误
    logError(error);
    throw error;
  }
}
```

### 4. 使用 finally 清理资源

```typescript
let loading = true;
try {
  const data = await client.topics.list(groupId);
  return data;
} catch (error) {
  handleError(error);
  return [];
} finally {
  loading = false;
}
```

---

## 相关文档

- [错误码参考](../design/error-codes.md) - 完整错误码列表
- [认证指南](authentication.md) - Token 相关错误处理
- [快速开始](quick-start.md) - 基础使用教程
