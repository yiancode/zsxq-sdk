package com.zsxq.sdk.model;

/**
 * 成员活跃摘要模型
 */
public class ActivitySummary {
    private Integer topicsCount;
    private Integer commentsCount;
    private Integer likesReceived;
    private Integer likesGiven;
    private Integer rewardsReceived;
    private Integer rewardsGiven;

    public Integer getTopicsCount() {
        return topicsCount;
    }

    public void setTopicsCount(Integer topicsCount) {
        this.topicsCount = topicsCount;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Integer getLikesReceived() {
        return likesReceived;
    }

    public void setLikesReceived(Integer likesReceived) {
        this.likesReceived = likesReceived;
    }

    public Integer getLikesGiven() {
        return likesGiven;
    }

    public void setLikesGiven(Integer likesGiven) {
        this.likesGiven = likesGiven;
    }

    public Integer getRewardsReceived() {
        return rewardsReceived;
    }

    public void setRewardsReceived(Integer rewardsReceived) {
        this.rewardsReceived = rewardsReceived;
    }

    public Integer getRewardsGiven() {
        return rewardsGiven;
    }

    public void setRewardsGiven(Integer rewardsGiven) {
        this.rewardsGiven = rewardsGiven;
    }

    @Override
    public String toString() {
        return "ActivitySummary{" +
                "topicsCount=" + topicsCount +
                ", commentsCount=" + commentsCount +
                ", likesReceived=" + likesReceived +
                ", likesGiven=" + likesGiven +
                ", rewardsReceived=" + rewardsReceived +
                ", rewardsGiven=" + rewardsGiven +
                '}';
    }
}
