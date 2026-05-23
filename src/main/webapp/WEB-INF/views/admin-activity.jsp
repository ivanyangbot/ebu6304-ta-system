<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="pageTitle" value="Activity Log" />
<c:set var="pageTitleKey" value="page.adminActivity" />
<%@ include file="includes/header.jspf" %>

<section class="section-header">
    <h2 data-i18n="activity.adminHeading">System Activity Log</h2>
    <p data-i18n="activity.adminDesc">View and filter all user actions recorded in the system.</p>
</section>

<%-- 筛选表单 --%>
<div class="card filter-card">
    <form method="get" action="${pageContext.request.contextPath}/admin/activity" class="filter-form">
        <div class="filter-row">
            <div class="filter-group">
                <label data-i18n="activity.filterUser">User Name</label>
                <input type="text" name="userFullName" class="form-control"
                       placeholder="Search by name..."
                       value="${not empty filterUserFullName ? filterUserFullName : ''}">
            </div>
            <div class="filter-group">
                <label data-i18n="activity.filterType">Action Type</label>
                <select name="actionType" class="form-control">
                    <option value="" ${empty filterActionType ? 'selected' : ''} data-i18n="common.all">All</option>
                    <option value="APPLY_JOB"                ${filterActionType == 'APPLY_JOB'                ? 'selected' : ''} data-i18n="action.applyJob">Apply Job</option>
                    <option value="WITHDRAW_APPLICATION"     ${filterActionType == 'WITHDRAW_APPLICATION'     ? 'selected' : ''} data-i18n="action.withdrawApplication">Withdraw Application</option>
                    <option value="CREATE_JOB"               ${filterActionType == 'CREATE_JOB'               ? 'selected' : ''} data-i18n="action.createJob">Create Job</option>
                    <option value="COMPLETE_JOB"             ${filterActionType == 'COMPLETE_JOB'             ? 'selected' : ''} data-i18n="action.completeJob">Complete Job</option>
                    <option value="REOPEN_JOB"               ${filterActionType == 'REOPEN_JOB'               ? 'selected' : ''} data-i18n="action.reopenJob">Reopen Job</option>
                    <option value="UPDATE_APPLICATION_STATUS" ${filterActionType == 'UPDATE_APPLICATION_STATUS' ? 'selected' : ''} data-i18n="action.updateStatus">Update Application Status</option>
                    <option value="CREATE_USER"              ${filterActionType == 'CREATE_USER'              ? 'selected' : ''} data-i18n="action.createUser">Create User</option>
                    <option value="DELETE_USER"              ${filterActionType == 'DELETE_USER'              ? 'selected' : ''} data-i18n="action.deleteUser">Delete User</option>
                </select>
            </div>
            <div class="filter-group">
                <label data-i18n="activity.filterRole">Role</label>
                <select name="userRole" class="form-control">
                    <option value="" ${empty filterUserRole ? 'selected' : ''} data-i18n="common.all">All</option>
                    <option value="APPLICANT" ${filterUserRole == 'APPLICANT' ? 'selected' : ''}>APPLICANT</option>
                    <option value="MO"        ${filterUserRole == 'MO'        ? 'selected' : ''}>MO</option>
                    <option value="ADMIN"     ${filterUserRole == 'ADMIN'     ? 'selected' : ''}>ADMIN</option>
                </select>
            </div>
            <div class="filter-group">
                <label data-i18n="activity.filterFrom">From</label>
                <input type="date" name="fromDate" class="form-control"
                       value="${not empty filterFromDate ? filterFromDate : ''}">
            </div>
            <div class="filter-group">
                <label data-i18n="activity.filterTo">To</label>
                <input type="date" name="toDate" class="form-control"
                       value="${not empty filterToDate ? filterToDate : ''}">
            </div>
        </div>
        <div class="filter-actions">
            <button type="submit" class="btn btn-primary" data-i18n="action.filter">Filter</button>
            <a href="${pageContext.request.contextPath}/admin/activity" class="btn btn-secondary" data-i18n="action.reset">Reset</a>
        </div>
    </form>
</div>

