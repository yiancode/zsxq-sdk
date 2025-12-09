package com.zsxq.sdk.exception;

/**
 * Token 无效异常 (10001)
 */
public class TokenInvalidException extends AuthException {

    public TokenInvalidException() {
        this("Token 无效");
    }

    public TokenInvalidException(String message) {
        super(10001, message);
    }

    public TokenInvalidException(String message, String requestId) {
        super(10001, message, requestId);
    }
}
