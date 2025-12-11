package com.zsxq.sdk.request;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zsxq.sdk.http.HttpClient;
import com.zsxq.sdk.model.Activity;
import com.zsxq.sdk.model.GlobalConfig;
import com.zsxq.sdk.model.PkBattle;
import com.zsxq.sdk.model.PkGroup;
import com.zsxq.sdk.model.UrlDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 杂项请求模块
 */
public class MiscRequest extends BaseRequest {

    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public MiscRequest(HttpClient httpClient) {
        super(httpClient);
    }

    /**
     * 获取 PK 群组详情
     */
    public PkGroup getPkGroup(long pkGroupId) {
        return getPkGroup(String.valueOf(pkGroupId));
    }

    /**
     * 获取 PK 群组详情
     */
    public PkGroup getPkGroup(String pkGroupId) {
        Map<String, Object> data = httpClient.get(
                "/v2/pk_groups/" + pkGroupId,
                new TypeToken<Map<String, Object>>() {}.getType());
        Object groupObj = data.get("group");
        if (groupObj == null) return null;
        String json = gson.toJson(groupObj);
        return gson.fromJson(json, PkGroup.class);
    }

    /**
     * 获取 PK 对战记录
     */
    public List<PkBattle> getPkBattles(long pkGroupId) {
        return getPkBattles(String.valueOf(pkGroupId), null);
    }

    /**
     * 获取 PK 对战记录
     */
    public List<PkBattle> getPkBattles(String pkGroupId) {
        return getPkBattles(pkGroupId, null);
    }

    /**
     * 获取 PK 对战记录（带参数）
     */
    public List<PkBattle> getPkBattles(long pkGroupId, PkBattlesOptions options) {
        return getPkBattles(String.valueOf(pkGroupId), options);
    }

    /**
     * 获取 PK 对战记录（带参数）
     */
    public List<PkBattle> getPkBattles(String pkGroupId, PkBattlesOptions options) {
        Map<String, Object> params = options != null ? options.toMap() : null;
        Map<String, Object> data = httpClient.get(
                "/v2/pk_groups/" + pkGroupId + "/records",
                params,
                new TypeToken<Map<String, Object>>() {}.getType());
        Object recordsObj = data.get("records");
        if (recordsObj == null) return new ArrayList<>();
        String json = gson.toJson(recordsObj);
        return gson.fromJson(json, new TypeToken<List<PkBattle>>() {}.getType());
    }

    /**
     * 解析 URL 详情
     */
    public UrlDetail parseUrl(String url) {
        Map<String, Object> params = new HashMap<>();
        params.put("url", url);
        Map<String, Object> data = httpClient.get(
                "/v2/url_details",
                params,
                new TypeToken<Map<String, Object>>() {}.getType());
        Object detailObj = data.get("url_detail");
        if (detailObj == null) return null;
        String json = gson.toJson(detailObj);
        return gson.fromJson(json, UrlDetail.class);
    }

    /**
     * 获取全局配置
     */
    public GlobalConfig getGlobalConfig() {
        Map<String, Object> data = httpClient.get(
                "/v2/settings",
                new TypeToken<Map<String, Object>>() {}.getType());
        String json = gson.toJson(data);
        return gson.fromJson(json, GlobalConfig.class);
    }

    /**
     * 获取动态列表
     */
    public List<Activity> getActivities() {
        return getActivities(null);
    }

    /**
     * 获取动态列表（带参数）
     */
    public List<Activity> getActivities(ActivitiesOptions options) {
        Map<String, Object> params = options != null ? options.toMap() : null;
        Map<String, Object> data = httpClient.get(
                "/v2/dynamics",
                params,
                new TypeToken<Map<String, Object>>() {}.getType());
        Object dynamicsObj = data.get("dynamics");
        if (dynamicsObj == null) return new ArrayList<>();
        String json = gson.toJson(dynamicsObj);
        return gson.fromJson(json, new TypeToken<List<Activity>>() {}.getType());
    }

    /**
     * PK 对战记录查询参数
     */
    public static class PkBattlesOptions {
        private Integer count;

        public PkBattlesOptions count(int count) {
            this.count = count;
            return this;
        }

        Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            if (count != null) map.put("count", count);
            return map;
        }
    }

    /**
     * 动态列表查询参数
     */
    public static class ActivitiesOptions {
        private String scope;  // "general" | "like" | "examinations" | "system_message"
        private Integer count;
        private String endTime;

        public ActivitiesOptions scope(String scope) {
            this.scope = scope;
            return this;
        }

        public ActivitiesOptions count(int count) {
            this.count = count;
            return this;
        }

        public ActivitiesOptions endTime(String endTime) {
            this.endTime = endTime;
            return this;
        }

        Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            if (scope != null) map.put("scope", scope);
            if (count != null) map.put("count", count);
            if (endTime != null) map.put("end_time", endTime);
            return map;
        }
    }
}
