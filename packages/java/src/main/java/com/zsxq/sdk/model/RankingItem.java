package com.zsxq.sdk.model;

import lombok.Data;

/**
 * 排行榜项目模型
 */
@Data
public class RankingItem {
    private User user;
    private Integer rank;
    private Integer count;
    private Integer continuousCount;
}
