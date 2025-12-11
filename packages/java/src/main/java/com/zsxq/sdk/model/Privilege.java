package com.zsxq.sdk.model;

import lombok.Data;

/**
 * 权限配置模型
 */
@Data
public class Privilege {
    private Long privilegeId;
    private String name;
    private String description;
    private Boolean enabled;
}
