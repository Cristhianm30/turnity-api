# Turnity API Documentation

## Overview

The Turnity API is a Kotlin + Ktor backend that provides user authentication, authorization, and profile management services. It supports both traditional email/password authentication and Google OAuth 2.0 integration.

**Base URL**: `http://localhost:8080/api`  
**Authentication**: JWT Bearer tokens  
**Content-Type**: `application/json`

---

## Authentication

### JWT Bearer Token

Most endpoints require authentication using a JWT Bearer token in the `Authorization` header:

```
Authorization: Bearer <your_jwt_token>
```

### Getting a Token

Tokens are obtained through:
1. **Email/Password Registration** - POST `/auth/register`
2. **Email/Password Login** - POST `/auth/login`
3. **Google OAuth 2.0** - GET `/auth/google` → `/auth/google/callback`
4. **Token Refresh** - POST `/auth/refresh`

---

## Authentication Endpoints

All authentication endpoints are under `/auth` prefix.

### 1. Register New User

**Endpoint**: `POST /auth/register`

**Description**: Register a new user with email and password.

**Request Body**:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "SecurePassword123",
  "phone": "+1-555-0123",
  "preferredName": "Johnny"
}
```

**Request Fields**:
| Field | Type | Required | Notes |
|-------|------|----------|-------|
| firstName | string | Yes | User's first name |
| lastName | string | Yes | User's last name |
| email | string | Yes | Valid email address, must be unique |
| password | string | Yes | Minimum 8 characters |
| phone | string | No | Phone number (optional) |
| preferredName | string | No | Preferred display name (optional) |

**Success Response** (201 Created):
```json
{
  "success": true,
  "data": {
    "token": {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    },
    "user": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "email": "john.doe@example.com",
      "firstName": "John",
      "lastName": "Doe",
      "preferredName": "Johnny",
      "phone": "+1-555-0123",
      "profilePhoto": null,
      "googleId": null,
      "loginMethod": "email_password",
      "createdAt": "2026-03-24T10:30:00Z",
      "updatedAt": "2026-03-24T10:30:00Z"
    }
  }
}
```

**Error Responses**:
| Status | Error Code | Message |
|--------|-----------|---------|
| 400 | INVALID_INPUT | Invalid email format / Password too short |
| 409 | CONFLICT | Email already registered |

---

### 2. Login User

**Endpoint**: `POST /auth/login`

**Description**: Authenticate user with email and password.

**Request Body**:
```json
{
  "email": "john.doe@example.com",
  "password": "SecurePassword123"
}
```

**Request Fields**:
| Field | Type | Required | Notes |
|-------|------|----------|-------|
| email | string | Yes | User's email address |
| password | string | Yes | User's password |

**Success Response** (200 OK):
```json
{
  "success": true,
  "data": {
    "token": {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    },
    "user": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "email": "john.doe@example.com",
      "firstName": "John",
      "lastName": "Doe",
      "preferredName": "Johnny",
      "phone": "+1-555-0123",
      "profilePhoto": null,
      "googleId": null,
      "loginMethod": "email_password",
      "createdAt": "2026-03-24T10:30:00Z",
      "updatedAt": "2026-03-24T10:30:00Z"
    }
  }
}
```

**Error Responses**:
| Status | Error Code | Message |
|--------|-----------|---------|
| 400 | INVALID_INPUT | Missing required fields |
| 401 | UNAUTHORIZED | Invalid email or password |

---

### 3. Refresh Token

**Endpoint**: `POST /auth/refresh`

**Description**: Refresh an expired or expiring JWT token.

**Request Body**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Request Fields**:
| Field | Type | Required | Notes |
|-------|------|----------|-------|
| token | string | Yes | Current JWT token to refresh |

**Success Response** (200 OK):
```json
{
  "success": true,
  "data": {
    "token": {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    },
    "user": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "email": "john.doe@example.com",
      "firstName": "John",
      "lastName": "Doe",
      "preferredName": "Johnny",
      "phone": "+1-555-0123",
      "profilePhoto": null,
      "googleId": null,
      "loginMethod": "email_password",
      "createdAt": "2026-03-24T10:30:00Z",
      "updatedAt": "2026-03-24T10:30:00Z"
    }
  }
}
```

**Error Responses**:
| Status | Error Code | Message |
|--------|-----------|---------|
| 400 | INVALID_INPUT | Token is required |
| 401 | UNAUTHORIZED | Invalid or expired token |
| 404 | NOT_FOUND | User not found |

---

### 4. Google OAuth 2.0 - Initiate Flow

**Endpoint**: `GET /auth/google`

**Description**: Initiate Google OAuth 2.0 authentication flow. Redirects user to Google's authorization server.

**Query Parameters**: None

**Response**: HTTP 302 Redirect to Google Authorization Endpoint

**Example Redirect URL**:
```
https://accounts.google.com/o/oauth2/v2/auth?
  client_id=YOUR_CLIENT_ID&
  redirect_uri=http://localhost:8080/auth/google/callback&
  response_type=code&
  scope=openid%20profile%20email&
  state=random_state_value
