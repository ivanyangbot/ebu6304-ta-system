<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="User Management" />
<c:set var="pageTitleKey" value="page.userManagement" />
<%@ include file="includes/header.jspf" %>

<section class="section-header">
    <h2 data-i18n="admin.userManagementHeading">User Management</h2>
    <p data-i18n="admin.userManagementDesc">Manage all users in the system. You can view, create, and delete accounts.</p>
</section>

<c:if test="${not empty successMessage}">
    <div class="alert alert-success" role="alert">${successMessage}</div>
</c:if>
<c:if test="${not empty errorMessage}">
    <div class="alert alert-error" role="alert">${errorMessage}</div>
</c:if>

<div class="card create-user-card">
    <h3 data-i18n="admin.createUser">Create New User</h3>
    <form action="${pageContext.request.contextPath}/admin/users" method="post" class="form-inline">
        <input type="hidden" name="action" value="create">
        
        <div class="form-group">
            <label data-i18n="form.role">Role</label>
            <select name="role" required>
                <option value="APPLICANT" data-i18n="role.APPLICANT">Applicant</option>
                <option value="MO" data-i18n="role.MO">Module Organiser</option>
                <option value="ADMIN" data-i18n="role.ADMIN">Administrator</option>
            </select>
        </div>
        
        <div class="form-group">
            <label data-i18n="form.fullName">Full Name</label>
            <input type="text" name="fullName" required data-i18n-placeholder="form.fullNamePlaceholder" placeholder="Enter full name">
        </div>
        
        <div class="form-group">
            <label data-i18n="form.email">Email</label>
            <input type="email" name="email" required data-i18n-placeholder="form.emailPlaceholder" placeholder="Enter email">
        </div>
        
        <div class="form-group">
            <label data-i18n="form.username">Username</label>
            <input type="text" name="username" required data-i18n-placeholder="form.usernamePlaceholder" placeholder="Enter username">
        </div>
        
        <div class="form-group">
            <label data-i18n="form.password">Password</label>
            <input type="password" name="password" required data-i18n-placeholder="form.passwordPlaceholder" placeholder="Enter password (min 6 chars)">
        </div>
        
        <button type="submit" class="btn btn-primary" data-i18n="action.create">Create User</button>
    </form>
</div>

