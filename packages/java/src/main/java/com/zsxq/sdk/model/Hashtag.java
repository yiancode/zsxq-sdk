package com.zsxq.sdk.model;

import lombok.Data;

/**
 * 标签模型
 */
@Data
public class Hashtag {
    private Long hashtagId;
    private String name;
    private Integer topicsCount;
}
