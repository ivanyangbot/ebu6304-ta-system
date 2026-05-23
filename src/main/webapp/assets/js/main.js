document.addEventListener("DOMContentLoaded", function () {
    var STORAGE_KEY = "ta-language";
    var translations = {
        en: {
            "brand.title": "TA Recruitment System",
            "brand.subtitle": "BUPT International School",
            "workspace.kicker": "Recruitment Workspace",
            "sidebar.account": "Current Account",
            "page.dashboard": "Dashboard",
            "page.jobs": "Browse Jobs",
            "page.jobDetail": "Job Detail",
            "page.profile": "My Profile",
            "page.myApplications": "My Applications",
            "page.myJobs": "My Jobs",
            "page.jobApplications": "Applications",
            "page.createJob": "Create Job",
            "page.editJob": "Edit Job",
            "page.workload": "Workload Overview",
            "page.userManagement": "User Management",
            "page.error": "Error",
            "page.login": "Login",
            "page.myActivity": "My Activity",
            "page.adminActivity": "System Activity Log",
            "nav.dashboard": "Dashboard",
            "nav.profile": "My Profile",
            "nav.jobs": "Browse Jobs",
            "nav.myApplications": "My Applications",
            "nav.moJobs": "My Jobs",
            "nav.createJob": "Create Job",
            "nav.workload": "Workload Overview",
            "nav.userManagement": "User Management",
            "nav.aiQuery": "AI Query",
            "page.aiQuery": "AI Data Query",
            "aiQuery.heading": "AI Data Query",
            "aiQuery.intro": "Ask a simple question about users, jobs, applications, or workload. The answer is generated from the current JSON data snapshot.",
            "aiQuery.askTitle": "Ask a Question",
            "aiQuery.questionLabel": "Your Question",
            "aiQuery.examples": "Examples: pending application count, open jobs, overloaded applicants.",
            "aiQuery.submit": "Ask AI",
            "aiQuery.snapshotTitle": "Current Data Snapshot",
            "aiQuery.answerTitle": "AI Answer",
            "action.logout": "Logout",
            "action.login": "Login",
            "action.save": "Save",
            "action.saveProfile": "Save Profile",
            "action.browseJobs": "Browse Jobs",
            "action.editProfile": "Edit Profile",
            "action.createJob": "Create Job",
            "action.viewMyJobs": "View My Jobs",
            "action.viewWorkload": "View Workload",
            "action.manageUsers": "Manage Users",
            "action.viewDetail": "View Detail",
            "action.viewApplications": "View Applications",
            "action.editJob": "Edit Job",
            "action.reviewApplications": "Review",
            "action.saveChanges": "Save Changes",
            "action.goBack": "Go Back",
            "action.returnDashboard": "Return to Dashboard",
            "action.applyNow": "Apply Now",
            "action.create": "Create",
            "action.delete": "Delete",
            "action.withdraw": "Withdraw Application",
            "action.withdrawApplication": "Withdraw Application",
            "action.applyJob": "Apply Job",
            "action.completeJob": "Complete Job",
            "action.reopenJob": "Reopen Job",
            "action.updateStatus": "Update Application Status",
            "action.deleteUser": "Delete User",
            "action.filter": "Filter",
            "action.reset": "Reset",
            "action.resetPassword": "Reset Password",
            "action.cancel": "Cancel",
            "action.createUser": "Create New User",
            "role.APPLICANT": "Applicant",
            "role.MO": "Module Organiser",
            "role.ADMIN": "Administrator",
            "status.Pending": "Pending",
            "status.Accepted": "Accepted",
            "status.Rejected": "Rejected",
            "status.Open": "Open",
            "status.Normal": "Normal",
            "status.Overloaded": "Overloaded",
            "status.Applied": "Applied",
            "status.AlreadyApplied": "You already applied",
            "statusDesc.Pending": "Waiting for MO review.",
            "statusDesc.Accepted": "You have been accepted for this position.",
            "statusDesc.Rejected": "Your application was not accepted.",
            "status.High": "High",
            "status.Medium": "Medium",
            "status.Low": "Low",
            "status.Advance": "Advance",
            "status.Discuss": "Discuss",
            "status.Hold": "Hold",
            "status.Light": "Light",
            "status.Heavy": "Heavy",
            "status.Critical": "Critical",
            "common.title": "Title",
            "common.jobTitle": "Job Title",
            "common.module": "Module",
            "common.description": "Description",
            "common.hours": "Hours",
            "common.requiredSkills": "Required Skills",
            "common.status": "Status",
            "common.action": "Action",
            "common.none": "None",
            "common.all": "All",
            "dashboard.welcome": "Welcome,",
            "dashboard.applicantIntro": "You can edit your profile, browse TA positions, and track your application status.",
            "dashboard.moIntro": "You can create TA jobs, review applicants, and update application results.",
            "dashboard.adminIntro": "You can review the overall TA workload and quickly identify overloaded applicants.",
            "dashboard.applicantPanel": "Applicant Panel",
            "dashboard.openJobs": "Open Jobs",
            "dashboard.myApplications": "My Applications",
            "dashboard.profileTitle": "Profile",
            "dashboard.profileDesc": "Keep your skills updated so the system can compute a clearer match score.",
            "dashboard.profileCompleted": "Completed",
            "dashboard.profileIncomplete": "Incomplete",
            "dashboard.moPanel": "MO Panel",
            "dashboard.myPostedJobs": "My Posted Jobs",
            "dashboard.pendingApplications": "Pending Applications",
            "dashboard.jobsNeedingAction": "Jobs Needing Action",
            "dashboard.totalUsers": "Total Users",
            "dashboard.totalApplicants": "Applicants",
            "dashboard.totalMOs": "Module Organisers",
            "dashboard.acceptanceRate": "Acceptance Rate",
            "dashboard.overloadedTAs": "Overloaded TAs",
            "dashboard.recentApplications": "Recent Pending Applications",
            "dashboard.noRecentApplications": "No pending applications yet.",
            "dashboard.actionRequiredJobs": "Jobs Needing Action",
            "dashboard.noActionRequiredJobs": "All open jobs are up to date.",
            "dashboard.pendingCount": "Pending",
            "dashboard.newJobTitle": "Create New Job",
            "dashboard.newJobDesc": "Post a new TA position and start collecting applications.",
            "dashboard.adminPanel": "Admin Panel",
            "dashboard.applicantsInSystem": "Applicants in System",
            "dashboard.workloadRuleTitle": "Workload Rule",
            "dashboard.workloadRuleDesc": "Applicants with accepted workload above 10 hours are marked as overloaded.",
            "dashboard.userManagementTitle": "User Management",
            "dashboard.userManagementDesc": "Create, view, and manage all user accounts in the system.",
            "dashboard.notifications": "Notifications",
            "notification.type.applicationStatus": "Application Status",
            "notification.type.newApplication": "New Application",
            "notification.time": "Time",
            "activity.myHeading": "My Activity History",
            "activity.myDesc": "Review recent actions recorded for your account.",
            "activity.adminHeading": "System Activity Log",
            "activity.adminDesc": "Audit user actions across the recruitment system.",
            "activity.time": "Time",
            "activity.operator": "Operator",
            "activity.role": "Role",
            "activity.actionType": "Action",
            "activity.description": "Description",
            "activity.stateChange": "State Change",
            "activity.filterType": "Action Type",
            "activity.filterUser": "User",
            "activity.filterRole": "Role",
            "activity.filterFrom": "From",
            "activity.filterTo": "To",
            "activity.resultCount": "Showing",
            "activity.resultSuffix": "record(s)",
            "activity.empty": "No activity records found.",
            "jobList.heading": "Available TA Jobs",
            "jobList.desc": "Browse all open positions and view the required skills before applying.",
            "jobList.empty": "No jobs are available right now.",
            "jobDetail.matchTitle": "Match Result",
            "jobDetail.submitted": "Application submitted successfully.",
            "jobDetail.duplicate": "You have already applied for this job.",
            "jobDetail.withdrawn": "Application withdrawn successfully.",
            "jobDetail.matchScore": "Match Score",
            "jobDetail.matchedSkills": "Matched Skills",
            "jobDetail.missingSkills": "Missing Skills",
            "profile.heading": "Applicant Profile",
            "profile.desc": "Update your name, email and skills for better matching.",
            "profile.updated": "Profile updated successfully.",
            "profile.fullName": "Full Name",
            "profile.email": "Email",
            "profile.skills": "Skills",
            "profile.skillsPlaceholder": "Java, Excel, Communication",
            "profile.skillsHint": "Use comma-separated skills, for example: Java, Python, Communication",
            "applications.heading": "My Application Records",
            "applications.desc": "Track the latest result of each job application.",
            "applications.appliedAt": "Applied At",
            "applications.matchScore": "Match Score",
            "applications.missingSkills": "Missing Skills",
            "applications.statusDescription": "Status Description",
            "applications.empty": "You have not submitted any applications yet.",
            "applications.withdrawnSuccess": "Application withdrawn successfully.",
            "moJobs.heading": "My Posted Jobs",
            "moJobs.desc": "Manage the jobs you have created and review all applicants.",
            "moJobs.created": "Job created successfully.",
            "moJobs.updated": "Job updated successfully.",
            "moJobs.empty": "You have not created any jobs yet.",
            "moJobs.openJobs": "Open Jobs",
            "moJobs.completedJobs": "Completed Jobs",
            "moJobs.noOpenJobs": "No open jobs found.",
            "moJobs.noCompletedJobs": "No completed jobs found.",
            "action.markAsCompleted": "Mark as Completed",
            "action.reopen": "Reopen",
            "action.viewApplications": "View Applications",
            "job.status.open": "Open",
            "job.status.completed": "Completed",
            "moApplications.heading": "Applications for",
            "moApplications.updated": "Application status updated successfully.",
            "moApplications.applicant": "Applicant",
            "moApplications.matchScore": "Match Score",
            "moApplications.missingSkills": "Missing Skills",
            "moApplications.priorityScore": "Priority Score",
            "moApplications.decisionBand": "Decision Band",
            "moApplications.fitScore": "Fit Score",
            "moApplications.nextStep": "Next Step",
            "moApplications.projectedWorkload": "Projected Workload",
            "moApplications.projectedHours": "Projected hours",
            "moApplications.workloadWarning": "Review workload before accepting.",
            "moApplications.update": "Update",
            "moApplications.empty": "No applicants yet for this job.",
            "editJob.heading": "Edit TA Job",
            "editJob.desc": "Modify the position details below.",
            "createJob.heading": "Create a New TA Job",
            "createJob.desc": "Fill in the position information below.",
            "createJob.jobTitle": "Job Title",
            "createJob.moduleName": "Module / Activity Name",
            "createJob.description": "Description",
            "createJob.requiredSkills": "Required Skills",
            "createJob.requiredSkillsPlaceholder": "Java, Python, Communication",
            "createJob.workloadHours": "Workload Hours",
            "admin.heading": "Applicant Workload Overview",
            "admin.thresholdPrefix": "Applicants above",
            "admin.thresholdSuffix": "accepted hours are marked as overloaded.",
            "admin.applicantName": "Applicant Name",
            "admin.acceptedJobsCount": "Accepted Jobs Count",
            "admin.totalHours": "Total Hours",
            "admin.workloadStatus": "Workload Status",
            "admin.empty": "No applicant workload data found.",
            "admin.userManagementHeading": "User Management",
            "admin.userManagementDesc": "Manage all users in the system. You can view, create, and delete accounts.",
            "admin.createUser": "Create New User",
            "admin.createUserDesc": "Create a new user account for the system.",
            "admin.userCreated": "User created successfully.",
            "admin.applicants": "Applicants",
            "admin.mos": "Module Organizers (MO)",
            "admin.admins": "Administrators",
            "admin.noApplicants": "No applicants found.",
            "admin.noMOs": "No module organizers found.",
            "admin.noAdmins": "No administrators found.",
            "admin.resetPasswordTitle": "Reset User Password",
            "admin.resetPasswordSuccess": "Password reset successfully for user: ",
            "error.heading": "Something went wrong",
            "login.systemTitle": "Teaching Assistant Recruitment System",
            "login.intro": "Switch between roles, review applications quickly, and manage the TA process in one cleaner workspace.",
            "login.highlightApplicantTitle": "Applicants",
            "login.highlightApplicantDesc": "Maintain profiles, compare matches, and track applications.",
            "login.highlightMoTitle": "Module Organisers",
            "login.highlightMoDesc": "Create positions and update results from one place.",
            "login.highlightAdminTitle": "Administrators",
            "login.highlightAdminDesc": "Monitor accepted workload and spot overload risks early.",
            "login.formTitle": "Sign in",
            "login.formDesc": "Use the demo accounts below or your own project credentials.",
            "login.username": "Username",
            "login.password": "Password",
            "login.usernamePlaceholder": "Enter username",
            "login.passwordPlaceholder": "Enter password",
            "login.demoTitle": "Demo Accounts",
            "login.demoApplicant": "Applicant",
            "login.demoMo": "Module Organiser",
            "login.demoAdmin": "Administrator",
            "confirm.submitApplication": "Submit application for this job?",
            "confirm.updateApplication": "Update this application status?",
            "confirm.deleteUser": "Are you sure you want to delete this user?",
            "confirm.withdrawApplication": "Are you sure you want to withdraw this application? Withdrawn applications cannot be restored.",
            "table.name": "Name",
            "table.email": "Email",
            "table.username": "Username",
            "table.role": "Role",
            "table.actions": "Actions",
            "form.role": "Role",
            "form.fullName": "Full Name",
            "form.email": "Email",
            "form.username": "Username",
            "form.password": "Password",
            "form.newPassword": "New Password",
            "form.confirmPassword": "Confirm Password",
            "form.fullNamePlaceholder": "Enter full name",
            "form.emailPlaceholder": "Enter email",
            "form.usernamePlaceholder": "Enter username",
            "form.passwordPlaceholder": "Enter password (min 6 chars)",
            "form.newPasswordPlaceholder": "Enter new password (min 6 chars)",
            "form.confirmPasswordPlaceholder": "Confirm new password",
            "admin.searchPlaceholder": "Search by name...",
            "action.search": "Search",
            "action.clear": "Clear",
            "common.all": "All",
            "action.viewAll": "View All",
            "action.filter": "Filter",
            "action.reset": "Reset",
            "action.applyJob": "Apply Job",
            "action.withdrawApplication": "Withdraw Application",
            "action.completeJob": "Complete Job",
            "action.reopenJob": "Reopen Job",
            "action.updateStatus": "Update Status",
            "action.deleteUser": "Delete User",
            "nav.myActivity": "My Activity",
            "nav.activityLog": "Activity Log",
            "nav.aiSkillPath": "AI Skill Path",
            "action.aiLearningPath": "Get AI Learning Path",
            "page.skillRecommend": "AI Skill Learning Path",
            "skillRecommend.title": "AI Skill Learning Path",
            "skillRecommend.subtitle": "Personalised learning recommendations for your missing skills based on the requirements of this position.",
            "skillRecommend.disclaimerTitle": "AI Output Notice:",
            "skillRecommend.disclaimer": "The recommendations below were generated by a large language model (LLM) and validated by structured logic before display. The reason field explains the AI\u2019s rationale to satisfy explainability requirements. Learning resources are verified to use HTTPS and checked against a curated catalogue. Always exercise your own judgement when following AI-generated study plans.",
            "skillRecommend.allMatched": "You have all the required skills!",
            "skillRecommend.allMatchedSub": "Great news \u2013 your current skills fully satisfy the requirements for this position. You can apply directly without any additional preparation.",
            "skillRecommend.whyMatters": "Why this skill matters",
            "skillRecommend.learningPath": "Suggested Learning Path",
            "skillRecommend.resources": "Learning Resources",
            "action.back": "Back to Job",
            "action.viewJob": "Back to Job Detail",
            "jobList.deadline": "Deadline",
            "jobDetail.deadline": "Application Deadline",
            "jobDetail.deadlinePassed": "Closed",
            "jobDetail.deadlineToday": "Closes today!",
            "jobDetail.daysLeft": "days left",
            "jobDetail.deadlineExpired": "The application deadline has passed. Your application was not submitted.",
            "page.myActivity": "My Activity History",
            "page.adminActivity": "Activity Log",
            "dashboard.recentActivity": "Recent Activity",
            "activity.myHeading": "My Activity History",
            "activity.myDesc": "A complete record of all actions you have performed in the system.",
            "activity.adminHeading": "System Activity Log",
            "activity.adminDesc": "View and filter all user actions recorded in the system.",
            "activity.filterType": "Action Type",
            "activity.filterUser": "User Name",
            "activity.filterRole": "Role",
            "activity.filterFrom": "From",
            "activity.filterTo": "To",
            "activity.time": "Time",
            "activity.actionType": "Action Type",
            "activity.description": "Description",
            "activity.stateChange": "State Change",
            "activity.operator": "Operator",
            "activity.role": "Role",
            "activity.empty": "No activity records found.",
            "activity.dashboardEmpty": "No recent activity. Start by browsing jobs or managing your postings.",
            "activity.resultCount": "Showing",
            "activity.resultSuffix": "record(s)",
            "cv.heading": "Resume / CV",
            "cv.desc": "Upload your CV so Module Organisers can review it alongside your application.",
            "cv.upload": "Upload CV",
            "cv.replace": "Replace CV",
            "cv.download": "Download CV",
            "cv.delete": "Delete CV",
            "cv.empty": "No resume uploaded yet.",
            "cv.none": "—",
            "cv.chooseFile": "Choose file…",
            "cv.hint": "PDF, DOC, DOCX — max 2 MB",
            "cv.uploaded": "CV uploaded successfully.",
            "cv.replaced": "CV replaced successfully.",
            "cv.deleted": "CV deleted successfully.",
            "cv.error.empty": "No file selected. Please choose a file before uploading.",
            "cv.error.size": "File is too large. Maximum allowed size is 2 MB.",
            "cv.error.type": "Invalid file type. Only PDF, DOC, and DOCX files are accepted.",
            "cv.error.sizeClient": "File exceeds the 2 MB limit. Please choose a smaller file.",
            "confirm.deleteCV": "Are you sure you want to delete your CV? This action cannot be undone.",
            "table.applicationId": "Application ID",
            "table.applicant": "Applicant",
            "table.job": "Job",
            "table.appliedAt": "Applied At",
            "admin.allApplicationsHeading": "All Applications",
            "admin.allApplicationsDesc": "View all applications submitted by applicants across all jobs.",
            "admin.noApplications": "No applications found."
        },
        zh: {
            "brand.title": "助教招聘系统",
            "brand.subtitle": "北邮国际学院",
            "workspace.kicker": "招聘工作台",
            "sidebar.account": "当前账号",
            "page.dashboard": "控制台",
            "page.jobs": "岗位浏览",
            "page.jobDetail": "岗位详情",
            "page.profile": "我的资料",
            "page.myApplications": "我的申请",
            "page.myJobs": "我的岗位",
            "page.jobApplications": "申请列表",
            "page.createJob": "创建岗位",
            "page.editJob": "编辑岗位",
            "page.workload": "工作量总览",
            "page.userManagement": "账号管理",
            "page.error": "错误",
            "page.login": "登录",
            "page.myActivity": "我的操作记录",
            "page.adminActivity": "系统操作日志",
            "nav.dashboard": "控制台",
            "nav.profile": "我的资料",
            "nav.jobs": "浏览岗位",
            "nav.myApplications": "我的申请",
            "nav.moJobs": "我的岗位",
            "nav.createJob": "创建岗位",
            "nav.workload": "工作量总览",
            "nav.userManagement": "账号管理",
            "nav.aiQuery": "AI 查询",
            "page.aiQuery": "AI 数据查询",
            "aiQuery.heading": "AI 数据查询",
            "aiQuery.intro": "你可以用自然语言询问用户、岗位、申请或工作量相关问题，回答基于当前 JSON 数据快照生成。",
            "aiQuery.askTitle": "提出问题",
            "aiQuery.questionLabel": "你的问题",
            "aiQuery.examples": "示例：有多少待处理申请？有哪些开放岗位？哪些 TA 超负荷？",
            "aiQuery.submit": "提交查询",
            "aiQuery.snapshotTitle": "当前数据快照",
            "aiQuery.answerTitle": "AI 回答",
            "action.logout": "退出登录",
            "action.login": "登录",
            "action.save": "保存",
            "action.saveProfile": "保存资料",
            "action.browseJobs": "浏览岗位",
            "action.editProfile": "编辑资料",
            "action.createJob": "创建岗位",
            "action.viewMyJobs": "查看岗位",
            "action.viewWorkload": "查看总览",
            "action.manageUsers": "管理用户",
            "action.viewDetail": "查看详情",
            "action.viewApplications": "查看申请",
            "action.editJob": "编辑岗位",
            "action.reviewApplications": "去审核",
            "action.saveChanges": "保存修改",
            "action.goBack": "返回上一页",
            "action.returnDashboard": "返回控制台",
            "action.applyNow": "立即申请",
            "action.create": "创建",
            "action.delete": "删除",
            "action.withdraw": "撤回申请",
            "action.withdrawApplication": "撤回申请",
            "action.applyJob": "申请岗位",
            "action.completeJob": "完成岗位",
            "action.reopenJob": "重新开放岗位",
            "action.updateStatus": "更新申请状态",
            "action.deleteUser": "删除用户",
            "action.filter": "筛选",
            "action.reset": "重置",
            "action.resetPassword": "重置密码",
            "action.cancel": "取消",
            "action.createUser": "创建新用户",
            "role.APPLICANT": "申请人",
            "role.MO": "课程负责人",
            "role.ADMIN": "管理员",
            "status.Pending": "待处理",
            "status.Accepted": "已录用",
            "status.Rejected": "已拒绝",
            "status.Open": "开放中",
            "status.Normal": "正常",
            "status.Overloaded": "超负荷",
            "status.Applied": "已申请",
            "status.AlreadyApplied": "你已申请过该岗位",
            "statusDesc.Pending": "等待课程负责人审核。",
            "statusDesc.Accepted": "你已被该岗位录用。",
            "statusDesc.Rejected": "你的申请未被录用。",
            "status.High": "高",
            "status.Medium": "中",
            "status.Low": "低",
            "status.Advance": "优先推进",
            "status.Discuss": "进一步讨论",
            "status.Hold": "暂缓",
            "status.Light": "轻负载",
            "status.Heavy": "较重",
            "status.Critical": "高风险",
            "common.title": "标题",
            "common.jobTitle": "岗位名称",
            "common.module": "课程 / 模块",
            "common.description": "描述",
            "common.hours": "工时",
            "common.requiredSkills": "所需技能",
            "common.status": "状态",
            "common.action": "操作",
            "common.none": "无",
            "common.all": "全部",
            "dashboard.welcome": "欢迎，",
            "dashboard.applicantIntro": "你可以维护个人资料、浏览助教岗位，并持续跟进申请状态。",
            "dashboard.moIntro": "你可以创建助教岗位、审核申请人，并更新申请结果。",
            "dashboard.adminIntro": "你可以查看整体助教工作量，并快速识别超负荷的申请人。",
            "dashboard.applicantPanel": "申请人面板",
            "dashboard.openJobs": "开放岗位数",
            "dashboard.myApplications": "我的申请数",
            "dashboard.profileTitle": "个人资料",
            "dashboard.profileDesc": "及时更新你的技能信息，系统才能给出更准确的匹配分数。",
            "dashboard.profileCompleted": "已完善",
            "dashboard.profileIncomplete": "未完善",
            "dashboard.moPanel": "课程负责人面板",
            "dashboard.myPostedJobs": "已发布岗位数",
            "dashboard.pendingApplications": "待处理申请数",
            "dashboard.jobsNeedingAction": "待处理岗位数",
            "dashboard.totalUsers": "用户总数",
            "dashboard.totalApplicants": "申请人数",
            "dashboard.totalMOs": "课程负责人数",
            "dashboard.acceptanceRate": "录用率",
            "dashboard.overloadedTAs": "超负荷 TA 数",
            "dashboard.recentApplications": "最近待处理申请",
            "dashboard.noRecentApplications": "当前还没有待处理申请。",
            "dashboard.actionRequiredJobs": "需要处理的岗位",
            "dashboard.noActionRequiredJobs": "所有开放岗位都已处理完毕。",
            "dashboard.pendingCount": "待处理",
            "dashboard.newJobTitle": "创建新岗位",
            "dashboard.newJobDesc": "发布新的助教岗位并开始收集申请。",
            "dashboard.adminPanel": "管理员面板",
            "dashboard.applicantsInSystem": "系统申请人数",
            "dashboard.workloadRuleTitle": "工作量规则",
            "dashboard.workloadRuleDesc": "录用工时超过 10 小时的申请人会被标记为超负荷。",
            "dashboard.userManagementTitle": "账号管理",
            "dashboard.userManagementDesc": "创建、查看和管理系统中的所有用户账号。",
            "dashboard.notifications": "通知",
            "notification.type.applicationStatus": "申请状态",
            "notification.type.newApplication": "新申请",
            "notification.time": "时间",
            "activity.myHeading": "我的操作记录",
            "activity.myDesc": "查看当前账号最近记录的操作。",
            "activity.adminHeading": "系统操作日志",
            "activity.adminDesc": "审计招聘系统内的用户操作。",
            "activity.time": "时间",
            "activity.operator": "操作者",
            "activity.role": "角色",
            "activity.actionType": "操作类型",
            "activity.description": "描述",
            "activity.stateChange": "状态变化",
            "activity.filterType": "操作类型",
            "activity.filterUser": "用户",
            "activity.filterRole": "角色",
            "activity.filterFrom": "开始时间",
            "activity.filterTo": "结束时间",
            "activity.resultCount": "显示",
            "activity.resultSuffix": "条记录",
            "activity.empty": "没有找到操作记录。",
            "jobList.heading": "当前开放的助教岗位",
            "jobList.desc": "浏览所有开放岗位，并在申请前查看所需技能。",
            "jobList.empty": "当前暂无可申请岗位。",
            "jobDetail.matchTitle": "匹配结果",
            "jobDetail.submitted": "申请已成功提交。",
            "jobDetail.duplicate": "你已经申请过这个岗位。",
            "jobDetail.withdrawn": "申请已成功撤回。",
            "jobDetail.matchScore": "匹配分数",
            "jobDetail.matchedSkills": "已匹配技能",
            "jobDetail.missingSkills": "缺失技能",
            "profile.heading": "申请人资料",
            "profile.desc": "更新你的姓名、邮箱和技能信息，以获得更好的匹配结果。",
            "profile.updated": "资料更新成功。",
            "profile.fullName": "姓名",
            "profile.email": "邮箱",
            "profile.skills": "技能",
            "profile.skillsPlaceholder": "Java, Excel, 沟通协作",
            "profile.skillsHint": "请用英文逗号分隔技能，例如：Java, Python, Communication",
            "applications.heading": "我的申请记录",
            "applications.desc": "跟踪每个岗位申请的最新处理结果。",
            "applications.appliedAt": "申请时间",
            "applications.matchScore": "匹配分数",
            "applications.missingSkills": "缺失技能",
            "applications.statusDescription": "状态说明",
            "applications.empty": "你还没有提交任何申请。",
            "applications.withdrawnSuccess": "申请已成功撤回。",
            "moJobs.heading": "我发布的岗位",
            "moJobs.desc": "管理你创建的岗位，并查看所有申请人。",
            "moJobs.created": "岗位创建成功。",
            "moJobs.updated": "岗位更新成功。",
            "moJobs.empty": "你还没有创建任何岗位。",
            "moJobs.openJobs": "招聘中",
            "moJobs.completedJobs": "已完成招聘",
            "moJobs.noOpenJobs": "没有招聘中的岗位。",
            "moJobs.noCompletedJobs": "没有已完成招聘的岗位。",
            "action.markAsCompleted": "标记为已完成",
            "action.reopen": "重新开放",
            "action.viewApplications": "查看申请",
            "job.status.open": "招聘中",
            "job.status.completed": "已完成",
            "moApplications.heading": "岗位申请：",
            "moApplications.updated": "申请状态更新成功。",
            "moApplications.applicant": "申请人",
            "moApplications.matchScore": "匹配分数",
            "moApplications.missingSkills": "缺失技能",
            "moApplications.priorityScore": "优先级分数",
            "moApplications.decisionBand": "推荐等级",
            "moApplications.fitScore": "适配分数",
            "moApplications.nextStep": "建议动作",
            "moApplications.projectedWorkload": "录用后负载",
            "moApplications.projectedHours": "预计总工时",
            "moApplications.workloadWarning": "录用前请复核工作量。",
            "moApplications.update": "更新",
            "moApplications.empty": "该岗位暂时还没有申请人。",
            "editJob.heading": "编辑助教岗位",
            "editJob.desc": "请修改下面的岗位信息。",
            "createJob.heading": "创建新的助教岗位",
            "createJob.desc": "请填写下面的岗位信息。",
            "createJob.jobTitle": "岗位标题",
            "createJob.moduleName": "课程 / 活动名称",
            "createJob.description": "岗位描述",
            "createJob.requiredSkills": "所需技能",
            "createJob.requiredSkillsPlaceholder": "Java, Python, Communication",
            "createJob.workloadHours": "工作量小时数",
            "admin.heading": "申请人工作量总览",
            "admin.thresholdPrefix": "录用工时超过",
            "admin.thresholdSuffix": "小时的申请人会被标记为超负荷。",
            "admin.applicantName": "申请人姓名",
            "admin.acceptedJobsCount": "录用岗位数",
            "admin.totalHours": "总工时",
            "admin.workloadStatus": "工作量状态",
            "admin.empty": "未找到申请人的工作量数据。",
            "admin.userManagementHeading": "账号管理",
            "admin.userManagementDesc": "管理系统中的所有用户。可以查看、创建和删除账号。",
            "admin.createUser": "创建新用户",
            "admin.createUserDesc": "为系统创建新的用户账号。",
            "admin.userCreated": "用户创建成功。",
            "admin.applicants": "申请人",
            "admin.mos": "课程负责人 (MO)",
            "admin.admins": "管理员",
            "admin.noApplicants": "未找到申请人。",
            "admin.noMOs": "未找到课程负责人。",
            "admin.noAdmins": "未找到管理员。",
            "admin.resetPasswordTitle": "重置用户密码",
            "admin.resetPasswordSuccess": "密码重置成功，用户：",
            "error.heading": "出现了一些问题",
            "login.systemTitle": "Teaching Assistant Recruitment System",
            "login.intro": "在同一套更清晰的工作台中切换角色、快速审阅申请并管理助教招聘流程。",
            "login.highlightApplicantTitle": "申请人",
            "login.highlightApplicantDesc": "维护资料、查看匹配结果并持续跟踪申请。",
            "login.highlightMoTitle": "课程负责人",
            "login.highlightMoDesc": "统一创建岗位并更新申请结果。",
            "login.highlightAdminTitle": "管理员",
            "login.highlightAdminDesc": "监控录用工时，及时识别超负荷风险。",
            "login.formTitle": "登录系统",
            "login.formDesc": "可直接使用下方演示账号，或输入你自己的项目账号。",
            "login.username": "用户名",
            "login.password": "密码",
            "login.usernamePlaceholder": "请输入用户名",
            "login.passwordPlaceholder": "请输入密码",
            "login.demoTitle": "演示账号",
            "login.demoApplicant": "申请人",
            "login.demoMo": "课程负责人",
            "login.demoAdmin": "管理员",
            "confirm.submitApplication": "确认提交该岗位申请吗？",
            "confirm.updateApplication": "确认更新该申请状态吗？",
            "confirm.deleteUser": "确定要删除此用户吗？",
            "confirm.withdrawApplication": "确定要撤回此申请吗？撤回后将无法恢复。",
            "table.name": "姓名",
            "table.email": "邮箱",
            "table.username": "用户名",
            "table.role": "角色",
            "table.actions": "操作",
            "form.role": "角色",
            "form.fullName": "姓名",
            "form.email": "邮箱",
            "form.username": "用户名",
            "form.password": "密码",
            "form.newPassword": "新密码",
            "form.confirmPassword": "确认密码",
            "form.fullNamePlaceholder": "请输入姓名",
            "form.emailPlaceholder": "请输入邮箱",
            "form.usernamePlaceholder": "请输入用户名",
            "form.passwordPlaceholder": "请输入密码",
            "form.newPasswordPlaceholder": "请输入新密码（至少6位）",
            "form.confirmPasswordPlaceholder": "请确认密码",
            "admin.searchPlaceholder": "按姓名搜索...",
            "action.search": "搜索",
            "action.clear": "清除",
            "common.all": "全部",
            "action.viewAll": "查看全部",
            "action.filter": "筛选",
            "action.reset": "重置",
            "action.applyJob": "申请岗位",
            "action.withdrawApplication": "撤回申请",
            "action.completeJob": "完成招聘",
            "action.reopenJob": "重新开放",
            "action.updateStatus": "更新状态",
            "action.deleteUser": "删除用户",
            "nav.myActivity": "我的操作历史",
            "nav.activityLog": "操作日志",
            "nav.aiSkillPath": "AI 技能路径",
            "action.aiLearningPath": "AI 学习路径",
            "page.skillRecommend": "AI 技能学习路径",
            "skillRecommend.title": "AI 技能学习路径",
            "skillRecommend.subtitle": "针对当前岗位的缺失技能，为你生成个性化学习路径建议。",
            "skillRecommend.disclaimerTitle": "AI 输出说明：",
            "skillRecommend.disclaimer": "以下建议由大语言模型生成，并经过结构化逻辑校验后展示。每张卡片包含\u300c为什么这项技能很重要\u300d的理由，以满足可解释性要求。资源链接均验证为 HTTPS 并经过精选目录核对。请始终运用自己的判断参考 AI 生成的学习计划。",
            "skillRecommend.allMatched": "你已具备所有必需技能！",
            "skillRecommend.allMatchedSub": "恭喜！你目前的技能完全满足该岗位要求，可以直接申请，无需额外准备。",
            "skillRecommend.whyMatters": "为什么这项技能很重要",
            "skillRecommend.learningPath": "建议学习路径",
            "skillRecommend.resources": "学习资源",
            "action.back": "返回岗位",
            "action.viewJob": "返回岗位详情",
            "jobList.deadline": "截止日期",
            "jobDetail.deadline": "申请截止日期",
            "jobDetail.deadlinePassed": "已关闭",
            "jobDetail.deadlineToday": "今天截止！",
            "jobDetail.daysLeft": "天后截止",
            "jobDetail.deadlineExpired": "申请截止日期已过，你的申请未提交。",
            "page.myActivity": "我的操作历史",
            "page.adminActivity": "系统操作日志",
            "dashboard.recentActivity": "最近操作",
            "activity.myHeading": "我的操作历史",
            "activity.myDesc": "系统中你所有操作行为的完整记录。",
            "activity.adminHeading": "系统操作日志",
            "activity.adminDesc": "查看并筛选系统中所有用户的操作记录。",
            "activity.filterType": "操作类型",
            "activity.filterUser": "用户姓名",
            "activity.filterRole": "角色",
            "activity.filterFrom": "开始日期",
            "activity.filterTo": "结束日期",
            "activity.time": "时间",
            "activity.actionType": "操作类型",
            "activity.description": "操作描述",
            "activity.stateChange": "状态变化",
            "activity.operator": "操作人",
            "activity.role": "角色",
            "activity.empty": "暂无操作记录。",
            "activity.dashboardEmpty": "暂无最近操作。浏览岗位或管理你的招聘开始记录吧。",
            "activity.resultCount": "共",
            "activity.resultSuffix": "条记录",
            "cv.heading": "简历 / CV",
            "cv.desc": "上传你的简历，方便课程负责人在审核申请时一并查阅。",
            "cv.upload": "上传简历",
            "cv.replace": "替换简历",
            "cv.download": "下载简历",
            "cv.delete": "删除简历",
            "cv.empty": "尚未上传简历。",
            "cv.none": "—",
            "cv.chooseFile": "选择文件…",
            "cv.hint": "支持 PDF、DOC、DOCX，最大 2 MB",
            "cv.uploaded": "简历上传成功。",
            "cv.replaced": "简历替换成功。",
            "cv.deleted": "简历已删除。",
            "cv.error.empty": "未选择文件，请先选择文件再上传。",
            "cv.error.size": "文件过大，最大允许 2 MB。",
            "cv.error.type": "文件类型不支持，仅接受 PDF、DOC 和 DOCX 格式。",
            "cv.error.sizeClient": "文件超过 2 MB 限制，请选择更小的文件。",
            "confirm.deleteCV": "确定要删除你的简历吗？此操作无法撤销。",
            "table.applicationId": "申请编号",
            "table.applicant": "申请人",
            "table.job": "岗位",
            "table.appliedAt": "申请时间",
            "admin.allApplicationsHeading": "全部申请",
            "admin.allApplicationsDesc": "查看系统中所有岗位的全部申请记录。",
            "admin.noApplications": "暂无申请记录。"
        }
    };

    translations.en["login.formTitle"] = "Access Workspace";
    translations.zh["login.formTitle"] = "\u8fdb\u5165\u5de5\u4f5c\u53f0";
    translations.en["login.heroTitle"] = "TA Recruitment\nSystem";
    translations.zh["login.heroTitle"] = "\u52a9\u6559\u62db\u8058\u7cfb\u7edf";
    translations.zh["brand.title"] = "\u52a9\u6559\u62db\u8058\u7cfb\u7edf";
    translations.zh["brand.subtitle"] = "\u5317\u90ae\u56fd\u9645\u5b66\u9662";
    translations.zh["workspace.kicker"] = "\u62db\u8058\u5de5\u4f5c\u53f0";
    translations.zh["sidebar.account"] = "\u5f53\u524d\u8d26\u53f7";
    translations.zh["page.dashboard"] = "\u63a7\u5236\u53f0";
    translations.zh["page.login"] = "\u767b\u5f55";
    translations.zh["page.profile"] = "\u6211\u7684\u8d44\u6599";
    translations.zh["page.jobs"] = "\u5c97\u4f4d\u6d4f\u89c8";
    translations.zh["page.myApplications"] = "\u6211\u7684\u7533\u8bf7";
    translations.zh["nav.dashboard"] = "\u63a7\u5236\u53f0";
    translations.zh["nav.profile"] = "\u6211\u7684\u8d44\u6599";
    translations.zh["nav.jobs"] = "\u6d4f\u89c8\u5c97\u4f4d";
    translations.zh["nav.myApplications"] = "\u6211\u7684\u7533\u8bf7";
    translations.zh["action.logout"] = "\u9000\u51fa\u767b\u5f55";
    translations.zh["action.login"] = "\u767b\u5f55";
    translations.zh["action.saveProfile"] = "\u4fdd\u5b58\u8d44\u6599";
    translations.zh["role.APPLICANT"] = "\u7533\u8bf7\u4eba";
    translations.zh["login.systemTitle"] = "\u6559\u5b66\u52a9\u7406\u62db\u8058\u7cfb\u7edf";
    translations.zh["login.intro"] = "\u5728\u540c\u4e00\u4e2a\u66f4\u6e05\u6670\u7684\u5de5\u4f5c\u53f0\u4e2d\u5207\u6362\u89d2\u8272\uff0c\u5feb\u901f\u5ba1\u9605\u7533\u8bf7\uff0c\u5e76\u7ba1\u7406 TA \u62db\u8058\u6d41\u7a0b\u3002";
    translations.zh["login.highlightApplicantTitle"] = "\u7533\u8bf7\u4eba";
    translations.zh["login.highlightApplicantDesc"] = "\u7ef4\u62a4\u8d44\u6599\u3001\u67e5\u770b\u5339\u914d\u60c5\u51b5\u5e76\u8ddf\u8e2a\u7533\u8bf7\u8fdb\u5ea6\u3002";
    translations.zh["login.highlightMoTitle"] = "\u8bfe\u7a0b\u8d1f\u8d23\u4eba";
    translations.zh["login.highlightMoDesc"] = "\u5728\u4e00\u4e2a\u5165\u53e3\u521b\u5efa\u5c97\u4f4d\u5e76\u66f4\u65b0\u7533\u8bf7\u7ed3\u679c\u3002";
    translations.zh["login.highlightAdminTitle"] = "\u7ba1\u7406\u5458";
    translations.zh["login.highlightAdminDesc"] = "\u76d1\u63a7\u5f55\u7528\u5de5\u65f6\uff0c\u53ca\u65f6\u53d1\u73b0\u8d85\u8d1f\u8377\u98ce\u9669\u3002";
    translations.zh["login.username"] = "\u7528\u6237\u540d";
    translations.zh["login.password"] = "\u5bc6\u7801";
    translations.zh["login.usernamePlaceholder"] = "\u8bf7\u8f93\u5165\u7528\u6237\u540d";
    translations.zh["login.passwordPlaceholder"] = "\u8bf7\u8f93\u5165\u5bc6\u7801";
    translations.zh["login.demoTitle"] = "\u6f14\u793a\u8d26\u53f7";
    translations.zh["login.demoApplicant"] = "\u7533\u8bf7\u4eba";
    translations.zh["login.demoMo"] = "\u8bfe\u7a0b\u8d1f\u8d23\u4eba";
    translations.zh["login.demoAdmin"] = "\u7ba1\u7406\u5458";
    translations.en["login.formDesc"] = "Sign in with an existing account or create a new applicant account.";
    translations.zh["login.formDesc"] = "\u4f7f\u7528\u73b0\u6709\u8d26\u53f7\u767b\u5f55\uff0c\u6216\u65b0\u5efa\u4e00\u4e2a\u7533\u8bf7\u4eba\u8d26\u53f7\u3002";
    translations.en["login.tabSignIn"] = "Sign in";
    translations.zh["login.tabSignIn"] = "\u767b\u5f55";
    translations.en["login.tabRegister"] = "Create account";
    translations.zh["login.tabRegister"] = "\u6ce8\u518c\u8d26\u53f7";
    translations.en["login.quickStart"] = "Quick Start";
    translations.zh["login.quickStart"] = "\u5feb\u901f\u4e0a\u624b";
    translations.en["login.stepAccountTitle"] = "Create an applicant account";
    translations.zh["login.stepAccountTitle"] = "\u521b\u5efa\u7533\u8bf7\u4eba\u8d26\u53f7";
    translations.en["login.stepAccountDesc"] = "Students can register directly and enter their skills immediately.";
    translations.zh["login.stepAccountDesc"] = "\u5b66\u751f\u53ef\u4ee5\u76f4\u63a5\u6ce8\u518c\uff0c\u5e76\u7acb\u5373\u586b\u5199\u521d\u59cb\u6280\u80fd\u4fe1\u606f\u3002";
    translations.en["login.stepProfileTitle"] = "Complete profile details";
    translations.zh["login.stepProfileTitle"] = "\u5b8c\u5584\u4e2a\u4eba\u8d44\u6599";
    translations.en["login.stepProfileDesc"] = "Update your name, email and skills to improve the match score.";
    translations.zh["login.stepProfileDesc"] = "\u66f4\u65b0\u59d3\u540d\u3001\u90ae\u7bb1\u548c\u6280\u80fd\uff0c\u53ef\u4ee5\u63d0\u5347\u5c97\u4f4d\u5339\u914d\u5206\u6570\u3002";
    translations.en["login.stepApplyTitle"] = "Track results in one place";
    translations.zh["login.stepApplyTitle"] = "\u5728\u4e00\u4e2a\u5de5\u4f5c\u53f0\u8ddf\u8e2a\u7ed3\u679c";
    translations.en["login.stepApplyDesc"] = "Apply for jobs, review outcomes and manage workload from the same workspace.";
    translations.zh["login.stepApplyDesc"] = "\u5728\u540c\u4e00\u4e2a\u5de5\u4f5c\u53f0\u7533\u8bf7\u5c97\u4f4d\u3001\u67e5\u770b\u7ed3\u679c\u5e76\u7ba1\u7406\u5de5\u65f6\u3002";
    translations.en["register.fullName"] = "Full Name";
    translations.zh["register.fullName"] = "\u59d3\u540d";
    translations.en["register.fullNamePlaceholder"] = "Enter your full name";
    translations.zh["register.fullNamePlaceholder"] = "\u8bf7\u8f93\u5165\u4f60\u7684\u59d3\u540d";
    translations.en["register.email"] = "Email";
    translations.zh["register.email"] = "\u90ae\u7bb1";
    translations.en["register.emailPlaceholder"] = "Enter your email";
    translations.zh["register.emailPlaceholder"] = "\u8bf7\u8f93\u5165\u4f60\u7684\u90ae\u7bb1";
    translations.en["register.username"] = "Username";
    translations.zh["register.username"] = "\u7528\u6237\u540d";
    translations.en["register.usernamePlaceholder"] = "4-20 characters";
    translations.zh["register.usernamePlaceholder"] = "4-20 \u4f4d\u5b57\u7b26";
    translations.en["register.password"] = "Password";
    translations.zh["register.password"] = "\u5bc6\u7801";
    translations.en["register.passwordPlaceholder"] = "At least 6 characters";
    translations.zh["register.passwordPlaceholder"] = "\u81f3\u5c11 6 \u4f4d";
    translations.en["register.confirmPassword"] = "Confirm Password";
    translations.zh["register.confirmPassword"] = "\u786e\u8ba4\u5bc6\u7801";
    translations.en["register.confirmPasswordPlaceholder"] = "Re-enter your password";
    translations.zh["register.confirmPasswordPlaceholder"] = "\u8bf7\u518d\u6b21\u8f93\u5165\u5bc6\u7801";
    translations.en["register.skills"] = "Initial Skills";
    translations.zh["register.skills"] = "\u521d\u59cb\u6280\u80fd";
    translations.en["register.skillsPlaceholder"] = "Java, Communication, Data Analysis";
    translations.zh["register.skillsPlaceholder"] = "Java, \u6c9f\u901a\u534f\u4f5c, \u6570\u636e\u5206\u6790";
    translations.en["register.skillsHint"] = "Optional. Separate skills with commas. You can edit them later in My Profile.";
    translations.zh["register.skillsHint"] = "\u9009\u586b\uff0c\u8bf7\u7528\u82f1\u6587\u9017\u53f7\u5206\u9694\u591a\u4e2a\u6280\u80fd\uff0c\u540e\u7eed\u4ecd\u53ef\u5728\u201c\u6211\u7684\u8d44\u6599\u201d\u4e2d\u4fee\u6539\u3002";
    translations.en["register.scopeTitle"] = "Registration Scope";
    translations.zh["register.scopeTitle"] = "\u6ce8\u518c\u8303\u56f4";
    translations.en["register.scopeDesc"] = "Self-registration is available for applicant accounts only. MO and administrator accounts remain managed by the system.";
    translations.zh["register.scopeDesc"] = "\u76ee\u524d\u4ec5\u5f00\u653e\u7533\u8bf7\u4eba\u8d26\u53f7\u81ea\u52a9\u6ce8\u518c\uff0cMO \u4e0e\u7ba1\u7406\u5458\u8d26\u53f7\u4ecd\u7531\u7cfb\u7edf\u7edf\u4e00\u7ba1\u7406\u3002";
    translations.en["register.submit"] = "Create Applicant Account";
    translations.zh["register.submit"] = "\u521b\u5efa\u7533\u8bf7\u4eba\u8d26\u53f7";
    translations.en["register.passwordMismatch"] = "Passwords do not match.";
    translations.zh["register.passwordMismatch"] = "\u4e24\u6b21\u8f93\u5165\u7684\u5bc6\u7801\u4e0d\u4e00\u81f4\u3002";
    translations.en["profile.registered"] = "Account created successfully. Complete your profile to improve matching.";
    translations.zh["profile.registered"] = "\u8d26\u53f7\u521b\u5efa\u6210\u529f\uff0c\u5efa\u8bae\u7ee7\u7eed\u5b8c\u5584\u8d44\u6599\u4ee5\u63d0\u5347\u5c97\u4f4d\u5339\u914d\u6548\u679c\u3002";
    translations.en["profile.desc"] = "Update your name, email, self introduction and skills for better matching.";
    translations.zh["profile.desc"] = "\u66f4\u65b0\u4f60\u7684\u59d3\u540d\u3001\u90ae\u7bb1\u3001\u81ea\u6211\u4ecb\u7ecd\u548c\u6280\u80fd\u4fe1\u606f\uff0c\u4ee5\u83b7\u5f97\u66f4\u597d\u7684\u5c97\u4f4d\u5339\u914d\u7ed3\u679c\u3002";
    translations.en["profile.selfIntroduction"] = "Self Introduction";
    translations.zh["profile.selfIntroduction"] = "\u81ea\u6211\u4ecb\u7ecd";
    translations.en["profile.selfIntroductionPlaceholder"] = "Share your background, strengths and TA-related experience";
    translations.zh["profile.selfIntroductionPlaceholder"] = "\u53ef\u4ee5\u7b80\u8981\u4ecb\u7ecd\u4f60\u7684\u80cc\u666f\u3001\u4f18\u52bf\u4ee5\u53ca\u4e0e TA \u76f8\u5173\u7684\u7ecf\u5386";
    translations.zh["profile.heading"] = "\u7533\u8bf7\u4eba\u8d44\u6599";
    translations.zh["profile.updated"] = "\u8d44\u6599\u66f4\u65b0\u6210\u529f\u3002";
    translations.zh["profile.fullName"] = "\u59d3\u540d";
    translations.zh["profile.email"] = "\u90ae\u7bb1";
    translations.zh["profile.skills"] = "\u6280\u80fd";
    translations.zh["profile.skillsPlaceholder"] = "Java, Excel, \u6c9f\u901a\u534f\u4f5c";
    translations.zh["profile.skillsHint"] = "\u8bf7\u7528\u82f1\u6587\u9017\u53f7\u5206\u9694\u6280\u80fd\uff0c\u4f8b\u5982\uff1aJava, Python, Communication";

    function getLanguage() {
        var stored = window.localStorage.getItem(STORAGE_KEY);
        if (stored === "zh" || stored === "en") {
            return stored;
        }
        return (window.navigator.language || "").toLowerCase().indexOf("zh") === 0 ? "zh" : "en";
    }

    function t(key, lang) {
        var locale = translations[lang] || translations.en;
        return locale[key] || translations.en[key] || key;
    }

    function translateTextNodes(lang) {
        var elements = document.querySelectorAll("[data-i18n]");
        for (var i = 0; i < elements.length; i++) {
            var key = elements[i].getAttribute("data-i18n");
            elements[i].textContent = t(key, lang);
        }
    }

    function translatePlaceholders(lang) {
        var fields = document.querySelectorAll("[data-i18n-placeholder]");
        for (var i = 0; i < fields.length; i++) {
            var key = fields[i].getAttribute("data-i18n-placeholder");
            fields[i].setAttribute("placeholder", t(key, lang));
        }
    }

    function translateStatuses(lang) {
        var statusElements = document.querySelectorAll("[data-status-label]");
        for (var i = 0; i < statusElements.length; i++) {
            var status = statusElements[i].getAttribute("data-status-label");
            statusElements[i].textContent = t("status." + status, lang);
        }

        var roleElements = document.querySelectorAll("[data-role-label]");
        for (var j = 0; j < roleElements.length; j++) {
            var role = roleElements[j].getAttribute("data-role-label");
            roleElements[j].textContent = t("role." + role, lang);
        }

        var statusOptions = document.querySelectorAll("[data-status-option]");
        for (var k = 0; k < statusOptions.length; k++) {
            var optionStatus = statusOptions[k].getAttribute("data-status-option");
            statusOptions[k].textContent = t("status." + optionStatus, lang);
        }
    }

    function updateDocumentTitle(lang) {
        var titleKey = document.body.getAttribute("data-page-title-key");
        if (!titleKey) {
            return;
        }
        document.title = t(titleKey, lang) + " - " + t("brand.title", lang);
    }

    function updateToggleLabels(lang) {
        var nextLabel = lang === "zh" ? "EN" : "中文";
        var toggles = document.querySelectorAll("[data-lang-toggle]");
        for (var i = 0; i < toggles.length; i++) {
            toggles[i].textContent = nextLabel;
        }
    }

    function translatePage(lang) {
        document.documentElement.setAttribute("lang", lang === "zh" ? "zh-CN" : "en");
        translateTextNodes(lang);
        translatePlaceholders(lang);
        translateStatuses(lang);
        updateDocumentTitle(lang);
        updateToggleLabels(lang);
        document.body.setAttribute("data-language", lang);
    }

    function getSizingText(element, lang) {
        if (element.hasAttribute("data-i18n")) {
            return t(element.getAttribute("data-i18n"), lang);
        }

        if (element.hasAttribute("data-status-label")) {
            return t("status." + element.getAttribute("data-status-label"), lang);
        }

        if (element.hasAttribute("data-role-label")) {
            return t("role." + element.getAttribute("data-role-label"), lang);
        }

        if (element.hasAttribute("data-lang-toggle")) {
            return lang === "zh" ? "EN" : "\u4e2d\u6587";
        }

        return null;
    }

    function shouldLockWidth(element) {
        var display = window.getComputedStyle(element).display;
        return element.matches(".btn, .nav-link, .lang-switch, .role-pill, .badge, label, .workspace-kicker, .account-label, .sidebar-eyebrow") ||
            display === "inline" ||
            display === "inline-block" ||
            display === "inline-flex";
    }

    function resetStableTranslationSizing(targets) {
        for (var i = 0; i < targets.length; i++) {
            targets[i].style.minWidth = "";
            targets[i].style.minHeight = "";
            if (targets[i].getAttribute("data-inline-lock") === "1") {
                targets[i].style.display = "";
                targets[i].removeAttribute("data-inline-lock");
            }
        }
    }

    function applyStableTranslationSizing() {
        var targets = document.querySelectorAll("[data-i18n], [data-status-label], [data-role-label], [data-lang-toggle]");
        resetStableTranslationSizing(targets);

        for (var i = 0; i < targets.length; i++) {
            var target = targets[i];
            var englishText = getSizingText(target, "en");
            var chineseText = getSizingText(target, "zh");

            if (!englishText || !chineseText || englishText === chineseText) {
                continue;
            }

            var lockWidth = shouldLockWidth(target);
            var computedDisplay = window.getComputedStyle(target).display;
            if (lockWidth && computedDisplay === "inline") {
                target.style.display = "inline-block";
                target.setAttribute("data-inline-lock", "1");
            }

            var originalText = target.textContent;
            var maxWidth = 0;
            var maxHeight = 0;
            var variants = [englishText, chineseText];

            for (var j = 0; j < variants.length; j++) {
                target.textContent = variants[j];
                maxWidth = Math.max(maxWidth, Math.ceil(target.getBoundingClientRect().width));
                maxHeight = Math.max(maxHeight, Math.ceil(target.getBoundingClientRect().height));
            }

            target.textContent = originalText;

            if (lockWidth && maxWidth > 0) {
                target.style.minWidth = maxWidth + "px";
            }

            if (maxHeight > 0) {
                target.style.minHeight = maxHeight + "px";
            }
        }
    }

    function setupStableTranslationSizing() {
        applyStableTranslationSizing();

        var resizeTimer = null;
        window.addEventListener("resize", function () {
            window.clearTimeout(resizeTimer);
            resizeTimer = window.setTimeout(applyStableTranslationSizing, 80);
        });
    }

    function resolveMessage(form, lang) {
        var key = form.getAttribute("data-confirm-key");
        if (key) {
            return t(key, lang);
        }
        return form.getAttribute("data-confirm");
    }

    function setupConfirmForms() {
        var forms = document.querySelectorAll("form[data-confirm], form[data-confirm-key]");
        for (var i = 0; i < forms.length; i++) {
            forms[i].addEventListener("submit", function (event) {
                var message = resolveMessage(this, getLanguage());
                if (message && !window.confirm(message)) {
                    event.preventDefault();
                }
            });
        }
    }

    function setupLanguageToggle() {
        var toggles = document.querySelectorAll("[data-lang-toggle]");
        for (var i = 0; i < toggles.length; i++) {
            toggles[i].addEventListener("click", function () {
                var next = getLanguage() === "zh" ? "en" : "zh";
                window.localStorage.setItem(STORAGE_KEY, next);
                translatePage(next);
                applyStableTranslationSizing();
            });
        }
    }

    function applyAuthMode(mode) {
        var tabs = document.querySelectorAll("[data-auth-tab]");
        var panels = document.querySelectorAll("[data-auth-panel]");

        if (!tabs.length || !panels.length) {
            return;
        }

        var activeMode = mode === "register" ? "register" : "login";
        document.body.setAttribute("data-auth-mode", activeMode);

        for (var i = 0; i < tabs.length; i++) {
            var isActiveTab = tabs[i].getAttribute("data-auth-tab") === activeMode;
            tabs[i].classList.toggle("is-active", isActiveTab);
            tabs[i].setAttribute("aria-selected", isActiveTab ? "true" : "false");
        }

        for (var j = 0; j < panels.length; j++) {
            var isActivePanel = panels[j].getAttribute("data-auth-panel") === activeMode;
            panels[j].hidden = !isActivePanel;
        }
    }

    function setupAuthMode() {
        var tabs = document.querySelectorAll("[data-auth-tab]");
        if (!tabs.length) {
            return;
        }

        applyAuthMode(document.body.getAttribute("data-auth-mode"));

        for (var i = 0; i < tabs.length; i++) {
            tabs[i].addEventListener("click", function () {
                applyAuthMode(this.getAttribute("data-auth-tab"));
            });
        }
    }

    function setupRegisterValidation() {
        var passwordField = document.getElementById("register-password");
        var confirmField = document.querySelector("[data-register-confirm]");

        if (!passwordField || !confirmField) {
            return;
        }

        function syncMessage() {
            if (!confirmField.value || passwordField.value === confirmField.value) {
                confirmField.setCustomValidity("");
                return;
            }
            confirmField.setCustomValidity(t("register.passwordMismatch", getLanguage()));
        }

        passwordField.addEventListener("input", syncMessage);
        confirmField.addEventListener("input", syncMessage);
    }

    function setupSidebar() {
        var toggle = document.querySelector("[data-sidebar-toggle]");
        var close = document.querySelector("[data-sidebar-close]");
        var navLinks = document.querySelectorAll(".nav-link");

        if (toggle) {
            toggle.addEventListener("click", function () {
                document.body.classList.toggle("sidebar-open");
            });
        }

        if (close) {
            close.addEventListener("click", function () {
                document.body.classList.remove("sidebar-open");
            });
        }

        for (var i = 0; i < navLinks.length; i++) {
            navLinks[i].addEventListener("click", function () {
                document.body.classList.remove("sidebar-open");
            });
        }
    }

    function setupActiveNav() {
        var contextPath = document.body.getAttribute("data-context-path") || "";
        var currentPath = window.location.pathname;

        if (contextPath && currentPath.indexOf(contextPath) === 0) {
            currentPath = currentPath.substring(contextPath.length);
        }

        var links = document.querySelectorAll("[data-nav-match]");
        var bestLink = null;
        var bestLength = -1;

        for (var i = 0; i < links.length; i++) {
            var patterns = links[i].getAttribute("data-nav-match").split(",");
            for (var j = 0; j < patterns.length; j++) {
                var pattern = patterns[j].trim();
                var isMatch = currentPath === pattern || currentPath.indexOf(pattern + "/") === 0;
                if (isMatch && pattern.length > bestLength) {
                    bestLink = links[i];
                    bestLength = pattern.length;
                }
            }
        }

        if (bestLink) {
            bestLink.classList.add("is-active");
        }
    }

    /**
     * CV upload client-side validation.
     * Validates file size (≤ 2 MB) and MIME type / extension before the form
     * is submitted, giving the user immediate feedback without a server round-trip.
     */
    function setupCvUploadValidation() {
        var MAX_SIZE_BYTES = 2 * 1024 * 1024; // 2 MB
        var ALLOWED_EXTENSIONS = ["pdf", "doc", "docx"];
        var ALLOWED_MIME = ["application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"];

        var form = document.querySelector("form.cv-upload-form");
        var fileInput = document.getElementById("cvFile");
        var errorContainer = document.querySelector(".cv-client-error");

        if (!form || !fileInput) {
            return;
        }

        // Create inline error element if not already present in the DOM
        if (!errorContainer) {
            errorContainer = document.createElement("p");
            errorContainer.className = "cv-client-error alert alert-error";
            errorContainer.style.display = "none";
            form.insertBefore(errorContainer, form.firstChild);
        }

        function showError(msg) {
            errorContainer.textContent = msg;
            errorContainer.style.display = "";
            fileInput.focus();
        }

        function clearError() {
            errorContainer.textContent = "";
            errorContainer.style.display = "none";
        }

        // Live feedback when user picks a file
        fileInput.addEventListener("change", function () {
            clearError();
            var files = this.files;
            if (!files || files.length === 0) {
                return;
            }
            var file = files[0];
            var lang = getLanguage();

            // Extension check
            var nameParts = file.name.split(".");
            var ext = nameParts.length > 1 ? nameParts[nameParts.length - 1].toLowerCase() : "";
            if (ALLOWED_EXTENSIONS.indexOf(ext) === -1) {
                showError(t("cv.error.type", lang));
                return;
            }

            // Size check
            if (file.size > MAX_SIZE_BYTES) {
                showError(t("cv.error.sizeClient", lang));
            }
        });

        // Block submit if validation fails
        form.addEventListener("submit", function (e) {
            clearError();
            var files = fileInput.files;
            var lang = getLanguage();

            if (!files || files.length === 0) {
                // Let the server-side empty check handle this (form action=upload).
                return;
            }

            var file = files[0];

            // Extension check
            var nameParts = file.name.split(".");
            var ext = nameParts.length > 1 ? nameParts[nameParts.length - 1].toLowerCase() : "";
            if (ALLOWED_EXTENSIONS.indexOf(ext) === -1) {
                e.preventDefault();
                showError(t("cv.error.type", lang));
                return;
            }

            // MIME check (best-effort; may be empty on some browsers)
            if (file.type && ALLOWED_MIME.indexOf(file.type) === -1) {
                e.preventDefault();
                showError(t("cv.error.type", lang));
                return;
            }

            // Size check
            if (file.size > MAX_SIZE_BYTES) {
                e.preventDefault();
                showError(t("cv.error.sizeClient", lang));
            }
        });
    }

    /**
     * AI Loading Overlay
     *
     * - Injects a full-screen frosted-glass overlay into every page.
     * - Any link/button with [data-ai-link] shows the overlay on click,
     *   then navigates normally (the server does the heavy AI work).
     * - skill-recommend pages hide the overlay immediately on load
     *   because rendering is already complete by the time the browser
     *   receives the response.
     */
    function setupAiLoadingOverlay() {
        var lang = getLanguage();

        // Build overlay DOM
        var overlay = document.createElement("div");
        overlay.className = "ai-loading-overlay";
        overlay.setAttribute("aria-live", "polite");
        overlay.setAttribute("aria-label", lang === "zh" ? "AI 正在思考中" : "AI is thinking");
        overlay.innerHTML =
            '<div class="ai-loading-card">' +
              '<div class="ai-loading-spinner"></div>' +
              '<p class="ai-loading-title">' +
                (lang === "zh" ? "AI 正在生成学习路径…" : "AI is generating your learning path…") +
              '</p>' +
              '<p class="ai-loading-sub">' +
                (lang === "zh"
                  ? "大语言模型正在思考，通常需要 20–40 秒，请稍候。"
                  : "The language model is thinking. This usually takes 20–40 seconds.") +
              '</p>' +
            '</div>';
        document.body.appendChild(overlay);

        // Show overlay when any [data-ai-link] is clicked
        var aiLinks = document.querySelectorAll("[data-ai-link]");
        for (var i = 0; i < aiLinks.length; i++) {
            aiLinks[i].addEventListener("click", function () {
                overlay.classList.add("visible");
            });
        }

        // Hide overlay immediately if we are already on the result page
        // (skill-recommend page is fully rendered server-side)
        var path = window.location.pathname;
        if (path.indexOf("skill-recommend") !== -1) {
            overlay.classList.remove("visible");
        }
    }

    translatePage(getLanguage());
    setupLanguageToggle();
    setupAuthMode();
    setupRegisterValidation();
    setupConfirmForms();
    setupSidebar();
    setupActiveNav();
    setupStableTranslationSizing();
    setupCvUploadValidation();
    setupAiLoadingOverlay();
});
