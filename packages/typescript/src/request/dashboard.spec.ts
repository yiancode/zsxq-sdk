import { describe, it, expect, beforeEach, jest } from '@jest/globals';
import { DashboardRequest } from './index';
import { HttpClient } from '../http';

describe('DashboardRequest', () => {
  let dashboardRequest: DashboardRequest;
  let mockHttpClient: HttpClient;

  beforeEach(() => {
    mockHttpClient = {
      get: jest.fn(),
      post: jest.fn(),
      put: jest.fn(),
      delete: jest.fn(),
    } as any;

    dashboardRequest = new DashboardRequest(mockHttpClient);
  });

  describe('getOverview', () => {
    it('应该获取星球概览数据', async () => {
      const mockOverview = {
        member_count: 5000,
        topic_count: 1000,
        active_member_count: 2000,
        total_views: 100000,
      };

      (mockHttpClient.get as any).mockResolvedValue(mockOverview);

      const result = await dashboardRequest.getOverview(12345);

      expect(result).toEqual(mockOverview);
      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/dashboard/groups/12345/overview');
    });

    it('应该支持字符串类型的星球 ID', async () => {
      (mockHttpClient.get as any).mockResolvedValue({});

      await dashboardRequest.getOverview('12345');

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/dashboard/groups/12345/overview');
    });
  });

  describe('getIncomes', () => {
    it('应该获取收入概览数据', async () => {
      const mockIncomes = {
        total_income: 50000,
        month_income: 5000,
        today_income: 200,
        member_fee: 30000,
        content_fee: 20000,
      };

      (mockHttpClient.get as any).mockResolvedValue(mockIncomes);

      const result = await dashboardRequest.getIncomes(12345);

      expect(result).toEqual(mockIncomes);
      expect(mockHttpClient.get).toHaveBeenCalledWith(
        '/v2/dashboard/groups/12345/incomes/overview',
      );
    });

    it('应该支持字符串类型的星球 ID', async () => {
      (mockHttpClient.get as any).mockResolvedValue({});

      await dashboardRequest.getIncomes('12345');

      expect(mockHttpClient.get).toHaveBeenCalledWith('/v2/dashboard/groups/12345/incomes/overview');
    });
  });

  describe('getScoreboardRanking', () => {
    it('应该获取积分排行榜', async () => {
      const mockRanking = [
        { user_id: 1, name: '用户1', score: 1000 },
        { user_id: 2, name: '用户2', score: 900 },
        { user_id: 3, name: '用户3', score: 800 },
      ];

      (mockHttpClient.get as any).mockResolvedValue({ ranking_list: mockRanking });

      const result = await dashboardRequest.getScoreboardRanking(12345);

      expect(result).toEqual(mockRanking);
      expect(mockHttpClient.get).toHaveBeenCalledWith(
        '/v2/dashboard/groups/12345/scoreboard/ranking_list',
        undefined,
      );
    });

    it('应该支持排行榜类型参数', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ ranking_list: [] });

      await dashboardRequest.getScoreboardRanking(12345, { type: 'continuous' });

      expect(mockHttpClient.get).toHaveBeenCalledWith(
        '/v2/dashboard/groups/12345/scoreboard/ranking_list',
        { type: 'continuous' },
      );
    });

    it('应该支持分页参数', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ ranking_list: [] });

      await dashboardRequest.getScoreboardRanking(12345, { index: 2 });

      expect(mockHttpClient.get).toHaveBeenCalledWith(
        '/v2/dashboard/groups/12345/scoreboard/ranking_list',
        { index: 2 },
      );
    });

    it('应该支持组合参数', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ ranking_list: [] });

      await dashboardRequest.getScoreboardRanking(12345, {
        type: 'accumulated',
        index: 3,
      });

      expect(mockHttpClient.get).toHaveBeenCalledWith(
        '/v2/dashboard/groups/12345/scoreboard/ranking_list',
        {
          type: 'accumulated',
          index: 3,
        },
      );
    });

    it('应该返回空数组当没有排行数据时', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ ranking_list: [] });

      const result = await dashboardRequest.getScoreboardRanking(12345);

      expect(result).toEqual([]);
    });

    it('应该支持字符串类型的星球 ID', async () => {
      (mockHttpClient.get as any).mockResolvedValue({ ranking_list: [] });

      await dashboardRequest.getScoreboardRanking('12345');

      expect(mockHttpClient.get).toHaveBeenCalledWith(
        '/v2/dashboard/groups/12345/scoreboard/ranking_list',
        undefined,
      );
    });
  });
});
