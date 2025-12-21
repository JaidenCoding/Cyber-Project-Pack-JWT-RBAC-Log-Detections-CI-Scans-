# Secure API + JWT + RBAC + Audit Logging (Spring Boot)

This is a small, resume-ready security engineering project:
- JWT login (`/auth/login`)
- Role-Based Access Control (RBAC)
  - `ROLE_USER` can access `/api/**`
  - `ROLE_ADMIN` can access `/admin/**`
- Audit logging (JSON Lines) of authenticated requests to `logs/audit.log`
- Integration tests (MockMvc) for auth + authorization

## Quick start

**Prereqs:** Java 17+, Maven 3.9+

```bash
cd secure-api-audit
mvn test
mvn spring-boot:run
```

App runs on `http://localhost:8080`.

## Default users

- **user** / **password**  (ROLE_USER)
- **admin** / **password** (ROLE_ADMIN)

## Login + use token

```bash
curl -s http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"password"}'
```

Copy `token` from the response, then:

```bash
TOKEN="PASTE_TOKEN_HERE"
curl -s http://localhost:8080/api/hello -H "Authorization: Bearer $TOKEN"
```

Admin-only audit viewer:

```bash
ADMIN_TOKEN=$(curl -s http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}' | python -c "import sys, json; print(json.load(sys.stdin)['token'])")

curl -s http://localhost:8080/admin/audit -H "Authorization: Bearer $ADMIN_TOKEN" | head
```

## Audit format

Each line in `logs/audit.log` is JSON, e.g.
```json
{"ts":"2025-12-15T22:00:00Z","user":"admin","roles":["ROLE_ADMIN"],"method":"GET","path":"/admin/audit","ip":"127.0.0.1","status":200}
```

## Notes

- JWT secret is configured in `application.yml` for demo purposes.
  In real deployments, store secrets in a secret manager / env var.

  ## Example request flows

**User role denied admin access**
![User forbidden](../screenshots/secure-api/user-forbidden-admin.png)

**Admin role accessing audit endpoint**
![Admin audit access](../screenshots/secure-api/admin-audit-success.png)

