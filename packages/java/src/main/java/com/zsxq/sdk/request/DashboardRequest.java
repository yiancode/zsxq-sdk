package com.zsxq.sdk.request;

import com.google.gson.reflect.TypeToken;
import com.zsxq.sdk.http.HttpClient;

import java.util.Map;

/**
 * Dashboard 请求模块
 */
public class DashboardRequest extends BaseRequest {

    public DashboardRequest(HttpClient httpClient) {
        super(httpClient);
    }

    /**
     * 获取星球概览
     */
    public Map<String, Object> getOverview(long groupId) {
        return httpClient.get(
                "/v2/dashboard/groups/" + groupId + "/overview",
                new TypeToken<Map<String, Object>>() {}.getType());
    }

    /**
     * 获取收入概览
     */
    public Map<String, Object> getIncomes(long groupId) {
        return httpClient.get(
                "/v2/dashboard/groups/" + groupId + "/incomes/overview",
                new TypeToken<Map<String, Object>>() {}.getType());
    }
}
