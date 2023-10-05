package matheus.io.model

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class UserCredentials(
    val username: String,
    val password: String
) {
    fun hashPassword(): String = BCrypt.hashpw(password, BCrypt.gensalt())

    fun isvalidPassword(hashedPassword: String): Boolean = BCrypt.checkpw(password, hashedPassword)
}
