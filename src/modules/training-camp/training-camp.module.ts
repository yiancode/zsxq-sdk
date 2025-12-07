import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { TrainingCampController } from './training-camp.controller';
import { TrainingCampService } from './training-camp.service';
import { TrainingCamp } from './entities/training-camp.entity';

@Module({
  imports: [TypeOrmModule.forFeature([TrainingCamp])],
  controllers: [TrainingCampController],
  providers: [TrainingCampService],
  exports: [TrainingCampService],
})
export class TrainingCampModule {}
