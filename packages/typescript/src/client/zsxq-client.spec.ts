import { describe, it, expect, beforeEach } from '@jest/globals';
import { ZsxqClient } from './zsxq-client';
import { ZsxqClientBuilder } from './zsxq-client-builder';

describe('ZsxqClient', () => {
  let client: ZsxqClient;

  beforeEach(() => {
    client = new ZsxqClientBuilder().setToken('test-token').build();
  });

  describe('模块初始化', () => {
    it('应该正确初始化所有模块', () => {
      expect(client.groups).toBeDefined();
      expect(client.topics).toBeDefined();
      expect(client.users).toBeDefined();
      expect(client.checkins).toBeDefined();
      expect(client.dashboard).toBeDefined();
    });
  });

  describe('模块类型检查', () => {
    it('groups 应该有正确的方法', () => {
      expect(typeof client.groups.list).toBe('function');
      expect(typeof client.groups.get).toBe('function');
      expect(typeof client.groups.getHashtags).toBe('function');
      expect(typeof client.groups.getStatistics).toBe('function');
      expect(typeof client.groups.getMember).toBe('function');
      expect(typeof client.groups.getUnreadCount).toBe('function');
    });

    it('topics 应该有正确的方法', () => {
      expect(typeof client.topics.list).toBe('function');
      expect(typeof client.topics.get).toBe('function');
      expect(typeof client.topics.getComments).toBe('function');
      expect(typeof client.topics.listByHashtag).toBe('function');
      expect(typeof client.topics.listByColumn).toBe('function');
    });

    it('users 应该有正确的方法', () => {
      expect(typeof client.users.self).toBe('function');
      expect(typeof client.users.get).toBe('function');
      expect(typeof client.users.getStatistics).toBe('function');
      expect(typeof client.users.getFootprints).toBe('function');
      expect(typeof client.users.getCreatedGroups).toBe('function');
    });

    it('checkins 应该有正确的方法', () => {
      expect(typeof client.checkins.list).toBe('function');
      expect(typeof client.checkins.get).toBe('function');
      expect(typeof client.checkins.getStatistics).toBe('function');
      expect(typeof client.checkins.getRankingList).toBe('function');
      expect(typeof client.checkins.getTopics).toBe('function');
    });

    it('dashboard 应该有正确的方法', () => {
      expect(typeof client.dashboard.getOverview).toBe('function');
      expect(typeof client.dashboard.getIncomes).toBe('function');
      expect(typeof client.dashboard.getScoreboardRanking).toBe('function');
    });
  });
});
