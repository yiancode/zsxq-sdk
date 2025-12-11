package com.zsxq.sdk.http;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.zsxq.sdk.client.ZsxqConfig;
import com.zsxq.sdk.exception.ExceptionFactory;
import com.zsxq.sdk.exception.NetworkException;
import com.zsxq.sdk.exception.ZsxqException;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * HTTP 客户端 - 处理请求、签名、重试
 */
public class HttpClient {

    private static final Logger log = LoggerFactory.getLogger(HttpClient.class);
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String SECRET_KEY = "zsxq-sdk-secret";

    private final OkHttpClient client;
    private final ZsxqConfig config;
    private final Gson gson;

    public HttpClient(ZsxqConfig config) {
        this.config = config;
        this.client = createOkHttpClient(config);
        this.gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    /**
     * 创建 OkHttpClient（带 SSL 信任配置）
     */
    private OkHttpClient createOkHttpClient(ZsxqConfig config) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(config.getTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(config.getTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(config.getTimeout(), TimeUnit.MILLISECONDS);

        // 配置信任所有证书（用于开发测试）
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                        @Override
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
        } catch (Exception e) {
            log.warn("Failed to configure SSL trust, using default settings", e);
        }

        return builder.build();
    }

    /**
     * GET 请求
     */
    public <T> T get(String path, Type responseType) {
        return get(path, null, responseType);
    }

    /**
     * GET 请求（带参数）
     */
    public <T> T get(String path, Map<String, Object> params, Type responseType) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(config.getBaseUrl() + path).newBuilder();
        if (params != null) {
            params.forEach((key, value) -> {
                if (value != null) {
                    urlBuilder.addQueryParameter(key, String.valueOf(value));
                }
            });
        }

        String requestId = UUID.randomUUID().toString();
        HttpUrl url = urlBuilder.build();
        log.debug("GET Request URL: {}", url);

        Request request = new Request.Builder()
                .url(url)
                .headers(buildHeaders("GET", path, null, requestId))
                .get()
                .build();

        return executeWithRetry(request, responseType, requestId, 0);
    }

    /**
     * POST 请求
     */
    public <T> T post(String path, Object data, Type responseType) {
        String body = data != null ? gson.toJson(data) : null;
        String requestId = UUID.randomUUID().toString();

        RequestBody requestBody = body != null
                ? RequestBody.create(body, JSON)
                : RequestBody.create("", JSON);

        Request request = new Request.Builder()
                .url(config.getBaseUrl() + path)
                .headers(buildHeaders("POST", path, body, requestId))
                .post(requestBody)
                .build();

        return executeWithRetry(request, responseType, requestId, 0);
    }

    /**
     * 构建请求头
     */
    private Headers buildHeaders(String method, String path, String body, String requestId) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String signature = generateSignature(timestamp, method, path, body);

        return new Headers.Builder()
                .add("Content-Type", "application/json; charset=utf-8")
                .add("User-Agent", "xiaomiquan/" + config.getAppVersion() + " SDK/1.0.0")
                .add("authorization", config.getToken())
                .add("x-timestamp", timestamp)
                .add("x-signature", signature)
                .add("x-request-id", requestId)
                .add("x-version", config.getAppVersion())
                .add("x-aduid", config.getDeviceId())
                .build();
    }

    /**
     * 生成签名
     */
    private String generateSignature(String timestamp, String method, String path, String body) {
        try {
            StringBuilder signData = new StringBuilder()
                    .append(timestamp).append("\n")
                    .append(method.toUpperCase()).append("\n")
                    .append(path);
            if (body != null && !body.isEmpty()) {
                signData.append("\n").append(body);
            }

            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA1"));
            byte[] hash = mac.doFinal(signData.toString().getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate signature", e);
        }
    }

    /**
     * 执行请求（带重试）
     */
    private <T> T executeWithRetry(Request request, Type responseType, String requestId, int retryCount) {
        try {
            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";

                if (!response.isSuccessful()) {
                    // 优先尝试解析业务错误（例如限流）
                    try {
                        return handleResponse(responseBody, responseType, requestId);
                    } catch (ZsxqException e) {
                        throw e;
                    } catch (Exception ignore) {
                        // fall through to network handling
                    }

                    // 5xx 视为可重试的网络错误
                    if (response.code() >= 500 && retryCount < config.getRetryCount()) {
                        sleepBeforeRetry(retryCount);
                        return executeWithRetry(request, responseType, requestId, retryCount + 1);
                    }

                    throw new NetworkException("HTTP " + response.code() + ": " + response.message(), null, requestId);
                }

                return handleResponse(responseBody, responseType, requestId);
            }
        } catch (IOException e) {
            if (retryCount < config.getRetryCount()) {
                sleepBeforeRetry(retryCount);
                return executeWithRetry(request, responseType, requestId, retryCount + 1);
            }
            throw new NetworkException(e.getMessage(), e, requestId);
        }
    }

    private void sleepBeforeRetry(int retryCount) {
        try {
            long delay = config.getRetryDelay() * (long) Math.pow(2, retryCount);
            Thread.sleep(delay);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 处理响应
     */
    @SuppressWarnings("unchecked")
    private <T> T handleResponse(String responseBody, Type responseType, String requestId) {
        // 先解析为 JsonObject 以安全检查 succeeded 字段
        JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

        log.debug("API Response: {}", responseBody);

        boolean succeeded = jsonResponse.has("succeeded") && jsonResponse.get("succeeded").getAsBoolean();

        if (!succeeded) {
            // 处理错误响应
            int code = jsonResponse.has("code") ? jsonResponse.get("code").getAsInt() : 0;
            String message = "未知错误";
            if (jsonResponse.has("error") && !jsonResponse.get("error").isJsonNull()) {
                message = jsonResponse.get("error").getAsString();
            } else if (jsonResponse.has("info") && !jsonResponse.get("info").isJsonNull()) {
                message = jsonResponse.get("info").getAsString();
            }
            log.error("API Error: code={}, message={}", code, message);
            throw ExceptionFactory.create(code, message, requestId);
        }

        // 成功响应，解析 resp_data
        if (!jsonResponse.has("resp_data") || jsonResponse.get("resp_data").isJsonNull()) {
            return null;
        }

        // 处理 resp_data 为基础类型的情况（如 boolean、number）
        com.google.gson.JsonElement respData = jsonResponse.get("resp_data");
        log.debug("resp_data type: {}, isJsonPrimitive: {}", respData.getClass().getName(), respData.isJsonPrimitive());

        if (respData.isJsonPrimitive()) {
            com.google.gson.JsonPrimitive primitive = respData.getAsJsonPrimitive();
            // 如果是基础类型，直接返回（需要类型匹配）
            if (primitive.isBoolean()) {
                return (T) Boolean.valueOf(primitive.getAsBoolean());
            } else if (primitive.isNumber()) {
                return (T) primitive.getAsNumber();
            } else if (primitive.isString()) {
                return (T) primitive.getAsString();
            }
        }

        return gson.fromJson(respData, responseType);
    }

    /**
     * API 响应包装
     */
    private static class ZsxqResponse<T> {
        private boolean succeeded;
        private Integer code;
        private String error;
        private String info;
        private T respData;

        public boolean isSucceeded() { return succeeded; }
        public Integer getCode() { return code; }
        public String getError() { return error; }
        public String getInfo() { return info; }
        public T getRespData() { return respData; }
    }
}
