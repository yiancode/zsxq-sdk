package com.zsxq.sdk.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * 打卡项目模型
 */
@Data
public class Checkin {
    @SerializedName("checkin_id")
    private Long checkinId;

    private Group group;
    private User owner;

    /**
     * 训练营/打卡项目标题
     * API 字段名: title
     */
    private String title;

    /**
     * 打卡项目描述
     * API 字段名: text
     */
    private String text;

    @SerializedName("cover_url")
    private String coverUrl;

    private String status;  // "ongoing" | "closed" | "over"

    @SerializedName("create_time")
    private String createTime;

    @SerializedName("begin_time")
    private String beginTime;

    @SerializedName("end_time")
    private String endTime;
}
