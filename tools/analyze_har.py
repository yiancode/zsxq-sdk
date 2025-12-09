#!/usr/bin/env python3
"""
HAR 文件分析工具
分析 Fiddler 导出的抓包数据，提取关键接口信息
"""

import json
import sys
from pathlib import Path
from collections import defaultdict
from urllib.parse import urlparse
from typing import Dict, List, Any


class HarAnalyzer:
    """HAR 文件分析器"""

    def __init__(self, har_file: str):
        self.har_file = Path(har_file)
        self.data = None
        self.entries = []

    def load(self):
        """加载 HAR 文件"""
        print(f"📂 正在读取文件: {self.har_file.name}")
        print(f"📊 文件大小: {self.har_file.stat().st_size / 1024 / 1024:.2f} MB")

        with open(self.har_file, 'r', encoding='utf-8-sig') as f:
            self.data = json.load(f)
            self.entries = self.data.get('log', {}).get('entries', [])

        print(f"✅ 共加载 {len(self.entries)} 个请求\n")

    def get_statistics(self) -> Dict:
        """获取统计信息"""
        stats = {
            'total_requests': len(self.entries),
            'methods': defaultdict(int),
            'status_codes': defaultdict(int),
            'domains': defaultdict(int),
            'content_types': defaultdict(int),
        }

        for entry in self.entries:
            request = entry.get('request', {})
            response = entry.get('response', {})

            # 统计请求方法
            method = request.get('method', 'UNKNOWN')
            stats['methods'][method] += 1

            # 统计状态码
            status = response.get('status', 0)
            stats['status_codes'][status] += 1

            # 统计域名
            url = request.get('url', '')
            domain = urlparse(url).netloc
            if domain:
                stats['domains'][domain] += 1

            # 统计内容类型
            content = response.get('content', {})
            mime_type = content.get('mimeType', 'unknown')
            # 简化类型
            if 'json' in mime_type:
                content_type = 'JSON'
            elif 'html' in mime_type:
                content_type = 'HTML'
            elif 'javascript' in mime_type:
                content_type = 'JavaScript'
            elif 'css' in mime_type:
                content_type = 'CSS'
            elif 'image' in mime_type:
                content_type = 'Image'
            elif 'font' in mime_type:
                content_type = 'Font'
            else:
                content_type = 'Other'
            stats['content_types'][content_type] += 1

        return stats

    def extract_api_endpoints(self) -> List[Dict]:
        """提取 API 接口"""
        apis = []

        for idx, entry in enumerate(self.entries):
            request = entry.get('request', {})
            response = entry.get('response', {})

            url = request.get('url', '')
            method = request.get('method', '')
            status = response.get('status', 0)

            # 过滤掉静态资源
            if self._is_static_resource(url):
                continue

            # 只保留 API 接口（通常是 JSON 返回）
            content = response.get('content', {})
            mime_type = content.get('mimeType', '')

            if 'json' in mime_type.lower() or method in ['POST', 'PUT', 'DELETE', 'PATCH']:
                api_info = {
                    'index': idx + 1,
                    'method': method,
                    'url': url,
                    'status': status,
                    'mime_type': mime_type,
                    'request_headers': self._extract_headers(request.get('headers', [])),
                    'response_headers': self._extract_headers(response.get('headers', [])),
                    'request_body': self._extract_request_body(request),
                    'response_body': self._extract_response_body(response),
                    'time': entry.get('time', 0),
                }
                apis.append(api_info)

        return apis

    def _is_static_resource(self, url: str) -> bool:
        """判断是否为静态资源"""
        static_extensions = [
            '.js', '.css', '.png', '.jpg', '.jpeg', '.gif', '.svg', '.ico',
            '.woff', '.woff2', '.ttf', '.eot', '.map'
        ]
        return any(url.lower().endswith(ext) for ext in static_extensions)

    def _extract_headers(self, headers: List[Dict]) -> Dict:
        """提取关键请求头"""
        key_headers = ['authorization', 'content-type', 'user-agent', 'cookie', 'token', 'x-']
        result = {}

        for header in headers:
            name = header.get('name', '').lower()
            value = header.get('value', '')

            # 保留关键 header
            if any(key in name for key in key_headers):
                # 敏感信息脱敏
                if 'authorization' in name or 'token' in name or 'cookie' in name:
                    if len(value) > 20:
                        value = value[:10] + '...' + value[-10:]
                result[header.get('name', '')] = value

        return result

    def _extract_request_body(self, request: Dict) -> Any:
        """提取请求体"""
        post_data = request.get('postData', {})
        text = post_data.get('text', '')

        if not text:
            return None

        # 尝试解析 JSON
        try:
            return json.loads(text)
        except:
            # 如果不是 JSON，返回前 200 字符
            return text[:200] + ('...' if len(text) > 200 else '')

    def _extract_response_body(self, response: Dict) -> Any:
        """提取响应体"""
        content = response.get('content', {})
        text = content.get('text', '')

        if not text:
            return None

        # 尝试解析 JSON
        try:
            data = json.loads(text)
            # 如果 JSON 太大，只保留结构
            text_len = len(text)
            if text_len > 1000:
                return {
                    '_size': f'{text_len} bytes',
                    '_keys': list(data.keys()) if isinstance(data, dict) else f'array[{len(data)}]',
                    '_preview': str(data)[:500] + '...'
                }
            return data
        except:
            # 如果不是 JSON，返回前 200 字符
            return text[:200] + ('...' if len(text) > 200 else '')

    def classify_apis(self, apis: List[Dict]) -> Dict[str, List[Dict]]:
        """对 API 进行分类"""
        categories = defaultdict(list)

        for api in apis:
            url = api['url'].lower()
            method = api['method']

            # 根据 URL 路径和方法分类
            if 'login' in url or 'signin' in url:
                category = '🔐 登录认证'
            elif 'user' in url or 'profile' in url or 'account' in url:
                category = '👤 用户信息'
            elif 'register' in url or 'signup' in url:
                category = '📝 注册'
            elif 'upload' in url:
                category = '📤 上传'
            elif 'download' in url:
                category = '📥 下载'
            elif 'search' in url or 'query' in url:
                category = '🔍 搜索'
            elif 'list' in url or method == 'GET':
                category = '📋 列表查询'
            elif method == 'POST':
                category = '✏️ 数据提交'
            elif method == 'PUT' or method == 'PATCH':
                category = '🔄 数据更新'
            elif method == 'DELETE':
                category = '🗑️ 删除'
            else:
                category = '❓ 其他'

            categories[category].append(api)

        return dict(categories)

    def print_statistics(self, stats: Dict):
        """打印统计信息"""
        print("=" * 80)
        print("📊 抓包数据统计")
        print("=" * 80)

        print(f"\n总请求数: {stats['total_requests']}")

        print("\n📌 请求方法分布:")
        for method, count in sorted(stats['methods'].items(), key=lambda x: x[1], reverse=True):
            print(f"  {method:10s}: {count:4d} ({count/stats['total_requests']*100:.1f}%)")

        print("\n📌 状态码分布:")
        for status, count in sorted(stats['status_codes'].items(), key=lambda x: x[1], reverse=True):
            print(f"  {str(status):10s}: {count:4d} ({count/stats['total_requests']*100:.1f}%)")

        print("\n📌 Top 10 域名:")
        for domain, count in sorted(stats['domains'].items(), key=lambda x: x[1], reverse=True)[:10]:
            print(f"  {domain:40s}: {count:4d}")

        print("\n📌 内容类型分布:")
        for content_type, count in sorted(stats['content_types'].items(), key=lambda x: x[1], reverse=True):
            print(f"  {content_type:15s}: {count:4d} ({count/stats['total_requests']*100:.1f}%)")

    def print_apis(self, categories: Dict[str, List[Dict]]):
        """打印 API 接口清单"""
        print("\n" + "=" * 80)
        print("🔥 API 接口清单（按分类）")
        print("=" * 80)

        for category, apis in sorted(categories.items()):
            print(f"\n{category} ({len(apis)} 个)")
            print("-" * 80)

            for api in apis[:10]:  # 每个分类只显示前 10 个
                print(f"\n  [{api['index']}] {api['method']} {api['status']}")
                print(f"  URL: {api['url']}")

                if api['request_headers']:
                    print(f"  请求头: {list(api['request_headers'].keys())}")

                if api['request_body']:
                    print(f"  请求体: {self._format_json(api['request_body'])}")

                if api['response_body']:
                    print(f"  响应: {self._format_json(api['response_body'])}")

                print(f"  耗时: {api['time']:.0f}ms")

            if len(apis) > 10:
                print(f"\n  ... 还有 {len(apis) - 10} 个接口")

    def _format_json(self, data: Any) -> str:
        """格式化 JSON 数据"""
        if isinstance(data, dict):
            if '_preview' in data:
                return data['_preview']
            keys = list(data.keys())[:5]
            return f"{{{', '.join(keys)}}}"
        elif isinstance(data, list):
            return f"[{len(data)} items]"
        else:
            s = str(data)
            return s[:100] + ('...' if len(s) > 100 else '')

    def save_report(self, apis: List[Dict], output_file: str):
        """保存详细报告"""
        report = {
            'summary': {
                'total_apis': len(apis),
                'file': str(self.har_file),
            },
            'apis': apis
        }

        output_path = Path(output_file)
        with open(output_path, 'w', encoding='utf-8') as f:
            json.dump(report, f, ensure_ascii=False, indent=2)

        print(f"\n💾 详细报告已保存到: {output_path}")
        print(f"📊 报告大小: {output_path.stat().st_size / 1024:.2f} KB")


def main():
    if len(sys.argv) < 2:
        print("用法: python analyze_har.py <har文件路径>")
        sys.exit(1)

    har_file = sys.argv[1]

    analyzer = HarAnalyzer(har_file)
    analyzer.load()

    # 统计信息
    stats = analyzer.get_statistics()
    analyzer.print_statistics(stats)

    # 提取 API
    apis = analyzer.extract_api_endpoints()
    print(f"\n✅ 识别出 {len(apis)} 个 API 接口")

    # 分类展示
    categories = analyzer.classify_apis(apis)
    analyzer.print_apis(categories)

    # 保存详细报告
    output_file = har_file.replace('.har', '_analysis.json')
    analyzer.save_report(apis, output_file)

    print("\n" + "=" * 80)
    print("✨ 分析完成！")
    print("=" * 80)


if __name__ == '__main__':
    main()
