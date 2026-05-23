<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="All Applications" />
<c:set var="pageTitleKey" value="page.allApplications" />
<%@ include file="includes/header.jspf" %>

<section class="section-header">
    <h2 data-i18n="admin.allApplicationsHeading">All Applications</h2>
    <p data-i18n="admin.allApplicationsDesc">View all applications submitted by applicants across all jobs.</p>
</section>

<div class="table-card">
    <table>
        <thead>
        <tr>
            <th data-i18n="table.applicationId">Application ID</th>
            <th data-i18n="table.applicant">Applicant</th>
            <th data-i18n="table.job">Job</th>
            <th data-i18n="cv.heading" class="text-center">CV</th>
            <th data-i18n="table.status" class="text-center">Status</th>
            <th data-i18n="table.appliedAt">Applied At</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${applications}" var="item">
            <tr>
                <td>${item.application.id}</td>
                <td>
                    <c:choose>
                        <c:when test="${not empty item.applicant}">
                            <span>${item.applicant.fullName}</span>
                        </c:when>
                        <c:otherwise>
                            <span class="text-muted">Unknown Applicant</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${not empty item.job}">
                            <span>${item.job.title}</span>
                        </c:when>
                        <c:otherwise>
                            <span class="text-muted">Unknown Job</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="text-center cv-cell">
                    <c:choose>
                        <c:when test="${not empty item.applicant and item.applicant.hasCv()}">
                            <a class="btn btn-secondary btn-small"
                               href="${pageContext.request.contextPath}/cv/download?applicantId=${item.applicant.id}"
                               title="${item.applicant.cvFileName}"
                               data-i18n="cv.download">
                                📄 Download CV
                            </a>
                        </c:when>
                        <c:otherwise>
                            <span class="cv-none" data-i18n="cv.none">—</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="text-center">
                    <span class="badge badge-${item.application.status}" data-status-label="${item.application.status}">${item.application.status}</span>
                </td>
                <td>${item.application.appliedAt}</td>
            </tr>
        </c:forEach>
        <c:if test="${empty applications}">
            <tr>
                <td colspan="6" class="empty-row" data-i18n="admin.noApplications">No applications found.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>

<style>
.text-muted {
    color: #999;
}
.cv-cell { white-space: nowrap; }
.cv-none  { color: #bbb; font-size: 16px; }
</style>

<%@ include file="includes/footer.jspf" %>