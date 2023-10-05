package matheus.io.routing

import com.typesafe.config.ConfigFactory
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import matheus.io.db.DatabaseConnection
import matheus.io.entities.UserEntity
import matheus.io.model.TokenResponse
import matheus.io.model.User
import matheus.io.model.UserCredentials
import matheus.io.utils.TokenManager
import org.ktorm.dsl.*

fun Application.authenticationRoutes() {
    val db = DatabaseConnection.database
    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))

    routing {
        post("/register") {
            val userCredentials = call.receive<UserCredentials>()

            val password = userCredentials.hashPassword()

            val user = db.from(UserEntity)
                .select()
                .where {
                    UserEntity.username eq userCredentials.username
                }.map {
                    it[UserEntity.username]
                }.firstOrNull()

            if (user != null) {
                call.respond(status = HttpStatusCode.Conflict, "User already exists")
                return@post
            }


            val result = db.insert(UserEntity) {
                set(it.username, userCredentials.username)
                set(it.password, password)
            }
            if (result > 0) {
                call.respond(status = HttpStatusCode.OK, "User created successfully ${userCredentials.username}")
            } else {
                call.respond(
                    status = HttpStatusCode.InternalServerError,
                    "Error creating user ${userCredentials.username}"
                )
            }
        }

        post("/login") {
            val db = DatabaseConnection.database

            val userCredentials = call.receive<UserCredentials>()

            val user = db.from(UserEntity)
                .select()
                .where {
                    UserEntity.username eq userCredentials.username
                }.map {
                    val username = it[UserEntity.username]
                    val id = it[UserEntity.id]
                    val password = it[UserEntity.password]
                    User(
                        id = id ?: -1,
                        username = username ?: "",
                        password = password ?: ""
                    )
                }.firstOrNull()

            if (user == null) {
                call.respond(status = HttpStatusCode.NotFound, "User not found")
                return@post
            }

            if (!userCredentials.isvalidPassword(user.password)) {
                call.respond(status = HttpStatusCode.Unauthorized, "Invalid password")
                return@post
            }

            val token = tokenManager.generateJWTToken(user)

            call.respond(status = OK, TokenResponse(token) ?: "")
        }

        authenticate {
            get("/me") {
                val principal = call.principal<Principal>()
                call.respond("$principal" ?: "")
            }
        }
    }
}