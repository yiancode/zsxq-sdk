# 认证指南

> 知识星球 Token 获取与管理

## 概述

知识星球 API 使用 Token 进行身份认证。Token 是一个唯一标识符，用于验证用户身份和授权 API 访问。

---

## Token 格式

Token 格式示例：
```
D047A423-A9E1-4C8B-B123-169922C77C3F
```

特点：
- UUID 格式（大写字母 + 数字 + 连字符）
- 长度固定 36 字符
- 由知识星球服务器生成

---

## 获取 Token

### 方法一：浏览器开发者工具

1. 打开 [知识星球网页版](https://wx.zsxq.com)
2. 登录你的账号
3. 按 `F12` 打开开发者工具
4. 切换到 `Network`（网络）选项卡
5. 刷新页面或进行任意操作
6. 找到任意 `api.zsxq.com` 请求
7. 在请求头中找到 `Cookie`，提取 `zsxq_access_token` 的值

```
Cookie: zsxq_access_token=D047A423-A9E1-4C8B-B123-169922C77C3F; ...
```

### 方法二：手机抓包工具

使用 Charles、Fiddler 或 mitmproxy 等工具：

1. 配置手机代理到抓包工具
2. 安装并信任 HTTPS 证书
3. 打开知识星球 App
4. 在抓包工具中过滤 `api.zsxq.com` 请求
5. 从请求头提取 Token

### 方法三：App 缓存（Android）

> 需要 Root 权限

```bash
# Token 存储位置
/data/data/com.unnoo.quan/shared_prefs/zsxq_prefs.xml
```

---

## 使用 Token

### 基础使用

```typescript
import { ZsxqClientBuilder } from 'zsxq-sdk';

const client = new ZsxqClientBuilder()
  .setToken('D047A423-A9E1-4C8B-B123-169922C77C3F')
  .build();
```

### 环境变量配置

推荐将 Token 存储在环境变量中：

```bash
# .env
ZSXQ_TOKEN=D047A423-A9E1-4C8B-B123-169922C77C3F
```

```typescript
const client = new ZsxqClientBuilder()
  .setToken(process.env.ZSXQ_TOKEN!)
  .build();
```

### 多账号管理

```typescript
// 创建多个客户端实例
const clients = {
  account1: new ZsxqClientBuilder()
    .setToken(process.env.ZSXQ_TOKEN_1!)
    .build(),
  account2: new ZsxqClientBuilder()
    .setToken(process.env.ZSXQ_TOKEN_2!)
    .build(),
};

// 使用不同账号
const groups1 = await clients.account1.groups.list();
const groups2 = await clients.account2.groups.list();
```

---

## Token 有效期

### 过期时间

- Token 有效期通常为 **30 天**
- 每次使用 App 登录会刷新 Token
- 长时间不使用可能导致 Token 失效

### 检测过期

```typescript
import { TokenExpiredException } from 'zsxq-sdk';

try {
  const me = await client.users.getSelf();
} catch (error) {
  if (error instanceof TokenExpiredException) {
    console.log('Token 已过期，请重新获取');
    // 触发重新获取 Token 的流程
  }
}
```

### 自动刷新策略

```typescript
class TokenManager {
  private client: ZsxqClient;
  private token: string;

  constructor(initialToken: string) {
    this.token = initialToken;
    this.client = this.createClient();
  }

  private createClient() {
    return new ZsxqClientBuilder()
      .setToken(this.token)
      .build();
  }

  async execute<T>(fn: (client: ZsxqClient) => Promise<T>): Promise<T> {
    try {
      return await fn(this.client);
    } catch (error) {
      if (error instanceof TokenExpiredException) {
        // 通知用户重新获取 Token
        throw new Error('Token 已过期，请重新获取并更新');
      }
      throw error;
    }
  }
}
```

---

## 安全建议

### 1. 不要硬编码 Token

```typescript
// 错误示例
const client = new ZsxqClientBuilder()
  .setToken('D047A423-A9E1-4C8B-B123-169922C77C3F') // 不要这样做！
  .build();

// 正确示例
const client = new ZsxqClientBuilder()
  .setToken(process.env.ZSXQ_TOKEN!)
  .build();
```

### 2. 使用 .gitignore 忽略配置文件

```gitignore
# .gitignore
.env
.env.local
*.env
```

### 3. 限制 Token 权限

- 仅在需要时获取 Token
- 定期更换 Token
- 不要在公共代码库中暴露 Token

### 4. 服务端存储

如果需要持久化存储 Token：

```typescript
// 加密存储示例
import { createCipheriv, createDecipheriv, randomBytes } from 'crypto';

class SecureTokenStorage {
  private readonly algorithm = 'aes-256-gcm';
  private readonly key: Buffer;

  constructor(encryptionKey: string) {
    this.key = Buffer.from(encryptionKey, 'hex');
  }

  encrypt(token: string): string {
    const iv = randomBytes(16);
    const cipher = createCipheriv(this.algorithm, this.key, iv);
    const encrypted = Buffer.concat([
      cipher.update(token, 'utf8'),
      cipher.final()
    ]);
    const authTag = cipher.getAuthTag();
    return Buffer.concat([iv, authTag, encrypted]).toString('base64');
  }

  decrypt(encryptedToken: string): string {
    const data = Buffer.from(encryptedToken, 'base64');
    const iv = data.subarray(0, 16);
    const authTag = data.subarray(16, 32);
    const encrypted = data.subarray(32);
    const decipher = createDecipheriv(this.algorithm, this.key, iv);
    decipher.setAuthTag(authTag);
    return decipher.update(encrypted) + decipher.final('utf8');
  }
}
```

---

## 常见问题

### Q: Token 无效怎么办？

检查以下情况：
1. Token 格式是否正确（UUID 格式）
2. Token 是否过期
3. 是否在其他设备重新登录导致 Token 失效

### Q: 如何判断 Token 是否有效？

```typescript
async function validateToken(token: string): Promise<boolean> {
  try {
    const client = new ZsxqClientBuilder()
      .setToken(token)
      .build();
    await client.users.getSelf();
    return true;
  } catch {
    return false;
  }
}
```

### Q: 多设备登录会影响 Token 吗？

是的，在新设备登录可能会导致旧 Token 失效。建议：
- 使用固定设备获取 Token
- 监控 Token 状态，及时更新

---

## 相关文档

- [快速开始](quick-start.md) - 基础使用教程
- [错误处理](error-handling.md) - Token 相关错误处理
- [错误码参考](../design/error-codes.md) - 认证相关错误码
