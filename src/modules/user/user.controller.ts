import { Controller, Get, UseGuards } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiBearerAuth } from '@nestjs/swagger';
import { UserService } from './user.service';
import { CurrentUser } from '@/common/decorators/current-user.decorator';
import { User } from './entities/user.entity';

@ApiTags('users')
@Controller('users')
export class UserController {
  constructor(private readonly userService: UserService) {}

  @Get('me')
  @ApiOperation({ summary: '获取当前用户信息' })
  @ApiBearerAuth()
  async getCurrentUser(@CurrentUser() user: User) {
    return this.userService.findById(user.id);
  }
}
