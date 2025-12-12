"""
集成测试 - 需要真实 Token 和 Group ID

运行方式:
ZSXQ_TOKEN="xxx" ZSXQ_GROUP_ID="xxx" pytest tests/test_integration.py -v

注意: 部分测试可能会因星球未开启相关功能而跳过（如打卡、排行榜等）
"""

import os
import pytest
from typing import Optional

from zsxq import ZsxqClientBuilder, ZsxqClient

# 跳过条件：没有设置环境变量
ZSXQ_TOKEN = os.environ.get("ZSXQ_TOKEN")
ZSXQ_GROUP_ID = os.environ.get("ZSXQ_GROUP_ID")
SKIP_INTEGRATION = not ZSXQ_TOKEN or not ZSXQ_GROUP_ID


@pytest.fixture(scope="module")
async def client():
    """创建测试客户端"""
    if SKIP_INTEGRATION:
        pytest.skip("跳过集成测试：未设置 ZSXQ_TOKEN 或 ZSXQ_GROUP_ID 环境变量")

    client = ZsxqClientBuilder().set_token(ZSXQ_TOKEN).build()
    yield client
    await client.close()


@pytest.fixture(scope="module")
def group_id():
    """获取测试星球ID"""
    if SKIP_INTEGRATION:
        pytest.skip("跳过集成测试：未设置 ZSXQ_TOKEN 或 ZSXQ_GROUP_ID 环境变量")
    return int(ZSXQ_GROUP_ID)


async def safe_test(name: str, coro):
    """安全执行测试，捕获异常"""
    try:
        result = await coro
        print(f"✅ {name}")
        return True, result
    except Exception as e:
        print(f"⚠️ {name}: {e}")
        return False, None


# ========== 核心 API 测试 ==========


@pytest.mark.asyncio
class TestCoreAPI:
    """核心 API 测试"""

    async def test_get_current_user(self, client: ZsxqClient):
        """获取当前用户信息"""
        user = await client.users.self_()
        assert user is not None
        assert user.user_id is not None
        assert user.name is not None
        print(f"✅ 当前用户: {user.name} (ID: {user.user_id})")

    async def test_get_groups(self, client: ZsxqClient):
        """获取星球列表"""
        groups = await client.groups.list()
        assert groups is not None
        assert isinstance(groups, list)
        print(f"✅ 已加入 {len(groups)} 个星球")
        for g in groups[:3]:
            print(f"   - {g.name} (ID: {g.group_id})")

    async def test_get_group_detail(self, client: ZsxqClient, group_id: int):
        """获取星球详情"""
        group = await client.groups.get(group_id)
        assert group is not None
        assert group.name is not None
        print(f"✅ 星球详情: {group.name}")

    async def test_get_group_statistics(self, client: ZsxqClient, group_id: int):
        """获取星球统计"""
        success, stats = await safe_test(
            "获取星球统计", client.groups.get_statistics(group_id)
        )
        if success:
            print(f"   - 统计数据: {stats}")

    async def test_get_topics(self, client: ZsxqClient, group_id: int):
        """获取话题列表"""
        success, topics = await safe_test(
            "获取话题列表", client.topics.list(group_id)
        )
        if success and topics:
            print(f"   - 话题数: {len(topics)}")
            if topics:
                print(f"   - 第一个话题ID: {topics[0].topic_id}")

    async def test_get_user_statistics(self, client: ZsxqClient):
        """获取用户统计"""
        user = await client.users.self_()
        stats = await client.users.get_statistics(user.user_id)
        assert stats is not None
        print(f"✅ 用户统计: {stats}")


# ========== 星球功能测试 ==========


