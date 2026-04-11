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
    </section>

    <section class="card">
        <h2 data-i18n="jobDetail.matchTitle">Match Result</h2>
        <c:if test="${param.msg == 'applied'}">
            <div class="alert alert-success" data-i18n="jobDetail.submitted">Application submitted successfully.</div>
        </c:if>
        <c:if test="${param.msg == 'duplicate'}">
            <div class="alert alert-error" data-i18n="jobDetail.duplicate">You have already applied for this job.</div>
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

        <c:choose>
            <c:when test="${alreadyApplied}">
                <span class="badge badge-info" data-status-label="AlreadyApplied">You already applied</span>
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

<%@ include file="includes/footer.jspf" %>
