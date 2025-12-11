package com.zsxq.sdk.model;

import java.util.List;

/**
 * 积分榜设置模型
 */
public class ScoreboardSettings {
    private Boolean enabled;
    private String name;
    private String description;
    private List<ScoreRule> scoreRules;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ScoreRule> getScoreRules() {
        return scoreRules;
    }

    public void setScoreRules(List<ScoreRule> scoreRules) {
        this.scoreRules = scoreRules;
    }

    @Override
    public String toString() {
        return "ScoreboardSettings{" +
                "enabled=" + enabled +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", scoreRules=" + scoreRules +
                '}';
    }

    /**
     * 积分规则
     */
    public static class ScoreRule {
        private String action;
        private Integer score;
        private String description;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "ScoreRule{" +
                    "action='" + action + '\'' +
                    ", score=" + score +
                    ", description='" + description + '\'' +
                    '}';
        }
    }
}
