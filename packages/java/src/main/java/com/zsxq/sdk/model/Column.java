package com.zsxq.sdk.model;

/**
 * 星球专栏模型
 */
public class Column {
    private Long columnId;
    private String name;
    private Integer topicsCount;
    private String description;
    private String coverUrl;

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTopicsCount() {
        return topicsCount;
    }

    public void setTopicsCount(Integer topicsCount) {
        this.topicsCount = topicsCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    @Override
    public String toString() {
        return "Column{" +
                "columnId=" + columnId +
                ", name='" + name + '\'' +
                ", topicsCount=" + topicsCount +
                ", description='" + description + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                '}';
    }
}
