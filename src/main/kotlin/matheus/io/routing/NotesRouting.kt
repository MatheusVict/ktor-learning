package matheus.io.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import matheus.io.NoteEntity
import matheus.io.db.DatabaseConnection
import matheus.io.model.Note
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select

fun Application.notesRoutes() {

    val db = DatabaseConnection.database

    routing {
        get("/notes") {
            val notes = db.from(NoteEntity).select()
                .map {
                    val id = it[NoteEntity.id]
                    val note = it[NoteEntity.note]
                    Note(
                        id = id ?: -1,
                        note = note ?: ""
                    )
                }
            call.respond(notes)
        }
    }
}