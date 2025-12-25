package com.zsxq.sdk.examples;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.request.CheckinsRequest.CreateCheckinParams;
import com.zsxq.sdk.request.CheckinsRequest.ListCheckinsOptions;
import com.zsxq.sdk.request.CheckinsRequest.UpdateCheckinParams;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * 关闭所有训练营并创建测试训练营的综合示例
 */
public class CloseAllAndCreateTestCheckinExample {
    public static void main(String[] args) {
        // 从参数获取 token 和 group_id
        String token = args.length > 0 ? args[0] : System.getenv("ZSXQ_TOKEN");
        String groupId = args.length > 1 ? args[1] : System.getenv("ZSXQ_GROUP_ID");

        if (token == null || groupId == null) {
            System.err.println("错误: 请提供 ZSXQ_TOKEN 和 ZSXQ_GROUP_ID");
            System.err.println("用法: java CloseAllAndCreateTestCheckinExample <token> <group_id>");
            System.exit(1);
        }

        try {
            // 创建客户端（禁用签名）
            ZsxqClient client = new ZsxqClientBuilder()
                    .token(token)
                    .disableSignature()
                    .build();

            System.out.println("========================================");
            System.out.println("开始执行任务");
            System.out.println("Group ID: " + groupId);
            System.out.println("========================================\n");

            // ============================
            // 步骤 1: 查询所有进行中的训练营
            // ============================
            System.out.println("【步骤 1】查询所有进行中的训练营...\n");
            List<Checkin> ongoingCheckins = client.checkins().list(
                    groupId,
                    new ListCheckinsOptions().scope("ongoing").count(100)
            );

            if (ongoingCheckins.isEmpty()) {
                System.out.println("✅ 没有找到进行中的训练营，无需关闭\n");
            } else {
                System.out.println("找到 " + ongoingCheckins.size() + " 个进行中的训练营:\n");

                for (int i = 0; i < ongoingCheckins.size(); i++) {
                    Checkin checkin = ongoingCheckins.get(i);
                    System.out.println("  " + (i + 1) + ". " + checkin.getTitle());
                    System.out.println("     ID: " + checkin.getCheckinId());
                    System.out.println("     状态: " + checkin.getStatus());
                }
                System.out.println();

                // ============================
                // 步骤 2: 关闭所有进行中的训练营
                // ============================
                System.out.println("【步骤 2】关闭所有进行中的训练营...\n");

                int successCount = 0;
                int failCount = 0;

                for (int i = 0; i < ongoingCheckins.size(); i++) {
                    Checkin checkin = ongoingCheckins.get(i);
                    System.out.println("正在关闭 [" + (i + 1) + "/" + ongoingCheckins.size() + "]: " + checkin.getTitle());

                    try {
                        UpdateCheckinParams params = new UpdateCheckinParams()
                                .status("closed");

                        Checkin updatedCheckin = client.checkins().update(
                                groupId,
                                String.valueOf(checkin.getCheckinId()),
                                params
                        );

                        if (updatedCheckin != null && "closed".equals(updatedCheckin.getStatus())) {
                            System.out.println("  ✅ 关闭成功");
                            successCount++;
                        } else {
                            System.out.println("  ⚠️  关闭可能失败（返回状态异常）");
                            failCount++;
                        }
                    } catch (Exception e) {
                        System.out.println("  ❌ 关闭失败: " + e.getMessage());
                        failCount++;
                    }

                    // 避免请求过快
                    if (i < ongoingCheckins.size() - 1) {
                        Thread.sleep(200);
                    }
                }

                System.out.println("\n关闭结果统计:");
                System.out.println("  总数: " + ongoingCheckins.size());
                System.out.println("  成功: " + successCount);
                System.out.println("  失败: " + failCount);
                System.out.println();
            }

            // ============================
            // 步骤 3: 创建新的测试训练营
            // ============================
            System.out.println("【步骤 3】创建新的测试训练营...\n");

            // 设置过期时间为 30 天后
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 30);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            String expirationTime = sdf.format(calendar.getTime());

            CreateCheckinParams createParams = new CreateCheckinParams()
                    .title("测试训练营")
                    .text("自动创建的测试训练营")
                    .checkinDays(7)
                    .type("accumulated")  // 累计打卡模式
                    .showTopicsOnTimeline(false)
                    .expirationTime(expirationTime);

            try {
                Checkin newCheckin = client.checkins().create(groupId, createParams);

                if (newCheckin != null) {
                    System.out.println("✅ 测试训练营创建成功！");
                    System.out.println("\n训练营信息:");
                    System.out.println("  ID: " + newCheckin.getCheckinId());
                    System.out.println("  标题: " + newCheckin.getTitle());
                    System.out.println("  描述: " + newCheckin.getText());
                    System.out.println("  状态: " + newCheckin.getStatus());
                    System.out.println("  开始时间: " + newCheckin.getBeginTime());
                    System.out.println("  结束时间: " + newCheckin.getEndTime());
                } else {
                    System.out.println("❌ 创建失败，API 返回 null");
                }
            } catch (Exception e) {
                System.out.println("❌ 创建失败: " + e.getMessage());
                e.printStackTrace();
            }

            System.out.println("\n========================================");
            System.out.println("任务执行完成！");
            System.out.println("========================================");

        } catch (Exception e) {
            System.err.println("\n操作失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
