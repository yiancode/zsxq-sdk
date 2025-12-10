package com.zsxq.sdk.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZsxqClientTest {

    private ZsxqClient client;

    @BeforeEach
    void setUp() {
        client = new ZsxqClientBuilder()
            .token("test-token")
            .build();
    }

    @Test
    void testModulesInitialization() {
        assertNotNull(client.groups());
        assertNotNull(client.topics());
        assertNotNull(client.users());
        assertNotNull(client.checkins());
        assertNotNull(client.dashboard());
    }

    @Test
    void testGroupsModuleMethods() {
        assertNotNull(client.groups());
        assertDoesNotThrow(() -> {
            // 验证方法存在
            client.groups().getClass().getMethod("list");
            client.groups().getClass().getMethod("get", long.class);
            client.groups().getClass().getMethod("get", String.class);
            client.groups().getClass().getMethod("getStatistics", long.class);
            client.groups().getClass().getMethod("getMember", long.class, long.class);
        });
    }

    @Test
    void testTopicsModuleMethods() {
        assertNotNull(client.topics());
        // Topics 模块应该有对应的方法
    }

    @Test
    void testUsersModuleMethods() {
        assertNotNull(client.users());
        // Users 模块应该有对应的方法
    }

    @Test
    void testCheckinsModuleMethods() {
        assertNotNull(client.checkins());
        // Checkins 模块应该有对应的方法
    }

    @Test
    void testDashboardModuleMethods() {
        assertNotNull(client.dashboard());
        // Dashboard 模块应该有对应的方法
    }

    @Test
    void testModulesAreSingletons() {
        // 每次调用应该返回同一个实例
        assertSame(client.groups(), client.groups());
        assertSame(client.topics(), client.topics());
        assertSame(client.users(), client.users());
        assertSame(client.checkins(), client.checkins());
        assertSame(client.dashboard(), client.dashboard());
    }

    @Test
    void testMultipleClientsAreIndependent() {
        ZsxqClient client1 = new ZsxqClientBuilder()
            .token("token-1")
            .build();

        ZsxqClient client2 = new ZsxqClientBuilder()
            .token("token-2")
            .build();

        assertNotSame(client1, client2);
        assertNotSame(client1.groups(), client2.groups());
    }
}
