package matheus.io.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.config.*
import matheus.io.model.User
import java.util.*

class TokenManager(
    private val config: HoconApplicationConfig
) {

    private val audience = config.property("audience").toString()
    private val secret = config.property("secret").toString()
    private val issuer = config.property("issuer").toString()
    private val expirationDate = System.currentTimeMillis() + 86400000

    fun generateJWTToken(user: User): String? {


        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username", user.username)
            .withClaim("id", user.id)
            .withClaim("expirationDate", expirationDate)
            .withExpiresAt(Date(expirationDate))
            .sign(Algorithm.HMAC256(secret))


    }

    fun verifyJWTToken(): JWTVerifier =
        JWT.require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()

}