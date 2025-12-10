import { describe, it, expect, beforeEach, jest } from '@jest/globals';
import { TopicsRequest } from './index';
import { HttpClient } from '../http';

describe('TopicsRequest', () => {
  let topicsRequest: TopicsRequest;
  let mockHttpClient: HttpClient;

  beforeEach(() => {
    mockHttpClient = {
      get: jest.fn(),
      post: jest.fn(),
      put: jest.fn(),
      delete: jest.fn(),
    } as any;

    topicsRequest = new TopicsRequest(mockHttpClient);
  });

  describe('list', () => {
    it('应该获取话题列表', async () => {
      const mockTopics = [
        { id: 1, title: '话题1' },
        { id: 2, title: '话题2' },
      ];

      (mockHttpClient.get as any).mockResolvedValue({ topics: mockTopics });

      const result = await topicsRequest.list(12345);

      expect(result).toEqual(mockTopics);
      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/12345/topics', undefined);
    });

    it('应该支持传递查询参数', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ topics: [] });

      await topicsRequest.list(12345, {
        count: 20,
        scope: 'digests',
        direction: 'forward',
      });

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/12345/topics', {
        count: 20,
        scope: 'digests',
        direction: 'forward',
      });
    });

    it('应该支持时间范围查询', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ topics: [] });

      await topicsRequest.list(12345, {
        begin_time: '2024-01-01T00:00:00Z',
        end_time: '2024-12-31T23:59:59Z',
      });

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/12345/topics', {
        begin_time: '2024-01-01T00:00:00Z',
        end_time: '2024-12-31T23:59:59Z',
      });
    });

    it('应该支持查询隐藏话题', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ topics: [] });

      await topicsRequest.list(12345, { with_invisibles: true });

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/12345/topics', {
        with_invisibles: true,
      });
    });
  });

  describe('get', () => {
    it('应该获取话题详情', async () => {
      const mockTopic = {
        id: 123,
        title: '测试话题',
        content: '话题内容',
      };

      (mockHttpClient.get as any).mockResolvedValue({ topic: mockTopic });

      const result = await topicsRequest.get(123);

      expect(result).toEqual(mockTopic);
      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/topics/123');
    });

    it('应该支持字符串 ID', async () => {
      const mockTopic = { id: '123', title: '测试话题' };
      (mockHttpClient.get as any).mockResolvedValue({ topic: mockTopic });

      await topicsRequest.get('123');

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/topics/123');
    });
  });

  describe('getComments', () => {
    it('应该获取话题评论', async () => {
      const mockComments = [
        { id: 1, content: '评论1' },
        { id: 2, content: '评论2' },
      ];

      (mockHttpClient.get as any).mockResolvedValue({ comments: mockComments });

      const result = await topicsRequest.getComments(123);

      expect(result).toEqual(mockComments);
      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/topics/123/comments', undefined);
    });

    it('应该支持评论查询参数', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ comments: [] });

      await topicsRequest.getComments(123, {
        count: 50,
        sort: 'asc',
        with_sticky: true,
      });

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/topics/123/comments', {
        count: 50,
        sort: 'asc',
        with_sticky: true,
      });
    });
  });

  describe('listByHashtag', () => {
    it('应该按标签获取话题', async () => {
      const mockTopics = [
        { id: 1, title: '话题1', hashtag_id: 100 },
        { id: 2, title: '话题2', hashtag_id: 100 },
      ];

      (mockHttpClient.get as any).mockResolvedValue({ topics: mockTopics });

      const result = await topicsRequest.listByHashtag(100);

      expect(result).toEqual(mockTopics);
      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/hashtags/100/topics', undefined);
    });

    it('应该支持查询参数', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ topics: [] });

      await topicsRequest.listByHashtag(100, { count: 30, scope: 'digests' });

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/hashtags/100/topics', {
        count: 30,
        scope: 'digests',
      });
    });
  });

  describe('listByColumn', () => {
    it('应该按专栏获取话题', async () => {
      const mockTopics = [
        { id: 1, title: '专栏话题1' },
        { id: 2, title: '专栏话题2' },
      ];

      (mockHttpClient.get as any).mockResolvedValue({ topics: mockTopics });

      const result = await topicsRequest.listByColumn(12345, 200);

      expect(result).toEqual(mockTopics);
      expect(mockHttpClient.get).toHaveBeenCalledWith(
        '/v2/groups/12345/columns/200/topics',
        undefined,
      );
    });

    it('应该支持查询参数', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ topics: [] });

      await topicsRequest.listByColumn(12345, 200, {
        count: 10,
        direction: 'backward',
      });

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/12345/columns/200/topics', {
        count: 10,
        direction: 'backward',
      });
    });

    it('应该支持字符串类型的 ID', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ topics: [] });

      await topicsRequest.listByColumn('12345', '200');

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/12345/columns/200/topics', undefined);
    });
  });
});
