<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="AI Data Query" />
<c:set var="pageTitleKey" value="page.aiQuery" />
<%@ include file="includes/header.jspf" %>

<section class="section-header">
    <h2 data-i18n="aiQuery.heading">AI Data Query</h2>
    <p data-i18n="aiQuery.intro">Ask a simple question about users, jobs, applications, or workload. The answer is generated from the current JSON data snapshot.</p>
</section>

<div class="grid-two">
    <div class="card">
        <h3 data-i18n="aiQuery.askTitle">Ask a Question</h3>
        <form action="${pageContext.request.contextPath}/admin/ai-query/ask" method="post" class="form-card">
            <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
            <label for="question" data-i18n="aiQuery.questionLabel">Your Question</label>
            <textarea id="question" name="question" rows="5" required placeholder="Example: How many pending applications are there?">${question}</textarea>
            <p class="hint" data-i18n="aiQuery.examples">Examples: pending application count, open jobs, overloaded applicants.</p>
            <button type="submit" class="btn btn-primary" data-i18n="aiQuery.submit">Ask AI</button>
        </form>
    </div>

    <div class="card">
        <h3 data-i18n="aiQuery.snapshotTitle">Current Data Snapshot</h3>
        <pre class="ai-query-snapshot">${dataSnapshot}</pre>
    </div>
</div>

<c:if test="${not empty errorMessage}">
    <div class="alert alert-error">${errorMessage}</div>
</c:if>

<c:if test="${not empty answer}">
    <div class="card ai-query-result">
        <h3 data-i18n="aiQuery.answerTitle">AI Answer</h3>
        <pre class="ai-query-answer">${answer}</pre>
    </div>
</c:if>

<%@ include file="includes/footer.jspf" %>
