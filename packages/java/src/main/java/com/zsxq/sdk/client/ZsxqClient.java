package com.zsxq.sdk.client;

import com.zsxq.sdk.http.HttpClient;
import com.zsxq.sdk.request.*;

/**
 * 知识星球 SDK 主客户端
 *
 * 使用 ZsxqClientBuilder 构建实例：
 * <pre>
 * ZsxqClient client = new ZsxqClientBuilder()
 *     .token("your-token")
 *     .build();
 *
 * // 获取星球列表
 * List&lt;Group&gt; groups = client.groups().list();
 *
 * // 获取话题
 * List&lt;Topic&gt; topics = client.topics().list(groupId);
 * </pre>
 */
public class ZsxqClient {

    private final GroupsRequest groups;
    private final TopicsRequest topics;
    private final UsersRequest users;
    private final CheckinsRequest checkins;
    private final DashboardRequest dashboard;
    private final RankingRequest ranking;
    private final MiscRequest misc;

    ZsxqClient(ZsxqConfig config) {
        HttpClient httpClient = new HttpClient(config);

        this.groups = new GroupsRequest(httpClient);
        this.topics = new TopicsRequest(httpClient);
        this.users = new UsersRequest(httpClient);
        this.checkins = new CheckinsRequest(httpClient);
        this.dashboard = new DashboardRequest(httpClient);
        this.ranking = new RankingRequest(httpClient);
        this.misc = new MiscRequest(httpClient);
    }

    /**
     * 星球管理
     */
    public GroupsRequest groups() {
        return groups;
    }

    /**
     * 话题管理
     */
    public TopicsRequest topics() {
        return topics;
    }

    /**
     * 用户管理
     */
    public UsersRequest users() {
        return users;
    }

    /**
     * 打卡管理
     */
    public CheckinsRequest checkins() {
        return checkins;
    }

    /**
     * 数据面板
     */
    public DashboardRequest dashboard() {
        return dashboard;
    }

    /**
     * 排行榜
     */
    public RankingRequest ranking() {
        return ranking;
    }

    /**
     * 杂项功能
     */
    public MiscRequest misc() {
        return misc;
    }
}
