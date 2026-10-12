# Spring Boot Full Stack Authentication System

A complete starter authentication app with registration, login, forgot password, password reset, server-side Bean Validation, client-side validation, BCrypt password hashing, Spring Security session login and CSRF protection, and MySQL persistence.

## Requirements
- Java 17 or newer
- Maven 3.9+
- MySQL 8+
- An SMTP server (Mailpit is convenient for local testing)

## Setup
1. Start MySQL and create a database and database user (or let JDBC create the database if the configured user has permission):

   ```sql
   CREATE DATABASE auth_system;
   CREATE USER 'authapp'@'localhost' IDENTIFIED BY 'change-this-local-password';
   GRANT ALL PRIVILEGES ON auth_system.* TO 'authapp'@'localhost';
   ```

2. Set environment variables before launching (replace the examples):

   ```bash
   export DB_USER=authapp
   export DB_PASSWORD='change-this-local-password'
   export DB_URL='jdbc:mysql://localhost:3306/auth_system'
   export APP_BASE_URL='http://localhost:8080'
   ```

   On Windows PowerShell use `$env:DB_USER="authapp"` and similar assignments.

3. For **local email testing**, run Mailpit (for example via Docker):

   ```bash
   docker run --rm -p 1025:1025 -p 8025:8025 axllent/mailpit
   ```

   Default app configuration sends mail to `localhost:1025` with no authentication. Open http://localhost:8025 to read the messages. To send real email, set MAIL_HOST, MAIL_PORT, MAIL_USERNAME, MAIL_PASSWORD, MAIL_FROM, MAIL_AUTH=true, MAIL_STARTTLS=true to your SMTP provider's values.

4. Run:

   ```bash
   mvn spring-boot:run
   ```

5. Open http://localhost:8080/register, register an account, sign in at /login, visit /dashboard, and test password reset at /forgot. Reset links expire after 15 minutes and are single-use.

## Notes
- Database tables are generated automatically by Hibernate for development (`ddl-auto=update`). Use migrations such as Flyway in production.
- CSRF tokens are inserted into Thymeleaf POST forms by Spring Security; do not disable CSRF for session-authenticated forms.
- The forgot-password response intentionally does not disclose whether an email is registered.
- Password reset tokens are generated with SecureRandom and stored only as SHA-256 hashes.
- Configure HTTPS, `server.servlet.session.cookie.secure=true`, SMTP, email-delivery monitoring, rate limiting on login/register/forgot-password, and account protection policies before public deployment.
- Password resets do not automatically revoke already active sessions; add session revocation for high-security deployments.
- No claim is made that this project has been integration-tested against your particular MySQL/SMTP environment.
