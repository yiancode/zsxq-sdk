package com.zsxq.sdk.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zsxq.sdk.http.HttpClient;
import com.zsxq.sdk.model.RankingItem;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DashboardRequestTest {

    private MockWebServer mockServer;
    private DashboardRequest dashboardRequest;
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
        dashboardRequest = new DashboardRequest(httpClient);

        gson = new GsonBuilder().create();
    }

    @AfterEach
    void tearDown() throws Exception {
        mockServer.shutdown();
    }

    @Test
    void testGetOverview() {
        Map<String, Object> overview = new HashMap<>();
        overview.put("member_count", 5000);
        overview.put("topic_count", 1000);
        overview.put("active_member_count", 2000);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(overview)))
            .setHeader("Content-Type", "application/json"));

        Map<String, Object> result = dashboardRequest.getOverview(123L);

        assertNotNull(result);
        assertEquals(5000.0, ((Number) result.get("member_count")).doubleValue());
        assertEquals(1000.0, ((Number) result.get("topic_count")).doubleValue());
    }

    @Test
    void testGetOverviewWithStringId() {
        Map<String, Object> overview = new HashMap<>();
        overview.put("member_count", 5000);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(overview)))
            .setHeader("Content-Type", "application/json"));

        Map<String, Object> result = dashboardRequest.getOverview("123");

        assertNotNull(result);
        assertEquals(5000.0, ((Number) result.get("member_count")).doubleValue());
    }

    @Test
    void testGetIncomes() {
        Map<String, Object> incomes = new HashMap<>();
        incomes.put("total_income", 50000);
        incomes.put("month_income", 5000);
        incomes.put("today_income", 200);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(incomes)))
            .setHeader("Content-Type", "application/json"));

        Map<String, Object> result = dashboardRequest.getIncomes(123L);

        assertNotNull(result);
        assertEquals(50000.0, ((Number) result.get("total_income")).doubleValue());
        assertEquals(5000.0, ((Number) result.get("month_income")).doubleValue());
    }

    @Test
    void testGetIncomesWithStringId() {
        Map<String, Object> incomes = new HashMap<>();
        incomes.put("total_income", 50000);

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(incomes)))
            .setHeader("Content-Type", "application/json"));

        Map<String, Object> result = dashboardRequest.getIncomes("123");

        assertNotNull(result);
        assertEquals(50000.0, ((Number) result.get("total_income")).doubleValue());
    }

    @Test
    void testGetScoreboardRanking() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("ranking_list", List.of(
            createRankingMap(1L, "用户1", 1000),
            createRankingMap(2L, "用户2", 900),
            createRankingMap(3L, "用户3", 800)
        ));

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<RankingItem> ranking = dashboardRequest.getScoreboardRanking(123L);

        assertNotNull(ranking);
        assertEquals(3, ranking.size());
    }

    @Test
    void testGetScoreboardRankingWithOptions() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("ranking_list", List.of());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        Map<String, Object> options = new HashMap<>();
        options.put("type", "continuous");
        options.put("index", 2);

        List<RankingItem> ranking = dashboardRequest.getScoreboardRanking(123L, options);

        assertNotNull(ranking);
        assertTrue(ranking.isEmpty());
    }

    @Test
    void testGetScoreboardRankingEmpty() {
        Map<String, Object> respData = new HashMap<>();
        respData.put("ranking_list", List.of());

        mockServer.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(gson.toJson(createSuccessResponse(respData)))
            .setHeader("Content-Type", "application/json"));

        List<RankingItem> ranking = dashboardRequest.getScoreboardRanking(123L);

        assertNotNull(ranking);
        assertTrue(ranking.isEmpty());
    }

    // Helper methods
    private Map<String, Object> createSuccessResponse(Object respData) {
        Map<String, Object> response = new HashMap<>();
        response.put("succeeded", true);
        response.put("resp_data", respData);
        return response;
    }

    private Map<String, Object> createRankingMap(Long userId, String name, int score) {
        Map<String, Object> ranking = new HashMap<>();
        ranking.put("user_id", userId);
        ranking.put("name", name);
        ranking.put("score", score);
        return ranking;
    }
}
