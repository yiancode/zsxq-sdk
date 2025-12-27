package com.zsxq.sdk.examples;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.exception.ZsxqException;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.model.RankingItem;
import com.zsxq.sdk.model.Topic;

import java.util.List;

/**
 * 测试参数修复
 * 验证 getTopics() 和 getRankingList() 的默认参数修复
 */
public class TestParameterFixes {
    public static void main(String[] args) {
        String token = System.getenv("ZSXQ_TOKEN");
        String groupId = System.getenv("ZSXQ_GROUP_ID");

        if (token == null || groupId == null) {
            System.err.println("❌ 错误: 请设置环境变量 ZSXQ_TOKEN 和 ZSXQ_GROUP_ID");
            System.exit(1);
        }

        ZsxqClient client = new ZsxqClientBuilder()
                .token(token)
                .build();

        System.out.println("=== 测试参数修复 ===");
        System.out.println("星球 ID: " + groupId);
        System.out.println();

        try {
            // 获取第一个训练营
            System.out.println("📋 获取训练营列表...");
            List<Checkin> checkins = client.checkins().list(groupId);

            if (checkins.isEmpty()) {
                System.out.println("❌ 没有找到训练营");
                System.exit(1);
            }

            Checkin checkin = checkins.get(0);
            String checkinId = String.valueOf(checkin.getCheckinId());
            System.out.println("✅ 找到训练营: " + checkin.getTitle() + " (ID: " + checkinId + ")");
            System.out.println();

            // 测试 1: getTopics() 默认参数
            System.out.println("【测试 1】getTopics() - 应该自动添加 scope 和 count 参数");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            try {
                List<Topic> topics = client.checkins().getTopics(groupId, checkinId);
                System.out.println("✅ 成功！获取到 " + topics.size() + " 条话题");
                if (!topics.isEmpty()) {
                    System.out.println("   第一条话题 ID: " + topics.get(0).getTopicId());
                }
            } catch (ZsxqException e) {
                System.out.println("❌ 失败！错误码: " + e.getCode() + ", 消息: " + e.getMessage());
            }
            System.out.println();

            // 测试 2: getRankingList() 默认参数
            System.out.println("【测试 2】getRankingList() - 应该自动添加 type 参数");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            try {
                List<RankingItem> ranking = client.checkins().getRankingList(groupId, checkinId);
                System.out.println("✅ 成功！获取到 " + ranking.size() + " 条排行");
                for (int i = 0; i < Math.min(3, ranking.size()); i++) {
                    RankingItem item = ranking.get(i);
                    if (item.getUser() != null) {
                        System.out.println("   #" + (i + 1) + " " + item.getUser().getName() +
                            " - 排名: " + item.getRank() +
                            ", 打卡: " + item.getCount() + " 次");
                    }
                }
            } catch (ZsxqException e) {
                System.out.println("❌ 失败！错误码: " + e.getCode() + ", 消息: " + e.getMessage());
            }
            System.out.println();

            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("🎉 测试完成！");
            System.out.println();
            System.out.println("修复内容总结:");
            System.out.println("  1. getTopics() - 自动添加 scope='all', count=20");
            System.out.println("  2. getRankingList() - 自动添加 type='accumulated'");

        } catch (Exception e) {
            System.err.println("❌ 发生错误: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