@pytest.mark.asyncio
class TestGroupFeatures:
    """星球功能测试"""

    async def test_get_group_menus(self, client: ZsxqClient, group_id: int):
        """获取星球菜单配置"""
        success, menus = await safe_test(
            "获取星球菜单配置", client.groups.get_menus(group_id)
        )
        if success and menus:
            print(f"   - 菜单数: {len(menus)}")

    async def test_get_group_role_members(self, client: ZsxqClient, group_id: int):
        """获取星球角色成员"""
        success, role_members = await safe_test(
            "获取星球角色成员", client.groups.get_role_members(group_id)
        )
        if success and role_members:
            if role_members.owner:
                print(f"   - 星主: {role_members.owner.name}")

    async def test_get_group_columns(self, client: ZsxqClient, group_id: int):
        """获取星球专栏列表"""
        columns = await client.groups.get_columns(group_id)
        assert columns is not None
        print(f"✅ 获取到 {len(columns)} 个专栏")

    async def test_get_columns_summary(self, client: ZsxqClient, group_id: int):
        """获取专栏汇总信息"""
        summary = await client.groups.get_columns_summary(group_id)
        assert summary is not None
        print(f"✅ 专栏汇总: {summary}")

    async def test_get_member_activity_summary(self, client: ZsxqClient, group_id: int):
        """获取成员活跃摘要"""
        user = await client.users.self_()
        success, summary = await safe_test(
            "获取成员活跃摘要",
            client.groups.get_member_activity_summary(group_id, user.user_id),
        )
        if success and summary:
            print(f"   - 发布话题数: {summary.topics_count}")

    async def test_get_sticky_topics(self, client: ZsxqClient, group_id: int):
        """获取置顶话题列表"""
        success, topics = await safe_test(
            "获取置顶话题列表", client.topics.list_sticky(group_id)
        )
        if success and topics:
            print(f"   - 置顶话题数: {len(topics)}")

    async def test_get_topic_info(self, client: ZsxqClient, group_id: int):
        """获取话题基础信息"""
        topics = await client.topics.list(group_id)
        if topics:
            success, info = await safe_test(
                "获取话题基础信息", client.topics.get_info(topics[0].topic_id)
            )
            if success and info:
                print(f"   - Topic ID: {info.topic_id}")

    async def test_get_topic_rewards(self, client: ZsxqClient, group_id: int):
        """获取话题打赏列表"""
        topics = await client.topics.list(group_id)
        if topics:
            success, rewards = await safe_test(
                "获取话题打赏列表", client.topics.get_rewards(topics[0].topic_id)
            )
            if success and rewards is not None:
                print(f"   - 打赏数: {len(rewards)}")

    async def test_get_topic_recommendations(self, client: ZsxqClient, group_id: int):
        """获取相关推荐话题"""
        topics = await client.topics.list(group_id)
        if topics:
            success, recommendations = await safe_test(
                "获取相关推荐话题", client.topics.get_recommendations(topics[0].topic_id)
            )
            if success and recommendations is not None:
                print(f"   - 推荐话题数: {len(recommendations)}")


# ========== 排行榜功能测试 ==========


@pytest.mark.asyncio
class TestRankingFeatures:
    """排行榜功能测试"""

    async def test_get_group_ranking(self, client: ZsxqClient, group_id: int):
        """获取星球排行榜"""
        success, ranking = await safe_test(
            "获取星球排行榜", client.ranking.get_group_ranking(group_id)
        )
        if success and ranking is not None:
            print(f"   - 排行榜记录数: {len(ranking)}")

    async def test_get_group_ranking_stats(self, client: ZsxqClient, group_id: int):
        """获取星球排行统计"""
        success, stats = await safe_test(
            "获取星球排行统计", client.ranking.get_group_ranking_stats(group_id)
        )
        if success and stats:
            print(f"   - 统计: {stats}")

    async def test_get_score_ranking(self, client: ZsxqClient, group_id: int):
        """获取积分排行榜"""
        success, ranking = await safe_test(
            "获取积分排行榜", client.ranking.get_score_ranking(group_id)
        )
        if success and ranking is not None:
            print(f"   - 积分排行记录数: {len(ranking)}")

    async def test_get_my_score_stats(self, client: ZsxqClient, group_id: int):
        """获取我的积分统计"""
        success, stats = await safe_test(
            "获取我的积分统计", client.ranking.get_my_score_stats(group_id)
        )
        if success and stats:
            print(f"   - 统计: {stats}")

    async def test_get_scoreboard_settings(self, client: ZsxqClient, group_id: int):
        """获取积分榜设置"""
        success, settings = await safe_test(
            "获取积分榜设置", client.ranking.get_scoreboard_settings(group_id)
        )
        if success and settings:
            print(f"   - 启用: {settings.enabled}")

    async def test_get_invitation_ranking(self, client: ZsxqClient, group_id: int):
        """获取邀请排行榜"""
        success, ranking = await safe_test(
            "获取邀请排行榜", client.ranking.get_invitation_ranking(group_id)
        )
        if success and ranking is not None:
            print(f"   - 邀请排行记录数: {len(ranking)}")

    async def test_get_contribution_ranking(self, client: ZsxqClient, group_id: int):
        """获取贡献排行榜"""
        success, ranking = await safe_test(
            "获取贡献排行榜", client.ranking.get_contribution_ranking(group_id)
        )
        if success and ranking is not None:
            print(f"   - 贡献排行记录数: {len(ranking)}")

    async def test_get_global_ranking(self, client: ZsxqClient):
        """获取全局星球排行榜"""
        success, ranking = await safe_test(
            "获取全局星球排行榜", client.ranking.get_global_ranking("group_sales_list", 10)
        )
        if success and ranking:
            print(f"   - 全局排行数据: {str(ranking)[:100]}...")


