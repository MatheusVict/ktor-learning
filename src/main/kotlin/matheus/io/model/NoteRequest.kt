package matheus.io.model

import kotlinx.serialization.Serializable

@Serializable
data class NoteRequest(
    val note: String
)
