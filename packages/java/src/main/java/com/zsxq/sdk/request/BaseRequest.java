package com.zsxq.sdk.request;

import com.zsxq.sdk.http.HttpClient;

/**
 * 请求基类
 */
public abstract class BaseRequest {

    protected final HttpClient httpClient;

    protected BaseRequest(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
