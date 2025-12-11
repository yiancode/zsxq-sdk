package com.zsxq.sdk.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZsxqClientBuilderTest {

    @Test
    void testBuildWithMinimalConfig() {
        ZsxqClient client = new ZsxqClientBuilder()
            .token("test-token")
            .build();

        assertNotNull(client);
        assertNotNull(client.groups());
        assertNotNull(client.topics());
        assertNotNull(client.users());
        assertNotNull(client.checkins());
        assertNotNull(client.dashboard());
    }

    @Test
    void testBuildWithoutToken() {
        ZsxqClientBuilder builder = new ZsxqClientBuilder();

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            builder::build
        );

        assertTrue(exception.getMessage().contains("Token is required"));
    }

    @Test
    void testBuildWithEmptyToken() {
        ZsxqClientBuilder builder = new ZsxqClientBuilder()
            .token("");

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            builder::build
        );

        assertTrue(exception.getMessage().contains("Token is required"));
    }

    @Test
    void testBuildWithNullToken() {
        ZsxqClientBuilder builder = new ZsxqClientBuilder()
            .token(null);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            builder::build
        );

        assertTrue(exception.getMessage().contains("Token is required"));
    }

    @Test
    void testBuildWithCustomBaseUrl() {
        ZsxqClient client = new ZsxqClientBuilder()
            .token("test-token")
            .baseUrl("https://custom.api.com")
            .build();

        assertNotNull(client);
    }

    @Test
    void testBuildWithCustomTimeout() {
        ZsxqClient client = new ZsxqClientBuilder()
            .token("test-token")
            .timeout(5000)
            .build();

        assertNotNull(client);
    }

    @Test
    void testBuildWithRetryConfig() {
        ZsxqClient client = new ZsxqClientBuilder()
            .token("test-token")
            .retryCount(5)
            .retryDelay(2000)
            .build();

        assertNotNull(client);
    }

    @Test
    void testBuildWithRetryConfigShorthand() {
        ZsxqClient client = new ZsxqClientBuilder()
            .token("test-token")
            .retry(5, 2000)
            .build();

        assertNotNull(client);
    }

    @Test
    void testBuildWithAppVersion() {
        ZsxqClient client = new ZsxqClientBuilder()
            .token("test-token")
            .appVersion("5.60.0")
            .build();

        assertNotNull(client);
    }

    @Test
    void testBuildWithDeviceId() {
        ZsxqClient client = new ZsxqClientBuilder()
            .token("test-token")
            .deviceId("custom-device-id")
            .build();

        assertNotNull(client);
    }

    @Test
    void testBuildWithAllConfigs() {
        ZsxqClient client = new ZsxqClientBuilder()
            .token("test-token")
            .baseUrl("https://custom.api.com")
            .timeout(8000)
            .retryCount(3)
            .retryDelay(1500)
            .appVersion("5.60.0")
            .deviceId("my-device")
            .build();

        assertNotNull(client);
    }

    @Test
    void testBuilderChaining() {
        ZsxqClientBuilder builder = new ZsxqClientBuilder();

        assertSame(builder, builder.token("test"));
        assertSame(builder, builder.baseUrl("url"));
        assertSame(builder, builder.timeout(1000));
        assertSame(builder, builder.retryCount(2));
        assertSame(builder, builder.retryDelay(500));
        assertSame(builder, builder.appVersion("1.0"));
        assertSame(builder, builder.deviceId("device"));
    }

    @Test
    void testBuildWithNegativeTimeout() {
        assertThrows(IllegalStateException.class, () -> new ZsxqClientBuilder()
            .token("test-token")
            .timeout(-1000)
            .build());
    }

    @Test
    void testBuildWithZeroTimeout() {
        ZsxqClient client = new ZsxqClientBuilder()
            .token("test-token")
            .timeout(0)
            .build();

        assertNotNull(client);
    }

    @Test
    void testBuildWithNegativeRetryCount() {
        ZsxqClient client = new ZsxqClientBuilder()
            .token("test-token")
            .retryCount(-1)
            .build();

        assertNotNull(client);
    }

    @Test
    void testBuildWithZeroRetryCount() {
        ZsxqClient client = new ZsxqClientBuilder()
            .token("test-token")
            .retryCount(0)
            .build();

        assertNotNull(client);
    }

    @Test
    void testBuildWithSpecialCharactersInToken() {
        ZsxqClient client = new ZsxqClientBuilder()
            .token("test-token-with-special-chars-!@#$%")
            .build();

        assertNotNull(client);
    }

    @Test
    void testBuildWithVeryLargeTimeout() {
        ZsxqClient client = new ZsxqClientBuilder()
            .token("test-token")
            .timeout(60000) // 60 seconds
            .build();

        assertNotNull(client);
    }

    @Test
    void testBuildWithVeryLargeRetryCount() {
        ZsxqClient client = new ZsxqClientBuilder()
            .token("test-token")
            .retryCount(10)
            .build();

        assertNotNull(client);
    }
}
