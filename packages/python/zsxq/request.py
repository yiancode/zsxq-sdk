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

        data = await self._client.get(
            f"/v2/groups/{group_id}/checkins/{checkin_id}/topics", params or None
        )
        return [Topic.model_validate(t) for t in data.get("topics", [])]


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
