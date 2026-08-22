import { RankingRequest } from './index';
import { HttpClient } from '../http';

jest.mock('../http');

describe('RankingRequest', () => {
  let rankingRequest: RankingRequest;
  let mockHttpClient: jest.Mocked<HttpClient>;

  beforeEach(() => {
    mockHttpClient = new HttpClient({} as any) as jest.Mocked<HttpClient>;
    rankingRequest = new RankingRequest(mockHttpClient);
  });

  describe('getInvitationRanking', () => {
    it('解析 App 返回的 member/rankings/invitees_count', async () => {
      mockHttpClient.get = jest.fn().mockResolvedValue({
        ranking_list: [
          {
            member: {
              user_id: 15514225885222,
              name: '华峰（兄）',
              avatar_url: 'https://images.zsxq.com/a.jpg',
              number: 120,
            },
            rankings: 1,
            invitees_count: 8,
          },
        ],
      });

      const result = await rankingRequest.getInvitationRanking(123);

      expect(mockHttpClient.get).toHaveBeenCalledWith(
        '/v2/groups/123/invitations/ranking_list',
        undefined,
      );
      expect(result).toHaveLength(1);
      expect(result[0].rankings).toBe(1);
      expect(result[0].invitees_count).toBe(8);
      expect(result[0].member.user_id).toBe(15514225885222);
      expect(result[0].member.number).toBe(120);
    });

    it('传入 App 实际查询参数 begin_time/count/with_extra', async () => {
      mockHttpClient.get = jest.fn().mockResolvedValue({ ranking_list: [] });

      const result = await rankingRequest.getInvitationRanking(15552841255452, {
        begin_time: '2026-08-17T00:00:00.000+0800',
        count: 10,
        with_extra: true,
      });

      expect(mockHttpClient.get).toHaveBeenCalledWith(
        '/v2/groups/15552841255452/invitations/ranking_list',
        {
          begin_time: '2026-08-17T00:00:00.000+0800',
          count: 10,
          with_extra: true,
        },
      );
      expect(result).toEqual([]);
    });

    it('自定义区间可额外传 end_time', async () => {
      mockHttpClient.get = jest.fn().mockResolvedValue({ ranking_list: [] });

      await rankingRequest.getInvitationRanking(15552841255452, {
        begin_time: '2026-08-22T00:00:00.000+0800',
        end_time: '2026-08-22T23:59:00.000+0800',
        count: 10,
        with_extra: true,
      });

      expect(mockHttpClient.get).toHaveBeenCalledWith(
        '/v2/groups/15552841255452/invitations/ranking_list',
        {
          begin_time: '2026-08-22T00:00:00.000+0800',
          end_time: '2026-08-22T23:59:00.000+0800',
          count: 10,
          with_extra: true,
        },
      );
    });
  });
});
