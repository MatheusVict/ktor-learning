package matheus.io.routing

import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import matheus.io.db.DatabaseConnection
import matheus.io.entities.UserEntity
import matheus.io.model.UserCredentials
import org.ktorm.dsl.insert

fun Application.authenticationRoutes() {
    val db = DatabaseConnection.database

    routing {
        post("/register") {
            val userCredentials = call.receive<UserCredentials>()

            val password = userCredentials.hashPassword()
            val result = db.insert(UserEntity) {
                set(it.username, userCredentials.username)
                set(it.password, password)
            }
            if (result > 0) {
                call.respond(status = HttpStatusCode.OK, "User created successfully ${userCredentials.username}")
            } else {
                call.respond(status = HttpStatusCode.InternalServerError, "Error creating user ${userCredentials.username}")
            }
        }
    }
}