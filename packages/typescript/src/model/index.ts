/**
 * 用户模型
 */
export interface User {
  user_id: number;
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
