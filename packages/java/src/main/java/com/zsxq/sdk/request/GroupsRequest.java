package com.zsxq.sdk.request;

import com.google.gson.reflect.TypeToken;
import com.zsxq.sdk.http.HttpClient;
import com.zsxq.sdk.model.Group;
import com.zsxq.sdk.model.User;

import java.util.List;
import java.util.Map;

/**
 * 星球请求模块
 */
public class GroupsRequest extends BaseRequest {

    public GroupsRequest(HttpClient httpClient) {
        super(httpClient);
    }

    /**
     * 获取我的星球列表
     */
    public List<Group> list() {
        Map<String, List<Group>> data = httpClient.get("/v2/groups",
                new TypeToken<Map<String, List<Group>>>() {}.getType());
        return data.get("groups");
    }

    /**
     * 获取星球详情
     */
    public Group get(long groupId) {
        Map<String, Group> data = httpClient.get("/v2/groups/" + groupId,
                new TypeToken<Map<String, Group>>() {}.getType());
        return data.get("group");
    }

    /**
     * 获取星球详情
     */
    public Group get(String groupId) {
        Map<String, Group> data = httpClient.get("/v2/groups/" + groupId,
                new TypeToken<Map<String, Group>>() {}.getType());
        return data.get("group");
    }

    /**
     * 获取星球统计
     */
    public Map<String, Object> getStatistics(long groupId) {
        return httpClient.get("/v2/groups/" + groupId + "/statistics",
                new TypeToken<Map<String, Object>>() {}.getType());
    }

    /**
     * 获取成员信息
     */
    public User getMember(long groupId, long memberId) {
        Map<String, User> data = httpClient.get(
                "/v2/groups/" + groupId + "/members/" + memberId,
                new TypeToken<Map<String, User>>() {}.getType());
        return data.get("user");
    }
}
