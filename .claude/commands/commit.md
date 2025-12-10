# 自动提交指令

智能分析当前开发进度，自动生成 Git 提交信息。

## 执行步骤

### 步骤 1: 分析当前变更

执行以下 Git 命令获取信息：

```bash
# 并行执行
git status --short
git diff
git diff --staged
git log -3 --oneline
```

### 步骤 2: 智能分析变更内容

**变更类型判断规则**:
- 新增功能文件 → `feat` (功能)
- 修复 bug 相关代码 → `fix` (修复)
- 修改文档（.md 文件）→ `docs` (文档)
- 修改测试文件（.spec.ts）→ `test` (测试)
- 代码重构（无功能变化）→ `refactor` (重构)
- 性能优化 → `perf` (性能)
- 构建配置变更（package.json/nest-cli.json）→ `chore` (构建)

**影响范围判断**:
- `src/modules/auth/` → `auth`
- `src/modules/user/` → `user`
- `src/modules/planet/` → `planet`
- `src/modules/topic/` → `topic`
- `src/modules/training-camp/` → `training-camp`
- `src/modules/member/` → `member`
- `src/zsxq-client/` → `zsxq-client`
- `src/common/` → `common`
- `docs/` → `docs`
- 多个目录 → 使用主要影响范围或省略scope

**生成提交信息格式** (Conventional Commits):
```
<type>(<scope>): <简短描述>

<详细说明>

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
```

### 步骤 3: 执行 Git 提交

```bash
# 1. 添加所有变更
git add .

# 2. 提交（使用 HEREDOC）
git commit -m "$(cat <<'EOF'
{生成的 commit message}

🤖 Generated with [Claude Code](https://claude.com/claude-code)

Co-Authored-By: Claude Opus 4.5 <noreply@anthropic.com>
EOF
)"

# 3. 验证提交
git status
```

### 步骤 4: 输出提交摘要

```
✅ 代码已提交

📝 提交信息:
{commit message subject}

📊 变更统计:
- 修改文件: {数量} 个
- 新增行: +{数量}
- 删除行: -{数量}

🔗 提交 Hash: {hash}
```

## 防护措施

### 敏感文件检查

检查以下文件是否被暂存，如发现则警告用户：
- `.env`
- `.env.local`
- `config.yml` (包含 token)
- `**/secret*`
- `**/password*`
- `**/credentials*`

### 错误处理

**没有变更**:
```
⚠️ 没有检测到任何变更
运行 'git status' 查看当前状态
```

**存在冲突**:
```
⚠️ 检测到未解决的合并冲突
请先解决冲突后重新运行
```

## 提交信息示例

### 功能开发
```
feat(zsxq-client): 增强API客户端请求头伪装

- 添加 User-Agent 伪装为知识星球APP
- 生成随机 x-request-id
- 实现业务状态码检查 (succeeded 字段)
- 添加自定义异常类
```

### 文档更新
```
docs: 添加知识星球原生API参考文档

- 整理打卡项目相关API端点
- 添加缓存设计方案
- 补充ZsxqClient增强方案
- 更新API接口文档
```

### Bug修复
```
fix(auth): 修复JWT token过期时间计算错误

token 过期时间应使用秒而非毫秒
```

### 重构
```
refactor(common): 统一响应格式拦截器

将分散的响应处理逻辑集中到 TransformInterceptor
```

### 步骤 5: 推送到远程仓库

提交成功后自动推送：

```bash
git push
```

**如果是新分支**:
```bash
git push -u origin {branch_name}
```

**推送成功输出**:
```
🚀 已推送到远程仓库

📊 推送统计:
- 分支: {branch_name}
- 远程: {remote_url}
```

**推送错误处理**:

推送被拒绝（远程有更新）:
```
⚠️ 推送被拒绝，远程分支有新的提交
请先执行 git pull --rebase 后重试
```

## 注意事项

1. **自动推送**: 提交成功后自动推送到远程仓库
2. **中文描述**: 提交信息使用中文，scope 使用英文
3. **单一职责**: 每次提交只做一件事
4. **敏感信息**: 绝不提交 token、密码等敏感信息
5. **不使用 force push**: 除非用户明确要求
