package com.zsxq.sdk.model;

/**
 * 打赏模型
 */
public class Reward {
    private User user;
    private Integer amount;
    private String createTime;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Reward{" +
                "user=" + user +
                ", amount=" + amount +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
