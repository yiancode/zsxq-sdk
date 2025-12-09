# ZsxqClientService 增强方案

> 对当前 zsxq-client 模块的增强建议

## 当前状态分析

### 现有实现

```typescript
// src/zsxq-client/zsxq-client.service.ts
@Injectable()
export class ZsxqClientService {
  // 基础HTTP封装
  async request<T>(config: AxiosRequestConfig): Promise<T>
  async get<T>(url: string, config?: AxiosRequestConfig): Promise<T>
  async post<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T>
  // ...
}
```

### 存在的问题

1. **缺少请求头伪装** - 可能被知识星球服务器识别并拒绝
2. **缺少业务状态码检查** - 只检查HTTP状态码，未检查 `succeeded` 字段
3. **异常类型单一** - 无法区分Token失效、限流等不同错误
4. **缺少重试机制** - 配置了但未实现实际重试逻辑

## 增强方案

### 1. 自定义异常类

```typescript
// src/zsxq-client/exceptions/zsxq.exception.ts

/**
 * 知识星球API基础异常
 */
export class ZsxqApiException extends Error {
  constructor(
    message: string,
    public readonly statusCode?: number,
    public readonly errorCode?: string,
    public readonly responseData?: any,
  ) {
    super(message);
    this.name = 'ZsxqApiException';
  }
}

/**
 * Token失效异常
 */
export class ZsxqTokenExpiredException extends ZsxqApiException {
  constructor(message = 'Token已失效，请重新获取') {
    super(message, 401, 'TOKEN_EXPIRED');
    this.name = 'ZsxqTokenExpiredException';
  }
}

/**
 * 请求频率限制异常
 */
export class ZsxqRateLimitException extends ZsxqApiException {
  constructor(message = '请求过于频繁，请稍后再试') {
    super(message, 429, 'RATE_LIMIT');
    this.name = 'ZsxqRateLimitException';
  }
}

/**
 * 资源不存在异常
 */
export class ZsxqNotFoundException extends ZsxqApiException {
  constructor(message = '请求的资源不存在') {
    super(message, 404, 'NOT_FOUND');
    this.name = 'ZsxqNotFoundException';
  }
}

/**
 * 服务器错误异常
 */
export class ZsxqServerException extends ZsxqApiException {
  constructor(message = '知识星球服务器错误') {
    super(message, 500, 'SERVER_ERROR');
    this.name = 'ZsxqServerException';
  }
}

/**
 * 业务逻辑错误异常
 */
export class ZsxqBusinessException extends ZsxqApiException {
  constructor(message: string, errorCode?: string, responseData?: any) {
    super(message, 200, errorCode, responseData);
    this.name = 'ZsxqBusinessException';
  }
}
```

### 2. 增强的客户端服务

