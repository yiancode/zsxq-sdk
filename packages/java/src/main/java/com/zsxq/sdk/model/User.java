package com.zsxq.sdk.model;

import lombok.Data;

/**
 * 用户模型
 */
@Data
public class User {
    private Long userId;
    private String uid;
    private String name;
    private String avatarUrl;
    private String location;
    private String introduction;
    private String uniqueId;
    private String userSid;
    private String grade;
    private Boolean verified;
}
