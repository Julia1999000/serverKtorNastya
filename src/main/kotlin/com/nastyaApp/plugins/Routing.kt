package com.nastyaApp.plugins

import com.nastyaApp.routing.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        configureUsersRouting()
        configureComsRouting()
        configureBoardsRouting()
        configureFilesRouting()
    }
}
