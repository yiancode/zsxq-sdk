package com.zsxq.sdk.examples;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.request.CheckinsRequest.ListCheckinsOptions;

import java.util.List;

/**
 * 查询进行中的训练营示例
 */
public class ListOngoingCheckinsExample {
    public static void main(String[] args) {
        // 从参数获取 token 和 group_id
        String token = args.length > 0 ? args[0] : System.getenv("ZSXQ_TOKEN");
        String groupId = args.length > 1 ? args[1] : System.getenv("ZSXQ_GROUP_ID");

        if (token == null || groupId == null) {
            System.err.println("错误: 请提供 ZSXQ_TOKEN 和 ZSXQ_GROUP_ID");
            System.err.println("用法: java ListOngoingCheckinsExample <token> <group_id>");
            System.exit(1);
        }

        try {
            // 创建客户端
            ZsxqClient client = new ZsxqClientBuilder()
                    .token(token)
                    .build();

            System.out.println("正在查询进行中的训练营...");
            System.out.println("Group ID: " + groupId);
            System.out.println("========================================");

            // 查询进行中的训练营
            List<Checkin> ongoingCheckins = client.checkins().list(
                    groupId,
                    new ListCheckinsOptions().scope("ongoing").count(100)
            );

            if (ongoingCheckins.isEmpty()) {
                System.out.println("没有找到进行中的训练营");
            } else {
                System.out.println("找到 " + ongoingCheckins.size() + " 个进行中的训练营:\n");

                for (int i = 0; i < ongoingCheckins.size(); i++) {
                    Checkin checkin = ongoingCheckins.get(i);
                    System.out.println("【训练营 " + (i + 1) + "】");
                    System.out.println("  ID: " + checkin.getCheckinId());
                    System.out.println("  标题: " + checkin.getTitle());
                    System.out.println("  描述: " + checkin.getText());
                    System.out.println("  状态: " + checkin.getStatus());
                    System.out.println("  开始时间: " + checkin.getBeginTime());
                    System.out.println("  结束时间: " + checkin.getEndTime());
                    System.out.println("  封面: " + checkin.getCoverUrl());
                    System.out.println("  创建时间: " + checkin.getCreateTime());
                    if (checkin.getOwner() != null) {
                        System.out.println("  创建者: " + checkin.getOwner().getName());
                    }
                    System.out.println("========================================");
                }
            }

        } catch (Exception e) {
            System.err.println("查询失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
