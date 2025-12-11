package com.zsxq.sdk.model;

/**
 * 排行榜统计模型
 */
public class RankingStatistics {
    private Integer totalCount;
    private Integer myRank;
    private Integer myScore;
    private String rankingType;
    private String periodType;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getMyRank() {
        return myRank;
    }

    public void setMyRank(Integer myRank) {
        this.myRank = myRank;
    }

    public Integer getMyScore() {
        return myScore;
    }

    public void setMyScore(Integer myScore) {
        this.myScore = myScore;
    }

    public String getRankingType() {
        return rankingType;
    }

    public void setRankingType(String rankingType) {
        this.rankingType = rankingType;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    @Override
    public String toString() {
        return "RankingStatistics{" +
                "totalCount=" + totalCount +
                ", myRank=" + myRank +
                ", myScore=" + myScore +
                ", rankingType='" + rankingType + '\'' +
                ", periodType='" + periodType + '\'' +
                '}';
    }
}
