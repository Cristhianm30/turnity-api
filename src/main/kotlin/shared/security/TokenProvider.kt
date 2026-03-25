package app.turnity.shared.security

import app.turnity.shared.constants.AppConstants
import app.turnity.shared.constants.AuthMessages
import app.turnity.shared.exceptions.AuthenticationException
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import java.util.Date
import java.util.UUID

data class JwtConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String,
    val tokenExpirationMinutes: Long,
)

class TokenProvider(
    private val config: JwtConfig,
) {
    fun createToken(userId: UUID): String {
        return createToken(userId.toString(), System.currentTimeMillis())
    }

    fun renewToken(token: String): String {
        val decoded = verifyToken(token)
        val userId = decoded.getClaim(AppConstants.CLAIM_USER_ID).asString()
            ?: throw AuthenticationException(AuthMessages.INVALID_TOKEN)
        return createTokenInternal(userId)
    }

    fun verifyToken(token: String): DecodedJWT {
        return try {
            JWT.require(Algorithm.HMAC256(config.secret))
                .withAudience(config.audience)
                .withIssuer(config.issuer)
                .build()
                .verify(token)
        } catch (_: Exception) {
            throw AuthenticationException(AuthMessages.INVALID_OR_EXPIRED_TOKEN)
        }
    }

    private fun createTokenInternal(userId: String): String {
        return createToken(userId, System.currentTimeMillis())
    }

    private fun createToken(userId: String, now: Long): String {
        val expirationMs = config.tokenExpirationMinutes * 60 * 1000
        return JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withClaim(AppConstants.CLAIM_USER_ID, userId)
            .withIssuedAt(Date(now))
            .withExpiresAt(Date(now + expirationMs))
            .sign(Algorithm.HMAC256(config.secret))
    }
}
