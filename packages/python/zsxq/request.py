"""请求模块"""

from typing import Any, Dict, List, Optional
from dataclasses import dataclass

from zsxq.http import HttpClient
from zsxq.model import (
    User,
    Group,
    Topic,
    Comment,
    Hashtag,
    Checkin,
    CheckinStatistics,
    RankingItem,
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
)


@dataclass
class ListTopicsOptions:
    """话题列表查询参数"""
    count: Optional[int] = None
    scope: Optional[str] = None  # all/digests/by_owner
    direction: Optional[str] = None  # forward/backward
    begin_time: Optional[str] = None  # ISO 8601
    end_time: Optional[str] = None  # ISO 8601
    with_invisibles: Optional[bool] = None


@dataclass
class ListCheckinsOptions:
    """打卡列表查询参数"""
    scope: Optional[str] = None  # ongoing/closed/over
    count: Optional[int] = None


@dataclass
class ListCommentsOptions:
    """评论列表查询参数"""
    count: Optional[int] = None
    sort: Optional[str] = None  # asc/desc
    with_sticky: Optional[bool] = None


@dataclass
class ListRankingOptions:
    """排行榜查询参数"""
    type: Optional[str] = None  # continuous/accumulated
    index: Optional[int] = None


@dataclass
class CheckinValidity:
    """打卡项目有效期配置"""
    long_period: Optional[bool] = None  # 是否长期有效
    expiration_time: Optional[str] = None  # 截止时间 (ISO 8601 格式)


@dataclass
class CreateCheckinParams:
    """创建打卡项目参数

    基于实际 API 结构:
    {
      "req_data": {
        "title": "训练营标题",
        "text": "训练营描述",
        "checkin_days": 7,
        "type": "accumulated",
        "show_topics_on_timeline": false,
        "validity": {
          "long_period": false,
          "expiration_time": "2025-12-24T23:59:59.798+0800"
        }
      }
    }
    """
    title: str  # 训练营标题
    checkin_days: int  # 打卡天数
    type: str  # 打卡类型: accumulated(累计打卡) / continuous(连续打卡)
    text: Optional[str] = None  # 训练营描述
    show_topics_on_timeline: Optional[bool] = None  # 是否在时间线展示
    validity: Optional[CheckinValidity] = None  # 有效期配置

    def to_dict(self) -> Dict[str, Any]:
        """转换为 API 请求字典"""
        result: Dict[str, Any] = {
            "title": self.title,
            "checkin_days": self.checkin_days,
            "type": self.type,
        }
        if self.text is not None:
            result["text"] = self.text
        if self.show_topics_on_timeline is not None:
            result["show_topics_on_timeline"] = self.show_topics_on_timeline
        if self.validity is not None:
            validity_dict: Dict[str, Any] = {}
            if self.validity.long_period is not None:
                validity_dict["long_period"] = self.validity.long_period
            if self.validity.expiration_time is not None:
                validity_dict["expiration_time"] = self.validity.expiration_time
            result["validity"] = validity_dict
        return result


@dataclass
class UpdateCheckinParams:
    """更新打卡项目参数"""
    title: Optional[str] = None
    text: Optional[str] = None
    checkin_days: Optional[int] = None
    type: Optional[str] = None
    show_topics_on_timeline: Optional[bool] = None
    validity: Optional[CheckinValidity] = None
    status: Optional[str] = None

    def to_dict(self) -> Dict[str, Any]:
        """转换为 API 请求字典"""
        result: Dict[str, Any] = {}
        if self.title is not None:
            result["title"] = self.title
        if self.text is not None:
            result["text"] = self.text
        if self.checkin_days is not None:
            result["checkin_days"] = self.checkin_days
        if self.type is not None:
            result["type"] = self.type
        if self.show_topics_on_timeline is not None:
            result["show_topics_on_timeline"] = self.show_topics_on_timeline
        if self.validity is not None:
            validity_dict: Dict[str, Any] = {}
            if self.validity.long_period is not None:
                validity_dict["long_period"] = self.validity.long_period
            if self.validity.expiration_time is not None:
                validity_dict["expiration_time"] = self.validity.expiration_time
            result["validity"] = validity_dict
        if self.status is not None:
            result["status"] = self.status
        return result


