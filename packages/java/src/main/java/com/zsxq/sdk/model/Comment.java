package com.zsxq.sdk.model;

import lombok.Data;

/**
 * 评论模型
 */
@Data
public class Comment {
    private Long commentId;
    private String text;
    private User owner;
    private String createTime;
    private Integer likesCount;
    private Boolean sticky;
    private Comment replyToComment;
    private User mentionedUser;
}
