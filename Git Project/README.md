# Cyber Project Pack — JWT/RBAC API, Log Detections, and CI Security Scans

This repository contains multiple focused security projects demonstrating
application security, detection engineering, and DevSecOps fundamentals using Java and Spring Boot.

Each project is self-contained and can be run independently.

---

## Projects

### 1. Secure API with JWT Authentication, RBAC, and Audit Logging
📁 `Git Project/secure-api-audit`

Spring Boot REST API implementing core backend security controls.

**Features**
- JWT-based authentication
- Role-Based Access Control (RBAC)
- Audit logging of authenticated requests
- Hardened Spring Security filter chain

**Purpose**
Demonstrates how modern APIs enforce authorization and maintain security audit trails
for authentication and sensitive operations.

---

### 2. Rate Limiting & Anomaly Detection API
📁 `Git Project/rate-limit-anomaly-api`

Spring Boot API focused on abuse prevention and detection engineering.

**Features**
- IP-based rate limiting using a custom `OncePerRequestFilter`
- Automatic `429 Too Many Requests` enforcement
- Behavioral anomaly detection service
- Centralized exception handling for security violations
- Unit and integration tests validating abuse scenarios

**Why this exists**
This project simulates real-world defenses used to protect APIs from:
- Brute-force attacks
- Credential stuffing
- Automated bot traffic
- Excessive request abuse

---

### 3. Log Analyzer (Detection Engineering)
📁 `log-analyzer`

Java CLI tool that analyzes authentication logs to detect suspicious behavior.

**Detections**
- Brute-force and password spray activity
- First-seen IPs per user
- CSV output suitable for further analysis or SIEM ingestion

---

## 🔁 CI & Security Tooling
📁 `.github/workflows`

CI pipeline demonstrating:
- Automated testing
- Dependency vulnerability scanning
- Secure-by-default build practices

---

## ▶️ Running Locally

**Prerequisites**
- Java 17+
- Maven 3.9+

### Secure API
```bash
cd secure-api-audit
mvn test
mvn spring-boot:run
