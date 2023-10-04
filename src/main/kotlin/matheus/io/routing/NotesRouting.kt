package matheus.io.routing

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import matheus.io.NoteEntity
import matheus.io.db.DatabaseConnection
import matheus.io.model.Note
import matheus.io.model.NoteRequest
import matheus.io.model.NoteResponse
import org.ktorm.dsl.*

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

        post("/notes") {
            val request = call.receive<NoteRequest>()
            val result = db.insert(NoteEntity) {
                set(it.note, request.note)
            }
            val noteResponse = NoteResponse<String>(
                data = request.note,
                success = true
            )
            if (result > 0) {
                call.respond(status = io.ktor.http.HttpStatusCode.OK, noteResponse)
            } else {

                val noteResponse = NoteResponse<String>(
                    data = request.note,
                    success = false
                )

                call.respond(status = io.ktor.http.HttpStatusCode.InternalServerError, noteResponse)
            }
        }

        get("/notes/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1

            db.from(NoteEntity)
                .select()
                .where {
                    NoteEntity.id eq id
                }
                .map {
                    val id = it[NoteEntity.id]
                    val note = it[NoteEntity.note]
                    Note(
                        id = id ?: -1,
                        note = note ?: ""
                    )
                }.firstOrNull()?.let {
                    call.respond(it)
                } ?: call.respond(io.ktor.http.HttpStatusCode.NotFound)
        }
    }
}