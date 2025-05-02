# Design Brief: Spring Boot Medical Record Application

## 1. Overview

This application is a Medical Record Management System built using **Spring Boot**, **Maven**, and **H2 Database** for backend persistence. It provides a RESTful API for managing patient medical records with robust auditing and logging features. The application supports **OAuth 2.0** authentication and authorization, supporting two user roles: **admin** and **user1**. All changes to records are logged for compliance and traceability.

---

## 2. Key Assumptions

- The application is a backend API (no frontend/UI).
- **MedicalRecord** is the primary domain entity, supporting CRUD operations (Create, Read, Update, Delete).
- **Audit logging** is performed for every modification or access to records.
- **OAuth 2.0** is the authentication mechanism, with two users: `admin` (full access) and `user1` (limited access).
- Logging is implemented using Spring’s logging facilities and a custom **AuditService**.
- H2 in-memory database is used for development; can be switched to a persistent RDBMS later.
- Application is containerizable and ready for CI/CD with GitHub Actions.

---

## 3. Integration Considerations

### 3.1 Authentication & Authorization

- **OAuth 2.0** is used for API security.
- Two users exist:
    - **admin**: Can perform all CRUD operations on medical records.
    - **user1**: Can perform read operations and (optionally) limited write operations as configured.
- Security configuration restricts endpoints based on user roles.

### 3.2 Audit Logging

- All operations (Create, Read, Update, Delete) are logged via an **AuditService**.
- Each audit log records:
    - User performing the action (from OAuth context)
    - Operation type (CRUD)
    - Timestamp
    - Record identifier
    - Additional metadata as needed
- Audit logs are persisted in the database for compliance.

### 3.3 Logging

- Standard application events and errors are logged using **SLF4J/Logback**.
- Logs can be extended to external services in production (e.g., ELK stack).

### 3.4 CI/CD

- **GitHub Actions** automate build, test, and package processes.
- Artifacts (JARs) are uploaded after each successful pipeline run.
- The workflow can be extended to deploy to cloud environments or Docker registries.

---

## 4. Future Expansion Considerations

- **Database**: Move from H2 to PostgreSQL or MySQL for production.
- **User Management**: Integrate with enterprise identity providers or add self-registration.
- **API Gateway**: Introduce API gateways for routing, throttling, and security.
- **Frontend**: Add a React/Angular frontend for user interaction.
- **Reporting**: Advanced analytics and reporting on medical records and audit logs.
- **Notification System**: Email/SMS alerts for critical changes or accesses.
- **Scalability**: Deploy using Kubernetes for horizontal scaling.
- **Compliance**: Enhance audit logs to support legal and regulatory requirements (e.g., HIPAA).

---

## 5. Assumptions & Uncertainties

- The current OAuth 2.0 configuration is basic and assumes a static set of users.
- Detailed schemas for MedicalRecord and AuditLog are assumed based on standard fields.
- No frontend/UI is included at this stage.
- Integration with external systems (e.g., hospitals, insurance) is not in scope but can be added.
- Email and notification features are not implemented but planned as future enhancements.

---

## 6. Summary

This application provides a secure, auditable, and extensible backend for managing medical records. With OAuth 2.0 security, detailed audit logging, and CI/CD readiness, it is suitable for further development and expansion into a full-fledged healthcare records platform.