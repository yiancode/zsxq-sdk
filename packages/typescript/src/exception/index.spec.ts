import { describe, it, expect } from '@jest/globals';
import {
  ZsxqException,
  AuthException,
  TokenInvalidException,
  TokenExpiredException,
  SignatureInvalidException,
  PermissionException,
  NotMemberException,
  NotOwnerException,
  ResourceNotFoundException,
  GroupNotFoundException,
  TopicNotFoundException,
  RateLimitException,
  BusinessException,
  NotJoinedCheckinException,
  NetworkException,
  TimeoutException,
  createException,
} from './index';

describe('异常类', () => {
  describe('ZsxqException', () => {
    it('应该正确创建基础异常', () => {
      const ex = new ZsxqException(99999, '测试错误', 'req-123');

      expect(ex).toBeInstanceOf(Error);
      expect(ex).toBeInstanceOf(ZsxqException);
      expect(ex.name).toBe('ZsxqException');
      expect(ex.code).toBe(99999);
      expect(ex.message).toBe('测试错误');
      expect(ex.requestId).toBe('req-123');
    });

    it('requestId 应该是可选的', () => {
      const ex = new ZsxqException(99999, '测试错误');

      expect(ex.requestId).toBeUndefined();
    });

    it('应该可以抛出和捕获', () => {
      expect(() => {
        throw new ZsxqException(99999, '测试错误');
      }).toThrow(ZsxqException);
    });
  });

  describe('AuthException', () => {
    it('应该正确创建认证异常', () => {
      const ex = new AuthException(10000, '认证失败', 'req-456');

      expect(ex).toBeInstanceOf(ZsxqException);
      expect(ex).toBeInstanceOf(AuthException);
      expect(ex.name).toBe('AuthException');
      expect(ex.code).toBe(10000);
    });

    describe('TokenInvalidException', () => {
      it('应该使用默认消息', () => {
        const ex = new TokenInvalidException();

        expect(ex).toBeInstanceOf(AuthException);
        expect(ex.name).toBe('TokenInvalidException');
        expect(ex.code).toBe(10001);
        expect(ex.message).toBe('Token 无效');
      });

      it('应该使用自定义消息', () => {
        const ex = new TokenInvalidException('自定义 Token 错误', 'req-789');

        expect(ex.message).toBe('自定义 Token 错误');
        expect(ex.requestId).toBe('req-789');
      });
    });

    describe('TokenExpiredException', () => {
      it('应该使用默认消息', () => {
        const ex = new TokenExpiredException();

        expect(ex).toBeInstanceOf(AuthException);
        expect(ex.name).toBe('TokenExpiredException');
        expect(ex.code).toBe(10002);
        expect(ex.message).toBe('Token 已过期');
      });

      it('应该使用自定义消息', () => {
        const ex = new TokenExpiredException('Token 于昨天过期');

        expect(ex.message).toBe('Token 于昨天过期');
      });
    });

    describe('SignatureInvalidException', () => {
      it('应该使用默认消息', () => {
        const ex = new SignatureInvalidException();

        expect(ex).toBeInstanceOf(AuthException);
        expect(ex.name).toBe('SignatureInvalidException');
        expect(ex.code).toBe(10003);
        expect(ex.message).toBe('签名验证失败');
      });
    });
  });

  describe('PermissionException', () => {
    it('应该正确创建权限异常', () => {
      const ex = new PermissionException(20001, '权限不足');

      expect(ex).toBeInstanceOf(ZsxqException);
      expect(ex).toBeInstanceOf(PermissionException);
      expect(ex.name).toBe('PermissionException');
      expect(ex.code).toBe(20001);
    });

    describe('NotMemberException', () => {
      it('应该使用默认消息', () => {
        const ex = new NotMemberException();

        expect(ex).toBeInstanceOf(PermissionException);
        expect(ex.name).toBe('NotMemberException');
        expect(ex.code).toBe(20002);
        expect(ex.message).toBe('非星球成员');
      });
    });

    describe('NotOwnerException', () => {
      it('应该使用默认消息', () => {
        const ex = new NotOwnerException();

        expect(ex).toBeInstanceOf(PermissionException);
        expect(ex.name).toBe('NotOwnerException');
        expect(ex.code).toBe(20003);
        expect(ex.message).toBe('非星主');
      });
    });
  });

  describe('ResourceNotFoundException', () => {
    it('应该正确创建资源不存在异常', () => {
      const ex = new ResourceNotFoundException(30000, '资源不存在');

      expect(ex).toBeInstanceOf(ZsxqException);
      expect(ex).toBeInstanceOf(ResourceNotFoundException);
      expect(ex.name).toBe('ResourceNotFoundException');
    });

    describe('GroupNotFoundException', () => {
      it('应该使用默认消息', () => {
        const ex = new GroupNotFoundException();

        expect(ex).toBeInstanceOf(ResourceNotFoundException);
        expect(ex.name).toBe('GroupNotFoundException');
        expect(ex.code).toBe(30001);
        expect(ex.message).toBe('星球不存在');
      });

      it('应该使用自定义消息', () => {
        const ex = new GroupNotFoundException('ID 为 123 的星球不存在');

        expect(ex.message).toBe('ID 为 123 的星球不存在');
      });
    });

    describe('TopicNotFoundException', () => {
      it('应该使用默认消息', () => {
        const ex = new TopicNotFoundException();

        expect(ex).toBeInstanceOf(ResourceNotFoundException);
        expect(ex.name).toBe('TopicNotFoundException');
        expect(ex.code).toBe(30002);
        expect(ex.message).toBe('话题不存在');
      });
    });
  });

  describe('RateLimitException', () => {
    it('应该使用默认消息', () => {
      const ex = new RateLimitException();

      expect(ex).toBeInstanceOf(ZsxqException);
      expect(ex.name).toBe('RateLimitException');
      expect(ex.code).toBe(40001);
      expect(ex.message).toBe('请求过于频繁');
      expect(ex.retryAfter).toBeUndefined();
    });

    it('应该包含 retryAfter 信息', () => {
      const ex = new RateLimitException('请求过于频繁', 'req-999', 60);

      expect(ex.retryAfter).toBe(60);
      expect(ex.code).toBe(40001);
      expect(ex.requestId).toBe('req-999');
    });
  });

  describe('BusinessException', () => {
    it('应该正确创建业务异常', () => {
      const ex = new BusinessException(50000, '业务错误');

      expect(ex).toBeInstanceOf(ZsxqException);
      expect(ex).toBeInstanceOf(BusinessException);
      expect(ex.name).toBe('BusinessException');
    });

    describe('NotJoinedCheckinException', () => {
      it('应该使用默认消息', () => {
        const ex = new NotJoinedCheckinException();

        expect(ex).toBeInstanceOf(BusinessException);
        expect(ex.name).toBe('NotJoinedCheckinException');
        expect(ex.code).toBe(52010);
        expect(ex.message).toBe('未参与打卡项目');
      });
    });
  });

  describe('NetworkException', () => {
    it('应该正确创建网络异常', () => {
      const cause = new Error('连接失败');
      const ex = new NetworkException('网络错误', cause, 'req-net');

      expect(ex).toBeInstanceOf(ZsxqException);
      expect(ex).toBeInstanceOf(NetworkException);
      expect(ex.name).toBe('NetworkException');
      expect(ex.code).toBe(70001);
      expect(ex.cause).toBe(cause);
    });

    it('cause 应该是可选的', () => {
      const ex = new NetworkException('网络错误');

      expect(ex.cause).toBeUndefined();
      expect(ex.message).toBe('网络错误');
      expect(ex.code).toBe(70001);
    });

    describe('TimeoutException', () => {
      it('应该使用默认消息', () => {
        const ex = new TimeoutException();

        expect(ex).toBeInstanceOf(NetworkException);
        expect(ex.name).toBe('TimeoutException');
        expect(ex.message).toBe('请求超时');
      });

      it('应该包含原始错误', () => {
        const cause = new Error('ECONNABORTED');
        const ex = new TimeoutException('超时了', cause, 'req-timeout');

        expect(ex.cause).toBe(cause);
        expect(ex.requestId).toBe('req-timeout');
      });
    });
  });

  describe('createException', () => {
    it('应该根据错误码创建 TokenInvalidException', () => {
      const ex = createException(10001, 'Token 错误', 'req-1');

      expect(ex).toBeInstanceOf(TokenInvalidException);
      expect(ex.message).toBe('Token 错误');
      expect(ex.requestId).toBe('req-1');
    });

    it('应该根据错误码创建 TokenExpiredException', () => {
      const ex = createException(10002, 'Token 过期', 'req-2');

      expect(ex).toBeInstanceOf(TokenExpiredException);
    });

    it('应该根据错误码创建 SignatureInvalidException', () => {
      const ex = createException(10003, '签名错误', 'req-3');

      expect(ex).toBeInstanceOf(SignatureInvalidException);
    });

    it('应该根据错误码创建 PermissionException', () => {
      const ex = createException(20001, '权限不足', 'req-4');

      expect(ex).toBeInstanceOf(PermissionException);
      expect(ex.code).toBe(20001);
    });

    it('应该根据错误码创建 NotMemberException', () => {
      const ex = createException(20002, '非成员', 'req-5');

      expect(ex).toBeInstanceOf(NotMemberException);
    });

    it('应该根据错误码创建 NotOwnerException', () => {
      const ex = createException(20003, '非星主', 'req-6');

      expect(ex).toBeInstanceOf(NotOwnerException);
    });

    it('应该根据错误码创建 GroupNotFoundException', () => {
      const ex = createException(30001, '星球不存在', 'req-7');

      expect(ex).toBeInstanceOf(GroupNotFoundException);
    });

    it('应该根据错误码创建 TopicNotFoundException', () => {
      const ex = createException(30002, '话题不存在', 'req-8');

      expect(ex).toBeInstanceOf(TopicNotFoundException);
    });

    it('应该根据错误码创建 RateLimitException', () => {
      const ex = createException(40001, '限流', 'req-9');

      expect(ex).toBeInstanceOf(RateLimitException);
    });

    it('应该根据错误码创建 NotJoinedCheckinException', () => {
      const ex = createException(52010, '未参与打卡', 'req-10');

      expect(ex).toBeInstanceOf(NotJoinedCheckinException);
    });

    it('应该对未知错误码创建 ZsxqException', () => {
      const ex = createException(99999, '未知错误', 'req-unknown');

      expect(ex).toBeInstanceOf(ZsxqException);
      expect(ex.constructor.name).toBe('ZsxqException');
      expect(ex.code).toBe(99999);
    });
  });

  describe('异常继承链', () => {
    it('TokenInvalidException 应该在继承链上', () => {
      const ex = new TokenInvalidException();

      expect(ex instanceof Error).toBe(true);
      expect(ex instanceof ZsxqException).toBe(true);
      expect(ex instanceof AuthException).toBe(true);
      expect(ex instanceof TokenInvalidException).toBe(true);
    });

    it('NotMemberException 应该在继承链上', () => {
      const ex = new NotMemberException();

      expect(ex instanceof Error).toBe(true);
      expect(ex instanceof ZsxqException).toBe(true);
      expect(ex instanceof PermissionException).toBe(true);
      expect(ex instanceof NotMemberException).toBe(true);
    });

    it('TimeoutException 应该在继承链上', () => {
      const ex = new TimeoutException();

      expect(ex instanceof Error).toBe(true);
      expect(ex instanceof ZsxqException).toBe(true);
      expect(ex instanceof NetworkException).toBe(true);
      expect(ex instanceof TimeoutException).toBe(true);
    });
  });

  describe('异常序列化', () => {
    it('应该包含所有关键字段', () => {
      const ex = new TokenInvalidException('Token 无效', 'req-123');

      expect(ex.name).toBe('TokenInvalidException');
      expect(ex.message).toBe('Token 无效');
      expect(ex.code).toBe(10001);
      expect(ex.requestId).toBe('req-123');
    });

    it('NetworkException 应该包含 cause', () => {
      const cause = new Error('网络错误');
      const ex = new NetworkException('请求失败', cause);

      expect(ex.message).toBe('请求失败');
      expect(ex.cause).toBe(cause);
      expect(ex.cause?.message).toBe('网络错误');
    });
  });
});
