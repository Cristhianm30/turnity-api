package app.turnity.shared.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*

/**
 * OpenAPI configuration for API documentation.
 * Currently disabled - use API_DOCUMENTATION.md instead for endpoint reference.
 * 
 * To enable OpenAPI with Swagger UI in the future:
 * 1. Generate openapi.json using a Gradle plugin (e.g., springdoc-openapi)
 * 2. Place generated file in src/main/resources/
 * 3. Uncomment the code below and enable in Application.kt
 */
fun Application.configureOpenAPI() {
    // routing {
    //     openAPI(path = "openapi.json")
    // }
}

fun Application.configureSwagger() {
    // Disabled: Requires openapi.json file to be generated
    // routing {
    //     swaggerUI(path = "swagger", swaggerFile = "openapi.json")
    // }
    
    // For now, use API_DOCUMENTATION.md for API reference
}
