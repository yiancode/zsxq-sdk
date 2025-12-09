package com.zsxq.sdk.model;

import lombok.Data;
import java.util.List;

/**
 * 话题内容 - 分享
 */
@Data
public class TalkContent {
    private User owner;
    private String text;
    private List<Image> images;
}
