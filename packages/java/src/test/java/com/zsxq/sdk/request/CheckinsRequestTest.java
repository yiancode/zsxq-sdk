package com.zsxq.sdk.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zsxq.sdk.http.HttpClient;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.model.CheckinStatistics;
import com.zsxq.sdk.model.RankingItem;
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

class CheckinsRequestTest {

    private MockWebServer mockServer;
    private CheckinsRequest checkinsRequest;
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
        checkinsRequest = new CheckinsRequest(httpClient);

        gson = new GsonBuilder().create();
    }

    @AfterEach
    void tearDown() throws Exception {
        mockServer.shutdown();
    }

    @Test
    void testList() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("checkins", List.of(
            createCheckinMap(1L, "打卡项目1"),
            createCheckinMap(2L, "打卡项目2")
        ));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<Checkin> checkins = checkinsRequest.list(123L);

        assertNotNull(checkins);
        assertEquals(2, checkins.size());
    }

    @Test
    void testListWithOptions() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("checkins", List.of());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        Map<String, Object> options = new HashMap<>();
        options.put("scope", "ongoing");
        options.put("count", 20);

        List<Checkin> checkins = checkinsRequest.list(123L, options);

        assertNotNull(checkins);
        assertTrue(checkins.isEmpty());
    }

    @Test
    void testGet() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("checkin", createCheckinMap(100L, "每日打卡"));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        Checkin checkin = checkinsRequest.get(123L, 100L);

        assertNotNull(checkin);
    }

    @Test
    void testGetStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total_count", 1000);
        stats.put("today_count", 50);
        stats.put("participant_count", 200);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(stats)))
            .setHeader("Content-Type", "application/json"));

        CheckinStatistics result = checkinsRequest.getStatistics(123L, 100L);

        assertNotNull(result);
    }

    @Test
    void testGetRankingList() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("ranking_list", List.of(
            createRankingMap(1L, "用户1", 100),
            createRankingMap(2L, "用户2", 90)
        ));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<RankingItem> ranking = checkinsRequest.getRankingList(123L, 100L);

        assertNotNull(ranking);
        assertEquals(2, ranking.size());
    }

    @Test
    void testGetRankingListWithOptions() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("ranking_list", List.of());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        Map<String, Object> options = new HashMap<>();
        options.put("type", "continuous");
        options.put("index", 2);

        List<RankingItem> ranking = checkinsRequest.getRankingList(123L, 100L, options);

        assertNotNull(ranking);
        assertTrue(ranking.isEmpty());
    }

    @Test
    void testGetTopics() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("topics", List.of(
            createTopicMap(1L, "打卡话题1"),
            createTopicMap(2L, "打卡话题2")
        ));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<Topic> topics = checkinsRequest.getTopics(123L, 100L);

        assertNotNull(topics);
        assertEquals(2, topics.size());
    }

    @Test
    void testGetTopicsEmpty() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("topics", List.of());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<Topic> topics = checkinsRequest.getTopics(123L, 100L);

        assertNotNull(topics);
        assertTrue(topics.isEmpty());
    }

    // Helper methods
    private Map<String, Object> createSuccessResponse(Object respData) {
        Map<String, Object> response = new HashMap<>();
        response.put("succeeded", true);
        response.put("resp_data", respData);
        return response;
    }

    private Map<String, Object> createCheckinMap(Long id, String name) {
        Map<String, Object> checkin = new HashMap<>();
        checkin.put("checkin_id", id);
        checkin.put("name", name);
        return checkin;
    }

    private Map<String, Object> createRankingMap(Long userId, String name, int score) {
        Map<String, Object> ranking = new HashMap<>();
        ranking.put("user_id", userId);
        ranking.put("name", name);
        ranking.put("score", score);
        return ranking;
    }

    private Map<String, Object> createTopicMap(Long id, String title) {
        Map<String, Object> topic = new HashMap<>();
        topic.put("topic_id", id);
        topic.put("title", title);
        return topic;
    }
}
