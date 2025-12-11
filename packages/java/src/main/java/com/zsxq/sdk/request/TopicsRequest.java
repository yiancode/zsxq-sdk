package com.zsxq.sdk.request;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zsxq.sdk.http.HttpClient;
import com.zsxq.sdk.model.Comment;
import com.zsxq.sdk.model.Reward;
import com.zsxq.sdk.model.Topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 话题请求模块
 */
public class TopicsRequest extends BaseRequest {

    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public TopicsRequest(HttpClient httpClient) {
        super(httpClient);
    }

    /**
     * 获取话题列表
     */
    public List<Topic> list(long groupId) {
        return list(groupId, null);
    }

    /**
     * 获取话题列表
     */
    public List<Topic> list(String groupId) {
        return list(groupId, null);
    }

    /**
     * 获取话题列表（带参数）
     */
    public List<Topic> list(long groupId, ListTopicsOptions options) {
        return list(String.valueOf(groupId), options);
    }

    /**
     * 获取话题列表（带参数）
     */
    public List<Topic> list(String groupId, ListTopicsOptions options) {
        Map<String, Object> params = options != null ? options.toMap() : new HashMap<>();
        // API 要求必须有 count 参数，默认 20
        if (!params.containsKey("count")) {
            params.put("count", 20);
        }
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/topics",
                params,
                new TypeToken<Map<String, Object>>() {}.getType());
        Object topicsObj = data.get("topics");
        if (topicsObj == null) return new ArrayList<>();
        String json = gson.toJson(topicsObj);
        return gson.fromJson(json, new TypeToken<List<Topic>>() {}.getType());
    }

    /**
     * 获取话题详情
     */
    public Topic get(long topicId) {
        Map<String, Object> data = httpClient.get("/v2/topics/" + topicId,
                new TypeToken<Map<String, Object>>() {}.getType());
        return convertToTopic(data.get("topic"));
    }

    /**
     * 获取话题详情
     */
    public Topic get(String topicId) {
        Map<String, Object> data = httpClient.get("/v2/topics/" + topicId,
                new TypeToken<Map<String, Object>>() {}.getType());
        return convertToTopic(data.get("topic"));
    }

    /**
     * 获取话题评论
     */
    public List<Comment> getComments(long topicId) {
        return getComments(String.valueOf(topicId), null);
    }

    /**
     * 获取话题评论
     */
    public List<Comment> getComments(long topicId, ListCommentsOptions options) {
        return getComments(String.valueOf(topicId), options);
    }

    /**
     * 获取话题评论
     */
    public List<Comment> getComments(String topicId) {
        return getComments(topicId, null);
    }

    /**
     * 获取话题评论
     */
    public List<Comment> getComments(String topicId, ListCommentsOptions options) {
        Map<String, Object> params = options != null ? options.toMap() : new HashMap<>();
        Map<String, Object> data = httpClient.get(
                "/v2/topics/" + topicId + "/comments",
                params,
                new TypeToken<Map<String, Object>>() {}.getType());
        Object commentsObj = data.get("comments");
        if (commentsObj == null) return new ArrayList<>();
        String json = gson.toJson(commentsObj);
        return gson.fromJson(json, new TypeToken<List<Comment>>() {}.getType());
    }

    /**
     * 按标签获取话题
     */
    public List<Topic> listByHashtag(long hashtagId) {
        return listByHashtag(String.valueOf(hashtagId), null);
    }

    /**
     * 按标签获取话题
     */
    public List<Topic> listByHashtag(String hashtagId) {
        return listByHashtag(hashtagId, null);
    }

    /**
     * 按标签获取话题（带参数）
     */
    public List<Topic> listByHashtag(long hashtagId, ListTopicsOptions options) {
        return listByHashtag(String.valueOf(hashtagId), options);
    }

    /**
     * 按标签获取话题（带参数）
     */
    public List<Topic> listByHashtag(String hashtagId, ListTopicsOptions options) {
        Map<String, Object> params = options != null ? options.toMap() : new HashMap<>();
        Map<String, Object> data = httpClient.get(
                "/v2/hashtags/" + hashtagId + "/topics",
                params,
                new TypeToken<Map<String, Object>>() {}.getType());
        Object topicsObj = data.get("topics");
        if (topicsObj == null) return new ArrayList<>();
        String json = gson.toJson(topicsObj);
        return gson.fromJson(json, new TypeToken<List<Topic>>() {}.getType());
    }

    /**
     * 按专栏获取话题
     */
    public List<Topic> listByColumn(long groupId, long columnId) {
        return listByColumn(String.valueOf(groupId), String.valueOf(columnId), null);
    }

    /**
     * 按专栏获取话题
     */
    public List<Topic> listByColumn(String groupId, String columnId) {
        return listByColumn(groupId, columnId, null);
    }

    /**
     * 按专栏获取话题（带参数）
     */
    public List<Topic> listByColumn(long groupId, long columnId, ListTopicsOptions options) {
        return listByColumn(String.valueOf(groupId), String.valueOf(columnId), options);
    }

    /**
     * 按专栏获取话题（带参数）
     */
    public List<Topic> listByColumn(String groupId, String columnId, ListTopicsOptions options) {
        Map<String, Object> params = options != null ? options.toMap() : new HashMap<>();
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/columns/" + columnId + "/topics",
                params,
                new TypeToken<Map<String, Object>>() {}.getType());
        Object topicsObj = data.get("topics");
        if (topicsObj == null) return new ArrayList<>();
        String json = gson.toJson(topicsObj);
        return gson.fromJson(json, new TypeToken<List<Topic>>() {}.getType());
    }

    private Topic convertToTopic(Object obj) {
        if (obj == null) return null;
        String json = gson.toJson(obj);
        return gson.fromJson(json, Topic.class);
    }

    /**
     * 获取话题基础信息（轻量级）
     *
     * @param topicId 话题ID
     * @return 话题对象
     */
    public Topic getInfo(long topicId) {
        return getInfo(String.valueOf(topicId));
    }

    /**
     * 获取话题基础信息（轻量级）
     *
     * @param topicId 话题ID
     * @return 话题对象
     */
    public Topic getInfo(String topicId) {
        Map<String, Object> data = httpClient.get("/v2/topics/" + topicId + "/info",
                new TypeToken<Map<String, Object>>() {}.getType());
        return convertToTopic(data.get("topic"));
    }

    /**
     * 获取话题打赏列表
     *
     * @param topicId 话题ID
     * @return 打赏列表
     */
    public List<Reward> getRewards(long topicId) {
        return getRewards(String.valueOf(topicId));
    }

    /**
     * 获取话题打赏列表
     *
     * @param topicId 话题ID
     * @return 打赏列表
     */
    public List<Reward> getRewards(String topicId) {
        Map<String, Object> data = httpClient.get("/v2/topics/" + topicId + "/rewards",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object rewardsObj = data.get("rewards");
        if (rewardsObj == null) return new ArrayList<>();
        String json = gson.toJson(rewardsObj);
        return gson.fromJson(json, new TypeToken<List<Reward>>() {}.getType());
    }

    /**
     * 获取相关推荐话题
     *
     * @param topicId 话题ID
     * @return 推荐话题列表
     */
    public List<Topic> getRecommendations(long topicId) {
        return getRecommendations(String.valueOf(topicId));
    }

    /**
     * 获取相关推荐话题
     *
     * @param topicId 话题ID
     * @return 推荐话题列表
     */
    public List<Topic> getRecommendations(String topicId) {
        Map<String, Object> data = httpClient.get("/v2/topics/" + topicId + "/recommendations",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object topicsObj = data.get("topics");
        if (topicsObj == null) return new ArrayList<>();
        String json = gson.toJson(topicsObj);
        return gson.fromJson(json, new TypeToken<List<Topic>>() {}.getType());
    }

    /**
     * 获取置顶话题列表
     *
     * @param groupId 星球ID
     * @return 置顶话题列表
     */
    public List<Topic> listSticky(long groupId) {
        return listSticky(String.valueOf(groupId));
    }

    /**
     * 获取置顶话题列表
     *
     * @param groupId 星球ID
     * @return 置顶话题列表
     */
    public List<Topic> listSticky(String groupId) {
        Map<String, Object> data = httpClient.get("/v2/groups/" + groupId + "/topics/sticky",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object topicsObj = data.get("topics");
        if (topicsObj == null) return new ArrayList<>();
        String json = gson.toJson(topicsObj);
        return gson.fromJson(json, new TypeToken<List<Topic>>() {}.getType());
    }

    /**
     * 话题列表查询参数
     */
    public static class ListTopicsOptions {
        private Integer count;
        private String scope;  // "all" | "digests" | "by_owner"
        private String direction;  // "forward" | "backward"
        private String beginTime;
        private String endTime;
        private Boolean withInvisibles;

        public ListTopicsOptions count(int count) {
            this.count = count;
            return this;
        }

        public ListTopicsOptions scope(String scope) {
            this.scope = scope;
            return this;
        }

        public ListTopicsOptions direction(String direction) {
            this.direction = direction;
            return this;
        }

        public ListTopicsOptions beginTime(String beginTime) {
            this.beginTime = beginTime;
            return this;
        }

        public ListTopicsOptions endTime(String endTime) {
            this.endTime = endTime;
            return this;
        }

        public ListTopicsOptions withInvisibles(boolean withInvisibles) {
            this.withInvisibles = withInvisibles;
            return this;
        }

        Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            if (count != null) map.put("count", count);
            if (scope != null) map.put("scope", scope);
            if (direction != null) map.put("direction", direction);
            if (beginTime != null) map.put("begin_time", beginTime);
            if (endTime != null) map.put("end_time", endTime);
            if (withInvisibles != null) map.put("with_invisibles", withInvisibles);
            return map;
        }
    }

    /**
     * 评论列表查询参数
     */
    public static class ListCommentsOptions {
        private Integer count;
        private String sort; // "asc" | "desc"
        private Boolean withSticky;

        public ListCommentsOptions count(int count) {
            this.count = count;
            return this;
        }

        public ListCommentsOptions sort(String sort) {
            this.sort = sort;
            return this;
        }

        public ListCommentsOptions withSticky(boolean withSticky) {
            this.withSticky = withSticky;
            return this;
        }

        Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            if (count != null) map.put("count", count);
            if (sort != null) map.put("sort", sort);
            if (withSticky != null) map.put("with_sticky", withSticky);
            return map;
        }
    }
}