class GroupsRequest:
    """星球请求模块"""

    def __init__(self, client: HttpClient):
        self._client = client

    async def list(self) -> List[Group]:
        """获取我的星球列表"""
        data = await self._client.get("/v2/groups")
        return [Group.model_validate(g) for g in data.get("groups", [])]

    async def get(self, group_id: int) -> Group:
        """获取星球详情"""
        data = await self._client.get(f"/v2/groups/{group_id}")
        return Group.model_validate(data["group"])

    async def get_hashtags(self, group_id: int) -> List[Hashtag]:
        """获取星球标签"""
        data = await self._client.get(f"/v2/groups/{group_id}/hashtags")
        return [Hashtag.model_validate(h) for h in data.get("hashtags", [])]

    async def get_statistics(self, group_id: int) -> Dict[str, Any]:
        """获取星球统计"""
        return await self._client.get(f"/v2/groups/{group_id}/statistics")

    async def get_member(self, group_id: int, member_id: int) -> User:
        """获取成员信息"""
        data = await self._client.get(f"/v2/groups/{group_id}/members/{member_id}")
        return User.model_validate(data["user"])

    async def get_unread_count(self) -> Dict[str, int]:
        """获取未读话题数"""
        return await self._client.get("/v2/groups/unread_topics_count")

    async def get_menus(self, group_id: int) -> List[Menu]:
        """获取星球菜单配置"""
        data = await self._client.get(f"/v2/groups/{group_id}/menus")
        return [Menu.model_validate(m) for m in data.get("menus", [])]

    async def get_role_members(self, group_id: int) -> RoleMembers:
        """获取星球角色成员"""
        data = await self._client.get(f"/v2/groups/{group_id}/role_members")
        return RoleMembers.model_validate(data)

    async def get_columns(self, group_id: int) -> List[Column]:
        """获取星球专栏列表"""
        data = await self._client.get(f"/v2/groups/{group_id}/columns")
        return [Column.model_validate(c) for c in data.get("columns", [])]

    async def get_columns_summary(self, group_id: int) -> Dict[str, Any]:
        """获取专栏汇总信息"""
        return await self._client.get(f"/v2/groups/{group_id}/columns/summary")

    async def get_member_activity_summary(self, group_id: int, member_id: int) -> ActivitySummary:
        """获取成员活跃摘要"""
        data = await self._client.get(f"/v2/groups/{group_id}/members/{member_id}/summary")
        return ActivitySummary.model_validate(data.get("summary", {}))

    async def get_renewal_info(self, group_id: int) -> RenewalInfo:
        """获取星球续费信息"""
        data = await self._client.get(f"/v2/groups/{group_id}/renewal")
        return RenewalInfo.model_validate(data.get("renewal", {}))

    async def get_distribution(self, group_id: int) -> DistributionInfo:
        """获取星球分销信息"""
        data = await self._client.get(f"/v2/groups/{group_id}/distribution")
        return DistributionInfo.model_validate(data.get("distribution", {}))

    async def get_upgradeable_groups(self) -> List[Group]:
        """获取可升级星球列表"""
        data = await self._client.get("/v2/groups/upgradable_groups")
        return [Group.model_validate(g) for g in data.get("groups", [])]

    async def get_recommended_groups(self) -> List[Group]:
        """获取推荐星球列表"""
        data = await self._client.get("/v2/groups/recommendations")
        return [Group.model_validate(g) for g in data.get("groups", [])]

    async def get_custom_tags(self, group_id: int) -> List[CustomTag]:
        """获取星球自定义标签"""
        data = await self._client.get(f"/v2/groups/{group_id}/labels")
        return [CustomTag.model_validate(l) for l in data.get("labels", [])]

    async def get_scheduled_tasks(self, group_id: int) -> List[ScheduledJob]:
        """获取星球定时任务"""
        data = await self._client.get(f"/v2/groups/{group_id}/scheduled_jobs")
        return [ScheduledJob.model_validate(j) for j in data.get("jobs", [])]

    async def get_risk_warnings(self, group_id: int) -> GroupWarning:
        """获取星球风险预警"""
        data = await self._client.get(f"/v3/groups/{group_id}/group_warning")
        return GroupWarning.model_validate(data.get("warning", {}))


