"""Request 模块测试"""

import pytest
from unittest.mock import AsyncMock, Mock
from zsxq.request import (
    GroupsRequest,
    TopicsRequest,
    CheckinsRequest,
    UsersRequest,
    RankingRequest,
    MiscRequest,
    ListTopicsOptions,
    ListCheckinsOptions,
    ListRankingOptions,
    ListActivitiesOptions,
)
from zsxq.http import HttpClient


@pytest.fixture
def mock_http_client():
    """创建 mock HTTP 客户端"""
    client = Mock(spec=HttpClient)
    client.get = AsyncMock()
    client.post = AsyncMock()
    return client


@pytest.fixture
def groups_request(mock_http_client):
    """创建 GroupsRequest 实例"""
    return GroupsRequest(mock_http_client)


@pytest.fixture
def topics_request(mock_http_client):
    """创建 TopicsRequest 实例"""
    return TopicsRequest(mock_http_client)


@pytest.fixture
def checkins_request(mock_http_client):
    """创建 CheckinsRequest 实例"""
    return CheckinsRequest(mock_http_client)


@pytest.fixture
def users_request(mock_http_client):
    """创建 UsersRequest 实例"""
    return UsersRequest(mock_http_client)


@pytest.fixture
def ranking_request(mock_http_client):
    """创建 RankingRequest 实例"""
    return RankingRequest(mock_http_client)


@pytest.fixture
def misc_request(mock_http_client):
    """创建 MiscRequest 实例"""
    return MiscRequest(mock_http_client)


# ==================== GroupsRequest Tests ====================


@pytest.mark.asyncio
async def test_groups_list(groups_request, mock_http_client):
    """测试获取星球列表"""
    mock_http_client.get.return_value = {
        "groups": [
            {"group_id": 123, "name": "测试星球1", "type": "free", "description": "描述1", "background_url": "url1", "create_time": "2024-01-01T00:00:00Z"},
            {"group_id": 456, "name": "测试星球2", "type": "pay", "description": "描述2", "background_url": "url2", "create_time": "2024-01-01T00:00:00Z"},
        ]
    }

    groups = await groups_request.list()

    mock_http_client.get.assert_called_once_with("/v2/groups")
    assert len(groups) == 2
    assert groups[0].name == "测试星球1"
    assert groups[1].name == "测试星球2"


@pytest.mark.asyncio
async def test_groups_list_empty(groups_request, mock_http_client):
    """测试获取空星球列表"""
    mock_http_client.get.return_value = {"groups": []}

    groups = await groups_request.list()

    assert len(groups) == 0


@pytest.mark.asyncio
async def test_groups_get(groups_request, mock_http_client):
    """测试获取星球详情"""
    mock_http_client.get.return_value = {
        "group": {
            "group_id": 123,
            "name": "测试星球",
            "type": "free",
            "description": "描述",
            "background_url": "url",
            "create_time": "2024-01-01T00:00:00Z",
        }
    }

    group = await groups_request.get(123)

    mock_http_client.get.assert_called_once_with("/v2/groups/123")
    assert group.name == "测试星球"
    assert group.group_id == 123


@pytest.mark.asyncio
async def test_groups_get_statistics(groups_request, mock_http_client):
    """测试获取星球统计"""
    mock_http_client.get.return_value = {
        "members_count": 100,
        "topics_count": 50,
    }

    stats = await groups_request.get_statistics(123)

    mock_http_client.get.assert_called_once_with("/v2/groups/123/statistics")
    assert stats["members_count"] == 100
    assert stats["topics_count"] == 50


@pytest.mark.asyncio
async def test_groups_get_member(groups_request, mock_http_client):
    """测试获取成员信息"""
    mock_http_client.get.return_value = {
        "user": {
            "user_id": 999,
            "name": "测试用户",
            "avatar_url": "https://example.com/avatar.png",
        }
    }

    user = await groups_request.get_member(123, 999)

    mock_http_client.get.assert_called_once_with("/v2/groups/123/members/999")
    assert user.name == "测试用户"
    assert user.user_id == 999


