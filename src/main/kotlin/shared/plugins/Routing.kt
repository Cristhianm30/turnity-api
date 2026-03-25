package app.turnity.shared.plugins

import app.turnity.auth.routes.authRoutes
import app.turnity.shared.constants.RouteMessages
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText(RouteMessages.ROOT_MESSAGE)
        }
        get("/health") {
            call.respondText(RouteMessages.HEALTH_UP)
        }
        
        // API Documentation endpoints
        get("/docs") {
            val docFile = File("API_DOCUMENTATION.md")
            if (docFile.exists()) {
                call.respondText(docFile.readText(), ContentType.Text.Plain)
            } else {
                call.respondText(
                    "API Documentation not found. See README.md for endpoint details.",
                    ContentType.Text.Plain,
                    HttpStatusCode.NotFound
                )
            }
        }
        
        authRoutes()
    }
}
