<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Workload Overview" />
<c:set var="pageTitleKey" value="page.workload" />
<%@ include file="includes/header.jspf" %>

<section class="section-header">
    <h2 data-i18n="admin.heading">Applicant Workload Overview</h2>
    <p><span data-i18n="admin.thresholdPrefix">Applicants above</span> ${threshold} <span data-i18n="admin.thresholdSuffix">accepted hours are marked as overloaded.</span></p>
</section>

<div class="table-card">
    <table>
        <thead>
        <tr>
            <th data-i18n="admin.applicantName">Applicant Name</th>
            <th data-i18n="profile.email">Email</th>
            <th data-i18n="admin.acceptedJobsCount">Accepted Jobs Count</th>
            <th data-i18n="admin.totalHours">Total Hours</th>
            <th data-i18n="admin.workloadStatus">Workload Status</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${workloadSummaries}" var="item">
            <tr>
                <td>${item.applicant.fullName}</td>
                <td>${item.applicant.email}</td>
                <td>${item.acceptedJobsCount}</td>
                <td>${item.totalHours}</td>
                <td>
                    <span class="badge badge-${item.workloadStatus}" data-status-label="${item.workloadStatus}">${item.workloadStatus}</span>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty workloadSummaries}">
            <tr>
                <td colspan="5" class="empty-row" data-i18n="admin.empty">No applicant workload data found.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="includes/footer.jspf" %>
