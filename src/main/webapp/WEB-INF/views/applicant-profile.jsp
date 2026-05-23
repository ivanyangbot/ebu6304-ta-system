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

<%-- ===== CV / Resume Section ===== --%>
<section class="card form-card cv-section">
    <div class="section-header">
        <h2 data-i18n="cv.heading">Resume / CV</h2>
        <p data-i18n="cv.desc">Upload your CV so Module Organisers can review it alongside your application.</p>
    </div>

    <%-- CV upload / replace success & error messages --%>
    <c:if test="${param.msg == 'cv_uploaded'}">
        <div class="alert alert-success" data-i18n="cv.uploaded">CV uploaded successfully.</div>
    </c:if>
    <c:if test="${param.msg == 'cv_replaced'}">
        <div class="alert alert-success" data-i18n="cv.replaced">CV replaced successfully.</div>
    </c:if>
    <c:if test="${param.msg == 'cv_deleted'}">
        <div class="alert alert-success" data-i18n="cv.deleted">CV deleted successfully.</div>
    </c:if>
    <c:if test="${param.msg == 'error_empty'}">
        <div class="alert alert-error" data-i18n="cv.error.empty">No file selected. Please choose a file before uploading.</div>
    </c:if>
    <c:if test="${param.msg == 'error_size'}">
        <div class="alert alert-error" data-i18n="cv.error.size">File is too large. Maximum allowed size is 2 MB.</div>
    </c:if>
    <c:if test="${param.msg == 'error_type'}">
        <div class="alert alert-error" data-i18n="cv.error.type">Invalid file type. Only PDF, DOC, and DOCX files are accepted.</div>
    </c:if>

    <%-- Current CV status --%>
    <c:choose>
        <c:when test="${applicant.hasCv()}">
            <div class="cv-current">
                <span class="cv-icon">📄</span>
                <span class="cv-filename">${applicant.cvFileName}</span>
                <a class="btn btn-secondary btn-small"
                   href="${pageContext.request.contextPath}/cv/download?applicantId=${applicant.id}"
                   data-i18n="cv.download">Download</a>
                <form action="${pageContext.request.contextPath}/applicant/cv/upload"
                      method="post" class="cv-delete-form" style="display:inline;"
                      data-confirm-key="confirm.deleteCV">
                    <input type="hidden" name="action" value="delete">
                    <button type="submit" class="btn btn-danger btn-small" data-i18n="cv.delete">Delete CV</button>
                </form>
            </div>
        </c:when>
        <c:otherwise>
            <p class="cv-empty" data-i18n="cv.empty">No resume uploaded yet.</p>
        </c:otherwise>
    </c:choose>

    <%-- Upload form --%>
    <form action="${pageContext.request.contextPath}/applicant/cv/upload"
          method="post" enctype="multipart/form-data" class="cv-upload-form">
        <input type="hidden" name="action" value="upload">
        <div class="cv-upload-row">
            <label class="cv-file-label">
                <input type="file" name="cvFile" id="cvFile" accept=".pdf,.doc,.docx"
                       class="cv-file-input">
                <span class="cv-file-placeholder" data-i18n="cv.chooseFile">Choose file…</span>
            </label>
            <span class="cv-hint" data-i18n="cv.hint">PDF, DOC, DOCX &mdash; max 2 MB</span>
        </div>
        <button type="submit" class="btn btn-primary" data-i18n="cv.upload">
            <c:choose>
                <c:when test="${applicant.hasCv()}" >Replace CV</c:when>
                <c:otherwise>Upload CV</c:otherwise>
            </c:choose>
        </button>
    </form>
</section>

<style>
.cv-section { margin-top: 20px; }
.cv-current {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px 16px;
    background: #f0f7ff;
    border: 1px solid #b8d9f8;
    border-radius: 8px;
    margin-bottom: 16px;
    flex-wrap: wrap;
}
.cv-icon  { font-size: 22px; flex-shrink: 0; }
.cv-filename {
    flex: 1;
    font-weight: 600;
    color: #1a4f8a;
    word-break: break-all;
}
.cv-empty {
    color: #888;
    font-size: 14px;
    margin-bottom: 14px;
}
.cv-upload-row {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 12px;
    flex-wrap: wrap;
}
.cv-file-label {
    position: relative;
    display: inline-block;
    cursor: pointer;
}
.cv-file-input {
    position: absolute;
    inset: 0;
    opacity: 0;
    cursor: pointer;
    width: 100%;
}
.cv-file-placeholder {
    display: inline-block;
    padding: 8px 16px;
    background: #fff;
    border: 1.5px dashed #aaa;
    border-radius: 6px;
    color: #555;
    font-size: 14px;
    min-width: 180px;
    text-align: center;
    transition: border-color .2s, background .2s;
}
.cv-file-label:hover .cv-file-placeholder {
    border-color: #007bff;
    background: #f0f7ff;
    color: #007bff;
}
.cv-hint {
    font-size: 12px;
    color: #999;
}
.cv-delete-form { margin: 0; }
</style>

<script>
(function () {
    // Show selected filename in the placeholder span after user picks a file
    var input = document.getElementById('cvFile');
    if (input) {
        input.addEventListener('change', function () {
            var placeholder = this.parentElement.querySelector('.cv-file-placeholder');
            if (placeholder) {
                placeholder.textContent = this.files.length > 0 ? this.files[0].name : 'Choose file\u2026';
            }
        });
    }
    // Delete confirmation and upload validation are handled globally by main.js
    // (setupConfirmForms via data-confirm-key, setupCvUploadValidation via .cv-upload-form)
})();
</script>

<%@ include file="includes/footer.jspf" %>
