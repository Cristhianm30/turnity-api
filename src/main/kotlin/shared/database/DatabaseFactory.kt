package app.turnity.shared.database

import app.turnity.shared.constants.ConfigKeys
import app.turnity.shared.constants.ConfigDefaults
import app.turnity.shared.constants.DatabaseConstants
import app.turnity.shared.constants.DatabaseMessages
import app.turnity.shared.constants.EnvKeys
import app.turnity.shared.database.querys.CREATE_USER_TABLE
import io.ktor.server.application.*
import java.sql.Connection
import java.sql.DriverManager

fun Application.createDatabaseConnection(): Connection {
    val url = environment.config.propertyOrNull(ConfigKeys.DATABASE_URL)?.getString()
        ?: System.getenv(EnvKeys.DATABASE_URL)
    val user = environment.config.propertyOrNull(ConfigKeys.DATABASE_USER)?.getString()
        ?: System.getenv(EnvKeys.DATABASE_USER)
    val password = environment.config.propertyOrNull(ConfigKeys.DATABASE_PASSWORD)?.getString()
        ?: System.getenv(EnvKeys.DATABASE_PASSWORD)
        ?: ConfigDefaults.EMPTY

    if (url == null || user == null) {
        error(DatabaseMessages.DATABASE_CONFIG_NOT_SET)
    }

    val driver = when {
        url.startsWith(DatabaseConstants.POSTGRES_URL_PREFIX) -> DatabaseConstants.POSTGRES_DRIVER
        url.startsWith(DatabaseConstants.H2_URL_PREFIX) -> DatabaseConstants.H2_DRIVER
        else -> error(DatabaseMessages.UNSUPPORTED_DATABASE_URL + url)
    }

    Class.forName(driver)
    log.info(DatabaseMessages.CONNECTING_TO_DATABASE, url)
    val connection = DriverManager.getConnection(url, user, password)

    initializeDatabaseSchema(connection)

    return connection
}

fun Application.initializeDatabaseSchema(connection: Connection) {
    try {
        connection.createStatement().use { statement ->
            statement.executeUpdate(CREATE_USER_TABLE.trimIndent())
        }
        log.info(DatabaseMessages.USERS_TABLE_ENSURED)
    } catch (e: Exception) {
        log.error(DatabaseMessages.FAILED_TO_INITIALIZE_SCHEMA, e)
        throw e
    }
}
