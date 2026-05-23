# PRD：操作历史功能（Activity Log）

**文档版本：** v1.0  
**日期：** 2026-05-23  
**项目：** EBU6304 TA Recruitment System（Group 71）

---

## 1. 背景与目标

### 1.1 现状分析

系统目前已有三种角色（Admin、MO、Applicant），并已实现以下核心操作：

| 角色 | 已有操作 |
|------|----------|
| Applicant | 申请职位、撤回申请、修改个人资料 |
| MO | 发布职位、关闭/重开职位、审核申请（Pending→Accepted/Rejected） |
| Admin | 创建用户、删除用户、重置密码 |

系统已存在 Notification 机制，但 Notification 是面向接收方的**事件推送**，不等于面向操作者的**行为审计**。当前系统对任何操作**没有持久化的历史记录**，无法追溯"谁、什么时候、做了什么"。

### 1.2 目标

- 为每个用户建立自己的操作足迹，增强系统可信度和可追溯性
- 为 Admin 提供全局行为审计视图，方便监督和排查问题
- 为未来审计合规打下数据基础

---

## 2. 用户故事

| 编号 | 角色 | 用户故事 |
|------|------|----------|
| US-01 | Applicant | 我希望在 Dashboard 上看到自己最近的操作记录（申请了哪些职位、何时撤回），以便核对自己的行为 |
| US-02 | MO | 我希望在 Dashboard 上看到自己最近的操作记录（发布了哪些职位、对哪个申请人做了什么决定），以便回顾招聘进度 |
| US-03 | Admin | 我希望在 Dashboard 上看到自己最近的用户管理操作，也能在专属页面查看全系统所有用户的操作历史，并支持多维度筛选 |

---

## 3. 功能范围

### 3.1 需要记录的操作事件（In Scope）

| 事件类型（`actionType`） | 触发角色 | 触发点 | 描述示例 |
|---|---|---|---|
| `APPLY_JOB` | Applicant | `ApplyJobServlet` | 申请职位《数据结构》助教 |
| `WITHDRAW_APPLICATION` | Applicant | `WithdrawApplicationServlet` | 撤回对《数据结构》助教的申请 |
| `CREATE_JOB` | MO | `JobCreateServlet` | 发布新职位《操作系统》助教 |
| `COMPLETE_JOB` | MO | `MOJobsServlet` | 标记职位《操作系统》为已完成 |
| `REOPEN_JOB` | MO | `MOJobsServlet` | 重新开放职位《操作系统》 |
| `UPDATE_APPLICATION_STATUS` | MO | `UpdateApplicationStatusServlet` | 将申请 #xxx 状态从 Pending 变更为 Accepted |
| `CREATE_USER` | Admin | `AdminCreateUserServlet` | 创建新用户 alice (APPLICANT) |
| `DELETE_USER` | Admin | `AdminUserManagementServlet` | 删除用户 bob (MO) |

> **不在本期范围内：** 登录/登出记录、个人资料修改记录（可作为后续迭代）

### 3.2 记录字段（数据模型）

每条 `ActivityLog` 记录包含：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| `id` | String | 唯一 ID，使用 IdUtil 生成 |
| `userId` | String | 操作者的用户 ID |
| `userFullName` | String | 操作者姓名（冗余存储，防止用户被删后查不到名字） |
| `userRole` | String | 操作者角色（APPLICANT / MO / ADMIN） |
| `actionType` | String | 操作类型（见 3.1 表格） |
| `description` | String | 操作的自然语言描述（含关联对象名称，如职位标题） |
| `relatedObjectId` | String | 关联对象 ID（职位 ID / 申请 ID / 被操作的用户 ID），可为 null |
| `beforeState` | String | 操作前状态（仅状态变更类操作填写，其余为 null，例如 `"Pending"`） |
| `afterState` | String | 操作后状态（例如 `"Accepted"`） |
| `createdAt` | LocalDateTime | 操作时间 |

**存储：** 沿用现有的 JSON 文件方案，新增 `activity_logs.json`，结构与 `notifications.json` 类似。

---

## 4. 页面与交互设计

### 4.1 Dashboard 卡片（所有角色）

**位置：** 在 `dashboard.jsp` 的现有卡片网格之后，新增一个**独立的卡片区域**，标题为 "Recent Activity"。

**展示规则：**
- 仅展示当前用户自己的最新 **5 条**操作记录（按时间倒序）
- 每条记录展示：操作类型标签、描述文字、时间
- 对于包含状态变更的记录，额外展示 `beforeState → afterState` 的徽章
- 卡片底部有 "View All" 链接，跳转至 `/activity/my` 页面查看完整历史

**交互要求：** 只读，不可删除、不可标为已读（操作日志不应由用户干预）

