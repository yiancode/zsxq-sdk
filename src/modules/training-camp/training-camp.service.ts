import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { TrainingCamp } from './entities/training-camp.entity';

@Injectable()
export class TrainingCampService {
  constructor(
    @InjectRepository(TrainingCamp)
    private readonly trainingCampRepository: Repository<TrainingCamp>,
  ) {}

  async findByPlanetId(planetId: string): Promise<TrainingCamp[]> {
    return this.trainingCampRepository.find({
      where: { planetId },
      order: { startDate: 'DESC' },
    });
  }
}
