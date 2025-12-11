package com.zsxq.sdk.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zsxq.sdk.model.RankingItem;
import com.zsxq.sdk.model.RankingStatistics;
import com.zsxq.sdk.model.ScoreboardSettings;
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

class RankingRequestTest {

    private MockWebServer mockServer;
    private RankingRequest rankingRequest;
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
        rankingRequest = new RankingRequest(httpClient);

        gson = new GsonBuilder().create();
    }

    @AfterEach
    void tearDown() throws Exception {
        mockServer.shutdown();
    }

    @Test
    void testGetGroupRankingWithLongId() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("ranking_list", List.of(
            createRankingItemMap(1L, "用户1", 100),
            createRankingItemMap(2L, "用户2", 80)
        ));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<RankingItem> ranking = rankingRequest.getGroupRanking(123L);

        assertNotNull(ranking);
        assertEquals(2, ranking.size());
    }

    @Test
    void testGetGroupRankingWithStringId() throws InterruptedException {
        Map<String, Object> respData = new HashMap<>();
        respData.put("ranking_list", List.of(
            createRankingItemMap(1L, "用户1", 100)
        ));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<RankingItem> ranking = rankingRequest.getGroupRanking("123");

        assertNotNull(ranking);
        String path = mockServer.takeRequest().getPath();
        assertTrue(path.startsWith("/v2/groups/123/ranking_list"));
    }

    @Test
    void testGetGroupRankingWithOptions() throws InterruptedException {
        Map<String, Object> respData = new HashMap<>();
        respData.put("ranking_list", List.of());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        RankingRequest.RankingOptions options = new RankingRequest.RankingOptions()
            .count(10)
            .index(0)
            .type("continuous");

        List<RankingItem> ranking = rankingRequest.getGroupRanking(123L, options);

        assertNotNull(ranking);
        String path = mockServer.takeRequest().getPath();
        assertTrue(path.contains("count=10"));
        assertTrue(path.contains("type=continuous"));
    }

    @Test
    void testGetGroupRankingEmpty() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("ranking_list", null);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<RankingItem> ranking = rankingRequest.getGroupRanking(123L);

        assertNotNull(ranking);
        assertTrue(ranking.isEmpty());
    }

    @Test
    void testGetGroupRankingStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total_members", 100);
        stats.put("ranking_members", 50);

        Map<String, Object> respData = new HashMap<>();
        respData.put("statistics", stats);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        RankingStatistics result = rankingRequest.getGroupRankingStats(123L);

        assertNotNull(result);
    }

    @Test
    void testGetGroupRankingStatsWithStringId() throws InterruptedException {
        Map<String, Object> respData = new HashMap<>();
        respData.put("statistics", new HashMap<>());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        RankingStatistics result = rankingRequest.getGroupRankingStats("123");

        assertNotNull(result);
        String path = mockServer.takeRequest().getPath();
        assertTrue(path.startsWith("/v2/groups/123/ranking_list/statistics"));
    }

    @Test
    void testGetGroupRankingStatsNull() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("statistics", null);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        RankingStatistics result = rankingRequest.getGroupRankingStats(123L);

        assertNull(result);
    }

    @Test
    void testGetScoreRanking() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("ranking_list", List.of(
            createRankingItemMap(1L, "用户1", 200)
        ));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<RankingItem> ranking = rankingRequest.getScoreRanking(123L);

        assertNotNull(ranking);
        assertEquals(1, ranking.size());
    }

    @Test
    void testGetScoreRankingWithStringId() throws InterruptedException {
        Map<String, Object> respData = new HashMap<>();
        respData.put("ranking_list", List.of());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<RankingItem> ranking = rankingRequest.getScoreRanking("123");

        assertNotNull(ranking);
        String path = mockServer.takeRequest().getPath();
        assertTrue(path.startsWith("/v2/groups/123/scoreboard/ranking_list"));
    }

    @Test
    void testGetScoreRankingWithOptions() throws InterruptedException {
        Map<String, Object> respData = new HashMap<>();
        respData.put("ranking_list", List.of());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        RankingRequest.RankingOptions options = new RankingRequest.RankingOptions()
            .count(5);

        List<RankingItem> ranking = rankingRequest.getScoreRanking("123", options);

        assertNotNull(ranking);
        String path = mockServer.takeRequest().getPath();
        assertTrue(path.contains("count=5"));
    }

    @Test
    void testGetMyScoreStats() throws InterruptedException {
        Map<String, Object> respData = new HashMap<>();
        respData.put("my_score", 150);
        respData.put("rank", 5);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        Map<String, Object> stats = rankingRequest.getMyScoreStats(123L);

        assertNotNull(stats);
        String path = mockServer.takeRequest().getPath();
        assertTrue(path.startsWith("/v2/groups/123/scoreboard/my_statistics"));
    }

    @Test
    void testGetMyScoreStatsWithStringId() throws InterruptedException {
        Map<String, Object> respData = new HashMap<>();

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        Map<String, Object> stats = rankingRequest.getMyScoreStats("123");

        assertNotNull(stats);
        String path = mockServer.takeRequest().getPath();
        assertTrue(path.startsWith("/v2/groups/123/scoreboard/my_statistics"));
    }

    @Test
    void testGetScoreboardSettings() {
        Map<String, Object> settings = new HashMap<>();
        settings.put("enabled", true);
        settings.put("reset_period", "weekly");

        Map<String, Object> respData = new HashMap<>();
        respData.put("settings", settings);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        ScoreboardSettings result = rankingRequest.getScoreboardSettings(123L);

        assertNotNull(result);
    }

    @Test
    void testGetScoreboardSettingsWithStringId() throws InterruptedException {
        Map<String, Object> respData = new HashMap<>();
        respData.put("settings", new HashMap<>());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        ScoreboardSettings result = rankingRequest.getScoreboardSettings("123");

        assertNotNull(result);
        String path = mockServer.takeRequest().getPath();
        assertTrue(path.startsWith("/v2/groups/123/scoreboard/settings"));
    }

    @Test
    void testGetScoreboardSettingsNull() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("settings", null);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        ScoreboardSettings result = rankingRequest.getScoreboardSettings(123L);

        assertNull(result);
    }

    @Test
    void testGetInvitationRanking() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("ranking_list", List.of(
            createRankingItemMap(1L, "用户1", 10)
        ));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<RankingItem> ranking = rankingRequest.getInvitationRanking(123L);

        assertNotNull(ranking);
        assertEquals(1, ranking.size());
    }

    @Test
    void testGetInvitationRankingWithStringId() throws InterruptedException {
        Map<String, Object> respData = new HashMap<>();
        respData.put("ranking_list", List.of());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<RankingItem> ranking = rankingRequest.getInvitationRanking("123");

        assertNotNull(ranking);
        String path = mockServer.takeRequest().getPath();
        assertTrue(path.startsWith("/v2/groups/123/invitation_ranking_list"));
    }

    @Test
    void testGetContributionRanking() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("ranking_list", List.of(
            createRankingItemMap(1L, "用户1", 500)
        ));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<RankingItem> ranking = rankingRequest.getContributionRanking(123L);

        assertNotNull(ranking);
        assertEquals(1, ranking.size());
    }

    @Test
    void testGetContributionRankingWithStringId() throws InterruptedException {
        Map<String, Object> respData = new HashMap<>();
        respData.put("ranking_list", List.of());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<RankingItem> ranking = rankingRequest.getContributionRanking("123");

        assertNotNull(ranking);
        String path = mockServer.takeRequest().getPath();
        assertTrue(path.startsWith("/v2/groups/123/contribution_ranking_list"));
    }

    @Test
    void testRankingOptionsBuilder() {
        RankingRequest.RankingOptions options = new RankingRequest.RankingOptions()
            .count(20)
            .index(5)
            .type("accumulated");

        Map<String, Object> map = options.toMap();

        assertEquals(20, map.get("count"));
        assertEquals(5, map.get("index"));
        assertEquals("accumulated", map.get("type"));
    }

    // Helper methods
    private Map<String, Object> createSuccessResponse(Object respData) {
        Map<String, Object> response = new HashMap<>();
        response.put("succeeded", true);
        response.put("resp_data", respData);
        return response;
    }

    private Map<String, Object> createRankingItemMap(Long userId, String name, int score) {
        Map<String, Object> item = new HashMap<>();
        Map<String, Object> user = new HashMap<>();
        user.put("user_id", userId);
        user.put("name", name);
        item.put("user", user);
        item.put("score", score);
        item.put("rank", 1);
        return item;
    }
}