---

### 4.2 我的操作历史页 `/activity/my`（所有角色）

**访问权限：** 已登录即可访问（无角色限制）

**功能：**
- 展示当前用户全部操作历史，按时间倒序
- 支持按 **操作类型** 下拉筛选（显示当前角色所对应的类型）
- 每条记录完整展示所有字段（含前后状态对比）
- 分页（可选，初期不做分页，全量展示即可）

---

### 4.3 Admin 全局操作历史页 `/admin/activity`

**访问权限：** 仅 ADMIN 角色

**功能：**

| 筛选项 | 实现方式 |
|--------|----------|
| 按用户名 | 文本输入框，模糊匹配 `userFullName` |
| 按操作类型 | 下拉单选（APPLY_JOB / CREATE_JOB / … / 全部） |
| 按时间范围 | 开始日期 + 结束日期两个日期选择器 |
| 按角色 | 下拉单选（APPLICANT / MO / ADMIN / 全部） |

- 筛选条件均为可选，不选则展示全部
- 支持组合筛选
- 结果按时间倒序展示
- 表格列：时间、操作者（姓名 + 角色）、操作类型、描述、前后状态
- Admin 侧边导航栏新增入口 "Activity Log"

---

## 5. 技术实现方案

### 5.1 新增文件清单

| 文件 | 说明 |
|------|------|
| `model/ActivityLog.java` | 数据模型 |
| `repository/ActivityLogRepository.java` | JSON 文件读写 |
| `service/ActivityLogService.java` | 业务逻辑（记录、查询、筛选） |
| `servlet/MyActivityServlet.java` | 处理 `/activity/my` |
| `servlet/AdminActivityServlet.java` | 处理 `/admin/activity` |
| `views/my-activity.jsp` | 我的操作历史页面 |
| `views/admin-activity.jsp` | Admin 全局历史页面 |

### 5.2 修改已有文件清单

| 文件 | 修改内容 |
|------|----------|
| `ApplyJobServlet.java` | 成功申请后调用 `ActivityLogService.log()` |
| `WithdrawApplicationServlet.java` | 撤回成功后记录日志 |
| `JobCreateServlet.java` | 发布职位成功后记录 |
| `MOJobsServlet.java` | complete/reopen 操作后记录 |
| `UpdateApplicationStatusServlet.java` | 状态变更前读取旧状态，变更后记录（包含前后状态） |
| `AdminCreateUserServlet.java` | 创建用户成功后记录 |
| `AdminUserManagementServlet.java` | 删除用户成功后记录 |
| `DashboardServlet.java` | 查询当前用户最新 5 条日志，传递给 dashboard.jsp |
| `dashboard.jsp` | 新增 "Recent Activity" 卡片 |
| `header.jspf` | Admin 侧边栏新增 "Activity Log" 导航项 |
| `web.xml` | 注册两个新 Servlet 的映射 |

### 5.3 关键设计决策

1. **不与 Notification 合并：** Notification 是接收方视角的消息推送，Activity Log 是操作者视角的行为审计，两者职责不同，分开维护
2. **冗余存储 `userFullName` 和 `userRole`：** 即使用户被 Admin 删除，历史日志仍能正常展示操作者信息
3. **`beforeState`/`afterState` 仅在状态变更事件中填写：** 其他事件此两字段存 `null`，避免模型过于复杂
4. **同步写入，不异步：** 与现有 NotificationRepository 保持一致，操作完成后立即写入 JSON 文件

---

## 6. 验收标准（AC）

| 编号 | 验收条件 |
|------|----------|
| AC-01 | Applicant 申请职位后，Dashboard 卡片显示该条日志（含职位名称、时间） |
| AC-02 | Applicant 撤回申请后，日志新增撤回记录 |
| AC-03 | MO 发布/关闭/重开职位后，各产生一条对应日志 |
| AC-04 | MO 更新申请状态后，日志显示 `Pending → Accepted`（或其他变化） |
| AC-05 | Admin 创建/删除用户后，日志记录目标用户的用户名和角色 |
| AC-06 | `/activity/my` 页面只显示当前登录用户自己的记录，不可见他人 |
| AC-07 | `/admin/activity` 页面按用户名、类型、时间、角色四维筛选均正常工作 |
| AC-08 | 用户被 Admin 删除后，其历史日志在 Admin 全局历史页仍正常显示 |
| AC-09 | 日志数据永久保存，系统重启后不丢失 |
| AC-10 | 操作日志**不提供**删除/修改入口（只读） |

---

## 7. 不在本期范围内（Out of Scope）

- 登录 / 登出事件记录
- 个人资料修改日志
- 日志导出（CSV/Excel）
- 日志自动清理策略
- 分页（初期全量展示）

---
