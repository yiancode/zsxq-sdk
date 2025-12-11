/**
 * 用户模型
 *
 * 注意：API 返回的数据中，用户ID可能以 uid 或 user_id 形式出现
 * 两个字段都设为可选，至少有一个会有值
 */
export interface User {
  user_id?: number | string;
  uid?: string;
  name: string;
  avatar_url: string;
  location?: string;
  introduction?: string;
  unique_id?: string;
  user_sid?: string;
  grade?: string;
  verified?: boolean;
}

/**
 * 星球模型
 */
export interface Group {
  group_id: number;
  number?: number;
  name: string;
  description: string;
  background_url: string;
  type: 'free' | 'pay';
  member_count?: number;
  owner?: User;
  create_time: string;
  risk_level?: string;
  partner_ids?: number[];
  admin_ids?: number[];
}

/**
 * 话题内容 - 分享
 */
export interface TalkContent {
  owner: User;
  text?: string;
  images?: Image[];
  files?: FileAttachment[];
  article?: Article;
}

/**
 * 话题内容 - 作业
 */
export interface TaskContent {
  owner: User;
  title?: string;
  text?: string;
}

/**
 * 话题内容 - 提问
 */
export interface QuestionContent {
  owner: User;
  text?: string;
}

/**
 * 话题内容 - 回答
 */
export interface SolutionContent {
  owner: User;
  text?: string;
}

/**
 * 话题模型
 */
export interface Topic {
  topic_id: number;
  topic_uid: string;
  group: Group;
  type: 'talk' | 'task' | 'q&a' | 'solution';
  create_time: string;
  talk?: TalkContent;
  task?: TaskContent;
  question?: QuestionContent;
  solution?: SolutionContent;
  likes_count?: number;
  comments_count?: number;
  rewards_count?: number;
  reading_count?: number;
  digested?: boolean;
  sticky?: boolean;
}

/**
 * 图片模型
 */
export interface Image {
  image_id: number;
  type?: string;
  original?: {
    url: string;
    width: number;
    height: number;
    size?: number;
  };
  thumbnail?: {
    url: string;
    width: number;
    height: number;
  };
  large?: {
    url: string;
    width: number;
    height: number;
  };
}

/**
 * 文件附件模型
 */
export interface FileAttachment {
  file_id: number;
  name: string;
  hash?: string;
  size?: number;
  duration?: number;
  url?: string;
}

/**
 * 文章模型
 */
export interface Article {
  article_id: number;
  title: string;
  inline_content_html?: string;
}

/**
 * 打卡项目模型
 */
export interface Checkin {
  checkin_id: number;
  group: Group;
  owner: User;
  name: string;
  description: string;
  cover_url?: string;
  status: 'ongoing' | 'closed' | 'over';
  create_time: string;
  begin_time: string;
  end_time: string;
}

/**
 * 打卡统计模型
 */
export interface CheckinStatistics {
  joined_count: number;
  completed_count: number;
  checkined_count: number;
}

/**
 * 排行榜项目模型
 */
export interface RankingItem {
  user: User;
  rank: number;
  count: number;
  continuous_count?: number;
}

/**
 * 评论模型
 */
export interface Comment {
  comment_id: number;
  owner: User;
  text: string;
  create_time: string;
  likes_count?: number;
  repliee?: User;
  sticky?: boolean;
}

/**
 * 标签模型
 */
export interface Hashtag {
  hashtag_id: number;
  name: string;
  topics_count?: number;
}

/**
 * 菜单模型
 */
export interface Menu {
  menu_id: number;
  name: string;
  type: string;
  icon?: string;
  url?: string;
  order?: number;
}

/**
 * 角色成员模型
 */
export interface RoleMembers {
  owner?: User;
  partners?: User[];
  admins?: User[];
}

/**
 * 专栏模型
 */
export interface Column {
  column_id: number;
  name: string;
  description?: string;
  topics_count?: number;
  cover_url?: string;
  create_time?: string;
}

/**
 * 活跃摘要模型
 */
export interface ActivitySummary {
  topics_count?: number;
  comments_count?: number;
  likes_count?: number;
  last_active_time?: string;
}