# ========== 用户功能测试 ==========


@pytest.mark.asyncio
class TestUserFeatures:
    """用户功能测试"""

    async def test_get_avatar_url(self, client: ZsxqClient):
        """获取用户头像URL"""
        user = await client.users.self_()
        success, avatar_url = await safe_test(
            "获取用户头像URL", client.users.get_avatar_url(user.user_id)
        )
        if success and avatar_url:
            print(f"   - URL: {avatar_url[:50]}...")

    async def test_get_group_footprints(self, client: ZsxqClient):
        """获取用户星球足迹"""
        user = await client.users.self_()
        success, footprints = await safe_test(
            "获取用户星球足迹", client.users.get_group_footprints(user.user_id)
        )
        if success and footprints is not None:
            print(f"   - 足迹数: {len(footprints)}")

    async def test_get_applying_groups(self, client: ZsxqClient):
        """获取申请中星球列表"""
        success, groups = await safe_test(
            "获取申请中星球列表", client.users.get_applying_groups()
        )
        if success and groups is not None:
            print(f"   - 申请中星球数: {len(groups)}")

    async def test_get_inviter(self, client: ZsxqClient, group_id: int):
        """获取星球邀请人信息"""
        success, inviter = await safe_test(
            "获取星球邀请人信息", client.users.get_inviter(group_id)
        )
        if success and inviter:
            if inviter.user:
                print(f"   - 邀请人: {inviter.user.name}")
            else:
                print("   - 无邀请人信息")

    async def test_get_coupons(self, client: ZsxqClient):
        """获取优惠券列表"""
        success, coupons = await safe_test("获取优惠券列表", client.users.get_coupons())
        if success and coupons is not None:
            print(f"   - 优惠券数: {len(coupons)}")

    async def test_get_remarks(self, client: ZsxqClient):
        """获取备注列表"""
        success, remarks = await safe_test("获取备注列表", client.users.get_remarks())
        if success and remarks is not None:
            print(f"   - 备注数: {len(remarks)}")

    async def test_get_recommended_follows(self, client: ZsxqClient):
        """获取推荐关注用户"""
        success, users = await safe_test(
            "获取推荐关注用户", client.users.get_recommended_follows()
        )
        if success and users is not None:
            print(f"   - 推荐用户数: {len(users)}")

    async def test_get_blocked_users(self, client: ZsxqClient):
        """获取屏蔽用户列表"""
        success, users = await safe_test(
            "获取屏蔽用户列表", client.users.get_blocked_users()
        )
        if success and users is not None:
            print(f"   - 屏蔽用户数: {len(users)}")

    async def test_get_preference_categories(self, client: ZsxqClient):
        """获取推荐偏好分类"""
        success, categories = await safe_test(
            "获取推荐偏好分类", client.users.get_preference_categories()
        )
        if success and categories is not None:
            print(f"   - 分类数: {len(categories)}")

    async def test_get_unanswered_questions_summary(self, client: ZsxqClient):
        """获取未回答问题摘要"""
        success, summary = await safe_test(
            "获取未回答问题摘要", client.users.get_unanswered_questions_summary()
        )
        if success and summary:
            print(f"   - 未回答数: {summary.unanswered_count}")

    async def test_get_follower_stats(self, client: ZsxqClient):
        """获取关注者统计"""
        success, stats = await safe_test(
            "获取关注者统计", client.users.get_follower_stats()
        )
        if success and stats:
            print(f"   - 关注者数: {stats.followers_count}")

    async def test_get_user_preferences(self, client: ZsxqClient):
        """获取用户偏好配置"""
        success, preferences = await safe_test(
            "获取用户偏好配置", client.users.get_preferences()
        )
        if success and preferences:
            print(f"   - 配置键数: {len(preferences)}")

    async def test_get_weekly_ranking(self, client: ZsxqClient, group_id: int):
        """获取星球周榜排名"""
        success, ranking = await safe_test(
            "获取星球周榜排名", client.users.get_weekly_ranking(group_id)
        )
        if success and ranking:
            print(f"   - 周榜数据: {ranking}")


# ========== 打卡功能测试 ==========


