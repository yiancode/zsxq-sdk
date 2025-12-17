import { HttpClient } from '../http';
import {
  Group,
  User,
  Topic,
  Checkin,
  Comment,
  Hashtag,
  RankingItem,
  CheckinStatistics,
  Menu,
  RoleMembers,
  Column,
  ActivitySummary,
  RenewalInfo,
  DistributionInfo,
  CustomTag,
  ScheduledJob,
  GroupWarning,
  Reward,
  Inviter,
  Coupon,
  Remark,
  PreferenceCategory,
  UnansweredQuestionsSummary,
  FollowerStatistics,
  Contribution,
  ContributionStatistics,
  AchievementSummary,
  WeeklyRanking,
  DailyStatistics,
  MyCheckinStatistics,
  RankingStatistics,
  ScoreboardSettings,
  InvoiceStats,
  GlobalConfig,
  Activity,
  PkGroup,
} from '../model';

/**
 * 请求基类
 */
export abstract class BaseRequest {
  protected readonly httpClient: HttpClient;

  constructor(httpClient: HttpClient) {
    this.httpClient = httpClient;
  }
}

/**
 * 话题列表查询参数
 */
export interface ListTopicsOptions {
  /** 返回数量 */
  count?: number;
  /** 查询范围: all/digests/by_owner */
  scope?: 'all' | 'digests' | 'by_owner';
  /** 分页方向 */
  direction?: 'forward' | 'backward';
  /** 开始时间 (ISO 8601) */
  begin_time?: string;
  /** 结束时间 (ISO 8601) */
  end_time?: string;
  /** 是否包含隐藏话题 */
  with_invisibles?: boolean;
}

/**
 * 打卡列表查询参数
 */
export interface ListCheckinsOptions {
  /** 状态: ongoing/closed/over */
  scope?: 'ongoing' | 'closed' | 'over';
  /** 返回数量 */
  count?: number;
}

/**
 * 评论列表查询参数
 */
export interface ListCommentsOptions {
  /** 返回数量 */
  count?: number;
  /** 排序: asc/desc */
  sort?: 'asc' | 'desc';
  /** 是否包含置顶 */
  with_sticky?: boolean;
}

/**
 * 排行榜查询参数
 */
export interface ListRankingOptions {
  /** 类型: continuous/accumulated */
  type?: 'continuous' | 'accumulated';
  /** 分页索引 */
  index?: number;
}

/**
 * 参与用户查询参数
 */
export interface JoinedUsersOptions {
  /** 返回数量 */
  count?: number;
  /** 结束时间 */
  end_time?: string;
}

/**
 * 我的打卡记录查询参数
 */
export interface MyCheckinsOptions {
  /** 返回数量 */
  count?: number;
  /** 结束时间 */
  end_time?: string;
}

/**
 * 打卡项目有效期配置
 */
export interface CheckinValidity {
  /** 是否长期有效 */
  long_period?: boolean;
  /** 截止时间 (ISO 8601 格式，如 "2025-12-31T23:59:59.000+0800") */
  expiration_time?: string;
}

/**
 * 创建打卡项目参数
 *
 * 基于实际 API 结构:
 * {
 *   "req_data": {
 *     "title": "训练营标题",
 *     "text": "训练营描述",
 *     "checkin_days": 7,
 *     "type": "accumulated",
 *     "show_topics_on_timeline": false,
 *     "validity": {
 *       "long_period": false,
 *       "expiration_time": "2025-12-24T23:59:59.798+0800"
 *     }
 *   }
 * }
 */
export interface CreateCheckinParams {
  /** 训练营标题 */
  title: string;
  /** 训练营描述 */
  text?: string;
  /** 打卡天数 */
  checkin_days: number;
  /** 打卡类型: accumulated(累计打卡) / continuous(连续打卡) */
  type: 'accumulated' | 'continuous';
  /** 是否在时间线展示 */
  show_topics_on_timeline?: boolean;
  /** 有效期配置 */
  validity?: CheckinValidity;
}

/**
 * 更新打卡项目参数
 */
