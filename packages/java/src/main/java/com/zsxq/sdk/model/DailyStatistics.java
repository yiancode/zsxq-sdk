package com.zsxq.sdk.model;

import lombok.Data;

/**
 * 打卡每日统计模型
 */
@Data
public class DailyStatistics {
    private String date;
    private Integer checkinCount;
    private Integer userCount;
}
