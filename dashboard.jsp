<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
            <c:choose>
                <c:when test="${not empty applicantProfile}">
                    <p><span data-i18n="profile.fullName">Full Name</span>: <strong>${applicantProfile.fullName}</strong></p>
                    <p><span data-i18n="profile.email">Email</span>: <strong>${applicantProfile.email}</strong></p>
                    <p><span data-i18n="profile.skills">Skills</span>: <strong>${fn:length(applicantProfile.skills)}</strong></p>
                    <p>
                        <span data-i18n="profile.selfIntroduction">Self Introduction</span>:
                        <strong>
                            <c:choose>
                                <c:when test="${empty applicantProfile.selfIntroduction}">
                                    <span data-i18n="dashboard.profileIncomplete">Incomplete</span>
                                </c:when>
                                <c:otherwise>
                                    <span data-i18n="dashboard.profileCompleted">Completed</span>
                                </c:otherwise>
                            </c:choose>
                        </strong>
                    </p>
                </c:when>
                <c:otherwise>
                    <p data-i18n="dashboard.profileDesc">Keep your skills updated so the system can compute a clearer match score.</p>
                </c:otherwise>
            </c:choose>
            <a class="btn btn-secondary" data-i18n="action.editProfile" href="${pageContext.request.contextPath}/applicant/profile">Edit Profile</a>
        </div>
    </c:if>

    <c:if test="${sessionScope.currentUser.role == 'MO'}">
        <div class="card">
            <h3 data-i18n="dashboard.moPanel">MO Panel</h3>
            <p><span data-i18n="dashboard.myPostedJobs">My Posted Jobs</span>: <strong>${myJobCount}</strong></p>
            <p><span data-i18n="dashboard.pendingApplications">Pending Applications</span>: <strong>${pendingApplicationCount}</strong></p>
            <p><span data-i18n="dashboard.jobsNeedingAction">Jobs Needing Action</span>: <strong>${jobsNeedingAction}</strong></p>
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
            <p><span data-i18n="dashboard.totalUsers">Total Users</span>: <strong>${totalUserCount}</strong></p>
            <p><span data-i18n="dashboard.totalApplicants">Applicants</span>: <strong>${applicantCount}</strong></p>
            <p><span data-i18n="dashboard.totalMOs">Module Organisers</span>: <strong>${moCount}</strong></p>
            <p><span data-i18n="dashboard.openJobs">Open Jobs</span>: <strong>${openJobCount}</strong></p>
            <p><span data-i18n="dashboard.pendingApplications">Pending Applications</span>: <strong>${pendingApplicationCount}</strong></p>
            <p><span data-i18n="dashboard.acceptanceRate">Acceptance Rate</span>: <strong>${acceptanceRate}%</strong></p>
            <p><span data-i18n="dashboard.overloadedTAs">Overloaded TAs</span>: <strong>${overloadedCount}</strong></p>
            <a class="btn btn-primary" data-i18n="action.viewWorkload" href="${pageContext.request.contextPath}/admin/workload">View Workload</a>
        </div>
        <div class="card">
            <h3 data-i18n="dashboard.userManagementTitle">User Management</h3>
            <p data-i18n="dashboard.userManagementDesc">Create, view, and manage all user accounts in the system.</p>
            <a class="btn btn-secondary" data-i18n="action.manageUsers" href="${pageContext.request.contextPath}/admin/users">Manage Users</a>
        </div>
    </c:if>
</div>

<c:if test="${sessionScope.currentUser.role == 'MO'}">
    <div class="grid-two dashboard-extra-grid">
        <div class="card">
            <h3 data-i18n="dashboard.recentApplications">Recent Pending Applications</h3>
            <c:choose>
                <c:when test="${not empty recentPendingApplications}">
                    <div class="dashboard-list">
                        <c:forEach items="${recentPendingApplications}" var="item">
                            <div class="dashboard-list-item">
                                <div>
                                    <strong>${item.applicantName}</strong>
                                    <span class="hint"> · ${item.jobTitle}</span>
                                </div>
                                <div class="dashboard-list-meta">
                                    <span>${item.appliedAt}</span>
                                    <a class="btn btn-secondary btn-sm"
                                       href="${pageContext.request.contextPath}/mo/applications?jobId=${item.jobId}"
                                       data-i18n="action.reviewApplications">Review</a>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="hint" data-i18n="dashboard.noRecentApplications">No pending applications yet.</p>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="card">
            <h3 data-i18n="dashboard.actionRequiredJobs">Jobs Needing Action</h3>
            <c:choose>
                <c:when test="${not empty actionRequiredJobs}">
                    <div class="dashboard-list">
                        <c:forEach items="${actionRequiredJobs}" var="item">
                            <div class="dashboard-list-item">
                                <div>
                                    <strong>${item.jobTitle}</strong>
                                    <span class="hint"> · <span data-i18n="dashboard.pendingCount">Pending</span>: ${item.pendingCount}</span>
                                </div>
                                <a class="btn btn-primary btn-sm"
                                   href="${pageContext.request.contextPath}/mo/applications?jobId=${item.jobId}"
                                   data-i18n="action.reviewApplications">Review</a>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="hint" data-i18n="dashboard.noActionRequiredJobs">All open jobs are up to date.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</c:if>

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

<%@ include file="includes/footer.jspf" %>