export interface UpdateCheckinParams {
  /** 训练营标题 */
  title?: string;
  /** 训练营描述 */
  text?: string;
  /** 打卡天数 */
  checkin_days?: number;
  /** 打卡类型 */
  type?: 'accumulated' | 'continuous';
  /** 是否在时间线展示 */
  show_topics_on_timeline?: boolean;
  /** 有效期配置 */
  validity?: CheckinValidity;
  /** 打卡状态 */
  status?: string;
}

/**
 * 星球请求模块
 */
export class GroupsRequest extends BaseRequest {
  /**
   * 获取我的星球列表
   */
  async list(): Promise<Group[]> {
    const data = await this.httpClient.get<{ groups: Group[] }>('/v2/groups');
    return data.groups;
  }

  /**
   * 获取星球详情
   */
  async get(groupId: number | string): Promise<Group> {
    const data = await this.httpClient.get<{ group: Group }>(`/v2/groups/${groupId}`);
    return data.group;
  }

  /**
   * 获取星球标签
   */
  async getHashtags(groupId: number | string): Promise<Hashtag[]> {
    const data = await this.httpClient.get<{ hashtags: Hashtag[] }>(
      `/v2/groups/${groupId}/hashtags`,
    );
    return data.hashtags;
  }

  /**
   * 获取星球统计
   */
  async getStatistics(groupId: number | string): Promise<Record<string, unknown>> {
    return this.httpClient.get(`/v2/groups/${groupId}/statistics`);
  }

  /**
   * 获取成员信息
   */
  async getMember(groupId: number | string, memberId: number | string): Promise<User> {
    const data = await this.httpClient.get<{ user: User }>(
      `/v2/groups/${groupId}/members/${memberId}`,
    );
    return data.user;
  }

  /**
   * 获取未读话题数
   */
  async getUnreadCount(): Promise<Record<string, number>> {
    return this.httpClient.get('/v2/groups/unread_topics_count');
  }

  /**
   * 获取星球菜单配置
   */
  async getMenus(groupId: number | string): Promise<Menu[]> {
    const data = await this.httpClient.get<{ menus: Menu[] }>(`/v2/groups/${groupId}/menus`);
    return data.menus;
  }

  /**
   * 获取星球角色成员（星主、合伙人、管理员）
   */
  async getRoleMembers(groupId: number | string): Promise<RoleMembers> {
    return this.httpClient.get<RoleMembers>(`/v2/groups/${groupId}/role_members`);
  }

  /**
   * 获取星球专栏列表
   */
  async getColumns(groupId: number | string): Promise<Column[]> {
    const data = await this.httpClient.get<{ columns: Column[] }>(`/v2/groups/${groupId}/columns`);
    return data.columns;
  }

  /**
   * 获取专栏汇总信息
   */
  async getColumnsSummary(groupId: number | string): Promise<Record<string, unknown>> {
    return this.httpClient.get(`/v2/groups/${groupId}/columns/summary`);
  }

  /**
   * 获取成员活跃摘要
   */
  async getMemberActivitySummary(
    groupId: number | string,
    memberId: number | string,
  ): Promise<ActivitySummary> {
    const data = await this.httpClient.get<{ summary: ActivitySummary }>(
      `/v2/groups/${groupId}/members/${memberId}/summary`,
    );
    return data.summary;
  }

  /**
   * 获取星球续费信息
   */
  async getRenewalInfo(groupId: number | string): Promise<RenewalInfo> {
    const data = await this.httpClient.get<{ renewal: RenewalInfo }>(
      `/v2/groups/${groupId}/renewal`,
    );
    return data.renewal || (data as unknown as RenewalInfo);
  }

  /**
   * 获取星球分销信息
   */
  async getDistribution(groupId: number | string): Promise<DistributionInfo> {
    const data = await this.httpClient.get<{ distribution: DistributionInfo }>(
      `/v2/groups/${groupId}/distribution`,
    );
    return data.distribution || (data as unknown as DistributionInfo);
  }

  /**
   * 获取可升级星球列表
   */
  async getUpgradeableGroups(): Promise<Group[]> {
    const data = await this.httpClient.get<{ groups: Group[] }>('/v2/groups/upgradable_groups');
    return data.groups;
  }

