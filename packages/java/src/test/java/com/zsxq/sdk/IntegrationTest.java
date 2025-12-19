package com.zsxq.sdk;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.exception.ZsxqException;
import com.zsxq.sdk.exception.NetworkException;
import com.zsxq.sdk.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 集成测试 - 需要真实 Token 和 Group ID
 *
 * 运行方式:
 * ZSXQ_TOKEN="xxx" ZSXQ_GROUP_ID="xxx" mvn test -Dtest=IntegrationTest
 *
 * 注意: 部分测试可能会因星球未开启相关功能而跳过（如打卡、排行榜等）
 */
@EnabledIfEnvironmentVariable(named = "ZSXQ_TOKEN", matches = ".+")
public class IntegrationTest {

    private static ZsxqClient client;
    private static String groupId;

    @BeforeAll
    static void setup() {
        String token = System.getenv("ZSXQ_TOKEN");
        groupId = System.getenv("ZSXQ_GROUP_ID");

        assertNotNull(token, "ZSXQ_TOKEN 环境变量未设置");
        assertNotNull(groupId, "ZSXQ_GROUP_ID 环境变量未设置");

        client = new ZsxqClientBuilder()
                .token(token)
                .build();
    }

    // ========== 核心 API 测试（必须通过）==========

    @Test
    void testGetCurrentUser() {
        User user = client.users().self();
        assertNotNull(user);
        assertNotNull(user.getUserId());
        assertNotNull(user.getName());
        System.out.println("✅ 当前用户: " + user.getName() + " (ID: " + user.getUserId() + ")");
    }

    @Test
    void testGetGroups() {
        List<Group> groups = client.groups().list();
        assertNotNull(groups);
        assertFalse(groups.isEmpty());
        System.out.println("✅ 已加入 " + groups.size() + " 个星球");
        for (Group group : groups) {
            System.out.println("   - " + group.getName() + " (ID: " + group.getGroupId() + ")");
        }
    }

    @Test
    void testGetGroupDetail() {
        Group group = client.groups().get(groupId);
        assertNotNull(group);
        assertNotNull(group.getName());
        System.out.println("✅ 星球详情: " + group.getName());
        System.out.println("   - 成员数: " + group.getMemberCount());
        System.out.println("   - 类型: " + group.getType());
    }

    @Test
    void testGetGroupStatistics() {
        try {
            Map<String, Object> stats = client.groups().getStatistics(groupId);
            assertNotNull(stats);
            System.out.println("✅ 星球统计: " + stats);
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取星球统计失败: " + e.getMessage());
        }
    }

