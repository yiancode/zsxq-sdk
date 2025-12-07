/**
 * 知识星球API端点定义
 */
export const ZSXQ_ENDPOINTS = {
  // 星球相关
  PLANETS: '/v2/groups',
  PLANET_DETAIL: (groupId: string) => `/v2/groups/${groupId}`,

  // 话题相关
  TOPICS: (groupId: string) => `/v2/groups/${groupId}/topics`,
  TOPIC_DETAIL: (groupId: string, topicId: string) => `/v2/groups/${groupId}/topics/${topicId}`,

  // 训练营相关
  TRAINING_CAMPS: (groupId: string) => `/v2/groups/${groupId}/training_camps`,
  TRAINING_CAMP_DETAIL: (groupId: string, campId: string) =>
    `/v2/groups/${groupId}/training_camps/${campId}`,

  // 成员相关
  MEMBERS: (groupId: string) => `/v2/groups/${groupId}/members`,
  MEMBER_DETAIL: (groupId: string, userId: string) => `/v2/groups/${groupId}/members/${userId}`,
};
