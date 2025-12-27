package com.zsxq.sdk.examples;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.exception.ZsxqException;
import com.zsxq.sdk.model.Topic;

import java.util.List;

/**
 * 测试修复后的 getTopics() 方法
 */
public class TestGetTopicsFixed {
    public static void main(String[] args) {
        String token = System.getenv("ZSXQ_TOKEN");
        String groupId = System.getenv("ZSXQ_GROUP_ID");

        if (token == null || groupId == null) {
            System.err.println("❌ 错误: 请设置环境变量 ZSXQ_TOKEN 和 ZSXQ_GROUP_ID");
            System.exit(1);
        }

        String checkinId = "8421558112";  // 使用之前创建的训练营

        ZsxqClient client = new ZsxqClientBuilder()
                .token(token)
                .build();

        System.out.println("=== 测试修复后的 getTopics() 方法 ===");
        System.out.println("星球 ID: " + groupId);
        System.out.println("训练营 ID: " + checkinId);
        System.out.println();

        try {
            System.out.println("🚀 正在调用 getTopics() ...");
            List<Topic> topics = client.checkins().getTopics(groupId, checkinId);

            System.out.println("✅ 成功！获取到 " + topics.size() + " 条话题");
            if (!topics.isEmpty()) {
                System.out.println();
                System.out.println("话题列表:");
                for (int i = 0; i < Math.min(3, topics.size()); i++) {
                    Topic topic = topics.get(i);
                    System.out.println("  " + (i + 1) + ". ID: " + topic.getTopicId());
                    if (topic.getTalk() != null && topic.getTalk().getOwner() != null) {
                        System.out.println("     作者: " + topic.getTalk().getOwner().getName());
                    }
                }
            } else {
                System.out.println("   （训练营暂无打卡话题）");
            }

            System.out.println();
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("🎉 Bug 已修复！");
            System.out.println();
            System.out.println("修复内容:");
            System.out.println("  1. getTopics() 方法现在会自动添加必需的 scope 参数");
            System.out.println("  2. 默认 scope='all', count=20");
            System.out.println("  3. 与 list() 方法保持一致的实现方式");

        } catch (ZsxqException e) {
            System.err.println("❌ 调用失败！");
            System.err.println("   错误码: " + e.getCode());
            System.err.println("   错误信息: " + e.getMessage());
            System.exit(1);
        }
    }
}
