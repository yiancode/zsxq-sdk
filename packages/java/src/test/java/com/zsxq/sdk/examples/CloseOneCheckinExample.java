package com.zsxq.sdk.examples;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.request.CheckinsRequest.UpdateCheckinParams;

/**
 * 关闭单个训练营示例（用于调试）
 */
public class CloseOneCheckinExample {
    public static void main(String[] args) {
        String token = args.length > 0 ? args[0] : System.getenv("ZSXQ_TOKEN");
        String groupId = args.length > 1 ? args[1] : System.getenv("ZSXQ_GROUP_ID");
        String checkinId = args.length > 2 ? args[2] : "5454884814";  // 默认使用用户之前提供的ID

        if (token == null || groupId == null) {
            System.err.println("错误: 请提供 ZSXQ_TOKEN 和 ZSXQ_GROUP_ID");
            System.err.println("用法: java CloseOneCheckinExample <token> <group_id> [checkin_id]");
            System.exit(1);
        }

        try {
            ZsxqClient client = new ZsxqClientBuilder()
                    .token(token)
                    .build();

            System.out.println("测试关闭训练营功能");
            System.out.println("Group ID: " + groupId);
            System.out.println("Checkin ID: " + checkinId);
            System.out.println("========================================\n");

            // 先获取当前状态
            System.out.println("1. 获取训练营当前状态...");
            Checkin currentCheckin = client.checkins().get(groupId, checkinId);
            if (currentCheckin != null) {
                System.out.println("   当前状态: " + currentCheckin.getStatus());
                System.out.println("   标题: " + currentCheckin.getTitle());
            } else {
                System.out.println("   ⚠️  未找到该训练营");
            }

            // 更新状态为 closed
            System.out.println("\n2. 正在关闭训练营...");
            UpdateCheckinParams params = new UpdateCheckinParams()
                    .status("closed");

            Checkin updatedCheckin = client.checkins().update(groupId, checkinId, params);

            System.out.println("\n3. 关闭结果:");
            if (updatedCheckin != null) {
                System.out.println("   ✅ API 调用成功");
                System.out.println("   训练营ID: " + updatedCheckin.getCheckinId());
                System.out.println("   标题: " + updatedCheckin.getTitle());
                System.out.println("   新状态: " + updatedCheckin.getStatus());
                System.out.println("   描述: " + updatedCheckin.getText());

                if ("closed".equals(updatedCheckin.getStatus())) {
                    System.out.println("\n   🎉 训练营已成功关闭！");
                } else {
                    System.out.println("\n   ⚠️  状态未变为 closed，当前为: " + updatedCheckin.getStatus());
                }
            } else {
                System.out.println("   ❌ API 返回 null");
            }

            // 再次获取状态确认
            System.out.println("\n4. 验证最终状态...");
            Checkin finalCheckin = client.checkins().get(groupId, checkinId);
            if (finalCheckin != null) {
                System.out.println("   最终状态: " + finalCheckin.getStatus());
            }

        } catch (Exception e) {
            System.err.println("\n❌ 操作失败:");
            System.err.println("   错误信息: " + e.getMessage());
            System.err.println("   异常类型: " + e.getClass().getName());
            e.printStackTrace();
        }
    }
}
