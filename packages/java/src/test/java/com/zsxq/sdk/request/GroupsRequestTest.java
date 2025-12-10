package com.zsxq.sdk.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zsxq.sdk.http.HttpClient;
import com.zsxq.sdk.model.Group;
import com.zsxq.sdk.model.User;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GroupsRequestTest {

    private MockWebServer mockServer;
    private GroupsRequest groupsRequest;
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
        groupsRequest = new GroupsRequest(httpClient);

        gson = new GsonBuilder().create();
    }

    @AfterEach
    void tearDown() throws Exception {
        mockServer.shutdown();
    }

    @Test
    void testList() {
        // 准备响应数据
        Map<String, Object> respData = new HashMap<>();
        respData.put("groups", List.of(
            createGroupMap(123L, "测试星球1"),
            createGroupMap(456L, "测试星球2")
        ));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        // 调用方法
        List<Group> groups = groupsRequest.list();

        // 断言
        assertNotNull(groups);
        assertEquals(2, groups.size());
        assertEquals("测试星球1", groups.get(0).getName());
        assertEquals("测试星球2", groups.get(1).getName());
    }

    @Test
    void testListEmpty() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("groups", List.of());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<Group> groups = groupsRequest.list();

        assertNotNull(groups);
        assertTrue(groups.isEmpty());
    }

    @Test
    void testGetWithLongId() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("group", createGroupMap(123L, "测试星球"));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        Group group = groupsRequest.get(123L);

        assertNotNull(group);
        assertEquals("测试星球", group.getName());
    }

    @Test
    void testGetWithStringId() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("group", createGroupMap(123L, "测试星球"));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        Group group = groupsRequest.get("123");

        assertNotNull(group);
        assertEquals("测试星球", group.getName());
    }

    @Test
    void testGetStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("members_count", 100);
        stats.put("topics_count", 50);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(stats)))
            .setHeader("Content-Type", "application/json"));

        Map<String, Object> result = groupsRequest.getStatistics(123L);

        assertNotNull(result);
        assertEquals(100.0, ((Number) result.get("members_count")).doubleValue());
        assertEquals(50.0, ((Number) result.get("topics_count")).doubleValue());
    }

    @Test
    void testGetMember() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("user", createUserMap(999L, "测试用户"));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        User user = groupsRequest.getMember(123L, 999L);

        assertNotNull(user);
        assertEquals("测试用户", user.getName());
    }

    @Test
    void testGetMemberNull() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("user", null);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        User user = groupsRequest.getMember(123L, 999L);

        assertNull(user);
    }

    // Helper methods
    private Map<String, Object> createSuccessResponse(Object respData) {
        Map<String, Object> response = new HashMap<>();
        response.put("succeeded", true);
        response.put("resp_data", respData);
        return response;
    }

    private Map<String, Object> createGroupMap(Long id, String name) {
        Map<String, Object> group = new HashMap<>();
        group.put("group_id", id);
        group.put("name", name);
        group.put("type", "public");
        return group;
    }

    private Map<String, Object> createUserMap(Long id, String name) {
        Map<String, Object> user = new HashMap<>();
        user.put("user_id", id);
        user.put("name", name);
        return user;
    }
}
