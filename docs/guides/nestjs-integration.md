# NestJS 集成

> 在 NestJS 项目中使用 zsxq-sdk

## 概述

zsxq-sdk 提供了 NestJS 模块，支持：
- 依赖注入
- 配置管理
- 多实例支持
- 请求作用域客户端

---

## 安装

```bash
npm install zsxq-sdk @nestjs/common @nestjs/config
```

---

## 基础配置

### 1. 导入模块

```typescript
// app.module.ts
import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { ZsxqModule } from 'zsxq-sdk/nestjs';

@Module({
  imports: [
    ConfigModule.forRoot(),
    ZsxqModule.forRoot({
      token: process.env.ZSXQ_TOKEN!,
      timeout: 10000,
      retry: 3,
    }),
  ],
})
export class AppModule {}
```

### 2. 注入客户端

```typescript
// planet.service.ts
import { Injectable } from '@nestjs/common';
import { ZsxqClient } from 'zsxq-sdk';
import { InjectZsxqClient } from 'zsxq-sdk/nestjs';

@Injectable()
export class PlanetService {
  constructor(
    @InjectZsxqClient()
    private readonly zsxq: ZsxqClient,
  ) {}

  async getMyPlanets() {
    return this.zsxq.groups.list();
  }

  async getPlanetTopics(groupId: string) {
    return this.zsxq.topics.list(groupId, { count: 20 });
  }
}
```

---

## 异步配置

### 使用 ConfigService

```typescript
// app.module.ts
import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { ZsxqModule } from 'zsxq-sdk/nestjs';

@Module({
  imports: [
    ConfigModule.forRoot(),
    ZsxqModule.forRootAsync({
      imports: [ConfigModule],
      useFactory: (configService: ConfigService) => ({
        token: configService.get('ZSXQ_TOKEN')!,
        timeout: configService.get('ZSXQ_TIMEOUT', 10000),
        retry: configService.get('ZSXQ_RETRY', 3),
        baseUrl: configService.get('ZSXQ_BASE_URL'),
      }),
      inject: [ConfigService],
    }),
  ],
})
export class AppModule {}
```

### 使用自定义配置类

```typescript
// zsxq.config.ts
import { Injectable } from '@nestjs/common';
import { ZsxqOptionsFactory, ZsxqModuleOptions } from 'zsxq-sdk/nestjs';

@Injectable()
export class ZsxqConfigService implements ZsxqOptionsFactory {
  createZsxqOptions(): ZsxqModuleOptions {
    return {
      token: process.env.ZSXQ_TOKEN!,
      timeout: 10000,
      retry: 3,
    };
  }
}

// app.module.ts
@Module({
  imports: [
    ZsxqModule.forRootAsync({
      useClass: ZsxqConfigService,
    }),
  ],
})
export class AppModule {}
```

---

## 多实例配置

### 注册多个客户端

```typescript
// app.module.ts
import { Module } from '@nestjs/common';
import { ZsxqModule } from 'zsxq-sdk/nestjs';

@Module({
  imports: [
    // 默认客户端
    ZsxqModule.forRoot({
      token: process.env.ZSXQ_TOKEN_DEFAULT!,
    }),
    // 命名客户端
    ZsxqModule.forRoot({
      name: 'admin',
      token: process.env.ZSXQ_TOKEN_ADMIN!,
    }),
    ZsxqModule.forRoot({
      name: 'bot',
      token: process.env.ZSXQ_TOKEN_BOT!,
    }),
  ],
})
export class AppModule {}
```

### 使用命名客户端

```typescript
// admin.service.ts
import { Injectable } from '@nestjs/common';
import { ZsxqClient } from 'zsxq-sdk';
import { InjectZsxqClient } from 'zsxq-sdk/nestjs';

@Injectable()
export class AdminService {
  constructor(
    @InjectZsxqClient('admin')
    private readonly adminClient: ZsxqClient,

    @InjectZsxqClient('bot')
    private readonly botClient: ZsxqClient,
  ) {}

  async getAdminGroups() {
    return this.adminClient.groups.list();
  }

  async getBotGroups() {
    return this.botClient.groups.list();
  }
}
```

---

## 请求作用域

### 动态 Token（每请求不同）

```typescript
// zsxq.module.ts
import { Module, Scope } from '@nestjs/common';
import { REQUEST } from '@nestjs/core';
import { ZsxqModule, ZsxqClient, ZsxqClientBuilder } from 'zsxq-sdk';

@Module({
  providers: [
    {
      provide: ZsxqClient,
      scope: Scope.REQUEST,
      useFactory: (request: Request) => {
        // 从请求中获取 Token（例如从 header 或 session）
        const token = request.headers['x-zsxq-token'] as string;
        return new ZsxqClientBuilder()
          .setToken(token)
          .build();
      },
      inject: [REQUEST],
    },
  ],
  exports: [ZsxqClient],
})
export class ZsxqRequestScopeModule {}
```

---

## 错误处理

### 全局异常过滤器

