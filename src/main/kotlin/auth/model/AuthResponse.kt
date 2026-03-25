package app.turnity.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String?,
    val profilePhoto: String?,
    val googleId: String?,
    val loginMethod: String,
    val createdAt: String,
    val updatedAt: String,
    val preferredName: String?,
)

@Serializable
data class TokenResponse(
    val token: String,
)

@Serializable
data class AuthResponse(
    val token: TokenResponse,
    val user: UserResponse,
)
