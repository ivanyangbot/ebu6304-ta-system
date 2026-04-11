<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Create Job" />
<c:set var="pageTitleKey" value="page.createJob" />
<%@ include file="includes/header.jspf" %>

<section class="card form-card">
    <div class="section-header">
        <h2 data-i18n="createJob.heading">Create a New TA Job</h2>
        <p data-i18n="createJob.desc">Fill in the position information below.</p>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-error">${errorMessage}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/mo/jobs/create" method="post">
        <label for="title" data-i18n="createJob.jobTitle">Job Title</label>
        <input id="title" name="title" type="text" value="${param.title}">

        <label for="moduleName" data-i18n="createJob.moduleName">Module / Activity Name</label>
        <input id="moduleName" name="moduleName" type="text" value="${param.moduleName}">

        <label for="description" data-i18n="createJob.description">Description</label>
        <textarea id="description" name="description" rows="4">${param.description}</textarea>

        <label for="requiredSkills" data-i18n="createJob.requiredSkills">Required Skills</label>
        <textarea id="requiredSkills" name="requiredSkills" rows="3"
                  data-i18n-placeholder="createJob.requiredSkillsPlaceholder"
                  placeholder="Java, Python, Communication">${param.requiredSkills}</textarea>

        <label for="hours" data-i18n="createJob.workloadHours">Workload Hours</label>
        <input id="hours" name="hours" type="number" min="1" value="${param.hours}">

        <button class="btn btn-primary" data-i18n="action.createJob" type="submit">Create Job</button>
    </form>
</section>

<%@ include file="includes/footer.jspf" %>
