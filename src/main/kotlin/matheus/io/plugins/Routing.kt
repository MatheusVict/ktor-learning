package matheus.io.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            println("URI ${call.request.uri}")
            println("Host ${call.request.host()}")
            println("Headers ${call.request.headers["User-Agent"]}")
            println("cookies ${call.request.cookies.rawCookies}")
            call.respondText("Hello World!")
        }
    }
}
