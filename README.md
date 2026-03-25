# Turnity API

A Kotlin + Ktor backend that exposes authentication flows, Google OAuth, and company coordination services with JWT security.

## Getting started

1. Copy the env template and fill in your database/jwt/google secrets:
   ```bash
   cp .env.example .env
   # edit .env with your credentials
   ```
2. Start the development server:
   ```bash
   ./run-dev.sh
   ```
3. Visit `http://localhost:8080/swagger` to explore the live OpenAPI sandbox or `http://localhost:8080/openapi` to download the generated spec.

## Highlights

- **Authentication**: register/login/refresh endpoints with hashed passwords (bcrypt) and JWT issuance.
- **Google OAuth**: trigger the OAuth dance (`/auth/google`) and handle callbacks (`/auth/google/callback`).
- **OpenAPI/Swagger**: auto-generated docs/webpack served via `ktor-server-openapi` + `ktor-server-swagger`.
- **Configuration**: environment-driven secrets (see `.env.example`) plus database setup via `DatabaseFactory`.

## Available commands

| Command | Description |
|---------|-------------|
| `./gradlew build` | Compile, run tests, and assemble artifacts. |
| `./gradlew test` | Run unit tests. |
| `./gradlew run` | Launch the Ktor server (requires `.env`). |
| `./gradlew runFatJar` | Build and run the fat JAR locally. |
| `./gradlew buildImage` | Build Docker image tarball. |
| `./run-dev.sh` | Convenience script for local dev with default env. |

## Routes

- `GET /` – root info.
- `GET /health` – readiness.
- `GET /docs` – legacy Markdown doc cache if still present.
- `POST /auth/register`, `/auth/login`, `/auth/refresh`, `/auth/google`, `/auth/google/callback` – authentication flows provided by `authRoutes`.

## OpenAPI & documentation

- OpenAPI spec is served at `GET /openapi` and is generated from routing metadata (compiler + inference).
- Swagger UI runs on `GET /swagger` and consumes the same runtime spec.
- Keep documentation in sync by commenting routes or relying on inference plus `OpenApi` plugin configuration in `build.gradle.kts`.

## Next steps

1. Run `./gradlew build` to verify the project compiles with the new OpenAPI metadata.
2. Access `/swagger` to confirm endpoint descriptions appear.
3. Add more route doc comments or runtime `.describe` blocks for richer specs in the future.
