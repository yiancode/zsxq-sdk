import { ZsxqConfig, DEFAULT_CONFIG } from './zsxq-config';
import { ZsxqClient } from './zsxq-client';

/**
 * ZsxqClient 构建器
 *
 * 使用示例：
 * ```typescript
 * const client = new ZsxqClientBuilder()
 *   .setToken('your-token')
 *   .setTimeout(10000)
 *   .setRetry(3)
 *   .build();
 * ```
 */
export class ZsxqClientBuilder {
  private config: Partial<ZsxqConfig> = {};

  /**
   * 设置认证 Token（必需）
   */
  setToken(token: string): this {
    this.config.token = token;
    return this;
  }

  /**
   * 设置 API 基础 URL
   * @default 'https://api.zsxq.com'
   */
  setBaseUrl(baseUrl: string): this {
    this.config.baseUrl = baseUrl;
    return this;
  }

  /**
   * 设置请求超时（毫秒）
   * @default 10000
   */
  setTimeout(timeout: number): this {
    this.config.timeout = timeout;
    return this;
  }

  /**
   * 设置重试次数
   * @default 3
   */
  setRetryCount(count: number): this {
    this.config.retryCount = count;
    return this;
  }

  /**
   * 设置重试间隔（毫秒）
   * @default 1000
   */
  setRetryDelay(delay: number): this {
    this.config.retryDelay = delay;
    return this;
  }

  /**
   * 设置重试配置
   */
  setRetry(count: number, delay?: number): this {
    this.config.retryCount = count;
    if (delay !== undefined) {
      this.config.retryDelay = delay;
    }
    return this;
  }

  /**
   * 设置设备 ID
   */
  setDeviceId(deviceId: string): this {
    this.config.deviceId = deviceId;
    return this;
  }

  /**
   * 设置 App 版本号
   * @default '2.83.0'
   */
  setAppVersion(version: string): this {
    this.config.appVersion = version;
    return this;
  }

  /**
   * 构建 ZsxqClient 实例
   * @throws {Error} 如果 Token 未设置
   */
  build(): ZsxqClient {
    if (!this.config.token) {
      throw new Error('Token is required. Use setToken() to set it.');
    }

    const finalConfig: ZsxqConfig = {
      token: this.config.token,
      baseUrl: this.config.baseUrl ?? DEFAULT_CONFIG.baseUrl,
      timeout: this.config.timeout ?? DEFAULT_CONFIG.timeout,
      retryCount: this.config.retryCount ?? DEFAULT_CONFIG.retryCount,
      retryDelay: this.config.retryDelay ?? DEFAULT_CONFIG.retryDelay,
      deviceId: this.config.deviceId ?? DEFAULT_CONFIG.deviceId,
      appVersion: this.config.appVersion ?? DEFAULT_CONFIG.appVersion,
    };

    return new ZsxqClient(finalConfig);
  }
}
