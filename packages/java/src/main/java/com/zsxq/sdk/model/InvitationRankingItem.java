package com.zsxq.sdk.model;

import lombok.Data;

/**
 * 邀请排行榜项目
 *
 * App: GET /v2/groups/GROUP_ID/invitations/ranking_list
 */
@Data
public class InvitationRankingItem {
    private User member;
    private Integer rankings;
    private Integer inviteesCount;
}
