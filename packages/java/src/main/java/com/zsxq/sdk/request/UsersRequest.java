package com.zsxq.sdk.request;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zsxq.sdk.http.HttpClient;
import com.zsxq.sdk.model.AchievementSummary;
import com.zsxq.sdk.model.Contribution;
import com.zsxq.sdk.model.ContributionStatistics;
import com.zsxq.sdk.model.Coupon;
import com.zsxq.sdk.model.FollowerStatistics;
import com.zsxq.sdk.model.Group;
import com.zsxq.sdk.model.Inviter;
import com.zsxq.sdk.model.PreferenceCategory;
import com.zsxq.sdk.model.Remark;
import com.zsxq.sdk.model.Topic;
import com.zsxq.sdk.model.UnansweredQuestionsSummary;
import com.zsxq.sdk.model.User;
import com.zsxq.sdk.model.WeeklyRanking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户请求模块
 */
public class UsersRequest extends BaseRequest {

    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public UsersRequest(HttpClient httpClient) {
        super(httpClient);
    }

    /**
     * 获取当前用户信息
     */
    public User self() {
        Map<String, Object> data = httpClient.get("/v3/users/self",
                new TypeToken<Map<String, Object>>() {}.getType());
        return convertToUser(data.get("user"));
    }

    /**
     * 获取指定用户信息
     */
    public User get(long userId) {
        return get(String.valueOf(userId));
    }

    /**
     * 获取指定用户信息
     */
    public User get(String userId) {
        Map<String, Object> data = httpClient.get("/v3/users/" + userId,
                new TypeToken<Map<String, Object>>() {}.getType());
        return convertToUser(data.get("user"));
    }

    /**
     * 获取用户统计
     */
    public Map<String, Object> getStatistics(long userId) {
        return getStatistics(String.valueOf(userId));
    }

    /**
     * 获取用户统计
     */
    public Map<String, Object> getStatistics(String userId) {
        return httpClient.get("/v3/users/" + userId + "/statistics",
                new TypeToken<Map<String, Object>>() {}.getType());
    }

    /**
     * 获取用户创建的星球
     */
    public List<Group> getCreatedGroups(long userId) {
        return getCreatedGroups(String.valueOf(userId));
    }

    /**
     * 获取用户创建的星球
     */
    public List<Group> getCreatedGroups(String userId) {
        Map<String, List<Group>> data = httpClient.get(
                "/v2/users/" + userId + "/created_groups",
                new TypeToken<Map<String, List<Group>>>() {}.getType());
        return data.getOrDefault("groups", new ArrayList<>());
    }

    /**
     * 获取用户动态足迹
     */
    public List<Topic> getFootprints(long userId) {
        return getFootprints(String.valueOf(userId));
    }