<%-- 结果统计 --%>
<div class="result-count">
    <span data-i18n="activity.resultCount">Showing</span>
    <strong>${fn:length(activityLogs)}</strong>
    <span data-i18n="activity.resultSuffix">record(s)</span>
</div>

<%-- 记录表格 --%>
<div class="table-card">
    <table>
        <thead>
        <tr>
            <th data-i18n="activity.time">Time</th>
            <th data-i18n="activity.operator">Operator</th>
            <th data-i18n="activity.role">Role</th>
            <th data-i18n="activity.actionType">Action Type</th>
            <th data-i18n="activity.description">Description</th>
            <th data-i18n="activity.stateChange">State Change</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${activityLogs}" var="log">
            <tr>
                <td class="activity-time">${log.createdAt}</td>
                <td>${log.userFullName}</td>
                <td>
                    <span class="role-pill" data-role-label="${log.userRole}">${log.userRole}</span>
                </td>
                <td>
                    <span class="activity-type-badge activity-type-${log.actionType}">${log.actionType}</span>
                </td>
                <td>${log.description}</td>
                <td>
                    <c:if test="${not empty log.beforeState and not empty log.afterState}">
                        <span class="state-before">${log.beforeState}</span>
                        <span class="state-arrow">→</span>
                        <span class="state-after badge badge-${log.afterState}">${log.afterState}</span>
                    </c:if>
                    <c:if test="${empty log.beforeState or empty log.afterState}">
                        <span class="state-na">—</span>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty activityLogs}">
            <tr>
                <td colspan="6" class="empty-row" data-i18n="activity.empty">No activity records found.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>

<style>
.filter-card {
    margin-bottom: 20px;
    padding: 16px 20px;
}
.filter-form .filter-row {
    display: flex;
    align-items: flex-end;
    gap: 12px;
    flex-wrap: wrap;
}
.filter-actions {
    display: flex;
    gap: 10px;
    margin-top: 12px;
}
.filter-group {
    display: flex;
    flex-direction: column;
    gap: 4px;
}
.filter-group label {
    font-size: 13px;
    color: #555;
    font-weight: 500;
}
.form-control {
    padding: 7px 10px;
    border: 1px solid #ddd;
    border-radius: 6px;
    font-size: 14px;
    min-width: 150px;
}
.result-count {
    margin-bottom: 12px;
    font-size: 14px;
    color: #555;
}
.activity-time {
    white-space: nowrap;
    font-size: 13px;
    color: #666;
}
.activity-type-badge {
    display: inline-block;
    padding: 3px 8px;
    border-radius: 4px;
    font-size: 11px;
    font-weight: 600;
    letter-spacing: 0.3px;
    background-color: #e9ecef;
    color: #495057;
    white-space: nowrap;
}
.activity-type-APPLY_JOB             { background-color: #d1ecf1; color: #0c5460; }
.activity-type-WITHDRAW_APPLICATION  { background-color: #fff3cd; color: #856404; }
.activity-type-CREATE_JOB            { background-color: #d4edda; color: #155724; }
.activity-type-COMPLETE_JOB          { background-color: #cce5ff; color: #004085; }
.activity-type-REOPEN_JOB            { background-color: #d4edda; color: #155724; }
.activity-type-UPDATE_APPLICATION_STATUS { background-color: #e2d9f3; color: #4a235a; }
.activity-type-CREATE_USER           { background-color: #d4edda; color: #155724; }
.activity-type-DELETE_USER           { background-color: #f8d7da; color: #721c24; }
.state-before {
    display: inline-block;
    padding: 2px 7px;
    border-radius: 4px;
    background-color: #f8d7da;
    color: #721c24;
    font-size: 12px;
}
.state-arrow {
    margin: 0 4px;
    color: #888;
    font-size: 13px;
}
.state-after {
    display: inline-block;
    padding: 2px 7px;
    border-radius: 4px;
    background-color: #d4edda;
    color: #155724;
    font-size: 12px;
}
.state-na {
    color: #bbb;
}
</style>

<%@ include file="includes/footer.jspf" %>
