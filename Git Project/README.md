# Cyber Project Pack — JWT/RBAC API, Log Detections, and CI Security Scans

This repository contains **three focused security projects** that demonstrate
application security, detection engineering, and DevSecOps fundamentals.

## Projects

1) **`secure-api-audit/`**  
   Spring Boot REST API with:
   - JWT authentication
   - Role-Based Access Control (RBAC)
   - Audit logging of authenticated requests

2) **`log-analyzer/`**  
   Java CLI tool that analyzes authentication logs and detects:
   - Brute-force / password spray activity
   - First-seen IPs per user
   - Outputs CSV reports suitable for further analysis

3) **`.github/workflows/`**  
   CI pipeline demonstrating:
   - Automated testing
   - Dependency vulnerability scanning
   - Secure-by-default build practices

Each project can be run independently.

---

## Run locally

**Prerequisites:** Java 17+, Maven 3.9+

### Secure API
```bash
cd secure-api-audit
mvn test
mvn spring-boot:run
