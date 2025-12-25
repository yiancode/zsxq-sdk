package com.zsxq.sdk.examples;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.request.CheckinsRequest.CreateCheckinParams;

import java.lang.reflect.Field;

/**
 * 调试签名配置
 */
public class DebugSignatureExample {
    public static void main(String[] args) {
        String token = args.length > 0 ? args[0] : System.getenv("ZSXQ_TOKEN");
        String groupId = args.length > 1 ? args[1] : System.getenv("ZSXQ_GROUP_ID");

        if (token == null || groupId == null) {
            System.err.println("错误: 请提供 ZSXQ_TOKEN 和 ZSXQ_GROUP_ID");
            System.exit(1);
        }

        try {
            System.out.println("========================================");
            System.out.println("调试签名配置");
            System.out.println("========================================\n");

            // 创建客户端（禁用签名）
            System.out.println("创建客户端（禁用签名）...");
            ZsxqClient client = new ZsxqClientBuilder()
                    .token(token)
                    .disableSignature()
                    .build();

            // 使用反射检查签名配置
            try {
                Field httpClientField = client.getClass().getDeclaredField("httpClient");
                httpClientField.setAccessible(true);
                Object httpClient = httpClientField.get(client);

                Field configField = httpClient.getClass().getDeclaredField("config");
                configField.setAccessible(true);
                Object config = configField.get(httpClient);

                Field signatureEnabledField = config.getClass().getDeclaredField("signatureEnabled");
                signatureEnabledField.setAccessible(true);
                boolean signatureEnabled = (boolean) signatureEnabledField.get(config);

                System.out.println("✅ 客户端创建成功");
                System.out.println("签名启用状态: " + signatureEnabled);
                System.out.println("预期: false (禁用)");
                System.out.println();

                if (signatureEnabled) {
                    System.out.println("⚠️  警告: 签名仍然是启用状态！");
                } else {
                    System.out.println("✅ 确认: 签名已禁用");
                }
            } catch (Exception e) {
                System.out.println("⚠️  无法通过反射检查配置: " + e.getMessage());
            }

            System.out.println("\n========================================");
            System.out.println("测试创建训练营（简化版）");
            System.out.println("========================================\n");

            CreateCheckinParams createParams = new CreateCheckinParams()
                    .title("调试测试")
                    .text("测试")
                    .checkinDays(7)
                    .type("accumulated")
                    .showTopicsOnTimeline(false)
                    .expirationTime("2026-01-25T23:59:59.000+0800");

            System.out.println("正在创建训练营...");
            Checkin newCheckin = client.checkins().create(groupId, createParams);

            if (newCheckin != null) {
                System.out.println("✅ 创建成功！");
                System.out.println("  ID: " + newCheckin.getCheckinId());
                System.out.println("  标题: " + newCheckin.getTitle());
                System.out.println("  状态: " + newCheckin.getStatus());
            } else {
                System.out.println("❌ 创建失败，返回 null");
            }

        } catch (Exception e) {
            System.err.println("❌ 操作失败: " + e.getMessage());
            System.err.println("异常类型: " + e.getClass().getName());
            e.printStackTrace();
        }
    }
}
