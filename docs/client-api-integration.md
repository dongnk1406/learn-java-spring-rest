# Frontend API integration guide

This document describes how a browser or mobile client should call the **Identity** Spring Boot service: base URL, authentication, response shapes, and endpoints.

## Base URL and OpenAPI

| Item | Value |
|------|--------|
| Default origin | `http://localhost:8080` |
| Context path | `/identity` |
| **API base** | `http://localhost:8080/identity` |
| Swagger UI | `http://localhost:8080/identity/swagger-ui/index.html` |
| OpenAPI JSON | `http://localhost:8080/identity/v3/api-docs` |

All paths below are relative to the API base (e.g. `POST /auth/login` → `http://localhost:8080/identity/auth/login`).

CORS is configured to allow common browser usage (`allowedOriginPattern("*")`, credentials allowed). For production, tighten origins on the server.

---

## Response envelope: `ApiResponse<T>`

Most successful responses use this JSON shape:

```json
{
  "code": 200,
  "message": "Request success",
  "data": { }
}
```

- **`code`**: HTTP-style numeric code (defaults to `200` on success in the handler).
- **`message`**: Human-readable message.
- **`data`**: Payload; type depends on the endpoint.

**Exceptions (no `ApiResponse` wrapper):**

- `GET /users/myInfo` returns a **UserResponse** object at the root.
- `GET /users/{userId}` and `PUT /users/{userId}` return a **User** entity at the root.
- `DELETE /users/{userId}` returns a plain **string** (e.g. `"User deleted successfully"`).

---

## Errors

The global handler returns `ApiResponse` with error information; HTTP status reflects the error (e.g. `400`, `401`, `403`, `404`, `500`).

```json
{
  "code": 1003,
  "message": "User not existed",
  "data": null
}
```

Common **business codes** (see `ErrorCode` in the backend):

| Code | Meaning | Typical HTTP status |
|------|---------|---------------------|
| 1001 | User already exists | 400 |
| 1002 | Uncategorized | 500 |
| 1003 | User not found | 404 |
| 1004 | Unauthenticated (bad credentials / invalid token) | 401 |
| 1005 | Forbidden (no permission) | 403 |

Validation failures (`@Valid` on request bodies) may return `code` 1002 with a field-related `message`.

**Frontend checklist:**

- Read **`response.status`** and **`body.code` / `body.message`**.
- On **401**, clear stored tokens and redirect to login.
- On **403**, show “not allowed” (missing role/authority).

---

## Authentication

### JWT bearer token

Protected routes expect:

```http
Authorization: Bearer <access_token>
```

The access token is a signed JWT (HS512) returned from **`POST /auth/login`** in `data.accessToken`.

### Public routes (no `Authorization` header)

Defined in `SecurityConfig` for **POST** only:

- `POST /users` — registration
- `POST /auth/login`
- `POST /auth/introspect`
- `POST /auth/logout`
- `POST /auth/refreshToken`

Everything else requires a valid JWT unless Spring Security is changed.

### Token contents (claims)

Relevant claims include:

- **`sub`**: username
- **`scope`**: space-separated list of `ROLE_<name>` and permission names (used for `@PreAuthorize` / `hasAuthority`)

### Login

**`POST /auth/login`**

Request:

```json
{
  "username": "admin",
  "password": "admin"
}
```

Response (`ApiResponse<AuthenticationResponse>`):

```json
{
  "code": 200,
  "message": "Request success",
  "data": {
    "authenticated": true,
    "accessToken": "<jwt>"
  }
}
```

Store `accessToken` securely (memory, `httpOnly` cookie if you add it, or secure storage on mobile). There is **no separate refresh token string** in the response: the same JWT is used for **refresh** within a longer window.

### Introspect

**`POST /auth/introspect`**

```json
{ "token": "<jwt>" }
```

Response:

```json
{
  "code": 200,
  "message": "Request success",
  "data": { "valid": true }
}
```

Use this to check validity before trusting a cached token (optional).

### Logout

**`POST /auth/logout`**

```json
{ "token": "<jwt>" }
```

The server records the token ID as invalidated; subsequent API calls with that JWT should fail with **401**.

### Refresh

**`POST /auth/refreshToken`**

