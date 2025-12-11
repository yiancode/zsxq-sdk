package com.zsxq.sdk.exception;

/**
 * 异常工厂 - 根据错误码创建对应异常
 */
public final class ExceptionFactory {

    private ExceptionFactory() {
    }

    public static ZsxqException create(int code, String message, String requestId) {
        switch (code) {
            case 10001:
                return new TokenInvalidException(message, requestId);
            case 10002:
                return new TokenExpiredException(message, requestId);
            case 10003:
                return new AuthException(code, message, requestId);
            case 40001:
                return new RateLimitException(message, requestId);
            default:
                return new ZsxqException(code, message, requestId);
        }
    }
}
