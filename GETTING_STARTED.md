# Getting Started with Turnity API

## Quick Start

### 1. Prerequisites

- **Java 21** or higher
- **PostgreSQL 12+** (running locally or accessible via network)
- **Gradle** (included via gradlew wrapper)

### 2. Environment Setup

The API requires environment variables for database credentials and JWT configuration.

**Option A: Using default development values (quick start)**

```bash
# Just run the script - it will use built-in defaults
./run-dev.sh
```

**Option B: Using a custom `.env` file (recommended)**

```bash
# Copy the example file
cp .env.example .env

# Edit .env with your PostgreSQL credentials
nano .env
# Update these lines with your actual values:
# DATABASE_URL=jdbc:postgresql://localhost:5432/turnity
# DATABASE_USER=postgres
# DATABASE_PASSWORD=your_password

# Start the server
./run-dev.sh
```

### 3. Environment Variables Reference

Create a `.env` file with these variables:

```bash
# Server Port
LOCAL_PORT=8080

# PostgreSQL Database
DATABASE_URL=jdbc:postgresql://localhost:5432/turnity
DATABASE_USER=postgres
DATABASE_PASSWORD=your_postgres_password

# JWT Configuration (any values work for development)
JWT_SECRET=my_super_secret_key_for_jwt_12345
JWT_ISSUER=turnity-api
JWT_AUDIENCE=turnity-web
JWT_REALM=turnity
JWT_EXPIRATION_MINUTES=60

# Google OAuth (optional - use dummy values for development)
GOOGLE_CLIENT_ID=dummy_client_id
GOOGLE_CLIENT_SECRET=dummy_client_secret
GOOGLE_CALLBACK_URL=http://localhost:8080/auth/google/callback
```

### 4. PostgreSQL Setup

If you don't have PostgreSQL running locally, start it with Docker:

```bash
# Create and start PostgreSQL container
docker run --name turnity-postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres:15

# Create the database (optional - app will create tables on startup)
docker exec turnity-postgres psql -U postgres -c "CREATE DATABASE turnity;"
```

### 5. Start the API Server

```bash
# Option 1: Using the convenience script (recommended)
./run-dev.sh

# Option 2: Direct Gradle command (if you set env vars)
./gradlew run

# Option 3: Build and run fat JAR
./gradlew buildFatJar
java -jar build/libs/turnity-api-all.jar
```

You should see output like:
```
2026-03-24 22:40:31.085 [main] INFO  Application - Connecting to database at jdbc:postgresql://localhost:5432/turnity
2026-03-24 22:40:32.000 [main] INFO  Application - Database connection successful
2026-03-24 22:40:32.500 [main] INFO  Application - Application started in 1.5 seconds.
2026-03-24 22:40:32.474 [main] INFO  Application - Responding at http://0.0.0.0:8080
```

### 6. Access API Documentation

Once the server is running, documentation is available at:

- **Plain Text API Docs**: `http://localhost:8080/docs`
- **Health Check**: `http://localhost:8080/health`
- **Root Info**: `http://localhost:8080/`

#### View Documentation

```bash
# View API documentation in terminal
curl http://localhost:8080/docs

# Or open in your browser
open http://localhost:8080/docs
```

The documentation file (`API_DOCUMENTATION.md`) contains:
- All endpoint specifications
- Request/response examples
- Error handling details
- HTTP status codes
- JWT token structure
- Database schema
- Testing examples with cURL

## Testing the API

### Register a New User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "password": "SecurePassword123"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "SecurePassword123"
  }'
```

### Check Health

```bash
curl http://localhost:8080/health
# Response: "API is up and running"
```

## Troubleshooting

### "Database connection refused"

**Problem**: `PSQLException: FATAL: password authentication failed for user "postgres"`

**Solution**: Update your PostgreSQL password in `.env`:
```bash
DATABASE_PASSWORD=your_actual_password
```

### "Required environment variable not found"

**Problem**: `ApplicationConfigurationException: Required environment variable "DATABASE_URL" not found`

**Solution**: The server needs environment variables. Either:
1. Create `.env` file with required values
2. Run `./run-dev.sh` which provides defaults
3. Export env vars before running: `export DATABASE_URL=...`

### "Failed to read OpenAPI document from file"

**Solution**: This is a known issue with Swagger UI configuration. Use the `/docs` endpoint instead for API documentation. OpenAPI auto-generation is planned for future releases.

### Port 8080 already in use

**Solution**: Either:
1. Kill the process using port 8080: `lsof -i :8080` then `kill -9 <PID>`
2. Change `LOCAL_PORT` in `.env` to a different port
3. Restart your system

## Development Workflow

### Run Tests

```bash
# Run all tests
./gradlew test

# Run tests for a specific class
./gradlew test --tests "AuthServiceTest"

# Run tests with specific pattern
./gradlew test --tests "*AuthTest"
```

### Build for Production

```bash
# Create optimized JAR
./gradlew buildFatJar

# Run the built JAR
java -jar build/libs/turnity-api-all.jar

# Or build Docker image
./gradlew buildImage
docker load -i build/image.tar
docker run -p 8080:8080 turnity-api:latest
```

### Check Code Quality

```bash
# Full build with all checks
./gradlew build

# Run only checks (no assembly)
./gradlew check

# List all available tasks
./gradlew tasks --all
```

## Project Structure

```
turnity-api/
├── src/
│   ├── main/kotlin/
│   │   ├── Application.kt              # Entry point
│   │   ├── auth/                       # Authentication module
│   │   │   ├── model/                  # Data models (User, requests, responses)
│   │   │   ├── routes/                 # API endpoints
│   │   │   ├── service/                # Business logic
│   │   │   ├── repository/             # Database access
│   │   │   └── di/                     # Dependency injection
│   │   └── shared/                     # Shared utilities
│   │       ├── constants/              # App-wide constants
│   │       ├── database/               # Database setup
│   │       ├── plugins/                # Ktor plugins
│   │       ├── security/               # JWT/Auth security
│   │       ├── exceptions/             # Error types
│   │       └── model/                  # Shared response models
│   ├── test/kotlin/                    # Tests
│   └── main/resources/
│       └── application.yaml            # Ktor configuration
├── build.gradle.kts                    # Gradle build config
├── gradle/libs.versions.toml           # Dependency versions
├── API_DOCUMENTATION.md                # Full API reference
├── GETTING_STARTED.md                  # This file
├── AGENTS.md                           # Development guidelines
├── .env.example                        # Environment template
└── run-dev.sh                          # Development startup script
```

## Next Steps

1. **Test the API** - Use cURL or Postman to test endpoints
2. **Read API_DOCUMENTATION.md** - Understand all available endpoints
3. **Check AGENTS.md** - Development guidelines and conventions
4. **Explore the code** - Start in `src/main/kotlin/auth/` to understand the auth flow
5. **Add new features** - Follow the existing patterns for new endpoints

## Additional Resources

- **Ktor Documentation**: https://ktor.io/docs/
- **PostgreSQL**: https://www.postgresql.org/docs/
- **JWT**: https://jwt.io/
- **Google OAuth**: https://developers.google.com/identity/protocols/oauth2

## Support

For issues:
1. Check the server logs - they contain detailed error messages
2. Read the error response from the API
3. Review API_DOCUMENTATION.md for endpoint details
4. Check that all environment variables are correctly set
5. Verify PostgreSQL is running and accessible

