package matheus.io.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.contactUsModule() {
    routing {
        get("/contact") {
            call.respondText("Contact us!")
        }

        get("/about") {
            call.respondRedirect("/contact")
        }

        get("/hello/{page}") {
            call.respondText("Hello ${call.parameters["page"]}")
        }

        post("/login") {
            val userInfo = call.receive<Login>()
            call.respondText("Login ${userInfo.username} ${userInfo.password}")
        }
    }
}

@Serializable
data class Login(
    val username: String,
    val password: String
)