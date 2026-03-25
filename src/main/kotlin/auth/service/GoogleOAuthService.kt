package app.turnity.auth.service

import app.turnity.auth.model.GoogleProfile
import app.turnity.shared.constants.AuthMessages
import app.turnity.shared.constants.OAuthConstants
import app.turnity.shared.exceptions.AuthenticationException
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class GoogleOAuthService(
    private val httpClient: HttpClient,
) {
    suspend fun fetchProfile(accessToken: String): GoogleProfile {
        val responseText = httpClient.get(OAuthConstants.GOOGLE_USERINFO_URL) {
            header(HttpHeaders.Authorization, OAuthConstants.BEARER_PREFIX + accessToken)
        }.bodyAsText()

        val json = Json.parseToJsonElement(responseText).jsonObject
        val id = json["id"]?.jsonPrimitive?.content
            ?: throw AuthenticationException(AuthMessages.INVALID_GOOGLE_PROFILE_RESPONSE)
        val email = json["email"]?.jsonPrimitive?.content
            ?: throw AuthenticationException(AuthMessages.GOOGLE_ACCOUNT_HAS_NO_EMAIL)
        val givenName = json["given_name"]?.jsonPrimitive?.content ?: AuthMessages.DEFAULT_GOOGLE_GIVEN_NAME
        val familyName = json["family_name"]?.jsonPrimitive?.content ?: AuthMessages.DEFAULT_GOOGLE_FAMILY_NAME

        return GoogleProfile(
            id = id,
            email = email,
            givenName = givenName,
            familyName = familyName,
        )
    }
}
