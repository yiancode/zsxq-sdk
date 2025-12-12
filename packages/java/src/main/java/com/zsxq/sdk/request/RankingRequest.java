package com.zsxq.sdk.request;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zsxq.sdk.http.HttpClient;
import com.zsxq.sdk.model.RankingItem;
import com.zsxq.sdk.model.RankingStatistics;
import com.zsxq.sdk.model.ScoreboardSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 排行榜请求模块
 */
public class RankingRequest extends BaseRequest {

    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public RankingRequest(HttpClient httpClient) {
        super(httpClient);
    }

    /**
     * 获取星球排行榜
     *
     * @param groupId 星球ID
     * @return 排行列表
     */
    public List<RankingItem> getGroupRanking(long groupId) {
        return getGroupRanking(String.valueOf(groupId), null);
    }

    /**
     * 获取星球排行榜
     *
     * @param groupId 星球ID
     * @return 排行列表
     */
    public List<RankingItem> getGroupRanking(String groupId) {
        return getGroupRanking(groupId, null);
    }

    /**
     * 获取星球排行榜（带参数）
     *
     * @param groupId 星球ID
     * @param options 查询参数
     * @return 排行列表
     */
    public List<RankingItem> getGroupRanking(long groupId, RankingOptions options) {
        return getGroupRanking(String.valueOf(groupId), options);
    }

