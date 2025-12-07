import { Controller, Get, Param } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiBearerAuth } from '@nestjs/swagger';
import { PlanetService } from './planet.service';

@ApiTags('planets')
@ApiBearerAuth()
@Controller('planets')
export class PlanetController {
  constructor(private readonly planetService: PlanetService) {}

  @Get()
  @ApiOperation({ summary: '获取星球列表' })
  async findAll() {
    return this.planetService.findAll();
  }

  @Get(':id')
  @ApiOperation({ summary: '获取星球详情' })
  async findOne(@Param('id') id: string) {
    return this.planetService.findById(id);
  }
}
