package com.zsxq.sdk.model;

import lombok.Data;

/**
 * 打卡统计模型
 */
@Data
public class CheckinStatistics {
    private Integer joinedCount;
    private Integer completedCount;
    private Integer checkinedCount;
}
