"""GroupsRequest 测试"""

import pytest
from unittest.mock import AsyncMock, Mock
from zsxq.request import GroupsRequest
from zsxq.http import HttpClient


@pytest.fixture
def mock_http_client():
    """创建 mock HTTP 客户端"""
    client = Mock(spec=HttpClient)
    client.get = AsyncMock()
    return client


@pytest.fixture
def groups_request(mock_http_client):
    """创建 GroupsRequest 实例"""
    return GroupsRequest(mock_http_client)


@pytest.mark.asyncio
async def test_list(groups_request, mock_http_client):
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
async def test_list_empty(groups_request, mock_http_client):
    """测试获取空星球列表"""
    mock_http_client.get.return_value = {"groups": []}

    groups = await groups_request.list()

    assert len(groups) == 0


@pytest.mark.asyncio
async def test_get(groups_request, mock_http_client):
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
async def test_get_statistics(groups_request, mock_http_client):
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
async def test_get_member(groups_request, mock_http_client):
    """测试获取成员信息"""
    mock_http_client.get.return_value = {
        "user": {
            "user_id": 999,
            "name": "测试用户",
        }
    }

    user = await groups_request.get_member(123, 999)

    mock_http_client.get.assert_called_once_with("/v2/groups/123/members/999")
    assert user.name == "测试用户"
    assert user.user_id == 999


@pytest.mark.asyncio
async def test_get_hashtags(groups_request, mock_http_client):
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
async def test_get_unread_count(groups_request, mock_http_client):
    """测试获取未读话题数"""
    mock_http_client.get.return_value = {
        "123": 5,
        "456": 10,
    }

    counts = await groups_request.get_unread_count()

    mock_http_client.get.assert_called_once_with("/v2/groups/unread_topics_count")
    assert counts["123"] == 5
    assert counts["456"] == 10
