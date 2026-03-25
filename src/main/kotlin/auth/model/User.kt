package app.turnity.auth.model

import kotlinx.serialization.Serializable
import java.util.UUID

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
    val preferredName: String?
)

data class User(
    val id: UUID,
    val email: String,
    val password: String?,
    val googleId: String?,
    val firstName: String,
    val lastName: String,
    val preferredName: String?,
    val phone: String?,
    val profilePhoto: String?,
    val loginMethod: String,
    val createdAt: String,
    val updatedAt: String,
)

fun User.toResponse(): UserResponse {
    return UserResponse(
        id = id.toString(),
        email = email,
        firstName = firstName,
        lastName = lastName,
        preferredName = preferredName,
        phone = phone,
        profilePhoto = profilePhoto,
        googleId = googleId,
        loginMethod = loginMethod,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
