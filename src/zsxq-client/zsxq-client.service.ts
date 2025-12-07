import { Injectable, Logger } from '@nestjs/common';
import { HttpService } from '@nestjs/axios';
import { ConfigService } from '@nestjs/config';
import { AxiosRequestConfig, AxiosError } from 'axios';
import { firstValueFrom } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable()
export class ZsxqClientService {
  private readonly logger = new Logger(ZsxqClientService.name);
  private readonly retryCount: number;
  private readonly retryDelay: number;

  constructor(
    private readonly httpService: HttpService,
    private readonly configService: ConfigService,
  ) {
    this.retryCount = this.configService.get<number>('ZSXQ_API_RETRY_COUNT', 3);
    this.retryDelay = this.configService.get<number>('ZSXQ_API_RETRY_DELAY', 1000);
  }

  /**
   * 执行HTTP请求
   */
  async request<T>(config: AxiosRequestConfig): Promise<T> {
    const { method = 'GET', url, data, headers, params } = config;

    this.logger.debug(`[ZSXQ API] ${method} ${url}`);

    try {
      const response = await firstValueFrom(
        this.httpService.request<T>({
          method,
          url,
          data,
          headers,
          params,
        }).pipe(
          catchError((error: AxiosError) => {
            this.handleError(error);
            throw error;
          }),
        ),
      );

      return response.data;
    } catch (error) {
      this.logger.error(`[ZSXQ API] 请求失败: ${method} ${url}`, error);
      throw error;
    }
  }

  /**
   * GET请求
   */
  async get<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return this.request<T>({ ...config, method: 'GET', url });
  }

  /**
   * POST请求
   */
  async post<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return this.request<T>({ ...config, method: 'POST', url, data });
  }

  /**
   * PUT请求
   */
  async put<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return this.request<T>({ ...config, method: 'PUT', url, data });
  }

  /**
   * DELETE请求
   */
  async delete<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return this.request<T>({ ...config, method: 'DELETE', url });
  }

  /**
   * 错误处理
   */
  private handleError(error: AxiosError) {
    if (error.response) {
      this.logger.error(
        `[ZSXQ API] 响应错误: ${error.response.status} - ${JSON.stringify(error.response.data)}`,
      );
    } else if (error.request) {
      this.logger.error('[ZSXQ API] 请求错误: 未收到响应');
    } else {
      this.logger.error(`[ZSXQ API] 错误: ${error.message}`);
    }
  }
}
