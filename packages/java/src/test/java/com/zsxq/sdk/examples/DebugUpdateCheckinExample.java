package com.zsxq.sdk.examples;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 调试更新训练营请求
 */
public class DebugUpdateCheckinExample {
    public static void main(String[] args) {
        String token = args.length > 0 ? args[0] : System.getenv("ZSXQ_TOKEN");
        String groupId = args.length > 1 ? args[1] : System.getenv("ZSXQ_GROUP_ID");
        String checkinId = args.length > 2 ? args[2] : "5454884484";

        if (token == null || groupId == null) {
            System.err.println("错误: 请提供 ZSXQ_TOKEN 和 ZSXQ_GROUP_ID");
            System.exit(1);
        }

        try {
            System.out.println("调试更新训练营请求");
            System.out.println("Group ID: " + groupId);
            System.out.println("Checkin ID: " + checkinId);
            System.out.println("========================================\n");

            // 构建请求体
            Map<String, Object> status = new HashMap<>();
            status.put("status", "closed");

            Map<String, Object> body = new HashMap<>();
            body.put("req_data", status);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonBody = gson.toJson(body);

            System.out.println("请求体:");
            System.out.println(jsonBody);
            System.out.println("\n========================================\n");

            // 发送请求
            OkHttpClient client = new OkHttpClient();

            String url = "https://api.zsxq.com/v2/groups/" + groupId + "/checkins/" + checkinId;
            System.out.println("请求 URL:");
            System.out.println(url);
            System.out.println("\n========================================\n");

            RequestBody requestBody = RequestBody.create(
                    jsonBody,
                    MediaType.parse("application/json; charset=utf-8")
            );

            long timestamp = System.currentTimeMillis() / 1000;

            Request request = new Request.Builder()
                    .url(url)
                    .put(requestBody)
                    .addHeader("authorization", token)
                    .addHeader("x-timestamp", String.valueOf(timestamp))
                    .addHeader("x-version", "2.83.0")
                    .addHeader("content-type", "application/json; charset=utf-8")
                    .build();

            System.out.println("请求头:");
            System.out.println("  authorization: " + token);
            System.out.println("  x-timestamp: " + timestamp);
            System.out.println("  x-version: 2.83.0");
            System.out.println("\n========================================\n");

            Response response = client.newCall(request).execute();

            System.out.println("响应状态码: " + response.code());
            System.out.println("\n响应体:");
            String responseBody = response.body().string();
            System.out.println(responseBody);

            // 格式化输出
            try {
                Map<String, Object> respMap = gson.fromJson(responseBody, Map.class);
                System.out.println("\n格式化响应:");
                System.out.println(gson.toJson(respMap));
            } catch (Exception e) {
                // 如果无法格式化，跳过
            }

        } catch (IOException e) {
            System.err.println("请求失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
