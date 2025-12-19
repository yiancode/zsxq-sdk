package com.zsxq.sdk.request;

import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.model.Checkin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试打卡列表的 count 参数行为
 */
public class CheckinCountTest {

    private ZsxqClient client;
    private String token;
    private String groupId;

    @BeforeEach
    public void setUp() {
        token = System.getenv("ZSXQ_TOKEN");
        groupId = System.getenv("ZSXQ_GROUP_ID");

        if (token == null || token.isEmpty()) {
            throw new IllegalStateException("请设置环境变量 ZSXQ_TOKEN");
        }
        if (groupId == null || groupId.isEmpty()) {
            throw new IllegalStateException("请设置环境变量 ZSXQ_GROUP_ID");
        }

        client = new ZsxqClientBuilder()
                .token(token)
                .disableSignature()
                .build();
    }

    @Test
    public void testCountParameter() {
        System.out.println("=== 测试 count 参数的不同值 ===\n");

        // 测试1: count=10
        System.out.println("1. scope=all, count=10");
        CheckinsRequest.ListCheckinsOptions options1 = new CheckinsRequest.ListCheckinsOptions()
                .scope("all")
                .count(10);
        List<Checkin> result1 = client.checkins().list(groupId, options1);
        System.out.println("   返回数量: " + result1.size());
        System.out.println();

        // 测试2: count=100
        System.out.println("2. scope=all, count=100");
        CheckinsRequest.ListCheckinsOptions options2 = new CheckinsRequest.ListCheckinsOptions()
                .scope("all")
                .count(100);
        List<Checkin> result2 = client.checkins().list(groupId, options2);
        System.out.println("   返回数量: " + result2.size());
        System.out.println();

        // 测试3: count=200
        System.out.println("3. scope=all, count=200");
        CheckinsRequest.ListCheckinsOptions options3 = new CheckinsRequest.ListCheckinsOptions()
                .scope("all")
                .count(200);
        try {
            List<Checkin> result3 = client.checkins().list(groupId, options3);
            System.out.println("   返回数量: " + result3.size() + " ✅");
        } catch (Exception e) {
            System.out.println("   错误: " + e.getMessage() + " ❌");
        }
        System.out.println();

        // 测试4: count=500
        System.out.println("4. scope=all, count=500");
        CheckinsRequest.ListCheckinsOptions options4 = new CheckinsRequest.ListCheckinsOptions()
                .scope("all")
                .count(500);
        try {
            List<Checkin> result4 = client.checkins().list(groupId, options4);
            System.out.println("   返回数量: " + result4.size() + " ✅");
        } catch (Exception e) {
            System.out.println("   错误: " + e.getMessage() + " ❌");
        }
        System.out.println();

        // 总结
        System.out.println("\n=== 总结 ===");
        System.out.println("当前星球实际打卡项目数: " + result2.size());
        System.out.println("- count=10: 返回 " + result1.size());
        System.out.println("- count=100: 返回 " + result2.size());
        System.out.println("\n结论: API 限制 count 参数的最大值,需要找到合适的上限");
    }
}
