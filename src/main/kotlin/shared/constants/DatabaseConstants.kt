package app.turnity.shared.constants

object DatabaseConstants {
    const val POSTGRES_URL_PREFIX = "jdbc:postgresql://"
    const val H2_URL_PREFIX = "jdbc:h2:"
    const val POSTGRES_DRIVER = "org.postgresql.Driver"
    const val H2_DRIVER = "org.h2.Driver"
}

object DatabaseMessages {
    const val DATABASE_CONFIG_NOT_SET =
        "Database config is not set. Provide DATABASE_URL and DATABASE_USER via environment or config"
    const val UNSUPPORTED_DATABASE_URL = "Unsupported database URL: "
    const val CONNECTING_TO_DATABASE = "Connecting to database at {}"
    const val USERS_TABLE_ENSURED = "Ensured users table exists"
    const val FAILED_TO_INITIALIZE_SCHEMA = "Failed to initialize database schema"
    const val DATABASE_CONNECTION_CLOSED = "Database connection closed"
}
