import { HttpClient } from '../http';
import { Group, User, Topic, Checkin, Comment, Hashtag, RankingItem, CheckinStatistics } from '../model';

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
}