<div class="card">
    <h3 data-i18n="admin.applicants">Applicants</h3>
    <div class="table-card">
        <table>
            <thead>
            <tr>
                <th data-i18n="table.name">Name</th>
                <th data-i18n="table.email">Email</th>
                <th data-i18n="table.username">Username</th>
                <th data-i18n="table.role" class="text-center">Role</th>
                <th data-i18n="table.actions" class="text-center">Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${applicants}" var="user">
                <tr>
                    <td>${user.fullName}</td>
                    <td>${user.email}</td>
                    <td>${user.username}</td>
                    <td class="text-center"><span class="badge badge-applicant" data-i18n="role.APPLICANT">Applicant</span></td>
                    <td class="text-center">
                        <button type="button" class="btn btn-secondary btn-sm reset-password-btn" data-user-id="${user.id}" data-i18n="action.resetPassword">Reset Password</button>
                        <form action="${pageContext.request.contextPath}/admin/users" method="post" class="delete-form">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="userId" value="${user.id}">
                            <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                            <button type="submit" class="btn btn-danger btn-sm delete-user-btn" data-i18n="action.delete">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty applicants}">
                <tr>
                    <td colspan="5" class="empty-row" data-i18n="admin.noApplicants">No applicants found.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<div class="card">
    <h3 data-i18n="admin.mos">Module Organizers (MO)</h3>
    <div class="table-card">
        <table>
            <thead>
            <tr>
                <th data-i18n="table.name">Name</th>
                <th data-i18n="table.email">Email</th>
                <th data-i18n="table.username">Username</th>
                <th data-i18n="table.role" class="text-center">Role</th>
                <th data-i18n="table.actions" class="text-center">Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${mos}" var="user">
                <tr>
                    <td>${user.fullName}</td>
                    <td>${user.email}</td>
                    <td>${user.username}</td>
                    <td class="text-center"><span class="badge badge-mo" data-i18n="role.MO">Module Organiser</span></td>
                    <td class="text-center">
                        <button type="button" class="btn btn-secondary btn-sm reset-password-btn" data-user-id="${user.id}" data-i18n="action.resetPassword">Reset Password</button>
                        <form action="${pageContext.request.contextPath}/admin/users" method="post" class="delete-form">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="userId" value="${user.id}">
                            <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                            <button type="submit" class="btn btn-danger btn-sm delete-user-btn" data-i18n="action.delete">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty mos}">
                <tr>
                    <td colspan="5" class="empty-row" data-i18n="admin.noMOs">No module organizers found.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<div class="card">
    <h3 data-i18n="admin.admins">Administrators</h3>
    <div class="table-card">
        <table>
            <thead>
            <tr>
                <th data-i18n="table.name">Name</th>
                <th data-i18n="table.email">Email</th>
                <th data-i18n="table.username">Username</th>
                <th data-i18n="table.role" class="text-center">Role</th>
                <th data-i18n="table.actions" class="text-center">Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${admins}" var="user">
                <tr>
                    <td>${user.fullName}</td>
                    <td>${user.email}</td>
                    <td>${user.username}</td>
                    <td class="text-center"><span class="badge badge-admin" data-i18n="role.ADMIN">Administrator</span></td>
                    <td class="text-center">
                        <button type="button" class="btn btn-secondary btn-sm reset-password-btn" data-user-id="${user.id}" data-i18n="action.resetPassword">Reset Password</button>
                        <form action="${pageContext.request.contextPath}/admin/users" method="post" class="delete-form">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="userId" value="${user.id}">
                            <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                            <button type="submit" class="btn btn-danger btn-sm delete-user-btn" data-i18n="action.delete">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty admins}">
                <tr>
                    <td colspan="5" class="empty-row" data-i18n="admin.noAdmins">No administrators found.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<div id="resetPasswordModal" class="modal" style="display: none;">
    <div class="modal-content">
        <span class="modal-close">&times;</span>
        <h3 data-i18n="admin.resetPasswordTitle">Reset User Password</h3>
        <form id="resetPasswordForm" action="${pageContext.request.contextPath}/admin/users" method="post">
            <input type="hidden" name="action" value="resetPassword">
            <input type="hidden" name="userId" id="resetUserId">
            <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
            <div class="form-group">
                <label data-i18n="form.newPassword">New Password</label>
                <input type="password" name="newPassword" id="newPassword" required minlength="6" data-i18n-placeholder="form.newPasswordPlaceholder" placeholder="Enter new password (min 6 chars)">
            </div>
            <div class="form-group">
                <label data-i18n="form.confirmPassword">Confirm Password</label>
                <input type="password" name="confirmPassword" id="confirmPassword" required minlength="6" data-i18n-placeholder="form.confirmPasswordPlaceholder" placeholder="Confirm new password">
            </div>
            <div class="modal-buttons">
                <button type="button" class="btn btn-secondary modal-cancel" data-i18n="action.cancel">Cancel</button>
                <button type="submit" class="btn btn-primary" data-i18n="action.resetPassword">Reset Password</button>
            </div>
        </form>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    var deleteForms = document.querySelectorAll('.delete-form');
    deleteForms.forEach(function(form) {
        form.addEventListener('submit', function(e) {
            var translations = {
                en: "Are you sure you want to delete this user?",
                zh: "确定要删除此用户吗？"
            };
            var lang = document.body.getAttribute('data-language') || 'en';
            var message = translations[lang] || translations.en;
            if (!confirm(message)) {
                e.preventDefault();
            }
        });
    });

    var resetPasswordModal = document.getElementById('resetPasswordModal');
    var resetPasswordForms = document.querySelectorAll('.reset-password-btn');
    var modalClose = document.querySelector('.modal-close');
    var modalCancel = document.querySelector('.modal-cancel');
    var resetPasswordForm = document.getElementById('resetPasswordForm');

    resetPasswordForms.forEach(function(btn) {
        btn.addEventListener('click', function() {
            var userId = btn.getAttribute('data-user-id');
            document.getElementById('resetUserId').value = userId;
            document.getElementById('newPassword').value = '';
            document.getElementById('confirmPassword').value = '';
            resetPasswordModal.style.display = 'block';
        });
    });

    modalClose.addEventListener('click', function() {
        resetPasswordModal.style.display = 'none';
    });

    modalCancel.addEventListener('click', function() {
        resetPasswordModal.style.display = 'none';
    });

    window.addEventListener('click', function(e) {
        if (e.target === resetPasswordModal) {
            resetPasswordModal.style.display = 'none';
        }
    });

    resetPasswordForm.addEventListener('submit', function(e) {
        var newPassword = document.getElementById('newPassword').value;
        var confirmPassword = document.getElementById('confirmPassword').value;
        var translations = {
            en: "Are you sure you want to reset this user's password?",
            zh: "确定要重置此用户的密码吗？"
        };
        var lang = document.body.getAttribute('data-language') || 'en';
        var message = translations[lang] || translations.en;

        if (newPassword.length < 6) {
            var minLengthMsg = lang === 'zh' ? '密码至少需要6个字符' : 'Password must be at least 6 characters';
            alert(minLengthMsg);
            e.preventDefault();
            return;
        }

        if (newPassword !== confirmPassword) {
            var mismatchMsg = lang === 'zh' ? '两次输入的密码不一致' : 'Passwords do not match';
            alert(mismatchMsg);
            e.preventDefault();
            return;
        }

        if (!confirm(message)) {
            e.preventDefault();
        }
    });
});
</script>

<style>
.modal {
    position: fixed;
    z-index: 1000;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
}

.modal-content {
    background-color: #fefefe;
    margin: 10% auto;
    padding: 24px;
    border-radius: 12px;
    width: 400px;
    max-width: 90%;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.modal-close {
    float: right;
    font-size: 24px;
    font-weight: bold;
    cursor: pointer;
    color: #888;
}

.modal-close:hover {
    color: #333;
}

.modal-buttons {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    margin-top: 20px;
}

#resetPasswordForm .form-group {
    margin-bottom: 15px;
}

#resetPasswordForm input[type="password"] {
    width: 100%;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 8px;
    font-size: 14px;
}
</style>

<%@ include file="includes/footer.jspf" %>