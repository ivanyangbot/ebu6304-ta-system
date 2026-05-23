<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="My Activity History" />
<c:set var="pageTitleKey" value="page.myActivity" />
<%@ include file="includes/header.jspf" %>

<section class="section-header">
    <h2 data-i18n="activity.myHeading">My Activity History</h2>
    <p data-i18n="activity.myDesc">A complete record of all actions you have performed in the system.</p>
</section>

<%-- 筛选表单 --%>
<div class="card filter-card">
    <form method="get" action="${pageContext.request.contextPath}/activity/my" class="filter-form">
        <div class="filter-row">
            <div class="filter-group">
                <label data-i18n="activity.filterType">Action Type</label>
                <select name="actionType" class="form-control">
                    <option value="" ${empty filterType ? 'selected' : ''} data-i18n="common.all">All</option>
                    <c:if test="${sessionScope.currentUser.role == 'APPLICANT'}">
                        <option value="APPLY_JOB"            ${filterType == 'APPLY_JOB'            ? 'selected' : ''} data-i18n="action.applyJob">Apply Job</option>
                        <option value="WITHDRAW_APPLICATION" ${filterType == 'WITHDRAW_APPLICATION' ? 'selected' : ''} data-i18n="action.withdrawApplication">Withdraw Application</option>
                    </c:if>
                    <c:if test="${sessionScope.currentUser.role == 'MO'}">
                        <option value="CREATE_JOB"                 ${filterType == 'CREATE_JOB'                 ? 'selected' : ''} data-i18n="action.createJob">Create Job</option>
                        <option value="COMPLETE_JOB"               ${filterType == 'COMPLETE_JOB'               ? 'selected' : ''} data-i18n="action.completeJob">Complete Job</option>
                        <option value="REOPEN_JOB"                 ${filterType == 'REOPEN_JOB'                 ? 'selected' : ''} data-i18n="action.reopenJob">Reopen Job</option>
                        <option value="UPDATE_APPLICATION_STATUS"  ${filterType == 'UPDATE_APPLICATION_STATUS'  ? 'selected' : ''} data-i18n="action.updateStatus">Update Application Status</option>
                    </c:if>
                    <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
                        <option value="CREATE_USER" ${filterType == 'CREATE_USER' ? 'selected' : ''} data-i18n="action.createUser">Create User</option>
                        <option value="DELETE_USER" ${filterType == 'DELETE_USER' ? 'selected' : ''} data-i18n="action.deleteUser">Delete User</option>
                    </c:if>
                </select>
            </div>
            <button type="submit" class="btn btn-primary" data-i18n="action.filter">Filter</button>
            <a href="${pageContext.request.contextPath}/activity/my" class="btn btn-secondary" data-i18n="action.reset">Reset</a>
        </div>
    </form>
</div>

<%-- 历史记录表格 --%>
<div class="table-card">
    <table>
        <thead>
        <tr>
            <th data-i18n="activity.time">Time</th>
            <th data-i18n="activity.actionType">Action Type</th>
            <th data-i18n="activity.description">Description</th>
            <th data-i18n="activity.stateChange">State Change</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${activityLogs}" var="log">
            <tr>
                <td class="activity-time">${log.createdAt}</td>
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
                <td colspan="4" class="empty-row" data-i18n="activity.empty">No activity records found.</td>
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
    min-width: 180px;
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
.activity-type-APPLY_JOB            { background-color: #d1ecf1; color: #0c5460; }
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
