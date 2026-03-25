package app.turnity.shared.constants

object AuthMessages {
    const val EMAIL_ALREADY_REGISTERED = "Email already registered"
    const val EMAIL_AND_PASSWORD_REQUIRED = "Email and password are required"
    const val INVALID_CREDENTIALS = "Invalid credentials"
    const val TOKEN_REQUIRED = "token is required"
    const val INVALID_TOKEN = "Invalid token"
    const val INVALID_OR_EXPIRED_TOKEN = "Invalid or expired token"
    const val USER_NOT_FOUND = "User not found"
    const val ACCOUNT_WITH_EMAIL_ALREADY_EXISTS = "An account with this email already exists"
    const val FIRST_NAME_REQUIRED = "firstName is required"
    const val LAST_NAME_REQUIRED = "lastName is required"
    const val EMAIL_REQUIRED = "email is required"
    const val EMAIL_FORMAT_INVALID = "email format is invalid"
    const val PASSWORD_MIN_LENGTH = "password must be at least 8 characters"
    const val NO_OAUTH_PRINCIPAL = "No OAuth principal"
    const val INVALID_GOOGLE_PROFILE_RESPONSE = "Invalid Google profile response"
    const val GOOGLE_ACCOUNT_HAS_NO_EMAIL = "Google account has no email"
    const val DEFAULT_GOOGLE_GIVEN_NAME = "Google"
    const val DEFAULT_GOOGLE_FAMILY_NAME = "User"
}

object AuthLogMessages {
    const val USER_REGISTERED = "User registered: {}"
    const val USER_LOGGED_IN = "User logged in: {}"
    const val TOKEN_RENEWED = "Token renewed for userId={}"
    const val GOOGLE_OAUTH_LOGIN = "Google OAuth login: {}"
}
