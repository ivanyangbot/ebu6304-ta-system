<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Browse Jobs" />
<c:set var="pageTitleKey" value="page.jobs" />
<%@ include file="includes/header.jspf" %>

<section class="section-header">
    <h2 data-i18n="jobList.heading">Available TA Jobs</h2>
    <p data-i18n="jobList.desc">Browse all open positions and view the required skills before applying.</p>
</section>

<div class="table-card">
    <table>
        <thead>
        <tr>
            <th data-i18n="common.title">Title</th>
            <th data-i18n="common.module">Module</th>
            <th data-i18n="common.hours">Hours</th>
            <th data-i18n="common.requiredSkills">Required Skills</th>
            <th data-i18n="common.status">Status</th>
            <th data-i18n="jobList.deadline">Deadline</th>
            <th data-i18n="common.action">Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${jobs}" var="job">
            <tr>
                <td>${job.title}</td>
                <td>${job.moduleName}</td>
                <td>${job.hours}</td>
                <td>
                    <c:forEach items="${job.requiredSkills}" var="skill" varStatus="status">
                        ${skill}<c:if test="${not status.last}">, </c:if>
                    </c:forEach>
                </td>
                <td><span class="badge badge-${job.status}" data-status-label="${job.status}">${job.status}</span></td>
                <td>
                    <c:choose>
                        <c:when test="${job.deadline == null}"><span style="color:#adb5bd;">—</span></c:when>
                        <c:when test="${job.deadlinePassed}"><span style="color:#dc3545;font-size:12px;">${job.deadline} <strong>Closed</strong></span></c:when>
                        <c:when test="${job.daysUntilDeadline <= 3}"><span style="color:#fd7e14;font-size:12px;">${job.deadline} (${job.daysUntilDeadline}d left)</span></c:when>
                        <c:otherwise><span style="font-size:12px;">${job.deadline}</span></c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <a class="btn btn-secondary btn-small" data-i18n="action.viewDetail"
                       href="${pageContext.request.contextPath}/jobs/detail?id=${job.id}">View Detail</a>
                    <c:if test="${sessionScope.currentUser.role == 'APPLICANT' and not empty job.requiredSkills}">
                        <a class="btn btn-light btn-small" data-i18n="nav.aiSkillPath"
                           href="${pageContext.request.contextPath}/applicant/skill-recommend?jobId=${job.id}"
                           style="display:inline-flex;align-items:center;gap:5px;">
                            <svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><rect x="3" y="11" width="18" height="10" rx="2"/><circle cx="9" cy="16" r="1.5" fill="currentColor" stroke="none"/><circle cx="15" cy="16" r="1.5" fill="currentColor" stroke="none"/><path d="M12 11V7"/><circle cx="12" cy="5" r="2"/></svg>
                            AI Skill Path
                        </a>
                    </c:if>
                    <c:if test="${appliedJobMap[job.id]}">
                        <span class="badge badge-info" data-status-label="Applied">Applied</span>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty jobs}">
            <tr>
                <td colspan="7" class="empty-row" data-i18n="jobList.empty">No jobs are available right now.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="includes/footer.jspf" %>
