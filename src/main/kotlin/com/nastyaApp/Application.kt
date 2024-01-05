package com.nastyaApp

import com.nastyaApp.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(
        Netty,
        port = System.getenv("SERVER_PORT").toInt(),
        host = System.getenv("SERVER_HOST"),
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureSecurity()
    configureRouting()
}
