package app.turnity.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String,
)

@Serializable
data class AuthResponse(
    val token: TokenResponse,
    val user: UserResponse,
)
