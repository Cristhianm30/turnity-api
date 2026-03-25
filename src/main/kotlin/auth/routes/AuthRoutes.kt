package app.turnity.auth.routes

import app.turnity.auth.model.LoginRequest
import app.turnity.auth.model.RefreshTokenRequest
import app.turnity.auth.model.RegisterRequest
import app.turnity.auth.service.AuthService
import app.turnity.auth.service.GoogleOAuthService
import app.turnity.shared.constants.AuthMessages
import app.turnity.shared.constants.OAuthConstants
import app.turnity.shared.model.ApiResponse
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

/**
 * Authentication routes for user registration, login, and token management.
 *
 * Provides endpoints for:
 * - User registration with email/password
 * - User login with credentials
 * - Token refresh for expired tokens
 * - Google OAuth authentication flow
 */
fun Route.authRoutes() {
    val authService by inject<AuthService>()
    val googleOAuthService by inject<GoogleOAuthService>()

    route("/auth") {
        /**
         * POST /auth/register
         *
         * Register a new user with email and password.
         *
         * Request Body:
         * - firstName (required): User's first name
         * - lastName (required): User's last name
         * - email (required): User's email address
         * - password (required): Password (minimum 8 characters)
         * - phone (optional): User's phone number
         * - preferredName (optional): User's preferred name
         *
         * Response (201 Created):
         * - token: JWT access token
         * - user: User information object
         *
         * Error Responses:
         * - 400 Bad Request: Validation error (invalid email, password too short, etc.)
         * - 409 Conflict: Email already registered
         */
        post("/register") {
            val request = call.receive<RegisterRequest>()
            val response = authService.register(request)
            call.respond(HttpStatusCode.Created, ApiResponse.ok(response))
        }

        /**
         * POST /auth/login
         *
         * Authenticate user with email and password.
         *
         * Request Body:
         * - email (required): User's email address
         * - password (required): User's password
         *
         * Response (200 OK):
         * - token: JWT access token
         * - user: Authenticated user information
         *
         * Error Responses:
         * - 400 Bad Request: Missing required fields
         * - 401 Unauthorized: Invalid email or password
         */
        post("/login") {
            val request = call.receive<LoginRequest>()
            val response = authService.login(request)
            call.respond(HttpStatusCode.OK, ApiResponse.ok(response))
        }

        /**
         * POST /auth/refresh
         *
         * Refresh an expired or expiring JWT token.
         *
         * Request Body:
         * - token (required): JWT token to refresh
         *
         * Response (200 OK):
         * - token: New JWT access token
         * - user: User information object
         *
         * Error Responses:
         * - 400 Bad Request: Token is required
         * - 401 Unauthorized: Invalid or expired token
         * - 404 Not Found: User not found
         */
        post("/refresh") {
            val request = call.receive<RefreshTokenRequest>()
            val response = authService.refresh(request)
            call.respond(HttpStatusCode.OK, ApiResponse.ok(response))
        }

        /**
         * Google OAuth 2.0 authentication flow
         */
        authenticate(OAuthConstants.GOOGLE_OAUTH_AUTH) {
            /**
             * GET /auth/google
             *
             * Initiate Google OAuth 2.0 authentication flow.
             * Redirects user to Google's authorization server.
             */
            get("/google") {
                // Redirect to Google OAuth (handled by Ktor auth plugin)
            }

            /**
             * GET /auth/google/callback
             *
             * Google OAuth 2.0 callback endpoint.
             * Called by Google after user grants permission.
             *
             * Query Parameters:
             * - code: Authorization code from Google
             * - state: State parameter for CSRF protection
             *
             * Response (200 OK):
             * - token: JWT access token
             * - user: User information (created or existing)
             *
             * Error Responses:
             * - 401 Unauthorized: Invalid authorization code
             * - 409 Conflict: Email already registered with different provider
             */
            get("/google/callback") {
                val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()
                    ?: throw IllegalStateException(AuthMessages.NO_OAUTH_PRINCIPAL)
                val profile = googleOAuthService.fetchProfile(principal.accessToken)
                val response = authService.loginOrRegisterWithGoogle(
                    googleId = profile.id,
                    email = profile.email,
                    firstName = profile.givenName,
                    lastName = profile.familyName,
                )
                call.respond(HttpStatusCode.OK, ApiResponse.ok(response))
            }
        }
    }
}