class TopicsRequest:
    """话题请求模块"""

    def __init__(self, client: HttpClient):
        self._client = client

    async def list(
        self, group_id: int, options: Optional[ListTopicsOptions] = None
    ) -> List[Topic]:
        """获取话题列表"""
        params: Dict[str, Any] = {}
        if options:
            if options.count is not None:
                params["count"] = options.count
            if options.scope:
                params["scope"] = options.scope
            if options.direction:
                params["direction"] = options.direction
            if options.begin_time:
                params["begin_time"] = options.begin_time
            if options.end_time:
                params["end_time"] = options.end_time
            if options.with_invisibles:
                params["with_invisibles"] = "true"

        data = await self._client.get(f"/v2/groups/{group_id}/topics", params or None)
        return [Topic.model_validate(t) for t in data.get("topics", [])]

    async def get(self, topic_id: int) -> Topic:
        """获取话题详情"""
        data = await self._client.get(f"/v2/topics/{topic_id}")
        return Topic.model_validate(data["topic"])

    async def get_comments(
        self, topic_id: int, options: Optional[ListCommentsOptions] = None
    ) -> List[Comment]:
        """获取话题评论"""
        params: Dict[str, Any] = {}
        if options:
            if options.count is not None:
                params["count"] = options.count
            if options.sort:
                params["sort"] = options.sort
            if options.with_sticky:
                params["with_sticky"] = "true"

        data = await self._client.get(f"/v2/topics/{topic_id}/comments", params or None)
        return [Comment.model_validate(c) for c in data.get("comments", [])]

    async def list_by_hashtag(
        self, hashtag_id: int, options: Optional[ListTopicsOptions] = None
    ) -> List[Topic]:
        """按标签获取话题"""
        params: Dict[str, Any] = {}
        if options:
            if options.count is not None:
                params["count"] = options.count
            if options.scope:
                params["scope"] = options.scope

        data = await self._client.get(f"/v2/hashtags/{hashtag_id}/topics", params or None)
        return [Topic.model_validate(t) for t in data.get("topics", [])]

    async def list_by_column(
        self, group_id: int, column_id: int, options: Optional[ListTopicsOptions] = None
    ) -> List[Topic]:
        """按专栏获取话题"""
        params: Dict[str, Any] = {}
        if options:
            if options.count is not None:
                params["count"] = options.count

        data = await self._client.get(
            f"/v2/groups/{group_id}/columns/{column_id}/topics", params or None
        )
        return [Topic.model_validate(t) for t in data.get("topics", [])]

    async def get_info(self, topic_id: int) -> Topic:
        """获取话题基础信息"""
        data = await self._client.get(f"/v2/topics/{topic_id}/info")
        return Topic.model_validate(data["topic"])

    async def get_rewards(self, topic_id: int) -> List[Reward]:
        """获取话题打赏列表"""
        data = await self._client.get(f"/v2/topics/{topic_id}/rewards")
        return [Reward.model_validate(r) for r in data.get("rewards", [])]

    async def get_recommendations(self, topic_id: int) -> List[Topic]:
        """获取相关推荐话题"""
        data = await self._client.get(f"/v2/topics/{topic_id}/recommendations")
        return [Topic.model_validate(t) for t in data.get("topics", [])]

    async def list_sticky(self, group_id: int) -> List[Topic]:
        """获取置顶话题列表"""
        data = await self._client.get(f"/v2/groups/{group_id}/topics/sticky")
        return [Topic.model_validate(t) for t in data.get("topics", [])]


