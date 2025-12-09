/**
 * 知识星球 TypeScript SDK
 *
 * @example
 * ```typescript
 * import { ZsxqClientBuilder } from 'zsxq-sdk';
 *
 * const client = new ZsxqClientBuilder()
 *   .setToken('your-token')
 *   .setTimeout(10000)
 *   .build();
 *
 * // 获取星球列表
 * const groups = await client.groups.list();
 *
 * // 获取话题
 * const topics = await client.topics.list(groupId, { count: 20 });
 *
 * // 获取当前用户
 * const user = await client.users.self();
 * ```
 *
 * @packageDocumentation
 */

// Client
export { ZsxqClient, ZsxqClientBuilder, ZsxqConfig, DEFAULT_CONFIG } from './client';

// Models
export {
  User,
  Group,
  Topic,
  TalkContent,
  TaskContent,
  QuestionContent,
  SolutionContent,
  Image,
  FileAttachment,
  Article,
  Checkin,
  CheckinStatistics,
  RankingItem,
  Comment,
  Hashtag,
} from './model';

// Requests
export {
  GroupsRequest,
  TopicsRequest,
  UsersRequest,
  CheckinsRequest,
  DashboardRequest,
  ListTopicsOptions,
  ListCheckinsOptions,
  ListCommentsOptions,
  ListRankingOptions,
} from './request';

// Exceptions
export {
  ZsxqException,
  AuthException,
  TokenInvalidException,
  TokenExpiredException,
  SignatureInvalidException,
  PermissionException,
  NotMemberException,
  NotOwnerException,
  ResourceNotFoundException,
  GroupNotFoundException,
  TopicNotFoundException,
  RateLimitException,
  BusinessException,
  NotJoinedCheckinException,
  NetworkException,
  TimeoutException,
  createException,
} from './exception';

// HTTP
export { HttpClient, ZsxqResponse } from './http';
