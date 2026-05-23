<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Job Detail" />
<c:set var="pageTitleKey" value="page.jobDetail" />
<%@ include file="includes/header.jspf" %>

<div class="grid-two">
    <section class="card">
        <h2>${job.title}</h2>
        <p><strong data-i18n="common.module">Module</strong>: ${job.moduleName}</p>
        <p><strong data-i18n="common.description">Description</strong>: ${job.description}</p>
        <p><strong data-i18n="common.requiredSkills">Required Skills</strong>:
            <c:forEach items="${job.requiredSkills}" var="skill" varStatus="status">
                ${skill}<c:if test="${not status.last}">, </c:if>
            </c:forEach>
        </p>
        <p><strong data-i18n="common.hours">Hours</strong>: ${job.hours}</p>
        <p><strong data-i18n="common.status">Status</strong>: <span class="badge badge-${job.status}" data-status-label="${job.status}">${job.status}</span></p>
        <c:if test="${job.deadline != null}">
            <p><strong data-i18n="jobDetail.deadline">Application Deadline</strong>:
                <span class="${deadlinePassed ? 'text-danger' : (job.daysUntilDeadline <= 3 ? 'text-warning' : '')}">
                    ${job.deadline}
                    <c:choose>
                        <c:when test="${deadlinePassed}"> &mdash; <span data-i18n="jobDetail.deadlinePassed" style="color:#dc3545;font-weight:bold;">Closed</span></c:when>
                        <c:when test="${job.daysUntilDeadline == 0}"> &mdash; <span style="color:#fd7e14;font-weight:bold;" data-i18n="jobDetail.deadlineToday">Closes today!</span></c:when>
                        <c:when test="${job.daysUntilDeadline > 0}"> &mdash; <span style="color:#fd7e14;">${job.daysUntilDeadline} <span data-i18n="jobDetail.daysLeft">days left</span></span></c:when>
                    </c:choose>
                </span>
            </p>
        </c:if>
    </section>

    <section class="card">
        <h2 data-i18n="jobDetail.matchTitle">Match Result</h2>
        <c:if test="${param.msg == 'applied'}">
            <div class="alert alert-success" data-i18n="jobDetail.submitted">Application submitted successfully.</div>
        </c:if>
        <c:if test="${param.msg == 'duplicate'}">
            <div class="alert alert-error" data-i18n="jobDetail.duplicate">You have already applied for this job.</div>
        </c:if>
        <c:if test="${param.msg == 'withdrawn'}">
            <div class="alert alert-success" data-i18n="jobDetail.withdrawn">Application withdrawn successfully.</div>
        </c:if>
        <c:if test="${param.msg == 'withdrawError'}">
            <div class="alert alert-error">${param.error}</div>
        </c:if>
        <c:if test="${param.msg == 'deadlinePassed'}">
            <div class="alert alert-error" data-i18n="jobDetail.deadlineExpired">The application deadline has passed. Your application was not submitted.</div>
        </c:if>

        <p><strong data-i18n="jobDetail.matchScore">Match Score</strong>: ${matchResult.score}%</p>
        <p><strong data-i18n="jobDetail.matchedSkills">Matched Skills</strong>:
            <c:choose>
                <c:when test="${empty matchResult.matchedSkills}"><span data-i18n="common.none">None</span></c:when>
                <c:otherwise>
                    <c:forEach items="${matchResult.matchedSkills}" var="skill" varStatus="status">
                        ${skill}<c:if test="${not status.last}">, </c:if>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </p>
        <p><strong data-i18n="jobDetail.missingSkills">Missing Skills</strong>:
            <c:choose>
                <c:when test="${empty matchResult.missingSkills}"><span data-i18n="common.none">None</span></c:when>
                <c:otherwise>
                    <c:forEach items="${matchResult.missingSkills}" var="skill" varStatus="status">
                        ${skill}<c:if test="${not status.last}">, </c:if>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </p>
        <c:if test="${not empty matchResult.missingSkills}">
            <a href="${pageContext.request.contextPath}/applicant/skill-recommend?jobId=${job.id}"
               class="btn btn-secondary" style="margin-top:6px; display:inline-flex; align-items:center; gap:7px;"
               data-i18n="action.aiLearningPath">
                <%-- robot SVG icon --%>
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                  <rect x="3" y="11" width="18" height="10" rx="2"/>
                  <circle cx="9" cy="16" r="1.5" fill="currentColor" stroke="none"/>
                  <circle cx="15" cy="16" r="1.5" fill="currentColor" stroke="none"/>
                  <path d="M12 11V7"/><circle cx="12" cy="5" r="2"/>
                </svg>
                Get AI Learning Path
            </a>
        </c:if>

        <c:choose>
            <c:when test="${alreadyApplied && currentApplication.status == 'Pending'}">
                <div class="action-buttons">
                    <span class="badge badge-info" data-status-label="AlreadyApplied">You already applied</span>
                    <form action="${pageContext.request.contextPath}/applicant/withdraw" method="post" class="withdraw-form">
                        <input type="hidden" name="applicationId" value="${currentApplication.id}">
                        <input type="hidden" name="jobId" value="${job.id}">
                        <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                        <button type="submit" class="btn btn-danger" data-i18n="action.withdraw">Withdraw Application</button>
                    </form>
                </div>
            </c:when>
            <c:when test="${alreadyApplied}">
                <span class="badge badge-info" data-status-label="AlreadyApplied">You already applied</span>
            </c:when>
            <c:when test="${deadlinePassed}">
                <div class="alert alert-error" data-i18n="jobDetail.deadlineExpired">The application deadline for this position has passed. No new applications are accepted.</div>
            </c:when>
            <c:otherwise>
                <form action="${pageContext.request.contextPath}/jobs/apply" method="post" data-confirm-key="confirm.submitApplication">
                    <input type="hidden" name="id" value="${job.id}">
                    <button class="btn btn-primary" data-i18n="action.applyNow" type="submit">Apply Now</button>
                </form>
            </c:otherwise>
        </c:choose>
    </section>
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
