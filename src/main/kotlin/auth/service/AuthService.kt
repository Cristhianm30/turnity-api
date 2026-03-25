package app.turnity.auth.service

import app.turnity.auth.model.*
import app.turnity.auth.repository.UserRepository
import app.turnity.shared.constants.AppConstants
import app.turnity.shared.constants.AuthLogMessages
import app.turnity.shared.constants.AuthMessages
import app.turnity.shared.exceptions.AuthenticationException
import app.turnity.shared.exceptions.ConflictException
import app.turnity.shared.exceptions.NotFoundException
import app.turnity.shared.exceptions.ValidationException
import app.turnity.shared.security.TokenProvider
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory
import java.util.UUID

class AuthService(
    private val userRepository: UserRepository,
    private val tokenProvider: TokenProvider,
) {
    private val logger = LoggerFactory.getLogger(AuthService::class.java)

    suspend fun register(request: RegisterRequest): AuthResponse {
        validateRegister(request)
        val existing = userRepository.findByEmail(request.email)
        if (existing != null) {
            throw ConflictException(AuthMessages.EMAIL_ALREADY_REGISTERED)
        }

        val password = BCrypt.hashpw(request.password, BCrypt.gensalt(AppConstants.BCRYPT_ROUNDS))
        val user = userRepository.createUser(
            firstName = request.firstName.trim(),
            lastName = request.lastName.trim(),
            email = request.email.trim().lowercase(),
            phone = request.phone?.trim(),
            password = password,
            googleId = null,
            loginMethod = AppConstants.LOGIN_METHOD_EMAIL,
            preferredName = request.preferredName?.trim(),
        )

        val token = tokenProvider.createToken(user.id)
        logger.info(AuthLogMessages.USER_REGISTERED, user.email)
        return AuthResponse(
            token = TokenResponse(token),
            user = user.toResponse(),
        )
    }

    suspend fun login(request: LoginRequest): AuthResponse {
        if (request.email.isBlank() || request.password.isBlank()) {
            throw ValidationException(AuthMessages.EMAIL_AND_PASSWORD_REQUIRED)
        }

        val user = userRepository.findByEmail(request.email.trim().lowercase())
            ?: throw AuthenticationException(AuthMessages.INVALID_CREDENTIALS)

        val hash = user.password ?: throw AuthenticationException(AuthMessages.INVALID_CREDENTIALS)
        if (!BCrypt.checkpw(request.password, hash)) {
            throw AuthenticationException(AuthMessages.INVALID_CREDENTIALS)
        }

        val token = tokenProvider.createToken(user.id)
        logger.info(AuthLogMessages.USER_LOGGED_IN, user.email)
        return AuthResponse(
            token = TokenResponse(token),
            user = user.toResponse(),
        )
    }

    suspend fun refresh(request: RefreshTokenRequest): AuthResponse {
        if (request.token.isBlank()) {
            throw ValidationException(AuthMessages.TOKEN_REQUIRED)
        }

        val decoded = tokenProvider.verifyToken(request.token)
        val userId = decoded.getClaim(AppConstants.CLAIM_USER_ID).asString()
            ?: throw AuthenticationException(AuthMessages.INVALID_TOKEN)

        val user = userRepository.findById(UUID.fromString(userId))
            ?: throw NotFoundException(AuthMessages.USER_NOT_FOUND)

        val renewedToken = tokenProvider.renewToken(request.token)
        logger.info(AuthLogMessages.TOKEN_RENEWED, user.id)
        return AuthResponse(
            token = TokenResponse(renewedToken),
            user = user.toResponse(),
        )
    }

    suspend fun loginOrRegisterWithGoogle(
        googleId: String,
        email: String,
        firstName: String,
        lastName: String,
    ): AuthResponse {
        val userByGoogle = userRepository.findByGoogleId(googleId)
        val user = userByGoogle ?: run {
            val existingByEmail = userRepository.findByEmail(email.trim().lowercase())
            if (existingByEmail != null) {
                throw ConflictException(AuthMessages.ACCOUNT_WITH_EMAIL_ALREADY_EXISTS)
            }
            userRepository.createUser(
                firstName = firstName.trim(),
                lastName = lastName.trim(),
                email = email.trim().lowercase(),
                phone = null,
                password = null,
                googleId = googleId,
                loginMethod = AppConstants.LOGIN_METHOD_GOOGLE,
                preferredName = null
            )
        }

        val token = tokenProvider.createToken(user.id)
        logger.info(AuthLogMessages.GOOGLE_OAUTH_LOGIN, user.email)
        return AuthResponse(
            token = TokenResponse(token),
            user = user.toResponse(),
        )
    }

    private fun validateRegister(request: RegisterRequest) {
        if (request.firstName.isBlank()) throw ValidationException(AuthMessages.FIRST_NAME_REQUIRED)
        if (request.lastName.isBlank()) throw ValidationException(AuthMessages.LAST_NAME_REQUIRED)
        if (request.email.isBlank()) throw ValidationException(AuthMessages.EMAIL_REQUIRED)
        if (!request.email.contains("@")) throw ValidationException(AuthMessages.EMAIL_FORMAT_INVALID)
        if (request.password.length < 8) throw ValidationException(AuthMessages.PASSWORD_MIN_LENGTH)
    }
}
