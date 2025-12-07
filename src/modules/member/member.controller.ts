import { Controller } from '@nestjs/common';
import { ApiTags, ApiBearerAuth } from '@nestjs/swagger';
import { MemberService } from './member.service';
import { Roles } from '@/common/decorators/roles.decorator';
import { UserRole } from '@/modules/user/enums/user-role.enum';

@ApiTags('owner')
@ApiBearerAuth()
@Controller('owner/members')
@Roles(UserRole.PLANET_OWNER)
export class MemberController {
  constructor(private readonly memberService: MemberService) {}
}
