package app.turnity.shared.plugins

import app.turnity.shared.constants.ConfigDefaults
import app.turnity.shared.constants.ConfigKeys
import app.turnity.shared.constants.OAuthConstants
import app.turnity.shared.constants.SecurityConstants.GOOGLE_CALLBACK
import app.turnity.shared.security.JwtConfig
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val jwtConfig by inject<JwtConfig>()
    val callbackUrl = environment.config.propertyOrNull(ConfigKeys.OAUTH_GOOGLE_CALLBACK_URL)?.getString()
        ?: GOOGLE_CALLBACK
    val clientId = environment.config.propertyOrNull(ConfigKeys.OAUTH_GOOGLE_CLIENT_ID)?.getString() ?: ConfigDefaults.GOOGLE_CLIENT_ID
    val clientSecret =
        environment.config.propertyOrNull(ConfigKeys.OAUTH_GOOGLE_CLIENT_SECRET)?.getString() ?: ConfigDefaults.GOOGLE_CLIENT_SECRET

    install(Authentication) {
        jwt("auth-jwt") {
            realm = jwtConfig.realm
            verifier(
                JWT.require(Algorithm.HMAC256(jwtConfig.secret))
                    .withAudience(jwtConfig.audience)
                    .withIssuer(jwtConfig.issuer)
                    .build(),
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtConfig.audience)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }

        oauth(OAuthConstants.GOOGLE_OAUTH_AUTH) {
            urlProvider = { callbackUrl }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = OAuthConstants.GOOGLE_PROVIDER_NAME,
                    authorizeUrl = OAuthConstants.GOOGLE_AUTHORIZE_URL,
                    accessTokenUrl = OAuthConstants.GOOGLE_ACCESS_TOKEN_URL,
                    requestMethod = HttpMethod.Post,
                    clientId = clientId,
                    clientSecret = clientSecret,
                    defaultScopes = listOf(
                        OAuthConstants.OPENID_SCOPE,
                        OAuthConstants.PROFILE_SCOPE,
                        OAuthConstants.EMAIL_SCOPE,
                    ),
                )
            }
            client = HttpClient(Apache)
        }
    }
}
