package app.turnity.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phone: String? = null,
    val preferredName: String? = null,
) {
    companion object {
        const val DESCRIPTION = "User registration request"
        const val FIRST_NAME_DESC = "User first name"
        const val LAST_NAME_DESC = "User last name"
        const val EMAIL_DESC = "User email address"
        const val PASSWORD_DESC = "User password (minimum 8 characters)"
        const val PHONE_DESC = "User phone number (optional)"
        const val PREFERRED_NAME_DESC = "User preferred name (optional)"
    }
}

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
) {
    companion object {
        const val DESCRIPTION = "User login request"
        const val EMAIL_DESC = "User email address"
        const val PASSWORD_DESC = "User password"
    }
}

@Serializable
data class RefreshTokenRequest(
    val token: String,
) {
    companion object {
        const val DESCRIPTION = "Token refresh request"
        const val TOKEN_DESC = "JWT token to refresh"
    }
}
