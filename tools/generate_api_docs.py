#!/usr/bin/env python3
"""
API 文档生成器
从分析报告生成完整的 API 文档
"""

import json
import sys
from pathlib import Path
from collections import defaultdict
from urllib.parse import urlparse, parse_qs
from typing import Dict, List, Any
from datetime import datetime


class ApiDocGenerator:
    """API 文档生成器"""

    def __init__(self, analysis_file: str):
        self.analysis_file = Path(analysis_file)
        self.apis = []
        self.categories = {}

    def load_analysis(self):
        """加载分析报告"""
        print(f"📂 正在加载分析报告: {self.analysis_file.name}")

        with open(self.analysis_file, 'r', encoding='utf-8') as f:
            data = json.load(f)
            self.apis = data.get('apis', [])

        print(f"✅ 加载了 {len(self.apis)} 个 API 接口\n")

    def _deduplicate_all_apis(self, apis: List[Dict]) -> List[Dict]:
        """去重所有 API（基于 method + URL）"""
        seen = {}
        unique = []

        for api in apis:
            # 使用完整 URL（包含查询参数）作为唯一标识
            key = f"{api['method']}:{api['url']}"

            if key not in seen:
                seen[key] = True
                unique.append(api)

        return unique

    def classify_apis(self) -> Dict[str, List[Dict]]:
        """对 API 进行详细分类"""
        categories = defaultdict(lambda: defaultdict(list))

        # 先去重所有接口（基于 method + URL）
        unique_apis = self._deduplicate_all_apis(self.apis)
        print(f"去重后接口数: {len(unique_apis)} (原始: {len(self.apis)})")

        for api in unique_apis:
            url = api['url'].lower()
            path = urlparse(api['url']).path
            method = api['method']

            # 一级分类：业务模块
            if '/users/' in path:
                module = '用户系统'
                if 'groups' in path:
                    sub_category = '用户星球管理'
                elif 'recommendations' in path:
                    sub_category = '推荐系统'
                elif 'merchant_coupons' in path:
                    sub_category = '优惠券'
                elif 'remarks' in path:
                    sub_category = '备注管理'
                elif 'checkins' in path:
                    sub_category = '打卡签到'
                else:
                    sub_category = '用户信息'

            elif '/groups/' in path and '/topics' in path:
                module = '内容系统'
                sub_category = '话题管理'

            elif '/groups/' in path and 'ranking' in path:
                module = '排行榜系统'
                sub_category = '星球排行'

            elif '/groups/' in path and '/menus/' in path:
                module = '阅读追踪'
                sub_category = '阅读进度'

            elif '/groups/' in path and not '/users/' in path:
                module = '星球系统'
                if 'unread' in path:
                    sub_category = '未读消息'
                elif 'inviter' in path:
                    sub_category = '邀请信息'
                elif 'recommendations' in path:
                    sub_category = '星球推荐'
                elif 'upgradable' in path:
                    sub_category = '升级管理'
                elif '/pk_groups/' in path:
                    sub_category = 'PK 活动'
                else:
                    sub_category = '星球信息'

            elif 'bugly' in url:
                module = '监控系统'
                sub_category = '错误上报'

            elif 'sentry' in url or 'client-report' in url:
                module = '监控系统'
                sub_category = '性能监控'

            elif 'sa.zsxq.com' in url:
                module = '数据分析'
                sub_category = '行为追踪'

            else:
                module = '其他'
                sub_category = '未分类'

            categories[module][sub_category].append(api)

        # 转换为普通字典
        return {k: dict(v) for k, v in categories.items()}

    def extract_path_params(self, url: str) -> List[str]:
        """提取 URL 路径参数"""
        path = urlparse(url).path
        params = []

        # 查找路径中的数字（通常是 ID）
        parts = path.split('/')
        for i, part in enumerate(parts):
            if part.isdigit() and i > 0:
                param_name = parts[i-1].rstrip('s')  # 去掉复数 s
                params.append(f"{{{param_name}_id}}")

        return params

    def extract_query_params(self, url: str) -> Dict[str, str]:
        """提取查询参数"""
        parsed = urlparse(url)
        query_params = parse_qs(parsed.query)

        # 将列表值转换为单个值
        result = {}
        for key, values in query_params.items():
            result[key] = values[0] if values else ''

        return result

    def format_api_endpoint(self, api: Dict) -> str:
        """格式化 API 端点"""
        parsed = urlparse(api['url'])
        path = parsed.path

        # 替换数字 ID 为参数占位符
        parts = path.split('/')
        formatted_parts = []
        for i, part in enumerate(parts):
            if part.isdigit() and i > 0:
                param_name = parts[i-1].rstrip('s')
                formatted_parts.append(f"{{{param_name}_id}}")
            else:
                formatted_parts.append(part)

        return '/'.join(formatted_parts)

    def generate_markdown(self, categories: Dict) -> str:
        """生成 Markdown 文档"""
        md = []

        # 文档标题
        md.append("# 知识星球 API 文档\n")
        md.append(f"**生成时间**: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}\n")
        md.append(f"**接口总数**: {len(self.apis)}\n")
        md.append("**数据来源**: Fiddler Everywhere 抓包分析\n")
        md.append("---\n")

        # 目录
        md.append("## 📚 目录\n")
        for module in sorted(categories.keys()):
            md.append(f"- [{module}](#{self._to_anchor(module)})\n")
            for sub_category in sorted(categories[module].keys()):
                md.append(f"  - [{sub_category}](#{self._to_anchor(module + sub_category)})\n")
        md.append("\n---\n")

        # 通用说明
        md.append("## 🔐 认证机制\n")
        md.append("所有 API 请求都需要以下请求头：\n\n")
        md.append("| 请求头 | 说明 | 示例 |\n")
        md.append("|--------|------|------|\n")
        md.append("| `authorization` | 认证 Token | `D047A423-A...` |\n")
        md.append("| `x-timestamp` | Unix 时间戳 | `1765268187` |\n")
        md.append("| `x-signature` | 请求签名 (SHA1) | `dd7b51bee...` |\n")
        md.append("| `x-aduid` | 设备唯一标识 | `d75d966c-ed30...` |\n")
        md.append("| `x-version` | App 版本 | `2.83.0` |\n")
        md.append("| `x-request-id` | 请求追踪 ID (UUID) | `9af8e4c1...` |\n")
        md.append("| `user-agent` | 用户代理 | `xiaomiquan/5.29.1 iOS/phone/26.1` |\n")
        md.append("| `content-type` | 内容类型 | `application/json; charset=utf-8` |\n")
        md.append("\n---\n")

        # 基础 URL
        md.append("## 🌐 基础 URL\n\n")
        md.append("```\n")
        md.append("https://api.zsxq.com\n")
        md.append("```\n\n")
        md.append("---\n")

        # 各模块接口详情
        for module in sorted(categories.keys()):
            md.append(f"\n## {module}\n\n")

            for sub_category in sorted(categories[module].keys()):
                apis = categories[module][sub_category]
                md.append(f"### {sub_category}\n\n")
                md.append(f"**接口数量**: {len(apis)}\n\n")

                # 显示所有接口（已在分类前去重）
                for api in apis:
                    md.extend(self._format_api_detail(api))
                    md.append("\n---\n\n")

        return ''.join(md)

    def _deduplicate_apis(self, apis: List[Dict]) -> List[Dict]:
        """去重 API（基于 method + 完整URL）"""
        seen = {}
        unique = []

        for api in apis:
            # 使用完整 URL（包含查询参数）作为唯一标识
            key = f"{api['method']}:{api['url']}"

            if key not in seen:
                seen[key] = True
                unique.append(api)

        return unique

    def _format_api_detail(self, api: Dict) -> List[str]:
        """格式化单个 API 详情"""
        md = []

        # 接口标题
        endpoint = self.format_api_endpoint(api)
        md.append(f"#### `{api['method']}` {endpoint}\n\n")

        # 完整 URL 示例
        md.append("**完整 URL**:\n")
        md.append(f"```\n{api['url']}\n```\n\n")

        # 查询参数
        query_params = self.extract_query_params(api['url'])
        if query_params:
            md.append("**查询参数**:\n\n")
            md.append("| 参数名 | 值 | 说明 |\n")
            md.append("|--------|----|----- |\n")
            for key, value in query_params.items():
                md.append(f"| `{key}` | `{value}` |  |\n")
            md.append("\n")

        # 请求头（仅显示特殊的）
        if api['request_headers']:
            special_headers = {k: v for k, v in api['request_headers'].items()
                             if k.lower() not in ['user-agent', 'content-type']}
            if special_headers:
                md.append("**特殊请求头**:\n\n")
                md.append("| 请求头 | 值 |\n")
                md.append("|--------|----|\n")
                for key, value in list(special_headers.items())[:5]:
                    md.append(f"| `{key}` | `{value}` |\n")
                md.append("\n")

        # 请求体
        if api['request_body']:
            md.append("**请求体**:\n\n")
            md.append("```json\n")
            md.append(self._format_json(api['request_body']))
            md.append("\n```\n\n")

        # 响应状态码
        md.append(f"**响应状态码**: `{api['status']}`\n\n")

        # 响应体
        if api['response_body']:
            md.append("**响应示例**:\n\n")
            md.append("```json\n")
            md.append(self._format_json(api['response_body']))
            md.append("\n```\n\n")

        # 响应时间
        md.append(f"**平均响应时间**: {api['time']:.0f}ms\n\n")

        return md

    def _format_json(self, data: Any, indent: int = 2) -> str:
        """格式化 JSON 数据"""
        if isinstance(data, dict):
            if '_preview' in data:
                return data['_preview']
            return json.dumps(data, ensure_ascii=False, indent=indent)
        elif isinstance(data, str):
            try:
                obj = json.loads(data)
                return json.dumps(obj, ensure_ascii=False, indent=indent)
            except:
                return data
        else:
            return json.dumps(data, ensure_ascii=False, indent=indent)

    def _to_anchor(self, text: str) -> str:
        """转换为 Markdown 锚点"""
        # 移除特殊字符，保留中文
        return text.replace(' ', '-').replace('/', '').lower()

    def save_documentation(self, output_file: str):
        """保存文档"""
        print("🔍 正在分类接口...")
        categories = self.classify_apis()

        print(f"✅ 分类完成，共 {len(categories)} 个模块\n")
        for module, sub_cats in categories.items():
            print(f"  📦 {module}:")
            for sub_cat, apis in sub_cats.items():
                print(f"     - {sub_cat}: {len(apis)} 个接口")

        print("\n📝 正在生成 Markdown 文档...")
        markdown = self.generate_markdown(categories)

        output_path = Path(output_file)
        with open(output_path, 'w', encoding='utf-8') as f:
            f.write(markdown)

        print(f"\n✅ 文档已生成: {output_path}")
        print(f"📊 文件大小: {output_path.stat().st_size / 1024:.2f} KB")
        print(f"📄 总字数: {len(markdown)} 字符")


def main():
    if len(sys.argv) < 2:
        print("用法: python generate_api_docs.py <分析报告.json> [输出文件.md]")
        sys.exit(1)

    analysis_file = sys.argv[1]
    output_file = sys.argv[2] if len(sys.argv) > 2 else analysis_file.replace('.json', '_API_DOCS.md')

    generator = ApiDocGenerator(analysis_file)
    generator.load_analysis()
    generator.save_documentation(output_file)

    print("\n" + "=" * 80)
    print("✨ API 文档生成完成！")
    print("=" * 80)


if __name__ == '__main__':
    main()
