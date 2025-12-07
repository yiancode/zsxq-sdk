import { NestFactory } from '@nestjs/core';
import { ValidationPipe } from '@nestjs/common';
import { SwaggerModule, DocumentBuilder } from '@nestjs/swagger';
import { ConfigService } from '@nestjs/config';
import helmet from 'helmet';
import compression from 'compression';
import { AppModule } from './app.module';
import { HttpExceptionFilter } from '@/common/filters/http-exception.filter';
import { TransformInterceptor } from '@/common/interceptors/transform.interceptor';
import { LoggingInterceptor } from '@/common/interceptors/logging.interceptor';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  const configService = app.get(ConfigService);

  // 全局前缀
  const apiPrefix = configService.get<string>('API_PREFIX', 'api/v1');
  app.setGlobalPrefix(apiPrefix);

  // 安全相关
  app.use(helmet());
  app.use(compression());

  // 启用CORS
  app.enableCors({
    origin: true,
    credentials: true,
  });

  // 全局验证管道
  app.useGlobalPipes(
    new ValidationPipe({
      transform: true,
      whitelist: true,
      forbidNonWhitelisted: true,
      transformOptions: {
        enableImplicitConversion: true,
      },
    }),
  );

  // 全局异常过滤器
  app.useGlobalFilters(new HttpExceptionFilter());

  // 全局拦截器
  app.useGlobalInterceptors(new TransformInterceptor(), new LoggingInterceptor());

  // Swagger文档配置
  const swaggerEnabled = configService.get<boolean>('SWAGGER_ENABLED', true);
  if (swaggerEnabled) {
    const swaggerPath = configService.get<string>('SWAGGER_PATH', 'api-docs');
    const config = new DocumentBuilder()
      .setTitle('知识星球API')
      .setDescription('知识星球API封装服务文档')
      .setVersion('1.0')
      .addBearerAuth()
      .addTag('auth', '认证相关')
      .addTag('planets', '星球相关')
      .addTag('topics', '话题相关')
      .addTag('training-camps', '训练营相关')
      .addTag('owner', '星主专用')
      .build();

    const document = SwaggerModule.createDocument(app, config);
    SwaggerModule.setup(swaggerPath, app, document, {
      swaggerOptions: {
        persistAuthorization: true,
      },
    });
  }

  // 启动服务
  const port = configService.get<number>('PORT', 3000);
  await app.listen(port);

  const url = await app.getUrl();
  console.log(`🚀 Application is running on: ${url}`);
  if (swaggerEnabled) {
    const swaggerPath = configService.get<string>('SWAGGER_PATH', 'api-docs');
    console.log(`📚 Swagger documentation: ${url}/${swaggerPath}`);
  }
}

bootstrap();
