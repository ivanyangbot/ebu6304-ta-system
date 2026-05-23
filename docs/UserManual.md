# User Manual — BUPT International School TA Recruitment System

**Group 71 · EBU6304 Software Engineering**

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [System Requirements](#2-system-requirements)
3. [Getting Started](#3-getting-started)
   - 3.1 [Accessing the System](#31-accessing-the-system)
   - 3.2 [Creating an Account (Applicant)](#32-creating-an-account-applicant)
   - 3.3 [Logging In](#33-logging-in)
   - 3.4 [Logging Out](#34-logging-out)
4. [Applicant Guide](#4-applicant-guide)
   - 4.1 [Dashboard](#41-dashboard)
   - 4.2 [Edit Profile](#42-edit-profile)
   - 4.3 [Browse Available Jobs](#43-browse-available-jobs)
   - 4.4 [View Job Detail & Apply](#44-view-job-detail--apply)
   - 4.5 [My Applications](#45-my-applications)
   - 4.6 [Withdraw an Application](#46-withdraw-an-application)
   - 4.7 [My Activity Log](#47-my-activity-log)
   - 4.8 [Upload / Manage Your CV](#48-upload--manage-your-cv)
5. [Module Organiser (MO) Guide](#5-module-organiser-mo-guide)
   - 5.1 [Dashboard (MO)](#51-dashboard-mo)
   - 5.2 [Create a New Job Posting](#52-create-a-new-job-posting)
   - 5.3 [My Posted Jobs](#53-my-posted-jobs)
   - 5.4 [Review Applications](#54-review-applications)
   - 5.5 [Accept or Reject an Applicant](#55-accept-or-reject-an-applicant)
   - 5.6 [Mark a Job as Completed / Reopen](#56-mark-a-job-as-completed--reopen)
   - 5.7 [Edit a Job Posting](#57-edit-a-job-posting)
6. [Administrator Guide](#6-administrator-guide)
   - 6.1 [Dashboard (Admin)](#61-dashboard-admin)
   - 6.2 [Workload Overview](#62-workload-overview)
   - 6.3 [User Management](#63-user-management)
   - 6.4 [Create a New User Account](#64-create-a-new-user-account)
   - 6.5 [Delete a User Account](#65-delete-a-user-account)
   - 6.6 [Activity Log (System-wide)](#66-activity-log-system-wide)
   - 6.7 [All Applications Overview](#67-all-applications-overview)
7. [Notifications](#7-notifications)
8. [Language Switch](#8-language-switch)
9. [Troubleshooting](#9-troubleshooting)

---

## 1. Introduction

The **BUPT International School Teaching Assistant Recruitment System** is a lightweight Java Servlet/JSP web application that replaces the traditional paper-and-Excel TA recruitment workflow.

Key features:
- Applicants can create profiles, declare skills, browse open positions, and track application status.
- Module Organisers can post jobs, review ranked candidate lists, and update application outcomes.
- Administrators can monitor every applicant's total accepted workload and manage all user accounts.
- An AI-powered skill-matching engine computes a percentage match score and highlights missing skills for every application.

---

## 2. System Requirements

| Component | Requirement |
|-----------|-------------|
| Java | JDK 17 or later |
| Servlet container | Apache Tomcat 9.x or 10.x |
| Browser | Chrome 110+, Firefox 110+, Edge 110+, Safari 16+ |
| Screen resolution | 1280 × 720 or higher recommended |

---

## 3. Getting Started

### 3.1 Accessing the System

Open your browser and navigate to:

```
http://localhost:8080/ta-recruitment-system/
```

> Replace `localhost:8080` with the actual host and port if deployed on a remote server.

You will be redirected to the **Login / Register** page.

<!-- SCREENSHOT: screenshots/01-login-page.png -->
> 📸 **Insert screenshot here:** `screenshots/01-login-page.png`
> *(Full login page showing the left hero panel with feature cards and the right sign-in form)*

---

### 3.2 Creating an Account (Applicant)

> Only **applicants** (students) can self-register. MO and Admin accounts must be created by an administrator.

1. On the Login page, click the **"Create account"** tab.
2. Fill in the form:
   - **Full Name** – your real name (required)
   - **Username** – 4–20 characters, letters/digits/`.`/`_`/`-` only (required, unique)
   - **Email** – a valid email address (required, unique)
   - **Password** – at least 6 characters
   - **Confirm Password** – must match Password
3. Click **"Create Applicant Account"**.

<!-- SCREENSHOT: screenshots/02-register-form.png -->
> 📸 **Insert screenshot here:** `screenshots/02-register-form.png`
> *(Registration form tab active with all fields visible)*

If any field is invalid an error banner will appear above the form describing the problem.

---

### 3.3 Logging In

1. Enter your **Username** and **Password** in the Sign In form.
2. Click **"Login"**.
3. On success you are redirected to your **Dashboard**.

If credentials are incorrect, an *"Invalid username or password"* error is shown.

---

### 3.4 Logging Out

Click the **"Logout"** link in the top navigation bar. Your session is immediately invalidated and you are returned to the Login page.

---

## 4. Applicant Guide

### 4.1 Dashboard

After logging in as an Applicant you will see:

| Area | Description |
|------|-------------|
| **Welcome banner** | Greeting with your full name |
| **Notifications panel** | Unread notifications highlighted in blue (e.g. application status updates) |
| **Applicant Panel card** | Shows number of open jobs and your total applications; quick link to Browse Jobs |
| **Profile card** | Quick link to edit your profile and improve your match score |
| **Recent Activity** | The 5 most recent actions you performed; click *"View All"* for the full log |

<!-- SCREENSHOT: screenshots/03-applicant-dashboard.png -->
> 📸 **Insert screenshot here:** `screenshots/03-applicant-dashboard.png`
> *(Applicant dashboard with notification banner, two quick-action cards, and recent activity list)*

---

### 4.2 Edit Profile

1. Click **"Edit Profile"** on the Dashboard, or use the navigation menu → **Profile**.
2. Update any of the following fields:
   - **Full Name**
   - **Email**
   - **Self Introduction** – a short text about yourself (at least 30 characters boosts your match score)
   - **Skills** – comma-separated list, e.g. `Java, Python, Communication`
3. Click **"Save Profile"**.

A green success banner confirms the update.

<!-- SCREENSHOT: screenshots/04-applicant-profile.png -->
> 📸 **Insert screenshot here:** `screenshots/04-applicant-profile.png`
> *(Profile edit form with Full Name, Email, Self Introduction, and Skills fields filled in)*

> **Tip:** The more complete your profile, the higher the profile-completeness component of your match score will be.

---

### 4.3 Browse Available Jobs

Navigate to **Jobs** in the top menu or click **"Browse Jobs"** on the Dashboard.

The page lists all **Open** TA positions as a table with columns: Title, Module, Hours, Required Skills, Status, Action.

- Click **"View Detail"** to see full job information and your skill match.
- An **"Applied"** badge is shown next to jobs you have already applied for.

<!-- SCREENSHOT: screenshots/05-job-list.png -->
> 📸 **Insert screenshot here:** `screenshots/05-job-list.png`
> *(Job list table showing multiple open positions, Applied badge on one row)*

---

### 4.4 View Job Detail & Apply

Clicking **"View Detail"** opens the Job Detail page with two panels:

**Left panel – Job Information:**
- Title, Module, Description, Required Skills, Hours, Status

**Right panel – Match Result:**
- **Match Score** (0–100%) – percentage of required skills you have declared
- **Matched Skills** – required skills you already have
- **Missing Skills** – required skills you still need

To apply, click **"Apply Now"**. A confirmation alert appears; confirm to submit.

<!-- SCREENSHOT: screenshots/06-job-detail-match.png -->
> 📸 **Insert screenshot here:** `screenshots/06-job-detail-match.png`
> *(Job detail page showing both panels; match score visible, Apply Now button highlighted)*

After applying successfully, the button is replaced by a *"You already applied"* badge and a **"Withdraw Application"** button.

---

### 4.5 My Applications

Navigate to **My Applications** in the top menu to see all applications you have submitted.

The table shows: Job Title, Module, Hours, Applied Date, Status.

Status values:

| Badge | Meaning |
|-------|---------|
| 🟡 **Pending** | Awaiting MO review |
| 🟢 **Accepted** | The MO has accepted you |
| 🔴 **Rejected** | The MO has rejected your application |

<!-- SCREENSHOT: screenshots/07-my-applications.png -->
> 📸 **Insert screenshot here:** `screenshots/07-my-applications.png`
> *(My Applications table with at least one row in each status colour)*

---

### 4.6 Withdraw an Application

You can withdraw a **Pending** application in two ways:
- From **My Applications** page: click **"Withdraw"** in the Action column.
- From the **Job Detail** page: click **"Withdraw Application"** button.

A confirmation dialog will appear. Confirm to permanently delete the application.

> **Note:** Only Pending applications can be withdrawn. Accepted or Rejected applications cannot be withdrawn.

---

### 4.7 My Activity Log

Navigate to **Activity** in the top menu to view your full personal audit trail.

Use the filter dropdowns to narrow results by **Action Type**. Each entry shows the action performed, a description, and the timestamp.

<!-- SCREENSHOT: screenshots/08-my-activity.png -->
> 📸 **Insert screenshot here:** `screenshots/08-my-activity.png`
> *(Activity log page with filter bar and a list of activity entries)*

---

### 4.8 Upload / Manage Your CV

You can attach a CV / résumé to your profile so that Module Organisers can download it when reviewing your application.

The CV section is located at the bottom of the **Profile** page (`Profile` → scroll down to *Resume / CV*).

**Uploading a CV**

1. Click the dashed file-picker area (labelled *Choose file…*).
2. Select a PDF, DOC, or DOCX file **no larger than 2 MB**.
3. The filename appears in the picker area to confirm your selection.
4. Click **"Upload CV"** (or **"Replace CV"** if you already have one on file).

A green success banner confirms the upload.

**Replacing an existing CV**

Follow the same upload steps above. The old file is automatically replaced by the new one.

**Deleting your CV**

If a CV is already on file, a **📄 Download** link and a **"Delete CV"** button are shown above the upload form:
1. Click **"Delete CV"**.
2. Confirm in the dialog that appears.

The CV is removed from your profile immediately.

Error messages:

| Message | Cause |
|---------|-------|
| *No file selected* | You clicked Upload without choosing a file |
| *File too large* | The chosen file exceeds the 2 MB limit |
| *Invalid file type* | Only PDF, DOC, and DOCX are accepted |

<!-- SCREENSHOT: screenshots/21-applicant-cv.png -->
> 📸 **Insert screenshot here:** `screenshots/21-applicant-cv.png`
> *(Profile page showing CV section with an uploaded CV, Download and Delete CV buttons, and the upload form below)*

---

## 5. Module Organiser (MO) Guide

### 5.1 Dashboard (MO)

After logging in as an MO you will see:

| Area | Description |
|------|-------------|
| **MO Panel card** | Total number of jobs you have posted; quick link to My Posted Jobs |
| **Create New Job card** | Quick link to the job creation form |
| **Recent Activity** | Last 5 actions; "View All" link for full log |

<!-- SCREENSHOT: screenshots/09-mo-dashboard.png -->
> 📸 **Insert screenshot here:** `screenshots/09-mo-dashboard.png`
> *(MO dashboard showing MO Panel and Create New Job cards)*

---

### 5.2 Create a New Job Posting

1. Click **"Create Job"** on the Dashboard or navigate to **My Jobs** → **"Create New Job"**.
2. Fill in the form:
   - **Job Title** (required)
   - **Module Name** (required)
   - **Description** (required)
   - **Required Skills** – comma-separated (optional)
   - **Hours per Week** – a positive integer (required)
3. Click **"Post Job"**.

The new job appears immediately in the Open Jobs list.

<!-- SCREENSHOT: screenshots/10-create-job.png -->
> 📸 **Insert screenshot here:** `screenshots/10-create-job.png`
> *(Create job form with all fields visible)*

---

### 5.3 My Posted Jobs

Navigate to **My Jobs** to see two sections:

- **Open Jobs** – accepting applications; actions: *View Applications*, *Edit*, *Mark as Completed*
- **Completed Jobs** – closed positions; actions: *View Applications*, *Reopen*

<!-- SCREENSHOT: screenshots/11-mo-jobs.png -->
> 📸 **Insert screenshot here:** `screenshots/11-mo-jobs.png`
> *(My Jobs page with Open Jobs and Completed Jobs tables)*

---

### 5.4 Review Applications

Click **"View Applications"** for any job. The page lists all applicants for that position, with:

- Applicant name and email
- Self-introduction text
- Declared skills
- Match score (%)
- Missing skills
- **CV** – a **📄 Download CV** button if the applicant has uploaded one; "—" otherwise
- Current application status

Applicants are ranked from highest to lowest match score to help you prioritise.

<!-- SCREENSHOT: screenshots/12-mo-applications.png -->
> 📸 **Insert screenshot here:** `screenshots/12-mo-applications.png`
> *(Applications review table with match scores and ranked candidates)*

---

### 5.5 Accept or Reject an Applicant

On the Applications page, use the **Status** dropdown in the Action column to change an application status to **Accepted** or **Rejected**, then click **"Update"**.

The applicant will receive a notification about the status change.

<!-- SCREENSHOT: screenshots/13-update-application-status.png -->
> 📸 **Insert screenshot here:** `screenshots/13-update-application-status.png`
> *(Application row with status dropdown set to Accepted and Update button)*

---

### 5.6 Mark a Job as Completed / Reopen

- **Mark as Completed**: on the Open Jobs table, click **"Mark as Completed"**. A confirmation dialog appears. Once confirmed, the job moves to the Completed section and no longer accepts new applications.
- **Reopen**: on the Completed Jobs table, click **"Reopen"**. The job returns to Open status and becomes visible to applicants again.

---

### 5.7 Edit a Job Posting

You can update the details of any of your **Open** jobs at any time before it is completed.

1. On the **My Jobs** page, click **"Edit"** next to the open job you want to modify.
2. The edit form pre-fills the current values for:
   - **Job Title** (required)
   - **Module Name** (required)
   - **Description** (required)
   - **Required Skills** – comma-separated (optional)
   - **Hours per Week** – positive integer (required)
3. Make your changes and click **"Save Changes"**.

Validation rules:
- All required fields must be non-empty.
- Hours must be a positive whole number.

On success you are redirected back to **My Jobs** with a confirmation message. The edit action is recorded in the system activity log.

> **Note:** Only **Open** jobs can be edited. Completed jobs are locked to preserve historical recruitment data.

<!-- SCREENSHOT: screenshots/19-mo-edit-job.png -->
> 📸 **Insert screenshot here:** `screenshots/19-mo-edit-job.png`
> *(Edit job form pre-filled with existing values, Save Changes button visible)*

---

## 6. Administrator Guide

### 6.1 Dashboard (Admin)

After logging in as an Admin you will see:

| Area | Description |
|------|-------------|
| **Admin Panel card** | Total applicant count in the system; quick link to Workload view |
| **User Management card** | Quick link to the user management page |
| **Recent Activity** | Last 5 system-wide actions |

<!-- SCREENSHOT: screenshots/14-admin-dashboard.png -->
> 📸 **Insert screenshot here:** `screenshots/14-admin-dashboard.png`
> *(Admin dashboard showing Admin Panel and User Management cards)*

---

### 6.2 Workload Overview

Navigate to **Workload** to see a table of every registered applicant with:

| Column | Description |
|--------|-------------|
| Applicant Name | Full name |
| Email | Contact email |
| Accepted Jobs Count | Number of accepted applications |
| Total Hours | Sum of weekly hours across accepted positions |
| Workload Status | **Normal** (≤ 10 hrs) or **Overloaded** (> 10 hrs) |

Overloaded applicants are highlighted with a red badge to draw immediate attention.

<!-- SCREENSHOT: screenshots/15-admin-workload.png -->
> 📸 **Insert screenshot here:** `screenshots/15-admin-workload.png`
> *(Workload table with at least one Overloaded row highlighted in red)*

---

### 6.3 User Management

Navigate to **Manage Users** to see all user accounts grouped by role (Applicant / MO / Admin).

Use the **Search by name** field to filter users.

<!-- SCREENSHOT: screenshots/16-admin-users.png -->
> 📸 **Insert screenshot here:** `screenshots/16-admin-users.png`
> *(User management page showing the user table with role badges)*

---

### 6.4 Create a New User Account

On the User Management page, click **"Create New User"** (or navigate to Admin → Create User):

1. Fill in Full Name, Username, Email, Password, and **Role** (Applicant / MO / Admin).
2. Click **"Create User"**.

> Only administrators can create MO and Admin accounts.

<!-- SCREENSHOT: screenshots/17-admin-create-user.png -->
> 📸 **Insert screenshot here:** `screenshots/17-admin-create-user.png`
> *(Create user form with Role dropdown visible)*

---

### 6.5 Delete a User Account

On the User Management page, click **"Delete"** next to the user you want to remove. Confirm in the dialog. The user's account is permanently removed.

> **Warning:** Deleting a user does not automatically remove their applications or job postings.

---

### 6.6 Activity Log (System-wide)

Navigate to **Activity** (Admin menu) to view all system events.

Filter by:
- **User Name** (substring match)
- **Action Type** (e.g. APPLY_JOB, UPDATE_APPLICATION_STATUS)
- **Role** (APPLICANT / MO / ADMIN)
- **Date range** (From / To)

Each entry shows: operator name, role, action type, description, before/after state, and timestamp.

<!-- SCREENSHOT: screenshots/18-admin-activity.png -->
> 📸 **Insert screenshot here:** `screenshots/18-admin-activity.png`
> *(Admin activity log page with filter bar and result table; state change arrows visible)*

---

### 6.7 All Applications Overview

Navigate to **All Applications** (Admin menu) to see a consolidated view of every application across the entire system.

The table shows:

| Column | Description |
|--------|-------------|
| **Application ID** | Unique identifier for the application |
| **Applicant** | Full name of the student who applied |
| **Job** | Title of the TA position applied for |
| **CV** | Download link if the applicant has uploaded a CV; "—" otherwise |
| **Status** | Current decision badge (Pending / Accepted / Rejected) |
| **Applied At** | Date and time the application was submitted |

This page gives administrators a bird's-eye view of recruitment activity and allows them to quickly spot any CV that needs review.

> **Note:** Admins can download an applicant's CV directly from this page by clicking the **📄 Download CV** button in the CV column.

<!-- SCREENSHOT: screenshots/20-admin-all-applications.png -->
> 📸 **Insert screenshot here:** `screenshots/20-admin-all-applications.png`
> *(All Applications table with CV download buttons visible in the CV column)*

---

## 7. Notifications

The system delivers in-app notifications to applicants when:
- An MO changes the status of their application (Accepted or Rejected).

Notifications appear at the top of the **Dashboard**:
- Unread notifications have a **blue left border** and blue background.
- Read notifications have a grey border.
- The red badge on the Notifications heading shows the unread count.

To dismiss a notification, click the **×** button on the right of each notification item.

---

## 8. Language Switch

The interface supports **English** and **中文 (Chinese)**. Click the language toggle button (labelled **中文** or **English**) at the top-right of the Login page and inside the application header to switch languages instantly without a page reload.

---

## 9. Troubleshooting

| Symptom | Likely Cause | Solution |
|---------|-------------|----------|
| "Invalid username or password" on login | Wrong credentials or wrong case | Check Caps Lock; ensure correct username |
| "Username is already taken" on register | Another account uses the same username | Choose a different username |
| "You have already applied for this job" | Duplicate application attempt | Go to My Applications to check your existing application |
| "Only pending applications can be withdrawn" | Attempting to withdraw after MO decision | Contact the MO if you need to discuss the outcome |
| Page shows "No jobs available" | No open positions at this time | Check back later or contact an MO |
| 500 / error page | Server or data file issue | Check Tomcat logs; ensure `WEB-INF/data/` is writable |
| Data appears stale after restart | Data files stored in deploy directory | The system reloads JSON files on every request; restart Tomcat |

---

*For further assistance, contact your assigned Teaching Assistant or the system administrator.*
