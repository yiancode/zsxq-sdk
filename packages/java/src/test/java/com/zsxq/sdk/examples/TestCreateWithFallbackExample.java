package com.zsxq.sdk.examples;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.exception.ZsxqException;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.request.CheckinsRequest.CreateCheckinParams;

/**
 * 方案一：默认启用签名，失败后禁用签名重试
 */
public class TestCreateWithFallbackExample {
    public static void main(String[] args) {
        String token = args.length > 0 ? args[0] : System.getenv("ZSXQ_TOKEN");
        String groupId = args.length > 1 ? args[1] : System.getenv("ZSXQ_GROUP_ID");

        if (token == null || groupId == null) {
            System.err.println("错误: 请提供 ZSXQ_TOKEN 和 ZSXQ_GROUP_ID");
            System.exit(1);
        }

        System.out.println("=== 方案一：智能降级测试 ===");
        System.out.println("策略：先启用签名，失败后禁用签名重试");
        System.out.println("========================================\n");

        // 准备创建参数
        CreateCheckinParams params = new CreateCheckinParams()
                .title("方案一测试-" + System.currentTimeMillis())
                .text("测试启用签名失败后，禁用签名重试")
                .checkinDays(7)
                .type("accumulated")
                .showTopicsOnTimeline(false)
                .expirationTime("2026-01-20T23:59:59.000+0800");

        Checkin result = null;

        // 第一次尝试：启用签名（默认配置）
        System.out.println("【尝试 1】使用默认配置（启用签名）...");
        try {
            ZsxqClient client = new ZsxqClientBuilder()
                    .token(token)
                    // signatureEnabled 默认为 true，不需要显式设置
                    .build();

            result = client.checkins().create(groupId, params);

            if (result != null) {
                System.out.println("  ✅ 成功！使用启用签名的配置");
                System.out.println("  训练营ID: " + result.getCheckinId());
                System.out.println("  标题: " + result.getTitle());
            } else {
                System.out.println("  ⚠️  返回 null");
            }

        } catch (ZsxqException e) {
            System.out.println("  ❌ 失败: " + e.getMessage());
            System.out.println("  错误码: " + e.getCode());

            // 检查是否是签名相关错误（1059 或其他）
            if (e.getCode() == 1059 || e.getMessage().contains("内部错误")) {
                System.out.println("  💡 检测到可能的签名问题，准备禁用签名重试...\n");

                // 第二次尝试：禁用签名
                System.out.println("【尝试 2】禁用签名重试...");
                try {
                    ZsxqClient clientWithoutSig = new ZsxqClientBuilder()
                            .token(token)
                            .disableSignature()  // 禁用签名
                            .build();

                    result = clientWithoutSig.checkins().create(groupId, params);

                    if (result != null) {
                        System.out.println("  ✅ 成功！禁用签名后创建成功");
                        System.out.println("  训练营ID: " + result.getCheckinId());
                        System.out.println("  标题: " + result.getTitle());
                    } else {
                        System.out.println("  ⚠️  返回 null，但可能已创建成功");
                    }

                } catch (Exception e2) {
                    System.out.println("  ❌ 禁用签名后仍然失败: " + e2.getMessage());
                    e2.printStackTrace();
                }
            } else {
                System.out.println("  ⚠️  不是签名问题，无需重试");
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("  ❌ 未预期的错误: " + e.getMessage());
            e.printStackTrace();
        }

        // 总结
        System.out.println("\n========================================");
        if (result != null) {
            System.out.println("✅ 创建训练营成功！");
            System.out.println("\n方案一的优点：");
            System.out.println("  1. 默认尝试标准配置（启用签名）");
            System.out.println("  2. 失败后自动降级（禁用签名）");
            System.out.println("  3. 兼容性好，适应不同的 API 需求");

            // 清理测试数据
            System.out.println("\n正在清理测试数据...");
            try {
                ZsxqClient cleanupClient = new ZsxqClientBuilder()
                        .token(token)
                        .disableSignature()
                        .build();

                cleanupClient.checkins().update(
                    groupId,
                    String.valueOf(result.getCheckinId()),
                    new com.zsxq.sdk.request.CheckinsRequest.UpdateCheckinParams().status("closed")
                );
                System.out.println("✅ 测试数据已清理");
            } catch (Exception e) {
                System.out.println("⚠️  清理失败（可以手动删除）: " + e.getMessage());
            }
        } else {
            System.out.println("❌ 创建训练营失败");
        }
        System.out.println("========================================");
    }
}
