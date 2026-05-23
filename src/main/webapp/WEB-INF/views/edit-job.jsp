<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Edit Job" />
<c:set var="pageTitleKey" value="page.editJob" />
<%@ include file="includes/header.jspf" %>

<section class="card form-card">
    <div class="section-header">
        <h2 data-i18n="editJob.heading">Edit TA Job</h2>
        <p data-i18n="editJob.desc">Modify the position details below.</p>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-error">${errorMessage}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/mo/jobs/edit" method="post">
        <input type="hidden" name="id" value="${job.id}">
        <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">

        <label for="title" data-i18n="createJob.jobTitle">Job Title</label>
        <input id="title" name="title" type="text" value="${job.title}">

        <label for="moduleName" data-i18n="createJob.moduleName">Module / Activity Name</label>
        <input id="moduleName" name="moduleName" type="text" value="${job.moduleName}">

        <label for="description" data-i18n="createJob.description">Description</label>
        <textarea id="description" name="description" rows="4">${job.description}</textarea>

        <label for="requiredSkills" data-i18n="createJob.requiredSkills">Required Skills</label>
        <textarea id="requiredSkills" name="requiredSkills" rows="3"
                  data-i18n-placeholder="createJob.requiredSkillsPlaceholder"
                  placeholder="Java, Python, Communication"><c:forEach items="${job.requiredSkills}" var="skill" varStatus="status">${skill}<c:if test="${not status.last}">, </c:if></c:forEach></textarea>

        <label for="hours" data-i18n="createJob.workloadHours">Workload Hours</label>
        <input id="hours" name="hours" type="number" min="1" value="${job.hours}">

        <button class="btn btn-primary" data-i18n="action.saveChanges" type="submit">Save Changes</button>
    </form>
</section>

<%@ include file="includes/footer.jspf" %>