  /**
   * 获取推荐星球列表
   */
  async getRecommendedGroups(): Promise<Group[]> {
    const data = await this.httpClient.get<{ groups: Group[] }>('/v2/groups/recommendations');
    return data.groups;
  }

  /**
   * 获取星球自定义标签
   */
  async getCustomTags(groupId: number | string): Promise<CustomTag[]> {
    const data = await this.httpClient.get<{ labels: CustomTag[] }>(
      `/v2/groups/${groupId}/labels`,
    );
    return data.labels;
  }

  /**
   * 获取星球定时任务
   */
  async getScheduledTasks(groupId: number | string): Promise<ScheduledJob[]> {
    const data = await this.httpClient.get<{ jobs: ScheduledJob[] }>(
      `/v2/groups/${groupId}/scheduled_jobs`,
    );
    return data.jobs;
  }

  /**
   * 获取星球风险预警
   */
  async getRiskWarnings(groupId: number | string): Promise<GroupWarning> {
    const data = await this.httpClient.get<{ warning: GroupWarning }>(
      `/v3/groups/${groupId}/group_warning`,
    );
    return data.warning || (data as unknown as GroupWarning);
  }
}

/**
 * 话题请求模块
 */
export class TopicsRequest extends BaseRequest {
  /**
   * 获取话题列表
   */
  async list(groupId: number | string, options?: ListTopicsOptions): Promise<Topic[]> {
    const data = await this.httpClient.get<{ topics: Topic[] }>(
      `/v2/groups/${groupId}/topics`,
      options as Record<string, unknown>,
    );
    return data.topics;
  }

  /**
   * 获取话题详情
   */
  async get(topicId: number | string): Promise<Topic> {
    const data = await this.httpClient.get<{ topic: Topic }>(`/v2/topics/${topicId}`);
    return data.topic;
  }

  /**
   * 获取话题评论
   */
  async getComments(topicId: number | string, options?: ListCommentsOptions): Promise<Comment[]> {
    const data = await this.httpClient.get<{ comments: Comment[] }>(
      `/v2/topics/${topicId}/comments`,
      options as Record<string, unknown>,
    );
    return data.comments;
  }

  /**
   * 按标签获取话题
   */
  async listByHashtag(hashtagId: number | string, options?: ListTopicsOptions): Promise<Topic[]> {
    const data = await this.httpClient.get<{ topics: Topic[] }>(
      `/v2/hashtags/${hashtagId}/topics`,
      options as Record<string, unknown>,
    );
    return data.topics;
  }

  /**
   * 按专栏获取话题
   */
  async listByColumn(
    groupId: number | string,
    columnId: number | string,
    options?: ListTopicsOptions,
  ): Promise<Topic[]> {
    const data = await this.httpClient.get<{ topics: Topic[] }>(
      `/v2/groups/${groupId}/columns/${columnId}/topics`,
      options as Record<string, unknown>,
    );
    return data.topics;
  }

  /**
   * 获取置顶话题列表
   */
  async listSticky(groupId: number | string): Promise<Topic[]> {
    const data = await this.httpClient.get<{ topics: Topic[] }>(
      `/v2/groups/${groupId}/topics/sticky`,
    );
    return data.topics;
  }

  /**
   * 获取话题基础信息（轻量级）
   */
  async getInfo(topicId: number | string): Promise<Topic> {
    const data = await this.httpClient.get<{ topic: Topic }>(`/v2/topics/${topicId}/info`);
    return data.topic;
  }

  /**
   * 获取话题打赏列表
   */
  async getRewards(topicId: number | string): Promise<Reward[]> {
    const data = await this.httpClient.get<{ rewards: Reward[] }>(
      `/v2/topics/${topicId}/rewards`,
    );
    return data.rewards;
  }

  /**
   * 获取相关推荐话题
   */
  async getRecommendations(topicId: number | string): Promise<Topic[]> {
    const data = await this.httpClient.get<{ topics: Topic[] }>(
      `/v2/topics/${topicId}/recommendations`,
    );
    return data.topics;
  }
}

/**
 * 用户请求模块
 */