class UsersRequest:
    """用户请求模块"""

    def __init__(self, client: HttpClient):
        self._client = client

    async def self_(self) -> User:
        """获取当前用户信息"""
        data = await self._client.get("/v3/users/self")
        return User.model_validate(data["user"])

    async def get(self, user_id: int) -> User:
        """获取指定用户信息"""
        data = await self._client.get(f"/v3/users/{user_id}")
        return User.model_validate(data["user"])

    async def get_statistics(self, user_id: int) -> Dict[str, Any]:
        """获取用户统计"""
        return await self._client.get(f"/v3/users/{user_id}/statistics")

    async def get_footprints(self, user_id: int) -> List[Topic]:
        """获取用户动态"""
        data = await self._client.get(f"/v2/users/{user_id}/footprints")
        return [Topic.model_validate(t) for t in data.get("topics", [])]

    async def get_created_groups(self, user_id: int) -> List[Group]:
        """获取用户创建的星球"""
        data = await self._client.get(f"/v2/users/{user_id}/created_groups")
        return [Group.model_validate(g) for g in data.get("groups", [])]

    async def get_avatar_url(self, user_id: int) -> str:
        """获取用户大尺寸头像URL"""
        data = await self._client.get(f"/v3/users/{user_id}/avatar_url")
        return data.get("avatar_url", "")

    async def get_group_footprints(self, user_id: int) -> List[Group]:
        """获取用户星球足迹"""
        data = await self._client.get(f"/v2/users/{user_id}/group_footprints")
        return [Group.model_validate(g) for g in data.get("groups", [])]

    async def get_applying_groups(self) -> List[Group]:
        """获取申请中的星球列表"""
        data = await self._client.get("/v2/groups/applying")
        return [Group.model_validate(g) for g in data.get("groups", [])]

    async def get_inviter(self, group_id: int) -> Inviter:
        """获取星球邀请人信息"""
        data = await self._client.get(f"/v2/groups/{group_id}/inviter")
        return Inviter.model_validate(data.get("inviter", {}))

    async def get_coupons(self) -> List[Coupon]:
        """获取我的优惠券列表"""
        data = await self._client.get("/v2/coupons")
        return [Coupon.model_validate(c) for c in data.get("coupons", [])]

    async def get_remarks(self) -> List[Remark]:
        """获取我的备注列表"""
        data = await self._client.get("/v2/remarks")
        return [Remark.model_validate(r) for r in data.get("remarks", [])]

    async def get_recommended_follows(self) -> List[User]:
        """获取推荐关注用户列表"""
        data = await self._client.get("/v2/users/recommended_follows")
        return [User.model_validate(u) for u in data.get("users", [])]

    async def get_blocked_users(self) -> List[User]:
        """获取屏蔽用户列表"""
        data = await self._client.get("/v2/users/block_users")
        return [User.model_validate(u) for u in data.get("users", [])]

    async def report_push_channel(self, channel: str, device_token: str) -> None:
        """上报推送通道"""
        body = {"channel": channel, "device_token": device_token}
        await self._client.post("/v2/users/self/push_channel", body)

    async def get_preference_categories(self) -> List[PreferenceCategory]:
        """获取推荐偏好分类"""
        data = await self._client.get("/v2/users/self/recommendations/preference_categories")
        return [PreferenceCategory.model_validate(c) for c in data.get("categories", [])]

    async def get_unanswered_questions_summary(self) -> UnansweredQuestionsSummary:
        """获取未回答问题摘要"""
        data = await self._client.get("/v2/users/self/unanswered_questions/brief")
        return UnansweredQuestionsSummary.model_validate(data)

    async def get_follower_stats(self, begin_time: Optional[str] = None) -> FollowerStatistics:
        """获取关注者统计"""
        path = "/v3/users/self/followers/statistics"
        if begin_time:
            path += f"?begin_time={begin_time}"
        data = await self._client.get(path)
        return FollowerStatistics.model_validate(data)

    async def get_contributions(self, begin_time: Optional[str] = None, end_time: Optional[str] = None) -> List[Contribution]:
        """获取贡献记录"""
        path = "/v3/users/self/contributions"
        if begin_time and end_time:
            path += f"?begin_time={begin_time}&end_time={end_time}"
        data = await self._client.get(path)
        return [Contribution.model_validate(c) for c in data.get("contributions", [])]

    async def get_contribution_stats(self) -> ContributionStatistics:
        """获取贡献统计"""
        data = await self._client.get("/v3/users/self/contributions/statistics")
        return ContributionStatistics.model_validate(data.get("statistics", {}))

    async def get_achievements_summary(self) -> List[AchievementSummary]:
        """获取成就摘要列表"""
        data = await self._client.get("/v3/users/self/achievements/summaries")
        return [AchievementSummary.model_validate(s) for s in data.get("summaries", [])]

    async def get_weekly_ranking(self, group_id: int) -> WeeklyRanking:
        """获取星球周榜排名"""
        data = await self._client.get(f"/v3/users/self/group_weekly_rankings?group_id={group_id}")
        return WeeklyRanking.model_validate(data)

    async def get_preferences(self) -> Dict[str, Any]:
        """获取用户偏好配置"""
        return await self._client.get("/v3/users/self/preferences")


