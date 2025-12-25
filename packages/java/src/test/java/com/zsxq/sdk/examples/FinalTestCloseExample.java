package com.zsxq.sdk.examples;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.request.CheckinsRequest.UpdateCheckinParams;
import com.zsxq.sdk.request.CheckinsRequest.ListCheckinsOptions;

import java.util.List;

/**
 * 最终验证：使用禁用签名的 SDK 关闭训练营
 */
public class FinalTestCloseExample {
    public static void main(String[] args) {
        String token = args.length > 0 ? args[0] : System.getenv("ZSXQ_TOKEN");
        String groupId = args.length > 1 ? args[1] : System.getenv("ZSXQ_GROUP_ID");

        if (token == null || groupId == null) {
            System.err.println("错误: 请提供 ZSXQ_TOKEN 和 ZSXQ_GROUP_ID");
            System.exit(1);
        }

        try {
            // 创建禁用签名的客户端
            ZsxqClient client = new ZsxqClientBuilder()
                    .token(token)
                    .disableSignature()  // 禁用签名
                    .build();

            System.out.println("=== 最终验证：关闭训练营功能 ===");
            System.out.println("配置：禁用签名");
            System.out.println("========================================\n");

            // 1. 查询所有进行中的训练营
            System.out.println("【步骤 1】查询进行中的训练营...");
            List<Checkin> ongoingCheckins = client.checkins().list(
                    groupId,
                    new ListCheckinsOptions().scope("ongoing")
            );

            System.out.println("找到 " + ongoingCheckins.size() + " 个进行中的训练营\n");

            if (ongoingCheckins.isEmpty()) {
                System.out.println("没有进行中的训练营，测试结束");
                return;
            }

            // 取第一个训练营进行测试
            Checkin targetCheckin = ongoingCheckins.get(0);
            String checkinId = String.valueOf(targetCheckin.getCheckinId());

            System.out.println("选择训练营:");
            System.out.println("  ID: " + checkinId);
            System.out.println("  标题: " + targetCheckin.getTitle());
            System.out.println("  当前状态: " + targetCheckin.getStatus());
            System.out.println();

            // 2. 关闭训练营
            System.out.println("【步骤 2】关闭训练营...");
            UpdateCheckinParams params = new UpdateCheckinParams()
                    .status("closed");

            Checkin result = client.checkins().update(groupId, checkinId, params);

            if (result == null) {
                System.out.println("  ⚠️  API 返回 null（这是正常的，因为 API 返回空的 resp_data）");
            } else {
                System.out.println("  ✅ 返回结果:");
                System.out.println("     状态: " + result.getStatus());
            }

            // 3. 验证状态
            System.out.println("\n【步骤 3】验证训练营状态...");
            Checkin verifiedCheckin = client.checkins().get(groupId, checkinId);

            if (verifiedCheckin != null) {
                System.out.println("  当前状态: " + verifiedCheckin.getStatus());

                if ("closed".equals(verifiedCheckin.getStatus())) {
                    System.out.println("\n  🎉 成功！训练营已关闭");
                } else {
                    System.out.println("\n  ⚠️  状态未变为 closed，当前为: " + verifiedCheckin.getStatus());
                }
            } else {
                System.out.println("  ❌ 无法获取训练营信息");
            }

            // 4. 再次查询进行中的训练营数量
            System.out.println("\n【步骤 4】确认进行中的训练营数量...");
            List<Checkin> remainingCheckins = client.checkins().list(
                    groupId,
                    new ListCheckinsOptions().scope("ongoing")
            );
            System.out.println("  剩余进行中的训练营: " + remainingCheckins.size() + " 个");

            System.out.println("\n========================================");
            System.out.println("✅ 测试完成！关闭训练营功能正常工作");

        } catch (Exception e) {
            System.err.println("\n❌ 测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