export class UsersRequest extends BaseRequest {
  /**
   * 获取当前用户信息
   */
  async self(): Promise<User> {
    const data = await this.httpClient.get<{ user: User }>('/v3/users/self');
    return data.user;
  }

  /**
   * 获取指定用户信息
   */
  async get(userId: number | string): Promise<User> {
    const data = await this.httpClient.get<{ user: User }>(`/v3/users/${userId}`);
    return data.user;
  }

  /**
   * 获取用户统计
   */
  async getStatistics(userId: number | string): Promise<Record<string, unknown>> {
    return this.httpClient.get(`/v3/users/${userId}/statistics`);
  }

  /**
   * 获取用户动态
   */
  async getFootprints(userId: number | string): Promise<Topic[]> {
    const data = await this.httpClient.get<{ topics: Topic[] }>(
      `/v2/users/${userId}/footprints`,
    );
    return data.topics;
  }

  /**
   * 获取用户创建的星球
   */
  async getCreatedGroups(userId: number | string): Promise<Group[]> {
    const data = await this.httpClient.get<{ groups: Group[] }>(
      `/v2/users/${userId}/created_groups`,
    );
    return data.groups;
  }

  /**
   * 获取用户大尺寸头像URL
   */
  async getAvatarUrl(userId: number | string): Promise<string> {
    const data = await this.httpClient.get<{ avatar_url: string }>(
      `/v3/users/${userId}/avatar_url`,
    );
    return data.avatar_url;
  }

  /**
   * 获取用户星球足迹
   */
  async getGroupFootprints(userId: number | string): Promise<Group[]> {
    const data = await this.httpClient.get<{ groups: Group[] }>(
      `/v2/users/${userId}/group_footprints`,
    );
    return data.groups;
  }

  /**
   * 获取申请中的星球列表
   */
  async getApplyingGroups(): Promise<Group[]> {
    const data = await this.httpClient.get<{ groups: Group[] }>('/v2/groups/applying');
    return data.groups;
  }

  /**
   * 获取星球邀请人信息
   */
  async getInviter(groupId: number | string): Promise<Inviter> {
    const data = await this.httpClient.get<{ inviter: Inviter }>(
      `/v2/groups/${groupId}/inviter`,
    );
    return data.inviter;
  }

  /**
   * 获取我的优惠券列表
   */
  async getCoupons(): Promise<Coupon[]> {
    const data = await this.httpClient.get<{ coupons: Coupon[] }>('/v2/coupons');
    return data.coupons;
  }

  /**
   * 获取我的备注列表
   */
  async getRemarks(): Promise<Remark[]> {
    const data = await this.httpClient.get<{ remarks: Remark[] }>('/v2/remarks');
    return data.remarks;
  }

  /**
   * 获取推荐关注用户列表
   */
  async getRecommendedFollows(): Promise<User[]> {
    const data = await this.httpClient.get<{ users: User[] }>('/v2/users/recommended_follows');
    return data.users;
  }

  /**
   * 获取屏蔽用户列表
   */
  async getBlockedUsers(): Promise<User[]> {
    const data = await this.httpClient.get<{ users: User[] }>('/v2/users/block_users');
    return data.users;
  }

  /**
   * 获取推荐偏好分类
   */
  async getPreferenceCategories(): Promise<PreferenceCategory[]> {
    const data = await this.httpClient.get<{ categories: PreferenceCategory[] }>(
      '/v2/users/self/recommendations/preference_categories',
    );
    return data.categories;
  }

  /**
   * 获取未回答问题摘要
   */
  async getUnansweredQuestionsSummary(): Promise<UnansweredQuestionsSummary> {
    return this.httpClient.get<UnansweredQuestionsSummary>(
      '/v2/users/self/unanswered_questions/brief',
    );
  }

  /**
   * 获取关注者统计
   */
  async getFollowerStats(beginTime?: string): Promise<FollowerStatistics> {
    const url = beginTime
      ? `/v3/users/self/followers/statistics?begin_time=${beginTime}`
      : '/v3/users/self/followers/statistics';
    return this.httpClient.get<FollowerStatistics>(url);
  }

