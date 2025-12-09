package com.zsxq.sdk.client;

/**
 * ZsxqClient 构建器
 *
 * 使用示例：
 * <pre>
 * ZsxqClient client = new ZsxqClientBuilder()
 *     .token("your-token")
 *     .timeout(10000)
 *     .retry(3)
 *     .build();
 * </pre>
 */
public class ZsxqClientBuilder {

    private String token;
    private String baseUrl = "https://api.zsxq.com";
    private int timeout = 10000;
    private int retryCount = 3;
    private int retryDelay = 1000;
    private String deviceId;
    private String appVersion = "2.83.0";

    /**
     * 设置认证 Token（必需）
     */
    public ZsxqClientBuilder token(String token) {
        this.token = token;
        return this;
    }

    /**
     * 设置 API 基础 URL
     */
    public ZsxqClientBuilder baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    /**
     * 设置请求超时（毫秒）
     */
    public ZsxqClientBuilder timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * 设置重试次数
     */
    public ZsxqClientBuilder retryCount(int count) {
        this.retryCount = count;
        return this;
    }

    /**
     * 设置重试间隔（毫秒）
     */
    public ZsxqClientBuilder retryDelay(int delay) {
        this.retryDelay = delay;
        return this;
    }

    /**
     * 设置重试配置
     */
    public ZsxqClientBuilder retry(int count) {
        this.retryCount = count;
        return this;
    }

    /**
     * 设置重试配置
     */
    public ZsxqClientBuilder retry(int count, int delay) {
        this.retryCount = count;
        this.retryDelay = delay;
        return this;
    }

    /**
     * 设置设备 ID
     */
    public ZsxqClientBuilder deviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    /**
     * 设置 App 版本号
     */
    public ZsxqClientBuilder appVersion(String version) {
        this.appVersion = version;
        return this;
    }

    /**
     * 构建 ZsxqClient 实例
     *
     * @throws IllegalArgumentException 如果 Token 未设置
     */
    public ZsxqClient build() {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token is required. Use token() to set it.");
        }

        if (deviceId == null) {
            deviceId = java.util.UUID.randomUUID().toString();
        }

        ZsxqConfig config = ZsxqConfig.builder()
                .token(token)
                .baseUrl(baseUrl)
                .timeout(timeout)
                .retryCount(retryCount)
                .retryDelay(retryDelay)
                .deviceId(deviceId)
                .appVersion(appVersion)
                .build();

        return new ZsxqClient(config);
    }
}
