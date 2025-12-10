package com.zsxq.sdk.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionFactoryTest {

    @Test
    void testCreateTokenInvalidException() {
        ZsxqException ex = ExceptionFactory.create(10001, "Token 无效", "req-1");

        assertInstanceOf(TokenInvalidException.class, ex);
        assertEquals(10001, ex.getCode());
        assertEquals("Token 无效", ex.getMessage());
        assertEquals("req-1", ex.getRequestId());
    }

    @Test
    void testCreateTokenExpiredException() {
        ZsxqException ex = ExceptionFactory.create(10002, "Token 已过期", "req-2");

        assertInstanceOf(TokenExpiredException.class, ex);
        assertEquals(10002, ex.getCode());
    }

    @Test
    void testCreateAuthException() {
        ZsxqException ex = ExceptionFactory.create(10003, "认证失败", "req-3");

        assertInstanceOf(AuthException.class, ex);
        assertEquals(10003, ex.getCode());
    }

    @Test
    void testCreateGenericZsxqException() {
        ZsxqException ex = ExceptionFactory.create(99999, "未知错误", "req-unknown");

        assertEquals(ZsxqException.class, ex.getClass());
        assertEquals(99999, ex.getCode());
        assertEquals("未知错误", ex.getMessage());
    }

    @Test
    void testCreateWithoutRequestId() {
        ZsxqException ex = ExceptionFactory.create(10001, "Token 无效", null);

        assertInstanceOf(TokenInvalidException.class, ex);
        assertNull(ex.getRequestId());
    }

    @Test
    void testExceptionHierarchy() {
        ZsxqException ex = ExceptionFactory.create(10001, "Token 无效", "req-1");

        assertTrue(ex instanceof Exception);
        assertTrue(ex instanceof RuntimeException);
        assertTrue(ex instanceof ZsxqException);
        assertTrue(ex instanceof AuthException);
        assertTrue(ex instanceof TokenInvalidException);
    }

    @Test
    void testNetworkException() {
        Exception cause = new Exception("Connection failed");
        NetworkException ex = new NetworkException("网络错误", cause, "req-net");

        assertEquals(70001, ex.getCode());
        assertEquals("网络错误", ex.getMessage());
        assertEquals(cause, ex.getCause());
        assertEquals("req-net", ex.getRequestId());
    }

    @Test
    void testNetworkExceptionWithoutCause() {
        NetworkException ex = new NetworkException("网络错误", null, "req-net");

        assertEquals(70001, ex.getCode());
        assertNull(ex.getCause());
    }

    @Test
    void testExceptionMessageAndCode() {
        ZsxqException ex = new ZsxqException(12345, "测试异常", "req-test");

        assertEquals(12345, ex.getCode());
        assertEquals("测试异常", ex.getMessage());
        assertEquals("req-test", ex.getRequestId());
    }
}
