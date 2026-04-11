<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="My Applications" />
<c:set var="pageTitleKey" value="page.myApplications" />
<%@ include file="includes/header.jspf" %>

<section class="section-header">
    <h2 data-i18n="applications.heading">My Application Records</h2>
    <p data-i18n="applications.desc">Track the latest result of each job application.</p>
</section>

<div class="table-card">
    <table>
        <thead>
        <tr>
            <th data-i18n="common.jobTitle">Job Title</th>
            <th data-i18n="common.module">Module</th>
            <th data-i18n="common.status">Status</th>
            <th data-i18n="applications.appliedAt">Applied At</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${applicationDisplays}" var="item">
            <tr>
                <td>${item.job.title}</td>
                <td>${item.job.moduleName}</td>
                <td>
                    <span class="badge badge-${item.application.status}" data-status-label="${item.application.status}">${item.application.status}</span>
                </td>
                <td>${item.application.appliedAt}</td>
            </tr>
        </c:forEach>
        <c:if test="${empty applicationDisplays}">
            <tr>
                <td colspan="4" class="empty-row" data-i18n="applications.empty">You have not submitted any applications yet.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="includes/footer.jspf" %>
