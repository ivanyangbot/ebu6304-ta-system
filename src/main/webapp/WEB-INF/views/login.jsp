<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="authModeValue" value="${empty authMode ? 'login' : authMode}" />
<c:set var="loginHiddenAttr" value="${authModeValue == 'register' ? 'hidden' : ''}" />
<c:set var="registerHiddenAttr" value="${authModeValue == 'login' ? 'hidden' : ''}" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - TA Recruitment System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="login-body" data-page-title-key="page.login" data-auth-mode="${authModeValue}"
      data-context-path="${pageContext.request.contextPath}">
<div class="login-shell">
    <section class="login-hero">
        <p class="login-kicker" data-i18n="brand.subtitle">BUPT International School</p>
        <h1 data-i18n="login.heroTitle">TA Recruitment
System</h1>
        <p class="login-copy" data-i18n="login.intro">
            Switch between roles, review applications quickly, and manage the TA process in one cleaner workspace.
        </p>

        <div class="login-feature-grid">
            <div class="feature-card">
                <strong data-i18n="login.highlightApplicantTitle">Applicants</strong>
                <p data-i18n="login.highlightApplicantDesc">Maintain profiles, compare matches, and track applications.</p>
            </div>
            <div class="feature-card">
                <strong data-i18n="login.highlightMoTitle">Module Organisers</strong>
                <p data-i18n="login.highlightMoDesc">Create positions and update results from one place.</p>
            </div>
            <div class="feature-card">
                <strong data-i18n="login.highlightAdminTitle">Administrators</strong>
                <p data-i18n="login.highlightAdminDesc">Monitor accepted workload and spot overload risks early.</p>
            </div>
        </div>

        <div class="journey-card">
            <p class="journey-kicker" data-i18n="login.quickStart">Quick Start</p>
            <div class="journey-list">
                <div class="journey-step">
                    <span class="journey-index">01</span>
                    <div>
                        <strong data-i18n="login.stepAccountTitle">Create an applicant account</strong>
                        <p data-i18n="login.stepAccountDesc">Students can register directly and enter their skills immediately.</p>
                    </div>
                </div>
                <div class="journey-step">
                    <span class="journey-index">02</span>
                    <div>
                        <strong data-i18n="login.stepProfileTitle">Complete profile details</strong>
                        <p data-i18n="login.stepProfileDesc">Update your name, email and skills to improve the match score.</p>
                    </div>
                </div>
                <div class="journey-step">
                    <span class="journey-index">03</span>
                    <div>
                        <strong data-i18n="login.stepApplyTitle">Track results in one place</strong>
                        <p data-i18n="login.stepApplyDesc">Apply for jobs, review outcomes and manage workload from the same workspace.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section class="login-card">
        <div class="login-card-top">
            <div>
                <p class="workspace-kicker" data-i18n="login.formTitle">Access Workspace</p>
                <h2 data-i18n="login.systemTitle">Teaching Assistant Recruitment System</h2>
                <p class="login-note" data-i18n="login.formDesc">Sign in with an existing account or create a new applicant account.</p>
            </div>
            <button class="lang-switch" type="button" data-lang-toggle>&#20013;&#25991;</button>
        </div>

        <div class="auth-tabs" role="tablist" aria-label="Authentication mode">
            <button class="auth-tab" type="button" role="tab" data-auth-tab="login"
                    data-i18n="login.tabSignIn">Sign in</button>
            <button class="auth-tab" type="button" role="tab" data-auth-tab="register"
                    data-i18n="login.tabRegister">Create account</button>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">${errorMessage}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post"
              class="login-form auth-form" data-auth-panel="login" data-auth-title-key="page.login" ${loginHiddenAttr}>
            <div class="form-group">
                <label for="login-username" data-i18n="login.username">Username</label>
                <input id="login-username" name="username" type="text" value="${username}"
                       autocomplete="username" data-i18n-placeholder="login.usernamePlaceholder"
                       placeholder="Enter username" required>
            </div>

            <div class="form-group">
                <label for="login-password" data-i18n="login.password">Password</label>
                <input id="login-password" name="password" type="password" autocomplete="current-password"
                       data-i18n-placeholder="login.passwordPlaceholder" placeholder="Enter password" required>
            </div>

            <button class="btn btn-primary btn-block" data-i18n="action.login" type="submit">Login</button>
        </form>

        <form action="${pageContext.request.contextPath}/register" method="post"
              class="login-form auth-form" data-auth-panel="register" data-auth-title-key="page.login" ${registerHiddenAttr}>
            <div class="form-row">
                <div class="form-group">
                    <label for="register-fullName" data-i18n="register.fullName">Full Name</label>
                    <input id="register-fullName" name="fullName" type="text" value="${registerFullName}"
                           autocomplete="name" data-i18n-placeholder="register.fullNamePlaceholder"
                           placeholder="Enter your full name" required>
                </div>

                <div class="form-group">
                    <label for="register-username" data-i18n="register.username">Username</label>
                    <input id="register-username" name="username" type="text" value="${registerUsername}"
                           autocomplete="username" data-i18n-placeholder="register.usernamePlaceholder"
                           placeholder="4-20 characters" required>
                </div>
            </div>

            <div class="form-group">
                <label for="register-email" data-i18n="register.email">Email</label>
                <input id="register-email" name="email" type="email" value="${registerEmail}"
                       autocomplete="email" data-i18n-placeholder="register.emailPlaceholder"
                       placeholder="Enter your email" required>
            </div>

            <div class="form-group">
                <label for="register-password" data-i18n="register.password">Password</label>
                <input id="register-password" name="password" type="password" autocomplete="new-password"
                       data-i18n-placeholder="register.passwordPlaceholder"
                       placeholder="At least 6 characters" required>
            </div>

            <div class="form-group">
                <label for="register-confirmPassword" data-i18n="register.confirmPassword">Confirm Password</label>
                <input id="register-confirmPassword" name="confirmPassword" type="password"
                       autocomplete="new-password" data-register-confirm
                       data-i18n-placeholder="register.confirmPasswordPlaceholder"
                       placeholder="Re-enter your password" required>
            </div>

            <button class="btn btn-primary btn-block" data-i18n="register.submit" type="submit">Create Applicant Account</button>
        </form>
    </section>
</div>
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>
