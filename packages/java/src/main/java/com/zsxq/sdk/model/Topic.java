package com.zsxq.sdk.model;

import lombok.Data;

/**
 * 话题模型
 */
@Data
public class Topic {
    private Long topicId;
    private String topicUid;
    private Group group;
    private String type;  // "talk" | "task" | "q&a" | "solution"
    private String createTime;
    private TalkContent talk;
    private Integer likesCount;
    private Integer commentsCount;
    private Integer rewardsCount;
    private Integer readingCount;
    private Boolean digested;
    private Boolean sticky;
}
