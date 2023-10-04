package matheus.io.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.io.File

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
            call.respond(status = HttpStatusCode.OK, message = "Login successful! $userInfo")
        }

        get("/headers") {
            call.response.headers.append("server-name", "Ktor server")
            call.respond(status = HttpStatusCode.OK, message = "Headers set!")
        }

        get("/files") {
            val file = File("files/mauricio.jpg")

            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment.withParameter(
                    ContentDisposition.Parameters.FileName,
                    "mauricio.jpg"
                ).toString()
            )

            call.respondFile(file)
        }

        get("/filesOpen") {
            val file = File("files/mauricio.jpg")

            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Inline.withParameter(
                    ContentDisposition.Parameters.FileName,
                    "mauricio.jpg"
                ).toString()
            )

            call.respondFile(file)
        }
    }
}

@Serializable
data class Login(
    val username: String,
    val password: String
)