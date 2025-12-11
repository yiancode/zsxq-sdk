package com.zsxq.sdk.model;

import java.util.List;

/**
 * 星球角色成员模型（包含星主、合伙人、管理员）
 */
public class RoleMembers {
    private User owner;
    private List<User> partners;
    private List<User> admins;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getPartners() {
        return partners;
    }

    public void setPartners(List<User> partners) {
        this.partners = partners;
    }

    public List<User> getAdmins() {
        return admins;
    }

    public void setAdmins(List<User> admins) {
        this.admins = admins;
    }

    @Override
    public String toString() {
        return "RoleMembers{" +
                "owner=" + owner +
                ", partners=" + partners +
                ", admins=" + admins +
                '}';
    }
}
