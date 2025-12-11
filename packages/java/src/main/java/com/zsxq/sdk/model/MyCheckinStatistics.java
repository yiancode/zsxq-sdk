package com.zsxq.sdk.model;

import lombok.Data;

/**
 * 我的打卡统计模型
 */
@Data
public class MyCheckinStatistics {
    private Integer totalCheckinCount;
    private Integer continuousDays;
    private Integer maxContinuousDays;
    private String lastCheckinTime;
}