```json
{ "token": "<current_jwt>" }
```

The server invalidates the old token and returns a **new** `accessToken` in `data`. Refresh eligibility is tied to **`jwt.refreshable-duration`** in `application.yml` (issued-at time + refresh window). Access expiry is controlled by **`jwt.valid-duration`**.

**Suggested client flow:**

1. After login, save `accessToken`.
2. Attach `Authorization: Bearer <accessToken>` to protected requests.
3. Before access expiry, call **`/auth/refreshToken`** with the current token and replace stored token with `data.accessToken`.
4. On logout, call **`/auth/logout`** and delete local tokens.

---

## Users

| Method | Path | Auth | Notes |
|--------|------|------|--------|
| POST | `/users` | Public | Body: `UserCreationRequest` |
| GET | `/users` | JWT | **Admin only** (`ROLE_ADMIN`) |
| GET | `/users/myInfo` | JWT | Current user; returns **UserResponse** at root |
| GET | `/users/{userId}` | JWT | Admin or owner (see `@PostAuthorize`) |
| PUT | `/users/{userId}` | JWT | Needs authority **`UPDATE_DATA`** |
| DELETE | `/users/{userId}` | JWT | Deletes user |

### `UserCreationRequest`

```json
{
  "username": "string (min 3)",
  "password": "string (min 8)",
  "firstName": "string",
  "lastName": "string",
  "dateOfBirth": "1990-05-15",
  "roles": ["USER", "ADMIN"]
}
```

`roles` must match **existing role names** in the database.

### `UserUpdateRequest`

```json
{
  "password": "string (min 8)",
  "firstName": "string",
  "lastName": "string",
  "dateOfBirth": "1990-05-15",
  "roles": ["USER"]
}
```

`dateOfBirth` is validated (e.g. age 16–100) when the `@DobConstraint` path is active.

### Security note on passwords in JSON

`GET /users/myInfo` maps **`UserResponse.password`** to the stored hash. Do not display it in the UI; prefer omitting it in a future API version.

---

## Roles

| Method | Path | Auth |
|--------|------|------|
| GET | `/roles` | JWT |
| POST | `/roles` | JWT |
| DELETE | `/roles/{role}` | JWT |

`{role}` is the role **name** (primary key).

**Create body (`RoleRequest`):**

```json
{
  "name": "EDITOR",
  "description": "Editor role",
  "permissions": ["READ_DATA", "UPDATE_DATA"]
}
```

`permissions` are permission **names** that must already exist.

---

## Permissions

| Method | Path | Auth |
|--------|------|------|
| GET | `/permissions` | JWT |
| POST | `/permissions` | JWT |
| DELETE | `/permissions/{permission}` | JWT |

**Create body (`PermissionRequest`):**

```json
{
  "name": "READ_DATA",
  "description": "Read data"
}
```

---

## Email

| Method | Path | Auth |
|--------|------|------|
| POST | `/email/send` | JWT |

**Body (`Email`):**

```json
{
  "to": "user@example.com",
  "subject": "Subject",
  "body": "Plain text body",
  "attachment": ""
}
```

SMTP must be configured under `spring.mail` in `application.yml`.

---

## NestJS proxy (demo)

| Method | Path | Auth |
|--------|------|------|
| GET | `/nestjs/health-check` | JWT |

Calls `http://localhost:3000/health-check` and returns wrapped data. Requires a NestJS app on port 3000.

---

## TypeScript-friendly types (optional)

```typescript
interface ApiResponse<T> {
  code: number;
  message: string;
  data: T | null;
}

interface AuthenticationResponse {
  authenticated: boolean;
  accessToken: string;
}

interface IntrospectResponse {
  valid: boolean;
}
```

---

## Postman

Import **`postman/Identity-Service.postman_collection.json`**. Set collection variable **`baseUrl`** if not using localhost. Run **Authentication → Login** first; the collection saves **`accessToken`** for bearer requests.

---

## Local run reminder

1. MySQL available and database `identity_service` (see `application.yml`).
2. `docker compose up -d` (if you use Docker for MySQL).
3. `mvn spring-boot:run` from the project root.

Profiles (`dev`, `staging`, `production`) may change datasource and secrets; align **`baseUrl`** with your deployment.
