package com.zsxq.sdk.request;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zsxq.sdk.http.HttpClient;
import com.zsxq.sdk.model.RankingItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dashboard 请求模块
 */
public class DashboardRequest extends BaseRequest {

    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public DashboardRequest(HttpClient httpClient) {
        super(httpClient);
    }

    /**
     * 获取星球概览
     */
    public Map<String, Object> getOverview(long groupId) {
        return getOverview(String.valueOf(groupId));
    }

    /**
     * 获取星球概览
     */
    public Map<String, Object> getOverview(String groupId) {
        return httpClient.get(
                "/v2/dashboard/groups/" + groupId + "/overview",
                new TypeToken<Map<String, Object>>() {}.getType());
    }

    /**
     * 获取收入概览
     */
    public Map<String, Object> getIncomes(long groupId) {
        return getIncomes(String.valueOf(groupId));
    }

    /**
     * 获取收入概览
     */
    public Map<String, Object> getIncomes(String groupId) {
        return httpClient.get(
                "/v2/dashboard/groups/" + groupId + "/incomes/overview",
                new TypeToken<Map<String, Object>>() {}.getType());
    }

    /**
     * 获取积分排行
     */
    public List<RankingItem> getScoreboardRanking(long groupId) {
        return getScoreboardRanking(String.valueOf(groupId), null);
    }

    /**
     * 获取积分排行
     */
    public List<RankingItem> getScoreboardRanking(String groupId) {
        return getScoreboardRanking(groupId, null);
    }

    /**
     * 获取积分排行（带参数）
     */
    public List<RankingItem> getScoreboardRanking(long groupId, RankingListOptions options) {
        return getScoreboardRanking(String.valueOf(groupId), options);
    }

    /**
     * 获取积分排行（带参数）
     */
    public List<RankingItem> getScoreboardRanking(String groupId, RankingListOptions options) {
        Map<String, Object> params = options != null ? options.toMap() : new HashMap<>();
        Map<String, Object> data = httpClient.get(
                "/v2/dashboard/groups/" + groupId + "/scoreboard/ranking_list",
                params,
                new TypeToken<Map<String, Object>>() {}.getType());
        Object rankingList = data.get("ranking_list");
        if (rankingList == null) return new ArrayList<>();
        String json = gson.toJson(rankingList);
        return gson.fromJson(json, new TypeToken<List<RankingItem>>() {}.getType());
    }

    /**
     * 排行榜查询参数
     */
    public static class RankingListOptions {
        private String type; // "continuous" | "accumulated"
        private Integer index;

        public RankingListOptions type(String type) {
            this.type = type;
            return this;
        }

        public RankingListOptions index(int index) {
            this.index = index;
            return this;
        }

        Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            if (type != null) map.put("type", type);
            if (index != null) map.put("index", index);
            return map;
        }
    }
}