```typescript
// src/zsxq-client/zsxq-client.service.ts
import { Injectable, Logger, OnModuleInit } from '@nestjs/common';
import { HttpService } from '@nestjs/axios';
import { ConfigService } from '@nestjs/config';
import { AxiosRequestConfig, AxiosError, AxiosResponse } from 'axios';
import { firstValueFrom } from 'rxjs';
import { v4 as uuidv4 } from 'uuid';
import {
  ZsxqApiException,
  ZsxqTokenExpiredException,
  ZsxqRateLimitException,
  ZsxqServerException,
  ZsxqBusinessException,
} from './exceptions/zsxq.exception';

@Injectable()
export class ZsxqClientService implements OnModuleInit {
  private readonly logger = new Logger(ZsxqClientService.name);

  private baseUrl: string;
  private token: string;
  private groupId: string;
  private timeout: number;
  private retryCount: number;
  private retryDelay: number;

  constructor(
    private readonly httpService: HttpService,
    private readonly configService: ConfigService,
  ) {}

  onModuleInit() {
    this.baseUrl = this.configService.get('ZSXQ_API_BASE_URL', 'https://api.zsxq.com');
    this.token = this.configService.get('ZSXQ_TOKEN');
    this.groupId = this.configService.get('ZSXQ_GROUP_ID');
    this.timeout = this.configService.get('ZSXQ_API_TIMEOUT', 30000);
    this.retryCount = this.configService.get('ZSXQ_API_RETRY_COUNT', 3);
    this.retryDelay = this.configService.get('ZSXQ_API_RETRY_DELAY', 1000);

    // 验证必需配置
    if (!this.token) {
      throw new Error('缺少必需的知识星球配置: ZSXQ_TOKEN');
    }
    if (!this.groupId) {
      throw new Error('缺少必需的知识星球配置: ZSXQ_GROUP_ID');
    }
  }

  /**
   * 获取星球ID
   */
  getGroupId(): string {
    return this.groupId;
  }

  /**
   * 构建请求头（关键：伪装为知识星球APP客户端）
   */
  private buildHeaders(customHeaders?: Record<string, string>): Record<string, string> {
    return {
      'Authorization': this.token,
      'User-Agent': 'xiaomiquan/5.28.1 (iPhone; iOS 14.7.1; Scale/3.00)',
      'Accept': 'application/json, text/plain, */*',
      'Content-Type': 'application/json',
      'x-request-id': uuidv4(),
      ...customHeaders,
    };
  }

  /**
   * 执行HTTP请求（带重试机制）
   */
  async request<T>(config: AxiosRequestConfig): Promise<T> {
    const { method = 'GET', url, data, headers: customHeaders, params } = config;
    const fullUrl = url.startsWith('http') ? url : `${this.baseUrl}${url}`;

    let lastError: Error;

    for (let attempt = 1; attempt <= this.retryCount; attempt++) {
      try {
        this.logger.debug(`[ZSXQ API] ${method} ${fullUrl} (尝试 ${attempt}/${this.retryCount})`);

        const response = await firstValueFrom(
          this.httpService.request<any>({
            method,
            url: fullUrl,
            data,
            params,
            headers: this.buildHeaders(customHeaders as Record<string, string>),
            timeout: this.timeout,
            // 注意：生产环境应启用SSL验证
            // httpsAgent: new https.Agent({ rejectUnauthorized: false }),
          }),
        );

        return this.handleResponse<T>(response);
      } catch (error) {
        lastError = error;

        // 不重试的错误类型
        if (
          error instanceof ZsxqTokenExpiredException ||
          error instanceof ZsxqRateLimitException
        ) {
          throw error;
        }

        // 记录错误日志
        this.logger.warn(
          `[ZSXQ API] 请求失败 (尝试 ${attempt}/${this.retryCount}): ${error.message}`,
        );

        // 最后一次尝试，不再等待
        if (attempt < this.retryCount) {
          await this.delay(this.retryDelay * attempt); // 递增延迟
        }
      }
    }

    // 所有重试都失败
    this.logger.error(`[ZSXQ API] 请求最终失败: ${method} ${fullUrl}`);
    throw lastError;
  }

  /**
   * 处理响应（核心：检查业务状态码）
   */
  private handleResponse<T>(response: AxiosResponse<any>): T {
    const { status, data } = response;

    // HTTP状态码检查
    if (status === 401) {
      throw new ZsxqTokenExpiredException();
    }
    if (status === 429) {
      throw new ZsxqRateLimitException();
    }
    if (status >= 500) {
      throw new ZsxqServerException(`服务器错误: ${status}`);
    }

    // 业务状态码检查（关键！）
    if (!data.succeeded) {
      const errorInfo = data.error || {};
      const errorMessage = typeof errorInfo === 'object'
        ? errorInfo.message || '未知错误'
        : String(errorInfo);

      throw new ZsxqBusinessException(
        `API调用失败: ${errorMessage}`,
        errorInfo.code,
        data,
      );
    }

    // 返回实际数据
    return data.resp_data as T;
  }

  /**
   * 处理Axios错误
   */
  private handleAxiosError(error: AxiosError): never {
    if (error.response) {
      const { status, data } = error.response;

      if (status === 401) {
        throw new ZsxqTokenExpiredException();
      }
      if (status === 429) {
        throw new ZsxqRateLimitException();
      }
      if (status >= 500) {
        throw new ZsxqServerException(`服务器错误: ${status}`);
      }

      throw new ZsxqApiException(
        `HTTP错误: ${status}`,
        status,
        undefined,
        data,
      );
    }

    if (error.request) {
      throw new ZsxqApiException('网络请求失败：未收到响应');
    }

    throw new ZsxqApiException(`请求配置错误: ${error.message}`);
  }

  /**
   * 延迟工具方法
   */
  private delay(ms: number): Promise<void> {
    return new Promise((resolve) => setTimeout(resolve, ms));
  }

  // ==================== 便捷方法 ====================

  async get<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return this.request<T>({ ...config, method: 'GET', url });
  }

  async post<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return this.request<T>({ ...config, method: 'POST', url, data });
  }

  async put<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return this.request<T>({ ...config, method: 'PUT', url, data });
  }

  async delete<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return this.request<T>({ ...config, method: 'DELETE', url });
  }
}
```

### 3. API端点封装

