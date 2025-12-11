package com.zsxq.sdk.model;

import lombok.Data;

/**
 * 评论模型
 */
@Data
public class Comment {
    /**
     * 评论 ID
     */
    private Long commentId;

    /**
     * 评论者
     */
    private User owner;

    /**
     * 评论内容
     */
    private String text;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 点赞数
     */
    private Integer likesCount;

    /**
     * 被回复者
     */
    private User repliee;

    /**
     * 是否置顶
     */
    private Boolean sticky;
}
