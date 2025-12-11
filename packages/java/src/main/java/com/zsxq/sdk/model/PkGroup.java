package com.zsxq.sdk.model;

import lombok.Data;

/**
 * PK 群组模型
 */
@Data
public class PkGroup {
    private Long groupId;
    private String name;
    private String backgroundUrl;
    private Integer power;
    private Integer defensiveSuccessCount;
    private Integer previousRankingPower;
    private UserSpecific userSpecific;

    @Data
    public static class UserSpecific {
        private Boolean isPrivilegedMember;
    }
}
