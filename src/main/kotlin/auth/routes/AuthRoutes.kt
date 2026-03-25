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

fun Route.authRoutes() {
    val authService by inject<AuthService>()
    val googleOAuthService by inject<GoogleOAuthService>()

    route("/auth") {
        post("/register") {
            val request = call.receive<RegisterRequest>()
            val response = authService.register(request)
            call.respond(HttpStatusCode.Created, ApiResponse.ok(response))
        }

        post("/login") {
            val request = call.receive<LoginRequest>()
            val response = authService.login(request)
            call.respond(HttpStatusCode.OK, ApiResponse.ok(response))
        }

        post("/refresh") {
            val request = call.receive<RefreshTokenRequest>()
            val response = authService.refresh(request)
            call.respond(HttpStatusCode.OK, ApiResponse.ok(response))
        }

        authenticate(OAuthConstants.GOOGLE_OAUTH_AUTH) {
            get("/google") {
            }

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
