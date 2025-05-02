# Medical Register Application

## Overview
This is a CRUD application to manage patient records in a medical register. The application allows users to create, read, update, and delete patient records.

## Features
- Full CRUD functionality for patient records
- LDAP Authentication using Auth0
- Logging and auditing support
- Unit and integration tests implemented
- CI/CD pipeline using GitHub Actions

## Tech Stack
- Java 17
- Spring Boot 3.x (JPA, Security, Thymeleaf)
- PostgreSQL
- Auth0 (Mock LDAP Integration)
- GitHub Actions for CI/CD

## Getting Started
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/medical-register.git
   cd medical-register
   ```

2. Configure the database connection in `application.properties`.

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. Access the application at `http://localhost:8080`.

## Running Tests
Run the following command to execute tests:
```bash
mvn test
```

## CI/CD
The application includes a GitHub Actions workflow for CI/CD. The workflow:
- Builds the application
- Runs tests
- Packages the artifact