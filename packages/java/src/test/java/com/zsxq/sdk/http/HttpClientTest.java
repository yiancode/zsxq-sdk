package com.zsxq.sdk.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zsxq.sdk.client.ZsxqConfig;
import com.zsxq.sdk.exception.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientTest {

    private MockWebServer mockServer;
    private HttpClient httpClient;
    private Gson gson;

    @BeforeEach
    void setUp() throws Exception {
        mockServer = new MockWebServer();
        mockServer.start();

        String baseUrl = mockServer.url("/").toString().replaceAll("/$", "");

        ZsxqConfig config = ZsxqConfig.builder()
            .token("test-token")
            .baseUrl(baseUrl)
            .timeout(5000)
            .retryCount(2)
            .retryDelay(10) // 快速重试以加速测试
            .build();

        httpClient = new HttpClient(config);
        gson = new Gson();
    }

    @AfterEach
    void tearDown() throws Exception {
        mockServer.shutdown();
    }

    @Test
    void testGetRequestSuccess() throws InterruptedException {
        // 准备响应
        Map<String, Object> respData = new HashMap<>();
        respData.put("value", "test");
        Map<String, Object> response = createSuccessResponse(respData);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(response))
            .setHeader("Content-Type", "application/json"));

        // 执行请求
        Map<String, Object> result = httpClient.get(
            "/v2/test",
            new TypeToken<Map<String, Object>>(){}.getType()
        );

        // 验证结果
        assertNotNull(result);
        assertEquals("test", result.get("value"));

        // 验证请求头
        RecordedRequest request = mockServer.takeRequest();
        assertEquals("/v2/test", request.getPath());
        assertEquals("GET", request.getMethod());
        assertNotNull(request.getHeader("authorization"));
        assertEquals("test-token", request.getHeader("authorization"));
        assertNotNull(request.getHeader("x-timestamp"));
        assertNotNull(request.getHeader("x-signature"));
        assertNotNull(request.getHeader("x-request-id"));
    }

    @Test
    void testGetRequestWithParams() throws InterruptedException {
        Map<String, Object> response = createSuccessResponse(new HashMap<>());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(response))
            .setHeader("Content-Type", "application/json"));

        Map<String, Object> params = new HashMap<>();
        params.put("count", 10);
        params.put("scope", "all");

        httpClient.get(
            "/v2/test",
            params,
            new TypeToken<Map<String, Object>>(){}.getType()
        );

        RecordedRequest request = mockServer.takeRequest();
        String path = request.getPath();
        assertTrue(path.contains("count=10"));
        assertTrue(path.contains("scope=all"));
    }

    @Test
    void testPostRequestSuccess() throws InterruptedException {
        Map<String, Object> response = createSuccessResponse(Map.of("id", 123));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(response))
            .setHeader("Content-Type", "application/json"));

        Map<String, Object> postData = Map.of("name", "test");
        Map<String, Object> result = httpClient.post(
            "/v2/test",
            postData,
            new TypeToken<Map<String, Object>>(){}.getType()
        );

        assertNotNull(result);
        assertEquals(123.0, ((Number) result.get("id")).doubleValue());

        RecordedRequest request = mockServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertTrue(request.getBody().readUtf8().contains("test"));
    }

    @Test
    void testBusinessErrorResponse() {
        Map<String, Object> response = new HashMap<>();
        response.put("succeeded", false);
        response.put("code", 10001);
        response.put("error", "Token 无效");

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(response))
            .setHeader("Content-Type", "application/json"));

        assertThrows(TokenInvalidException.class, () ->
            httpClient.get("/v2/test", new TypeToken<Map<String, Object>>(){}.getType())
        );
    }

    @Test
    void testTokenExpiredException() {
        Map<String, Object> response = new HashMap<>();
        response.put("succeeded", false);
        response.put("code", 10002);
        response.put("error", "Token 已过期");

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(response))
            .setHeader("Content-Type", "application/json"));

        assertThrows(TokenExpiredException.class, () ->
            httpClient.get("/v2/test", new TypeToken<Map<String, Object>>(){}.getType())
        );
    }

    @Test
    void testNetworkError() {
        mockServer.enqueue(new MockResponse()
            .setResponseCode(500)
            .setBody("Internal Server Error"));

        assertThrows(ZsxqException.class, () ->
            httpClient.get("/v2/test", new TypeToken<Map<String, Object>>(){}.getType())
        );
    }

    @Test
    void testRetryOnNetworkError() {
        // 第一次和第二次失败，第三次成功
        mockServer.enqueue(new MockResponse()
            .setResponseCode(500));
        mockServer.enqueue(new MockResponse()
            .setResponseCode(500));
        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(Map.of("retry", "success"))))
            .setHeader("Content-Type", "application/json"));

        Map<String, Object> result = httpClient.get(
            "/v2/test",
            new TypeToken<Map<String, Object>>(){}.getType()
        );

        assertNotNull(result);
        assertEquals("success", result.get("retry"));
        assertEquals(3, mockServer.getRequestCount());
    }

    @Test
    void testNoRetryOnBusinessError() {
        Map<String, Object> response = new HashMap<>();
        response.put("succeeded", false);
        response.put("code", 10001);
        response.put("error", "Token 无效");

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(response))
            .setHeader("Content-Type", "application/json"));

        assertThrows(TokenInvalidException.class, () ->
            httpClient.get("/v2/test", new TypeToken<Map<String, Object>>(){}.getType())
        );

        assertEquals(1, mockServer.getRequestCount());
    }

    @Test
    void testMaxRetryReached() {
        // 所有请求都失败
        mockServer.enqueue(new MockResponse().setResponseCode(500));
        mockServer.enqueue(new MockResponse().setResponseCode(500));
        mockServer.enqueue(new MockResponse().setResponseCode(500));

        assertThrows(ZsxqException.class, () ->
            httpClient.get("/v2/test", new TypeToken<Map<String, Object>>(){}.getType())
        );

        assertEquals(3, mockServer.getRequestCount()); // 原始请求 + 2次重试
    }

    @Test
    void testSignatureGeneration() throws InterruptedException {
        Map<String, Object> response = createSuccessResponse(new HashMap<>());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(response))
            .setHeader("Content-Type", "application/json"));

        httpClient.get("/v2/test", new TypeToken<Map<String, Object>>(){}.getType());

        RecordedRequest request = mockServer.takeRequest();
        String signature = request.getHeader("x-signature");

        assertNotNull(signature);
        assertEquals(40, signature.length()); // HMAC-SHA1 hex = 40 chars
        assertTrue(signature.matches("^[a-f0-9]{40}$"));
    }

    @Test
    void testRequestIdGeneration() throws InterruptedException {
        Map<String, Object> response = createSuccessResponse(new HashMap<>());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(response))
            .setHeader("Content-Type", "application/json"));

        httpClient.get("/v2/test", new TypeToken<Map<String, Object>>(){}.getType());

        RecordedRequest request = mockServer.takeRequest();
        String requestId = request.getHeader("x-request-id");

        assertNotNull(requestId);
        // UUID 格式检查
        assertTrue(requestId.matches(
            "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"
        ));
    }

    @Test
    void testEmptyResponseData() {
        Map<String, Object> response = createSuccessResponse(null);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(response))
            .setHeader("Content-Type", "application/json"));

        Map<String, Object> result = httpClient.get(
            "/v2/test",
            new TypeToken<Map<String, Object>>(){}.getType()
        );

        assertNull(result);
    }

    @Test
    void testRateLimitException() {
        Map<String, Object> response = new HashMap<>();
        response.put("succeeded", false);
        response.put("code", 40001);
        response.put("error", "请求过于频繁");

        mockServer.enqueue(new MockResponse()
            .setResponseCode(429)
            .setBody(gson.toJson(response))
            .setHeader("Content-Type", "application/json"));

        RateLimitException exception = assertThrows(RateLimitException.class, () ->
            httpClient.get("/v2/test", new TypeToken<Map<String, Object>>(){}.getType())
        );

        assertEquals(40001, exception.getCode());
    }

    // Helper method
    private Map<String, Object> createSuccessResponse(Object respData) {
        Map<String, Object> response = new HashMap<>();
        response.put("succeeded", true);
        response.put("resp_data", respData);
        return response;
    }
}
