<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Deadline Monitor" />
<c:set var="pageTitleKey" value="page.deadlines" />
<%@ include file="includes/header.jspf" %>

<section class="section-header">
    <h2 data-i18n="deadlines.heading">Deadline Monitor</h2>
    <p><span data-i18n="deadlines.desc">Jobs expiring within</span> <strong>${lookaheadDays}</strong> <span data-i18n="deadlines.days">days, and jobs whose deadline has already passed.</span></p>
</section>

<%-- Expiring Soon --%>
<div class="card" style="margin-bottom:20px;">
    <h3 data-i18n="deadlines.expiringSoon">⏰ Expiring Soon (within ${lookaheadDays} days)</h3>
    <div class="table-card">
        <table>
            <thead>
            <tr>
                <th data-i18n="common.title">Title</th>
                <th data-i18n="common.module">Module</th>
                <th data-i18n="common.hours">Hours</th>
                <th data-i18n="deadlines.deadline">Deadline</th>
                <th data-i18n="deadlines.daysLeft">Days Left</th>
                <th data-i18n="common.postedBy">Posted By MO ID</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${expiringSoon}" var="job">
                <tr>
                    <td>${job.title}</td>
                    <td>${job.moduleName}</td>
                    <td>${job.hours}</td>
                    <td>${job.deadline}</td>
                    <td>
                        <c:choose>
                            <c:when test="${job.daysUntilDeadline == 0}">
                                <span class="badge" style="background:#fd7e14;color:#fff;" data-i18n="deadlines.today">Today</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge" style="background:#ffc107;color:#212529;">${job.daysUntilDeadline}</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${job.postedByMoId}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty expiringSoon}">
                <tr>
                    <td colspan="6" class="empty-row" data-i18n="deadlines.noneExpiring">No jobs expiring within this period.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<%-- Overdue (deadline passed, still Open) --%>
<div class="card">
    <h3 data-i18n="deadlines.overdue">🚨 Overdue — Deadline Passed (still Open)</h3>
    <p style="color:#6c757d;font-size:14px;" data-i18n="deadlines.overdueDesc">These jobs are still marked as Open but their application deadline has already passed. Consider marking them as Completed.</p>
    <div class="table-card">
        <table>
            <thead>
            <tr>
                <th data-i18n="common.title">Title</th>
                <th data-i18n="common.module">Module</th>
                <th data-i18n="common.hours">Hours</th>
                <th data-i18n="deadlines.deadline">Deadline</th>
                <th data-i18n="deadlines.daysOverdue">Days Overdue</th>
                <th data-i18n="common.postedBy">Posted By MO ID</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${overdueJobs}" var="job">
                <tr>
                    <td>${job.title}</td>
                    <td>${job.moduleName}</td>
                    <td>${job.hours}</td>
                    <td>${job.deadline}</td>
                    <td>
                        <span class="badge" style="background:#dc3545;color:#fff;">${-job.daysUntilDeadline}</span>
                    </td>
                    <td>${job.postedByMoId}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty overdueJobs}">
                <tr>
                    <td colspan="6" class="empty-row" data-i18n="deadlines.noneOverdue">No overdue jobs found.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="includes/footer.jspf" %>
