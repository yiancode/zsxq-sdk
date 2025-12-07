import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Planet } from './entities/planet.entity';

@Injectable()
export class PlanetService {
  constructor(
    @InjectRepository(Planet)
    private readonly planetRepository: Repository<Planet>,
  ) {}

  async findAll(): Promise<Planet[]> {
    return this.planetRepository.find({
      relations: ['owner'],
      order: { createdAt: 'DESC' },
    });
  }

  async findById(id: string): Promise<Planet> {
    const planet = await this.planetRepository.findOne({
      where: { id },
      relations: ['owner'],
    });
    if (!planet) {
      throw new NotFoundException(`星球 ID ${id} 不存在`);
    }
    return planet;
  }

  async findByZsxqPlanetId(zsxqPlanetId: string): Promise<Planet | null> {
    return this.planetRepository.findOne({ where: { zsxqPlanetId } });
  }

  async create(planetData: Partial<Planet>): Promise<Planet> {
    const planet = this.planetRepository.create(planetData);
    return this.planetRepository.save(planet);
  }

  async update(id: string, planetData: Partial<Planet>): Promise<Planet> {
    await this.findById(id);
    await this.planetRepository.update(id, planetData);
    return this.findById(id);
  }
}
