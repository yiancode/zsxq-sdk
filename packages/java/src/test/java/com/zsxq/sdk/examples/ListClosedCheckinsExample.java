package com.zsxq.sdk.examples;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.request.CheckinsRequest.ListCheckinsOptions;

import java.util.List;

/**
 * 查询已关闭的训练营
 */
public class ListClosedCheckinsExample {
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

            System.out.println("查询已关闭的训练营...");
            System.out.println("========================================\n");

            List<Checkin> closedCheckins = client.checkins().list(
                    groupId,
                    new ListCheckinsOptions().scope("closed").count(20)
            );

            System.out.println("找到 " + closedCheckins.size() + " 个已关闭的训练营:\n");

            for (int i = 0; i < Math.min(closedCheckins.size(), 10); i++) {
                Checkin checkin = closedCheckins.get(i);
                System.out.println("【" + (i + 1) + "】");
                System.out.println("  ID: " + checkin.getCheckinId());
                System.out.println("  标题: " + checkin.getTitle());
                System.out.println("  状态: " + checkin.getStatus());
                System.out.println("  创建时间: " + checkin.getCreateTime());
                System.out.println();
            }

            if (closedCheckins.size() > 10) {
                System.out.println("... 还有 " + (closedCheckins.size() - 10) + " 个");
            }

        } catch (Exception e) {
            System.err.println("查询失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
