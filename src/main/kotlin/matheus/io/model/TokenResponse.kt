package matheus.io.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String?
)
