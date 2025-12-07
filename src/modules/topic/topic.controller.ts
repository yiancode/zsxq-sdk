import { Controller } from '@nestjs/common';
import { ApiTags } from '@nestjs/swagger';
import { TopicService } from './topic.service';

@ApiTags('topics')
@Controller('topics')
export class TopicController {
  constructor(private readonly topicService: TopicService) {}
}