class CheckinsRequest:
    """打卡请求模块"""

    def __init__(self, client: HttpClient):
        self._client = client

    async def list(
        self, group_id: int, options: Optional[ListCheckinsOptions] = None
    ) -> List[Checkin]:
        """获取打卡项目列表"""
        params: Dict[str, Any] = {}
        if options:
            if options.scope:
                params["scope"] = options.scope
            if options.count is not None:
                params["count"] = options.count

        data = await self._client.get(f"/v2/groups/{group_id}/checkins", params or None)
        return [Checkin.model_validate(c) for c in data.get("checkins", [])]

    async def get(self, group_id: int, checkin_id: int) -> Checkin:
        """获取打卡项目详情"""
        data = await self._client.get(f"/v2/groups/{group_id}/checkins/{checkin_id}")
        return Checkin.model_validate(data["checkin"])

    async def get_statistics(self, group_id: int, checkin_id: int) -> CheckinStatistics:
        """获取打卡统计"""
        data = await self._client.get(
            f"/v2/groups/{group_id}/checkins/{checkin_id}/statistics"
        )
        return CheckinStatistics.model_validate(data)

    async def get_ranking_list(
        self, group_id: int, checkin_id: int, options: Optional[ListRankingOptions] = None
    ) -> List[RankingItem]:
        """获取打卡排行榜"""
        params: Dict[str, Any] = {}
        if options:
            if options.type:
                params["type"] = options.type
            if options.index is not None:
                params["index"] = options.index

        # API 要求必须有 type 参数，默认查询累计打卡排行
        if "type" not in params:
            params["type"] = "accumulated"

        data = await self._client.get(
            f"/v2/groups/{group_id}/checkins/{checkin_id}/ranking_list", params or None
        )
        return [RankingItem.model_validate(r) for r in data.get("ranking_list", [])]

    async def get_topics(
        self, group_id: int, checkin_id: int, options: Optional[ListTopicsOptions] = None
    ) -> List[Topic]:
        """获取打卡话题"""
        params: Dict[str, Any] = {}
        if options:
            if options.count is not None:
                params["count"] = options.count

        # API 要求必须有 scope 参数，默认查询所有话题
        if "scope" not in params:
            params["scope"] = "all"
        # API 要求必须有 count 参数，默认 20
        if "count" not in params:
            params["count"] = 20

        data = await self._client.get(
            f"/v2/groups/{group_id}/checkins/{checkin_id}/topics", params or None
        )
        return [Topic.model_validate(t) for t in data.get("topics", [])]

    async def get_daily_statistics(self, group_id: int, checkin_id: int) -> List[DailyStatistics]:
        """获取打卡每日统计"""
        data = await self._client.get(
            f"/v2/groups/{group_id}/checkins/{checkin_id}/statistics/daily"
        )
        return [DailyStatistics.model_validate(d) for d in data.get("daily_statistics", [])]

    async def get_joined_users(
        self, group_id: int, checkin_id: int, count: Optional[int] = None, end_time: Optional[str] = None
    ) -> List[User]:
        """获取打卡参与用户列表"""
        params: Dict[str, Any] = {}
        if count is not None:
            params["count"] = count
        if end_time:
            params["end_time"] = end_time
        data = await self._client.get(
            f"/v2/groups/{group_id}/checkins/{checkin_id}/joined_users", params or None
        )
        return [User.model_validate(u) for u in data.get("users", [])]

    async def get_my_checkins(
        self, group_id: int, checkin_id: int, count: Optional[int] = None, end_time: Optional[str] = None
    ) -> List[Topic]:
        """获取我的打卡记录"""
        params: Dict[str, Any] = {}
        if count is not None:
            params["count"] = count
        if end_time:
            params["end_time"] = end_time
        data = await self._client.get(
            f"/v2/users/self/groups/{group_id}/checkins/{checkin_id}/topics", params or None
        )
        return [Topic.model_validate(t) for t in data.get("topics", [])]

    async def get_my_checkin_days(self, group_id: int, checkin_id: int) -> List[str]:
        """获取我的打卡日期列表"""
        data = await self._client.get(
            f"/v2/users/self/groups/{group_id}/checkins/{checkin_id}/checkined_dates"
        )
        return data.get("dates", [])

    async def get_my_statistics(self, group_id: int, checkin_id: int) -> MyCheckinStatistics:
        """获取我的打卡统计"""
        data = await self._client.get(
            f"/v2/users/self/groups/{group_id}/checkins/{checkin_id}/statistics"
        )
        return MyCheckinStatistics.model_validate(data.get("statistics", {}))

    async def create(self, group_id: int, params: CreateCheckinParams) -> Checkin:
        """创建打卡项目（训练营）

        示例 - 创建有截止时间的训练营::

            params = CreateCheckinParams(
                title="7天打卡挑战",
                text="每天完成一个任务",
                checkin_days=7,
                type="accumulated",
                show_topics_on_timeline=False,
                validity=CheckinValidity(
                    long_period=False,
                    expiration_time="2025-12-31T23:59:59.000+0800"
                )
            )
            checkin = await client.checkins.create(group_id, params)
            print(f"创建成功: {checkin.checkin_id}")

        示例 - 创建长期有效的训练营::

            params = CreateCheckinParams(
                title="每日学习打卡",
                text="持续学习，每天进步",
                checkin_days=21,
                type="accumulated",
                validity=CheckinValidity(long_period=True)
            )
            checkin = await client.checkins.create(group_id, params)

        Args:
            group_id: 星球ID
            params: 创建打卡项目参数

        Returns:
            创建的打卡项目
        """
        body = {"req_data": params.to_dict()}
        data = await self._client.post(f"/v2/groups/{group_id}/checkins", body)
        return Checkin.model_validate(data["checkin"])

    async def update(
        self, group_id: int, checkin_id: int, params: UpdateCheckinParams
    ) -> Checkin:
        """更新打卡项目

        Args:
            group_id: 星球ID
            checkin_id: 打卡项目ID
            params: 更新参数

        Returns:
            更新后的打卡项目
        """
        body = {"req_data": params.to_dict()}
        data = await self._client.put(f"/v2/groups/{group_id}/checkins/{checkin_id}", body)
        return Checkin.model_validate(data["checkin"])


