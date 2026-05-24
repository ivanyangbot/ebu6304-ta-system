# Test Documentation

## 1. Purpose

This document describes the automated test programs added for the TA Recruitment System acceptance package. The tests are designed to verify repository persistence, servlet request handling, configuration loading, and a representative recruitment workflow without requiring a running Tomcat server, browser automation, or external AI API access.

## 2. Test Technology

- Test framework: JUnit Jupiter 5
- Mocking framework: Mockito
- Build runner: Maven Surefire Plugin
- Test data storage: temporary JSON files created by JUnit `@TempDir`
- Servlet testing style: direct servlet method calls with mocked Servlet API objects

The tests use the same JSON serialization utility and repository classes as the production application. Each test points repository access to a temporary data directory through `PathUtil.DATA_DIR_ATTRIBUTE`, so running tests does not modify deployed application data under `src/main/webapp/WEB-INF/data/`.

## 3. How to Run the Tests

Run all tests from the project root:

```bash
mvn test
```

If running from the parent coursework directory, specify the project POM:

```bash
mvn -f ebu6304-ta-system/pom.xml test
```

Run repository tests only:

```bash
mvn -Dtest='com.bupt.tarecruitment.repository.*Test' test
```

Run servlet tests only:

```bash
mvn -Dtest='com.bupt.tarecruitment.servlet.*Test' test
```

Run configuration tests only:

```bash
mvn -Dtest=ConfigLoaderTest test
```

## 4. Test Support Classes

### 4.1 `TestFixtures`

`src/test/java/com/bupt/tarecruitment/TestFixtures.java`

Creates deterministic test objects for applicants, module organisers, administrators, jobs, applications, notifications, and activity logs. These fixtures keep test setup concise and make expected IDs, roles, and statuses explicit.

### 4.2 `JsonTestData`

`src/test/java/com/bupt/tarecruitment/JsonTestData.java`

Writes temporary JSON files with production file names such as `users.json`, `jobs.json`, `applications.json`, `notifications.json`, and `activity_logs.json`. It uses the production `JsonFileUtil`, so tests exercise the same JSON format used by the application.

### 4.3 `ServletTestSupport`

`src/test/java/com/bupt/tarecruitment/servlet/ServletTestSupport.java`

Provides shared Mockito helpers for servlet tests, including mocked servlet contexts, servlet initialisation, sessions containing `currentUser`, and JSP forward dispatchers.

## 5. Repository Unit Tests

### 5.1 `ApplicationRepositoryTest`

`src/test/java/com/bupt/tarecruitment/repository/ApplicationRepositoryTest.java`

Covers JSON-backed application persistence and query behaviour:

- loading all applications from `applications.json`
- lookup by application ID
- filtering by applicant ID and job ID
- duplicate application detection by job and applicant
- appending new applications
- updating application status
- trimming and saving MO feedback
- deleting applications and throwing errors for missing records

### 5.2 `NotificationRepositoryTest`

`src/test/java/com/bupt/tarecruitment/repository/NotificationRepositoryTest.java`

Covers notification persistence and state changes:

- newest-first sorting
- filtering notifications by user
- unread notification queries and counts
- marking a single notification as read
- marking all notifications for one user as read
- deleting one notification
- creating application status notifications for applicants
- creating new application notifications for module organisers

### 5.3 `ActivityLogRepositoryTest`

`src/test/java/com/bupt/tarecruitment/repository/ActivityLogRepositoryTest.java`

Covers audit-log persistence and administrator filtering:

- newest-first sorting
- appending a new activity log
- filtering logs by operator user ID
- limiting recent logs for a user
- filtering by operator name, action type, role, and timestamp range

## 6. Servlet Integration-Style Tests

These tests call servlet `doGet` and `doPost` methods directly. They use mocked request, response, session, servlet context, and request dispatcher objects while still exercising real servlet logic and real repositories backed by temporary JSON data.

### 6.1 `LoginServletTest`

`src/test/java/com/bupt/tarecruitment/servlet/LoginServletTest.java`

Covers login behaviour:

- anonymous `GET` forwards to the login JSP
- logged-in `GET` redirects to the dashboard
- invalid credentials set the login error and preserve the username
- valid credentials invalidate the old session, create a new authenticated session, and redirect to the dashboard

### 6.2 `RegisterServletTest`

`src/test/java/com/bupt/tarecruitment/servlet/RegisterServletTest.java`

Covers applicant registration behaviour:

- `GET` sets register mode and forwards to the login/register JSP
- invalid email input shows a validation error and preserves form values
- valid applicant registration persists a new user, stores `currentUser` in the session, and redirects to the applicant profile page

### 6.3 `AdminAiQueryServletTest`

`src/test/java/com/bupt/tarecruitment/servlet/AdminAiQueryServletTest.java`

Covers administrator AI query page access:

- anonymous users are redirected to login
- non-admin users receive the permission error page
- admin users receive a CSRF token and recruitment data snapshot before forwarding to the AI query JSP

### 6.4 `AdminAiQueryStreamServletTest`

`src/test/java/com/bupt/tarecruitment/servlet/AdminAiQueryStreamServletTest.java`

Covers validation paths for AI query submission:

- invalid CSRF tokens are rejected with an error page
- blank questions show a validation error and return to the AI query page

The successful AI streaming path is intentionally not tested because it would call the configured external AI service. This keeps automated tests deterministic and safe for offline execution.

### 6.5 `ApplyJobServletTest`

`src/test/java/com/bupt/tarecruitment/servlet/ApplyJobServletTest.java`

Covers a representative recruitment workflow:

- anonymous users are redirected to login
- non-applicant users receive a permission error
- a valid applicant applying to an open job creates an application, creates a module-organiser notification, writes an activity log, and redirects to the job detail page

## 7. Utility Unit Tests

### 7.1 `ConfigLoaderTest`

`src/test/java/com/bupt/tarecruitment/util/ConfigLoaderTest.java`

Covers local configuration loading:

- non-placeholder values from a temporary `local.properties` file are injected into servlet context init parameters
- placeholder and blank values are skipped
- repeated loading returns immediately when the loaded marker already exists

These tests do not depend on the developer machine's real configuration files or environment variables.

## 8. External Dependency Policy

The test suite avoids network calls and external services. In particular, the AI query servlet tests cover access control and validation, but they do not execute the successful AI API request path. This ensures that `mvn test` can run consistently in local development, CI, and coursework marking environments.

## 9. Expected Result

A successful test run should complete with all JUnit tests passing. The tests should leave production JSON data unchanged because all repository and servlet test data is stored in temporary directories managed by JUnit.
