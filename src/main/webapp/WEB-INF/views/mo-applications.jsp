<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Applications" />
<c:set var="pageTitleKey" value="page.jobApplications" />
<%@ include file="includes/header.jspf" %>

<section class="section-header">
    <h2><span data-i18n="moApplications.heading">Applications for</span> ${job.title}</h2>
    <p><span data-i18n="common.module">Module</span>: ${job.moduleName} | <span data-i18n="common.hours">Hours</span>: ${job.hours}</p>
</section>

<c:if test="${param.msg == 'updated'}">
    <div class="alert alert-success" data-i18n="moApplications.updated">Application status updated successfully.</div>
</c:if>

<div class="table-card">
    <table class="wide-table">
        <thead>
        <tr>
            <th data-i18n="moApplications.applicant">Applicant</th>
            <th data-i18n="profile.email">Email</th>
            <th data-i18n="profile.selfIntroduction">Self Introduction</th>
            <th data-i18n="profile.skills">Skills</th>
            <th data-i18n="moApplications.matchScore">Match Score</th>
            <th data-i18n="moApplications.missingSkills">Missing Skills</th>
            <th data-i18n="cv.heading">CV</th>
            <th data-i18n="common.status">Status</th>
            <th data-i18n="moApplications.update">Update &amp; Feedback</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${applicationDisplays}" var="item">
            <tr>
                <td>${item.applicant.fullName}</td>
                <td>${item.applicant.email}</td>
                <td class="table-text-cell">
                    <c:choose>
                        <c:when test="${empty item.applicant.selfIntroduction}">
                            <span data-i18n="common.none">None</span>
                        </c:when>
                        <c:otherwise>${item.applicant.selfIntroduction}</c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:forEach items="${item.applicant.skills}" var="skill" varStatus="status">
                        ${skill}<c:if test="${not status.last}">, </c:if>
                    </c:forEach>
                </td>
                <td>${item.matchResult.score}%</td>
                <td>
                    <c:choose>
                        <c:when test="${empty item.matchResult.missingSkills}"><span data-i18n="common.none">None</span></c:when>
                        <c:otherwise>
                            <c:forEach items="${item.matchResult.missingSkills}" var="skill" varStatus="status">
                                ${skill}<c:if test="${not status.last}">, </c:if>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="cv-cell">
                    <c:choose>
                        <c:when test="${item.applicant.hasCv()}">
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
                <td>
                    <span class="badge badge-${item.application.status}" data-status-label="${item.application.status}">${item.application.status}</span>
                </td>
                <td>
                    <form action="${pageContext.request.contextPath}/mo/application/update" method="post" class="inline-form"
                          data-confirm-key="confirm.updateApplication">
                        <input type="hidden" name="applicationId" value="${item.application.id}">
                        <input type="hidden" name="jobId" value="${job.id}">
                        <select name="status" class="feedback-status-select">
                            <option value="Pending" data-status-option="Pending" <c:if test="${item.application.status == 'Pending'}">selected</c:if>>Pending</option>
                            <option value="Accepted" data-status-option="Accepted" <c:if test="${item.application.status == 'Accepted'}">selected</c:if>>Accepted</option>
                            <option value="Rejected" data-status-option="Rejected" <c:if test="${item.application.status == 'Rejected'}">selected</c:if>>Rejected</option>
                        </select>
                        <div class="feedback-area" style="margin-top:6px;">
                            <textarea name="moFeedback" rows="2"
                                      style="width:100%;font-size:12px;padding:4px;border:1px solid #ced4da;border-radius:4px;resize:vertical;"
                                      data-i18n-placeholder="moApplications.feedbackPlaceholder"
                                      placeholder="Optional: reason for decision (visible to applicant)">${item.application.moFeedback}</textarea>
                        </div>
                        <button class="btn btn-primary btn-small" data-i18n="action.save" type="submit" style="margin-top:4px;">Save</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty applicationDisplays}">
            <tr>
                <td colspan="9" class="empty-row" data-i18n="moApplications.empty">No applicants yet for this job.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>

<style>
.cv-cell { white-space: nowrap; }
.cv-none  { color: #bbb; font-size: 16px; }
</style>

<%@ include file="includes/footer.jspf" %>
