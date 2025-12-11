package com.zsxq.sdk.model;

import lombok.Data;

/**
 * 发票统计模型
 */
@Data
public class InvoiceStats {
    private Integer totalCount;
    private Integer pendingCount;
    private Integer needIssueCount;
    private Integer needPayCount;
    private Integer reviewingCount;
    private Integer rejectedCount;
    private Integer finishedCount;
}
