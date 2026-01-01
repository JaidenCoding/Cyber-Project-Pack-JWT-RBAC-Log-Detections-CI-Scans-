# Cyber Security Project Pack (Java / Spring Boot)

This repository is a collection of hands-on backend security project built in Java using Spring Boot.  
Each project focuses on a different part of real-world application security. Hopefully to expand on what you would expect to see in production systems.

The goal was to learn how security features are implemented, tested, and enforced, not just configured.

## Projects

### Secure API with JWT, RBAC, and Audit Logging  
 `Git Project/secure-api-audit`

This project focuses on authentication, authorization, and security logging.

I built a secured REST API using JWTs for stateless authentication and enforced access rules using role-based access control (RBAC). In addition, all authentication and sensitive API activity is logged to create an audit trail.

**What this project includes**
- JWT-based login and token validation  
- Role-based access control on protected endpoints  
- Centralized audit logging for authentication and API actions  
- Custom Spring Security filters  

**What it demonstrates**
- How stateless authentication works in practice  
- How roles are enforced at the API level  
- How security events can be logged for monitoring and investigations  

---

### Rate Limiting & Anomaly Detection API  
`Git Project/rate-limit-anomaly-api`

This project focuses on abuse prevention and detection, specifically protecting APIs from excessive or suspicious traffic.

I implemented a custom rate-limiting filter that tracks requests per IP and automatically blocks clients once limits are exceeded. The project also includes basic anomaly detection logic to flag unusual behavior patterns.

**What this project includes**
- IP-based rate limiting using a custom servlet filter  
- Automatic `429 Too Many Requests` responses  
- Centralized handling of rate-limit and security exceptions  
- Simple anomaly detection service  
- Unit and integration tests covering abuse scenarios  

**Why this matters**
These are the same types of protections used to reduce:
- Brute-force login attempts  
- Credential stuffing attacks  
- API scraping  
- Automated bot traffic  

---

## Testing & Validation

Both projects include tests to verify that security controls actually work as expected.

- JUnit 5 and MockMvc for API testing  
- Tests that confirm rate limits are enforced  
- Separate configuration profiles for test and runtime environments  

---

## CI & Security Tooling

The repository is structured to support automated security checks and CI workflows.

- Semgrep for static analysis  
- Gitleaks for secret detection  
- OWASP Dependency-Check  
- GitHub Actions–ready layout  

---

## Tech Stack

- Java 17  
- Spring Boot 3  
- Spring Security  
- Maven  
- JUnit 5  

---

## Purpose

I built these projects as a practical security portfolio to strengthen my understanding of backend and application security. They reflect the kinds of controls used in real systems and the tradeoffs involved in implementing them.

This work is relevant to roles in:
- Application Security  
- Backend / Platform Security  
- Security Engineering  
- Blue Team / Detection Engineering  
