import { Controller } from '@nestjs/common';
import { ApiTags } from '@nestjs/swagger';
import { TrainingCampService } from './training-camp.service';

@ApiTags('training-camps')
@Controller('training-camps')
export class TrainingCampController {
  constructor(private readonly trainingCampService: TrainingCampService) {}
}
