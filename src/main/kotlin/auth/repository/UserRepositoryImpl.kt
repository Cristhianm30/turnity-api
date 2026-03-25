package app.turnity.auth.repository

import app.turnity.auth.model.User
import app.turnity.shared.database.querys.INSERT_USER
import app.turnity.shared.database.querys.SELECT_USER_BY_EMAIL
import app.turnity.shared.database.querys.SELECT_USER_BY_GOOGLE
import app.turnity.shared.database.querys.SELECT_USER_BY_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.ResultSet
import java.util.*

class UserRepositoryImpl(
    private val connection: Connection,
) : UserRepository {
    private val logger = LoggerFactory.getLogger(UserRepositoryImpl::class.java)

    override suspend fun createUser(
        firstName: String,
        lastName: String,
        email: String,
        phone: String?,
        password: String?,
        googleId: String?,
        loginMethod: String,
        preferredName: String?,
        profilePhoto: String?,
    ): User = withContext(Dispatchers.IO) {
        val uuid = UUID.randomUUID()
        val statement = connection.prepareStatement(
            INSERT_USER.trimIndent(),
        )
        statement.setObject(1, uuid)
        statement.setString(2, email)
        statement.setString(3, password)
        statement.setString(4, googleId)
        statement.setString(5, firstName)
        statement.setString(6, lastName)
        statement.setString(7, preferredName)
        statement.setString(8, phone)
        statement.setString(9, profilePhoto)
        statement.setString(10, loginMethod)
        statement.executeUpdate()
        logger.info("Created user {} with method {}", email, loginMethod)

        User(
            id = uuid,
            email = email,
            password = password,
            googleId = googleId,
            firstName = firstName,
            lastName = lastName,
            preferredName = preferredName,
            phone = phone,
            profilePhoto = profilePhoto,
            loginMethod = loginMethod,
            createdAt = java.time.Instant.now().toString(),
            updatedAt = java.time.Instant.now().toString(),
        )
    }

    override suspend fun findByEmail(email: String): User? = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(
            SELECT_USER_BY_EMAIL.trimIndent(),
        )
        statement.setString(1, email)
        val result = statement.executeQuery()
        if (!result.next()) {
            return@withContext null
        }
        mapRow(result)
    }

    override suspend fun findByGoogleId(googleId: String): User? = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(
            SELECT_USER_BY_GOOGLE.trimIndent(),
        )
        statement.setString(1, googleId)
        val result = statement.executeQuery()
        if (!result.next()) {
            return@withContext null
        }
        mapRow(result)
    }

    override suspend fun findById(id: UUID): User? = withContext(Dispatchers.IO) {
        val statement = connection.prepareStatement(
            SELECT_USER_BY_ID.trimIndent(),
        )
        statement.setObject(1, id)
        val result = statement.executeQuery()
        if (!result.next()) {
            return@withContext null
        }
        mapRow(result)
    }

    private fun mapRow(result: ResultSet): User {
        return User(
            id = result.getObject("id") as UUID,
            email = result.getString("email"),
            password = result.getString("password"),
            googleId = result.getString("google_id"),
            firstName = result.getString("first_name"),
            lastName = result.getString("last_name"),
            preferredName = result.getString("preferred_name"),
            phone = result.getString("phone"),
            profilePhoto = result.getString("profile_photo"),
            loginMethod = result.getString("login_method"),
            createdAt = result.getTimestamp("created_at").toInstant().toString(),
            updatedAt = result.getTimestamp("updated_at").toInstant().toString(),
        )
    }
}

