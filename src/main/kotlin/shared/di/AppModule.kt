package app.turnity.shared.di

import app.turnity.auth.di.authModule
import app.turnity.auth.service.GoogleOAuthService
import app.turnity.shared.constants.ConfigDefaults
import app.turnity.shared.constants.ConfigKeys
import app.turnity.shared.constants.DatabaseMessages
import app.turnity.shared.database.createDatabaseConnection
import app.turnity.shared.security.JwtConfig
import app.turnity.shared.security.TokenProvider
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.server.application.*
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDI() {
    val jwtConfig = JwtConfig(
        secret = environment.config.propertyOrNull(ConfigKeys.JWT_SECRET)?.getString() ?: ConfigDefaults.DEV_ACCESS_SECRET,
        issuer = environment.config.propertyOrNull(ConfigKeys.JWT_ISSUER)?.getString() ?: ConfigDefaults.JWT_ISSUER,
        audience = environment.config.propertyOrNull(ConfigKeys.JWT_AUDIENCE)?.getString() ?: ConfigDefaults.JWT_AUDIENCE,
        realm = environment.config.propertyOrNull(ConfigKeys.JWT_REALM)?.getString() ?: ConfigDefaults.JWT_REALM,
        tokenExpirationMinutes = environment.config.propertyOrNull(ConfigKeys.JWT_EXPIRATION_MINUTES)?.getString()?.toLong()
            ?: ConfigDefaults.TOKEN_EXPIRATION_MINUTES,
    )

    val connection = createDatabaseConnection()

    install(Koin) {
        slf4jLogger()
        modules(coreModule(connection, jwtConfig), authModule)
    }

    environment.monitor.subscribe(ApplicationStopped) {
        connection.close()
        log.info(DatabaseMessages.DATABASE_CONNECTION_CLOSED)
    }
}

private fun coreModule(
    connection: java.sql.Connection,
    jwtConfig: JwtConfig,
): Module = module {
    single { connection }
    single { jwtConfig }
    single { TokenProvider(get()) }
    single { HttpClient(Apache) }
    single { GoogleOAuthService(get()) }
}
