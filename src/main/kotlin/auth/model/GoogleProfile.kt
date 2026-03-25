package app.turnity.auth.model

data class GoogleProfile(
    val id: String,
    val email: String,
    val givenName: String,
    val familyName: String,
)
