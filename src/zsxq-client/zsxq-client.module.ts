import { Module } from '@nestjs/common';
import { HttpModule } from '@nestjs/axios';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { ZsxqClientService } from './zsxq-client.service';

@Module({
  imports: [
    HttpModule.registerAsync({
      imports: [ConfigModule],
      inject: [ConfigService],
      useFactory: (configService: ConfigService) => ({
        baseURL: configService.get<string>('ZSXQ_API_BASE_URL'),
        timeout: configService.get<number>('ZSXQ_API_TIMEOUT', 30000),
        headers: {
          'Content-Type': 'application/json',
        },
      }),
    }),
  ],
  providers: [ZsxqClientService],
  exports: [ZsxqClientService],
})
export class ZsxqClientModule {}
