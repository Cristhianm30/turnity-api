# Turnity API

A modern REST API built with Kotlin + Ktor for user authentication, profile management, and company coordination.

**Status**: ✅ Core authentication system implemented and tested

## Quick Start

See **[GETTING_STARTED.md](GETTING_STARTED.md)** for detailed setup instructions.

```bash
# Copy environment template
cp .env.example .env

# Update .env with your PostgreSQL credentials

# Start the server
./run-dev.sh

# Access API documentation
curl http://localhost:8080/docs
```

## Documentation

- **[GETTING_STARTED.md](GETTING_STARTED.md)** - Setup and development guide
- **[API_DOCUMENTATION.md](API_DOCUMENTATION.md)** - Complete API reference with examples
- **[AGENTS.md](AGENTS.md)** - Development guidelines and code conventions

## Features

### ✅ Implemented

- **Email/Password Authentication**
  - User registration with email validation
  - Secure login with password hashing
  - JWT token generation and refresh
  - Token verification on protected routes

- **Google OAuth 2.0 Integration**
  - Initiate OAuth flow
  - Callback handling with automatic user creation
  - Seamless login or registration

- **User Management**
  - User profile with multiple fields (name, phone, profile photo)
  - Support for email/password and Google OAuth login methods
  - User data persistence in PostgreSQL with UUID primary keys

- **API Documentation**
  - Comprehensive endpoint documentation
  - Request/response examples
  - Error handling guide
  - Available at `/docs` endpoint

### 🔄 In Progress

- OpenAPI/Swagger UI auto-generation
- Company management endpoints
- User profile update endpoints
- Email verification

### 📋 Planned

- Two-factor authentication
- Role-based access control (RBAC)
- Invitation system
- Audit logging
- API rate limiting

## Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Language | Kotlin | 2.3.0 |
| Framework | Ktor | 3.4.1 |
| Database | PostgreSQL | 12+ |
| DI | Koin | 4.0.0 |
| Serialization | kotlinx.serialization | Latest |
| Auth | JWT (io.jsonwebtoken) | Latest |
| Build | Gradle | 9.3.0 |

## Project Structure

```
src/main/kotlin/
├── Application.kt                      # Entry point with plugin setup
├── auth/                               # Authentication module
│   ├── model/                          # Data models
│   ├── routes/                         # API endpoints
│   ├── service/                        # Business logic
│   ├── repository/                     # Database access
│   └── di/                             # Dependency injection
└── shared/                             # Shared utilities
    ├── constants/                      # Application constants
    ├── database/                       # Database setup
    ├── plugins/                        # Ktor plugins
    ├── security/                       # JWT/Auth config
    ├── exceptions/                     # Custom exceptions
    └── model/                          # Shared models
```

## Building & Running

### Requirements

- Java 21+
- PostgreSQL 12+
- Gradle (included via wrapper)

### Available Commands

| Command | Description |
|---------|-------------|
| `./gradlew test` | Run all tests |
| `./gradlew build` | Build everything with tests |
| `./gradlew check` | Run verification tasks |
| `./gradlew run` | Run development server (requires env vars) |
| `./gradlew buildFatJar` | Build standalone JAR for production |
| `./gradlew buildImage` | Build Docker image |
| `./run-dev.sh` | Run server with default dev environment |

### Running the Server

**Development (with built-in defaults)**:
```bash
./run-dev.sh
```

**Production (requires env vars)**:
```bash
# Set environment variables first
export DATABASE_URL=...
export DATABASE_USER=...
export DATABASE_PASSWORD=...
export JWT_SECRET=...
# ... other required variables

./gradlew run
```

**Docker**:
```bash
./gradlew buildImage
docker load -i build/image.tar
docker run -p 8080:8080 -e DATABASE_URL=... turnity-api:latest
```

## API Endpoints

### Health Check
```
GET /health
```

### Authentication

- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login with email/password
- `POST /api/auth/refresh` - Refresh JWT token
- `GET /api/auth/google` - Initiate Google OAuth
- `GET /api/auth/google/callback` - OAuth callback handler

### Documentation

- `GET /docs` - View API documentation (plain text)
- `GET /` - API information

See [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for detailed specifications.

## Configuration

Configure via environment variables (see `.env.example`):

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

# Google OAuth (optional)
GOOGLE_CLIENT_ID=your_client_id
GOOGLE_CLIENT_SECRET=your_secret
GOOGLE_CALLBACK_URL=http://localhost:8080/auth/google/callback
```

## Testing

### Run All Tests
```bash
./gradlew test
```

### Run Specific Test Class
```bash
./gradlew test --tests "AuthServiceTest"
```

### Test with cURL

```bash
# Health check
curl http://localhost:8080/health

# Register user
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
```

## Development

### Code Style

This project follows Kotlin official style guide:
- 4 spaces indentation
- Trailing commas in multiline declarations
- Expression bodies for simple functions
- Clear naming conventions (PascalCase for classes, camelCase for functions)

See [AGENTS.md](AGENTS.md) for detailed style guidelines.

### Making Changes

1. **Before coding** - Check [AGENTS.md](AGENTS.md) for conventions
2. **During coding** - Keep changes focused and isolated
3. **After coding** - Run `./gradlew build` to validate
4. **Testing** - Add tests for new features in `src/test/kotlin/`

### Git Workflow

```bash
# Create feature branch
git checkout -b feature/my-feature

# Make changes and test
./gradlew build

# Commit with clear message
git commit -m "Add feature X"

# Push and create PR
git push origin feature/my-feature
```

## Troubleshooting

### PostgreSQL Connection Failed

**Error**: `FATAL: password authentication failed for user "postgres"`

**Solution**: Update `DATABASE_PASSWORD` in `.env` with correct credentials.

### Port Already in Use

**Error**: `Port 8080 is already in use`

**Solution**: Change `LOCAL_PORT` in `.env` or kill the process using port 8080.

### Database Not Found

**Error**: `FATAL: database "turnity" does not exist`

**Solution**: The app creates tables automatically, but you may need to create the database:
```bash
# With Docker PostgreSQL
docker exec turnity-postgres psql -U postgres -c "CREATE DATABASE turnity;"
```

## CI/CD

GitHub Actions pipeline planned for:
- Automated testing on pull requests
- Code quality checks
- Docker image building and pushing
- Deployment to staging/production

## Contributing

1. Read [AGENTS.md](AGENTS.md) for development guidelines
2. Follow the code style conventions
3. Write tests for new features
4. Ensure all tests pass: `./gradlew build`
5. Create pull requests with clear descriptions

## Support

- **Issues**: Open a GitHub issue with details
- **Documentation**: Check GETTING_STARTED.md and API_DOCUMENTATION.md
- **Logs**: Check server logs for detailed error messages
- **Status**: Health check available at `/health`

## License

Proprietary - Turnity Project

## References

- [Ktor Documentation](https://ktor.io/docs/home.html)
- [PostgreSQL Docs](https://www.postgresql.org/docs/)
- [Kotlin Docs](https://kotlinlang.org/docs/home.html)
- [JWT.io](https://jwt.io/)
- [Google OAuth](https://developers.google.com/identity/protocols/oauth2)

