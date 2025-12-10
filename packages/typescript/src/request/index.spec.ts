import { GroupsRequest } from './index';
import { HttpClient } from '../http';
import { Group, User } from '../model';

// Mock HttpClient
jest.mock('../http');

describe('GroupsRequest', () => {
  let groupsRequest: GroupsRequest;
  let mockHttpClient: jest.Mocked<HttpClient>;

  beforeEach(() => {
    mockHttpClient = new HttpClient({} as any) as jest.Mocked<HttpClient>;
    groupsRequest = new GroupsRequest(mockHttpClient);
  });

  describe('list', () => {
    it('应该返回星球列表', async () => {
      const mockGroups: Partial<Group>[] = [
        { group_id: 123, name: '测试星球1', type: 'free' },
        { group_id: 456, name: '测试星球2', type: 'pay' },
      ];

      mockHttpClient.get = jest.fn().mockResolvedValue({ groups: mockGroups });

      const result = await groupsRequest.list();

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups');
      expect(result).toEqual(mockGroups);
      expect(result).toHaveLength(2);
      expect(result[0].name).toBe('测试星球1');
    });

    it('应该返回空列表', async () => {
      mockHttpClient.get = jest.fn().mockResolvedValue({ groups: [] });

      const result = await groupsRequest.list();

      expect(result).toEqual([]);
      expect(result).toHaveLength(0);
    });
  });

  describe('get', () => {
    it('应该返回星球详情（使用 number ID）', async () => {
      const mockGroup: Partial<Group> = {
        group_id: 123,
        name: '测试星球',
        type: 'free',
      };

      mockHttpClient.get = jest.fn().mockResolvedValue({ group: mockGroup });

      const result = await groupsRequest.get(123);

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/123');
      expect(result).toEqual(mockGroup);
      expect(result.name).toBe('测试星球');
    });

    it('应该返回星球详情（使用 string ID）', async () => {
      const mockGroup: Partial<Group> = {
        group_id: 123,
        name: '测试星球',
        type: 'free',
      };

      mockHttpClient.get = jest.fn().mockResolvedValue({ group: mockGroup });

      const result = await groupsRequest.get('123');

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/123');
      expect(result).toEqual(mockGroup);
    });
  });

  describe('getStatistics', () => {
    it('应该返回星球统计信息', async () => {
      const mockStats = {
        members_count: 100,
        topics_count: 50,
      };

      mockHttpClient.get = jest.fn().mockResolvedValue(mockStats);

      const result = await groupsRequest.getStatistics(123);

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/123/statistics');
      expect(result).toEqual(mockStats);
      expect(result.members_count).toBe(100);
      expect(result.topics_count).toBe(50);
    });
  });

  describe('getMember', () => {
    it('应该返回成员信息', async () => {
      const mockUser: Partial<User> = {
        user_id: 999,
        name: '测试用户',
      };

      mockHttpClient.get = jest.fn().mockResolvedValue({ user: mockUser });

      const result = await groupsRequest.getMember(123, 999);

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/123/members/999');
      expect(result).toEqual(mockUser);
      expect(result.name).toBe('测试用户');
    });
  });

  describe('getHashtags', () => {
    it('应该返回标签列表', async () => {
      const mockHashtags = [
        { hashtag_id: 1, name: '标签1' },
        { hashtag_id: 2, name: '标签2' },
      ];

      mockHttpClient.get = jest.fn().mockResolvedValue({ hashtags: mockHashtags });

      const result = await groupsRequest.getHashtags(123);

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/123/hashtags');
      expect(result).toEqual(mockHashtags);
      expect(result).toHaveLength(2);
    });
  });

  describe('getUnreadCount', () => {
    it('应该返回未读话题数', async () => {
      const mockCount = {
        '123': 5,
        '456': 10,
      };

      mockHttpClient.get = jest.fn().mockResolvedValue(mockCount);

      const result = await groupsRequest.getUnreadCount();

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/unread_topics_count');
      expect(result).toEqual(mockCount);
    });
  });
});