@pytest.mark.asyncio
async def test_groups_get_hashtags(groups_request, mock_http_client):
    """测试获取星球标签"""
    mock_http_client.get.return_value = {
        "hashtags": [
            {"hashtag_id": 1, "name": "标签1"},
            {"hashtag_id": 2, "name": "标签2"},
        ]
    }

    hashtags = await groups_request.get_hashtags(123)

    mock_http_client.get.assert_called_once_with("/v2/groups/123/hashtags")
    assert len(hashtags) == 2
    assert hashtags[0].name == "标签1"


@pytest.mark.asyncio
async def test_groups_get_unread_count(groups_request, mock_http_client):
    """测试获取未读话题数"""
    mock_http_client.get.return_value = {
        "123": 5,
        "456": 10,
    }

    counts = await groups_request.get_unread_count()

    mock_http_client.get.assert_called_once_with("/v2/groups/unread_topics_count")
    assert counts["123"] == 5
    assert counts["456"] == 10


# ==================== TopicsRequest Tests ====================


@pytest.mark.asyncio
async def test_topics_list(topics_request, mock_http_client):
    """测试获取话题列表"""
    mock_http_client.get.return_value = {
        "topics": [
            {"topic_id": 1, "type": "talk", "create_time": "2024-01-01T00:00:00Z"},
            {"topic_id": 2, "type": "q&a", "create_time": "2024-01-02T00:00:00Z"},
        ]
    }

    topics = await topics_request.list(123)

    mock_http_client.get.assert_called_once_with("/v2/groups/123/topics", None)
    assert len(topics) == 2


@pytest.mark.asyncio
async def test_topics_list_with_options(topics_request, mock_http_client):
    """测试带参数获取话题列表"""
    mock_http_client.get.return_value = {"topics": []}

    options = ListTopicsOptions(count=10, scope="digests")
    await topics_request.list(123, options)

    mock_http_client.get.assert_called_once_with(
        "/v2/groups/123/topics", {"count": 10, "scope": "digests"}
    )


@pytest.mark.asyncio
async def test_topics_get(topics_request, mock_http_client):
    """测试获取话题详情"""
    mock_http_client.get.return_value = {
        "topic": {
            "topic_id": 1,
            "type": "talk",
            "create_time": "2024-01-01T00:00:00Z",
        }
    }

    topic = await topics_request.get(1)

    mock_http_client.get.assert_called_once_with("/v2/topics/1")
    assert topic.topic_id == 1


@pytest.mark.asyncio
async def test_topics_get_comments(topics_request, mock_http_client):
    """测试获取话题评论"""
    mock_http_client.get.return_value = {
        "comments": [
            {"comment_id": 1, "text": "评论1", "create_time": "2024-01-01T00:00:00Z", "owner": {"user_id": 1, "name": "用户1", "avatar_url": "url1"}},
            {"comment_id": 2, "text": "评论2", "create_time": "2024-01-02T00:00:00Z", "owner": {"user_id": 2, "name": "用户2", "avatar_url": "url2"}},
        ]
    }

    comments = await topics_request.get_comments(1)

    mock_http_client.get.assert_called_once_with("/v2/topics/1/comments", None)
    assert len(comments) == 2


# ==================== CheckinsRequest Tests ====================


@pytest.mark.asyncio
async def test_checkins_list(checkins_request, mock_http_client):
    """测试获取打卡项目列表"""
    mock_http_client.get.return_value = {
        "checkins": [
            {"checkin_id": 1, "name": "打卡1", "status": "ongoing"},
            {"checkin_id": 2, "name": "打卡2", "status": "ongoing"},
        ]
    }

    checkins = await checkins_request.list(123)

    mock_http_client.get.assert_called_once_with("/v2/groups/123/checkins", None)
    assert len(checkins) == 2


@pytest.mark.asyncio
async def test_checkins_list_with_options(checkins_request, mock_http_client):
    """测试带参数获取打卡项目"""
    mock_http_client.get.return_value = {"checkins": []}

    options = ListCheckinsOptions(scope="ongoing", count=5)
    await checkins_request.list(123, options)

    mock_http_client.get.assert_called_once_with(
        "/v2/groups/123/checkins", {"scope": "ongoing", "count": 5}
    )


