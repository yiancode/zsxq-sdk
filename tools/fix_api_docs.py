#!/usr/bin/env python3
"""
修复 API 文档：
1. 为每个接口添加序号
2. 修复不规范的 JSON 格式
"""

import re
import json
import sys
from typing import List, Tuple


def fix_json_response(json_str: str) -> str:
    """修复不规范的 JSON 字符串"""
    original = json_str.strip()

    # 先尝试直接解析 JSON
    try:
        obj = json.loads(original)
        return json.dumps(obj, indent=2, ensure_ascii=False)
    except:
        pass

    # 最后尝试用 eval（安全性较低，但对 Python 字典有效）
    # 对于完整的 Python 字典，这个方法很有效
    try:
        obj = eval(original)
        return json.dumps(obj, indent=2, ensure_ascii=False)
    except:
        pass

    # 如果 eval 失败，尝试基本的语法转换
    # 这对于截断的 JSON 很有用
    converted = original
    converted = converted.replace("True", "true").replace("False", "false").replace("None", "null")

    # 全局替换所有单引号为双引号
    # 这个方法简单但有效，因为文档中的字符串值基本不包含单引号
    converted = converted.replace("'", '"')

    # 尝试解析修复后的字符串
    try:
        obj = json.loads(converted)
        return json.dumps(obj, indent=2, ensure_ascii=False)
    except:
        pass

    # 如果都失败，至少返回语法转换后的版本
    # 这样即使不是完整的 JSON，至少看起来像 JSON 而不是 Python
    if converted != original:
        return converted

    return original


def process_api_doc(content: str) -> str:
    """处理 API 文档内容"""
    lines = content.split('\n')
    result_lines = []

    # 跟踪当前模块和子模块的接口计数
    current_section = None
    current_subsection = None
    api_counter = {}

    i = 0
    while i < len(lines):
        line = lines[i]

        # 检测模块标题（## 开头）
        if line.startswith('## ') and '.' in line.split()[1]:
            current_section = line.split()[1].rstrip('.')
            api_counter[current_section] = 0
            result_lines.append(line)
            i += 1
            continue

        # 检测子模块标题（### 开头）
        if line.startswith('### ') and '.' in line.split()[1]:
            section_parts = line.split()[1].rstrip('.').split('.')
            if len(section_parts) == 2:
                current_section = section_parts[0]
                current_subsection = '.'.join(section_parts)
                api_counter[current_subsection] = 0
            result_lines.append(line)
            i += 1
            continue

        # 检测接口定义（#### 开头）
        if line.startswith('#### '):
            # 提取接口方法和路径
            match = re.match(r'####\s+`([A-Z]+)`\s+(.+?)(?:\s+\(.*\))?$', line)
            if match:
                method = match.group(1)
                path = match.group(2)

                # 确定接口所属的计数器
                counter_key = current_subsection if current_subsection else current_section
                if counter_key and counter_key in api_counter:
                    api_counter[counter_key] += 1
                    api_num = api_counter[counter_key]

                    # 添加接口序号
                    new_line = f"#### {counter_key}.{api_num} `{method}` {path}"
                    result_lines.append(new_line)
                else:
                    result_lines.append(line)
            else:
                result_lines.append(line)
            i += 1
            continue

        # 检测并修复 JSON 响应示例
        if line.strip() == '```json' or line.strip() == '```python':
            result_lines.append('```json')
            i += 1

            # 收集 JSON 内容
            json_lines = []
            while i < len(lines) and not lines[i].strip().startswith('```'):
                json_lines.append(lines[i])
                i += 1

            # 尝试修复 JSON
            json_content = '\n'.join(json_lines)
            if json_content.strip():
                fixed_json = fix_json_response(json_content.strip())
                result_lines.append(fixed_json)

            # 添加结束标记
            if i < len(lines):
                result_lines.append('```')
                i += 1
            continue

        result_lines.append(line)
        i += 1

    return '\n'.join(result_lines)


def main():
    input_file = '/Users/stinglong/code/github/zsxq-sdk/docs/archive/v0.1/Fiddler原始API文档.md'

    # 读取文档
    with open(input_file, 'r', encoding='utf-8') as f:
        content = f.read()

    # 处理文档
    print("正在处理 API 文档...")
    processed_content = process_api_doc(content)

    # 写回文件
    with open(input_file, 'w', encoding='utf-8') as f:
        f.write(processed_content)

    print("✅ 文档处理完成！")


if __name__ == '__main__':
    main()
