package com.zsxq.sdk.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zsxq.sdk.client.ZsxqClient;
import com.zsxq.sdk.client.ZsxqClientBuilder;
import com.zsxq.sdk.exception.ZsxqException;
import com.zsxq.sdk.model.Checkin;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * 测试 Checkin 模型的 name 字段
 */
public class CheckinNameTest {

    private ZsxqClient client;
    private String token;
    private String groupId;
    private Gson gson;

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
                .disableSignature()  // 禁用签名
                .build();

        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Test
    public void testCheckinName() {
        System.out.println("=== 测试 Checkin 模型的 name 字段 ===\n");
        System.out.println("GroupId: " + groupId + "\n");

        // 先用原生 OkHttp 直接调用 API 看看返回什么
        try {
            System.out.println("正在使用 OkHttp 直接调用 API...");
            OkHttpClient httpClient = new OkHttpClient();
            String url = "https://api.zsxq.com/v2/groups/" + groupId + "/checkins?scope=ongoing&count=10";

            Request request = new Request.Builder()
                    .url(url)
                    .header("authorization", token)
                    .header("x-version", "2.83.0")
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                String body = response.body().string();
                System.out.println("HTTP Status: " + response.code());
                System.out.println("Response Body:");
                System.out.println(body);
                System.out.println();

                if (response.isSuccessful()) {
                    Map<String, Object> responseMap = gson.fromJson(body, Map.class);
                    Object respData = responseMap.get("resp_data");
                    if (respData != null) {
                        Map<String, Object> data = (Map<String, Object>) respData;
                        Object checkinsObj = data.get("checkins");
                        if (checkinsObj != null) {
                            List<Map<String, Object>> checkins = (List<Map<String, Object>>) checkinsObj;
                            System.out.println("找到 " + checkins.size() + " 个打卡项目\n");

                            for (int i = 0; i < Math.min(3, checkins.size()); i++) {
                                Map<String, Object> checkin = checkins.get(i);
                                System.out.println("打卡项目 #" + (i + 1) + ":");
                                System.out.println("  - checkin_id: " + checkin.get("checkin_id"));
                                System.out.println("  - title (API字段): " + checkin.get("title"));
                                System.out.println("  - description: " + checkin.get("text"));
                                System.out.println("  - status: " + checkin.get("status"));
                                System.out.println();
                            }

                            // 测试 Gson 反序列化
                            System.out.println("\n=== 测试 Gson 反序列化到 Checkin 模型 ===\n");
                            String checkinsJson = gson.toJson(checkinsObj);
                            com.google.gson.reflect.TypeToken<List<com.zsxq.sdk.model.Checkin>> typeToken =
                                    new com.google.gson.reflect.TypeToken<List<com.zsxq.sdk.model.Checkin>>() {};
                            List<com.zsxq.sdk.model.Checkin> checkinModels = gson.fromJson(checkinsJson, typeToken.getType());

                            for (int i = 0; i < Math.min(3, checkinModels.size()); i++) {
                                com.zsxq.sdk.model.Checkin checkin = checkinModels.get(i);
                                System.out.println("Checkin 模型 #" + (i + 1) + ":");
                                System.out.println("  - checkinId: " + checkin.getCheckinId());
                                System.out.println("  - title (映射自title): " + checkin.getTitle());
                                System.out.println("  - text: " + checkin.getText());
                                System.out.println("  - status: " + checkin.getStatus());
                                System.out.println();
                            }

                            // 总结
                            System.out.println("\n=== 总结 ===");
                            System.out.println("name 字段的含义：训练营/打卡项目的标题名称");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("直接调用 API 失败: " + e.getMessage());
            e.printStackTrace();
        }

        // 再尝试用 SDK
        System.out.println("\n\n=== 使用 SDK 调用 ===\n");
        try {
            System.out.println("正在获取打卡项目列表（默认参数：scope=ongoing, count=20）...");
            System.out.println("调用路径: /v2/groups/" + groupId + "/checkins");
            System.out.println();

            // 测试不带参数的调用（应该使用默认参数）
            List<Checkin> checkins = client.checkins().list(groupId);
            testCheckinList(checkins);
        } catch (ZsxqException e) {
            System.err.println("SDK 调用失败: " + e.getMessage());
            System.err.println("错误码: " + e.getCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("获取打卡列表失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void testCheckinList(List<Checkin> checkins) {

        System.out.println("找到 " + checkins.size() + " 个打卡项目\n");

        if (checkins.isEmpty()) {
            System.out.println("当前星球没有进行中的打卡项目");
            System.out.println("提示：name 字段应该是训练营/打卡项目的名称，例如：\"21天打卡挑战\"");
            return;
        }

        // 打印每个打卡项目的 title 字段
        for (int i = 0; i < checkins.size(); i++) {
            Checkin checkin = checkins.get(i);
            System.out.println("打卡项目 #" + (i + 1) + ":");
            System.out.println("  - checkin_id: " + checkin.getCheckinId());
            System.out.println("  - title: " + checkin.getTitle());
            System.out.println("  - text: " + (checkin.getText() != null ?
                    (checkin.getText().length() > 50 ?
                            checkin.getText().substring(0, 50) + "..." :
                            checkin.getText()) : "null"));
            System.out.println("  - status: " + checkin.getStatus());
            System.out.println("  - createTime: " + checkin.getCreateTime());
            System.out.println();
        }

        // 总结
        System.out.println("\n=== 总结 ===");
        System.out.println("name 字段的含义：训练营/打卡项目的标题名称");
        System.out.println("name 字段示例数据：");
        checkins.stream()
                .limit(3)
                .forEach(c -> System.out.println("  - " + c.getTitle()));
    }
}
