package app.turnity.shared.plugins

import io.ktor.openapi.OpenApiInfo
import io.ktor.server.application.*
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.*
import io.ktor.server.routing.openapi.OpenApiDocSource

private val documentInfo = OpenApiInfo(
    title = "Turnity API",
    version = "1.0.0",
    description = "Authentication and company coordination services for Turnity.",
)

private fun Application.openApiDocSource() = OpenApiDocSource.Routing {
    routingRoot.descendants()
}

fun Application.configureOpenAPI() {
    routing {
        openAPI(path = "openapi") {
            info = documentInfo
            source = openApiDocSource()
        }
    }
}

fun Application.configureSwagger() {
    routing {
        swaggerUI(path = "swagger") {
            info = documentInfo
            source = openApiDocSource()
        }
    }
}
