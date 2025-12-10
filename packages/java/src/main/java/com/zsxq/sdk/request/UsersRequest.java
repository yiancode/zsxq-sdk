package com.zsxq.sdk.request;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zsxq.sdk.http.HttpClient;
import com.zsxq.sdk.model.Group;
import com.zsxq.sdk.model.User;

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
        Map<String, Object> data = httpClient.get("/v3/users/" + userId,
                new TypeToken<Map<String, Object>>() {}.getType());
        return convertToUser(data.get("user"));
    }

    /**
     * 获取用户统计
     */
    public Map<String, Object> getStatistics(long userId) {
        return httpClient.get("/v3/users/" + userId + "/statistics",
                new TypeToken<Map<String, Object>>() {}.getType());
    }

    /**
     * 获取用户创建的星球
     */
    public List<Group> getCreatedGroups(long userId) {
        Map<String, List<Group>> data = httpClient.get(
                "/v2/users/" + userId + "/created_groups",
                new TypeToken<Map<String, List<Group>>>() {}.getType());
        return data.get("groups");
    }

    /**
     * 将 Object 转换为 User
     */
    private User convertToUser(Object obj) {
        if (obj == null) return null;
        String json = gson.toJson(obj);
        return gson.fromJson(json, User.class);
    }
}
