# zsxq-sdk-java

> 知识星球 Java SDK - 轻量级、类型安全的 API 客户端

[![Java Version](https://img.shields.io/badge/java-%3E%3D17-blue)](https://openjdk.org/)
[![License](https://img.shields.io/badge/license-MIT-green)](LICENSE)

## 特性

- **类型安全** - 完整的 Java 类型定义
- **Builder 模式** - 灵活的客户端配置
- **模块化设计** - 按需使用各功能模块
- **自动重试** - 内置网络错误重试机制
- **签名认证** - 内置 HMAC-SHA1 请求签名

## 安装

### Maven

```xml
<dependency>
    <groupId>com.zsxq</groupId>
    <artifactId>zsxq-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'com.zsxq:zsxq-sdk:1.0.0'
```

## 快速开始

```java
import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.model.Group;
import com.zsxq.sdk.model.Topic;

// 创建客户端
ZsxqClient client = new ZsxqClientBuilder()
    .token(System.getenv("ZSXQ_TOKEN"))
    .timeout(10000)
    .retry(3)
    .build();

// 获取我的星球列表
List<Group> groups = client.groups().list();

// 获取星球话题
List<Topic> topics = client.topics().list(groupId);

// 获取当前用户信息
User me = client.users().self();
```

## 核心功能

### 星球管理

```java
// 获取星球列表
List<Group> groups = client.groups().list();

// 获取星球详情
Group group = client.groups().get(groupId);

// 获取星球统计
Map<String, Object> stats = client.groups().getStatistics(groupId);
```

### 话题操作

```java
// 获取话题列表
List<Topic> topics = client.topics().list(groupId);

// 带参数的话题列表
List<Topic> topics = client.topics().list(groupId,
    new TopicsRequest.ListTopicsOptions()
        .count(20)
        .scope("all"));

// 获取话题详情
Topic topic = client.topics().get(topicId);
```

### 训练营（打卡）

```java
// 获取打卡项目列表
List<Checkin> checkins = client.checkins().list(groupId);

// 创建训练营
CheckinsRequest.CreateCheckinParams params = new CheckinsRequest.CreateCheckinParams()
    .title("7天打卡挑战")           // 训练营标题
    .text("每天完成一个任务")        // 训练营描述
    .checkinDays(7)                // 打卡天数
    .type("accumulated")           // 打卡类型: accumulated(累计) / continuous(连续)
    .showTopicsOnTimeline(false)   // 是否在时间线展示
    .expirationTime("2025-12-31T23:59:59.000+0800");  // 截止时间

Checkin checkin = client.checkins().create(groupId, params);
System.out.println("创建成功: " + checkin.getCheckinId());

// 长期有效的训练营
CheckinsRequest.CreateCheckinParams longTermParams = new CheckinsRequest.CreateCheckinParams()
    .title("每日学习打卡")
    .text("持续学习，每天进步")
    .checkinDays(21)
    .type("accumulated")
    .longPeriod();  // 设置为长期有效

Checkin longTermCheckin = client.checkins().create(groupId, longTermParams);
```

## 高级配置

```java
// 自定义签名密钥
ZsxqClient client = new ZsxqClientBuilder()
    .token("your-token")
    .signatureKey("custom-secret-key")  // 自定义签名密钥
    .build();

// 禁用签名（仅用于测试）
ZsxqClient testClient = new ZsxqClientBuilder()
    .token("your-token")
    .disableSignature()  // 禁用签名
    .build();

// 完整配置示例
ZsxqClient client = new ZsxqClientBuilder()
    .token("your-token")
    .baseUrl("https://api.zsxq.com")
    .timeout(15000)
    .retryCount(3)
    .retryDelay(1000)
    .deviceId("custom-device-id")
    .appVersion("2.83.0")
    .build();
```

## 错误处理

```java
import com.zsxq.sdk.exception.*;

try {
    List<Group> groups = client.groups().list();
} catch (TokenExpiredException e) {
    // Token 过期，需要重新获取
    System.out.println("Token 已过期");
} catch (ZsxqException e) {
    // 其他 API 错误
    System.err.println("错误码: " + e.getCode() + ", 信息: " + e.getMessage());
}
```

## 开发

```bash
# 编译
mvn compile

# 测试
mvn test

# 集成测试
ZSXQ_TOKEN="your-token" ZSXQ_GROUP_ID="group-id" mvn test -Dtest=IntegrationTest

# 打包
mvn package

# 安装到本地仓库
mvn install
```

## 许可证

MIT License

---

**注意**: 本项目仅供学习和研究使用，请遵守知识星球的服务条款。
