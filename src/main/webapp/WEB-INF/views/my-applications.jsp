<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="My Applications" />
<c:set var="pageTitleKey" value="page.myApplications" />
<%@ include file="includes/header.jspf" %>

<section class="section-header">
    <h2 data-i18n="applications.heading">My Application Records</h2>
    <p data-i18n="applications.desc">Track the latest result of each job application.</p>
</section>

<c:if test="${param.msg == 'withdrawn'}">
    <div class="alert alert-success" data-i18n="applications.withdrawnSuccess">Application withdrawn successfully.</div>
</c:if>
<c:if test="${param.msg == 'withdrawError'}">
    <div class="alert alert-error">${param.error}</div>
</c:if>

<div class="table-card">
    <table>
        <thead>
        <tr>
            <th data-i18n="common.jobTitle">Job Title</th>
            <th data-i18n="common.module">Module</th>
            <th data-i18n="common.status">Status</th>
            <th data-i18n="applications.appliedAt">Applied At</th>
            <th data-i18n="table.actions">Actions</th>
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
                <td>
                    <c:if test="${item.application.status == 'Pending'}">
                        <form action="${pageContext.request.contextPath}/applicant/withdraw" method="post" class="withdraw-form">
                            <input type="hidden" name="applicationId" value="${item.application.id}">
                            <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                            <button type="submit" class="btn btn-danger btn-sm" data-i18n="action.withdraw">Withdraw</button>
                        </form>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty applicationDisplays}">
            <tr>
                <td colspan="5" class="empty-row" data-i18n="applications.empty">You have not submitted any applications yet.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    var withdrawForms = document.querySelectorAll('.withdraw-form');
    withdrawForms.forEach(function(form) {
        form.addEventListener('submit', function(e) {
            var translations = {
                en: "Are you sure you want to withdraw this application? Withdrawn applications cannot be restored.",
                zh: "确定要撤回此申请吗？撤回后将无法恢复。"
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
