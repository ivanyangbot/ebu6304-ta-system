<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="My Jobs" />
<c:set var="pageTitleKey" value="page.myJobs" />
<%@ include file="includes/header.jspf" %>

<section class="section-header">
    <h2 data-i18n="moJobs.heading">My Posted Jobs</h2>
    <p data-i18n="moJobs.desc">Manage the jobs you have created and review all applicants.</p>
</section>

<c:if test="${param.msg == 'created'}">
    <div class="alert alert-success" data-i18n="moJobs.created">Job created successfully.</div>
</c:if>

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
                    <a class="btn btn-secondary btn-small" data-i18n="action.viewApplications"
                       href="${pageContext.request.contextPath}/mo/applications?jobId=${job.id}">View Applications</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty jobs}">
            <tr>
                <td colspan="6" class="empty-row" data-i18n="moJobs.empty">You have not created any jobs yet.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="includes/footer.jspf" %>