```typescript
// zsxq-exception.filter.ts
import {
  ExceptionFilter,
  Catch,
  ArgumentsHost,
  HttpStatus,
} from '@nestjs/common';
import { Response } from 'express';
import {
  ZsxqException,
  TokenExpiredException,
  RateLimitException,
  PermissionException,
} from 'zsxq-sdk';

@Catch(ZsxqException)
export class ZsxqExceptionFilter implements ExceptionFilter {
  catch(exception: ZsxqException, host: ArgumentsHost) {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse<Response>();

    let status = HttpStatus.INTERNAL_SERVER_ERROR;
    let message = exception.message;

    if (exception instanceof TokenExpiredException) {
      status = HttpStatus.UNAUTHORIZED;
      message = 'Token 已过期';
    } else if (exception instanceof RateLimitException) {
      status = HttpStatus.TOO_MANY_REQUESTS;
      message = '请求过于频繁';
    } else if (exception instanceof PermissionException) {
      status = HttpStatus.FORBIDDEN;
      message = '权限不足';
    }

    response.status(status).json({
      success: false,
      error: {
        code: exception.code,
        message,
      },
      timestamp: new Date().toISOString(),
    });
  }
}
```

### 注册过滤器

```typescript
// main.ts
import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { ZsxqExceptionFilter } from './filters/zsxq-exception.filter';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.useGlobalFilters(new ZsxqExceptionFilter());
  await app.listen(3000);
}
bootstrap();
```

---

## 健康检查

### 实现健康检查指示器

```typescript
// zsxq.health.ts
import { Injectable } from '@nestjs/common';
import {
  HealthIndicator,
  HealthIndicatorResult,
  HealthCheckError,
} from '@nestjs/terminus';
import { ZsxqClient } from 'zsxq-sdk';
import { InjectZsxqClient } from 'zsxq-sdk/nestjs';

@Injectable()
export class ZsxqHealthIndicator extends HealthIndicator {
  constructor(
    @InjectZsxqClient()
    private readonly zsxq: ZsxqClient,
  ) {
    super();
  }

  async isHealthy(key: string): Promise<HealthIndicatorResult> {
    try {
      await this.zsxq.users.getSelf();
      return this.getStatus(key, true);
    } catch (error) {
      throw new HealthCheckError(
        'Zsxq API check failed',
        this.getStatus(key, false, { error: error.message }),
      );
    }
  }
}
```

### 使用健康检查

```typescript
// health.controller.ts
import { Controller, Get } from '@nestjs/common';
import { HealthCheck, HealthCheckService } from '@nestjs/terminus';
import { ZsxqHealthIndicator } from './zsxq.health';

@Controller('health')
export class HealthController {
  constructor(
    private health: HealthCheckService,
    private zsxqHealth: ZsxqHealthIndicator,
  ) {}

  @Get()
  @HealthCheck()
  check() {
    return this.health.check([
      () => this.zsxqHealth.isHealthy('zsxq'),
    ]);
  }
}
```

---

## 完整示例

### 项目结构

```
src/
├── app.module.ts
├── main.ts
├── config/
│   └── zsxq.config.ts
├── filters/
│   └── zsxq-exception.filter.ts
├── health/
│   └── zsxq.health.ts
└── modules/
    └── planet/
        ├── planet.module.ts
        ├── planet.controller.ts
        └── planet.service.ts
```

### 完整配置

```typescript
// app.module.ts
import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { TerminusModule } from '@nestjs/terminus';
import { ZsxqModule } from 'zsxq-sdk/nestjs';
import { PlanetModule } from './modules/planet/planet.module';
import { ZsxqHealthIndicator } from './health/zsxq.health';
import { HealthController } from './health/health.controller';

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
    }),
    TerminusModule,
    ZsxqModule.forRootAsync({
      imports: [ConfigModule],
      useFactory: (config: ConfigService) => ({
        token: config.getOrThrow('ZSXQ_TOKEN'),
        timeout: config.get('ZSXQ_TIMEOUT', 10000),
        retry: config.get('ZSXQ_RETRY', 3),
      }),
      inject: [ConfigService],
    }),
    PlanetModule,
  ],
  controllers: [HealthController],
  providers: [ZsxqHealthIndicator],
})
export class AppModule {}
```

### 服务示例

```typescript
// planet.service.ts
import { Injectable, Logger } from '@nestjs/common';
import { ZsxqClient, Group, Topic } from 'zsxq-sdk';
import { InjectZsxqClient } from 'zsxq-sdk/nestjs';

@Injectable()
export class PlanetService {
  private readonly logger = new Logger(PlanetService.name);

  constructor(
    @InjectZsxqClient()
    private readonly zsxq: ZsxqClient,
  ) {}

  async getMyPlanets(): Promise<Group[]> {
    this.logger.log('Fetching user planets');
    return this.zsxq.groups.list();
  }

  async getPlanetTopics(
    groupId: string,
    count = 20,
  ): Promise<Topic[]> {
    this.logger.log(`Fetching topics for group ${groupId}`);
    return this.zsxq.topics.list(groupId, { count });
  }

  async getPlanetDigests(groupId: string): Promise<Topic[]> {
    return this.zsxq.topics.list(groupId, { scope: 'digests' });
  }
}
```

### 控制器示例

```typescript
// planet.controller.ts
import { Controller, Get, Param, Query } from '@nestjs/common';
import { PlanetService } from './planet.service';

@Controller('planets')
export class PlanetController {
  constructor(private readonly planetService: PlanetService) {}

  @Get()
  getMyPlanets() {
    return this.planetService.getMyPlanets();
  }

  @Get(':id/topics')
  getTopics(
    @Param('id') id: string,
    @Query('count') count?: number,
  ) {
    return this.planetService.getPlanetTopics(id, count);
  }

  @Get(':id/digests')
  getDigests(@Param('id') id: string) {
    return this.planetService.getPlanetDigests(id);
  }
}
```

---

## 相关文档

- [快速开始](quick-start.md) - SDK 基础使用
- [客户端 API](../api/client.md) - 完整 API 文档
- [错误处理](error-handling.md) - 错误处理最佳实践