    /**
     * 获取用户动态足迹
     */
    public List<Topic> getFootprints(String userId) {
        Map<String, Object> data = httpClient.get(
                "/v2/users/" + userId + "/footprints",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object topicsObj = data.get("topics");
        if (topicsObj == null) return new ArrayList<>();
        String json = gson.toJson(topicsObj);
        return gson.fromJson(json, new TypeToken<List<Topic>>() {}.getType());
    }

    /**
     * 将 Object 转换为 User
     */
    private User convertToUser(Object obj) {
        if (obj == null) return null;
        String json = gson.toJson(obj);
        return gson.fromJson(json, User.class);
    }

    /**
     * 获取用户大尺寸头像URL
     *
     * @param userId 用户ID
     * @return 头像URL
     */
    public String getAvatarUrl(long userId) {
        return getAvatarUrl(String.valueOf(userId));
    }

    /**
     * 获取用户大尺寸头像URL
     *
     * @param userId 用户ID
     * @return 头像URL
     */
    public String getAvatarUrl(String userId) {
        Map<String, Object> data = httpClient.get("/v3/users/" + userId + "/avatar_url",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object urlObj = data.get("avatar_url");
        return urlObj != null ? urlObj.toString() : null;
    }

    /**
     * 获取用户星球足迹
     *
     * @param userId 用户ID
     * @return 星球列表
     */
    public List<Group> getGroupFootprints(long userId) {
        return getGroupFootprints(String.valueOf(userId));
    }

    /**
     * 获取用户星球足迹
     *
     * @param userId 用户ID
     * @return 星球列表
     */
    public List<Group> getGroupFootprints(String userId) {
        Map<String, Object> data = httpClient.get(
                "/v2/users/" + userId + "/group_footprints",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object groupsObj = data.get("groups");
        if (groupsObj == null) return new ArrayList<>();
        String json = gson.toJson(groupsObj);
        return gson.fromJson(json, new TypeToken<List<Group>>() {}.getType());
    }

    /**
     * 获取申请中的星球列表
     *
     * @return 申请中的星球列表
     */
    public List<Group> getApplyingGroups() {
        Map<String, Object> data = httpClient.get("/v2/groups/applying",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object groupsObj = data.get("groups");
        if (groupsObj == null) return new ArrayList<>();
        String json = gson.toJson(groupsObj);
        return gson.fromJson(json, new TypeToken<List<Group>>() {}.getType());
    }

    /**
     * 获取星球邀请人信息
     *
     * @param groupId 星球ID
     * @return 邀请人信息
     */
    public Inviter getInviter(long groupId) {
        return getInviter(String.valueOf(groupId));
    }

    /**
     * 获取星球邀请人信息
     *
     * @param groupId 星球ID
     * @return 邀请人信息
     */
    public Inviter getInviter(String groupId) {
        Map<String, Object> data = httpClient.get("/v2/groups/" + groupId + "/inviter",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object inviterObj = data.get("inviter");
        if (inviterObj == null) return null;
        String json = gson.toJson(inviterObj);
        return gson.fromJson(json, Inviter.class);
    }

    /**
     * 获取我的优惠券列表
     *
     * @return 优惠券列表
     */
    public List<Coupon> getCoupons() {
        Map<String, Object> data = httpClient.get("/v2/coupons",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object couponsObj = data.get("coupons");
        if (couponsObj == null) return new ArrayList<>();
        String json = gson.toJson(couponsObj);
        return gson.fromJson(json, new TypeToken<List<Coupon>>() {}.getType());
    }

    /**
     * 获取我的备注列表
     *
     * @return 备注列表
     */
    public List<Remark> getRemarks() {
        Map<String, Object> data = httpClient.get("/v2/remarks",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object remarksObj = data.get("remarks");
        if (remarksObj == null) return new ArrayList<>();
        String json = gson.toJson(remarksObj);
        return gson.fromJson(json, new TypeToken<List<Remark>>() {}.getType());
    }

    /**
     * 获取推荐关注用户列表
     *
     * @return 推荐关注的用户列表
     */
    public List<User> getRecommendedFollows() {
        Map<String, Object> data = httpClient.get("/v2/users/recommended_follows",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object usersObj = data.get("users");
        if (usersObj == null) return new ArrayList<>();
        String json = gson.toJson(usersObj);
        return gson.fromJson(json, new TypeToken<List<User>>() {}.getType());
    }

    /**
     * 获取屏蔽用户列表
     *
     * @return 已屏蔽的用户列表
     */
    public List<User> getBlockedUsers() {
        Map<String, Object> data = httpClient.get("/v2/users/block_users",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object usersObj = data.get("users");
        if (usersObj == null) return new ArrayList<>();
        String json = gson.toJson(usersObj);
        return gson.fromJson(json, new TypeToken<List<User>>() {}.getType());
    }

    /**
     * 上报推送通道
     *
     * @param channel 推送通道名称（如 JPush）
     * @param deviceToken 设备 Token
     */
    public void reportPushChannel(String channel, String deviceToken) {
        Map<String, Object> reqData = new java.util.HashMap<>();
        reqData.put("channel", channel);
        reqData.put("device_token", deviceToken);
        httpClient.post("/v2/users/self/push_channel", reqData,
                new TypeToken<Map<String, Object>>() {}.getType());
    }

    /**
     * 获取推荐偏好分类
     *
     * @return 推荐偏好分类列表
     */
    public List<PreferenceCategory> getPreferenceCategories() {
        Map<String, Object> data = httpClient.get("/v2/users/self/recommendations/preference_categories",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object categoriesObj = data.get("categories");
        if (categoriesObj == null) return new ArrayList<>();
        String json = gson.toJson(categoriesObj);
        return gson.fromJson(json, new TypeToken<List<PreferenceCategory>>() {}.getType());
    }

    /**
     * 获取未回答问题摘要
     *
     * @return 未回答问题摘要
     */
    public UnansweredQuestionsSummary getUnansweredQuestionsSummary() {
        Map<String, Object> data = httpClient.get("/v2/users/self/unanswered_questions/brief",
                new TypeToken<Map<String, Object>>() {}.getType());
        String json = gson.toJson(data);
        return gson.fromJson(json, UnansweredQuestionsSummary.class);
    }

    /**
     * 获取关注者统计
     *
     * @return 关注者统计
     */
    public FollowerStatistics getFollowerStats() {
        Map<String, Object> data = httpClient.get("/v3/users/self/followers/statistics",
                new TypeToken<Map<String, Object>>() {}.getType());
        String json = gson.toJson(data);
        return gson.fromJson(json, FollowerStatistics.class);
    }

    /**
     * 获取关注者统计（指定起始时间）
     *
     * @param beginTime 起始时间（ISO8601格式）
     * @return 关注者统计
     */
    public FollowerStatistics getFollowerStats(String beginTime) {
        Map<String, Object> data = httpClient.get(
                "/v3/users/self/followers/statistics?begin_time=" + beginTime,
                new TypeToken<Map<String, Object>>() {}.getType());
        String json = gson.toJson(data);
        return gson.fromJson(json, FollowerStatistics.class);
    }

    /**
     * 获取贡献记录
     *
     * @return 贡献记录列表
     */
    public List<Contribution> getContributions() {
        Map<String, Object> data = httpClient.get("/v3/users/self/contributions",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object contributionsObj = data.get("contributions");
        if (contributionsObj == null) return new ArrayList<>();
        String json = gson.toJson(contributionsObj);
        return gson.fromJson(json, new TypeToken<List<Contribution>>() {}.getType());
    }

    /**
     * 获取贡献记录（指定时间范围）
     *
     * @param beginTime 起始时间（ISO8601格式）
     * @param endTime 结束时间（ISO8601格式）
     * @return 贡献记录列表
     */
    public List<Contribution> getContributions(String beginTime, String endTime) {
        String url = "/v3/users/self/contributions?begin_time=" + beginTime + "&end_time=" + endTime;
        Map<String, Object> data = httpClient.get(url,
                new TypeToken<Map<String, Object>>() {}.getType());
        Object contributionsObj = data.get("contributions");
        if (contributionsObj == null) return new ArrayList<>();
        String json = gson.toJson(contributionsObj);
        return gson.fromJson(json, new TypeToken<List<Contribution>>() {}.getType());
    }

    /**
     * 获取贡献统计
     *
     * @return 贡献统计
     */
    public ContributionStatistics getContributionStats() {
        Map<String, Object> data = httpClient.get("/v3/users/self/contributions/statistics",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object statsObj = data.get("statistics");
        if (statsObj == null) return null;
        String json = gson.toJson(statsObj);
        return gson.fromJson(json, ContributionStatistics.class);
    }

    /**
     * 获取成就摘要列表
     *
     * @return 成就摘要列表
     */
    public List<AchievementSummary> getAchievementsSummary() {
        Map<String, Object> data = httpClient.get("/v3/users/self/achievements/summaries",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object summariesObj = data.get("summaries");
        if (summariesObj == null) return new ArrayList<>();
        String json = gson.toJson(summariesObj);
        return gson.fromJson(json, new TypeToken<List<AchievementSummary>>() {}.getType());
    }

    /**
     * 获取星球周榜排名
     *
     * @param groupId 星球ID
     * @return 周榜排名
     */
    public WeeklyRanking getWeeklyRanking(long groupId) {
        return getWeeklyRanking(String.valueOf(groupId));
    }

    /**
     * 获取星球周榜排名
     *
     * @param groupId 星球ID
     * @return 周榜排名
     */
    public WeeklyRanking getWeeklyRanking(String groupId) {
        Map<String, Object> data = httpClient.get(
                "/v3/users/self/group_weekly_rankings?group_id=" + groupId,
                new TypeToken<Map<String, Object>>() {}.getType());
        String json = gson.toJson(data);
        return gson.fromJson(json, WeeklyRanking.class);
    }

    /**
     * 获取用户偏好配置
     *
     * @return 用户偏好配置
     */
    public Map<String, Object> getPreferences() {
        return httpClient.get("/v3/users/self/preferences",
                new TypeToken<Map<String, Object>>() {}.getType());
    }
}