@pytest.mark.asyncio
async def test_checkins_get(checkins_request, mock_http_client):
    """测试获取打卡项目详情"""
    mock_http_client.get.return_value = {
        "checkin": {"checkin_id": 1, "name": "打卡项目", "status": "ongoing"}
    }

    checkin = await checkins_request.get(123, 1)

    mock_http_client.get.assert_called_once_with("/v2/groups/123/checkins/1")
    assert checkin.checkin_id == 1


@pytest.mark.asyncio
async def test_checkins_get_statistics(checkins_request, mock_http_client):
    """测试获取打卡统计"""
    mock_http_client.get.return_value = {
        "joined_count": 100,
        "completed_count": 80,
        "checkined_count": 10,
    }

    stats = await checkins_request.get_statistics(123, 1)

    mock_http_client.get.assert_called_once_with("/v2/groups/123/checkins/1/statistics")


@pytest.mark.asyncio
async def test_checkins_get_ranking_list(checkins_request, mock_http_client):
    """测试获取打卡排行榜"""
    mock_http_client.get.return_value = {
        "ranking_list": [
            {"rank": 1, "count": 10, "user": {"user_id": 1, "name": "用户1", "avatar_url": "url1"}},
            {"rank": 2, "count": 8, "user": {"user_id": 2, "name": "用户2", "avatar_url": "url2"}},
        ]
    }

    ranking = await checkins_request.get_ranking_list(123, 1)

    mock_http_client.get.assert_called_once_with(
        "/v2/groups/123/checkins/1/ranking_list", None
    )
    assert len(ranking) == 2


# ==================== UsersRequest Tests ====================


@pytest.mark.asyncio
async def test_users_self(users_request, mock_http_client):
    """测试获取当前用户信息"""
    mock_http_client.get.return_value = {
        "user": {"user_id": 123, "name": "当前用户", "avatar_url": "https://example.com/avatar.png"}
    }

    user = await users_request.self_()

    mock_http_client.get.assert_called_once_with("/v3/users/self")
    assert user.name == "当前用户"


@pytest.mark.asyncio
async def test_users_get(users_request, mock_http_client):
    """测试获取指定用户信息"""
    mock_http_client.get.return_value = {
        "user": {"user_id": 456, "name": "其他用户", "avatar_url": "https://example.com/avatar.png"}
    }

    user = await users_request.get(456)

    mock_http_client.get.assert_called_once_with("/v3/users/456")
    assert user.name == "其他用户"


@pytest.mark.asyncio
async def test_users_get_statistics(users_request, mock_http_client):
    """测试获取用户统计"""
    mock_http_client.get.return_value = {
        "topics_count": 100,
        "comments_count": 50,
    }

    stats = await users_request.get_statistics(456)

    mock_http_client.get.assert_called_once_with("/v3/users/456/statistics")
    assert stats["topics_count"] == 100


# ==================== RankingRequest Tests ====================


@pytest.mark.asyncio
async def test_ranking_get_group_ranking(ranking_request, mock_http_client):
    """测试获取星球排行榜"""
    mock_http_client.get.return_value = {
        "ranking_list": [
            {"rank": 1, "count": 10, "user": {"user_id": 1, "name": "用户1", "avatar_url": "url1"}},
            {"rank": 2, "count": 8, "user": {"user_id": 2, "name": "用户2", "avatar_url": "url2"}},
        ]
    }

    ranking = await ranking_request.get_group_ranking(123)

    mock_http_client.get.assert_called_once_with("/v2/groups/123/ranking_list", None)
    assert len(ranking) == 2


@pytest.mark.asyncio
async def test_ranking_get_group_ranking_with_options(ranking_request, mock_http_client):
    """测试带参数获取星球排行榜"""
    mock_http_client.get.return_value = {"ranking_list": []}

    options = ListRankingOptions(type="continuous", index=0)
    await ranking_request.get_group_ranking(123, options)

    mock_http_client.get.assert_called_once_with(
        "/v2/groups/123/ranking_list", {"type": "continuous", "index": 0}
    )


@pytest.mark.asyncio
async def test_ranking_get_group_ranking_stats(ranking_request, mock_http_client):
    """测试获取排行统计"""
    mock_http_client.get.return_value = {
        "statistics": {"total_members": 100, "ranking_members": 50}
    }

    await ranking_request.get_group_ranking_stats(123)

    mock_http_client.get.assert_called_once_with("/v2/groups/123/ranking_list/statistics")


