package com.zsxq.sdk.request;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zsxq.sdk.http.HttpClient;
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
     * 获取话题列表（带参数）
     */
    public List<Topic> list(long groupId, ListTopicsOptions options) {
        Map<String, Object> params = options != null ? options.toMap() : new HashMap<>();
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

    private Topic convertToTopic(Object obj) {
        if (obj == null) return null;
        String json = gson.toJson(obj);
        return gson.fromJson(json, Topic.class);
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

        Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            if (count != null) map.put("count", count);
            if (scope != null) map.put("scope", scope);
            if (direction != null) map.put("direction", direction);
            if (beginTime != null) map.put("begin_time", beginTime);
            if (endTime != null) map.put("end_time", endTime);
            return map;
        }
    }
}
