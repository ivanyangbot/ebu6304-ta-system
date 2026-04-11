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
            "page.workload": "Workload Overview",
            "page.error": "Error",
            "page.login": "Login",
            "nav.dashboard": "Dashboard",
            "nav.profile": "My Profile",
            "nav.jobs": "Browse Jobs",
            "nav.myApplications": "My Applications",
            "nav.moJobs": "My Jobs",
            "nav.createJob": "Create Job",
            "nav.workload": "Workload Overview",
            "action.logout": "Logout",
            "action.login": "Login",
            "action.save": "Save",
            "action.saveProfile": "Save Profile",
            "action.browseJobs": "Browse Jobs",
            "action.editProfile": "Edit Profile",
            "action.createJob": "Create Job",
            "action.viewMyJobs": "View My Jobs",
            "action.viewWorkload": "View Workload",
            "action.viewDetail": "View Detail",
            "action.viewApplications": "View Applications",
            "action.goBack": "Go Back",
            "action.returnDashboard": "Return to Dashboard",
            "action.applyNow": "Apply Now",
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
            "common.title": "Title",
            "common.jobTitle": "Job Title",
            "common.module": "Module",
            "common.description": "Description",
            "common.hours": "Hours",
            "common.requiredSkills": "Required Skills",
            "common.status": "Status",
            "common.action": "Action",
            "common.none": "None",
            "dashboard.welcome": "Welcome,",
            "dashboard.applicantIntro": "You can edit your profile, browse TA positions, and track your application status.",
            "dashboard.moIntro": "You can create TA jobs, review applicants, and update application results.",
            "dashboard.adminIntro": "You can review the overall TA workload and quickly identify overloaded applicants.",
            "dashboard.applicantPanel": "Applicant Panel",
            "dashboard.openJobs": "Open Jobs",
            "dashboard.myApplications": "My Applications",
            "dashboard.profileTitle": "Profile",
            "dashboard.profileDesc": "Keep your skills updated so the system can compute a clearer match score.",
            "dashboard.moPanel": "MO Panel",
            "dashboard.myPostedJobs": "My Posted Jobs",
            "dashboard.newJobTitle": "Create New Job",
            "dashboard.newJobDesc": "Post a new TA position and start collecting applications.",
            "dashboard.adminPanel": "Admin Panel",
            "dashboard.applicantsInSystem": "Applicants in System",
            "dashboard.workloadRuleTitle": "Workload Rule",
            "dashboard.workloadRuleDesc": "Applicants with accepted workload above 10 hours are marked as overloaded.",
            "jobList.heading": "Available TA Jobs",
            "jobList.desc": "Browse all open positions and view the required skills before applying.",
            "jobList.empty": "No jobs are available right now.",
            "jobDetail.matchTitle": "Match Result",
            "jobDetail.submitted": "Application submitted successfully.",
            "jobDetail.duplicate": "You have already applied for this job.",
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
            "applications.empty": "You have not submitted any applications yet.",
            "moJobs.heading": "My Posted Jobs",
            "moJobs.desc": "Manage the jobs you have created and review all applicants.",
            "moJobs.created": "Job created successfully.",
            "moJobs.empty": "You have not created any jobs yet.",
            "moApplications.heading": "Applications for",
            "moApplications.updated": "Application status updated successfully.",
            "moApplications.applicant": "Applicant",
            "moApplications.matchScore": "Match Score",
            "moApplications.missingSkills": "Missing Skills",
            "moApplications.update": "Update",
            "moApplications.empty": "No applicants yet for this job.",
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
            "confirm.updateApplication": "Update this application status?"
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
            "page.workload": "工作量总览",
            "page.error": "错误",
            "page.login": "登录",
            "nav.dashboard": "控制台",
            "nav.profile": "我的资料",
            "nav.jobs": "浏览岗位",
            "nav.myApplications": "我的申请",
            "nav.moJobs": "我的岗位",
            "nav.createJob": "创建岗位",
            "nav.workload": "工作量总览",
            "action.logout": "退出登录",
            "action.login": "登录",
            "action.save": "保存",
            "action.saveProfile": "保存资料",
            "action.browseJobs": "浏览岗位",
            "action.editProfile": "编辑资料",
            "action.createJob": "创建岗位",
            "action.viewMyJobs": "查看岗位",
            "action.viewWorkload": "查看总览",
            "action.viewDetail": "查看详情",
            "action.viewApplications": "查看申请",
            "action.goBack": "返回上一页",
            "action.returnDashboard": "返回控制台",
            "action.applyNow": "立即申请",
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
            "common.title": "标题",
            "common.jobTitle": "岗位名称",
            "common.module": "课程 / 模块",
            "common.description": "描述",
            "common.hours": "工时",
            "common.requiredSkills": "所需技能",
            "common.status": "状态",
            "common.action": "操作",
            "common.none": "无",
            "dashboard.welcome": "欢迎，",
            "dashboard.applicantIntro": "你可以维护个人资料、浏览助教岗位，并持续跟进申请状态。",
            "dashboard.moIntro": "你可以创建助教岗位、审核申请人，并更新申请结果。",
            "dashboard.adminIntro": "你可以查看整体助教工作量，并快速识别超负荷的申请人。",
            "dashboard.applicantPanel": "申请人面板",
            "dashboard.openJobs": "开放岗位数",
            "dashboard.myApplications": "我的申请数",
            "dashboard.profileTitle": "个人资料",
            "dashboard.profileDesc": "及时更新你的技能信息，系统才能给出更准确的匹配分数。",
            "dashboard.moPanel": "课程负责人面板",
            "dashboard.myPostedJobs": "已发布岗位数",
            "dashboard.newJobTitle": "创建新岗位",
            "dashboard.newJobDesc": "发布新的助教岗位并开始收集申请。",
            "dashboard.adminPanel": "管理员面板",
            "dashboard.applicantsInSystem": "系统申请人数",
            "dashboard.workloadRuleTitle": "工作量规则",
            "dashboard.workloadRuleDesc": "录用工时超过 10 小时的申请人会被标记为超负荷。",
            "jobList.heading": "当前开放的助教岗位",
            "jobList.desc": "浏览所有开放岗位，并在申请前查看所需技能。",
            "jobList.empty": "当前暂无可申请岗位。",
            "jobDetail.matchTitle": "匹配结果",
            "jobDetail.submitted": "申请已成功提交。",
            "jobDetail.duplicate": "你已经申请过这个岗位。",
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
            "applications.empty": "你还没有提交任何申请。",
            "moJobs.heading": "我发布的岗位",
            "moJobs.desc": "管理你创建的岗位，并查看所有申请人。",
            "moJobs.created": "岗位创建成功。",
            "moJobs.empty": "你还没有创建任何岗位。",
            "moApplications.heading": "岗位申请：",
            "moApplications.updated": "申请状态更新成功。",
            "moApplications.applicant": "申请人",
            "moApplications.matchScore": "匹配分数",
            "moApplications.missingSkills": "缺失技能",
            "moApplications.update": "更新",
            "moApplications.empty": "该岗位暂时还没有申请人。",
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
            "confirm.updateApplication": "确认更新该申请状态吗？"
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

    translatePage(getLanguage());
    setupLanguageToggle();
    setupAuthMode();
    setupRegisterValidation();
    setupConfirmForms();
    setupSidebar();
    setupActiveNav();
    setupStableTranslationSizing();
});
