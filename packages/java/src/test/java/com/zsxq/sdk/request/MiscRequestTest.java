package com.zsxq.sdk.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zsxq.sdk.model.Activity;
import com.zsxq.sdk.model.GlobalConfig;
import com.zsxq.sdk.model.PkBattle;
import com.zsxq.sdk.model.PkGroup;
import com.zsxq.sdk.model.UrlDetail;
import com.zsxq.sdk.http.HttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MiscRequestTest {

    private MockWebServer mockServer;
    private MiscRequest miscRequest;
    private Gson gson;

    @BeforeEach
    void setUp() throws Exception {
        mockServer = new MockWebServer();
        mockServer.start();

        String baseUrl = mockServer.url("/").toString().replaceAll("/$", "");

        com.zsxq.sdk.client.ZsxqConfig config = com.zsxq.sdk.client.ZsxqConfig.builder()
            .token("test-token")
            .baseUrl(baseUrl)
            .timeout(5000)
            .retryCount(1)
            .build();

        HttpClient httpClient = new HttpClient(config);
        miscRequest = new MiscRequest(httpClient);

        gson = new GsonBuilder().create();
    }

    @AfterEach
    void tearDown() throws Exception {
        mockServer.shutdown();
    }

    @Test
    void testGetPkGroupWithLongId() {
        Map<String, Object> group = new HashMap<>();
        group.put("pk_group_id", 123L);
        group.put("name", "测试PK群组");

        Map<String, Object> respData = new HashMap<>();
        respData.put("group", group);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        PkGroup result = miscRequest.getPkGroup(123L);

        assertNotNull(result);
    }

    @Test
    void testGetPkGroupWithStringId() throws InterruptedException {
        Map<String, Object> group = new HashMap<>();
        group.put("pk_group_id", 123L);

        Map<String, Object> respData = new HashMap<>();
        respData.put("group", group);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        PkGroup result = miscRequest.getPkGroup("123");

        assertNotNull(result);
        String path = mockServer.takeRequest().getPath();
        assertTrue(path.startsWith("/v2/pk_groups/123"));
    }

    @Test
    void testGetPkGroupNull() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("group", null);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        PkGroup result = miscRequest.getPkGroup(123L);

        assertNull(result);
    }

    @Test
    void testGetPkBattlesWithLongId() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("records", List.of(
            createPkBattleMap(1L, "对战1"),
            createPkBattleMap(2L, "对战2")
        ));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<PkBattle> battles = miscRequest.getPkBattles(123L);

        assertNotNull(battles);
        assertEquals(2, battles.size());
    }

    @Test
    void testGetPkBattlesWithStringId() throws InterruptedException {
        Map<String, Object> respData = new HashMap<>();
        respData.put("records", List.of());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<PkBattle> battles = miscRequest.getPkBattles("123");

        assertNotNull(battles);
        String path = mockServer.takeRequest().getPath();
        assertTrue(path.startsWith("/v2/pk_groups/123/records"));
    }

    @Test
    void testGetPkBattlesWithOptions() throws InterruptedException {
        Map<String, Object> respData = new HashMap<>();
        respData.put("records", List.of());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        MiscRequest.PkBattlesOptions options = new MiscRequest.PkBattlesOptions()
            .count(10);

        List<PkBattle> battles = miscRequest.getPkBattles(123L, options);

        assertNotNull(battles);
        String path = mockServer.takeRequest().getPath();
        assertTrue(path.contains("count=10"));
    }

    @Test
    void testGetPkBattlesEmpty() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("records", null);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<PkBattle> battles = miscRequest.getPkBattles(123L);

        assertNotNull(battles);
        assertTrue(battles.isEmpty());
    }

    @Test
    void testParseUrl() throws InterruptedException {
        Map<String, Object> urlDetail = new HashMap<>();
        urlDetail.put("url", "https://example.com");
        urlDetail.put("title", "示例网站");
        urlDetail.put("description", "这是一个示例");

        Map<String, Object> respData = new HashMap<>();
        respData.put("url_detail", urlDetail);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        UrlDetail result = miscRequest.parseUrl("https://example.com");

        assertNotNull(result);
        String path = mockServer.takeRequest().getPath();
        assertTrue(path.contains("/v2/url_details"));
        assertTrue(path.contains("url="));
    }

    @Test
    void testParseUrlNull() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("url_detail", null);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        UrlDetail result = miscRequest.parseUrl("https://invalid.com");

        assertNull(result);
    }

    @Test
    void testGetGlobalConfig() throws InterruptedException {
        Map<String, Object> respData = new HashMap<>();
        respData.put("app_version", "1.0.0");
        respData.put("feature_flags", new HashMap<>());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        GlobalConfig config = miscRequest.getGlobalConfig();

        assertNotNull(config);
        String path = mockServer.takeRequest().getPath();
        assertTrue(path.startsWith("/v2/settings"));
    }

    @Test
    void testGetActivities() throws InterruptedException {
        Map<String, Object> respData = new HashMap<>();
        respData.put("dynamics", List.of(
            createActivityMap(1L, "动态1"),
            createActivityMap(2L, "动态2")
        ));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<Activity> activities = miscRequest.getActivities();

        assertNotNull(activities);
        assertEquals(2, activities.size());
        String path = mockServer.takeRequest().getPath();
        assertTrue(path.startsWith("/v2/dynamics"));
    }

    @Test
    void testGetActivitiesWithOptions() throws InterruptedException {
        Map<String, Object> respData = new HashMap<>();
        respData.put("dynamics", List.of());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        MiscRequest.ActivitiesOptions options = new MiscRequest.ActivitiesOptions()
            .scope("general")
            .count(20)
            .endTime("2024-01-01T00:00:00.000+0800");

        List<Activity> activities = miscRequest.getActivities(options);

        assertNotNull(activities);
        String path = mockServer.takeRequest().getPath();
        assertTrue(path.contains("scope=general"));
        assertTrue(path.contains("count=20"));
        assertTrue(path.contains("end_time="));
    }

    @Test
    void testGetActivitiesEmpty() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("dynamics", null);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<Activity> activities = miscRequest.getActivities();

        assertNotNull(activities);
        assertTrue(activities.isEmpty());
    }

    @Test
    void testPkBattlesOptionsBuilder() {
        MiscRequest.PkBattlesOptions options = new MiscRequest.PkBattlesOptions()
            .count(15);

        Map<String, Object> map = options.toMap();

        assertEquals(15, map.get("count"));
    }

    @Test
    void testActivitiesOptionsBuilder() {
        MiscRequest.ActivitiesOptions options = new MiscRequest.ActivitiesOptions()
            .scope("like")
            .count(30)
            .endTime("2024-12-01T00:00:00.000+0800");

        Map<String, Object> map = options.toMap();

        assertEquals("like", map.get("scope"));
        assertEquals(30, map.get("count"));
        assertEquals("2024-12-01T00:00:00.000+0800", map.get("end_time"));
    }

    // Helper methods
    private Map<String, Object> createSuccessResponse(Object respData) {
        Map<String, Object> response = new HashMap<>();
        response.put("succeeded", true);
        response.put("resp_data", respData);
        return response;
    }

    private Map<String, Object> createPkBattleMap(Long id, String title) {
        Map<String, Object> battle = new HashMap<>();
        battle.put("record_id", id);
        battle.put("title", title);
        return battle;
    }

    private Map<String, Object> createActivityMap(Long id, String content) {
        Map<String, Object> activity = new HashMap<>();
        activity.put("activity_id", id);
        activity.put("content", content);
        return activity;
    }
}
