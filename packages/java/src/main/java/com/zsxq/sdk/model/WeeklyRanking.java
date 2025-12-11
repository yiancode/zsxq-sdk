package com.zsxq.sdk.model;

/**
 * 星球周榜排名模型
 */
public class WeeklyRanking {
    private RankingItem topTopics;
    private RankingItem topLikes;
    private RankingItem topDigests;

    public RankingItem getTopTopics() {
        return topTopics;
    }

    public void setTopTopics(RankingItem topTopics) {
        this.topTopics = topTopics;
    }

    public RankingItem getTopLikes() {
        return topLikes;
    }

    public void setTopLikes(RankingItem topLikes) {
        this.topLikes = topLikes;
    }

    public RankingItem getTopDigests() {
        return topDigests;
    }

    public void setTopDigests(RankingItem topDigests) {
        this.topDigests = topDigests;
    }

    /**
     * 排名项
     */
    public static class RankingItem {
        private Integer ranking;
        private Integer count;

        public Integer getRanking() {
            return ranking;
        }

        public void setRanking(Integer ranking) {
            this.ranking = ranking;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }
}
