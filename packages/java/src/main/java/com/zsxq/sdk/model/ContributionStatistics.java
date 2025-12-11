package com.zsxq.sdk.model;

/**
 * 贡献统计模型
 */
public class ContributionStatistics {
    private Integer maxConsecutiveDays;
    private Integer currentConsecutiveDays;

    public Integer getMaxConsecutiveDays() {
        return maxConsecutiveDays;
    }

    public void setMaxConsecutiveDays(Integer maxConsecutiveDays) {
        this.maxConsecutiveDays = maxConsecutiveDays;
    }

    public Integer getCurrentConsecutiveDays() {
        return currentConsecutiveDays;
    }

    public void setCurrentConsecutiveDays(Integer currentConsecutiveDays) {
        this.currentConsecutiveDays = currentConsecutiveDays;
    }
}
