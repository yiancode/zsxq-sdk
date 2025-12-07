import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { PlanetController } from './planet.controller';
import { PlanetService } from './planet.service';
import { Planet } from './entities/planet.entity';

@Module({
  imports: [TypeOrmModule.forFeature([Planet])],
  controllers: [PlanetController],
  providers: [PlanetService],
  exports: [PlanetService],
})
export class PlanetModule {}
