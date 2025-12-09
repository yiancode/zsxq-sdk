package com.zsxq.sdk.exception;

/**
 * 知识星球 SDK 基础异常类
 */
public class ZsxqException extends RuntimeException {

    private final int code;
    private final String requestId;

    public ZsxqException(int code, String message) {
        this(code, message, null);
    }

    public ZsxqException(int code, String message, String requestId) {
        super(message);
        this.code = code;
        this.requestId = requestId;
    }

    public int getCode() {
        return code;
    }

    public String getRequestId() {
        return requestId;
    }
}
