import { describe, it, expect, beforeEach, jest } from '@jest/globals';
import { GroupsRequest } from './index';
import { HttpClient } from '../http';

describe('GroupsRequest', () => {
  let groupsRequest: GroupsRequest;
  let mockHttpClient: HttpClient;

  beforeEach(() => {
    mockHttpClient = {
      get: jest.fn(),
      post: jest.fn(),
      put: jest.fn(),
      delete: jest.fn(),
    } as any;

    groupsRequest = new GroupsRequest(mockHttpClient);
  });

  describe('list', () => {
    it('应该获取星球列表', async () => {
      const mockGroups = [
        { id: 1, name: '测试星球 1' },
        { id: 2, name: '测试星球 2' },
      ];

      (mockHttpClient.get as any).mockResolvedValue({ groups: mockGroups });

      const result = await groupsRequest.list();

      expect(result).toEqual(mockGroups);
      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups');
    });

    it('应该返回空数组当没有星球时', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ groups: [] });

      const result = await groupsRequest.list();

      expect(result).toEqual([]);
    });
  });

  describe('get', () => {
    it('应该通过数字 ID 获取星球详情', async () => {
      const mockGroup = { id: 12345, name: '测试星球' };
      (mockHttpClient.get as any).mockResolvedValue({ group: mockGroup });

      const result = await groupsRequest.get(12345);

      expect(result).toEqual(mockGroup);
      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/12345');
    });

    it('应该通过字符串 ID 获取星球详情', async () => {
      const mockGroup = { id: '12345', name: '测试星球' };
      (mockHttpClient.get as any).mockResolvedValue({ group: mockGroup });

      const result = await groupsRequest.get('12345');

      expect(result).toEqual(mockGroup);
      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/12345');
    });
  });

  describe('getHashtags', () => {
    it('应该获取星球标签列表', async () => {
      const mockHashtags = [
        { id: 1, name: '技术' },
        { id: 2, name: '产品' },
      ];

      (mockHttpClient.get as any).mockResolvedValue({ hashtags: mockHashtags });

      const result = await groupsRequest.getHashtags(12345);

      expect(result).toEqual(mockHashtags);
      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/12345/hashtags');
    });

    it('应该返回空数组当没有标签时', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ hashtags: [] });

      const result = await groupsRequest.getHashtags(12345);

      expect(result).toEqual([]);
    });
  });

  describe('getStatistics', () => {
    it('应该获取星球统计数据', async () => {
      const mockStats = {
        member_count: 1000,
        topic_count: 500,
        view_count: 10000,
      };

      (mockHttpClient.get as any).mockResolvedValue(mockStats);

      const result = await groupsRequest.getStatistics(12345);

      expect(result).toEqual(mockStats);
      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/12345/statistics');
    });
  });

  describe('getMember', () => {
    it('应该获取成员信息', async () => {
      const mockUser = {
        id: 123,
        name: '张三',
        avatar: 'https://example.com/avatar.jpg',
      };

      (mockHttpClient.get as any).mockResolvedValue({ user: mockUser });

      const result = await groupsRequest.getMember(12345, 123);

      expect(result).toEqual(mockUser);
      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/12345/members/123');
    });

    it('应该支持字符串类型的成员 ID', async () => {
      const mockUser = { id: '123', name: '张三' };
      (mockHttpClient.get as any).mockResolvedValue({ user: mockUser });

      await groupsRequest.getMember('12345', '123');

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/12345/members/123');
    });
  });

  describe('getUnreadCount', () => {
    it('应该获取未读话题数', async () => {
      const mockCounts = {
        '12345': 10,
        '67890': 5,
      };

      (mockHttpClient.get as any).mockResolvedValue(mockCounts);

      const result = await groupsRequest.getUnreadCount();

      expect(result).toEqual(mockCounts);
      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/unread_topics_count');
    });

    it('应该返回空对象当没有未读时', async () => {
      (mockHttpClient.get as any).mockResolvedValue({});

      const result = await groupsRequest.getUnreadCount();

      expect(result).toEqual({});
    });
  });
});
