package com.zsxq.sdk.model;

/**
 * 续费信息模型
 */
public class RenewalInfo {
    private Boolean renewalEnabled;
    private Integer renewalPrice;
    private Integer renewalDays;
    private String renewalDescription;
    private String expireTime;

    public Boolean getRenewalEnabled() {
        return renewalEnabled;
    }

    public void setRenewalEnabled(Boolean renewalEnabled) {
        this.renewalEnabled = renewalEnabled;
    }

    public Integer getRenewalPrice() {
        return renewalPrice;
    }

    public void setRenewalPrice(Integer renewalPrice) {
        this.renewalPrice = renewalPrice;
    }

    public Integer getRenewalDays() {
        return renewalDays;
    }

    public void setRenewalDays(Integer renewalDays) {
        this.renewalDays = renewalDays;
    }

    public String getRenewalDescription() {
        return renewalDescription;
    }

    public void setRenewalDescription(String renewalDescription) {
        this.renewalDescription = renewalDescription;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }
}
