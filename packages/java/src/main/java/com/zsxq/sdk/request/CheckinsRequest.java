package com.zsxq.sdk.request;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zsxq.sdk.http.HttpClient;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.model.CheckinStatistics;
import com.zsxq.sdk.model.RankingItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 打卡请求模块
 */
public class CheckinsRequest extends BaseRequest {

    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public CheckinsRequest(HttpClient httpClient) {
        super(httpClient);
    }

    /**
     * 获取打卡项目列表
     */
    public List<Checkin> list(long groupId) {
        return list(groupId, null);
    }

    /**
     * 获取打卡项目列表（带参数）
     */
    public List<Checkin> list(long groupId, ListCheckinsOptions options) {
        Map<String, Object> params = options != null ? options.toMap() : null;
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/checkins",
                params,
                new TypeToken<Map<String, Object>>() {}.getType());
        Object checkinsObj = data.get("checkins");
        if (checkinsObj == null) return new ArrayList<>();
        String json = gson.toJson(checkinsObj);
        return gson.fromJson(json, new TypeToken<List<Checkin>>() {}.getType());
    }

    /**
     * 获取打卡项目详情
     */
    public Checkin get(long groupId, long checkinId) {
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/checkins/" + checkinId,
                new TypeToken<Map<String, Object>>() {}.getType());
        Object checkinObj = data.get("checkin");
        if (checkinObj == null) return null;
        String json = gson.toJson(checkinObj);
        return gson.fromJson(json, Checkin.class);
    }

    /**
     * 获取打卡统计
     */
    public CheckinStatistics getStatistics(long groupId, long checkinId) {
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/checkins/" + checkinId + "/statistics",
                new TypeToken<Map<String, Object>>() {}.getType());
        String json = gson.toJson(data);
        return gson.fromJson(json, CheckinStatistics.class);
    }

    /**
     * 获取打卡排行榜
     */
    public List<RankingItem> getRankingList(long groupId, long checkinId) {
        return getRankingList(groupId, checkinId, null);
    }

    /**
     * 获取打卡排行榜（带参数）
     */
    public List<RankingItem> getRankingList(long groupId, long checkinId, RankingListOptions options) {
        Map<String, Object> params = options != null ? options.toMap() : null;
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/checkins/" + checkinId + "/ranking_list",
                params,
                new TypeToken<Map<String, Object>>() {}.getType());
        Object rankingObj = data.get("ranking_list");
        if (rankingObj == null) return new ArrayList<>();
        String json = gson.toJson(rankingObj);
        return gson.fromJson(json, new TypeToken<List<RankingItem>>() {}.getType());
    }

    /**
     * 打卡列表查询参数
     */
    public static class ListCheckinsOptions {
        private String scope;  // "ongoing" | "closed" | "over"

        public ListCheckinsOptions scope(String scope) {
            this.scope = scope;
            return this;
        }

        Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            if (scope != null) map.put("scope", scope);
            return map;
        }
    }

    /**
     * 排行榜查询参数
     */
    public static class RankingListOptions {
        private String type;  // "continuous" | "accumulated"
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