```

---

### 5. Google OAuth 2.0 - Callback

**Endpoint**: `GET /auth/google/callback`

**Description**: Google OAuth 2.0 callback endpoint. Called by Google after user grants permission. Automatically creates a new user or logs in existing user.

**Query Parameters**:
| Parameter | Type | Required | Notes |
|-----------|------|----------|-------|
| code | string | Yes | Authorization code from Google |
| state | string | Yes | State parameter for CSRF protection |

**Success Response** (200 OK):
```json
{
  "success": true,
  "data": {
    "token": {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    },
    "user": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "email": "john.doe@gmail.com",
      "firstName": "John",
      "lastName": "Doe",
      "preferredName": null,
      "phone": null,
      "profilePhoto": "https://lh3.googleusercontent.com/...",
      "googleId": "118364975550386953693",
      "loginMethod": "google_oauth",
      "createdAt": "2026-03-24T10:30:00Z",
      "updatedAt": "2026-03-24T10:30:00Z"
    }
  }
}
```

**Error Responses**:
| Status | Error Code | Message |
|--------|-----------|---------|
| 401 | UNAUTHORIZED | Invalid authorization code / OAuth flow failed |
| 409 | CONFLICT | Email already registered with different provider |

---

## API Response Format

### Success Response

All successful responses follow this envelope format:

```json
{
  "success": true,
  "data": {
    "token": {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    },
    "user": { ... }
  }
}
```

### Error Response

All error responses follow this envelope format:

```json
{
  "success": false,
  "error": "Error message describing what went wrong"
}
```

---

## HTTP Status Codes

| Code | Meaning | Usage |
|------|---------|-------|
| 200 | OK | Successful request |
| 201 | Created | Successful resource creation (registration) |
| 400 | Bad Request | Invalid input or missing required fields |
| 401 | Unauthorized | Invalid credentials or expired token |
| 404 | Not Found | Resource not found (user not found) |
| 409 | Conflict | Email already registered / Duplicate resource |
| 500 | Internal Server Error | Unexpected server error |

---

## JWT Token Claims

JWT tokens contain the following claims:

```json
{
  "sub": "550e8400-e29b-41d4-a716-446655440000",
  "iat": 1711270200,
  "exp": 1711356600,
  "iss": "turnity-api",
  "aud": "turnity-web"
}
```

**Claims**:
- `sub` (Subject): User ID (UUID)
- `iat` (Issued At): Token creation timestamp (Unix epoch)
- `exp` (Expiration): Token expiration timestamp (Unix epoch)
- `iss` (Issuer): API identifier
- `aud` (Audience): Intended recipient of token

---

## Database Schema

### Users Table

```sql
CREATE TABLE users (
  id UUID PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255),
  google_id VARCHAR(255) UNIQUE,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  preferred_name VARCHAR(255),
  phone VARCHAR(20),
  profile_photo TEXT,
  login_method VARCHAR(50) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## Configuration

The API requires the following environment variables:

```bash
# Server
LOCAL_PORT=8080

# Database
DATABASE_URL=jdbc:postgresql://localhost:5432/turnity
DATABASE_USER=postgres
DATABASE_PASSWORD=your_password

# JWT
JWT_SECRET=your_secret_key
JWT_ISSUER=turnity-api
JWT_AUDIENCE=turnity-web
JWT_REALM=turnity
JWT_EXPIRATION_MINUTES=60

# Google OAuth
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
GOOGLE_CALLBACK_URL=http://localhost:8080/auth/google/callback
```

---

## Error Handling

The API provides detailed error messages for debugging:

- **Validation Errors**: Return 400 with specific field validation messages
- **Authentication Errors**: Return 401 with clear auth failure reasons
- **Conflict Errors**: Return 409 when attempting duplicate operations
- **Not Found Errors**: Return 404 when requested resource doesn't exist
- **Server Errors**: Return 500 with generic message (details logged server-side)

Sensitive information (stack traces, internal queries) are never exposed to clients.

---

## Future Endpoints

The following endpoints are planned for future implementation:

- **User Profile Management**
  - GET `/profile` - Get authenticated user profile
  - PUT `/profile` - Update user profile
  - DELETE `/account` - Delete user account

- **Company Management**
  - GET `/companies` - List user's companies
  - POST `/companies` - Create new company
  - GET `/companies/:id` - Get company details
  - PUT `/companies/:id` - Update company
  - DELETE `/companies/:id` - Delete company

---

## Testing the API

### Using cURL

```bash
# Register a new user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "password": "SecurePassword123"
  }'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "SecurePassword123"
  }'

# Refresh token
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"token": "YOUR_JWT_TOKEN"}'
```

### Using Postman

1. Import the endpoints into Postman
2. Set `{{base_url}}` variable to `http://localhost:8080/api`
3. Set `{{token}}` variable after login/register for authenticated requests
4. Use pre-request scripts to automatically update `{{token}}` from responses

---

## Support

For issues or questions:
- Check the server logs for detailed error messages
- Review error responses for validation guidance
- Verify environment variables are correctly configured
- Ensure PostgreSQL database is running and accessible