@pytest.mark.asyncio
async def test_ranking_get_score_ranking(ranking_request, mock_http_client):
    """测试获取积分排行榜"""
    mock_http_client.get.return_value = {
        "ranking_list": [
            {"rank": 1, "count": 1000, "user": {"user_id": 1, "name": "用户1", "avatar_url": "url1"}},
        ]
    }

    ranking = await ranking_request.get_score_ranking(123)

    mock_http_client.get.assert_called_once_with(
        "/v2/groups/123/scoreboard/ranking_list", None
    )
    assert len(ranking) == 1


@pytest.mark.asyncio
async def test_ranking_get_my_score_stats(ranking_request, mock_http_client):
    """测试获取我的积分统计"""
    mock_http_client.get.return_value = {"my_score": 500, "rank": 10}

    stats = await ranking_request.get_my_score_stats(123)

    mock_http_client.get.assert_called_once_with("/v2/groups/123/scoreboard/my_statistics")
    assert stats["my_score"] == 500


@pytest.mark.asyncio
async def test_ranking_get_scoreboard_settings(ranking_request, mock_http_client):
    """测试获取积分榜设置"""
    mock_http_client.get.return_value = {
        "settings": {"enabled": True, "reset_period": "weekly"}
    }

    await ranking_request.get_scoreboard_settings(123)

    mock_http_client.get.assert_called_once_with("/v2/groups/123/scoreboard/settings")


@pytest.mark.asyncio
async def test_ranking_get_invitation_ranking(ranking_request, mock_http_client):
    """测试获取邀请排行榜"""
    mock_http_client.get.return_value = {
        "ranking_list": [{"rank": 1, "count": 10, "user": {"user_id": 1, "name": "用户1", "avatar_url": "url1"}}]
    }

    ranking = await ranking_request.get_invitation_ranking(123)

    mock_http_client.get.assert_called_once_with(
        "/v2/groups/123/invitation_ranking_list", None
    )
    assert len(ranking) == 1


@pytest.mark.asyncio
async def test_ranking_get_contribution_ranking(ranking_request, mock_http_client):
    """测试获取贡献排行榜"""
    mock_http_client.get.return_value = {
        "ranking_list": [{"rank": 1, "count": 500, "user": {"user_id": 1, "name": "用户1", "avatar_url": "url1"}}]
    }

    ranking = await ranking_request.get_contribution_ranking(123)

    mock_http_client.get.assert_called_once_with(
        "/v2/groups/123/contribution_ranking_list", None
    )
    assert len(ranking) == 1


# ==================== MiscRequest Tests ====================


@pytest.mark.asyncio
async def test_misc_get_global_config(misc_request, mock_http_client):
    """测试获取全局配置"""
    mock_http_client.get.return_value = {
        "config": {"version": "1.0.0", "features": {}}
    }

    await misc_request.get_global_config()

    mock_http_client.get.assert_called_once_with("/v2/global/config")


@pytest.mark.asyncio
async def test_misc_get_activities(misc_request, mock_http_client):
    """测试获取用户动态"""
    mock_http_client.get.return_value = {
        "activities": [
            {"activity_id": 1, "type": "topic"},
            {"activity_id": 2, "type": "comment"},
        ]
    }

    activities = await misc_request.get_activities()

    mock_http_client.get.assert_called_once_with("/v2/activities", None)
    assert len(activities) == 2


@pytest.mark.asyncio
async def test_misc_get_activities_with_options(misc_request, mock_http_client):
    """测试带参数获取用户动态"""
    mock_http_client.get.return_value = {"activities": []}

    options = ListActivitiesOptions(count=20, end_time="2024-01-01T00:00:00Z")
    await misc_request.get_activities(options)

    mock_http_client.get.assert_called_once_with(
        "/v2/activities", {"count": 20, "end_time": "2024-01-01T00:00:00Z"}
    )


@pytest.mark.asyncio
async def test_misc_get_pk_group(misc_request, mock_http_client):
    """测试获取 PK 群组信息"""
    mock_http_client.get.return_value = {
        "pk_group": {"pk_group_id": 1, "name": "PK群组"}
    }

    await misc_request.get_pk_group(1)

    mock_http_client.get.assert_called_once_with("/v2/pk/groups/1")
