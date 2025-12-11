# Java SDK 快速开始

> 5 分钟上手 zsxq-sdk Java 版

## 安装

### Maven

```xml
<dependency>
    <groupId>io.github.yiancode</groupId>
    <artifactId>zsxq-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'io.github.yiancode:zsxq-sdk:1.0.0'
```

---

## 基础使用

### 1. 获取 Token

首先需要获取知识星球的认证 Token。可以通过以下方式获取：

- 浏览器开发者工具抓包
- 手机抓包工具（如 Charles、Fiddler）

Token 格式示例：`D047A423-A...169922C77C`

### 2. 初始化客户端

```java
import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;

ZsxqClient client = new ZsxqClientBuilder()
    .token("your-token-here")
    .build();
```

### 3. 调用 API

```java
// 获取我的星球列表
List<Group> groups = client.groups().list();
System.out.println("已加入 " + groups.size() + " 个星球");

// 获取星球话题
List<Topic> topics = client.topics().list(groups.get(0).getGroupId());
topics.forEach(topic -> System.out.println(topic.getType()));
```

---

## 完整示例

```java
import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.exception.ZsxqException;
import com.zsxq.sdk.model.*;

public class ZsxqExample {
    public static void main(String[] args) {
        // 初始化客户端
        ZsxqClient client = new ZsxqClientBuilder()
            .token("your-token")
            .timeout(5000)
            .retryCount(3)
            .build();

        try {
            // 获取当前用户
            User me = client.users().self();
            System.out.println("欢迎, " + me.getName());

            // 获取我的星球
            List<Group> groups = client.groups().list();
            System.out.println("你加入了 " + groups.size() + " 个星球");

            // 获取第一个星球的最新话题
            if (!groups.isEmpty()) {
                List<Topic> topics = client.topics().list(
                    groups.get(0).getGroupId(),
                    new TopicsRequest.ListTopicsOptions().count(10)
                );

                topics.forEach(topic -> {
                    System.out.println("- " + topic.getType() + ": " + topic.getTopicId());
                });
            }
        } catch (ZsxqException e) {
            System.err.println("SDK 错误: " + e.getCode() + " - " + e.getMessage());
        }
    }
}
```

---

## 配置选项

```java
ZsxqClient client = new ZsxqClientBuilder()
    // 必填：认证 Token
    .token("your-token")

    // 可选：API 基础 URL（默认: https://api.zsxq.com）
    .baseUrl("https://api.zsxq.com")

    // 可选：请求超时（默认: 10000ms）
    .timeout(5000)

    // 可选：重试次数（默认: 3）
    .retryCount(3)

    .build();
```

---

## API 模块概览

SDK 提供以下 API 模块：

| 模块 | 访问方式 | 说明 |
|------|---------|------|
| `groups` | `client.groups()` | 星球管理 |
| `topics` | `client.topics()` | 话题管理 |
| `users` | `client.users()` | 用户管理 |
| `checkins` | `client.checkins()` | 打卡管理 |
| `dashboard` | `client.dashboard()` | 数据面板 |
| `ranking` | `client.ranking()` | 排行榜 |
| `misc` | `client.misc()` | 杂项功能 |

---

## 常用操作

### 星球管理

```java
// 获取已加入的星球列表
List<Group> groups = client.groups().list();

// 获取星球详情
Group group = client.groups().get(groupId);
System.out.println("星球名称: " + group.getName());

// 获取星球统计
Map<String, Object> stats = client.groups().getStatistics(groupId);
System.out.println("话题数: " + stats.get("topics_count"));

// 获取星球专栏
List<Column> columns = client.groups().getColumns(groupId);

// 获取星球菜单配置
List<Menu> menus = client.groups().getMenus(groupId);

// 获取续费信息
RenewalInfo renewal = client.groups().getRenewalInfo(groupId);

// 获取分销信息
DistributionInfo distribution = client.groups().getDistribution(groupId);

// 获取推荐星球
List<Group> recommended = client.groups().getRecommendedGroups();
```

### 话题管理

```java
// 获取最新话题
List<Topic> topics = client.topics().list(groupId);

// 带参数查询
List<Topic> topics = client.topics().list(groupId,
    new TopicsRequest.ListTopicsOptions()
        .count(20)
        .scope("digests")  // all, digests, by_owner
);

// 获取置顶话题
List<Topic> sticky = client.topics().listSticky(groupId);

// 获取话题详情
Topic topic = client.topics().get(topicId);

// 获取话题基础信息
Topic info = client.topics().getInfo(topicId);

// 获取话题打赏列表
List<Reward> rewards = client.topics().getRewards(topicId);

// 获取相关推荐
List<Topic> recommendations = client.topics().getRecommendations(topicId);
```

### 用户管理

```java
// 获取当前用户
User me = client.users().self();

// 获取指定用户
User user = client.users().get(userId);

// 获取用户统计
Map<String, Object> stats = client.users().getStatistics(userId);

// 获取用户创建的星球
List<Group> created = client.users().getCreatedGroups(userId);

// 获取用户动态足迹
List<Topic> footprints = client.users().getFootprints(userId);

// 获取关注者统计
FollowerStatistics followerStats = client.users().getFollowerStats();

// 获取未回答问题摘要
UnansweredQuestionsSummary summary = client.users().getUnansweredQuestionsSummary();

// 获取用户偏好
Map<String, Object> preferences = client.users().getPreferences();
```

### 打卡管理

