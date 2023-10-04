package matheus.io

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import matheus.io.plugins.*
import matheus.io.routing.authenticationRoutes
import matheus.io.routing.notesRoutes
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
   /* val database = Database.connect(
        url = "jdbc:mysql://localhost:3306/notes",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = ""
    )*/

    /*database.insert(NoteEntity) {
        set(it.note, "Hello World!")
    }*/

   /* val notes = database.from(NoteEntity)
        .select()

    for (row in notes) {
        println(row[NoteEntity.note])
    }*/

    /*database.update(NoteEntity) {
        set(it.note, "KTOR AND GOLANG")
        where {
            it.id eq 1
        }
    }*/

   /* database.delete(NoteEntity) {
        it.id eq 1
    }
*/
    install(ContentNegotiation) {
        json()
    }
    configureRouting()
    contactUsModule()
    notesRoutes()
    authenticationRoutes()
}
