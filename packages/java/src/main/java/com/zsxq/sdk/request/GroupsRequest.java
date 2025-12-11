package com.zsxq.sdk.request;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zsxq.sdk.http.HttpClient;
import com.zsxq.sdk.model.ActivitySummary;
import com.zsxq.sdk.model.Column;
import com.zsxq.sdk.model.CustomTag;
import com.zsxq.sdk.model.DistributionInfo;
import com.zsxq.sdk.model.Group;
import com.zsxq.sdk.model.GroupWarning;
import com.zsxq.sdk.model.Hashtag;
import com.zsxq.sdk.model.Menu;
import com.zsxq.sdk.model.RenewalInfo;
import com.zsxq.sdk.model.RoleMembers;
import com.zsxq.sdk.model.ScheduledJob;
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

    /**
     * 获取星球菜单配置
     *
     * @param groupId 星球ID
     * @return 菜单列表
     */
    public List<Menu> getMenus(long groupId) {
        return getMenus(String.valueOf(groupId));
    }

    /**
     * 获取星球菜单配置
     *
     * @param groupId 星球ID
     * @return 菜单列表
     */
    public List<Menu> getMenus(String groupId) {
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/menus",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object menusObj = data.get("menus");
        if (menusObj == null) return new ArrayList<>();
        String json = gson.toJson(menusObj);
        return gson.fromJson(json, new TypeToken<List<Menu>>() {}.getType());
    }

    /**
     * 获取星球角色成员（星主、合伙人、管理员）
     *
     * @param groupId 星球ID
     * @return 角色成员对象
     */
    public RoleMembers getRoleMembers(long groupId) {
        return getRoleMembers(String.valueOf(groupId));
    }

    /**
     * 获取星球角色成员（星主、合伙人、管理员）
     *
     * @param groupId 星球ID
     * @return 角色成员对象
     */
    public RoleMembers getRoleMembers(String groupId) {
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/role_members",
                new TypeToken<Map<String, Object>>() {}.getType());
        String json = gson.toJson(data);
        return gson.fromJson(json, RoleMembers.class);
    }

    /**
     * 获取星球专栏列表
     *
     * @param groupId 星球ID
     * @return 专栏列表
     */
    public List<Column> getColumns(long groupId) {
        return getColumns(String.valueOf(groupId));
    }

    /**
     * 获取星球专栏列表
     *
     * @param groupId 星球ID
     * @return 专栏列表
     */
    public List<Column> getColumns(String groupId) {
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/columns",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object columnsObj = data.get("columns");
        if (columnsObj == null) return new ArrayList<>();
        String json = gson.toJson(columnsObj);
        return gson.fromJson(json, new TypeToken<List<Column>>() {}.getType());
    }

    /**
     * 获取专栏汇总信息
     *
     * @param groupId 星球ID
     * @return 专栏汇总数据
     */
    public Map<String, Object> getColumnsSummary(long groupId) {
        return getColumnsSummary(String.valueOf(groupId));
    }

    /**
     * 获取专栏汇总信息
     *
     * @param groupId 星球ID
     * @return 专栏汇总数据
     */
    public Map<String, Object> getColumnsSummary(String groupId) {
        return httpClient.get(
                "/v2/groups/" + groupId + "/columns/summary",
                new TypeToken<Map<String, Object>>() {}.getType());
    }

    /**
     * 获取成员活跃摘要
     *
     * @param groupId 星球ID
     * @param memberId 成员ID
     * @return 活跃摘要
     */
    public ActivitySummary getMemberActivitySummary(long groupId, long memberId) {
        return getMemberActivitySummary(String.valueOf(groupId), String.valueOf(memberId));
    }

    /**
     * 获取成员活跃摘要
     *
     * @param groupId 星球ID
     * @param memberId 成员ID
     * @return 活跃摘要
     */
    public ActivitySummary getMemberActivitySummary(String groupId, String memberId) {
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/members/" + memberId + "/summary",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object summaryObj = data.get("summary");
        if (summaryObj == null) return null;
        String json = gson.toJson(summaryObj);
        return gson.fromJson(json, ActivitySummary.class);
    }

    /**
     * 获取星球续费信息
     *
     * @param groupId 星球ID
     * @return 续费信息
     */
    public RenewalInfo getRenewalInfo(long groupId) {
        return getRenewalInfo(String.valueOf(groupId));
    }

    /**
     * 获取星球续费信息
     *
     * @param groupId 星球ID
     * @return 续费信息
     */
    public RenewalInfo getRenewalInfo(String groupId) {
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/renewal",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object renewalObj = data.get("renewal");
        if (renewalObj == null) {
            String json = gson.toJson(data);
            return gson.fromJson(json, RenewalInfo.class);
        }
        String json = gson.toJson(renewalObj);
        return gson.fromJson(json, RenewalInfo.class);
    }

    /**
     * 获取星球分销信息
     *
     * @param groupId 星球ID
     * @return 分销信息
     */
    public DistributionInfo getDistribution(long groupId) {
        return getDistribution(String.valueOf(groupId));
    }

    /**
     * 获取星球分销信息
     *
     * @param groupId 星球ID
     * @return 分销信息
     */
    public DistributionInfo getDistribution(String groupId) {
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/distribution",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object distributionObj = data.get("distribution");
        if (distributionObj == null) {
            String json = gson.toJson(data);
            return gson.fromJson(json, DistributionInfo.class);
        }
        String json = gson.toJson(distributionObj);
        return gson.fromJson(json, DistributionInfo.class);
    }

    /**
     * 获取可升级星球列表
     *
     * @return 可升级的星球列表
     */
    public List<Group> getUpgradeableGroups() {
        Map<String, Object> data = httpClient.get("/v2/groups/upgradable_groups",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object groupsObj = data.get("groups");
        if (groupsObj == null) return new ArrayList<>();
        String json = gson.toJson(groupsObj);
        return gson.fromJson(json, new TypeToken<List<Group>>() {}.getType());
    }

    /**
     * 获取推荐星球列表
     *
     * @return 推荐的星球列表
     */
    public List<Group> getRecommendedGroups() {
        Map<String, Object> data = httpClient.get("/v2/groups/recommendations",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object groupsObj = data.get("groups");
        if (groupsObj == null) return new ArrayList<>();
        String json = gson.toJson(groupsObj);
        return gson.fromJson(json, new TypeToken<List<Group>>() {}.getType());
    }

    /**
     * 获取星球自定义标签
     *
     * @param groupId 星球ID
     * @return 自定义标签列表
     */
    public List<CustomTag> getCustomTags(long groupId) {
        return getCustomTags(String.valueOf(groupId));
    }

    /**
     * 获取星球自定义标签
     *
     * @param groupId 星球ID
     * @return 自定义标签列表
     */
    public List<CustomTag> getCustomTags(String groupId) {
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/labels",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object labelsObj = data.get("labels");
        if (labelsObj == null) return new ArrayList<>();
        String json = gson.toJson(labelsObj);
        return gson.fromJson(json, new TypeToken<List<CustomTag>>() {}.getType());
    }

    /**
     * 获取星球定时任务
     *
     * @param groupId 星球ID
     * @return 定时任务列表
     */
    public List<ScheduledJob> getScheduledTasks(long groupId) {
        return getScheduledTasks(String.valueOf(groupId));
    }

    /**
     * 获取星球定时任务
     *
     * @param groupId 星球ID
     * @return 定时任务列表
     */
    public List<ScheduledJob> getScheduledTasks(String groupId) {
        Map<String, Object> data = httpClient.get(
                "/v2/groups/" + groupId + "/scheduled_jobs",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object jobsObj = data.get("jobs");
        if (jobsObj == null) return new ArrayList<>();
        String json = gson.toJson(jobsObj);
        return gson.fromJson(json, new TypeToken<List<ScheduledJob>>() {}.getType());
    }

    /**
     * 获取星球风险预警
     *
     * @param groupId 星球ID
     * @return 风险预警信息
     */
    public GroupWarning getRiskWarnings(long groupId) {
        return getRiskWarnings(String.valueOf(groupId));
    }

    /**
     * 获取星球风险预警
     *
     * @param groupId 星球ID
     * @return 风险预警信息
     */
    public GroupWarning getRiskWarnings(String groupId) {
        Map<String, Object> data = httpClient.get(
                "/v3/groups/" + groupId + "/group_warning",
                new TypeToken<Map<String, Object>>() {}.getType());
        Object warningObj = data.get("warning");
        if (warningObj == null) {
            String json = gson.toJson(data);
            return gson.fromJson(json, GroupWarning.class);
        }
        String json = gson.toJson(warningObj);
        return gson.fromJson(json, GroupWarning.class);
    }
}
