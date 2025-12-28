import { describe, it, expect, beforeEach, jest } from '@jest/globals';
import { CheckinsRequest } from './index';
import { HttpClient } from '../http';

describe('CheckinsRequest', () => {
  let checkinsRequest: CheckinsRequest;
  let mockHttpClient: HttpClient;

  beforeEach(() => {
    mockHttpClient = {
      get: jest.fn(),
      post: jest.fn(),
      put: jest.fn(),
      delete: jest.fn(),
    } as any;

    checkinsRequest = new CheckinsRequest(mockHttpClient);
  });

  describe('list', () => {
    it('应该获取打卡项目列表', async () => {
      const mockCheckins = [
        { id: 1, name: '打卡项目1' },
        { id: 2, name: '打卡项目2' },
      ];

      (mockHttpClient.get as any).mockResolvedValue({ checkins: mockCheckins });

      const result = await checkinsRequest.list(12345);

      expect(result).toEqual(mockCheckins);
      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/12345/checkins', undefined);
    });

    it('应该支持按状态筛选', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ checkins: [] });

      await checkinsRequest.list(12345, { scope: 'ongoing' });

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/12345/checkins', {
        scope: 'ongoing',
      });
    });

    it('应该支持限制返回数量', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ checkins: [] });

      await checkinsRequest.list(12345, { count: 20 });

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/12345/checkins', {
        count: 20,
      });
    });

    it('应该支持组合参数', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ checkins: [] });

      await checkinsRequest.list(12345, { scope: 'closed', count: 10 });

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/12345/checkins', {
        scope: 'closed',
        count: 10,
      });
    });
  });

  describe('get', () => {
    it('应该获取打卡项目详情', async () => {
      const mockCheckin = {
        id: 100,
        name: '每日打卡',
        description: '坚持每日打卡',
      };

      (mockHttpClient.get as any).mockResolvedValue({ checkin: mockCheckin });

      const result = await checkinsRequest.get(12345, 100);

      expect(result).toEqual(mockCheckin);
      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/12345/checkins/100');
    });

    it('应该支持字符串类型的 ID', async () => {
      const mockCheckin = { id: '100', name: '每日打卡' };
      (mockHttpClient.get as any).mockResolvedValue({ checkin: mockCheckin });

      await checkinsRequest.get('12345', '100');

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/12345/checkins/100');
    });
  });

  describe('getStatistics', () => {
    it('应该获取打卡统计数据', async () => {
      const mockStats = {
        total_count: 1000,
        today_count: 50,
        participant_count: 200,
      };

      (mockHttpClient.get as any).mockResolvedValue(mockStats);

      const result = await checkinsRequest.getStatistics(12345, 100);

      expect(result).toEqual(mockStats);
      expect(mockHttpClient.get).toHaveBeenCalledWith(
        '/v2/groups/12345/checkins/100/statistics',
      );
    });

    it('应该支持字符串类型的 ID', async () => {
      (mockHttpClient.get as any).mockResolvedValue({});

      await checkinsRequest.getStatistics('12345', '100');

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/12345/checkins/100/statistics');
    });
  });

  describe('getRankingList', () => {
    it('应该获取打卡排行榜', async () => {
      const mockRanking = [
        { user_id: 1, name: '用户1', score: 100 },
        { user_id: 2, name: '用户2', score: 90 },
      ];

      (mockHttpClient.get as any).mockResolvedValue({ ranking_list: mockRanking });

      const result = await checkinsRequest.getRankingList(12345, 100);

      expect(result).toEqual(mockRanking);
      expect(mockHttpClient.get).toHaveBeenCalledWith(
        '/v2/groups/12345/checkins/100/ranking_list',
        { type: 'accumulated' }, // 应自动注入默认 type 参数
      );
    });

    it('应该支持排行榜类型参数', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ ranking_list: [] });

      await checkinsRequest.getRankingList(12345, 100, { type: 'continuous' });

      expect(mockHttpClient.get).toHaveBeenCalledWith(
        '/v2/groups/12345/checkins/100/ranking_list',
        { type: 'continuous' },
      );
    });

    it('应该支持分页参数', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ ranking_list: [] });

      await checkinsRequest.getRankingList(12345, 100, { index: 2 });

      expect(mockHttpClient.get).toHaveBeenCalledWith(
        '/v2/groups/12345/checkins/100/ranking_list',
        { index: 2, type: 'accumulated' }, // 应自动注入 type 参数
      );
    });

    it('应该支持组合参数', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ ranking_list: [] });

      await checkinsRequest.getRankingList(12345, 100, {
        type: 'accumulated',
        index: 3,
      });

      expect(mockHttpClient.get).toHaveBeenCalledWith(
        '/v2/groups/12345/checkins/100/ranking_list',
        {
          type: 'accumulated',
          index: 3,
        },
      );
    });
  });

  describe('getTopics', () => {
    it('应该获取打卡话题列表', async () => {
      const mockTopics = [
        { id: 1, title: '打卡话题1' },
        { id: 2, title: '打卡话题2' },
      ];

      (mockHttpClient.get as any).mockResolvedValue({ topics: mockTopics });

      const result = await checkinsRequest.getTopics(12345, 100);

      expect(result).toEqual(mockTopics);
      expect(mockHttpClient.get).toHaveBeenCalledWith(
        '/v2/groups/12345/checkins/100/topics',
        { scope: 'all', count: 20 }, // 应自动注入默认参数
      );
    });

    it('应该支持话题查询参数', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ topics: [] });

      await checkinsRequest.getTopics(12345, 100, {
        count: 30,
        scope: 'all',
        direction: 'forward',
      });

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/groups/12345/checkins/100/topics', {
        count: 30,
        scope: 'all',
        direction: 'forward',
      });
    });

    it('应该返回空数组当没有打卡话题时', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ topics: [] });

      const result = await checkinsRequest.getTopics(12345, 100);

      expect(result).toEqual([]);
    });
  });
});
