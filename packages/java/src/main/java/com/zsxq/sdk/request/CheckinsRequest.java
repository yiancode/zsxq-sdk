package com.zsxq.sdk.request;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zsxq.sdk.http.HttpClient;
import com.zsxq.sdk.model.Checkin;
import com.zsxq.sdk.model.CheckinStatistics;
import com.zsxq.sdk.model.DailyStatistics;
import com.zsxq.sdk.model.MyCheckinStatistics;
import com.zsxq.sdk.model.RankingItem;
import com.zsxq.sdk.model.Topic;
import com.zsxq.sdk.model.User;

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
     * 默认查询所有状态的打卡项目
     */
    public List<Checkin> list(long groupId) {
        return list(groupId, null);
    }

    /**
     * 获取打卡项目列表
     * 默认查询所有状态的打卡项目
     */
    public List<Checkin> list(String groupId) {
        return list(groupId, null);
    }

    /**
     * 获取打卡项目列表（带参数）
     */
    public List<Checkin> list(long groupId, ListCheckinsOptions options) {
        return list(String.valueOf(groupId), options);
    }

    /**
     * 获取打卡项目列表（带参数）
     */
    public List<Checkin> list(String groupId, ListCheckinsOptions options) {
        Map<String, Object> params = options != null ? options.toMap() : new HashMap<>();

        // API 要求必须有 scope 参数，默认查询所有状态
        if (!params.containsKey("scope")) {
            params.put("scope", "all");
        }
        // API 要求必须有 count 参数，默认 100
        // 注意：经测试 count 上限可能为 100-200 之间，过大会报参数错误
        if (!params.containsKey("count")) {
            params.put("count", 100);
        }

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
        return get(String.valueOf(groupId), String.valueOf(checkinId));
    }

    /**
     * 获取打卡项目详情
     */
    public Checkin get(String groupId, String checkinId) {
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
        return getStatistics(String.valueOf(groupId), String.valueOf(checkinId));
    }

    /**
     * 获取打卡统计
     */
    public CheckinStatistics getStatistics(String groupId, String checkinId) {
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
     * 获取打卡排行榜
     */
    public List<RankingItem> getRankingList(String groupId, String checkinId) {
        return getRankingList(groupId, checkinId, null);
    }

    /**
     * 获取打卡排行榜（带参数）
     */
    public List<RankingItem> getRankingList(long groupId, long checkinId, RankingListOptions options) {
        return getRankingList(String.valueOf(groupId), String.valueOf(checkinId), options);
    }

    /**
     * 获取打卡排行榜（带参数）
     */
    public List<RankingItem> getRankingList(String groupId, String checkinId, RankingListOptions options) {
        Map<String, Object> params = options != null ? options.toMap() : new HashMap<>();

        // API 要求必须有 type 参数，默认查询累计打卡排行
        if (!params.containsKey("type")) {
            params.put("type", "accumulated");
        }

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
     * 获取打卡话题列表
     */
    public List<Topic> getTopics(long groupId, long checkinId) {
        return getTopics(String.valueOf(groupId), String.valueOf(checkinId), null);
    }

    /**
     * 获取打卡话题列表
     */
    public List<Topic> getTopics(String groupId, String checkinId) {
        return getTopics(groupId, checkinId, null);
    }

    /**
     * 获取打卡话题列表（带参数）
     */
    public List<Topic> getTopics(long groupId, long checkinId, TopicsRequest.ListTopicsOptions options) {
        return getTopics(String.valueOf(groupId), String.valueOf(checkinId), options);
    }

    /**
     * 获取打卡话题列表（带参数）
     */
    public List<Topic> getTopics(String groupId, String checkinId, TopicsRequest.ListTopicsOptions options) {
        Map<String, Object> params = options != null ? options.toMap() : new HashMap<>();

        // API 要求必须有 scope 参数，默认查询所有话题
        if (!params.containsKey("scope")) {
            params.put("scope", "all");
        }
        // API 要求必须有 count 参数，默认 20
        if (!params.containsKey("count")) {
            params.put("count", 20);
        }

        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/checkins/" + checkinId + "/topics",
                params,
                new TypeToken<Map<String, Object>>() {}.getType());
        Object topicsObj = data.get("topics");
        if (topicsObj == null) return new ArrayList<>();
        String json = gson.toJson(topicsObj);
        return gson.fromJson(json, new TypeToken<List<Topic>>() {}.getType());
    }

    /**
     * 获取打卡每日统计
     */
    public List<DailyStatistics> getDailyStatistics(long groupId, long checkinId) {
        return getDailyStatistics(String.valueOf(groupId), String.valueOf(checkinId));
    }

    /**
     * 获取打卡每日统计
     */
    public List<DailyStatistics> getDailyStatistics(String groupId, String checkinId) {
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/checkins/" + checkinId + "/statistics/daily",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object dailyObj = data.get("daily_statistics");
        if (dailyObj == null) return new ArrayList<>();
        String json = gson.toJson(dailyObj);
        return gson.fromJson(json, new TypeToken<List<DailyStatistics>>() {}.getType());
    }

    /**
     * 获取打卡参与用户列表
     */
    public List<User> getJoinedUsers(long groupId, long checkinId) {
        return getJoinedUsers(String.valueOf(groupId), String.valueOf(checkinId), null);
    }

    /**
     * 获取打卡参与用户列表
     */
    public List<User> getJoinedUsers(String groupId, String checkinId) {
        return getJoinedUsers(groupId, checkinId, null);
    }

    /**
     * 获取打卡参与用户列表（带参数）
     */
    public List<User> getJoinedUsers(long groupId, long checkinId, JoinedUsersOptions options) {
        return getJoinedUsers(String.valueOf(groupId), String.valueOf(checkinId), options);
    }

    /**
     * 获取打卡参与用户列表（带参数）
     */
    public List<User> getJoinedUsers(String groupId, String checkinId, JoinedUsersOptions options) {
        Map<String, Object> params = options != null ? options.toMap() : null;
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/checkins/" + checkinId + "/joined_users",
                params,
                new TypeToken<Map<String, Object>>() {}.getType());
        Object usersObj = data.get("users");
        if (usersObj == null) return new ArrayList<>();
        String json = gson.toJson(usersObj);
        return gson.fromJson(json, new TypeToken<List<User>>() {}.getType());
    }

    /**
     * 获取我的打卡记录
     */
    public List<Topic> getMyCheckins(long groupId, long checkinId) {
        return getMyCheckins(String.valueOf(groupId), String.valueOf(checkinId), null);
    }

    /**
     * 获取我的打卡记录
     */
    public List<Topic> getMyCheckins(String groupId, String checkinId) {
        return getMyCheckins(groupId, checkinId, null);
    }

    /**
     * 获取我的打卡记录（带参数）
     */
    public List<Topic> getMyCheckins(long groupId, long checkinId, MyCheckinsOptions options) {
        return getMyCheckins(String.valueOf(groupId), String.valueOf(checkinId), options);
    }

    /**
     * 获取我的打卡记录（带参数）
     */
    public List<Topic> getMyCheckins(String groupId, String checkinId, MyCheckinsOptions options) {
        Map<String, Object> params = options != null ? options.toMap() : null;
        Map<String, Object> data = httpClient.get(
                "/v2/users/self/groups/" + groupId + "/checkins/" + checkinId + "/topics",
                params,
                new TypeToken<Map<String, Object>>() {}.getType());
        Object topicsObj = data.get("topics");
        if (topicsObj == null) return new ArrayList<>();
        String json = gson.toJson(topicsObj);
        return gson.fromJson(json, new TypeToken<List<Topic>>() {}.getType());
    }

    /**
     * 获取我的打卡日期列表
     */
    public List<String> getMyCheckinDays(long groupId, long checkinId) {
        return getMyCheckinDays(String.valueOf(groupId), String.valueOf(checkinId));
    }

    /**
     * 获取我的打卡日期列表
     */
    public List<String> getMyCheckinDays(String groupId, String checkinId) {
        Map<String, Object> data = httpClient.get(
                "/v2/users/self/groups/" + groupId + "/checkins/" + checkinId + "/checkined_dates",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object datesObj = data.get("dates");
        if (datesObj == null) return new ArrayList<>();
        String json = gson.toJson(datesObj);
        return gson.fromJson(json, new TypeToken<List<String>>() {}.getType());
    }

    /**
     * 获取我的打卡统计
     */
    public MyCheckinStatistics getMyStatistics(long groupId, long checkinId) {
        return getMyStatistics(String.valueOf(groupId), String.valueOf(checkinId));
    }

    /**
     * 获取我的打卡统计
     */
    public MyCheckinStatistics getMyStatistics(String groupId, String checkinId) {
        Map<String, Object> data = httpClient.get(
                "/v2/users/self/groups/" + groupId + "/checkins/" + checkinId + "/statistics",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object statsObj = data.get("statistics");
        if (statsObj == null) return null;
        String json = gson.toJson(statsObj);
        return gson.fromJson(json, MyCheckinStatistics.class);
    }

    /**
     * 创建打卡项目
     *
     * @param groupId 星球ID
     * @param params 创建参数
     * @return 创建的打卡项目
     */
    public Checkin create(long groupId, CreateCheckinParams params) {
        return create(String.valueOf(groupId), params);
    }

    /**
     * 创建打卡项目（训练营）
     *
     * @param groupId 星球ID
     * @param params 创建参数
     * @return 创建的打卡项目
     */
    public Checkin create(String groupId, CreateCheckinParams params) {
        Map<String, Object> body = new HashMap<>();
        body.put("req_data", params.toMap());
        Map<String, Object> data = httpClient.post(
                "/v2/groups/" + groupId + "/checkins",
                body,
                new TypeToken<Map<String, Object>>() {}.getType());
        Object checkinObj = data.get("checkin");
        if (checkinObj == null) return null;
        String json = gson.toJson(checkinObj);
        return gson.fromJson(json, Checkin.class);
    }

    /**
     * 更新打卡项目
     *
     * @param groupId 星球ID
     * @param checkinId 打卡项目ID
     * @param params 更新参数
     * @return 更新后的打卡项目
     */
    public Checkin update(long groupId, long checkinId, UpdateCheckinParams params) {
        return update(String.valueOf(groupId), String.valueOf(checkinId), params);
    }

    /**
     * 更新打卡项目
     *
     * @param groupId 星球ID
     * @param checkinId 打卡项目ID
     * @param params 更新参数
     * @return 更新后的打卡项目
     */
    public Checkin update(String groupId, String checkinId, UpdateCheckinParams params) {
        Map<String, Object> body = new HashMap<>();
        body.put("req_data", params.toMap());
        Map<String, Object> data = httpClient.put(
                "/v2/groups/" + groupId + "/checkins/" + checkinId,
                body,
                new TypeToken<Map<String, Object>>() {}.getType());
        Object checkinObj = data.get("checkin");
        if (checkinObj == null) return null;
        String json = gson.toJson(checkinObj);
        return gson.fromJson(json, Checkin.class);
    }

    /**
     * 打卡列表查询参数
     */
    public static class ListCheckinsOptions {
        private String scope;  // "ongoing" | "closed" | "over"
        private Integer count;

        public ListCheckinsOptions scope(String scope) {
            this.scope = scope;
            return this;
        }

        public ListCheckinsOptions count(int count) {
            this.count = count;
            return this;
        }

        Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            if (scope != null) map.put("scope", scope);
            if (count != null) map.put("count", count);
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

    /**
     * 参与用户查询参数
     */
    public static class JoinedUsersOptions {
        private Integer count;
        private String endTime;

        public JoinedUsersOptions count(int count) {
            this.count = count;
            return this;
        }

        public JoinedUsersOptions endTime(String endTime) {
            this.endTime = endTime;
            return this;
        }

        Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            if (count != null) map.put("count", count);
            if (endTime != null) map.put("end_time", endTime);
            return map;
        }
    }

    /**
     * 我的打卡记录查询参数
     */
    public static class MyCheckinsOptions {
        private Integer count;
        private String endTime;

        public MyCheckinsOptions count(int count) {
            this.count = count;
            return this;
        }

        public MyCheckinsOptions endTime(String endTime) {
            this.endTime = endTime;
            return this;
        }

        Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            if (count != null) map.put("count", count);
            if (endTime != null) map.put("end_time", endTime);
            return map;
        }
    }

    /**
     * 创建打卡项目参数
     * 基于 HAR 抓包分析的实际 API 结构:
     * {
     *   "req_data": {
     *     "title": "训练营标题",
     *     "text": "训练营描述",
     *     "checkin_days": 7,
     *     "type": "accumulated",
     *     "show_topics_on_timeline": false,
     *     "validity": {
     *       "long_period": false,
     *       "expiration_time": "2025-12-24T23:59:59.798+0800"
     *     }
     *   }
     * }
     */
    public static class CreateCheckinParams {
        private String title;
        private String text;
        private Integer checkinDays;
        private String type;  // "accumulated" (累计打卡) | "continuous" (连续打卡)
        private Boolean showTopicsOnTimeline;
        private Validity validity;

        /**
         * 设置训练营标题
         */
        public CreateCheckinParams title(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置训练营描述
         */
        public CreateCheckinParams text(String text) {
            this.text = text;
            return this;
        }

        /**
         * 设置打卡天数
         */
        public CreateCheckinParams checkinDays(int checkinDays) {
            this.checkinDays = checkinDays;
            return this;
        }

        /**
         * 设置打卡类型
         * @param type "accumulated" (累计打卡) 或 "continuous" (连续打卡)
         */
        public CreateCheckinParams type(String type) {
            this.type = type;
            return this;
        }

        /**
         * 设置是否在时间线展示打卡话题
         */
        public CreateCheckinParams showTopicsOnTimeline(boolean show) {
            this.showTopicsOnTimeline = show;
            return this;
        }

        /**
         * 设置有效期
         */
        public CreateCheckinParams validity(Validity validity) {
            this.validity = validity;
            return this;
        }

        /**
         * 设置有效期（便捷方法：设置过期时间）
         * @param expirationTime 过期时间，格式如 "2025-12-24T23:59:59.798+0800"
         */
        public CreateCheckinParams expirationTime(String expirationTime) {
            this.validity = new Validity().longPeriod(false).expirationTime(expirationTime);
            return this;
        }

        /**
         * 设置为长期有效
         */
        public CreateCheckinParams longPeriod() {
            this.validity = new Validity().longPeriod(true);
            return this;
        }

        /**
         * 转换为请求 Map（用于序列化为 JSON）
         */
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            if (title != null) map.put("title", title);
            if (text != null) map.put("text", text);
            if (checkinDays != null) map.put("checkin_days", checkinDays);
            if (type != null) map.put("type", type);
            if (showTopicsOnTimeline != null) map.put("show_topics_on_timeline", showTopicsOnTimeline);
            if (validity != null) map.put("validity", validity.toMap());
            return map;
        }
    }

    /**
     * 打卡有效期配置
     */
    public static class Validity {
        private Boolean longPeriod;
        private String expirationTime;

        /**
         * 设置是否长期有效
         */
        public Validity longPeriod(boolean longPeriod) {
            this.longPeriod = longPeriod;
            return this;
        }

        /**
         * 设置过期时间
         * @param expirationTime 格式如 "2025-12-24T23:59:59.798+0800"
         */
        public Validity expirationTime(String expirationTime) {
            this.expirationTime = expirationTime;
            return this;
        }

        /**
         * 转换为请求 Map（用于序列化为 JSON）
         */
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            if (longPeriod != null) map.put("long_period", longPeriod);
            if (expirationTime != null) map.put("expiration_time", expirationTime);
            return map;
        }
    }

    /**
     * 更新打卡项目参数
     */
    public static class UpdateCheckinParams {
        private String name;
        private String description;
        private String endTime;
        private String rules;
        private String status;

        public UpdateCheckinParams name(String name) {
            this.name = name;
            return this;
        }

        public UpdateCheckinParams description(String description) {
            this.description = description;
            return this;
        }

        public UpdateCheckinParams endTime(String endTime) {
            this.endTime = endTime;
            return this;
        }

        public UpdateCheckinParams rules(String rules) {
            this.rules = rules;
            return this;
        }

        public UpdateCheckinParams status(String status) {
            this.status = status;
            return this;
        }

        Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            if (name != null) map.put("name", name);
            if (description != null) map.put("description", description);
            if (endTime != null) map.put("end_time", endTime);
            if (rules != null) map.put("rules", rules);
            if (status != null) map.put("status", status);
            return map;
        }
    }
}
