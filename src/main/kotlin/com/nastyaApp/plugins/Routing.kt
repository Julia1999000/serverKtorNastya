package com.nastyaApp.plugins

import com.nastyaApp.routing.configureBoardsRouting
import com.nastyaApp.routing.configureComsRouting
import com.nastyaApp.routing.configureUsersRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        configureUsersRouting()
        configureComsRouting()
        configureBoardsRouting()
    }
}
