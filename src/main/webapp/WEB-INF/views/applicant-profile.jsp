<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="My Profile" />
<c:set var="pageTitleKey" value="page.profile" />
<%@ include file="includes/header.jspf" %>

<section class="card form-card">
    <div class="section-header">
        <h2 data-i18n="profile.heading">Applicant Profile</h2>
        <p data-i18n="profile.desc">Update your name, email, self introduction and skills for better matching.</p>
    </div>

    <c:if test="${param.msg == 'updated'}">
        <div class="alert alert-success" data-i18n="profile.updated">Profile updated successfully.</div>
    </c:if>
    <c:if test="${param.msg == 'registered'}">
        <div class="alert alert-success" data-i18n="profile.registered">Account created successfully. Complete your profile to improve matching.</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-error">${errorMessage}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/applicant/profile" method="post">
        <label for="fullName" data-i18n="profile.fullName">Full Name</label>
        <input id="fullName" name="fullName" type="text" value="${applicant.fullName}">

        <label for="email" data-i18n="profile.email">Email</label>
        <input id="email" name="email" type="email" value="${applicant.email}">

        <label for="selfIntroduction" data-i18n="profile.selfIntroduction">Self Introduction</label>
        <textarea id="selfIntroduction" name="selfIntroduction" rows="5"
                  data-i18n-placeholder="profile.selfIntroductionPlaceholder"
                  placeholder="Share your background, strengths and TA-related experience">${applicant.selfIntroduction}</textarea>

        <label for="skills" data-i18n="profile.skills">Skills</label>
        <textarea id="skills" name="skills" rows="4" data-i18n-placeholder="profile.skillsPlaceholder" placeholder="Java, Excel, Communication">${skillsText}</textarea>
        <p class="hint" data-i18n="profile.skillsHint">Use comma-separated skills, for example: Java, Python, Communication</p>

        <button class="btn btn-primary" data-i18n="action.saveProfile" type="submit">Save Profile</button>
    </form>
</section>

<%@ include file="includes/footer.jspf" %>