  /**
   * 获取贡献记录
   */
  async getContributions(beginTime?: string, endTime?: string): Promise<Contribution[]> {
    let url = '/v3/users/self/contributions';
    if (beginTime && endTime) {
      url += `?begin_time=${beginTime}&end_time=${endTime}`;
    }
    const data = await this.httpClient.get<{ contributions: Contribution[] }>(url);
    return data.contributions;
  }

  /**
   * 获取贡献统计
   */
  async getContributionStats(): Promise<ContributionStatistics> {
    const data = await this.httpClient.get<{ statistics: ContributionStatistics }>(
      '/v3/users/self/contributions/statistics',
    );
    return data.statistics;
  }

  /**
   * 获取成就摘要列表
   */
  async getAchievementsSummary(): Promise<AchievementSummary[]> {
    const data = await this.httpClient.get<{ summaries: AchievementSummary[] }>(
      '/v3/users/self/achievements/summaries',
    );
    return data.summaries;
  }

  /**
   * 获取星球周榜排名
   */
  async getWeeklyRanking(groupId: number | string): Promise<WeeklyRanking> {
    return this.httpClient.get<WeeklyRanking>(
      `/v3/users/self/group_weekly_rankings?group_id=${groupId}`,
    );
  }

  /**
   * 获取用户偏好配置
   */
  async getPreferences(): Promise<Record<string, unknown>> {
    return this.httpClient.get('/v3/users/self/preferences');
  }
}

/**
 * 打卡请求模块
 */
export class CheckinsRequest extends BaseRequest {
  /**
   * 获取打卡项目列表
   */
  async list(groupId: number | string, options?: ListCheckinsOptions): Promise<Checkin[]> {
    const data = await this.httpClient.get<{ checkins: Checkin[] }>(
      `/v2/groups/${groupId}/checkins`,
      options as Record<string, unknown>,
    );
    return data.checkins;
  }

  /**
   * 获取打卡项目详情
   */
  async get(groupId: number | string, checkinId: number | string): Promise<Checkin> {
    const data = await this.httpClient.get<{ checkin: Checkin }>(
      `/v2/groups/${groupId}/checkins/${checkinId}`,
    );
    return data.checkin;
  }

  /**
   * 获取打卡统计
   */
  async getStatistics(
    groupId: number | string,
    checkinId: number | string,
  ): Promise<CheckinStatistics> {
    return this.httpClient.get(`/v2/groups/${groupId}/checkins/${checkinId}/statistics`);
  }

  /**
   * 获取打卡排行榜
   */
  async getRankingList(
    groupId: number | string,
    checkinId: number | string,
    options?: ListRankingOptions,
  ): Promise<RankingItem[]> {
    const data = await this.httpClient.get<{ ranking_list: RankingItem[] }>(
      `/v2/groups/${groupId}/checkins/${checkinId}/ranking_list`,
      options as Record<string, unknown>,
    );
    return data.ranking_list;
  }

  /**
   * 获取打卡话题
   */
  async getTopics(
    groupId: number | string,
    checkinId: number | string,
    options?: ListTopicsOptions,
  ): Promise<Topic[]> {
    const data = await this.httpClient.get<{ topics: Topic[] }>(
      `/v2/groups/${groupId}/checkins/${checkinId}/topics`,
      options as Record<string, unknown>,
    );
    return data.topics;
  }

  /**
   * 获取打卡每日统计
   */
  async getDailyStatistics(
    groupId: number | string,
    checkinId: number | string,
  ): Promise<DailyStatistics[]> {
    const data = await this.httpClient.get<{ daily_statistics: DailyStatistics[] }>(
      `/v2/groups/${groupId}/checkins/${checkinId}/statistics/daily`,
    );
    return data.daily_statistics;
  }

  /**
   * 获取打卡参与用户列表
   */
  async getJoinedUsers(
    groupId: number | string,
    checkinId: number | string,
    options?: JoinedUsersOptions,
  ): Promise<User[]> {
    const data = await this.httpClient.get<{ users: User[] }>(
      `/v2/groups/${groupId}/checkins/${checkinId}/joined_users`,
      options as Record<string, unknown>,
    );
    return data.users;
  }