class DashboardRequest:
    """Dashboard 请求模块"""

    def __init__(self, client: HttpClient):
        self._client = client

    async def get_overview(self, group_id: int) -> Dict[str, Any]:
        """获取星球概览"""
        return await self._client.get(f"/v2/dashboard/groups/{group_id}/overview")

    async def get_incomes(self, group_id: int) -> Dict[str, Any]:
        """获取收入概览"""
        return await self._client.get(f"/v2/dashboard/groups/{group_id}/incomes/overview")

    async def get_scoreboard_ranking(
        self, group_id: int, options: Optional[ListRankingOptions] = None
    ) -> List[RankingItem]:
        """获取积分排行"""
        params: Dict[str, Any] = {}
        if options:
            if options.type:
                params["type"] = options.type
            if options.index is not None:
                params["index"] = options.index

        data = await self._client.get(
            f"/v2/dashboard/groups/{group_id}/scoreboard/ranking_list", params or None
        )
        return [RankingItem.model_validate(r) for r in data.get("ranking_list", [])]

    async def get_privileges(self, group_id: int) -> Dict[str, Any]:
        """获取星球权限配置"""
        data = await self._client.get(f"/v2/dashboard/groups/{group_id}/privileges")
        return data.get("privileges", {})

    async def get_invoice_stats(self) -> InvoiceStats:
        """获取发票统计"""
        data = await self._client.get("/v3/invoices/statistics")
        return InvoiceStats.model_validate(data)


