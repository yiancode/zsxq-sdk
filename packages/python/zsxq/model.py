"""数据模型定义"""

from typing import Optional, List, Union
from pydantic import BaseModel, Field, model_validator
from enum import Enum


class User(BaseModel):
    """用户模型

    注意：API 返回的数据中，用户ID可能以 uid 或 user_id 形式出现
    使用 model_validator 来处理字段兼容性
    """
    user_id: Optional[Union[int, str]] = None
    uid: Optional[str] = None
    name: str
    avatar_url: str
    location: Optional[str] = None
    introduction: Optional[str] = None
    unique_id: Optional[str] = None
    user_sid: Optional[str] = None
    grade: Optional[str] = None
    verified: Optional[bool] = None

    @model_validator(mode='before')
    @classmethod
    def handle_user_id(cls, data):
        """处理 user_id 和 uid 字段的兼容性"""
        if isinstance(data, dict):
            # 如果只有 uid，将其赋值给 user_id
            if 'uid' in data and 'user_id' not in data:
                data['user_id'] = data['uid']
            # 如果只有 user_id，将其赋值给 uid
            elif 'user_id' in data and 'uid' not in data:
                data['uid'] = str(data['user_id'])
        return data

    class Config:
        populate_by_name = True  # 允许通过别名和字段名填充


class GroupType(str, Enum):
    """星球类型"""
    FREE = "free"
    PAY = "pay"


class Group(BaseModel):
    """星球模型"""
    group_id: int
    number: Optional[int] = None
    name: str
    description: str = ""
    background_url: str = ""
    type: GroupType
    member_count: Optional[int] = None
    owner: Optional[User] = None
    create_time: str = ""
    risk_level: Optional[str] = None
    partner_ids: Optional[List[int]] = None
    admin_ids: Optional[List[int]] = None


class ImageSize(BaseModel):
    """图片尺寸"""
    url: str
    width: int
    height: int
    size: Optional[int] = None


class Image(BaseModel):
    """图片模型"""
    image_id: int
    type: Optional[str] = None
    original: Optional[ImageSize] = None
    thumbnail: Optional[ImageSize] = None
    large: Optional[ImageSize] = None


class FileAttachment(BaseModel):
    """文件附件模型"""
    file_id: int
    name: str
    hash: Optional[str] = None
    size: Optional[int] = None
    duration: Optional[int] = None
    url: Optional[str] = None


class Article(BaseModel):
    """文章模型"""
    article_id: int
    title: str
    inline_content_html: Optional[str] = None


class TalkContent(BaseModel):
    """分享内容"""
    owner: User
    text: Optional[str] = None
    images: Optional[List[Image]] = None
    files: Optional[List[FileAttachment]] = None
    article: Optional[Article] = None


class TaskContent(BaseModel):
    """作业内容"""
    owner: User
    title: Optional[str] = None
    text: Optional[str] = None


class QuestionContent(BaseModel):
    """提问内容"""
    owner: User
    text: Optional[str] = None


class SolutionContent(BaseModel):
    """回答内容"""
    owner: User
    text: Optional[str] = None


class TopicType(str, Enum):
    """话题类型"""
    TALK = "talk"
    TASK = "task"
    QA = "q&a"
    SOLUTION = "solution"


class Topic(BaseModel):
    """话题模型"""
    topic_id: int
    topic_uid: str = ""
    group: Optional[Group] = None
    type: TopicType
    create_time: str
    talk: Optional[TalkContent] = None
    task: Optional[TaskContent] = None
    question: Optional[QuestionContent] = None
    solution: Optional[SolutionContent] = None
    likes_count: Optional[int] = None
    comments_count: Optional[int] = None
    rewards_count: Optional[int] = None
    reading_count: Optional[int] = None
    digested: Optional[bool] = None
    sticky: Optional[bool] = None


class Comment(BaseModel):
    """评论模型"""
    comment_id: int
    owner: User
    text: str
    create_time: str
    likes_count: Optional[int] = None
    repliee: Optional[User] = None
    sticky: Optional[bool] = None


class Hashtag(BaseModel):
    """标签模型"""
    hashtag_id: int
    name: str
    topics_count: Optional[int] = None


class CheckinStatus(str, Enum):
    """打卡状态"""
    ONGOING = "ongoing"
    CLOSED = "closed"
    OVER = "over"


class Checkin(BaseModel):
    """打卡项目模型"""
    checkin_id: int
    group: Optional[Group] = None
    owner: Optional[User] = None
    name: str
    description: str = ""
    cover_url: Optional[str] = None
    status: CheckinStatus
    create_time: str = ""
    begin_time: str = ""
    end_time: str = ""


class CheckinStatistics(BaseModel):
    """打卡统计模型"""
    joined_count: int
    completed_count: int
    checkined_count: int


class RankingItem(BaseModel):
    """排行榜项目模型"""
    user: User
    rank: int
    count: int
    continuous_count: Optional[int] = None


class Menu(BaseModel):
    """菜单模型"""
    name: str
    type: str
    url: Optional[str] = None


class RoleMembers(BaseModel):
    """角色成员模型"""
    owner: Optional[User] = None
    partners: Optional[List[User]] = None
    admins: Optional[List[User]] = None


