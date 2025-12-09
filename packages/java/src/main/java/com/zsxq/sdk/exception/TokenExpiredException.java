package com.zsxq.sdk.exception;

/**
 * Token 过期异常 (10002)
 */
public class TokenExpiredException extends AuthException {

    public TokenExpiredException() {
        this("Token 已过期");
    }

    public TokenExpiredException(String message) {
        super(10002, message);
    }

    public TokenExpiredException(String message, String requestId) {
        super(10002, message, requestId);
    }
}
