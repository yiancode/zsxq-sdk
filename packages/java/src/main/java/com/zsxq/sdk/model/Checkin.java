package com.zsxq.sdk.model;

import lombok.Data;

/**
 * 打卡项目模型
 */
@Data
public class Checkin {
    private Long checkinId;
    private Group group;
    private User owner;
    private String name;
    private String description;
    private String coverUrl;
    private String status;  // "ongoing" | "closed" | "over"
    private String createTime;
    private String beginTime;
    private String endTime;
}
