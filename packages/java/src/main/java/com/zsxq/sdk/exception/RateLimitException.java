package com.zsxq.sdk.exception;

/**
 * 频率限制异常
 */
public class RateLimitException extends ZsxqException {

    public RateLimitException(String message, String requestId) {
        super(40001, message, requestId);
    }
}
