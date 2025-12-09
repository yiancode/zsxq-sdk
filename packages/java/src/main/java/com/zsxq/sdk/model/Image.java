package com.zsxq.sdk.model;

import lombok.Data;

/**
 * 图片模型
 */
@Data
public class Image {
    private Long imageId;
    private String type;
    private ImageSize original;
    private ImageSize thumbnail;
    private ImageSize large;

    @Data
    public static class ImageSize {
        private String url;
        private Integer width;
        private Integer height;
        private Long size;
    }
}
