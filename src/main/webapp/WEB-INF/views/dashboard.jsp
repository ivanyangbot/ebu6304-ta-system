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
            <h3 data-i18n="dashboard.workloadRuleTitle">Workload Rule</h3>
            <p data-i18n="dashboard.workloadRuleDesc">Applicants with accepted workload above 10 hours are marked as overloaded.</p>
        </div>
    </c:if>
</div>

<%@ include file="includes/footer.jspf" %>
