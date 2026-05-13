<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Create User" />
<c:set var="pageTitleKey" value="page.createUser" />
<%@ include file="includes/header.jspf" %>

<section class="section-header">
    <h2 data-i18n="admin.createUser">Create New User</h2>
    <p data-i18n="admin.createUserDesc">Create a new user account for the system.</p>
</section>

<c:if test="${not empty errorMessage}">
    <div class="alert alert-error" role="alert">${errorMessage}</div>
</c:if>

<div class="card">
    <form action="${pageContext.request.contextPath}/admin/users/create" method="post" class="form-vertical">
        <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
        
        <div class="form-group">
            <label data-i18n="form.role">Role</label>
            <select name="role" required>
                <option value="APPLICANT" ${role == 'APPLICANT' ? 'selected' : ''} data-i18n="role.APPLICANT">Applicant</option>
                <option value="MO" ${role == 'MO' ? 'selected' : ''} data-i18n="role.MO">Module Organiser</option>
                <option value="ADMIN" ${role == 'ADMIN' ? 'selected' : ''} data-i18n="role.ADMIN">Administrator</option>
            </select>
        </div>
        
        <div class="form-group">
            <label data-i18n="form.fullName">Full Name</label>
            <input type="text" name="fullName" value="${fullName}" required data-i18n-placeholder="form.fullNamePlaceholder" placeholder="Enter full name">
        </div>
        
        <div class="form-group">
            <label data-i18n="form.email">Email</label>
            <input type="email" name="email" value="${email}" required data-i18n-placeholder="form.emailPlaceholder" placeholder="Enter email">
        </div>
        
        <div class="form-group">
            <label data-i18n="form.username">Username</label>
            <input type="text" name="username" value="${username}" required data-i18n-placeholder="form.usernamePlaceholder" placeholder="Enter username">
        </div>
        
        <div class="form-group">
            <label data-i18n="form.password">Password</label>
            <input type="password" name="password" required data-i18n-placeholder="form.passwordPlaceholder" placeholder="Enter password (min 6 chars)">
        </div>
        
        <div class="form-actions">
            <button type="submit" class="btn btn-primary" data-i18n="action.create">Create User</button>
            <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary" data-i18n="action.cancel">Cancel</a>
        </div>
    </form>
</div>

<%@ include file="includes/footer.jspf" %>
