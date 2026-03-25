package app.turnity.shared.constants

object ConfigDefaults {
    const val DEV_ACCESS_SECRET = "dev-access-secret"
    const val JWT_ISSUER = "turnity-local"
    const val JWT_AUDIENCE = "turnity-clients"
    const val JWT_REALM = "turnity"
    const val TOKEN_EXPIRATION_MINUTES = 15L
    const val GOOGLE_CLIENT_ID = "google-client-id"
    const val GOOGLE_CLIENT_SECRET = "google-client-secret"
    const val EMPTY = ""
}

object ConfigKeys {
    const val DATABASE_URL = "database.url"
    const val DATABASE_USER = "database.user"
    const val DATABASE_PASSWORD = "database.password"
    const val JWT_SECRET = "jwt.secret"
    const val JWT_ISSUER = "jwt.issuer"
    const val JWT_AUDIENCE = "jwt.audience"
    const val JWT_REALM = "jwt.realm"
    const val JWT_EXPIRATION_MINUTES = "jwt.tokenExpirationMinutes"
    const val OAUTH_GOOGLE_CALLBACK_URL = "oauth.google.callbackUrl"
    const val OAUTH_GOOGLE_CLIENT_ID = "oauth.google.clientId"
    const val OAUTH_GOOGLE_CLIENT_SECRET = "oauth.google.clientSecret"
}

object EnvKeys {
    const val DATABASE_URL = "DATABASE_URL"
    const val DATABASE_USER = "DATABASE_USER"
    const val DATABASE_PASSWORD = "DATABASE_PASSWORD"
}
