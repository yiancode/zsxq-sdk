package com.zsxq.sdk.examples;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.request.CheckinsRequest.UpdateCheckinParams;

/**
 * 关闭指定训练营的示例
 */
public class CloseSpecificCheckinExample {
    public static void main(String[] args) {
        // 从参数获取 token, group_id 和 checkin_id
        String token = args.length > 0 ? args[0] : System.getenv("ZSXQ_TOKEN");
        String groupId = args.length > 1 ? args[1] : System.getenv("ZSXQ_GROUP_ID");
        String checkinId = args.length > 2 ? args[2] : "4251444218";

        if (token == null || groupId == null) {
            System.err.println("错误: 请提供 ZSXQ_TOKEN 和 ZSXQ_GROUP_ID");
            System.err.println("用法: java CloseSpecificCheckinExample <token> <group_id> [checkin_id]");
            System.exit(1);
        }

        try {
            // 创建客户端（禁用签名）
            ZsxqClient client = new ZsxqClientBuilder()
                    .token(token)
                    .disableSignature()
                    .build();

            System.out.println("========================================");
            System.out.println("测试 SDK 关闭训练营功能");
            System.out.println("========================================");
            System.out.println("Group ID: " + groupId);
            System.out.println("Checkin ID: " + checkinId);
            System.out.println();

            // 1. 查询训练营当前状态
            System.out.println("【步骤 1】查询训练营当前状态...\n");
            try {
                Checkin beforeCheckin = client.checkins().get(groupId, checkinId);
                if (beforeCheckin != null) {
                    System.out.println("训练营信息:");
                    System.out.println("  ID: " + beforeCheckin.getCheckinId());
                    System.out.println("  标题: " + beforeCheckin.getTitle());
                    System.out.println("  当前状态: " + beforeCheckin.getStatus());
                    System.out.println("  创建时间: " + beforeCheckin.getCreateTime());
                    System.out.println();
                } else {
                    System.out.println("❌ 训练营不存在");
                    System.exit(1);
                }
            } catch (Exception e) {
                System.out.println("❌ 查询失败: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }

            // 2. 关闭训练营
            System.out.println("【步骤 2】关闭训练营...\n");
            try {
                UpdateCheckinParams params = new UpdateCheckinParams()
                        .status("closed");

                Checkin updatedCheckin = client.checkins().update(groupId, checkinId, params);

                if (updatedCheckin != null) {
                    System.out.println("✅ 更新成功！");
                    System.out.println("\n更新后的训练营信息:");
                    System.out.println("  ID: " + updatedCheckin.getCheckinId());
                    System.out.println("  标题: " + updatedCheckin.getTitle());
                    System.out.println("  新状态: " + updatedCheckin.getStatus());
                    System.out.println();

                    if ("closed".equals(updatedCheckin.getStatus())) {
                        System.out.println("✅ 确认：训练营已成功关闭！");
                    } else {
                        System.out.println("⚠️  警告：状态不是 'closed'，实际为: " + updatedCheckin.getStatus());
                    }
                } else {
                    System.out.println("⚠️  API 返回 null，让我们验证一下状态...");
                }
            } catch (Exception e) {
                System.out.println("❌ 关闭失败: " + e.getMessage());
                System.out.println("异常类型: " + e.getClass().getName());
                e.printStackTrace();
                System.exit(1);
            }

            // 3. 验证最终状态
            System.out.println("\n【步骤 3】验证最终状态...\n");
            try {
                Checkin afterCheckin = client.checkins().get(groupId, checkinId);
                if (afterCheckin != null) {
                    System.out.println("最终状态: " + afterCheckin.getStatus());
                    if ("closed".equals(afterCheckin.getStatus())) {
                        System.out.println("✅ 验证成功：训练营确实已关闭！");
                    } else {
                        System.out.println("❌ 验证失败：状态仍然是 " + afterCheckin.getStatus());
                    }
                } else {
                    System.out.println("❌ 无法获取训练营信息");
                }
            } catch (Exception e) {
                System.out.println("❌ 验证失败: " + e.getMessage());
                e.printStackTrace();
            }

            System.out.println("\n========================================");
            System.out.println("测试完成！");
            System.out.println("========================================");

        } catch (Exception e) {
            System.err.println("\n操作失败: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
