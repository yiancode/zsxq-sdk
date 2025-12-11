# zsxq-sdk-python

> 知识星球 Python SDK - 异步、类型安全的 API 客户端

[![Python Version](https://img.shields.io/badge/python-%3E%3D3.10-yellow)](https://python.org/)
[![License](https://img.shields.io/badge/license-MIT-green)](LICENSE)

## 特性

- **异步支持** - 基于 asyncio 的异步 API
- **类型安全** - 完整的 Pydantic 类型定义
- **Builder 模式** - 灵活的客户端配置
- **模块化设计** - 按需使用各功能模块
- **自动重试** - 内置网络错误重试机制

## 安装

```bash
pip install zsxq-sdk
```

## 快速开始

```python
import asyncio
import os
from zsxq import ZsxqClientBuilder

async def main():
    # 创建客户端
    client = ZsxqClientBuilder() \
        .set_token(os.environ["ZSXQ_TOKEN"]) \
        .set_timeout(10) \
        .set_retry_count(3) \
        .build()

    # 获取我的星球列表
    groups = await client.groups.list()
    for g in groups:
        print(f"星球: {g.name}")

    # 获取当前用户信息
    me = await client.users.self_()
    print(f"用户: {me.name}")

asyncio.run(main())
```

## 核心功能

### 星球管理

```python
# 获取星球列表
groups = await client.groups.list()

# 获取星球详情
group = await client.groups.get(group_id)

# 获取星球统计
stats = await client.groups.get_statistics(group_id)

# 获取星球标签
hashtags = await client.groups.get_hashtags(group_id)
```

### 话题操作

```python
from zsxq.request import ListTopicsOptions

# 获取话题列表
topics = await client.topics.list(group_id)

# 带参数的话题列表
options = ListTopicsOptions(count=20, scope="all")
topics = await client.topics.list(group_id, options)

# 获取话题详情
topic = await client.topics.get(topic_id)

# 获取话题评论
comments = await client.topics.get_comments(topic_id)
```

### 打卡管理

```python
from zsxq.request import ListCheckinsOptions

# 获取打卡项目列表
checkins = await client.checkins.list(group_id)

# 获取打卡统计
stats = await client.checkins.get_statistics(group_id, checkin_id)

# 获取打卡排行榜
ranking = await client.checkins.get_ranking_list(group_id, checkin_id)
```

### 排行榜

```python
from zsxq.request import ListRankingOptions

# 获取星球排行榜
ranking = await client.ranking.get_group_ranking(group_id)

# 获取积分排行榜
score_ranking = await client.ranking.get_score_ranking(group_id)

# 获取我的积分统计
my_stats = await client.ranking.get_my_score_stats(group_id)
```

## 错误处理

```python
from zsxq.exception import (
    ZsxqException,
    TokenExpiredException,
    RateLimitException,
)

try:
    groups = await client.groups.list()
except TokenExpiredException:
    # Token 过期，需要重新获取
    print("Token 已过期")
except RateLimitException:
    # 触发限流
    print("请求过于频繁")
except ZsxqException as e:
    # 其他 API 错误
    print(f"错误码: {e.code}, 信息: {e.message}")
```

## 开发

```bash
# 安装开发依赖
pip install -e ".[dev]"

# 运行测试
pytest

# 运行单个测试文件
pytest tests/test_request.py -v

# 类型检查
mypy zsxq

# 格式化代码
black zsxq
```

## 许可证

MIT License

---

**注意**: 本项目仅供学习和研究使用，请遵守知识星球的服务条款。
