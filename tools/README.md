# 知识星球 API 分析工具

本目录包含用于分析知识星球 App 抓包数据的 Python 工具脚本。

## 📦 工具清单

### 1. analyze_har.py - HAR 文件分析器

**功能**: 解析 Fiddler Everywhere 导出的 HAR 格式抓包文件，提取和分析 API 接口信息。

**使用方法**:

```bash
python3 analyze_har.py <har文件路径>
```

**示例**:

```bash
python3 analyze_har.py /path/to/zsxq_capture.har
```

**输出**:
- 控制台输出: 统计信息、接口分类清单
- JSON 文件: `<原文件名>_analysis.json` - 详细分析报告

**功能特性**:
- ✅ 统计请求方法、状态码、域名分布
- ✅ 自动识别 API 接口（过滤静态资源）
- ✅ 提取请求头、请求体、响应体
- ✅ 敏感信息自动脱敏（Token、Cookie）
- ✅ 接口智能分类（登录、用户、列表等）

---

### 2. generate_api_docs.py - API 文档生成器

**功能**: 从分析报告生成完整的 Markdown 格式 API 文档。

**使用方法**:

```bash
python3 generate_api_docs.py <分析报告.json> [输出文件.md]
```

**示例**:

```bash
# 使用默认输出文件名
python3 generate_api_docs.py zsxq_capture_analysis.json

# 指定输出文件名
python3 generate_api_docs.py zsxq_capture_analysis.json 知识星球API文档.md
```

**输出**:
- Markdown 文档，包含:
  - 📚 目录索引
  - 🔐 认证机制说明
  - 📦 接口分类（8个业务模块）
  - 🔍 每个接口的详细信息（URL、参数、请求/响应示例）

**功能特性**:
- ✅ 智能去重（基于 method + URL）
- ✅ 接口按业务模块分类
- ✅ 自动生成目录
- ✅ 格式化 JSON 数据
- ✅ 提取认证参数

---

## 🚀 完整工作流程

### 步骤 1: 使用 Fiddler 抓包

1. 在 Fiddler Everywhere 中配置手机代理
2. 打开知识星球 App 并操作
3. 导出抓包数据为 **HTTPArchive v1.2** 格式（.har 文件）

### 步骤 2: 分析抓包数据

```bash
cd /Users/stinglong/code/github/zsxq-api/tools
python3 analyze_har.py ~/Downloads/zsxq.har
```

输出示例：
```
📂 正在读取文件: zsxq.har
📊 文件大小: 5.87 MB
✅ 共加载 387 个请求

================================================================================
📊 抓包数据统计
================================================================================
总请求数: 387
📌 请求方法分布:
  GET       :  326 (84.2%)
  POST      :   16 (4.1%)
...
✅ 识别出 188 个 API 接口
💾 详细报告已保存到: zsxq_analysis.json
```

### 步骤 3: 生成 API 文档

```bash
python3 generate_api_docs.py zsxq_analysis.json ../docs/Fiddler原始API文档.md
```

输出示例：
```
📂 正在加载分析报告: zsxq_analysis.json
✅ 加载了 188 个 API 接口

🔍 正在分类接口...
去重后接口数: 118 (原始: 188)
✅ 分类完成，共 8 个模块

✅ 文档已生成: ../docs/抓包API文档.md
📊 文件大小: 118.50 KB
```

---

## 📋 依赖要求

```bash
# Python 版本
Python 3.6+

# 依赖库（均为标准库，无需额外安装）
- json
- pathlib
- typing
- collections
- urllib.parse
- datetime
```

---

## 📁 输出文件说明

### 分析报告 JSON 格式

```json
{
  "summary": {
    "total_apis": 188,
    "file": "/path/to/zsxq.har"
  },
  "apis": [
    {
      "index": 1,
      "method": "GET",
      "url": "https://api.zsxq.com/v2/groups",
      "status": 200,
      "mime_type": "application/json; charset=UTF-8",
      "request_headers": {...},
      "response_headers": {...},
      "request_body": null,
      "response_body": {...},
      "time": 227
    }
  ]
}
```

### API 文档结构

```
知识星球 API 文档
├── 📚 目录
├── 🔐 认证机制
├── 🌐 基础 URL
├── 用户系统
│   ├── 用户信息
│   ├── 用户星球管理
│   └── ...
├── 星球系统
│   ├── 星球信息
│   ├── 星球推荐
│   └── ...
└── ...
```

---

## 🔧 高级用法

### 自定义接口分类

编辑 `generate_api_docs.py` 中的 `classify_apis()` 方法，修改分类规则：

```python
def classify_apis(self) -> Dict[str, List[Dict]]:
    """对 API 进行详细分类"""
    categories = defaultdict(lambda: defaultdict(list))

    for api in self.apis:
        url = api['url'].lower()
        path = urlparse(api['url']).path
        method = api['method']

        # 添加自定义分类规则
        if '/custom/' in path:
            module = '自定义模块'
            sub_category = '自定义分类'
        # ...
```

### 批量处理多个抓包文件

```bash
#!/bin/bash
for file in *.har; do
    echo "处理文件: $file"
    python3 analyze_har.py "$file"

    analysis_file="${file%.har}_analysis.json"
    python3 generate_api_docs.py "$analysis_file"
done
```

---

## 📖 相关文档

- [抓包API文档](../docs/抓包API文档.md) - 完整的 API 接口文档
- [API接口.md](../docs/API接口.md) - 项目设计的 API 文档
- [IMPLEMENTATION_PLAN.md](../IMPLEMENTATION_PLAN.md) - 项目实施计划

---

## 🐛 常见问题

### Q: 运行报错 `JSONDecodeError: Unexpected UTF-8 BOM`

**A**: HAR 文件包含 BOM 编码，已在脚本中使用 `encoding='utf-8-sig'` 处理。

### Q: 分析出的接口数量比预期少？

**A**: 工具会自动过滤掉：
- 静态资源（.js, .css, .png 等）
- 重复的接口调用（基于 method + URL 去重）

### Q: 如何获取完整的签名算法？

**A**: 需要逆向工程：
1. iOS App 脱壳（frida-ios-dump）
2. 反汇编分析（Hopper/IDA）
3. 动态调试（Frida hook）

参考文档中的"逆向工程提示"部分。

---

## 📝 更新日志

- **2025-12-09**: 初始版本
  - 实现 HAR 文件解析
  - 实现 API 文档自动生成
  - 支持接口智能分类

---

**维护者**: Claude Code
**项目**: zsxq-api
**版本**: 1.0.0
