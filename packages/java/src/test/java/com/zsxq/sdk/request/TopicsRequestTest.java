package com.zsxq.sdk.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zsxq.sdk.http.HttpClient;
import com.zsxq.sdk.model.Comment;
import com.zsxq.sdk.model.Topic;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TopicsRequestTest {

    private MockWebServer mockServer;
    private TopicsRequest topicsRequest;
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
        topicsRequest = new TopicsRequest(httpClient);

        gson = new GsonBuilder().create();
    }

    @AfterEach
    void tearDown() throws Exception {
        mockServer.shutdown();
    }

    @Test
    void testList() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("topics", List.of(
                createTopicMap(1L, "话题1"),
                createTopicMap(2L, "话题2")
        ));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<Topic> topics = topicsRequest.list(123L);

        assertNotNull(topics);
        assertEquals(2, topics.size());
    }

    @Test
    void testGet() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("topic", createTopicMap(123L, "测试话题"));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        Topic topic = topicsRequest.get(123L);

        assertNotNull(topic);
    }

    @Test
    void testGetWithStringId() throws InterruptedException {
        Map<String, Object> respData = new HashMap<>();
        respData.put("topic", createTopicMap(123L, "测试话题"));

        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(gson.toJson(createSuccessResponse(respData)))
                .setHeader("Content-Type", "application/json"));

        Topic topic = topicsRequest.get("123");

        assertNotNull(topic);
        String path = mockServer.takeRequest().getPath();
        assertTrue(path.startsWith("/v2/topics/123"));
    }

    @Test
    void testListWithOptions() throws InterruptedException {
        Map<String, Object> respData = new HashMap<>();
        respData.put("topics", List.of(createTopicMap(1L, "话题1")));

        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(gson.toJson(createSuccessResponse(respData)))
                .setHeader("Content-Type", "application/json"));

        TopicsRequest.ListTopicsOptions options = new TopicsRequest.ListTopicsOptions()
                .count(20)
                .scope("digests")
                .direction("forward")
                .withInvisibles(true);

        List<Topic> topics = topicsRequest.list(123L, options);

        assertNotNull(topics);
        assertEquals(1, topics.size());

        String path = mockServer.takeRequest().getPath();
        assertTrue(path.contains("count=20"));
        assertTrue(path.contains("scope=digests"));
        assertTrue(path.contains("direction=forward"));
        assertTrue(path.contains("with_invisibles=true"));
    }

    @Test
    void testGetComments() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("comments", List.of(
                createCommentMap(1L, "评论1"),
                createCommentMap(2L, "评论2")
        ));

        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(gson.toJson(createSuccessResponse(respData)))
                .setHeader("Content-Type", "application/json"));

        List<Comment> comments = topicsRequest.getComments(123L);

        assertNotNull(comments);
        assertEquals(2, comments.size());
    }

    @Test
    void testGetCommentsWithOptions() throws InterruptedException {
        Map<String, Object> respData = new HashMap<>();
        respData.put("comments", List.of());

        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(gson.toJson(createSuccessResponse(respData)))
                .setHeader("Content-Type", "application/json"));

        TopicsRequest.ListCommentsOptions options = new TopicsRequest.ListCommentsOptions()
                .count(50)
                .sort("asc")
                .withSticky(true);

        List<Comment> comments = topicsRequest.getComments(123L, options);

        assertNotNull(comments);
        assertTrue(comments.isEmpty());

        String path = mockServer.takeRequest().getPath();
        assertTrue(path.contains("count=50"));
        assertTrue(path.contains("sort=asc"));
        assertTrue(path.contains("with_sticky=true"));
    }

    @Test
    void testListByHashtag() throws InterruptedException {
        Map<String, Object> respData = new HashMap<>();
        respData.put("topics", List.of(createTopicMap(1L, "话题1")));

        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(gson.toJson(createSuccessResponse(respData)))
                .setHeader("Content-Type", "application/json"));

        List<Topic> topics = topicsRequest.listByHashtag(100L);

        assertNotNull(topics);
        assertEquals(1, topics.size());

        String path = mockServer.takeRequest().getPath();
        assertTrue(path.startsWith("/v2/hashtags/100/topics"));
    }

    @Test
    void testListByColumn() throws InterruptedException {
        Map<String, Object> respData = new HashMap<>();
        respData.put("topics", List.of(
                createTopicMap(1L, "专栏话题1"),
                createTopicMap(2L, "专栏话题2")
        ));

        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(gson.toJson(createSuccessResponse(respData)))
                .setHeader("Content-Type", "application/json"));

        List<Topic> topics = topicsRequest.listByColumn(123L, 200L);

        assertNotNull(topics);
        assertEquals(2, topics.size());

        String path = mockServer.takeRequest().getPath();
        assertTrue(path.startsWith("/v2/groups/123/columns/200/topics"));
    }

    @Test
    void testListByColumnWithStringIds() throws InterruptedException {
        Map<String, Object> respData = new HashMap<>();
        respData.put("topics", List.of());

        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(gson.toJson(createSuccessResponse(respData)))
                .setHeader("Content-Type", "application/json"));

        List<Topic> topics = topicsRequest.listByColumn("123", "200");

        assertNotNull(topics);
        String path = mockServer.takeRequest().getPath();
        assertTrue(path.startsWith("/v2/groups/123/columns/200/topics"));
    }

    // Helper methods
    private Map<String, Object> createSuccessResponse(Object respData) {
        Map<String, Object> response = new HashMap<>();
        response.put("succeeded", true);
        response.put("resp_data", respData);
        return response;
    }

    private Map<String, Object> createTopicMap(Long id, String title) {
        Map<String, Object> topic = new HashMap<>();
        topic.put("topic_id", id);
        topic.put("title", title);
        return topic;
    }

    private Map<String, Object> createCommentMap(Long id, String content) {
        Map<String, Object> comment = new HashMap<>();
        comment.put("comment_id", id);
        comment.put("text", content);
        return comment;
    }
}
