package com.zsxq.sdk.model;

/**
 * 星球菜单配置模型
 */
public class Menu {
    private Long menuId;
    private String name;
    private String type;

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "menuId=" + menuId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
