package com.zsxq.sdk.model;

import lombok.Data;

/**
 * PK 对战记录模型
 */
@Data
public class PkBattle {
    private Long recordId;
    private PkGroup group;
    private PkGroup opponent;
    private String result;  // "win" | "lose" | "draw"
    private Integer scoreDelta;
    private String battleTime;
}
