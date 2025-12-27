# Changelog

All notable changes to the zsxq-sdk Java package will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.2.3] - 2025-12-28

### Fixed
- `CheckinsRequest.getTopics()` - 修复缺少必需的 `scope` 和 `count` 参数导致 1059 错误
- `CheckinsRequest.getRankingList()` - 修复缺少必需的 `type` 参数导致 1004 错误
- `CheckinsRequest.update()` - 修复请求体字段名错误（从 `checkin` 改为 `req_data`）

### Added
- 添加多个测试示例类用于验证修复
- 添加参数默认值自动注入机制

## [1.2.1] - 2025-12-26

### Fixed
- 训练营更新接口请求体参数名错误

### Added
- 新增训练营创建/更新功能的多语言 SDK 支持

## [1.2.0] - 2025-12-25

### Added
- 完整的训练营（Checkins）API 支持
- 打卡话题查询接口
- 打卡排行榜接口
- 打卡统计接口

## [1.1.0] - 2025-12-22

### Added
- 星球（Groups）基础 API
- 话题（Topics）基础 API
- 用户（Users）基础 API
- 排行榜（Ranking）基础 API

## [1.0.0] - 2025-12-20

### Added
- 初始发布
- 基础 HTTP 客户端实现
- 请求签名机制
- 异常处理体系
