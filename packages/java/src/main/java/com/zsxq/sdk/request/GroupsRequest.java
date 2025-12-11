package com.zsxq.sdk.request;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zsxq.sdk.http.HttpClient;
import com.zsxq.sdk.model.Group;
import com.zsxq.sdk.model.Hashtag;
import com.zsxq.sdk.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 星球请求模块
 */
public class GroupsRequest extends BaseRequest {

    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public GroupsRequest(HttpClient httpClient) {
        super(httpClient);
    }

    /**
     * 获取我的星球列表
     */
    @SuppressWarnings("unchecked")
    public List<Group> list() {
        Map<String, Object> data = httpClient.get("/v2/groups",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object groupsObj = data.get("groups");
        if (groupsObj == null) return new ArrayList<>();
        String json = gson.toJson(groupsObj);
        return gson.fromJson(json, new TypeToken<List<Group>>() {}.getType());
    }

    /**
     * 获取星球详情
     */
    public Group get(long groupId) {
        return get(String.valueOf(groupId));
    }

    /**
     * 获取星球详情
     */
    public Group get(String groupId) {
        Map<String, Object> data = httpClient.get("/v2/groups/" + groupId,
                new TypeToken<Map<String, Object>>() {}.getType());
        return convertToGroup(data.get("group"));
    }

    /**
     * 获取星球统计
     */
    public Map<String, Object> getStatistics(long groupId) {
        return getStatistics(String.valueOf(groupId));
    }

    /**
     * 获取星球统计
     */
    public Map<String, Object> getStatistics(String groupId) {
        return httpClient.get("/v2/groups/" + groupId + "/statistics",
                new TypeToken<Map<String, Object>>() {}.getType());
    }

    /**
     * 获取成员信息
     */
    public User getMember(long groupId, long memberId) {
        return getMember(String.valueOf(groupId), String.valueOf(memberId));
    }

    /**
     * 获取成员信息
     */
    public User getMember(String groupId, String memberId) {
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/members/" + memberId,
                new TypeToken<Map<String, Object>>() {}.getType());
        Object userObj = data.get("user");
        if (userObj == null) return null;
        String json = gson.toJson(userObj);
        return gson.fromJson(json, User.class);
    }

    /**
     * 获取星球标签
     */
    public List<Hashtag> getHashtags(long groupId) {
        return getHashtags(String.valueOf(groupId));
    }

    /**
     * 获取星球标签
     */
    public List<Hashtag> getHashtags(String groupId) {
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/hashtags",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object hashtagsObj = data.get("hashtags");
        if (hashtagsObj == null) return new ArrayList<>();
        String json = gson.toJson(hashtagsObj);
        return gson.fromJson(json, new TypeToken<List<Hashtag>>() {}.getType());
    }

    /**
     * 获取未读话题数量
     */
    public Map<String, Integer> getUnreadCount() {
        return httpClient.get("/v2/groups/unread_topics_count",
                new TypeToken<Map<String, Integer>>() {}.getType());
    }

    private Group convertToGroup(Object obj) {
        if (obj == null) return null;
        String json = gson.toJson(obj);
        return gson.fromJson(json, Group.class);
    }
}
