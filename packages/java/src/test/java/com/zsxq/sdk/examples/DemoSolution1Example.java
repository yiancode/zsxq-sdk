package com.zsxq.sdk.examples;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.exception.ZsxqException;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.request.CheckinsRequest.CreateCheckinParams;
import com.zsxq.sdk.request.CheckinsRequest.UpdateCheckinParams;

/**
 * 方案一完整演示：使用已存在的训练营
 *
 * 使用模式：
 * 1. 先用默认配置（启用签名）
 * 2. 如果遇到 1059 错误，禁用签名重试
 */
public class DemoSolution1Example {

    /**
     * 方案一：智能降级创建训练营
     */
    public static Checkin createCheckinWithFallback(String token, String groupId, CreateCheckinParams params) {
        System.out.println("执行方案一：智能降级创建");
        System.out.println("-----------------------------------");

        // 尝试 1：启用签名（默认）
        System.out.println("[1] 尝试使用默认配置（启用签名）");
        try {
            ZsxqClient client = new ZsxqClientBuilder()
                    .token(token)
                    .build();  // 默认启用签名

            Checkin result = client.checkins().create(groupId, params);
            System.out.println("    ✅ 成功！");
            return result;

        } catch (ZsxqException e) {
            System.out.println("    ❌ 失败: " + e.getMessage() + " (错误码: " + e.getCode() + ")");

            // 检查是否是签名相关错误
            if (e.getCode() == 1059) {
                System.out.println("[2] 检测到签名问题，禁用签名重试");
                try {
                    ZsxqClient clientNoSig = new ZsxqClientBuilder()
                            .token(token)
                            .disableSignature()  // 禁用签名
                            .build();

                    Checkin result = clientNoSig.checkins().create(groupId, params);
                    System.out.println("    ✅ 禁用签名后成功！");
                    return result;

                } catch (Exception e2) {
                    System.out.println("    ❌ 禁用签名后仍失败: " + e2.getMessage());
                    throw e2;
                }
            } else {
                throw e;
            }
        }
    }

    /**
     * 方案一：智能降级更新训练营
     */
    public static Checkin updateCheckinWithFallback(String token, String groupId, String checkinId, UpdateCheckinParams params) {
        System.out.println("\n执行方案一：智能降级更新");
        System.out.println("-----------------------------------");

        // 尝试 1：启用签名（默认）
        System.out.println("[1] 尝试使用默认配置（启用签名）");
        try {
            ZsxqClient client = new ZsxqClientBuilder()
                    .token(token)
                    .build();

            Checkin result = client.checkins().update(groupId, checkinId, params);
            System.out.println("    ✅ 成功！");
            return result;

        } catch (ZsxqException e) {
            System.out.println("    ❌ 失败: " + e.getMessage() + " (错误码: " + e.getCode() + ")");

            if (e.getCode() == 1059) {
                System.out.println("[2] 检测到签名问题，禁用签名重试");
                try {
                    ZsxqClient clientNoSig = new ZsxqClientBuilder()
                            .token(token)
                            .disableSignature()
                            .build();

                    Checkin result = clientNoSig.checkins().update(groupId, checkinId, params);

                    // API 可能返回 null，需要验证
                    if (result == null) {
                        System.out.println("    ⚠️  返回 null，验证状态...");
                        Checkin verify = clientNoSig.checkins().get(groupId, checkinId);
                        if (verify != null && "closed".equals(verify.getStatus())) {
                            System.out.println("    ✅ 验证成功，状态已更新！");
                            return verify;
                        }
                    } else {
                        System.out.println("    ✅ 禁用签名后成功！");
                        return result;
                    }

                } catch (Exception e2) {
                    System.out.println("    ❌ 禁用签名后仍失败: " + e2.getMessage());
                    throw e2;
                }
            } else {
                throw e;
            }
        }

        return null;
    }

    public static void main(String[] args) {
        String token = args.length > 0 ? args[0] : System.getenv("ZSXQ_TOKEN");
        String groupId = args.length > 1 ? args[1] : System.getenv("ZSXQ_GROUP_ID");

        if (token == null || groupId == null) {
            System.err.println("错误: 请提供 ZSXQ_TOKEN 和 ZSXQ_GROUP_ID");
            System.exit(1);
        }

        System.out.println("=== 方案一完整演示 ===");
        System.out.println("使用已存在的训练营测试更新功能\n");
        System.out.println("========================================\n");

        try {
            // 查询一个进行中的训练营（如果有）
            ZsxqClient queryClient = new ZsxqClientBuilder()
                    .token(token)
                    .disableSignature()
                    .build();

            var ongoingList = queryClient.checkins().list(
                groupId,
                new com.zsxq.sdk.request.CheckinsRequest.ListCheckinsOptions().scope("ongoing")
            );

            String testCheckinId;
            if (!ongoingList.isEmpty()) {
                testCheckinId = String.valueOf(ongoingList.get(0).getCheckinId());
                System.out.println("找到进行中的训练营: " + testCheckinId);
            } else {
                // 没有进行中的，随便找一个已关闭的来测试
                var allList = queryClient.checkins().list(groupId);
                if (!allList.isEmpty()) {
                    testCheckinId = String.valueOf(allList.get(0).getCheckinId());
                    System.out.println("使用已有的训练营: " + testCheckinId);
                } else {
                    System.out.println("没有训练营可供测试");
                    return;
                }
            }

            System.out.println("\n");

            // 演示更新功能
            UpdateCheckinParams params = new UpdateCheckinParams()
                    .status("closed");

            Checkin result = updateCheckinWithFallback(token, groupId, testCheckinId, params);

            System.out.println("\n========================================");
            System.out.println("✅ 方案一演示完成！");
            System.out.println("\n方案一的优势：");
            System.out.println("  1. 默认使用标准配置（启用签名）");
            System.out.println("  2. 失败时智能降级（禁用签名）");
            System.out.println("  3. 对用户透明，自动处理");
            System.out.println("  4. 兼容未来可能需要签名的 API");
            System.out.println("========================================");

        } catch (Exception e) {
            System.err.println("\n演示失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