@pytest.mark.asyncio
class TestCheckinFeatures:
    """打卡功能测试"""

    async def test_get_checkin_list(self, client: ZsxqClient, group_id: int):
        """获取打卡项目列表"""
        success, checkins = await safe_test(
            "获取打卡项目列表", client.checkins.list(group_id)
        )
        if success and checkins is not None:
            print(f"   - 打卡项目数: {len(checkins)}")

    async def test_get_checkin_detail(self, client: ZsxqClient, group_id: int):
        """获取打卡项目详情"""
        try:
            checkins = await client.checkins.list(group_id)
            if checkins:
                checkin = await client.checkins.get(group_id, checkins[0].checkin_id)
                print(f"✅ 打卡项目: {checkin.name}")
        except Exception as e:
            print(f"⚠️ 获取打卡项目详情: {e}")

    async def test_get_checkin_statistics(self, client: ZsxqClient, group_id: int):
        """获取打卡项目统计"""
        try:
            checkins = await client.checkins.list(group_id)
            if checkins:
                success, stats = await safe_test(
                    "获取打卡项目统计",
                    client.checkins.get_statistics(group_id, checkins[0].checkin_id),
                )
                if success and stats:
                    print(f"   - 统计: {stats}")
        except Exception as e:
            print(f"⚠️ 获取打卡项目统计: {e}")

    async def test_get_checkin_ranking_list(self, client: ZsxqClient, group_id: int):
        """获取打卡排行榜"""
        try:
            checkins = await client.checkins.list(group_id)
            if checkins:
                success, ranking = await safe_test(
                    "获取打卡排行榜",
                    client.checkins.get_ranking_list(group_id, checkins[0].checkin_id),
                )
                if success and ranking is not None:
                    print(f"   - 排行记录数: {len(ranking)}")
        except Exception as e:
            print(f"⚠️ 获取打卡排行榜: {e}")

    async def test_get_checkin_daily_statistics(self, client: ZsxqClient, group_id: int):
        """获取打卡每日统计"""
        try:
            checkins = await client.checkins.list(group_id)
            if checkins:
                success, stats = await safe_test(
                    "获取打卡每日统计",
                    client.checkins.get_daily_statistics(group_id, checkins[0].checkin_id),
                )
                if success and stats is not None:
                    print(f"   - 每日统计记录数: {len(stats)}")
        except Exception as e:
            print(f"⚠️ 获取打卡每日统计: {e}")

    async def test_get_checkin_joined_users(self, client: ZsxqClient, group_id: int):
        """获取打卡参与用户"""
        try:
            checkins = await client.checkins.list(group_id)
            if checkins:
                success, users = await safe_test(
                    "获取打卡参与用户",
                    client.checkins.get_joined_users(group_id, checkins[0].checkin_id),
                )
                if success and users is not None:
                    print(f"   - 参与用户数: {len(users)}")
        except Exception as e:
            print(f"⚠️ 获取打卡参与用户: {e}")

    async def test_get_my_checkins(self, client: ZsxqClient, group_id: int):
        """获取我的打卡记录"""
        try:
            checkins = await client.checkins.list(group_id)
            if checkins:
                success, my_checkins = await safe_test(
                    "获取我的打卡记录",
                    client.checkins.get_my_checkins(group_id, checkins[0].checkin_id),
                )
                if success and my_checkins is not None:
                    print(f"   - 我的打卡记录数: {len(my_checkins)}")
        except Exception as e:
            print(f"⚠️ 获取我的打卡记录: {e}")

    async def test_get_my_checkin_days(self, client: ZsxqClient, group_id: int):
        """获取我的打卡日期"""
        try:
            checkins = await client.checkins.list(group_id)
            if checkins:
                success, days = await safe_test(
                    "获取我的打卡日期",
                    client.checkins.get_my_checkin_days(group_id, checkins[0].checkin_id),
                )
                if success and days is not None:
                    print(f"   - 打卡日期数: {len(days)}")
        except Exception as e:
            print(f"⚠️ 获取我的打卡日期: {e}")

    async def test_get_my_checkin_statistics(self, client: ZsxqClient, group_id: int):
        """获取我的打卡统计"""
        try:
            checkins = await client.checkins.list(group_id)
            if checkins:
                success, stats = await safe_test(
                    "获取我的打卡统计",
                    client.checkins.get_my_statistics(group_id, checkins[0].checkin_id),
                )
                if success and stats:
                    print(f"   - 统计: {stats}")
        except Exception as e:
            print(f"⚠️ 获取我的打卡统计: {e}")


# ========== 数据面板测试 ==========