    @Test
    void testGetTopics() {
        try {
            List<Topic> topics = client.topics().list(groupId);
            assertNotNull(topics);
            System.out.println("✅ 获取到 " + topics.size() + " 个话题");
            if (!topics.isEmpty()) {
                Topic first = topics.get(0);
                System.out.println("   - 第一个话题ID: " + first.getTopicId());
                System.out.println("   - 类型: " + first.getType());
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取话题列表失败: " + e.getMessage());
        }
    }

    @Test
    void testGetUserStatistics() {
        User user = client.users().self();
        Map<String, Object> stats = client.users().getStatistics(user.getUserId());
        assertNotNull(stats);
        System.out.println("✅ 用户统计: " + stats);
    }

    // ========== 星球功能测试 ==========

    @Test
    void testGetGroupMenus() {
        try {
            List<Menu> menus = client.groups().getMenus(groupId);
            assertNotNull(menus);
            System.out.println("✅ 获取到 " + menus.size() + " 个菜单配置");
            for (Menu menu : menus) {
                System.out.println("   - " + menu.getName() + " (type: " + menu.getType() + ")");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取菜单配置失败: " + e.getMessage());
        }
    }

    @Test
    void testGetGroupRoleMembers() {
        try {
            RoleMembers roleMembers = client.groups().getRoleMembers(groupId);
            assertNotNull(roleMembers);
            System.out.println("✅ 星球角色成员:");
            if (roleMembers.getOwner() != null) {
                System.out.println("   - 星主: " + roleMembers.getOwner().getName());
            }
            if (roleMembers.getPartners() != null) {
                System.out.println("   - 合伙人数: " + roleMembers.getPartners().size());
            }
            if (roleMembers.getAdmins() != null) {
                System.out.println("   - 管理员数: " + roleMembers.getAdmins().size());
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取角色成员失败（可能无权限）: " + e.getMessage());
        }
    }

    @Test
    void testGetGroupColumns() {
        List<Column> columns = client.groups().getColumns(groupId);
        assertNotNull(columns);
        System.out.println("✅ 获取到 " + columns.size() + " 个专栏");
        for (Column column : columns) {
            System.out.println("   - " + column.getName() + " (话题数: " + column.getTopicsCount() + ")");
        }
    }

    @Test
    void testGetGroupColumnsSummary() {
        Map<String, Object> summary = client.groups().getColumnsSummary(groupId);
        assertNotNull(summary);
        System.out.println("✅ 专栏汇总: " + summary);
    }

    @Test
    void testGetMemberActivitySummary() {
        try {
            User user = client.users().self();
            ActivitySummary summary = client.groups().getMemberActivitySummary(groupId, user.getUserId());
            if (summary != null) {
                System.out.println("✅ 成员活跃摘要:");
                System.out.println("   - 发布话题数: " + summary.getTopicsCount());
                System.out.println("   - 评论数: " + summary.getCommentsCount());
                System.out.println("   - 获赞数: " + summary.getLikesReceived());
            } else {
                System.out.println("⚠️ 成员活跃摘要为空");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取成员活跃摘要失败: " + e.getMessage());
        }
    }

    @Test
    void testGetStickyTopics() {
        try {
            List<Topic> stickyTopics = client.topics().listSticky(groupId);
            assertNotNull(stickyTopics);
            System.out.println("✅ 获取到 " + stickyTopics.size() + " 个置顶话题");
            for (Topic topic : stickyTopics) {
                System.out.println("   - Topic ID: " + topic.getTopicId() + ", Type: " + topic.getType());
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取置顶话题失败: " + e.getMessage());
        }
    }

    @Test
    void testGetTopicInfo() {
        try {
            List<Topic> topics = client.topics().list(groupId);
            if (!topics.isEmpty()) {
                Topic firstTopic = topics.get(0);
                Topic info = client.topics().getInfo(firstTopic.getTopicId());
                assertNotNull(info);
                System.out.println("✅ 话题基础信息:");
                System.out.println("   - Topic ID: " + info.getTopicId());
                System.out.println("   - Type: " + info.getType());
            } else {
                System.out.println("⚠️ 没有话题可测试");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取话题信息失败: " + e.getMessage());
        }
    }

    @Test
    void testGetTopicRewards() {
        try {
            List<Topic> topics = client.topics().list(groupId);
            if (!topics.isEmpty()) {
                Topic firstTopic = topics.get(0);
                List<Reward> rewards = client.topics().getRewards(firstTopic.getTopicId());
                assertNotNull(rewards);
                System.out.println("✅ 话题打赏数: " + rewards.size());
                for (Reward reward : rewards) {
                    if (reward.getUser() != null) {
                        System.out.println("   - " + reward.getUser().getName() + " 打赏 " + reward.getAmount() + " 元");
                    } else {
                        System.out.println("   - 匿名用户打赏 " + reward.getAmount() + " 元");
                    }
                }
            } else {
                System.out.println("⚠️ 没有话题可测试");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取话题打赏失败: " + e.getMessage());
        }
    }

    @Test
    void testGetTopicRecommendations() {
        try {
            List<Topic> topics = client.topics().list(groupId);
            if (!topics.isEmpty()) {
                Topic firstTopic = topics.get(0);
                List<Topic> recommendations = client.topics().getRecommendations(firstTopic.getTopicId());
                assertNotNull(recommendations);
                System.out.println("✅ 相关推荐话题数: " + recommendations.size());
            } else {
                System.out.println("⚠️ 没有话题可测试");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取话题推荐失败: " + e.getMessage());
        }
    }

    // ========== 排行榜功能测试（依赖星球开启排行功能）==========

    @Test
    void testGetGroupRanking() {
        try {
            List<RankingItem> ranking = client.ranking().getGroupRanking(groupId);
            assertNotNull(ranking);
            System.out.println("✅ 获取到 " + ranking.size() + " 条排行记录");
            for (int i = 0; i < Math.min(3, ranking.size()); i++) {
                RankingItem item = ranking.get(i);
                System.out.println("   - #" + (i + 1) + " " + item.getUser().getName());
            }
        } catch (NetworkException e) {
            System.out.println("⚠️ 星球排行榜功能未开启（404）");
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取星球排行失败: " + e.getMessage());
        }
    }

    @Test
    void testGetGroupRankingStats() {
        try {
            RankingStatistics stats = client.ranking().getGroupRankingStats(groupId);
            if (stats != null) {
                System.out.println("✅ 排行统计:");
                System.out.println("   - 总人数: " + stats.getTotalCount());
                System.out.println("   - 我的排名: " + stats.getMyRank());
            } else {
                System.out.println("⚠️ 排行统计为空");
            }
        } catch (NetworkException e) {
            System.out.println("⚠️ 排行榜统计功能未开启（404）");
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取排行统计失败: " + e.getMessage());
        }
    }

    @Test
    void testGetScoreRanking() {
        try {
            List<RankingItem> ranking = client.ranking().getScoreRanking(groupId);
            assertNotNull(ranking);
            System.out.println("✅ 获取到 " + ranking.size() + " 条积分排行");
        } catch (NetworkException e) {
            System.out.println("⚠️ 积分排行功能未开启（404）");
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取积分排行失败: " + e.getMessage());
        }
    }

    @Test
    void testGetMyScoreStats() {
        try {
            Map<String, Object> stats = client.ranking().getMyScoreStats(groupId);
            assertNotNull(stats);
            System.out.println("✅ 我的积分统计: " + stats);
        } catch (NetworkException e) {
            System.out.println("⚠️ 我的积分统计功能未开启（404）");
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取积分统计失败: " + e.getMessage());
        }
    }

    @Test
    void testGetScoreboardSettings() {
        try {
            ScoreboardSettings settings = client.ranking().getScoreboardSettings(groupId);
            if (settings != null) {
                System.out.println("✅ 积分榜设置:");
                System.out.println("   - 启用: " + settings.getEnabled());
                System.out.println("   - 名称: " + settings.getName());
            } else {
                System.out.println("⚠️ 积分榜设置为空");
            }
        } catch (NetworkException e) {
            System.out.println("⚠️ 积分榜设置功能未开启（404）");
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取积分榜设置失败: " + e.getMessage());
        }
    }

    @Test
    void testGetInvitationRanking() {
        try {
            List<RankingItem> ranking = client.ranking().getInvitationRanking(groupId);
            assertNotNull(ranking);
            System.out.println("✅ 获取到 " + ranking.size() + " 条邀请排行");
        } catch (NetworkException e) {
            System.out.println("⚠️ 邀请排行功能未开启（404）");
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取邀请排行失败: " + e.getMessage());
        }
    }

    @Test
    void testGetContributionRanking() {
        try {
            List<RankingItem> ranking = client.ranking().getContributionRanking(groupId);
            assertNotNull(ranking);
            System.out.println("✅ 获取到 " + ranking.size() + " 条贡献排行");
        } catch (NetworkException e) {
            System.out.println("⚠️ 贡献排行功能未开启（404）");
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取贡献排行失败: " + e.getMessage());
        }
    }

    // ========== 用户功能测试 ==========

    @Test
    void testGetAvatarUrl() {
        try {
            User user = client.users().self();
            String avatarUrl = client.users().getAvatarUrl(user.getUserId());
            System.out.println("✅ 用户头像URL: " + (avatarUrl != null ? avatarUrl.substring(0, Math.min(50, avatarUrl.length())) + "..." : "null"));
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取头像URL失败: " + e.getMessage());
        }
    }

    @Test
    void testGetGroupFootprints() {
        try {
            User user = client.users().self();
            List<Group> footprints = client.users().getGroupFootprints(user.getUserId());
            assertNotNull(footprints);
            System.out.println("✅ 获取到 " + footprints.size() + " 条星球足迹");
        } catch (NetworkException e) {
            System.out.println("⚠️ 星球足迹功能不可用（404）");
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取星球足迹失败: " + e.getMessage());
        }
    }

    @Test
    void testGetApplyingGroups() {
        try {
            List<Group> groups = client.users().getApplyingGroups();
            assertNotNull(groups);
            System.out.println("✅ 申请中的星球数: " + groups.size());
        } catch (NetworkException e) {
            System.out.println("⚠️ 申请中星球功能不可用（404）");
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取申请中星球失败: " + e.getMessage());
        }
    }

    @Test
    void testGetInviter() {
        try {
            Inviter inviter = client.users().getInviter(groupId);
            if (inviter != null && inviter.getUser() != null) {
                System.out.println("✅ 邀请人: " + inviter.getUser().getName());
            } else {
                System.out.println("⚠️ 无邀请人信息");
            }
        } catch (NetworkException e) {
            System.out.println("⚠️ 邀请人信息不可用（404）");
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取邀请人失败: " + e.getMessage());
        }
    }

    @Test
    void testGetCoupons() {
        try {
            List<Coupon> coupons = client.users().getCoupons();
            assertNotNull(coupons);
            System.out.println("✅ 获取到 " + coupons.size() + " 张优惠券");
        } catch (NetworkException e) {
            System.out.println("⚠️ 优惠券功能不可用（404）");
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取优惠券失败: " + e.getMessage());
        }
    }

    @Test
    void testGetRemarks() {
        try {
            List<Remark> remarks = client.users().getRemarks();
            assertNotNull(remarks);
            System.out.println("✅ 获取到 " + remarks.size() + " 条备注");
        } catch (NetworkException e) {
            System.out.println("⚠️ 备注功能不可用（404）");
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取备注失败: " + e.getMessage());
        }
    }

    @Test
    void testGetRecommendedFollows() {
        try {
            List<User> users = client.users().getRecommendedFollows();
            assertNotNull(users);
            System.out.println("✅ 推荐关注用户数: " + users.size());
        } catch (NetworkException e) {
            System.out.println("⚠️ 推荐关注功能不可用（404）");
        } catch (ZsxqException e) {
            System.out.println("⚠️ 获取推荐关注失败: " + e.getMessage());
        }
    }

    // ========== 打卡模块测试（依赖星球开启打卡功能）==========

    @Test
    void testGetCheckinList() {
        try {
            List<Checkin> checkins = client.checkins().list(groupId);
            assertNotNull(checkins);
            System.out.println("✅ 获取到 " + checkins.size() + " 个打卡项目");
            for (Checkin checkin : checkins) {
                System.out.println("   - " + checkin.getTitle() + " (ID: " + checkin.getCheckinId() + ", 状态: " + checkin.getStatus() + ")");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 打卡功能未开启或无权限: " + e.getMessage());
        }
    }

    @Test
    void testGetCheckinDailyStatistics() {
        try {
            List<Checkin> checkins = client.checkins().list(groupId);
            if (!checkins.isEmpty()) {
                Checkin firstCheckin = checkins.get(0);
                List<DailyStatistics> stats = client.checkins().getDailyStatistics(groupId, String.valueOf(firstCheckin.getCheckinId()));
                assertNotNull(stats);
                System.out.println("✅ 获取到 " + stats.size() + " 条每日打卡统计");
            } else {
                System.out.println("⚠️ 没有打卡项目可测试");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 打卡统计功能不可用: " + e.getMessage());
        }
    }

    @Test
    void testGetCheckinJoinedUsers() {
        try {
            List<Checkin> checkins = client.checkins().list(groupId);
            if (!checkins.isEmpty()) {
                Checkin firstCheckin = checkins.get(0);
                List<User> users = client.checkins().getJoinedUsers(groupId, String.valueOf(firstCheckin.getCheckinId()));
                assertNotNull(users);
                System.out.println("✅ 打卡参与用户数: " + users.size());
            } else {
                System.out.println("⚠️ 没有打卡项目可测试");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 打卡参与用户功能不可用: " + e.getMessage());
        }
    }

    @Test
    void testGetMyCheckins() {
        try {
            List<Checkin> checkins = client.checkins().list(groupId);
            if (!checkins.isEmpty()) {
                Checkin firstCheckin = checkins.get(0);
                List<Topic> myCheckins = client.checkins().getMyCheckins(groupId, String.valueOf(firstCheckin.getCheckinId()));
                assertNotNull(myCheckins);
                System.out.println("✅ 我的打卡记录数: " + myCheckins.size());
            } else {
                System.out.println("⚠️ 没有打卡项目可测试");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 我的打卡记录功能不可用: " + e.getMessage());
        }
    }

    @Test
    void testGetMyCheckinDays() {
        try {
            List<Checkin> checkins = client.checkins().list(groupId);
            if (!checkins.isEmpty()) {
                Checkin firstCheckin = checkins.get(0);
                List<String> days = client.checkins().getMyCheckinDays(groupId, String.valueOf(firstCheckin.getCheckinId()));
                assertNotNull(days);
                System.out.println("✅ 我的打卡日期数: " + days.size());
            } else {
                System.out.println("⚠️ 没有打卡项目可测试");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 我的打卡日期功能不可用: " + e.getMessage());
        }
    }

    @Test
    void testGetMyCheckinStatistics() {
        try {
            List<Checkin> checkins = client.checkins().list(groupId);
            if (!checkins.isEmpty()) {
                Checkin firstCheckin = checkins.get(0);
                MyCheckinStatistics stats = client.checkins().getMyStatistics(groupId, String.valueOf(firstCheckin.getCheckinId()));
                if (stats != null) {
                    System.out.println("✅ 我的打卡统计:");
                    System.out.println("   - 总打卡次数: " + stats.getTotalCheckinCount());
                    System.out.println("   - 连续天数: " + stats.getContinuousDays());
                } else {
                    System.out.println("⚠️ 我的打卡统计为空");
                }
            } else {
                System.out.println("⚠️ 没有打卡项目可测试");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 我的打卡统计功能不可用: " + e.getMessage());
        }
    }

    // ========== 数据面板测试 ==========

    @Test
    void testGetDashboardPrivileges() {
        try {
            Map<String, Object> privileges = client.dashboard().getPrivileges(groupId);
            assertNotNull(privileges);
            System.out.println("✅ 获取到 " + privileges.size() + " 个权限配置");
        } catch (ZsxqException e) {
            System.out.println("⚠️ 权限配置功能不可用: " + e.getMessage());
        }
    }

    @Test
    void testGetInvoiceStats() {
        try {
            InvoiceStats stats = client.dashboard().getInvoiceStats();
            if (stats != null) {
                System.out.println("✅ 发票统计:");
                System.out.println("   - 总数: " + stats.getTotalCount());
                System.out.println("   - 待处理: " + stats.getPendingCount());
                System.out.println("   - 已完成: " + stats.getFinishedCount());
            } else {
                System.out.println("⚠️ 发票统计为空");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 发票统计功能不可用: " + e.getMessage());
        }
    }

    // ========== 杂项功能测试 ==========

    @Test
    void testGetGlobalConfig() {
        try {
            GlobalConfig config = client.misc().getGlobalConfig();
            assertNotNull(config);
            System.out.println("✅ 全局配置:");
            if (config.getTopic() != null && config.getTopic().getTalk() != null) {
                System.out.println("   - 话题最大字数: " + config.getTopic().getTalk().getMaxTextLength());
                System.out.println("   - 最大图片数: " + config.getTopic().getTalk().getMaxImageCount());
            }
            System.out.println("   - 最大视频大小: " + config.getMaxVideoSize());
        } catch (ZsxqException e) {
            System.out.println("⚠️ 全局配置功能不可用: " + e.getMessage());
        }
    }

    @Test
    void testGetActivities() {
        try {
            List<Activity> activities = client.misc().getActivities();
            assertNotNull(activities);
            System.out.println("✅ 获取到 " + activities.size() + " 条动态");
            for (int i = 0; i < Math.min(3, activities.size()); i++) {
                Activity activity = activities.get(i);
                System.out.println("   - 动态ID: " + activity.getDynamicId() + ", 类型: " + activity.getAction());
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 动态功能不可用: " + e.getMessage());
        }
    }

    @Test
    void testGetPkGroup() {
        try {
            PkGroup pkGroup = client.misc().getPkGroup(groupId);
            if (pkGroup != null) {
                System.out.println("✅ PK群组信息:");
                System.out.println("   - 名称: " + pkGroup.getName());
                System.out.println("   - 战力: " + pkGroup.getPower());
            } else {
                System.out.println("⚠️ PK群组信息为��");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ PK群组功能不可用: " + e.getMessage());
        }
    }

    // ========== 用户高级功能测试 ==========

    @Test
    void testGetBlockedUsers() {
        try {
            List<User> blockedUsers = client.users().getBlockedUsers();
            assertNotNull(blockedUsers);
            System.out.println("✅ 屏蔽用户数: " + blockedUsers.size());
            for (User user : blockedUsers) {
                System.out.println("   - " + user.getName() + " (ID: " + user.getUserId() + ")");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 屏蔽用户功能不可用: " + e.getMessage());
        }
    }

    @Test
    void testGetPreferenceCategories() {
        try {
            List<PreferenceCategory> categories = client.users().getPreferenceCategories();
            assertNotNull(categories);
            System.out.println("✅ 获取到 " + categories.size() + " 个推荐偏好分类");
            for (PreferenceCategory category : categories) {
                System.out.println("   - " + category.getName());
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 推荐偏好分类功能不可用: " + e.getMessage());
        }
    }

    @Test
    void testGetUnansweredQuestionsSummary() {
        try {
            UnansweredQuestionsSummary summary = client.users().getUnansweredQuestionsSummary();
            assertNotNull(summary);
            System.out.println("✅ 未回答问题数: " + summary.getUnansweredCount());
        } catch (ZsxqException e) {
            System.out.println("⚠️ 未回答问题摘要功能不可用: " + e.getMessage());
        }
    }

    @Test
    void testGetFollowerStats() {
        try {
            FollowerStatistics stats = client.users().getFollowerStats();
            assertNotNull(stats);
            System.out.println("✅ 关注者统计:");
            System.out.println("   - 关注者数: " + stats.getFollowersCount());
        } catch (ZsxqException e) {
            System.out.println("⚠️ 关注者统计功能不可用: " + e.getMessage());
        }
    }

    @Test
    void testGetContributions() {
        try {
            List<Contribution> contributions = client.users().getContributions();
            assertNotNull(contributions);
            System.out.println("✅ 获取到 " + contributions.size() + " 条贡献记录");
            for (int i = 0; i < Math.min(5, contributions.size()); i++) {
                Contribution c = contributions.get(i);
                System.out.println("   - " + c.getDate() + " (" + c.getType() + ")");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 贡献记录功能不可用: " + e.getMessage());
        }
    }

    @Test
    void testGetContributionStats() {
        try {
            ContributionStatistics stats = client.users().getContributionStats();
            if (stats != null) {
                System.out.println("✅ 贡献统计:");
                System.out.println("   - 最长连续天数: " + stats.getMaxConsecutiveDays());
                System.out.println("   - 当前连续天数: " + stats.getCurrentConsecutiveDays());
            } else {
                System.out.println("⚠️ 贡献统计为空");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 贡献统计功能不可用: " + e.getMessage());
        }
    }

    @Test
    void testGetAchievementsSummary() {
        try {
            List<AchievementSummary> summaries = client.users().getAchievementsSummary();
            assertNotNull(summaries);
            System.out.println("✅ 获取到 " + summaries.size() + " 条成就摘要");
            for (AchievementSummary summary : summaries) {
                System.out.println("   - " + summary.getTitle());
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 成就摘要功能不可用: " + e.getMessage());
        }
    }

    @Test
    void testGetWeeklyRanking() {
        try {
            WeeklyRanking ranking = client.users().getWeeklyRanking(groupId);
            assertNotNull(ranking);
            System.out.println("✅ 星球周榜排名:");
            if (ranking.getTopTopics() != null) {
                System.out.println("   - 话题排名: #" + ranking.getTopTopics().getRanking() + " (数量: " + ranking.getTopTopics().getCount() + ")");
            }
            if (ranking.getTopLikes() != null) {
                System.out.println("   - 点赞排名: #" + ranking.getTopLikes().getRanking() + " (数量: " + ranking.getTopLikes().getCount() + ")");
            }
            if (ranking.getTopDigests() != null) {
                System.out.println("   - 精华排名: #" + ranking.getTopDigests().getRanking() + " (数量: " + ranking.getTopDigests().getCount() + ")");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 周榜排名功能不可用: " + e.getMessage());
        }
    }

    @Test
    void testGetUserPreferences() {
        try {
            Map<String, Object> preferences = client.users().getPreferences();
            assertNotNull(preferences);
            System.out.println("✅ 用户偏好配置:");
            System.out.println("   - 键数量: " + preferences.size());
            if (preferences.containsKey("vibrate_and_sound")) {
                System.out.println("   - 震动与声音: " + preferences.get("vibrate_and_sound"));
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 用户偏好功能不可用: " + e.getMessage());
        }
    }

    // ========== 星球高级功能测试 ==========

    @Test
    void testGetRenewalInfo() {
        try {
            RenewalInfo renewal = client.groups().getRenewalInfo(groupId);
            if (renewal != null) {
                System.out.println("✅ 星球续费信息:");
                System.out.println("   - 续费启用: " + renewal.getRenewalEnabled());
                System.out.println("   - 续费价格: " + renewal.getRenewalPrice());
                System.out.println("   - 续费天数: " + renewal.getRenewalDays());
            } else {
                System.out.println("⚠️ 续费信息为空");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 续费信息功能不可用: " + e.getMessage());
        }
    }

    @Test
    void testGetDistribution() {
        try {
            DistributionInfo distribution = client.groups().getDistribution(groupId);
            if (distribution != null) {
                System.out.println("✅ 星球分销信息:");
                System.out.println("   - 分销启用: " + distribution.getEnabled());
                System.out.println("   - 佣金比例: " + distribution.getCommissionRate());
            } else {
                System.out.println("⚠️ 分销信息为空");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 分销信息功能不可用: " + e.getMessage());
        }
    }

    @Test
    void testGetUpgradeableGroups() {
        try {
            List<Group> groups = client.groups().getUpgradeableGroups();
            assertNotNull(groups);
            System.out.println("✅ 可升级星球数: " + groups.size());
            for (Group group : groups) {
                System.out.println("   - " + group.getName());
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 可升级星球功能不可用: " + e.getMessage());
        }
    }

    @Test
    void testGetRecommendedGroups() {
        try {
            List<Group> groups = client.groups().getRecommendedGroups();
            assertNotNull(groups);
            System.out.println("✅ 推荐星球数: " + groups.size());
            for (int i = 0; i < Math.min(5, groups.size()); i++) {
                System.out.println("   - " + groups.get(i).getName());
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 推荐星球功能不可用: " + e.getMessage());
        }
    }

    @Test
    void testGetCustomTags() {
        try {
            List<CustomTag> tags = client.groups().getCustomTags(groupId);
            assertNotNull(tags);
            System.out.println("✅ 获取到 " + tags.size() + " 个自定义标签");
            for (CustomTag tag : tags) {
                System.out.println("   - " + tag.getName() + " (话题数: " + tag.getTopicsCount() + ")");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 自定义标签功能不可用: " + e.getMessage());
        }
    }

    @Test
    void testGetScheduledTasks() {
        try {
            List<ScheduledJob> jobs = client.groups().getScheduledTasks(groupId);
            assertNotNull(jobs);
            System.out.println("✅ 获取到 " + jobs.size() + " 个定时任务");
            for (ScheduledJob job : jobs) {
                System.out.println("   - " + job.getName() + " (状态: " + job.getStatus() + ")");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 定时任务功能不可用: " + e.getMessage());
        }
    }

    @Test
    void testGetRiskWarnings() {
        try {
            GroupWarning warning = client.groups().getRiskWarnings(groupId);
            if (warning != null && warning.getType() != null) {
                System.out.println("✅ 风险预警:");
                System.out.println("   - 类型: " + warning.getType());
                System.out.println("   - 级别: " + warning.getLevel());
                System.out.println("   - 标题: " + warning.getTitle());
            } else {
                System.out.println("✅ 无风险预警");
            }
        } catch (ZsxqException e) {
            System.out.println("⚠️ 风险预警功能不可用: " + e.getMessage());
        }
    }

    // ========== 打卡创建测试（写操作，默认跳过）==========

    /**
     * 测试创建打卡项目（训练营）
     *
     * 注意：这是一个写操作，会真实创建打卡项目！
     * 需要设置 ZSXQ_TEST_CREATE=true 环境变量才会执行
     *
     * 基于 HAR 抓包分析的参数:
     * - title: "测试训练营"
     * - text: "完成7天打卡"
     * - checkin_days: 7
     * - type: "accumulated"
     * - show_topics_on_timeline: false
     * - validity.long_period: false
     * - validity.expiration_time: "2025-12-24T23:59:59.798+0800"
     */
    @Test
    @EnabledIfEnvironmentVariable(named = "ZSXQ_TEST_CREATE", matches = "true")
    void testCreateCheckin() {
        try {
            com.zsxq.sdk.request.CheckinsRequest.CreateCheckinParams params =
                new com.zsxq.sdk.request.CheckinsRequest.CreateCheckinParams()
                    .title("SDK测试训练营")
                    .text("完成7天打卡测试")
                    .checkinDays(7)
                    .type("accumulated")  // 累计打卡
                    .showTopicsOnTimeline(false)
                    .expirationTime("2025-12-24T23:59:59.798+0800");

            System.out.println("📝 创建打卡项目参数:");
            System.out.println("   - 标题: SDK测试训练营");
            System.out.println("   - 描述: 完成7天打卡测试");
            System.out.println("   - 天数: 7");
            System.out.println("   - 类型: accumulated (累计打卡)");

            Checkin checkin = client.checkins().create(groupId, params);

            if (checkin != null) {
                System.out.println("✅ 打卡项目创建成功!");
                System.out.println("   - ID: " + checkin.getCheckinId());
                System.out.println("   - 名称: " + checkin.getTitle());
                System.out.println("   - 状态: " + checkin.getStatus());
            } else {
                System.out.println("⚠️ 创建返回空值");
            }
        } catch (ZsxqException e) {
            System.out.println("❌ 创建打卡项目失败: " + e.getMessage());
            System.out.println("   错误码: " + e.getCode());
        }
    }

}
