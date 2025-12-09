import * as crypto from 'crypto';

/**
 * SDK 配置接口
 */
export interface ZsxqConfig {
  /** 用户认证 Token（必需） */
  token: string;

  /** API 基础 URL */
  baseUrl: string;

  /** 请求超时（毫秒） */
  timeout: number;

  /** 重试次数 */
  retryCount: number;

  /** 重试间隔（毫秒） */
  retryDelay: number;

  /** 设备 ID */
  deviceId: string;

  /** App 版本号 */
  appVersion: string;
}

/**
 * SDK 配置默认值
 */
export const DEFAULT_CONFIG: Omit<ZsxqConfig, 'token'> = {
  baseUrl: 'https://api.zsxq.com',
  timeout: 10000,
  retryCount: 3,
  retryDelay: 1000,
  deviceId: crypto.randomUUID(),
  appVersion: '2.83.0',
};
