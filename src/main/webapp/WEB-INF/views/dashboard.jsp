<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Dashboard" />
<c:set var="pageTitleKey" value="page.dashboard" />
<%@ include file="includes/header.jspf" %>

<section class="hero-card">
    <h2><span data-i18n="dashboard.welcome">Welcome,</span> ${sessionScope.currentUser.fullName}</h2>
    <p>
        <c:choose>
            <c:when test="${sessionScope.currentUser.role == 'APPLICANT'}">
                <span data-i18n="dashboard.applicantIntro">You can edit your profile, browse TA positions, and track your application status.</span>
            </c:when>
            <c:when test="${sessionScope.currentUser.role == 'MO'}">
                <span data-i18n="dashboard.moIntro">You can create TA jobs, review applicants, and update application results.</span>
            </c:when>
            <c:otherwise>
                <span data-i18n="dashboard.adminIntro">You can review the overall TA workload and quickly identify overloaded applicants.</span>
            </c:otherwise>
        </c:choose>
    </p>
</section>

<c:if test="${not empty notifications}">
    <div class="card notifications-card">
        <div class="card-header">
            <h3 data-i18n="dashboard.notifications">Notifications</h3>
            <c:if test="${unreadCount > 0}">
                <span class="badge badge-notification">${unreadCount}</span>
            </c:if>
        </div>
        <div class="notifications-list">
            <c:forEach items="${notifications}" var="notification">
                <div class="notification-item ${notification.read ? 'read' : 'unread'}">
                    <div class="notification-content">
                        <span class="notification-type" data-type="${notification.type}">
                            <c:if test="${notification.type == 'APPLICATION_STATUS'}">
                                <span data-i18n="notification.type.applicationStatus">Application Status</span>
                            </c:if>
                            <c:if test="${notification.type == 'NEW_APPLICATION'}">
                                <span data-i18n="notification.type.newApplication">New Application</span>
                            </c:if>
                        </span>
                        <p>${notification.message}</p>
                        <span class="notification-time">${notification.createdAt}</span>
                    </div>
                    <form action="${pageContext.request.contextPath}/notification/delete" method="post" class="delete-notification-form">
                        <input type="hidden" name="notificationId" value="${notification.id}">
                        <button type="submit" class="delete-notification-btn" title="Delete notification">
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <line x1="18" y1="6" x2="6" y2="18"></line>
                                <line x1="6" y1="6" x2="18" y2="18"></line>
                            </svg>
                        </button>
                    </form>
                </div>
            </c:forEach>
        </div>
    </div>
</c:if>

<div class="grid-two">
    <c:if test="${sessionScope.currentUser.role == 'APPLICANT'}">
        <div class="card">
            <h3 data-i18n="dashboard.applicantPanel">Applicant Panel</h3>
            <p><span data-i18n="dashboard.openJobs">Open Jobs</span>: <strong>${openJobCount}</strong></p>
            <p><span data-i18n="dashboard.myApplications">My Applications</span>: <strong>${myApplicationCount}</strong></p>
            <a class="btn btn-primary" data-i18n="action.browseJobs" href="${pageContext.request.contextPath}/jobs">Browse Jobs</a>
        </div>
        <div class="card">
            <h3 data-i18n="dashboard.profileTitle">Profile</h3>
            <p data-i18n="dashboard.profileDesc">Keep your skills updated so the system can compute a clearer match score.</p>
            <a class="btn btn-secondary" data-i18n="action.editProfile" href="${pageContext.request.contextPath}/applicant/profile">Edit Profile</a>
        </div>
    </c:if>

    <c:if test="${sessionScope.currentUser.role == 'MO'}">
        <div class="card">
            <h3 data-i18n="dashboard.moPanel">MO Panel</h3>
            <p><span data-i18n="dashboard.myPostedJobs">My Posted Jobs</span>: <strong>${myJobCount}</strong></p>
            <a class="btn btn-primary" data-i18n="action.viewMyJobs" href="${pageContext.request.contextPath}/mo/jobs">View My Jobs</a>
        </div>
        <div class="card">
            <h3 data-i18n="dashboard.newJobTitle">Create New Job</h3>
            <p data-i18n="dashboard.newJobDesc">Post a new TA position and start collecting applications.</p>
            <a class="btn btn-secondary" data-i18n="action.createJob" href="${pageContext.request.contextPath}/mo/jobs/create">Create Job</a>
        </div>
    </c:if>

    <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
        <div class="card">
            <h3 data-i18n="dashboard.adminPanel">Admin Panel</h3>
            <p><span data-i18n="dashboard.applicantsInSystem">Applicants in System</span>: <strong>${summaryCount}</strong></p>
            <a class="btn btn-primary" data-i18n="action.viewWorkload" href="${pageContext.request.contextPath}/admin/workload">View Workload</a>
        </div>
        <div class="card">
            <h3 data-i18n="dashboard.userManagementTitle">User Management</h3>
            <p data-i18n="dashboard.userManagementDesc">Create, view, and manage all user accounts in the system.</p>
            <a class="btn btn-secondary" data-i18n="action.manageUsers" href="${pageContext.request.contextPath}/admin/users">Manage Users</a>
        </div>
    </c:if>
