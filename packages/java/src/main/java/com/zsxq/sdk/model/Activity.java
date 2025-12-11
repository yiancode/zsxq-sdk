package com.zsxq.sdk.model;

import lombok.Data;

/**
 * 动态模型
 */
@Data
public class Activity {
    private Long dynamicId;
    private String action;  // "create_topic" | "like" | "comment" | etc.
    private String createTime;
    private Topic topic;
    private User user;
    private Group group;
}
