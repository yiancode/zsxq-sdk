package com.zsxq.sdk.model;

import lombok.Data;

/**
 * 全局配置模型
 */
@Data
public class GlobalConfig {
    private TopicConfig topic;
    private Long maxVideoSize;

    @Data
    public static class TopicConfig {
        private TalkConfig talk;
        private QuestionConfig question;
        private AnswerConfig answer;
    }

    @Data
    public static class TalkConfig {
        private Integer maxTextLength;
        private Integer maxImageCount;
        private Integer maxFileCount;
    }

    @Data
    public static class QuestionConfig {
        private Integer maxTextLength;
        private Integer maxImageCount;
    }

    @Data
    public static class AnswerConfig {
        private Integer maxVoiceLength;
    }
}