class Column(BaseModel):
    """专栏模型"""
    column_id: int
    name: str
    topics_count: Optional[int] = None
    cover_url: Optional[str] = None


class ActivitySummary(BaseModel):
    """活跃摘要模型"""
    topics_count: Optional[int] = None
    comments_count: Optional[int] = None
    likes_received: Optional[int] = None
    questions_count: Optional[int] = None
    answers_count: Optional[int] = None


class RenewalInfo(BaseModel):
    """续费信息模型"""
    renewal_enabled: Optional[bool] = None
    renewal_price: Optional[int] = None
    renewal_days: Optional[int] = None


class DistributionInfo(BaseModel):
    """分销信息模型"""
    enabled: Optional[bool] = None
    commission_rate: Optional[float] = None


class CustomTag(BaseModel):
    """自定义标签模型"""
    label_id: Optional[int] = None
    name: str
    topics_count: Optional[int] = None


class ScheduledJob(BaseModel):
    """定时任务模型"""
    job_id: Optional[int] = None
    name: str
    status: Optional[str] = None
    cron: Optional[str] = None


class GroupWarning(BaseModel):
    """风险预警模型"""
    type: Optional[str] = None
    level: Optional[str] = None
    title: Optional[str] = None
    message: Optional[str] = None


class Reward(BaseModel):
    """打赏模型"""
    reward_id: Optional[int] = None
    user: Optional[User] = None
    amount: Optional[int] = None
    create_time: Optional[str] = None


class Inviter(BaseModel):
    """邀请人模型"""
    user: Optional[User] = None
    join_time: Optional[str] = None


class Coupon(BaseModel):
    """优惠券模型"""
    coupon_id: int
    name: str
    discount: Optional[int] = None
    expire_time: Optional[str] = None


class Remark(BaseModel):
    """备注模型"""
    remark_id: int
    content: str
    user: Optional[User] = None
    create_time: Optional[str] = None


class PreferenceCategory(BaseModel):
    """推荐偏好分类"""
    category_id: Optional[int] = None
    name: str
    selected: Optional[bool] = None


class UnansweredQuestionsSummary(BaseModel):
    """未回答问题摘要"""
    unanswered_count: Optional[int] = None


class FollowerStatistics(BaseModel):
    """关注者统计"""
    followers_count: Optional[int] = None
    new_followers_count: Optional[int] = None


class Contribution(BaseModel):
    """贡献记录"""
    date: str
    type: Optional[str] = None
    count: Optional[int] = None


class ContributionStatistics(BaseModel):
    """贡献统计"""
    max_consecutive_days: Optional[int] = None
    current_consecutive_days: Optional[int] = None
    total_contributions: Optional[int] = None


class AchievementSummary(BaseModel):
    """成就摘要"""
    achievement_id: Optional[int] = None
    title: str
    description: Optional[str] = None
    icon_url: Optional[str] = None


class WeeklyRankingItem(BaseModel):
    """周榜排名项"""
    ranking: Optional[int] = None
    count: Optional[int] = None


class WeeklyRanking(BaseModel):
    """周榜排名"""
    top_topics: Optional[WeeklyRankingItem] = None
    top_likes: Optional[WeeklyRankingItem] = None
    top_digests: Optional[WeeklyRankingItem] = None


class DailyStatistics(BaseModel):
    """每日统计"""
    date: str
    checkin_count: Optional[int] = None
    user_count: Optional[int] = None


class MyCheckinStatistics(BaseModel):
    """我的打卡统计"""
    total_checkin_count: Optional[int] = None
    continuous_days: Optional[int] = None
    last_checkin_date: Optional[str] = None


class RankingStatistics(BaseModel):
    """排行统计"""
    total_count: Optional[int] = None
    my_rank: Optional[int] = None


class ScoreboardSettings(BaseModel):
    """积分榜设置"""
    enabled: Optional[bool] = None
    name: Optional[str] = None
    rules: Optional[str] = None


class InvoiceStats(BaseModel):
    """发票统计"""
    total_count: Optional[int] = None
    pending_count: Optional[int] = None
    finished_count: Optional[int] = None


class GlobalConfig(BaseModel):
    """全局配置"""
    topic: Optional[dict] = None
    max_video_size: Optional[int] = None


class Activity(BaseModel):
    """动态"""
    dynamic_id: Optional[int] = None
    action: Optional[str] = None
    user: Optional[User] = None
    topic: Optional["Topic"] = None
    create_time: Optional[str] = None


class PkGroup(BaseModel):
    """PK群组"""
    pk_group_id: Optional[int] = None
    name: Optional[str] = None
    power: Optional[int] = None


class PkBattle(BaseModel):
    """PK对战记录"""
    id: Optional[int] = None
    pk_group_id: Optional[int] = None
    title: Optional[str] = None
    status: Optional[str] = None
    start_time: Optional[str] = None
    end_time: Optional[str] = None
    result: Optional[dict] = None
    participants: Optional[list] = None


class UrlDetail(BaseModel):
    """URL详情"""
    url: str
    title: Optional[str] = None
    description: Optional[str] = None
    image_url: Optional[str] = None
    site_name: Optional[str] = None
    type: Optional[str] = None
