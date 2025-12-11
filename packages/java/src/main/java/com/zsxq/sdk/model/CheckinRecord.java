package com.zsxq.sdk.model;

import lombok.Data;

/**
 * 打卡记录模型
 */
@Data
public class CheckinRecord {
    private Long topicId;
    private String topicUid;
    private User owner;
    private String text;
    private String createTime;
    private Integer likesCount;
    private Integer commentsCount;
}
