package com.zsxq.sdk.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * 用户模型
 *
 * 注意：API 返回的数据中，用户ID可能以 uid 或 user_id 形式出现
 * 使用 @SerializedName 注解来兼容两种情况
 */
@Data
public class User {
    /**
     * 用户 ID（主要字段，兼容 uid 和 user_id）
     */
    @SerializedName(value = "user_id", alternate = {"uid"})
    private String userId;

    /**
     * 用户名
     */
    private String name;

    /**
     * 头像 URL
     */
    private String avatarUrl;

    /**
     * 位置
     */
    private String location;

    /**
     * 个人介绍
     */
    private String introduction;

    /**
     * 唯一 ID
     */
    private String uniqueId;

    /**
     * 用户 SID
     */
    private String userSid;

    /**
     * 等级
     */
    private String grade;

    /**
     * 是否认证
     */
    private Boolean verified;

    /**
     * 获取用户 ID（字符串格式）
     */
    public String getUid() {
        return userId;
    }

    /**
     * 设置用户 ID（字符串格式）
     */
    public void setUid(String uid) {
        this.userId = uid;
    }
}