  /**
   * 获取我的打卡记录
   */
  async getMyCheckins(
    groupId: number | string,
    checkinId: number | string,
    options?: MyCheckinsOptions,
  ): Promise<Topic[]> {
    const data = await this.httpClient.get<{ topics: Topic[] }>(
      `/v2/users/self/groups/${groupId}/checkins/${checkinId}/topics`,
      options as Record<string, unknown>,
    );
    return data.topics;
  }

  /**
   * 获取我的打卡日期列表
   */
  async getMyCheckinDays(
    groupId: number | string,
    checkinId: number | string,
  ): Promise<string[]> {
    const data = await this.httpClient.get<{ dates: string[] }>(
      `/v2/users/self/groups/${groupId}/checkins/${checkinId}/checkined_dates`,
    );
    return data.dates;
  }

  /**
   * 获取我的打卡统计
   */
  async getMyStatistics(
    groupId: number | string,
    checkinId: number | string,
  ): Promise<MyCheckinStatistics> {
    const data = await this.httpClient.get<{ statistics: MyCheckinStatistics }>(
      `/v2/users/self/groups/${groupId}/checkins/${checkinId}/statistics`,
    );
    return data.statistics;
  }

  /**
   * 创建打卡项目（训练营）
   *
   * @example
   * // 创建有截止时间的训练营
   * const checkin = await client.checkins.create(groupId, {
   *   title: '7天打卡挑战',
   *   text: '每天完成一个任务',
   *   checkin_days: 7,
   *   type: 'accumulated',
   *   show_topics_on_timeline: false,
   *   validity: {
   *     long_period: false,
   *     expiration_time: '2025-12-31T23:59:59.000+0800'
   *   }
   * });
   *
   * @example
   * // 创建长期有效的训练营
   * const checkin = await client.checkins.create(groupId, {
   *   title: '每日学习打卡',
   *   text: '持续学习，每天进步',
   *   checkin_days: 21,
   *   type: 'accumulated',
   *   validity: { long_period: true }
   * });
   */
  async create(groupId: number | string, params: CreateCheckinParams): Promise<Checkin> {
    const data = await this.httpClient.post<{ checkin: Checkin }>(
      `/v2/groups/${groupId}/checkins`,
      { req_data: params },
    );
    return data.checkin;
  }

  /**
   * 更新打卡项目
   */
  async update(
    groupId: number | string,
    checkinId: number | string,
    params: UpdateCheckinParams,
  ): Promise<Checkin> {
    const data = await this.httpClient.put<{ checkin: Checkin }>(
      `/v2/groups/${groupId}/checkins/${checkinId}`,
      { req_data: params },
    );
    return data.checkin;
  }
}

/**
 * Dashboard 请求模块
 */
export class DashboardRequest extends BaseRequest {
  /**
   * 获取星球概览
   */
  async getOverview(groupId: number | string): Promise<Record<string, unknown>> {
    return this.httpClient.get(`/v2/dashboard/groups/${groupId}/overview`);
  }

  /**
   * 获取收入概览
   */
  async getIncomes(groupId: number | string): Promise<Record<string, unknown>> {
    return this.httpClient.get(`/v2/dashboard/groups/${groupId}/incomes/overview`);
  }

  /**
   * 获取积分排行
   */
  async getScoreboardRanking(
    groupId: number | string,
    options?: ListRankingOptions,
  ): Promise<RankingItem[]> {
    const data = await this.httpClient.get<{ ranking_list: RankingItem[] }>(
      `/v2/dashboard/groups/${groupId}/scoreboard/ranking_list`,
      options as Record<string, unknown>,
    );
    return data.ranking_list;
  }

  /**
   * 获取星球权限配置
   */
  async getPrivileges(groupId: number | string): Promise<Record<string, unknown>> {
    const data = await this.httpClient.get<{ privileges: Record<string, unknown> }>(
      `/v2/dashboard/groups/${groupId}/privileges`,
    );
    return data.privileges;
  }

  /**
   * 获取发票统计
   */
  async getInvoiceStats(): Promise<InvoiceStats> {
    return this.httpClient.get<InvoiceStats>('/v3/invoices/statistics');
  }
}

/**
 * 排行榜请求模块
 */
