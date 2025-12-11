package com.zsxq.sdk.model;

import lombok.Data;

/**
 * URL 解析结果模型
 */
@Data
public class UrlDetail {
    private String url;
    private String title;
    private String description;
    private String imageUrl;
    private String siteName;
}
