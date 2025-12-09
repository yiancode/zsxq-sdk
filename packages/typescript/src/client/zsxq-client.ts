import { ZsxqConfig } from './zsxq-config';
import { HttpClient } from '../http';
import {
  GroupsRequest,
  TopicsRequest,
  UsersRequest,
  CheckinsRequest,
  DashboardRequest,
} from '../request';

/**
 * 知识星球 SDK 主客户端
 *
 * 使用 ZsxqClientBuilder 构建实例：
 * ```typescript
 * const client = new ZsxqClientBuilder()
 *   .setToken('your-token')
 *   .build();
 *
 * // 获取星球列表
 * const groups = await client.groups.list();
 *
 * // 获取话题
 * const topics = await client.topics.list(groupId);
 * ```
 */
export class ZsxqClient {
  /** 星球管理 */
  readonly groups: GroupsRequest;

  /** 话题管理 */
  readonly topics: TopicsRequest;

  /** 用户管理 */
  readonly users: UsersRequest;

  /** 打卡管理 */
  readonly checkins: CheckinsRequest;

  /** 数据面板 */
  readonly dashboard: DashboardRequest;

  private readonly httpClient: HttpClient;

  constructor(config: ZsxqConfig) {
    this.httpClient = new HttpClient(config);

    // 初始化各功能模块
    this.groups = new GroupsRequest(this.httpClient);
    this.topics = new TopicsRequest(this.httpClient);
    this.users = new UsersRequest(this.httpClient);
    this.checkins = new CheckinsRequest(this.httpClient);
    this.dashboard = new DashboardRequest(this.httpClient);
  }
}
