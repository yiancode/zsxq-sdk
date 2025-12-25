package com.zsxq.sdk.request;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.request.CheckinsRequest.UpdateCheckinParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 测试训练营更新功能（包括关闭）
 */
public class CheckinsUpdateTest {

    private String token;
    private String groupId;
    private ZsxqClient client;

    @BeforeEach
    void setUp() {
        token = System.getenv("ZSXQ_TOKEN");
        groupId = System.getenv("ZSXQ_GROUP_ID");

        assertNotNull(token, "ZSXQ_TOKEN 环境变量未设置");
        assertNotNull(groupId, "ZSXQ_GROUP_ID 环境变量未设置");

        // 创建客户端（禁用签名）
        client = new ZsxqClientBuilder()
                .token(token)
                .disableSignature()
                .build();
    }

    @Test
    void testUpdateCheckinStatus() {
        // 先创建一个测试训练营
        System.out.println("创建测试训练营...");
        CheckinsRequest.CreateCheckinParams createParams = new CheckinsRequest.CreateCheckinParams()
                .title("SDK测试训练营")
                .text("用于测试关闭功能")
                .checkinDays(7)
                .type("accumulated")
                .showTopicsOnTimeline(false)
                .expirationTime("2026-01-25T23:59:59.000+0800");

        Checkin newCheckin = client.checkins().create(groupId, createParams);
        assertNotNull(newCheckin, "创建训练营失败");
        System.out.println("训练营创建成功，ID: " + newCheckin.getCheckinId());
        System.out.println("初始状态: " + newCheckin.getStatus());

        String checkinId = String.valueOf(newCheckin.getCheckinId());

        // 验证初始状态
        assertEquals("ongoing", newCheckin.getStatus(), "新创建的训练营状态应为 ongoing");

        // 关闭训练营
        System.out.println("\n关闭训练营...");
        UpdateCheckinParams updateParams = new UpdateCheckinParams()
                .status("closed");

        Checkin updatedCheckin = client.checkins().update(groupId, checkinId, updateParams);

        // 注意：API 可能返回 null，但操作可能成功
        if (updatedCheckin != null) {
            System.out.println("更新响应: status = " + updatedCheckin.getStatus());
            assertEquals("closed", updatedCheckin.getStatus(), "关闭后状态应为 closed");
        } else {
            System.out.println("更新响应为 null，验证实际状态...");
        }

        // 验证最终状态
        System.out.println("\n验证最终状态...");
        Checkin verifiedCheckin = client.checkins().get(groupId, checkinId);
        assertNotNull(verifiedCheckin, "无法获取训练营信息");
        System.out.println("最终状态: " + verifiedCheckin.getStatus());

        assertEquals("closed", verifiedCheckin.getStatus(), "训练营应该已关闭");

        System.out.println("\n✅ 测试通过！训练营已成功关闭");
    }
}
