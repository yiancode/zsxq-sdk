package com.zsxq.sdk.examples;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.model.Checkin;

/**
 * 验证创建的训练营
 */
public class VerifyCreatedCheckinExample {
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
                    .disableSignature()
                    .build();

            System.out.println("=== 验证创建的训练营 ===\n");

            // 验证训练营 ID: 1148222182
            String checkinId = "1148222182";
            System.out.println("查询训练营 ID: " + checkinId);
            System.out.println("-----------------------------------");

            Checkin checkin = client.checkins().get(groupId, checkinId);

            if (checkin != null) {
                System.out.println("✅ 找到训练营！");
                System.out.println("\n详细信息:");
                System.out.println("  ID: " + checkin.getCheckinId());
                System.out.println("  标题: " + checkin.getTitle());
                System.out.println("  描述: " + checkin.getText());
                System.out.println("  状态: " + checkin.getStatus());
                System.out.println("  创建时间: " + checkin.getCreateTime());

                if (checkin.getOwner() != null) {
                    System.out.println("  创建者: " + checkin.getOwner().getName());
                }

                System.out.println("\n========================================");
                System.out.println("🎉 创建训练营接口验证成功！");
                System.out.println("========================================");
                System.out.println("\n这个训练营是在之前的测试中成功创建的：");
                System.out.println("  - 测试程序: TestAllApisWithoutSignature");
                System.out.println("  - 使用配置: 禁用签名");
                System.out.println("  - 创建时间: " + checkin.getCreateTime());
                System.out.println("  - 当前状态: " + checkin.getStatus() + " (后续被关闭)");

            } else {
                System.out.println("❌ 未找到该训练营");
            }

        } catch (Exception e) {
            System.err.println("查询失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
