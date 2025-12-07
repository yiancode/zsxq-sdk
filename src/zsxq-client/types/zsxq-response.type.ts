/**
 * 知识星球API响应基础类型
 */
export interface ZsxqBaseResponse<T = any> {
  succeeded: boolean;
  code: number;
  message?: string;
  resp_data: T;
}

/**
 * 知识星球星球信息
 */
export interface ZsxqPlanetInfo {
  group_id: string;
  name: string;
  description: string;
  members_count: number;
  logo: string;
  background_url: string;
  create_time: string;
  owner: {
    user_id: string;
    name: string;
    avatar_url: string;
  };
}

/**
 * 知识星球话题信息
 */
export interface ZsxqTopicInfo {
  topic_id: string;
  group_id: string;
  title: string;
  text: string;
  create_time: string;
  author: {
    user_id: string;
    name: string;
    avatar_url: string;
  };
  likes_count: number;
  comments_count: number;
}

/**
 * 知识星球训练营信息
 */
export interface ZsxqTrainingCampInfo {
  camp_id: string;
  group_id: string;
  name: string;
  description: string;
  start_time: string;
  end_time: string;
  participants_count: number;
}