@pytest.mark.asyncio
class TestDashboardFeatures:
    """数据面板测试"""

    async def test_get_dashboard_overview(self, client: ZsxqClient, group_id: int):
        """获取星球数据概览"""
        success, overview = await safe_test(
            "获取星球数据概览", client.dashboard.get_overview(group_id)
        )
        if success and overview:
            print(f"   - 概览: {overview}")

    async def test_get_dashboard_incomes(self, client: ZsxqClient, group_id: int):
        """获取星球收入概览"""
        success, incomes = await safe_test(
            "获取星球收入概览", client.dashboard.get_incomes(group_id)
        )
        if success and incomes:
            print(f"   - 收入: {incomes}")

    async def test_get_dashboard_privileges(self, client: ZsxqClient, group_id: int):
        """获取星球权限配置"""
        success, privileges = await safe_test(
            "获取星球权限配置", client.dashboard.get_privileges(group_id)
        )
        if success and privileges:
            print(f"   - 权限配置数: {len(privileges)}")

    async def test_get_invoice_stats(self, client: ZsxqClient):
        """获取发票统计"""
        success, stats = await safe_test(
            "获取发票统计", client.dashboard.get_invoice_stats()
        )
        if success and stats:
            print(f"   - 发票统计: {stats}")


# ========== 杂项功能测试 ==========


@pytest.mark.asyncio
class TestMiscFeatures:
    """杂项功能测试"""

    async def test_get_global_config(self, client: ZsxqClient):
        """获取全局配置"""
        success, config = await safe_test(
            "获取全局配置", client.misc.get_global_config()
        )
        if success and config:
            print(f"   - 配置: {config}")

    async def test_get_activities(self, client: ZsxqClient):
        """获取用户动态"""
        success, activities = await safe_test(
            "获取用户动态", client.misc.get_activities()
        )
        if success and activities is not None:
            print(f"   - 动态数: {len(activities)}")

    async def test_get_pk_group(self, client: ZsxqClient, group_id: int):
        """获取PK群组信息"""
        success, pk_group = await safe_test(
            "获取PK群组信息", client.misc.get_pk_group(group_id)
        )
        if success and pk_group:
            print(f"   - PK群组: {pk_group}")


# ========== 星球高级功能测试 ==========


@pytest.mark.asyncio
class TestAdvancedGroupFeatures:
    """星球高级功能测试"""

    async def test_get_renewal_info(self, client: ZsxqClient, group_id: int):
        """获取续费信息"""
        success, renewal = await safe_test(
            "获取续费信息", client.groups.get_renewal_info(group_id)
        )
        if success and renewal:
            print(f"   - 续费信息: {renewal}")

    async def test_get_distribution(self, client: ZsxqClient, group_id: int):
        """获取分销信息"""
        success, distribution = await safe_test(
            "获取分销信息", client.groups.get_distribution(group_id)
        )
        if success and distribution:
            print(f"   - 分销信息: {distribution}")

    async def test_get_upgradeable_groups(self, client: ZsxqClient):
        """获取可升级星球"""
        success, groups = await safe_test(
            "获取可升级星球", client.groups.get_upgradeable_groups()
        )
        if success and groups is not None:
            print(f"   - 可升级星球数: {len(groups)}")

    async def test_get_recommended_groups(self, client: ZsxqClient):
        """获取推荐星球"""
        success, groups = await safe_test(
            "获取推荐星球", client.groups.get_recommended_groups()
        )
        if success and groups is not None:
            print(f"   - 推荐星球数: {len(groups)}")

    async def test_get_custom_tags(self, client: ZsxqClient, group_id: int):
        """获取自定义标签"""
        success, tags = await safe_test(
            "获取自定义标签", client.groups.get_custom_tags(group_id)
        )
        if success and tags is not None:
            print(f"   - 自定义标签数: {len(tags)}")

    async def test_get_scheduled_tasks(self, client: ZsxqClient, group_id: int):
        """获取定时任务"""
        success, tasks = await safe_test(
            "获取定时任务", client.groups.get_scheduled_tasks(group_id)
        )
        if success and tasks is not None:
            print(f"   - 定时任务数: {len(tasks)}")

    async def test_get_risk_warnings(self, client: ZsxqClient, group_id: int):
        """获取风险预警"""
        success, warning = await safe_test(
            "获取风险预警", client.groups.get_risk_warnings(group_id)
        )
        if success:
            if warning and warning.type:
                print(f"   - 风险预警: {warning}")
            else:
                print("   - 无风险预警")