```typescript
// src/zsxq-client/endpoints/checkin.endpoints.ts
import { Injectable } from '@nestjs/common';
import { ZsxqClientService } from '../zsxq-client.service';

/**
 * 知识星球原生API响应类型
 */
interface ZsxqCheckinProject {
  checkin_id: number;
  name: string;
  description: string;
  start_time: string;
  end_time: string;
  background?: { image_url: string };
  users_count: number;
  checkined_count: number;
  current_continuous_days?: number;
  rules?: string;
  create_time: string;
}

interface ZsxqRankingItem {
  rankings: number;
  user: {
    user_id: number;
    name: string;
    alias?: string;
    avatar_url: string;
  };
  checkined_days: number;
}

interface ZsxqRankingResponse {
  ranking_list: ZsxqRankingItem[];
  user_specific?: {
    rankings: number;
    checkined_days: number;
  };
}

@Injectable()
export class CheckinEndpoints {
  constructor(private zsxqClient: ZsxqClientService) {}

  /**
   * 获取打卡项目列表
   * @param scope ongoing | closed | over
   */
  async getProjects(scope: 'ongoing' | 'closed' | 'over' = 'ongoing') {
    const groupId = this.zsxqClient.getGroupId();
    const response = await this.zsxqClient.get<{ checkins: ZsxqCheckinProject[] }>(
      `/v2/groups/${groupId}/checkins`,
      { params: { scope, count: 100 } },
    );
    return response.checkins || [];
  }

  /**
   * 获取项目详情
   */
  async getProjectDetail(checkinId: string) {
    const groupId = this.zsxqClient.getGroupId();
    return this.zsxqClient.get<ZsxqCheckinProject>(
      `/v2/groups/${groupId}/checkins/${checkinId}`,
    );
  }

  /**
   * 获取项目统计
   */
  async getProjectStats(checkinId: string) {
    const groupId = this.zsxqClient.getGroupId();
    return this.zsxqClient.get<{
      users_count: number;
      checkined_count: number;
      today_count: number;
      continuous_rate: number;
    }>(`/v2/groups/${groupId}/checkins/${checkinId}/statistics`);
  }

  /**
   * 获取每日统计
   */
  async getDailyStats(checkinId: string, date?: string) {
    const groupId = this.zsxqClient.getGroupId();

    // 构建日期参数
    const dateParam = date || new Date().toISOString();
    const encodedDate = encodeURIComponent(dateParam);

    return this.zsxqClient.get<{
      date: string;
      count: number;
      new_users: number;
      active_users: number;
    }>(`/v2/groups/${groupId}/checkins/${checkinId}/statistics/daily`, {
      params: { date: encodedDate },
    });
  }

  /**
   * 获取排行榜
   */
  async getRankingList(
    checkinId: string,
    type: 'continuous' | 'accumulated' = 'continuous',
    index = 0,
  ) {
    const groupId = this.zsxqClient.getGroupId();
    return this.zsxqClient.get<ZsxqRankingResponse>(
      `/v2/groups/${groupId}/checkins/${checkinId}/ranking_list`,
      { params: { type, index } },
    );
  }

  /**
   * 获取打卡话题列表
   */
  async getTopics(checkinId: string, count = 20) {
    const groupId = this.zsxqClient.getGroupId();
    return this.zsxqClient.get<{ topics: any[] }>(
      `/v2/groups/${groupId}/checkins/${checkinId}/topics`,
      { params: { count } },
    );
  }
}
```

### 4. 全局异常过滤器集成

```typescript
// src/common/filters/zsxq-exception.filter.ts
import {
  ExceptionFilter,
  Catch,
  ArgumentsHost,
  HttpStatus,
  Logger,
} from '@nestjs/common';
import { Response } from 'express';
import {
  ZsxqApiException,
  ZsxqTokenExpiredException,
  ZsxqRateLimitException,
} from '@/zsxq-client/exceptions/zsxq.exception';

@Catch(ZsxqApiException)
export class ZsxqExceptionFilter implements ExceptionFilter {
  private readonly logger = new Logger(ZsxqExceptionFilter.name);

  catch(exception: ZsxqApiException, host: ArgumentsHost) {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse<Response>();
    const request = ctx.getRequest();

    let httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    let errorCode = 'ZSXQ_API_ERROR';
    let message = exception.message;

    // 根据异常类型设置响应
    if (exception instanceof ZsxqTokenExpiredException) {
      httpStatus = HttpStatus.UNAUTHORIZED;
      errorCode = 'ZSXQ_TOKEN_EXPIRED';
      message = 'Token已失效，请联系管理员更新';
    } else if (exception instanceof ZsxqRateLimitException) {
      httpStatus = HttpStatus.TOO_MANY_REQUESTS;
      errorCode = 'ZSXQ_RATE_LIMIT';
      message = '请求过于频繁，请稍后再试';
    }

    // 记录日志
    this.logger.error(
      `[${errorCode}] ${message}`,
      exception.stack,
    );

    response.status(httpStatus).json({
      success: false,
      error: {
        code: errorCode,
        message: [message],
      },
      timestamp: new Date().toISOString(),
      path: request.url,
    });
  }
}
```

