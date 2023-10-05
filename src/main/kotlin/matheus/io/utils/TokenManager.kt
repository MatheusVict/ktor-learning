package matheus.io.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.config.*
import matheus.io.model.User

class TokenManager(
    private val config: HoconApplicationConfig
) {
    fun generateJWTToken(user: User): String? {
        val audience = config.property("audience").toString()
        val secret = config.property("secret").toString()
        val issuer = config.property("issuer").toString()
        val expirationDate = System.currentTimeMillis() + 86400000

        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username", user.username)
            .withClaim("id", user.id)
            .withClaim("expirationDate", expirationDate)
            .sign(Algorithm.HMAC256(secret))


    }
}