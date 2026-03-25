package app.turnity.shared.plugins

import app.turnity.shared.constants.ApiMessages
import app.turnity.shared.exceptions.*
import app.turnity.shared.model.ApiResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<ValidationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ApiResponse.error<Unit>(cause.message ?: ApiMessages.BAD_REQUEST))
        }
        exception<AuthenticationException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized, ApiResponse.error<Unit>(cause.message ?: ApiMessages.UNAUTHORIZED))
        }
        exception<AuthorizationException> { call, cause ->
            call.respond(HttpStatusCode.Forbidden, ApiResponse.error<Unit>(cause.message ?: ApiMessages.FORBIDDEN))
        }
        exception<ConflictException> { call, cause ->
            call.respond(HttpStatusCode.Conflict, ApiResponse.error<Unit>(cause.message ?: ApiMessages.CONFLICT))
        }
        exception<NotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, ApiResponse.error<Unit>(cause.message ?: ApiMessages.NOT_FOUND))
        }
        exception<Throwable> { call, cause ->
            call.application.log.error(ApiMessages.UNHANDLED_EXCEPTION, cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                ApiResponse.error<Unit>(ApiMessages.INTERNAL_SERVER_ERROR),
            )
        }
    }
}
