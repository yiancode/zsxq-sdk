package com.zsxq.sdk.model;

/**
 * 邀请人信息模型
 */
public class Inviter {
    private User user;
    private String inviteTime;
    private String inviteCode;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getInviteTime() {
        return inviteTime;
    }

    public void setInviteTime(String inviteTime) {
        this.inviteTime = inviteTime;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    @Override
    public String toString() {
        return "Inviter{" +
                "user=" + user +
                ", inviteTime='" + inviteTime + '\'' +
                ", inviteCode='" + inviteCode + '\'' +
                '}';
    }
}