/**
 * 续费信息模型
 */
export interface RenewalInfo {
  renewal_price?: number;
  renewal_discount?: number;
  renewal_expire_time?: string;
  auto_renewal?: boolean;
}

/**
 * 分销信息模型
 */
export interface DistributionInfo {
  distribution_enabled?: boolean;
  commission_rate?: number;
  total_sales?: number;
  total_commission?: number;
}

/**
 * 自定义标签模型
 */
export interface CustomTag {
  label_id: number;
  name: string;
  color?: string;
  member_count?: number;
}

/**
 * 定时任务模型
 */
export interface ScheduledJob {
  job_id: number;
  name: string;
  type: string;
  status: string;
  schedule_time?: string;
  create_time?: string;
}

/**
 * 星球风险预警模型
 */
export interface GroupWarning {
  warning_type?: string;
  warning_level?: string;
  warning_message?: string;
  warning_time?: string;
}

/**
 * 打赏模型
 */
export interface Reward {
  reward_id?: number;
  user: User;
  amount?: number;
  message?: string;
  create_time?: string;
}

/**
 * 邀请人模型
 */
export interface Inviter {
  user?: User;
  invite_time?: string;
}

/**
 * 优惠券模型
 */
export interface Coupon {
  coupon_id: number;
  name: string;
  amount?: number;
  discount?: number;
  expire_time?: string;
  status?: string;
}

/**
 * 备注模型
 */
export interface Remark {
  remark_id: number;
  user: User;
  content?: string;
  create_time?: string;
}

/**
 * 推荐偏好分类模型
 */
export interface PreferenceCategory {
  category_id: number;
  name: string;
  selected?: boolean;
}

/**
 * 未回答问题摘要模型
 */
export interface UnansweredQuestionsSummary {
  total_count?: number;
  unanswered_count?: number;
  oldest_question_time?: string;
}

/**
 * 关注者统计模型
 */
export interface FollowerStatistics {
  followers_count?: number;
  followings_count?: number;
  new_followers_count?: number;
}

/**
 * 贡献记录模型
 */
export interface Contribution {
  contribution_id?: number;
  type: string;
  count?: number;
  date?: string;
}

/**
 * 贡献统计模型
 */
export interface ContributionStatistics {
  total_count?: number;
  total_topics?: number;
  total_comments?: number;
  total_likes?: number;
}

/**
 * 成就摘要模型
 */
export interface AchievementSummary {
  achievement_id: number;
  name: string;
  description?: string;
  icon?: string;
  completed?: boolean;
  progress?: number;
}

/**
 * 周榜排名模型
 */
export interface WeeklyRanking {
  rank?: number;
  score?: number;
  week_start?: string;
  week_end?: string;
}

/**
 * 每日统计模型
 */
export interface DailyStatistics {
  date: string;
  checkin_count?: number;
  user_count?: number;
}

/**
 * 我的打卡统计模型
 */
export interface MyCheckinStatistics {
  total_count?: number;
  continuous_count?: number;
  max_continuous_count?: number;
  last_checkin_time?: string;
}

/**
 * 排行统计模型
 */
export interface RankingStatistics {
  total_users?: number;
  my_rank?: number;
  my_score?: number;
}

/**
 * 积分榜设置模型
 */
export interface ScoreboardSettings {
  enabled?: boolean;
  score_rules?: Record<string, number>;
  rank_rewards?: Record<string, unknown>;
}

/**
 * 发票统计模型
 */
export interface InvoiceStats {
  total_amount?: number;
  invoice_count?: number;
  pending_count?: number;
}

/**
 * 全局配置模型
 */
export interface GlobalConfig {
  version?: string;
  features?: Record<string, boolean>;
  settings?: Record<string, unknown>;
}

/**
 * 动态模型
 */
export interface Activity {
  activity_id?: number;
  type: string;
  user?: User;
  content?: string;
  create_time?: string;
  target?: Record<string, unknown>;
}

/**
 * PK群组模型
 */
export interface PkGroup {
  pk_group_id: number;
  name: string;
  description?: string;
  status?: string;
  groups?: Group[];
  start_time?: string;
  end_time?: string;
}
