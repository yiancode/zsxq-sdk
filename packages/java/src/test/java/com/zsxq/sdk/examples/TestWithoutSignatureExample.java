package com.zsxq.sdk.examples;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.client.ZsxqConfig;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.request.CheckinsRequest.UpdateCheckinParams;

/**
 * 测试不使用签名的请求
 */
public class TestWithoutSignatureExample {
    public static void main(String[] args) {
        String token = args.length > 0 ? args[0] : System.getenv("ZSXQ_TOKEN");
        String groupId = args.length > 1 ? args[1] : System.getenv("ZSXQ_GROUP_ID");
        String checkinId = args.length > 2 ? args[2] : "8424114182";

        if (token == null || groupId == null) {
            System.err.println("错误: 请提供 ZSXQ_TOKEN 和 ZSXQ_GROUP_ID");
            System.exit(1);
        }

        try {
            System.out.println("测试不使用签名的请求");
            System.out.println("========================================\n");

            // 创建禁用签名的客户端
            ZsxqClient client = new ZsxqClientBuilder()
                    .token(token)
                    .disableSignature()  // 禁用签名
                    .build();

            System.out.println("1. 测试查询（GET 请求）...");
            try {
                Checkin checkin = client.checkins().get(groupId, checkinId);
                if (checkin != null) {
                    System.out.println("   ✅ 查询成功");
                    System.out.println("   状态: " + checkin.getStatus());
                } else {
                    System.out.println("   ⚠️  返回 null");
                }
            } catch (Exception e) {
                System.out.println("   ❌ 查询失败: " + e.getMessage());
            }

            System.out.println("\n2. 测试更新（PUT 请求）...");
            try {
                UpdateCheckinParams params = new UpdateCheckinParams()
                        .status("closed");

                Checkin updatedCheckin = client.checkins().update(groupId, checkinId, params);

                if (updatedCheckin != null) {
                    System.out.println("   ✅ 更新成功");
                    System.out.println("   新状态: " + updatedCheckin.getStatus());
                } else {
                    System.out.println("   ⚠️  返回 null");
                }
            } catch (Exception e) {
                System.out.println("   ❌ 更新失败: " + e.getMessage());
                System.out.println("   错误类型: " + e.getClass().getSimpleName());
            }

            System.out.println("\n========================================");
            System.out.println("结论：对比使用签名和不使用签名的结果差异");

        } catch (Exception e) {
            System.err.println("操作失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
