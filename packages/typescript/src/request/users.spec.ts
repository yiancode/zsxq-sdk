import { describe, it, expect, beforeEach, jest } from '@jest/globals';
import { UsersRequest } from './index';
import { HttpClient } from '../http';

describe('UsersRequest', () => {
  let usersRequest: UsersRequest;
  let mockHttpClient: HttpClient;

  beforeEach(() => {
    mockHttpClient = {
      get: jest.fn(),
      post: jest.fn(),
      put: jest.fn(),
      delete: jest.fn(),
    } as any;

    usersRequest = new UsersRequest(mockHttpClient);
  });

  describe('self', () => {
    it('应该获取当前用户信息', async () => {
      const mockUser = {
        id: 123,
        name: '当前用户',
        avatar: 'https://example.com/avatar.jpg',
      };

      (mockHttpClient.get as any).mockResolvedValue({ user: mockUser });

      const result = await usersRequest.self();

      expect(result).toEqual(mockUser);
      expect(mockHttpClient.get).toHaveBeenCalledWith('/v3/users/self');
    });
  });

  describe('get', () => {
    it('应该通过数字 ID 获取用户信息', async () => {
      const mockUser = {
        id: 456,
        name: '指定用户',
        avatar: 'https://example.com/user.jpg',
      };

      (mockHttpClient.get as any).mockResolvedValue({ user: mockUser });

      const result = await usersRequest.get(456);

      expect(result).toEqual(mockUser);
      expect(mockHttpClient.get).toHaveBeenCalledWith('/v3/users/456');
    });

    it('应该通过字符串 ID 获取用户信息', async () => {
      const mockUser = { id: '456', name: '指定用户' };
      (mockHttpClient.get as any).mockResolvedValue({ user: mockUser });

      const result = await usersRequest.get('456');

      expect(result).toEqual(mockUser);
      expect(mockHttpClient.get).toHaveBeenCalledWith('/v3/users/456');
    });
  });

  describe('getStatistics', () => {
    it('应该获取用户统计数据', async () => {
      const mockStats = {
        topic_count: 100,
        comment_count: 200,
        like_count: 500,
        follower_count: 1000,
      };

      (mockHttpClient.get as any).mockResolvedValue(mockStats);

      const result = await usersRequest.getStatistics(456);

      expect(result).toEqual(mockStats);
      expect(mockHttpClient.get).toHaveBeenCalledWith('/v3/users/456/statistics');
    });

    it('应该支持字符串类型的用户 ID', async () => {
      (mockHttpClient.get as any).mockResolvedValue({});

      await usersRequest.getStatistics('456');

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v3/users/456/statistics');
    });
  });

  describe('getFootprints', () => {
    it('应该获取用户动态', async () => {
      const mockTopics = [
        { id: 1, title: '用户话题1' },
        { id: 2, title: '用户话题2' },
      ];

      (mockHttpClient.get as any).mockResolvedValue({ topics: mockTopics });

      const result = await usersRequest.getFootprints(456);

      expect(result).toEqual(mockTopics);
      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/users/456/footprints');
    });

    it('应该返回空数组当用户没有动态时', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ topics: [] });

      const result = await usersRequest.getFootprints(456);

      expect(result).toEqual([]);
    });
  });

  describe('getCreatedGroups', () => {
    it('应该获取用户创建的星球', async () => {
      const mockGroups = [
        { id: 1, name: '创建的星球1' },
        { id: 2, name: '创建的星球2' },
      ];

      (mockHttpClient.get as any).mockResolvedValue({ groups: mockGroups });

      const result = await usersRequest.getCreatedGroups(456);

      expect(result).toEqual(mockGroups);
      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/users/456/created_groups');
    });

    it('应该返回空数组当用户没有创建星球时', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ groups: [] });

      const result = await usersRequest.getCreatedGroups(456);

      expect(result).toEqual([]);
    });

    it('应该支持字符串类型的用户 ID', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ groups: [] });

      await usersRequest.getCreatedGroups('456');

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/users/456/created_groups');
    });
  });
});
