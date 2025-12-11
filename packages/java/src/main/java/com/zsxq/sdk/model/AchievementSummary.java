package com.zsxq.sdk.model;

/**
 * 成就摘要模型
 */
public class AchievementSummary {
    private String uid;
    private String type;
    private String achieveTime;
    private Issuer issuer;
    private String title;
    private String description;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAchieveTime() {
        return achieveTime;
    }

    public void setAchieveTime(String achieveTime) {
        this.achieveTime = achieveTime;
    }

    public Issuer getIssuer() {
        return issuer;
    }

    public void setIssuer(Issuer issuer) {
        this.issuer = issuer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 成就颁发者
     */
    public static class Issuer {
        private String name;
        private String avatarUrl;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }
    }
}
