package com.zsxq.sdk.examples;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * 测试签名生成
 */
public class TestSignatureExample {
    public static void main(String[] args) {
        // 从用户提供的原始请求中提取的参数
        String timestamp = "1766675880";
        String method = "PUT";
        String path = "/v2/groups/51115214421144/checkins/5454884814";
        String body = "{\"req_data\":{\"status\":\"closed\"}}";
        String expectedSignature = "5c3f56a1b62a0abd49477281418def31da65b126";

        System.out.println("=== 签名测试 ===");
        System.out.println("时间戳: " + timestamp);
        System.out.println("方法: " + method);
        System.out.println("路径: " + path);
        System.out.println("请求体: " + body);
        System.out.println("期望签名: " + expectedSignature);
        System.out.println("\n=================================\n");

        // 尝试不同的密钥
        String[] testKeys = {
            "zsxq-sdk-secret",
            "",  // 空密钥
            "xiaomiquan",
            "zsxq",
            "api-secret",
            // 如果有其他已知的可能密钥，可以添加在这里
        };

        for (String key : testKeys) {
            String signature = generateSignature(timestamp, method, path, body, key);
            boolean matches = signature.equals(expectedSignature);
            System.out.println("密钥: '" + key + "'");
            System.out.println("  生成签名: " + signature);
            System.out.println("  匹配: " + (matches ? "✅ 是" : "❌ 否"));
            System.out.println();

            if (matches) {
                System.out.println("🎉 找到正确的密钥: " + key);
                return;
            }
        }

        System.out.println("⚠️  未找到匹配的密钥");
        System.out.println("\n签名数据格式:");
        System.out.println("---");
        System.out.println(timestamp);
        System.out.println(method);
        System.out.println(path);
        System.out.println(body);
        System.out.println("---");
    }

    private static String generateSignature(String timestamp, String method, String path, String body, String key) {
        try {
            StringBuilder signData = new StringBuilder()
                    .append(timestamp).append("\n")
                    .append(method.toUpperCase()).append("\n")
                    .append(path);
            if (body != null && !body.isEmpty()) {
                signData.append("\n").append(body);
            }

            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA1"));
            byte[] hash = mac.doFinal(signData.toString().getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}
