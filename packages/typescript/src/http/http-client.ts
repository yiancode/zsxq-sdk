import axios, { AxiosInstance, AxiosRequestConfig, AxiosError } from 'axios';
import * as crypto from 'crypto';
import { ZsxqConfig } from '../client/zsxq-config';
import {
  ZsxqException,
  NetworkException,
  TimeoutException,
  createException,
} from '../exception';

/**
 * 知识星球 API 响应格式
 */
export interface ZsxqResponse<T = unknown> {
  succeeded: boolean;
  code?: number;
  error?: string;
  info?: string;
  resp_data: T;
}

/**
 * HTTP 客户端 - 处理请求、签名、重试
 */
export class HttpClient {
  private readonly axios: AxiosInstance;
  private readonly config: ZsxqConfig;

  constructor(config: ZsxqConfig) {
    this.config = config;
    this.axios = axios.create({
      baseURL: config.baseUrl,
      timeout: config.timeout,
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
        'User-Agent': `xiaomiquan/${config.appVersion} SDK/1.0.0`,
        'x-version': config.appVersion,
      },
    });
  }

  /**
   * 生成请求签名
   */
  private generateSignature(
    timestamp: string,
    method: string,
    path: string,
    body?: string,
  ): string {
    // 签名算法（基于抓包分析推测）
    // 实际密钥需要通过逆向工程获取
    const secretKey = 'zsxq-sdk-secret';
    let signData = `${timestamp}\n${method.toUpperCase()}\n${path}`;
    if (body) {
      signData += `\n${body}`;
    }

    return crypto.createHmac('sha1', secretKey).update(signData).digest('hex');
  }

  /**
   * 生成请求 ID
   */
  private generateRequestId(): string {
    return crypto.randomUUID();
  }

  /**
   * 构建请求头
   */
  private buildHeaders(method: string, path: string, body?: string): Record<string, string> {
    const timestamp = Math.floor(Date.now() / 1000).toString();
    const requestId = this.generateRequestId();
    const signature = this.generateSignature(timestamp, method, path, body);

    return {
      authorization: this.config.token,
      'x-timestamp': timestamp,
      'x-signature': signature,
      'x-request-id': requestId,
      'x-aduid': this.config.deviceId,
    };
  }

  /**
   * 处理响应
   */
  private handleResponse<T>(response: ZsxqResponse<T>, requestId: string): T {
    if (response.succeeded) {
      return response.resp_data;
    }

    const code = response.code || 0;
    const message = response.error || response.info || '未知错误';
    throw createException(code, message, requestId);
  }

  /**
   * 处理错误,将axios错误转换为ZsxqException
   */
  private convertError(error: AxiosError, requestId: string): ZsxqException {
    if (error.code === 'ECONNABORTED' || error.message?.includes('timeout')) {
      return new TimeoutException(error.message || '请求超时', error, requestId);
    }

    if (error.response) {
      // 服务器返回错误
      const data = error.response.data as ZsxqResponse;
      if (data && typeof data === 'object' && 'code' in data) {
        return createException(
          data.code || error.response.status,
          data.error || data.info || error.message,
          requestId,
        );
      }
      return new ZsxqException(error.response.status, error.message, requestId);
    }

    // 网络错误
    return new NetworkException(error.message || '网络错误', error, requestId);
  }

  /**
   * 处理错误(用于不需要重试的情况)
   */
  private handleError(error: AxiosError, requestId: string): never {
    throw this.convertError(error, requestId);
  }

  /**
   * 执行请求（带重试）
   */
  private async executeWithRetry<T>(
    requestFn: () => Promise<T>,
    requestId: string,
    retryCount = 0,
  ): Promise<T> {
    try {
      return await requestFn();
    } catch (error) {
      // 将axios错误转换为我们的异常类型
      const exception = error instanceof ZsxqException
        ? error
        : this.convertError(error as AxiosError, requestId);

      const shouldRetry =
        retryCount < this.config.retryCount &&
        (exception instanceof NetworkException || exception instanceof TimeoutException);

      if (shouldRetry) {
        const delay = this.config.retryDelay * Math.pow(2, retryCount);
        await new Promise((resolve) => setTimeout(resolve, delay));
        return this.executeWithRetry(requestFn, requestId, retryCount + 1);
      }

      throw exception;
    }
  }

  /**
   * GET 请求
   */
  async get<T>(path: string, params?: Record<string, unknown>): Promise<T> {
    const requestId = this.generateRequestId();
    const headers = this.buildHeaders('GET', path);

    return this.executeWithRetry(async () => {
      const response = await this.axios.get<ZsxqResponse<T>>(path, {
        params,
        headers,
      });
      return this.handleResponse(response.data, requestId);
    }, requestId);
  }

  /**
   * POST 请求
   */
  async post<T>(path: string, data?: unknown): Promise<T> {
    const requestId = this.generateRequestId();
    const body = data ? JSON.stringify(data) : undefined;
    const headers = this.buildHeaders('POST', path, body);

    return this.executeWithRetry(async () => {
      const response = await this.axios.post<ZsxqResponse<T>>(path, data, {
        headers,
      });
      return this.handleResponse(response.data, requestId);
    }, requestId);
  }

  /**
   * PUT 请求
   */
  async put<T>(path: string, data?: unknown): Promise<T> {
    const requestId = this.generateRequestId();
    const body = data ? JSON.stringify(data) : undefined;
    const headers = this.buildHeaders('PUT', path, body);

    return this.executeWithRetry(async () => {
      const response = await this.axios.put<ZsxqResponse<T>>(path, data, {
        headers,
      });
      return this.handleResponse(response.data, requestId);
    }, requestId);
  }

  /**
   * DELETE 请求
   */
  async delete<T>(path: string): Promise<T> {
    const requestId = this.generateRequestId();
    const headers = this.buildHeaders('DELETE', path);

    return this.executeWithRetry(async () => {
      const response = await this.axios.delete<ZsxqResponse<T>>(path, {
        headers,
      });
      return this.handleResponse(response.data, requestId);
    }, requestId);
  }
}
