package com.nastyaApp.utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

suspend fun apiCatch(call: ApplicationCall, runBlock: suspend () -> Unit) {
    try {
        runBlock()
    } catch (e: Exception) {
        when (e) {
            is IllegalArgumentException -> {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
            }
            else -> {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Exception")
            }
        }
    }
}