export class RankingRequest extends BaseRequest {
  /**
   * 获取星球排行榜
   */
  async getGroupRanking(
    groupId: number | string,
    options?: ListRankingOptions,
  ): Promise<RankingItem[]> {
    const data = await this.httpClient.get<{ ranking_list: RankingItem[] }>(
      `/v2/groups/${groupId}/ranking_list`,
      options as Record<string, unknown>,
    );
    return data.ranking_list;
  }

  /**
   * 获取星球排行统计
   */
  async getGroupRankingStats(groupId: number | string): Promise<RankingStatistics> {
    const data = await this.httpClient.get<{ statistics: RankingStatistics }>(
      `/v3/groups/${groupId}/ranking_list/statistics`,
    );
    return data.statistics;
  }

  /**
   * 获取积分排行榜
   */
  async getScoreRanking(
    groupId: number | string,
    options?: ListRankingOptions,
  ): Promise<RankingItem[]> {
    const data = await this.httpClient.get<{ ranking_list: RankingItem[] }>(
      `/v2/dashboard/groups/${groupId}/scoreboard/ranking_list`,
      options as Record<string, unknown>,
    );
    return data.ranking_list;
  }

  /**
   * 获取我的积分统计
   */
  async getMyScoreStats(groupId: number | string): Promise<Record<string, unknown>> {
    const data = await this.httpClient.get<{ statistics: Record<string, unknown> }>(
      `/v2/dashboard/groups/${groupId}/scoreboard/statistics/self`,
    );
    return data.statistics || {};
  }

  /**
   * 获取积分榜设置
   */
  async getScoreboardSettings(groupId: number | string): Promise<ScoreboardSettings> {
    const data = await this.httpClient.get<ScoreboardSettings>(
      `/v2/dashboard/groups/${groupId}/scoreboard/settings`,
    );
    return data;
  }

  /**
   * 获取邀请排行榜
   */
  async getInvitationRanking(groupId: number | string): Promise<RankingItem[]> {
    const data = await this.httpClient.get<{ ranking_list: RankingItem[] }>(
      `/v2/groups/${groupId}/invitations/ranking_list`,
    );
    return data.ranking_list;
  }

  /**
   * 获取贡献排行榜
   */
  async getContributionRanking(groupId: number | string): Promise<RankingItem[]> {
    const data = await this.httpClient.get<{ ranking_list: RankingItem[] }>(
      `/v2/groups/${groupId}/contribution_ranking_list`,
    );
    return data.ranking_list;
  }

  /**
   * 获取全局星球排行榜（v3接口）
   *
   * @param type 排行类型: group_sales_list(畅销榜), new_star_list(新星榜),
   *             paid_group_active_list(活跃榜), group_fortune_list(财富榜)
   * @param count 返回数量
   * @returns 排行数据
   */
  async getGlobalRanking(type: string, count: number): Promise<Record<string, unknown>> {
    return this.httpClient.get('/v3/groups/ranking_list', { type, count });
  }
}

/**
 * 动态查询参数
 */
export interface ActivitiesOptions {
  /** 查询范围: general/like/examinations/system_message */
  scope?: 'general' | 'like' | 'examinations' | 'system_message';
  /** 返回数量 */
  count?: number;
  /** 结束时间 */
  end_time?: string;
}

/**
 * 杂项请求模块
 */
export class MiscRequest extends BaseRequest {
  /**
   * 获取全局配置
   */
  async getGlobalConfig(): Promise<GlobalConfig> {
    return this.httpClient.get<GlobalConfig>('/v2/settings');
  }

  /**
   * 获取动态列表
   */
  async getActivities(options?: ActivitiesOptions): Promise<Activity[]> {
    const data = await this.httpClient.get<{ dynamics: Activity[] }>(
      '/v2/dynamics',
      options as Record<string, unknown>,
    );
    return data.dynamics;
  }

  /**
   * 获取PK群组详情
   */
  async getPkGroup(pkGroupId: number | string): Promise<PkGroup> {
    const data = await this.httpClient.get<{ group: PkGroup }>(`/v2/pk_groups/${pkGroupId}`);
    return data.group;
  }
}
