package com.zsxq.sdk.exception;

/**
 * 认证异常基类
 */
public class AuthException extends ZsxqException {

    public AuthException(int code, String message) {
        super(code, message);
    }

    public AuthException(int code, String message, String requestId) {
        super(code, message, requestId);
    }
}
