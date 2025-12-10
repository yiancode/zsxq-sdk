package com.zsxq.sdk.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zsxq.sdk.http.HttpClient;
import com.zsxq.sdk.model.Group;
import com.zsxq.sdk.model.Topic;
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

class UsersRequestTest {

    private MockWebServer mockServer;
    private UsersRequest usersRequest;
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
        usersRequest = new UsersRequest(httpClient);

        gson = new GsonBuilder().create();
    }

    @AfterEach
    void tearDown() throws Exception {
        mockServer.shutdown();
    }

    @Test
    void testSelf() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("user", createUserMap(123L, "当前用户"));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        User user = usersRequest.self();

        assertNotNull(user);
        assertEquals("当前用户", user.getName());
    }

    @Test
    void testGetWithLongId() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("user", createUserMap(456L, "指定用户"));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        User user = usersRequest.get(456L);

        assertNotNull(user);
        assertEquals("指定用户", user.getName());
    }

    @Test
    void testGetWithStringId() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("user", createUserMap(456L, "指定用户"));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        User user = usersRequest.get("456");

        assertNotNull(user);
        assertEquals("指定用户", user.getName());
    }

    @Test
    void testGetStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("topic_count", 100);
        stats.put("comment_count", 200);
        stats.put("like_count", 500);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(stats)))
            .setHeader("Content-Type", "application/json"));

        Map<String, Object> result = usersRequest.getStatistics(456L);

        assertNotNull(result);
        assertEquals(100.0, ((Number) result.get("topic_count")).doubleValue());
        assertEquals(200.0, ((Number) result.get("comment_count")).doubleValue());
    }

    @Test
    void testGetFootprints() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("topics", List.of(
            createTopicMap(1L, "用户话题1"),
            createTopicMap(2L, "用户话题2")
        ));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<Topic> topics = usersRequest.getFootprints(456L);

        assertNotNull(topics);
        assertEquals(2, topics.size());
    }

    @Test
    void testGetFootprintsEmpty() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("topics", List.of());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<Topic> topics = usersRequest.getFootprints(456L);

        assertNotNull(topics);
        assertTrue(topics.isEmpty());
    }

    @Test
    void testGetCreatedGroups() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("groups", List.of(
            createGroupMap(1L, "创建的星球1"),
            createGroupMap(2L, "创建的星球2")
        ));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<Group> groups = usersRequest.getCreatedGroups(456L);

        assertNotNull(groups);
        assertEquals(2, groups.size());
    }

    @Test
    void testGetCreatedGroupsEmpty() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("groups", List.of());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<Group> groups = usersRequest.getCreatedGroups(456L);

        assertNotNull(groups);
        assertTrue(groups.isEmpty());
    }

    // Helper methods
    private Map<String, Object> createSuccessResponse(Object respData) {
        Map<String, Object> response = new HashMap<>();
        response.put("succeeded", true);
        response.put("resp_data", respData);
        return response;
    }

    private Map<String, Object> createUserMap(Long id, String name) {
        Map<String, Object> user = new HashMap<>();
        user.put("user_id", id);
        user.put("name", name);
        return user;
    }

    private Map<String, Object> createTopicMap(Long id, String title) {
        Map<String, Object> topic = new HashMap<>();
        topic.put("topic_id", id);
        topic.put("title", title);
        return topic;
    }

    private Map<String, Object> createGroupMap(Long id, String name) {
        Map<String, Object> group = new HashMap<>();
        group.put("group_id", id);
        group.put("name", name);
        group.put("type", "public");
        return group;
    }
}