### 5. 模块配置

```typescript
// src/zsxq-client/zsxq-client.module.ts
import { Module } from '@nestjs/common';
import { HttpModule } from '@nestjs/axios';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { ZsxqClientService } from './zsxq-client.service';
import { CheckinEndpoints } from './endpoints/checkin.endpoints';
import { PlanetEndpoints } from './endpoints/planet.endpoints';
import { TopicEndpoints } from './endpoints/topic.endpoints';

@Module({
  imports: [
    HttpModule.registerAsync({
      imports: [ConfigModule],
      inject: [ConfigService],
      useFactory: (configService: ConfigService) => ({
        timeout: configService.get('ZSXQ_API_TIMEOUT', 30000),
        maxRedirects: 5,
      }),
    }),
  ],
  providers: [
    ZsxqClientService,
    CheckinEndpoints,
    PlanetEndpoints,
    TopicEndpoints,
  ],
  exports: [
    ZsxqClientService,
    CheckinEndpoints,
    PlanetEndpoints,
    TopicEndpoints,
  ],
})
export class ZsxqClientModule {}
```

## 环境变量配置

```bash
# .env
# 知识星球API配置
ZSXQ_API_BASE_URL=https://api.zsxq.com
ZSXQ_TOKEN=your_token_from_charles
ZSXQ_GROUP_ID=your_group_id
ZSXQ_API_TIMEOUT=30000
ZSXQ_API_RETRY_COUNT=3
ZSXQ_API_RETRY_DELAY=1000
```

## 使用示例

### 在Service中使用

```typescript
// src/modules/checkin/checkin.service.ts
@Injectable()
export class CheckinService {
  constructor(
    private checkinEndpoints: CheckinEndpoints,
    private cacheService: CacheService,
  ) {}

  async getLeaderboard(checkinId: string, type: 'continuous' | 'accumulated', limit: number) {
    const cacheKey = CacheKeys.checkinLeaderboard(checkinId, type);

    // 尝试从缓存获取
    const cached = await this.cacheService.get(cacheKey);
    if (cached) {
      return this.formatForLimit(cached, limit);
    }

    // 调用知识星球API
    const rawData = await this.checkinEndpoints.getRankingList(checkinId, type);

    // 格式化并缓存
    const formatted = this.formatLeaderboard(rawData);
    await this.cacheService.set(cacheKey, formatted, CacheTTL.CHECKIN_LEADERBOARD);

    return this.formatForLimit(formatted, limit);
  }
}
```

### 在Controller中处理异常

```typescript
// src/modules/checkin/checkin.controller.ts
@Controller('checkins')
@UseFilters(ZsxqExceptionFilter) // 使用自定义异常过滤器
export class CheckinController {
  constructor(private checkinService: CheckinService) {}

  @Get(':id/leaderboard')
  async getLeaderboard(
    @Param('id') id: string,
    @Query('type') type: 'continuous' | 'accumulated' = 'continuous',
    @Query('limit', new DefaultValuePipe(10), ParseIntPipe) limit: number,
  ) {
    return this.checkinService.getLeaderboard(id, type, limit);
  }
}
```

## 测试建议

```typescript
// src/zsxq-client/__tests__/zsxq-client.service.spec.ts
describe('ZsxqClientService', () => {
  it('should build correct headers', () => {
    const headers = service['buildHeaders']();
    expect(headers['User-Agent']).toContain('xiaomiquan');
    expect(headers['x-request-id']).toBeDefined();
  });

  it('should throw ZsxqTokenExpiredException on 401', async () => {
    // Mock 401 response
    await expect(service.get('/test')).rejects.toThrow(ZsxqTokenExpiredException);
  });

  it('should throw ZsxqBusinessException on succeeded: false', async () => {
    // Mock { succeeded: false, error: { message: 'xxx' } }
    await expect(service.get('/test')).rejects.toThrow(ZsxqBusinessException);
  });

  it('should retry on network error', async () => {
    // Mock network error then success
    const result = await service.get('/test');
    expect(httpService.request).toHaveBeenCalledTimes(2);
  });
});
```

## 迁移步骤

1. **创建异常类** - `src/zsxq-client/exceptions/`
2. **更新ZsxqClientService** - 添加请求头伪装和业务状态码检查
3. **创建端点封装类** - `src/zsxq-client/endpoints/`
4. **添加异常过滤器** - `src/common/filters/zsxq-exception.filter.ts`
5. **更新环境变量** - 添加ZSXQ_TOKEN和ZSXQ_GROUP_ID
6. **编写单元测试** - 覆盖关键路径
7. **集成测试验证** - 使用真实Token测试
