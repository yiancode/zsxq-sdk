package com.zsxq.sdk.request;

import com.google.gson.reflect.TypeToken;
import com.zsxq.sdk.http.HttpClient;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.model.CheckinStatistics;
import com.zsxq.sdk.model.RankingItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 打卡请求模块
 */
public class CheckinsRequest extends BaseRequest {

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
        Map<String, List<Checkin>> data = httpClient.get(
                "/v2/groups/" + groupId + "/checkins",
                params,
                new TypeToken<Map<String, List<Checkin>>>() {}.getType());
        return data.get("checkins");
    }

    /**
     * 获取打卡项目详情
     */
    public Checkin get(long groupId, long checkinId) {
        Map<String, Checkin> data = httpClient.get(
                "/v2/groups/" + groupId + "/checkins/" + checkinId,
                new TypeToken<Map<String, Checkin>>() {}.getType());
        return data.get("checkin");
    }

    /**
     * 获取打卡统计
     */
    public CheckinStatistics getStatistics(long groupId, long checkinId) {
        return httpClient.get(
                "/v2/groups/" + groupId + "/checkins/" + checkinId + "/statistics",
                new TypeToken<CheckinStatistics>() {}.getType());
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
        Map<String, List<RankingItem>> data = httpClient.get(
                "/v2/groups/" + groupId + "/checkins/" + checkinId + "/ranking_list",
                params,
                new TypeToken<Map<String, List<RankingItem>>>() {}.getType());
        return data.get("ranking_list");
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
