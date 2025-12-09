package com.zsxq.sdk.model;

import lombok.Data;
import java.util.List;

/**
 * 星球模型
 */
@Data
public class Group {
    private Long groupId;
    private Long number;
    private String name;
    private String description;
    private String backgroundUrl;
    private String type;  // "free" | "pay"
    private Integer memberCount;
    private User owner;
    private String createTime;
    private String riskLevel;
    private List<Long> partnerIds;
    private List<Long> adminIds;
}
