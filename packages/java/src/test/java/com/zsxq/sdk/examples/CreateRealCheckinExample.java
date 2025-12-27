package com.zsxq.sdk.examples;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.exception.ZsxqException;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.request.CheckinsRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 创建真实训练营的示例
 * 使用环境变量：ZSXQ_TOKEN, ZSXQ_GROUP_ID
 */
public class CreateRealCheckinExample {
    public static void main(String[] args) {
        String token = System.getenv("ZSXQ_TOKEN");
        String groupId = System.getenv("ZSXQ_GROUP_ID");

        if (token == null || groupId == null) {
            System.err.println("❌ 错误: 请设置环境变量 ZSXQ_TOKEN 和 ZSXQ_GROUP_ID");
            System.exit(1);
        }

        // 构建客户端
        ZsxqClient client = new ZsxqClientBuilder()
                .token(token)
                .build();

        try {
            // 计算过期时间：30天后
            LocalDateTime expirationDateTime = LocalDateTime.now().plusDays(30);
            String expirationTime = expirationDateTime.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS+0800")
            );

            System.out.println("=== 创建训练营测试 ===");
            System.out.println("星球 ID: " + groupId);
            System.out.println("过期时间: " + expirationTime);
            System.out.println();

            // 创建训练营参数
            CheckinsRequest.CreateCheckinParams params = new CheckinsRequest.CreateCheckinParams()
                    .title("SDK测试训练营")
                    .text("这是一个 SDK 创建的测试训练营，用于验证 req_data 包装功能")
                    .checkinDays(7)
                    .type("accumulated")  // 累计打卡
                    .showTopicsOnTimeline(false)
                    .expirationTime(expirationTime);

            System.out.println("📝 创建参数:");
            System.out.println("   - 标题: SDK测试训练营");
            System.out.println("   - 描述: 这是一个 SDK 创建的测试训练营");
            System.out.println("   - 天数: 7天");
            System.out.println("   - 类型: accumulated (累计打卡)");
            System.out.println("   - 时间线: 不展示");
            System.out.println("   - 有效期: " + expirationTime);
            System.out.println();

            // 创建训练营
            System.out.println("🚀 正在创建训练营...");
            Checkin checkin = client.checkins().create(groupId, params);

            // 输出结果
            System.out.println();
            System.out.println("✅ 训练营创建成功！");
            System.out.println("===================");
            System.out.println("训练营信息:");
            System.out.println("   - ID: " + checkin.getCheckinId());
            System.out.println("   - 标题: " + checkin.getTitle());
            System.out.println("   - 描述: " + checkin.getText());
            System.out.println("   - 状态: " + checkin.getStatus());
            System.out.println("   - 开始时间: " + checkin.getBeginTime());
            System.out.println("   - 结束时间: " + checkin.getEndTime());
            System.out.println();
            System.out.println("🎉 测试完成！训练营已成功创建到星球中。");

        } catch (ZsxqException e) {
            System.err.println();
            System.err.println("❌ 创建失败！");
            System.err.println("===================");
            System.err.println("错误信息: " + e.getMessage());
            System.err.println("错误码: " + e.getCode());
            if (e.getCause() != null) {
                System.err.println("原因: " + e.getCause().getMessage());
            }
            System.exit(1);
        }
    }
}
