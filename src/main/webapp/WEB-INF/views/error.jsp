<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Error" />
<c:set var="pageTitleKey" value="page.error" />
<%@ include file="includes/header.jspf" %>

<section class="card narrow-card">
    <h2 data-i18n="error.heading">Something went wrong</h2>
    <p class="error-text">${errorMessage}</p>
    <c:choose>
        <c:when test="${not empty backUrl}">
            <a class="btn btn-primary" data-i18n="action.goBack" href="${backUrl}">Go Back</a>
        </c:when>
        <c:otherwise>
            <a class="btn btn-primary" data-i18n="action.returnDashboard" href="${pageContext.request.contextPath}/dashboard">Return to Dashboard</a>
        </c:otherwise>
    </c:choose>
</section>

<%@ include file="includes/footer.jspf" %>
