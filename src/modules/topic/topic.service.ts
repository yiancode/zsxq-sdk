import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Topic } from './entities/topic.entity';

@Injectable()
export class TopicService {
  constructor(
    @InjectRepository(Topic)
    private readonly topicRepository: Repository<Topic>,
  ) {}

  async findByPlanetId(planetId: string): Promise<Topic[]> {
    return this.topicRepository.find({
      where: { planetId },
      relations: ['author'],
      order: { createdAt: 'DESC' },
    });
  }
}