    /**
     * 获取星球排行榜（带参数）
     *
     * @param groupId 星球ID
     * @param options 查询参数
     * @return 排行列表
     */
    public List<RankingItem> getGroupRanking(String groupId, RankingOptions options) {
        Map<String, Object> params = options != null ? options.toMap() : new HashMap<>();
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/ranking_list",
                params,
                new TypeToken<Map<String, Object>>() {}.getType());
        Object rankingObj = data.get("ranking_list");
        if (rankingObj == null) return new ArrayList<>();
        String json = gson.toJson(rankingObj);
        return gson.fromJson(json, new TypeToken<List<RankingItem>>() {}.getType());
    }

    /**
     * 获取全局星球排行榜（v3接口）
     *
     * @param type 排行类型: group_sales_list(畅销榜), new_star_list(新星榜),
     *             paid_group_active_list(活跃榜), group_fortune_list(财富榜)
     * @param count 返回数量
     * @return 排行列表
     */
    public Map<String, Object> getGlobalRanking(String type, int count) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("count", count);
        return httpClient.get(
                "/v3/groups/ranking_list",
                params,
                new TypeToken<Map<String, Object>>() {}.getType());
    }

    /**
     * 获取星球排行统计
     *
     * @param groupId 星球ID
     * @return 排行统计
     */
    public RankingStatistics getGroupRankingStats(long groupId) {
        return getGroupRankingStats(String.valueOf(groupId));
    }

    /**
     * 获取星球排行统计
     *
     * @param groupId 星球ID
     * @return 排行统计
     */
    public RankingStatistics getGroupRankingStats(String groupId) {
        Map<String, Object> data = httpClient.get(
                "/v3/groups/" + groupId + "/ranking_list/statistics",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object statsObj = data.get("statistics");
        if (statsObj == null) return null;
        String json = gson.toJson(statsObj);
        return gson.fromJson(json, RankingStatistics.class);
    }

    /**
     * 获取积分排行榜
     *
     * @param groupId 星球ID
     * @return 排行列表
     */
    public List<RankingItem> getScoreRanking(long groupId) {
        return getScoreRanking(String.valueOf(groupId), null);
    }

    /**
     * 获取积分排行榜
     *
     * @param groupId 星球ID
     * @return 排行列表
     */
    public List<RankingItem> getScoreRanking(String groupId) {
        return getScoreRanking(groupId, null);
    }

    /**
     * 获取积分排行榜（带参数）
     *
     * @param groupId 星球ID
     * @param options 查询参数
     * @return 排行列表
     */
    public List<RankingItem> getScoreRanking(long groupId, RankingOptions options) {
        return getScoreRanking(String.valueOf(groupId), options);
    }

    /**
     * 获取积分排行榜（带参数）
     *
     * @param groupId 星球ID
     * @param options 查询参数
     * @return 排行列表
     */
    public List<RankingItem> getScoreRanking(String groupId, RankingOptions options) {
        Map<String, Object> params = options != null ? options.toMap() : new HashMap<>();
        Map<String, Object> data = httpClient.get(
                "/v2/dashboard/groups/" + groupId + "/scoreboard/ranking_list",
                params,
                new TypeToken<Map<String, Object>>() {}.getType());
        Object rankingObj = data.get("ranking_list");
        if (rankingObj == null) return new ArrayList<>();
        String json = gson.toJson(rankingObj);
        return gson.fromJson(json, new TypeToken<List<RankingItem>>() {}.getType());
    }

    /**
     * 获取我的积分统计
     *
     * @param groupId 星球ID
     * @return 我的积分统计
     */
    public Map<String, Object> getMyScoreStats(long groupId) {
        return getMyScoreStats(String.valueOf(groupId));
    }

    /**
     * 获取我的积分统计
     *
     * @param groupId 星球ID
     * @return 我的积分统计
     */
    public Map<String, Object> getMyScoreStats(String groupId) {
        return httpClient.get(
                "/v2/groups/" + groupId + "/scoreboard/my_statistics",
                new TypeToken<Map<String, Object>>() {}.getType());
    }

    /**
     * 获取积分榜设置
     *
     * @param groupId 星球ID
     * @return 积分榜设置
     */
    public ScoreboardSettings getScoreboardSettings(long groupId) {
        return getScoreboardSettings(String.valueOf(groupId));
    }

    /**
     * 获取积分榜设置
     *
     * @param groupId 星球ID
     * @return 积分榜设置
     */
    public ScoreboardSettings getScoreboardSettings(String groupId) {
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/scoreboard/settings",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object settingsObj = data.get("settings");
        if (settingsObj == null) return null;
        String json = gson.toJson(settingsObj);
        return gson.fromJson(json, ScoreboardSettings.class);
    }

    /**
     * 获取邀请排行榜
     *
     * @param groupId 星球ID
     * @return 排行列表
     */
    public List<RankingItem> getInvitationRanking(long groupId) {
        return getInvitationRanking(String.valueOf(groupId));
    }

    /**
     * 获取邀请排行榜
     *
     * @param groupId 星球ID
     * @return 排行列表
     */
    public List<RankingItem> getInvitationRanking(String groupId) {
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/invitations/ranking_list",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object rankingObj = data.get("ranking_list");
        if (rankingObj == null) return new ArrayList<>();
        String json = gson.toJson(rankingObj);
        return gson.fromJson(json, new TypeToken<List<RankingItem>>() {}.getType());
    }

    /**
     * 获取贡献排行榜
     *
     * @param groupId 星球ID
     * @return 排行列表
     */
    public List<RankingItem> getContributionRanking(long groupId) {
        return getContributionRanking(String.valueOf(groupId));
    }

    /**
     * 获取贡献排行榜
     *
     * @param groupId 星球ID
     * @return 排行列表
     */
    public List<RankingItem> getContributionRanking(String groupId) {
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/contribution_ranking_list",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object rankingObj = data.get("ranking_list");
        if (rankingObj == null) return new ArrayList<>();
        String json = gson.toJson(rankingObj);
        return gson.fromJson(json, new TypeToken<List<RankingItem>>() {}.getType());
    }

    /**
     * 排行榜查询参数
     */
    public static class RankingOptions {
        private Integer count;
        private Integer index;
        private String type;  // "continuous" | "accumulated"

        public RankingOptions count(int count) {
            this.count = count;
            return this;
        }

        public RankingOptions index(int index) {
            this.index = index;
            return this;
        }

        public RankingOptions type(String type) {
            this.type = type;
            return this;
        }

        Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            if (count != null) map.put("count", count);
            if (index != null) map.put("index", index);
            if (type != null) map.put("type", type);
            return map;
        }
    }
}
