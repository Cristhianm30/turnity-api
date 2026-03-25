package app.turnity.shared.plugins

import app.turnity.auth.routes.authRoutes
import app.turnity.shared.constants.RouteMessages
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText(RouteMessages.ROOT_MESSAGE)
        }
        get("/health") {
            call.respondText(RouteMessages.HEALTH_UP)
        }
        authRoutes()
    }
}
