package com.zsxq.sdk.examples;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.request.CheckinsRequest.CreateCheckinParams;
import com.zsxq.sdk.request.CheckinsRequest.UpdateCheckinParams;

/**
 * 对比创建和更新训练营的行为
 */
public class CompareCreateAndUpdateExample {
    public static void main(String[] args) {
        String token = args.length > 0 ? args[0] : System.getenv("ZSXQ_TOKEN");
        String groupId = args.length > 1 ? args[1] : System.getenv("ZSXQ_GROUP_ID");

        if (token == null || groupId == null) {
            System.err.println("错误: 请提供 ZSXQ_TOKEN 和 ZSXQ_GROUP_ID");
            System.exit(1);
        }

        try {
            ZsxqClient client = new ZsxqClientBuilder()
                    .token(token)
                    .build();

            System.out.println("=== 对比创建和更新训练营 ===");
            System.out.println("Group ID: " + groupId);
            System.out.println("\n========================================\n");

            // 测试 1: 创建训练营
            System.out.println("【测试 1】创建新训练营...");
            try {
                CreateCheckinParams createParams = new CreateCheckinParams()
                        .title("测试关闭功能-" + System.currentTimeMillis())
                        .text("这是一个用于测试关闭功能的训练营")
                        .checkinDays(7)
                        .type("accumulated")
                        .showTopicsOnTimeline(false)
                        .expirationTime("2026-01-10T23:59:59.000+0800");

                Checkin newCheckin = client.checkins().create(groupId, createParams);

                if (newCheckin != null) {
                    System.out.println("  ✅ 创建成功！");
                    System.out.println("  训练营ID: " + newCheckin.getCheckinId());
                    System.out.println("  标题: " + newCheckin.getTitle());
                    System.out.println("  状态: " + newCheckin.getStatus());

                    // 测试 2: 立即关闭刚创建的训练营
                    System.out.println("\n【测试 2】关闭刚创建的训练营...");
                    String newCheckinId = String.valueOf(newCheckin.getCheckinId());

                    UpdateCheckinParams updateParams = new UpdateCheckinParams()
                            .status("closed");

                    Checkin closedCheckin = client.checkins().update(groupId, newCheckinId, updateParams);

                    if (closedCheckin != null) {
                        System.out.println("  ✅ 关闭成功！");
                        System.out.println("  新状态: " + closedCheckin.getStatus());
                    } else {
                        System.out.println("  ⚠️  API 返回 null");

                        // 验证状态
                        System.out.println("\n【测试 3】验证训练营状态...");
                        Checkin verifyCheckin = client.checkins().get(groupId, newCheckinId);
                        if (verifyCheckin != null) {
                            System.out.println("  当前状态: " + verifyCheckin.getStatus());
                            if ("closed".equals(verifyCheckin.getStatus())) {
                                System.out.println("  ✅ 虽然返回 null，但状态已成功更新为 closed");
                            }
                        }
                    }
                } else {
                    System.out.println("  ❌ 创建失败，返回 null");
                }

            } catch (Exception e) {
                System.out.println("  ❌ 操作失败: " + e.getMessage());
                System.out.println("  错误类型: " + e.getClass().getSimpleName());
                e.printStackTrace();
            }

            System.out.println("\n========================================");
            System.out.println("\n结论：");
            System.out.println("- 如果创建成功但关闭失败，说明不是签名问题");
            System.out.println("- 如果都失败，说明可能是 Token 或签名问题");
            System.out.println("- 如果关闭返回 null 但状态已更新，说明 API 行为特殊");

        } catch (Exception e) {
            System.err.println("\n整体操作失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
