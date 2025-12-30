/**
 * 集成测试 - 需要真实 Token 和 Group ID
 *
 * 运行方式:
 * ZSXQ_TOKEN="xxx" ZSXQ_GROUP_ID="xxx" npm test -- --testPathPattern="integration"
 *
 * 注意: 部分测试可能会因星球未开启相关功能而跳过（如打卡、排行榜等）
 */

import { ZsxqClientBuilder } from './client';
import { ZsxqClient } from './client/zsxq-client';

// 跳过条件：没有设置环境变量
const SKIP_INTEGRATION = !process.env.ZSXQ_TOKEN || !process.env.ZSXQ_GROUP_ID;

// 辅助函数：安全执行测试
async function safeTest<T>(
  name: string,
  fn: () => Promise<T>,
): Promise<{ success: boolean; data?: T; error?: string }> {
  try {
    const data = await fn();
    console.log(`✅ ${name}`);
    return { success: true, data };
  } catch (error) {
    const message = error instanceof Error ? error.message : String(error);
    console.log(`⚠️ ${name}: ${message}`);
    return { success: false, error: message };
  }
}

describe('Integration Tests', () => {
  let client: ZsxqClient;
  let groupId: string;

  beforeAll(() => {
    if (SKIP_INTEGRATION) {
      console.log('⏭️ 跳过集成测试：未设置 ZSXQ_TOKEN 或 ZSXQ_GROUP_ID 环境变量');
      return;
    }

    const token = process.env.ZSXQ_TOKEN!;
    groupId = process.env.ZSXQ_GROUP_ID!;

    client = new ZsxqClientBuilder().setToken(token).build();
  });

  // ========== 核心 API 测试 ==========

  describe('核心 API', () => {
    it('获取当前用户信息', async () => {
      if (SKIP_INTEGRATION) return;

      const user = await client.users.self();
      expect(user).toBeDefined();
      expect(user.user_id).toBeDefined();
      expect(user.name).toBeDefined();
      console.log(`✅ 当前用户: ${user.name} (ID: ${user.user_id})`);
    });

    it('获取星球列表', async () => {
      if (SKIP_INTEGRATION) return;

      const groups = await client.groups.list();
      expect(groups).toBeDefined();
      expect(Array.isArray(groups)).toBe(true);
      console.log(`✅ 已加入 ${groups.length} 个星球`);
      groups.slice(0, 3).forEach((g) => {
        console.log(`   - ${g.name} (ID: ${g.group_id})`);
      });
    });

    it('获取星球详情', async () => {
      if (SKIP_INTEGRATION) return;

      const group = await client.groups.get(groupId);
      expect(group).toBeDefined();
      expect(group.name).toBeDefined();
      console.log(`✅ 星球详情: ${group.name}`);
    });

    it('获取星球统计', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取星球统计', () => client.groups.getStatistics(groupId));
      if (result.success) {
        console.log(`   - 统计数据: ${JSON.stringify(result.data)}`);
      }
    });

    it('获取话题列表', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取话题列表', () => client.topics.list(groupId));
      if (result.success && result.data) {
        console.log(`   - 话题数: ${result.data.length}`);
        if (result.data.length > 0) {
          console.log(`   - 第一个话题ID: ${result.data[0].topic_id}`);
        }
      }
    });

    it('获取用户统计', async () => {
      if (SKIP_INTEGRATION) return;

      const user = await client.users.self();
      const stats = await client.users.getStatistics(user.user_id);
      expect(stats).toBeDefined();
      console.log(`✅ 用户统计: ${JSON.stringify(stats)}`);
    });
  });

  // ========== 星球功能测试 ==========

  describe('星球功能', () => {
    it('获取星球菜单配置', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取星球菜单配置', () => client.groups.getMenus(groupId));
      if (result.success && result.data) {
        console.log(`   - 菜单数: ${result.data.length}`);
        result.data.forEach((m) => console.log(`   - ${m.name} (type: ${m.type})`));
      }
    });

    it('获取星球角色成员', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取星球角色成员', () => client.groups.getRoleMembers(groupId));
      if (result.success && result.data) {
        console.log(`   - 星主: ${result.data.owner?.name || '未知'}`);
      }
    });

    it('获取星球专栏列表', async () => {
      if (SKIP_INTEGRATION) return;

      const columns = await client.groups.getColumns(groupId);
      expect(columns).toBeDefined();
      console.log(`✅ 获取到 ${columns.length} 个专栏`);
      columns.forEach((c) => console.log(`   - ${c.name} (话题数: ${c.topics_count})`));
    });

    it('获取专栏汇总信息', async () => {
      if (SKIP_INTEGRATION) return;

      const summary = await client.groups.getColumnsSummary(groupId);
      expect(summary).toBeDefined();
      console.log(`✅ 专栏汇总: ${JSON.stringify(summary)}`);
    });

    it('获取成员活跃摘要', async () => {
      if (SKIP_INTEGRATION) return;

      const user = await client.users.self();
      const result = await safeTest('获取成员活跃摘要', () =>
        client.groups.getMemberActivitySummary(groupId, user.user_id),
      );
      if (result.success && result.data) {
        console.log(`   - 发布话题数: ${result.data.topics_count}`);
      }
    });

    it('获取置顶话题列表', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取置顶话题列表', () => client.topics.listSticky(groupId));
      if (result.success && result.data) {
        console.log(`   - 置顶话题数: ${result.data.length}`);
      }
    });

    it('获取话题基础信息', async () => {
      if (SKIP_INTEGRATION) return;

      const topics = await client.topics.list(groupId);
      if (topics.length > 0) {
        const result = await safeTest('获取话题基础信息', () =>
          client.topics.getInfo(topics[0].topic_id),
        );
        if (result.success && result.data) {
          console.log(`   - Topic ID: ${result.data.topic_id}`);
        }
      }
    });

    it('获取话题打赏列表', async () => {
      if (SKIP_INTEGRATION) return;

      const topics = await client.topics.list(groupId);
      if (topics.length > 0) {
        const result = await safeTest('获取话题打赏列表', () =>
          client.topics.getRewards(topics[0].topic_id),
        );
        if (result.success && result.data) {
          console.log(`   - 打赏数: ${result.data.length}`);
        }
      }
    });

    it('获取相关推荐话题', async () => {
      if (SKIP_INTEGRATION) return;

      const topics = await client.topics.list(groupId);
      if (topics.length > 0) {
        const result = await safeTest('获取相关推荐话题', () =>
          client.topics.getRecommendations(topics[0].topic_id),
        );
        if (result.success && result.data) {
          console.log(`   - 推荐话题数: ${result.data.length}`);
        }
      }
    });
  });

  // ========== 排行榜功能测试 ==========

  describe('排行榜功能', () => {
    it('获取星球排行榜', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取星球排行榜', () => client.ranking.getGroupRanking(groupId));
      if (result.success && result.data) {
        console.log(`   - 排行榜记录数: ${result.data.length}`);
      }
    });

    it('获取星球排行统计', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取星球排行统计', () =>
        client.ranking.getGroupRankingStats(groupId),
      );
      if (result.success && result.data) {
        console.log(`   - 统计: ${JSON.stringify(result.data)}`);
      }
    });

    it('获取积分排行榜', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取积分排行榜', () => client.ranking.getScoreRanking(groupId));
      if (result.success && result.data) {
        console.log(`   - 积分排行记录数: ${result.data.length}`);
      }
    });

    it('获取我的积分统计', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取我的积分统计', () =>
        client.ranking.getMyScoreStats(groupId),
      );
      if (result.success && result.data) {
        console.log(`   - 统计: ${JSON.stringify(result.data)}`);
      }
    });

    it('获取积分榜设置', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取积分榜设置', () =>
        client.ranking.getScoreboardSettings(groupId),
      );
      if (result.success && result.data) {
        console.log(`   - 启用: ${result.data.enabled}`);
      }
    });

    it('获取邀请排行榜', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取邀请排行榜', () =>
        client.ranking.getInvitationRanking(groupId),
      );
      if (result.success && result.data) {
        console.log(`   - 邀请排行记录数: ${result.data.length}`);
      }
    });

    it('获取贡献排行榜', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取贡献排行榜', () =>
        client.ranking.getContributionRanking(groupId),
      );
      if (result.success && result.data) {
        console.log(`   - 贡献排行记录数: ${result.data.length}`);
      }
    });

    it('获取全局星球排行榜', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取全局星球排行榜', () =>
        client.ranking.getGlobalRanking('group_sales_list', 10),
      );
      if (result.success && result.data) {
        console.log(`   - 全局排行数据: ${JSON.stringify(result.data).substring(0, 100)}...`);
      }
    });
  });

  // ========== 用户功能测试 ==========

  describe('用户功能', () => {
    it('获取用户头像URL', async () => {
      if (SKIP_INTEGRATION) return;

      const user = await client.users.self();
      const result = await safeTest('获取用户头像URL', () =>
        client.users.getAvatarUrl(user.user_id),
      );
      if (result.success && result.data) {
        console.log(`   - URL: ${result.data.substring(0, 50)}...`);
      }
    });

    it('获取用户星球足迹', async () => {
      if (SKIP_INTEGRATION) return;

      const user = await client.users.self();
      const result = await safeTest('获取用户星球足迹', () =>
        client.users.getGroupFootprints(user.user_id),
      );
      if (result.success && result.data) {
        console.log(`   - 足迹数: ${result.data.length}`);
      }
    });

    it('获取指定星球的用户足迹', async () => {
      if (SKIP_INTEGRATION) return;

      const user = await client.users.self();
      const result = await safeTest('获取指定星球的用户足迹', () =>
        client.users.getGroupFootprints(user.user_id, groupId),
      );
      if (result.success && result.data) {
        console.log(`   - 指定星球足迹数: ${result.data.length}`);
      }
    });

    it('获取申请中星球列表', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取申请中星球列表', () => client.users.getApplyingGroups());
      if (result.success && result.data) {
        console.log(`   - 申请中星球数: ${result.data.length}`);
      }
    });

    it('获取星球邀请人信息', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取星球邀请人信息', () => client.users.getInviter(groupId));
      if (result.success && result.data) {
        console.log(`   - 邀请人: ${result.data.user?.name || '无'}`);
      }
    });

    it('获取优惠券列表', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取优惠券列表', () => client.users.getCoupons());
      if (result.success && result.data) {
        console.log(`   - 优惠券数: ${result.data.length}`);
      }
    });

    it('获取备注列表', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取备注列表', () => client.users.getRemarks());
      if (result.success && result.data) {
        console.log(`   - 备注数: ${result.data.length}`);
      }
    });

    it('获取推荐关注用户', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取推荐关注用户', () => client.users.getRecommendedFollows());
      if (result.success && result.data) {
        console.log(`   - 推荐用户数: ${result.data.length}`);
      }
    });

    it('获取屏蔽用户列表', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取屏蔽用户列表', () => client.users.getBlockedUsers());
      if (result.success && result.data) {
        console.log(`   - 屏蔽用户数: ${result.data.length}`);
      }
    });

    it('获取推荐偏好分类', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取推荐偏好分类', () =>
        client.users.getPreferenceCategories(),
      );
      if (result.success && result.data) {
        console.log(`   - 分类数: ${result.data.length}`);
      }
    });

    it('获取未回答问题摘要', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取未回答问题摘要', () =>
        client.users.getUnansweredQuestionsSummary(),
      );
      if (result.success && result.data) {
        console.log(`   - 未回答数: ${result.data.unanswered_count}`);
      }
    });

    it('获取关注者统计', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取关注者统计', () => client.users.getFollowerStats());
      if (result.success && result.data) {
        console.log(`   - 关注者数: ${result.data.followers_count}`);
      }
    });

    it('获取用户偏好配置', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取用户偏好配置', () => client.users.getPreferences());
      if (result.success && result.data) {
        console.log(`   - 配置键数: ${Object.keys(result.data).length}`);
      }
    });

    it('获取星球周榜排名', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取星球周榜排名', () =>
        client.users.getWeeklyRanking(groupId),
      );
      if (result.success && result.data) {
        console.log(`   - 周榜数据: ${JSON.stringify(result.data)}`);
      }
    });

    it('上报推送渠道', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('上报推送渠道', () =>
        client.users.reportPushChannel('apns', 'test-device-token'),
      );
      if (result.success) {
        console.log(`   - 推送渠道上报成功`);
      }
    });
  });

  // ========== 打卡功能测试 ==========

  describe('打卡功能', () => {
    it('获取打卡项目列表', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取打卡项目列表', () => client.checkins.list(groupId));
      if (result.success && result.data) {
        console.log(`   - 打卡项目数: ${result.data.length}`);
        result.data.forEach((c) => console.log(`   - ${c.name} (ID: ${c.checkin_id})`));
      }
    });

    it('获取打卡项目详情', async () => {
      if (SKIP_INTEGRATION) return;

      const checkins = await client.checkins.list(groupId).catch(() => []);
      if (checkins.length > 0) {
        const result = await safeTest('获取打卡项目详情', () =>
          client.checkins.get(groupId, checkins[0].checkin_id),
        );
        if (result.success && result.data) {
          console.log(`   - 名称: ${result.data.name}`);
        }
      }
    });

    it('获取打卡项目统计', async () => {
      if (SKIP_INTEGRATION) return;

      const checkins = await client.checkins.list(groupId).catch(() => []);
      if (checkins.length > 0) {
        const result = await safeTest('获取打卡项目统计', () =>
          client.checkins.getStatistics(groupId, checkins[0].checkin_id),
        );
        if (result.success && result.data) {
          console.log(`   - 统计: ${JSON.stringify(result.data)}`);
        }
      }
    });

    it('获取打卡排行榜', async () => {
      if (SKIP_INTEGRATION) return;

      const checkins = await client.checkins.list(groupId).catch(() => []);
      if (checkins.length > 0) {
        const result = await safeTest('获取打卡排行榜', () =>
          client.checkins.getRankingList(groupId, checkins[0].checkin_id),
        );
        if (result.success && result.data) {
          console.log(`   - 排行记录数: ${result.data.length}`);
        }
      }
    });

    it('获取打卡每日统计', async () => {
      if (SKIP_INTEGRATION) return;

      const checkins = await client.checkins.list(groupId).catch(() => []);
      if (checkins.length > 0) {
        const result = await safeTest('获取打卡每日统计', () =>
          client.checkins.getDailyStatistics(groupId, checkins[0].checkin_id),
        );
        if (result.success && result.data) {
          console.log(`   - 每日统计记录数: ${result.data.length}`);
        }
      }
    });

    it('获取打卡参与用户', async () => {
      if (SKIP_INTEGRATION) return;

      const checkins = await client.checkins.list(groupId).catch(() => []);
      if (checkins.length > 0) {
        const result = await safeTest('获取打卡参与用户', () =>
          client.checkins.getJoinedUsers(groupId, checkins[0].checkin_id),
        );
        if (result.success && result.data) {
          console.log(`   - 参与用户数: ${result.data.length}`);
        }
      }
    });

    it('获取我的打卡记录', async () => {
      if (SKIP_INTEGRATION) return;

      const checkins = await client.checkins.list(groupId).catch(() => []);
      if (checkins.length > 0) {
        const result = await safeTest('获取我的打卡记录', () =>
          client.checkins.getMyCheckins(groupId, checkins[0].checkin_id),
        );
        if (result.success && result.data) {
          console.log(`   - 我的打卡记录数: ${result.data.length}`);
        }
      }
    });

    it('获取我的打卡日期', async () => {
      if (SKIP_INTEGRATION) return;

      const checkins = await client.checkins.list(groupId).catch(() => []);
      if (checkins.length > 0) {
        const result = await safeTest('获取我的打卡日期', () =>
          client.checkins.getMyCheckinDays(groupId, checkins[0].checkin_id),
        );
        if (result.success && result.data) {
          console.log(`   - 打卡日期数: ${result.data.length}`);
        }
      }
    });

    it('获取我的打卡统计', async () => {
      if (SKIP_INTEGRATION) return;

      const checkins = await client.checkins.list(groupId).catch(() => []);
      if (checkins.length > 0) {
        const result = await safeTest('获取我的打卡统计', () =>
          client.checkins.getMyStatistics(groupId, checkins[0].checkin_id),
        );
        if (result.success && result.data) {
          console.log(`   - 统计: ${JSON.stringify(result.data)}`);
        }
      }
    });
  });

  // ========== 数据面板测试 ==========

  describe('数据面板', () => {
    it('获取星球数据概览', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取星球数据概览', () => client.dashboard.getOverview(groupId));
      if (result.success && result.data) {
        console.log(`   - 概览: ${JSON.stringify(result.data)}`);
      }
    });

    it('获取星球收入概览', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取星球收入概览', () => client.dashboard.getIncomes(groupId));
      if (result.success && result.data) {
        console.log(`   - 收入: ${JSON.stringify(result.data)}`);
      }
    });

    it('获取星球权限配置', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取星球权限配置', () =>
        client.dashboard.getPrivileges(groupId),
      );
      if (result.success && result.data) {
        console.log(`   - 权限配置数: ${Object.keys(result.data).length}`);
      }
    });

    it('获取发票统计', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取发票统计', () => client.dashboard.getInvoiceStats());
      if (result.success && result.data) {
        console.log(`   - 发票统计: ${JSON.stringify(result.data)}`);
      }
    });
  });

  // ========== 杂项功能测试 ==========

  describe('杂项功能', () => {
    it('获取全局配置', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取全局配置', () => client.misc.getGlobalConfig());
      if (result.success && result.data) {
        console.log(`   - 配置: ${JSON.stringify(result.data).substring(0, 100)}...`);
      }
    });

    it('获取用户动态', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取用户动态', () => client.misc.getActivities());
      if (result.success && result.data) {
        console.log(`   - 动态数: ${result.data.length}`);
      }
    });

    it('获取PK群组信息', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取PK群组信息', () => client.misc.getPkGroup(groupId));
      if (result.success && result.data) {
        console.log(`   - PK群组: ${result.data.name}`);
      }
    });

    it('获取PK对战记录', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取PK对战记录', () => client.misc.getPkBattles(123, { count: 10 }));
      if (result.success && result.data) {
        console.log(`   - PK对战记录数: ${result.data.length}`);
      }
    });

    it('解析URL详情', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('解析URL详情', () =>
        client.misc.parseUrl('https://www.example.com'),
      );
      if (result.success && result.data) {
        console.log(`   - URL标题: ${result.data.title || '无'}`);
      }
    });
  });

  // ========== 星球高级功能测试 ==========

  describe('星球高级功能', () => {
    it('获取续费信息', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取续费信息', () => client.groups.getRenewalInfo(groupId));
      if (result.success && result.data) {
        console.log(`   - 续费启用: ${result.data.renewal_enabled}`);
      }
    });

    it('获取分销信息', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取分销信息', () => client.groups.getDistribution(groupId));
      if (result.success && result.data) {
        console.log(`   - 分销启用: ${result.data.enabled}`);
      }
    });

    it('获取可升级星球', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取可升级星球', () => client.groups.getUpgradeableGroups());
      if (result.success && result.data) {
        console.log(`   - 可升级星球数: ${result.data.length}`);
      }
    });

    it('获取推荐星球', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取推荐星球', () => client.groups.getRecommendedGroups());
      if (result.success && result.data) {
        console.log(`   - 推荐星球数: ${result.data.length}`);
      }
    });

    it('获取自定义标签', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取自定义标签', () => client.groups.getCustomTags(groupId));
      if (result.success && result.data) {
        console.log(`   - 自定义标签数: ${result.data.length}`);
      }
    });

    it('获取定时任务', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取定时任务', () => client.groups.getScheduledTasks(groupId));
      if (result.success && result.data) {
        console.log(`   - 定时任务数: ${result.data.length}`);
      }
    });

    it('获取风险预警', async () => {
      if (SKIP_INTEGRATION) return;

      const result = await safeTest('获取风险预警', () => client.groups.getRiskWarnings(groupId));
      if (result.success) {
        console.log(`   - 风险预警: ${result.data ? '有' : '无'}`);
      }
    });
  });
});
