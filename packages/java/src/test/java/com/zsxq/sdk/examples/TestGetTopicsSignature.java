package com.zsxq.sdk.examples;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.exception.ZsxqException;
import com.zsxq.sdk.model.Topic;

import java.util.List;

/**
 * 测试 getTopics() 方法的签名问题
 * 对比启用和禁用签名的两种情况
 */
public class TestGetTopicsSignature {
    public static void main(String[] args) {
        String token = System.getenv("ZSXQ_TOKEN");
        String groupId = System.getenv("ZSXQ_GROUP_ID");

        if (token == null || groupId == null) {
            System.err.println("❌ 错误: 请设置环境变量 ZSXQ_TOKEN 和 ZSXQ_GROUP_ID");
            System.exit(1);
        }

        // 使用刚创建的训练营 ID
        String checkinId = "8421558112";

        System.out.println("=== 测试 getTopics() 签名问题 ===");
        System.out.println("星球 ID: " + groupId);
        System.out.println("训练营 ID: " + checkinId);
        System.out.println();

        // 测试 1: 启用签名（默认行为）
        System.out.println("【测试 1】启用签名（signatureEnabled = true，默认）");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        try {
            ZsxqClient clientWithSignature = new ZsxqClientBuilder()
                    .token(token)
                    .signatureEnabled(true)  // 显式启用签名
                    .build();

            System.out.println("🚀 正在调用 getTopics() ...");
            List<Topic> topics = clientWithSignature.checkins().getTopics(groupId, checkinId);

            System.out.println("✅ 成功！获取到 " + topics.size() + " 条话题");
            if (!topics.isEmpty()) {
                System.out.println("   第一条话题 ID: " + topics.get(0).getTopicId());
            }
        } catch (ZsxqException e) {
            System.out.println("❌ 失败！");
            System.out.println("   错误码: " + e.getCode());
            System.out.println("   错误信息: " + e.getMessage());
            if (e.getCode() == 1059) {
                System.out.println("   ⚠️  这是预期的错误 - 签名导致 1059 内部错误");
            }
        }
        System.out.println();

        // 测试 2: 禁用签名
        System.out.println("【测试 2】禁用签名（signatureEnabled = false）");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        try {
            ZsxqClient clientWithoutSignature = new ZsxqClientBuilder()
                    .token(token)
                    .disableSignature()  // 禁用签名
                    .build();

            System.out.println("🚀 正在调用 getTopics() ...");
            List<Topic> topics = clientWithoutSignature.checkins().getTopics(groupId, checkinId);

            System.out.println("✅ 成功！获取到 " + topics.size() + " 条话题");
            if (!topics.isEmpty()) {
                System.out.println("   第一条话题 ID: " + topics.get(0).getTopicId());
            }
        } catch (ZsxqException e) {
            System.out.println("❌ 失败！");
            System.out.println("   错误码: " + e.getCode());
            System.out.println("   错误信息: " + e.getMessage());
        }
        System.out.println();

        // 结论
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("📊 测试结论:");
        System.out.println("   如果测试 1 失败（1059 错误），测试 2 成功，");
        System.out.println("   则说明问题是：SDK 默认启用的签名功能导致 API 返回 1059 错误");
        System.out.println();
        System.out.println("💡 解决方案:");
        System.out.println("   在创建客户端时禁用签名:");
        System.out.println("   ZsxqClient client = new ZsxqClientBuilder()");
        System.out.println("       .token(token)");
        System.out.println("       .disableSignature()  // 添加这一行");
        System.out.println("       .build();");
    }
}
