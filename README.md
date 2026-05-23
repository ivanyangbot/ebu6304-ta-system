# EBU6304 Software Engineering Group Project

## Group 71

### Group Members

| Name | Student ID |
| --- | --- |
| Zhiyuan Yang | 231226509 |
| Lifeng Sun | 231226808 |
| Jinhong Cheng | 231226705 |
| Yucheng Jian | 231226554 |
| Fei Ye | 231226750 |
| Yibo Zhang | 231226369 |

### Project Overview

This project aims to develop a Teaching Assistant (TA) recruitment management system for the BUPT International School using Agile methodologies. The system streamlines the traditional paper-and-Excel workflow by providing core functions such as job posting, CV submission, application tracking, and AI-powered candidate matching to improve recruitment efficiency and workload balancing.

---

## Technology Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Web Framework | Java Servlet 4.0 + JSP / JSTL |
| Build Tool | Apache Maven 3.8+ |
| Server | Apache Tomcat 9.x / 10.x |
| Data Storage | JSON flat files (Gson 2.10) |
| Testing | JUnit 5.10 + Mockito 5.11 |

---

## Prerequisites

- **JDK 17** or later — [Download](https://adoptium.net/)
- **Apache Maven 3.8+** — [Download](https://maven.apache.org/download.cgi)
- **Apache Tomcat 9.x** — [Download](https://tomcat.apache.org/download-90.cgi)

Verify your environment:
```bash
java -version   # should print 17.x
mvn -version    # should print 3.8.x or later
```

---

## Build

Clone the repository and build the WAR file:

```bash
git clone https://github.com/ivanyangbot/ebu6304-ta-system.git
cd ebu6304-ta-system
mvn clean package -DskipTests
```

The compiled WAR is generated at:
```
target/ta-recruitment-system.war
```

---

## Deploy to Tomcat

1. Copy the WAR file into Tomcat's `webapps/` directory:
   ```bash
   cp target/ta-recruitment-system.war /path/to/tomcat/webapps/
   ```
2. Start Tomcat:
   ```bash
   /path/to/tomcat/bin/startup.sh      # macOS / Linux
   /path/to/tomcat/bin/startup.bat     # Windows
   ```
3. Open your browser and navigate to:
   ```
   http://localhost:8080/ta-recruitment-system/
   ```

> **Data files** are stored at `WEB-INF/data/` inside the deployed webapp directory. Tomcat must have write permission on that directory.

---

## Default Accounts

The system seeds the following accounts on first startup (see `DataBootstrapListener`):

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin` | `admin123` |
| Module Organiser | `mo1` | `mo123456` |
| Applicant | `alice` | `alice123` |
| Applicant | `bob` | `bob12345` |

---

## Run Tests

```bash
mvn test
```

Test classes are located in `src/test/java/` and cover the core service and utility layers using JUnit 5 and Mockito.

---

## Generate JavaDoc

```bash
mvn javadoc:javadoc -Dadditionalparam="-Xdoclint:none"
```

The HTML documentation is generated at:
```
target/reports/apidocs/index.html
```

---

## Project Structure

```
ebu6304-ta-system/
├── docs/
│   └── UserManual.md          # End-user guide
├── src/
│   ├── main/
│   │   ├── java/com/bupt/tarecruitment/
│   │   │   ├── listener/      # Servlet context lifecycle
│   │   │   ├── model/         # Domain entities (User, Job, Application…)
│   │   │   ├── repository/    # JSON file persistence layer
│   │   │   ├── service/       # Business logic (Auth, Match, Workload…)
│   │   │   ├── servlet/       # HTTP controllers
│   │   │   └── util/          # Utility helpers (Id, Skill, Workload…)
│   │   ├── resources/
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── data/      # JSON data files (auto-created)
│   │       │   ├── views/     # JSP page templates
│   │       │   └── web.xml    # Servlet / filter mappings
│   │       └── assets/        # CSS and JavaScript
│   └── test/
│       └── java/com/bupt/tarecruitment/
│           ├── service/       # Service layer unit tests
│           └── util/          # Utility unit tests
├── pom.xml
└── README.md
```

---

## User Roles

| Role | Capabilities |
|------|-------------|
| **Applicant** | Register, edit profile & skills, browse jobs, apply / withdraw, view notifications |
| **Module Organiser (MO)** | Post jobs, review ranked applicants, accept / reject applications, mark jobs complete |
| **Admin** | View workload overview, manage all user accounts, inspect system-wide activity log |

---

## Key Features

- **Skill-based matching** — computes a percentage match score and shows matched / missing skills for every application
- **Ranked candidate list** — MOs see applicants ordered by fit score (skill coverage + profile completeness bonus)
- **Workload guard** — Admin dashboard flags applicants whose total accepted hours exceed the threshold (10 hrs/week by default)
- **In-app notifications** — applicants are notified when their application status changes
- **Audit trail** — every significant action is logged with before/after state and timestamp
- **Bilingual UI** — English / Chinese toggle without page reload

---

## License

For academic use only — EBU6304 Software Engineering, BUPT International School, 2025–2026.