```java
// 获取打卡项目列表
List<Checkin> checkins = client.checkins().list(groupId);

// 获取进行中的打卡
List<Checkin> ongoing = client.checkins().list(groupId,
    new CheckinsRequest.ListCheckinsOptions().scope("ongoing"));

// 获取打卡详情
Checkin checkin = client.checkins().get(groupId, checkinId);

// 获取打卡统计
CheckinStatistics stats = client.checkins().getStatistics(groupId, checkinId);

// 获取打卡排行榜
List<RankingItem> ranking = client.checkins().getRankingList(groupId, checkinId);

// 获取我的打卡记录
List<Topic> myCheckins = client.checkins().getMyCheckins(groupId, checkinId);

// 获取我的打卡统计
MyCheckinStatistics myStats = client.checkins().getMyStatistics(groupId, checkinId);
```

### 排行榜

```java
// 获取星球排行榜
List<RankingItem> ranking = client.ranking().getGroupRanking(groupId);

// 获取排行统计
RankingStatistics stats = client.ranking().getGroupRankingStats(groupId);

// 获取积分排行榜
List<RankingItem> scoreRanking = client.ranking().getScoreRanking(groupId);

// 获取我的积分统计
Map<String, Object> myScoreStats = client.ranking().getMyScoreStats(groupId);

// 获取邀请排行榜
List<RankingItem> invitationRanking = client.ranking().getInvitationRanking(groupId);

// 获取贡献排行榜
List<RankingItem> contributionRanking = client.ranking().getContributionRanking(groupId);
```

### 数据面板

```java
// 获取星球概览
Map<String, Object> overview = client.dashboard().getOverview(groupId);

// 获取收入概览
Map<String, Object> incomes = client.dashboard().getIncomes(groupId);

// 获取权限配置
Map<String, Object> privileges = client.dashboard().getPrivileges(groupId);

// 获取发票统计
InvoiceStats invoiceStats = client.dashboard().getInvoiceStats();
```

### 杂项功能

```java
// 获取全局配置
GlobalConfig config = client.misc().getGlobalConfig();

// 获取用户动态
List<Activity> activities = client.misc().getActivities();

// 获取 PK 群组信息
PkGroup pkGroup = client.misc().getPkGroup(groupId);
```

---

## 错误处理

```java
import com.zsxq.sdk.exception.*;

try {
    List<Group> groups = client.groups().list();
} catch (TokenExpiredException e) {
    // Token 过期，需要重新获取
    System.out.println("Token 已过期");
} catch (TokenInvalidException e) {
    // Token 无效
    System.out.println("Token 无效");
} catch (RateLimitException e) {
    // 请求过于频繁
    System.out.println("请求频率过高，请稍后重试");
} catch (PermissionException e) {
    // 权限不足
    System.out.println("权限不足: " + e.getMessage());
} catch (ResourceNotFoundException e) {
    // 资源不存在
    System.out.println("资源不存在: " + e.getMessage());
} catch (NetworkException e) {
    // 网络错误
    System.out.println("网络错误: " + e.getMessage());
} catch (ZsxqException e) {
    // 其他 SDK 错误
    System.err.println("错误码: " + e.getCode() + ", 消息: " + e.getMessage());
}
```

### 异常层次

```
ZsxqException (基类)
├── AuthException           # 认证错误 (1xxxx)
│   ├── TokenInvalidException
│   └── TokenExpiredException
├── PermissionException     # 权限错误 (2xxxx)
├── ResourceNotFoundException  # 资源不存在 (3xxxx)
├── RateLimitException      # 限流 (4xxxx)
└── NetworkException        # 网络错误 (7xxxx)
```

---

## 模型类

SDK 提供完整的模型类定义：

```java
import com.zsxq.sdk.model.*;

// 星球相关
Group group;                    // 星球
Column column;                  // 专栏
Menu menu;                      // 菜单
RoleMembers roleMembers;        // 角色成员
ActivitySummary activitySummary; // 活跃摘要
RenewalInfo renewalInfo;        // 续费信息
DistributionInfo distributionInfo; // 分销信息
CustomTag customTag;            // 自定义标签
ScheduledJob scheduledJob;      // 定时任务
GroupWarning groupWarning;      // 风险预警

// 话题相关
Topic topic;                    // 话题
Reward reward;                  // 打赏

// 用户相关
User user;                      // 用户
FollowerStatistics followerStats; // 关注者统计
ContributionStatistics contribStats; // 贡献统计
AchievementSummary achievement; // 成就摘要
WeeklyRanking weeklyRanking;   // 周榜排名
PreferenceCategory prefCategory; // 推荐偏好分类

// 打卡相关
Checkin checkin;               // 打卡项目
CheckinStatistics checkinStats; // 打卡统计
MyCheckinStatistics myCheckinStats; // 我的打卡统计
DailyStatistics dailyStats;    // 每日统计

// 排行榜
RankingItem rankingItem;       // 排行项
RankingStatistics rankingStats; // 排行统计
ScoreboardSettings settings;   // 积分榜设置

// 其他
GlobalConfig globalConfig;     // 全局配置
Activity activity;             // 动态
PkGroup pkGroup;              // PK群组
InvoiceStats invoiceStats;    // 发票统计
```

---

## 下一步

- [认证指南](authentication.md) - 了解更多认证细节
- [错误处理](error-handling.md) - 错误处理最佳实践
- [测试状态](../TESTING_STATUS.md) - 查看 API 测试覆盖情况