</div>

<style>
.notifications-card {
    margin-bottom: 20px;
}

.notifications-card .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
}

.notifications-list {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.notification-item {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    padding: 15px;
    background-color: #f8f9fa;
    border-radius: 8px;
    border-left: 4px solid #007bff;
}

.notification-item.unread {
    background-color: #e3f2fd;
    border-left-color: #28a745;
}

.notification-item.read {
    background-color: #f8f9fa;
    border-left-color: #6c757d;
}

.notification-content {
    flex: 1;
    padding-right: 15px;
}

.notification-type {
    font-size: 12px;
    font-weight: bold;
    color: #007bff;
    text-transform: uppercase;
    letter-spacing: 0.5px;
}

.notification-item p {
    margin: 8px 0;
    color: #333;
    font-size: 14px;
    line-height: 1.5;
}

.notification-time {
    font-size: 12px;
    color: #6c757d;
}

.delete-notification-form {
    margin: 0;
}

.delete-notification-btn {
    background: none;
    border: none;
    font-size: 24px;
    color: #999;
    cursor: pointer;
    padding: 0 8px;
    line-height: 1;
    transition: color 0.2s;
}

.delete-notification-btn:hover {
    color: #dc3545;
}

.badge-notification {
    background-color: #dc3545;
    color: white;
    font-size: 12px;
    padding: 2px 8px;
    border-radius: 10px;
    font-weight: bold;
}
</style>

<%-- Recent Activity Card --%>
<div class="card activity-card">
    <div class="card-header">
        <h3 data-i18n="dashboard.recentActivity">Recent Activity</h3>
        <a href="${pageContext.request.contextPath}/activity/my" class="view-all-link" data-i18n="action.viewAll">View All</a>
    </div>
    <c:choose>
        <c:when test="${not empty recentActivities}">
            <div class="activity-list">
                <c:forEach items="${recentActivities}" var="log">
                    <div class="activity-item">
                        <div class="activity-item-left">
                            <span class="activity-type-badge activity-type-${log.actionType}">${log.actionType}</span>
                            <span class="activity-desc">${log.description}</span>
                        </div>
                        <div class="activity-item-right">
                            <c:if test="${not empty log.beforeState and not empty log.afterState}">
                                <span class="state-before">${log.beforeState}</span>
                                <span class="state-arrow">→</span>
                                <span class="state-after">${log.afterState}</span>
                            </c:if>
                            <span class="activity-time">${log.formattedCreatedAt}</span>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <p class="activity-empty" data-i18n="activity.dashboardEmpty">No recent activity. Start by browsing jobs or managing your postings.</p>
        </c:otherwise>
    </c:choose>
</div>

<style>
.activity-card {
    margin-top: 20px;
}
.activity-card .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 14px;
}
.view-all-link {
    font-size: 13px;
    color: #007bff;
    text-decoration: none;
}
.view-all-link:hover {
    text-decoration: underline;
}
.activity-list {
    display: flex;
    flex-direction: column;
    gap: 10px;
}
.activity-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 14px;
    background-color: #f8f9fa;
    border-radius: 6px;
    gap: 12px;
    flex-wrap: wrap;
}
.activity-item-left {
    display: flex;
    align-items: center;
    gap: 10px;
    flex: 1;
    min-width: 0;
}
.activity-item-right {
    display: flex;
    align-items: center;
    gap: 6px;
    white-space: nowrap;
    flex-shrink: 0;
}
.activity-desc {
    font-size: 14px;
    color: #333;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
.activity-time {
    font-size: 12px;
    color: #999;
}
.activity-empty {
    color: #999;
    font-size: 14px;
    text-align: center;
    padding: 20px 0;
}
.activity-type-badge {
    display: inline-block;
    padding: 3px 8px;
    border-radius: 4px;
    font-size: 11px;
    font-weight: 600;
    letter-spacing: 0.3px;
    background-color: #e9ecef;
    color: #495057;
    white-space: nowrap;
    flex-shrink: 0;
}
.activity-type-APPLY_JOB             { background-color: #d1ecf1; color: #0c5460; }
.activity-type-WITHDRAW_APPLICATION  { background-color: #fff3cd; color: #856404; }
.activity-type-CREATE_JOB            { background-color: #d4edda; color: #155724; }
.activity-type-COMPLETE_JOB          { background-color: #cce5ff; color: #004085; }
.activity-type-REOPEN_JOB            { background-color: #d4edda; color: #155724; }
.activity-type-UPDATE_APPLICATION_STATUS { background-color: #e2d9f3; color: #4a235a; }
.activity-type-CREATE_USER           { background-color: #d4edda; color: #155724; }
.activity-type-DELETE_USER           { background-color: #f8d7da; color: #721c24; }
.state-before {
    display: inline-block;
    padding: 2px 6px;
    border-radius: 4px;
    background-color: #f8d7da;
    color: #721c24;
    font-size: 12px;
}
.state-arrow {
    color: #888;
    font-size: 12px;
}
.state-after {
    display: inline-block;
    padding: 2px 6px;
    border-radius: 4px;
    background-color: #d4edda;
    color: #155724;
    font-size: 12px;
}
</style>

<%@ include file="includes/footer.jspf" %>
