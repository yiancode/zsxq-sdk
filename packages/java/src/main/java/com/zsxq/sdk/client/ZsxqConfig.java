package com.zsxq.sdk.client;

import lombok.Builder;
import lombok.Getter;

/**
 * SDK 配置
 */
@Getter
@Builder
public class ZsxqConfig {

    /**
     * 用户认证 Token（必需）
     */
    private final String token;

    /**
     * API 基础 URL
     */
    @Builder.Default
    private final String baseUrl = "https://api.zsxq.com";

    /**
     * 请求超时（毫秒）
     */
    @Builder.Default
    private final int timeout = 10000;

    /**
     * 重试次数
     */
    @Builder.Default
    private final int retryCount = 3;

    /**
     * 重试间隔（毫秒）
     */
    @Builder.Default
    private final int retryDelay = 1000;

    /**
     * 设备 ID
     */
    @Builder.Default
    private final String deviceId = java.util.UUID.randomUUID().toString();

    /**
     * App 版本号
     */
    @Builder.Default
    private final String appVersion = "2.83.0";
}
