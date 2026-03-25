package app.turnity.shared.constants

object HttpConstants {
    const val X_APP_HEADER = "X-App"
    const val APP_NAME = "hortensia"
}

object OAuthConstants {
    const val GOOGLE_PROVIDER_NAME = "google"
    const val GOOGLE_OAUTH_AUTH = "google-oauth"
    const val GOOGLE_AUTHORIZE_URL = "https://accounts.google.com/o/oauth2/auth"
    const val GOOGLE_ACCESS_TOKEN_URL = "https://accounts.google.com/o/oauth2/token"
    const val GOOGLE_USERINFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo"
    const val BEARER_PREFIX = "Bearer "
    const val OPENID_SCOPE = "openid"
    const val PROFILE_SCOPE = "profile"
    const val EMAIL_SCOPE = "email"
}
