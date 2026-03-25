package app.turnity

import app.turnity.shared.di.configureDI
import app.turnity.shared.plugins.configureHTTP
import app.turnity.shared.plugins.configureMonitoring
import app.turnity.shared.plugins.configureOpenAPI
import app.turnity.shared.plugins.configureRouting
import app.turnity.shared.plugins.configureSecurity
import app.turnity.shared.plugins.configureSerialization
import app.turnity.shared.plugins.configureStatusPages
import app.turnity.shared.plugins.configureSwagger
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureDI()
    configureSerialization()
    configureMonitoring()
    configureStatusPages()
    configureSecurity()
    configureHTTP()
    configureOpenAPI()
    configureSwagger()
    configureRouting()
}
