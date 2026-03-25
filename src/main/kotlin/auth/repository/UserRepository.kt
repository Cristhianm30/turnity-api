package app.turnity.auth.repository

import app.turnity.auth.model.User
import java.util.UUID

interface UserRepository {
    suspend fun createUser(
        firstName: String,
        lastName: String,
        email: String,
        phone: String?,
        password: String?,
        googleId: String?,
        loginMethod: String,
        preferredName: String? = null,
        profilePhoto: String? = null,
    ): User

    suspend fun findByEmail(email: String): User?

    suspend fun findByGoogleId(googleId: String): User?

    suspend fun findById(id: UUID): User?
}
