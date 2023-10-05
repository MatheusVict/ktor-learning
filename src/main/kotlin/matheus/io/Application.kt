package matheus.io

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import matheus.io.plugins.configureRouting
import matheus.io.plugins.contactUsModule
import matheus.io.routing.authenticationRoutes
import matheus.io.routing.notesRoutes


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    install(ContentNegotiation) {
        json()
    }

    install(Authentication) {
        jwt {
            // Configure jwt authentication
        }
    }

    configureRouting()
    contactUsModule()
    notesRoutes()
    authenticationRoutes()
}
