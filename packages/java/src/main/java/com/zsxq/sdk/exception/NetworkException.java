package com.zsxq.sdk.exception;

/**
 * 网络异常
 */
public class NetworkException extends ZsxqException {

    private final Throwable cause;

    public NetworkException(String message) {
        this(message, null, null);
    }

    public NetworkException(String message, Throwable cause) {
        this(message, cause, null);
    }

    public NetworkException(String message, Throwable cause, String requestId) {
        super(70001, message, requestId);
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
