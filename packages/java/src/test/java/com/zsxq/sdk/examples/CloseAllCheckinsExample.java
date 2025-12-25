package com.zsxq.sdk.examples;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.request.CheckinsRequest.ListCheckinsOptions;
import com.zsxq.sdk.request.CheckinsRequest.UpdateCheckinParams;

import java.util.List;

/**
 * 关闭所有进行中的训练营示例
 */
public class CloseAllCheckinsExample {
    public static void main(String[] args) {
        // 从参数获取 token 和 group_id
        String token = args.length > 0 ? args[0] : System.getenv("ZSXQ_TOKEN");
        String groupId = args.length > 1 ? args[1] : System.getenv("ZSXQ_GROUP_ID");

        if (token == null || groupId == null) {
            System.err.println("错误: 请提供 ZSXQ_TOKEN 和 ZSXQ_GROUP_ID");
            System.err.println("用法: java CloseAllCheckinsExample <token> <group_id>");
            System.exit(1);
        }

        try {
            // 创建客户端
            ZsxqClient client = new ZsxqClientBuilder()
                    .token(token)
                    .build();

            System.out.println("开始关闭所有进行中的训练营...");
            System.out.println("Group ID: " + groupId);
            System.out.println("========================================\n");

            // 1. 查询所有进行中的训练营
            System.out.println("正在查询进行中的训练营...");
            List<Checkin> ongoingCheckins = client.checkins().list(
                    groupId,
                    new ListCheckinsOptions().scope("ongoing").count(100)
            );

            if (ongoingCheckins.isEmpty()) {
                System.out.println("没有找到进行中的训练营，无需关闭");
                return;
            }

            System.out.println("找到 " + ongoingCheckins.size() + " 个进行中的训练营\n");

            // 2. 逐个关闭训练营
            int successCount = 0;
            int failCount = 0;

            for (int i = 0; i < ongoingCheckins.size(); i++) {
                Checkin checkin = ongoingCheckins.get(i);
                System.out.println("【" + (i + 1) + "/" + ongoingCheckins.size() + "】正在关闭训练营...");
                System.out.println("  ID: " + checkin.getCheckinId());
                System.out.println("  标题: " + checkin.getTitle());
                System.out.println("  当前状态: " + checkin.getStatus());

                try {
                    // 调用 update 方法关闭训练营
                    UpdateCheckinParams params = new UpdateCheckinParams()
                            .status("closed");

                    Checkin updatedCheckin = client.checkins().update(
                            groupId,
                            String.valueOf(checkin.getCheckinId()),
                            params
                    );

                    if (updatedCheckin != null && "closed".equals(updatedCheckin.getStatus())) {
                        System.out.println("  ✅ 关闭成功！新状态: " + updatedCheckin.getStatus());
                        successCount++;
                    } else {
                        System.out.println("  ⚠️  关闭可能失败");
                        System.out.println("      返回对象: " + updatedCheckin);
                        if (updatedCheckin != null) {
                            System.out.println("      返回状态: " + updatedCheckin.getStatus());
                            System.out.println("      训练营ID: " + updatedCheckin.getCheckinId());
                            System.out.println("      标题: " + updatedCheckin.getTitle());
                        }
                        failCount++;
                    }
                } catch (Exception e) {
                    System.out.println("  ❌ 关闭失败: " + e.getMessage());
                    System.out.println("      异常类型: " + e.getClass().getName());
                    if (e.getCause() != null) {
                        System.out.println("      根本原因: " + e.getCause().getMessage());
                    }
                    e.printStackTrace();
                    failCount++;
                }

                System.out.println("----------------------------------------");

                // 避免请求过快，稍微延迟
                if (i < ongoingCheckins.size() - 1) {
                    Thread.sleep(200);
                }
            }

            // 3. 输出统计结果
            System.out.println("\n========================================");
            System.out.println("关闭操作完成！");
            System.out.println("  总数: " + ongoingCheckins.size());
            System.out.println("  成功: " + successCount);
            System.out.println("  失败: " + failCount);
            System.out.println("========================================");

        } catch (Exception e) {
            System.err.println("操作失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
