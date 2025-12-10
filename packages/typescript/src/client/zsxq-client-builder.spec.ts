import { describe, it, expect } from '@jest/globals';
import { ZsxqClientBuilder } from './zsxq-client-builder';
import { ZsxqClient } from './zsxq-client';

describe('ZsxqClientBuilder', () => {
  describe('基本构建', () => {
    it('应该使用最小配置构建客户端', () => {
      const client = new ZsxqClientBuilder().setToken('test-token').build();

      expect(client).toBeInstanceOf(ZsxqClient);
    });

    it('应该在没有 token 时抛出错误', () => {
      const builder = new ZsxqClientBuilder();

      expect(() => builder.build()).toThrow('Token is required');
    });

    it('应该在 token 为空字符串时抛出错误', () => {
      const builder = new ZsxqClientBuilder().setToken('');

      expect(() => builder.build()).toThrow('Token is required');
    });
  });

  describe('配置项设置', () => {
    it('应该设置自定义 baseUrl', () => {
      const client = new ZsxqClientBuilder()
        .setToken('test-token')
        .setBaseUrl('https://custom.api.com')
        .build();

      expect(client).toBeInstanceOf(ZsxqClient);
    });

    it('应该设置超时时间', () => {
      const client = new ZsxqClientBuilder()
        .setToken('test-token')
        .setTimeout(5000)
        .build();

      expect(client).toBeInstanceOf(ZsxqClient);
    });

    it('应该设置重试次数', () => {
      const client = new ZsxqClientBuilder()
        .setToken('test-token')
        .setRetryCount(5)
        .build();

      expect(client).toBeInstanceOf(ZsxqClient);
    });

    it('应该设置重试延迟', () => {
      const client = new ZsxqClientBuilder()
        .setToken('test-token')
        .setRetryDelay(2000)
        .build();

      expect(client).toBeInstanceOf(ZsxqClient);
    });

    it('应该设置应用版本', () => {
      const client = new ZsxqClientBuilder()
        .setToken('test-token')
        .setAppVersion('5.60.0')
        .build();

      expect(client).toBeInstanceOf(ZsxqClient);
    });

    it('应该设置设备 ID', () => {
      const client = new ZsxqClientBuilder()
        .setToken('test-token')
        .setDeviceId('custom-device-id')
        .build();

      expect(client).toBeInstanceOf(ZsxqClient);
    });
  });

  describe('链式调用', () => {
    it('应该支持所有配置的链式调用', () => {
      const client = new ZsxqClientBuilder()
        .setToken('test-token')
        .setBaseUrl('https://custom.api.com')
        .setTimeout(8000)
        .setRetryCount(3)
        .setRetryDelay(1500)
        .setAppVersion('5.60.0')
        .setDeviceId('my-device')
        .build();

      expect(client).toBeInstanceOf(ZsxqClient);
    });

    it('每个 setter 都应该返回 builder 实例', () => {
      const builder = new ZsxqClientBuilder();

      expect(builder.setToken('test')).toBe(builder);
      expect(builder.setBaseUrl('url')).toBe(builder);
      expect(builder.setTimeout(1000)).toBe(builder);
      expect(builder.setRetryCount(2)).toBe(builder);
      expect(builder.setRetryDelay(500)).toBe(builder);
      expect(builder.setAppVersion('1.0')).toBe(builder);
      expect(builder.setDeviceId('device')).toBe(builder);
    });
  });

  describe('默认值', () => {
    it('应该使用默认的 baseUrl', () => {
      const client = new ZsxqClientBuilder().setToken('test-token').build();

      expect(client).toBeInstanceOf(ZsxqClient);
      // 默认值应该在内部正确设置
    });

    it('应该使用默认的超时时间', () => {
      const client = new ZsxqClientBuilder().setToken('test-token').build();

      expect(client).toBeInstanceOf(ZsxqClient);
      // 默认 10000ms
    });

    it('应该使用默认的重试次数', () => {
      const client = new ZsxqClientBuilder().setToken('test-token').build();

      expect(client).toBeInstanceOf(ZsxqClient);
      // 默认 2 次
    });
  });

  describe('参数验证', () => {
    it('应该接受负数超时时间(无验证)', () => {
      const client = new ZsxqClientBuilder().setToken('test-token').setTimeout(-1000).build();

      expect(client).toBeInstanceOf(ZsxqClient);
    });

    it('应该接受零超时时间(无验证)', () => {
      const client = new ZsxqClientBuilder().setToken('test-token').setTimeout(0).build();

      expect(client).toBeInstanceOf(ZsxqClient);
    });

    it('应该接受负数重试次数(无验证)', () => {
      const client = new ZsxqClientBuilder().setToken('test-token').setRetryCount(-1).build();

      expect(client).toBeInstanceOf(ZsxqClient);
    });

    it('应该接受零重试次数', () => {
      const client = new ZsxqClientBuilder().setToken('test-token').setRetryCount(0).build();

      expect(client).toBeInstanceOf(ZsxqClient);
    });

    it('应该接受负数重试延迟(无验证)', () => {
      const client = new ZsxqClientBuilder().setToken('test-token').setRetryDelay(-500).build();

      expect(client).toBeInstanceOf(ZsxqClient);
    });

    it('应该接受零重试延迟', () => {
      const client = new ZsxqClientBuilder().setToken('test-token').setRetryDelay(0).build();

      expect(client).toBeInstanceOf(ZsxqClient);
    });
  });

  describe('边界情况', () => {
    it('应该处理非常大的超时值', () => {
      const client = new ZsxqClientBuilder()
        .setToken('test-token')
        .setTimeout(60000) // 60秒
        .build();

      expect(client).toBeInstanceOf(ZsxqClient);
    });

    it('应该处理非常大的重试次数', () => {
      const client = new ZsxqClientBuilder()
        .setToken('test-token')
        .setRetryCount(10)
        .build();

      expect(client).toBeInstanceOf(ZsxqClient);
    });

    it('应该处理包含特殊字符的 token', () => {
      const client = new ZsxqClientBuilder()
        .setToken('test-token-with-special-chars-!@#$%')
        .build();

      expect(client).toBeInstanceOf(ZsxqClient);
    });
  });
});