class RankingRequest:
    """排行榜请求模块"""

    def __init__(self, client: HttpClient):
        self._client = client

    async def get_group_ranking(
        self, group_id: int, options: Optional[ListRankingOptions] = None
    ) -> List[RankingItem]:
        """获取星球排行榜"""
        params: Dict[str, Any] = {}
        if options:
            if options.type:
                params["type"] = options.type
            if options.index is not None:
                params["index"] = options.index

        data = await self._client.get(
            f"/v2/groups/{group_id}/ranking_list", params or None
        )
        return [RankingItem.model_validate(r) for r in data.get("ranking_list", [])]

    async def get_group_ranking_stats(self, group_id: int) -> RankingStatistics:
        """获取排行统计"""
        data = await self._client.get(f"/v3/groups/{group_id}/ranking_list/statistics")
        return RankingStatistics.model_validate(data.get("statistics", {}))

    async def get_score_ranking(
        self, group_id: int, options: Optional[ListRankingOptions] = None
    ) -> List[RankingItem]:
        """获取积分排行榜"""
        params: Dict[str, Any] = {}
        if options:
            if options.type:
                params["type"] = options.type
            if options.index is not None:
                params["index"] = options.index

        data = await self._client.get(
            f"/v2/dashboard/groups/{group_id}/scoreboard/ranking_list", params or None
        )
        return [RankingItem.model_validate(r) for r in data.get("ranking_list", [])]

    async def get_my_score_stats(self, group_id: int) -> Dict[str, Any]:
        """获取我的积分统计"""
        data = await self._client.get(f"/v2/dashboard/groups/{group_id}/scoreboard/statistics/self")
        return data.get("statistics", {})

    async def get_scoreboard_settings(self, group_id: int) -> ScoreboardSettings:
        """获取积分榜设置"""
        data = await self._client.get(f"/v2/dashboard/groups/{group_id}/scoreboard/settings")
        return ScoreboardSettings.model_validate(data)

    async def get_invitation_ranking(
        self, group_id: int, options: Optional[ListRankingOptions] = None
    ) -> List[RankingItem]:
        """获取邀请排行榜"""
        params: Dict[str, Any] = {}
        if options:
            if options.type:
                params["type"] = options.type
            if options.index is not None:
                params["index"] = options.index

        data = await self._client.get(
            f"/v2/groups/{group_id}/invitations/ranking_list", params or None
        )
        return [RankingItem.model_validate(r) for r in data.get("ranking_list", [])]

    async def get_contribution_ranking(
        self, group_id: int, options: Optional[ListRankingOptions] = None
    ) -> List[RankingItem]:
        """获取贡献排行榜"""
        params: Dict[str, Any] = {}
        if options:
            if options.type:
                params["type"] = options.type
            if options.index is not None:
                params["index"] = options.index

        data = await self._client.get(
            f"/v2/groups/{group_id}/contribution_ranking_list", params or None
        )
        return [RankingItem.model_validate(r) for r in data.get("ranking_list", [])]

    async def get_global_ranking(self, rank_type: str, count: int) -> Dict[str, Any]:
        """获取全局星球排行榜（v3接口）

        Args:
            rank_type: 排行类型 - group_sales_list(畅销榜), new_star_list(新星榜),
                      paid_group_active_list(活跃榜), group_fortune_list(财富榜)
            count: 返回数量

        Returns:
            排行数据
        """
        params = {"type": rank_type, "count": count}
        return await self._client.get("/v3/groups/ranking_list", params)


@dataclass
class ListActivitiesOptions:
    """动态列表查询参数"""
    count: Optional[int] = None
    end_time: Optional[str] = None


class MiscRequest:
    """杂项请求模块"""

    def __init__(self, client: HttpClient):
        self._client = client

    async def get_global_config(self) -> GlobalConfig:
        """获取全局配置"""
        data = await self._client.get("/v2/global/config")
        return GlobalConfig.model_validate(data.get("config", {}))

    async def get_activities(self, options: Optional[ListActivitiesOptions] = None) -> List[Activity]:
        """获取用户动态"""
        params: Dict[str, Any] = {}
        if options:
            if options.count is not None:
                params["count"] = options.count
            if options.end_time:
                params["end_time"] = options.end_time

        data = await self._client.get("/v2/activities", params or None)
        return [Activity.model_validate(a) for a in data.get("activities", [])]

    async def get_pk_group(self, group_id: int) -> PkGroup:
        """获取 PK 群组信息"""
        data = await self._client.get(f"/v2/pk/groups/{group_id}")
        return PkGroup.model_validate(data.get("pk_group", {}))
