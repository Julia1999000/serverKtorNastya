package com.nastyaApp.plugins

import com.nastyaApp.routing.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        configureUsersRouting()
        configureAdminsRouting()
        configureComsRouting()
        configureBoardsRouting()
        configureFilesRouting()
        configureCommentsRouting()
        configureLikesRouting()
    }
}
