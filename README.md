# Service Desk Backend

Spring Boot backend for a simple service desk: requesters create tickets, support agents process them, and admins manage users/categories and dashboard stats.

## Run dependencies

```powershell
docker compose up -d postgres redis
```

## Run the API with Docker

```powershell
docker compose --profile app up --build
```

API: `http://localhost:8080`

Swagger UI: `http://localhost:8080/swagger-ui.html`

## Demo accounts

All demo passwords are `password`.

- Admin: `admin@servicedesk.local`
- Agent: `agent@servicedesk.local`
- Requester: `user@servicedesk.local`

## Quick API check

Login:

```powershell
$login = Invoke-RestMethod -Method Post http://localhost:8080/api/auth/login `
  -ContentType "application/json" `
  -Body '{"email":"user@servicedesk.local","password":"password"}'
$token = $login.token
```

List categories:

```powershell
Invoke-RestMethod http://localhost:8080/api/categories `
  -Headers @{ Authorization = "Bearer $token" }
```

Create ticket:

```powershell
Invoke-RestMethod -Method Post http://localhost:8080/api/tickets `
  -Headers @{ Authorization = "Bearer $token" } `
  -ContentType "application/json" `
  -Body '{"title":"VPN does not connect","description":"The VPN client fails with timeout.","categoryId":3}'
```

Agent login and update ticket:

```powershell
$agentLogin = Invoke-RestMethod -Method Post http://localhost:8080/api/auth/login `
  -ContentType "application/json" `
  -Body '{"email":"agent@servicedesk.local","password":"password"}'
$agentToken = $agentLogin.token

Invoke-RestMethod -Method Patch http://localhost:8080/api/tickets/1 `
  -Headers @{ Authorization = "Bearer $agentToken" } `
  -ContentType "application/json" `
  -Body '{"status":"IN_PROGRESS","assignedAgentId":2}'
```
