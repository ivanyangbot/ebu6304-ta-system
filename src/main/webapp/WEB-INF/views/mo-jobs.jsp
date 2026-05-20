<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="My Jobs" />
<c:set var="pageTitleKey" value="page.myJobs" />
<%@ include file="includes/header.jspf" %>

<%
    // Generate CSRF token if not present
    if (session.getAttribute("csrfToken") == null) {
        session.setAttribute("csrfToken", java.util.UUID.randomUUID().toString());
    }
%>

<section class="section-header">
    <h2 data-i18n="moJobs.heading">My Posted Jobs</h2>
    <p data-i18n="moJobs.desc">Manage the jobs you have created and review all applicants.</p>
</section>

<c:if test="${not empty successMessage}">
    <div class="alert alert-success">${successMessage}</div>
</c:if>
<c:if test="${not empty errorMessage}">
    <div class="alert alert-error">${errorMessage}</div>
</c:if>

<c:if test="${param.msg == 'created'}">
    <div class="alert alert-success" data-i18n="moJobs.created">Job created successfully.</div>
</c:if>

<div class="card">
    <h3 data-i18n="moJobs.openJobs">Open Jobs</h3>
    <div class="table-card">
        <table>
            <thead>
            <tr>
                <th data-i18n="common.title">Title</th>
                <th data-i18n="common.module">Module</th>
                <th data-i18n="common.hours">Hours</th>
                <th data-i18n="common.requiredSkills">Required Skills</th>
                <th data-i18n="common.status">Status</th>
                <th data-i18n="common.action">Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${openJobs}" var="job">
                <tr>
                    <td>${job.title}</td>
                    <td>${job.moduleName}</td>
                    <td>${job.hours}</td>
                    <td>
                        <c:forEach items="${job.requiredSkills}" var="skill" varStatus="status">
                            ${skill}<c:if test="${not status.last}">, </c:if>
                        </c:forEach>
                    </td>
                    <td><span class="badge badge-success" data-i18n="job.status.open">Open</span></td>
                    <td>
                        <a class="btn btn-secondary btn-sm" data-i18n="action.viewApplications"
                           href="${pageContext.request.contextPath}/mo/applications?jobId=${job.id}">View Applications</a>
                        <form action="${pageContext.request.contextPath}/mo/jobs" method="post" class="complete-form" style="display: inline;">
                            <input type="hidden" name="action" value="complete">
                            <input type="hidden" name="jobId" value="${job.id}">
                            <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                            <button type="submit" class="btn btn-warning btn-sm complete-job-btn" data-i18n="action.markAsCompleted">Mark as Completed</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty openJobs}">
                <tr>
                    <td colspan="6" class="empty-row" data-i18n="moJobs.noOpenJobs">No open jobs found.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<div class="card">
    <h3 data-i18n="moJobs.completedJobs">Completed Jobs</h3>
    <div class="table-card">
        <table>
            <thead>
            <tr>
                <th data-i18n="common.title">Title</th>
                <th data-i18n="common.module">Module</th>
                <th data-i18n="common.hours">Hours</th>
                <th data-i18n="common.requiredSkills">Required Skills</th>
                <th data-i18n="common.status">Status</th>
                <th data-i18n="common.action">Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${completedJobs}" var="job">
                <tr>
                    <td>${job.title}</td>
                    <td>${job.moduleName}</td>
                    <td>${job.hours}</td>
                    <td>
                        <c:forEach items="${job.requiredSkills}" var="skill" varStatus="status">
                            ${skill}<c:if test="${not status.last}">, </c:if>
                        </c:forEach>
                    </td>
                    <td><span class="badge badge-secondary" data-i18n="job.status.completed">Completed</span></td>
                    <td>
                        <a class="btn btn-secondary btn-sm" data-i18n="action.viewApplications"
                           href="${pageContext.request.contextPath}/mo/applications?jobId=${job.id}">View Applications</a>
                        <form action="${pageContext.request.contextPath}/mo/jobs" method="post" class="reopen-form" style="display: inline;">
                            <input type="hidden" name="action" value="reopen">
                            <input type="hidden" name="jobId" value="${job.id}">
                            <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                            <button type="submit" class="btn btn-info btn-sm reopen-job-btn" data-i18n="action.reopen">Reopen</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty completedJobs}">
                <tr>
                    <td colspan="6" class="empty-row" data-i18n="moJobs.noCompletedJobs">No completed jobs found.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    var completeForms = document.querySelectorAll('.complete-form');
    completeForms.forEach(function(form) {
        form.addEventListener('submit', function(e) {
            var translations = {
                en: "Are you sure you want to mark this job as completed? This action cannot be undone.",
                zh: "确定要将此岗位标记为已完成招聘吗？此操作无法撤销。"
            };
            var lang = document.body.getAttribute('data-language') || 'en';
            var message = translations[lang] || translations.en;
            if (!confirm(message)) {
                e.preventDefault();
            }
        });
    });

    var reopenForms = document.querySelectorAll('.reopen-form');
    reopenForms.forEach(function(form) {
        form.addEventListener('submit', function(e) {
            var translations = {
                en: "Are you sure you want to reopen this job? It will be visible to applicants again.",
                zh: "确定要重新开放此岗位吗？岗位将再次对申请者可见。"
            };
            var lang = document.body.getAttribute('data-language') || 'en';
            var message = translations[lang] || translations.en;
            if (!confirm(message)) {
                e.preventDefault();
            }
        });
    });
});
</script>

<%@ include file="includes/footer.jspf" %>
