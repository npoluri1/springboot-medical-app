# Design Brief: Spring Boot Medical Record Application

## 1. Overview

This application is a Medical Record Management System built with **Spring Boot**, **Maven**, and **H2 Database** (for development). It exposes a RESTful API and a basic web UI for managing patient medical records, with robust auditing and logging. The system supports **OAuth 2.0** authentication for two roles—**admin** and **user1**—replacing LDAP for local development. Comprehensive service and controller layer tests are included. The frontend uses server-side rendering with XHTML/HTML templates.

---

## 2. Key Assumptions

- The application supports both RESTful and basic web-based (JSF/Thymeleaf-like) frontends.
- **MedicalRecord** is the primary entity, with CRUD operations.
- **Audit logging** is performed for every significant operation (CRUD and read).
- **OAuth 2.0** is the authentication mechanism for local development, with two users: `admin` (full access) and `user1` (restricted).
- LDAP integration is planned for production, but OAuth 2.0 simulates roles for local use.
- **Logging** uses standard Spring logging and a custom AuditService.
- H2 database is for local/dev, can migrate to a persistent RDBMS.
- CI/CD is set up through GitHub Actions.
- Image 1 indicates that the frontend uses server-side rendered templates (`.xhtml` and `.html`) for pages like home, form, error handling, and shared layout.

---

## 3. Integration Considerations

### 3.1 Authentication & Authorization

- **OAuth 2.0** replaces LDAP for local dev; two users: `admin` and `user1`.
- Fine-grained endpoint security based on roles.
- When deployed in production, LDAP can be re-enabled with minimal config changes.

### 3.2 Audit Logging

- All CRUD/read operations invoke `AuditService.audit(String user, String action, String details)`.
- Each log entry records:
    - Username (from OAuth context)
    - Operation (CRUD/read)
    - Timestamp
    - Record ID or details
- Audit logs are stored in a dedicated table for compliance and traceability.

### 3.3 Logging

- Uses SLF4J/Logback for system-level logs.
- All service and controller actions are logged.
- Error and exception handling is logged for troubleshooting.

### 3.4 Testing

- **Service layer tests**: Use Mockito for unit testing business logic and audit logging.
- **Controller integration tests**: Test the REST endpoints, authentication, and role-based access.
- Tests verify that:
    - All CRUD operations function as expected.
    - Audit logs are recorded for each operation.
    - Authorization restrictions are enforced.
- Tests are included in the codebase and run in CI/CD.

### 3.5 Frontend (see Image 1)

- Uses server-side rendered templates:
    - `/templates/layout/header.html` for shared layout.
    - `error.xhtml`, `form.xhtml`, `home.xhtml`, `index.xhtml` for main pages.
- Form submission, navigation, and error handling are managed via these templates.
- Frontend interacts with the backend via standard Spring MVC patterns.

### 3.6 CI/CD

- **GitHub Actions** pipeline:
    - Checks out code, sets up JDK, caches Maven, builds, runs tests, and uploads JAR artifacts.
    - Ensures all tests (including service and controller integration) pass before packaging.
    - Artifacts are available for download after each successful run.

---

## 4. Future Expansion Considerations

- **Production Database**: Migrate from H2 to PostgreSQL/MySQL.
- **LDAP Support**: Enable LDAP authentication for enterprise deployments.
- **Frontend**: Upgrade to a modern SPA (React/Vue) or enhance JSF/Thymeleaf templates.
- **API Gateway**: Add for routing, throttling, security.
- **User Management**: Integrate with enterprise IdP or add self-registration.
- **Advanced Reporting**: Dashboards for audit/compliance.
- **Notifications**: Email/SMS alerts for critical events.
- **Containerization**: Deploy with Docker/K8s for scalability.
- **Compliance**: Enhance audit for regulatory standards (e.g., HIPAA).

---

## 5. Assumptions & Uncertainties

- OAuth 2.0 is used locally instead of LDAP due to environment limitations.
- MedicalRecord and AuditLog entities use standard fields.
- Frontend is server-side rendered; no SPA yet.
- Test coverage includes both service and controller layers.
- No external system integrations yet (e.g., insurance, hospital EHR).

---

## 6. Summary

This application provides a secure, auditable, and extensible platform for managing medical records, with OAuth-based authentication (simulating LDAP), comprehensive testing, and both RESTful and web-based UIs. The architecture and CI/CD pipeline (via GitHub Actions) are ready for further expansion into a full-featured, enterprise-grade system.

---
**Image 1: Frontend Template Structure**
- `/templates/layout/header.html` (shared layout/header)
- `/templates/error.xhtml` (error page)
- `/templates/form.xhtml` (record form)
- `/templates/home.xhtml` (home/dashboard)
- `/templates/index.xhtml` (main landing page)