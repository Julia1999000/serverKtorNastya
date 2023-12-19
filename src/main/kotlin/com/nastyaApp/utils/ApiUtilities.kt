package com.nastyaApp.utils

import com.nastyaApp.controllers.TokensController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import java.util.*

suspend fun authHeaderHandle(call: ApplicationCall, token: UUID?, userId: UUID?, runValidBlock: suspend () -> Unit) {
    token ?: return call.respond(HttpStatusCode.BadRequest, "Token not found")
    userId ?: return call.respond(HttpStatusCode.BadRequest, "User id not found")

    if (isValidToken(token, userId)) {
        runValidBlock()
    } else {
        call.respond(HttpStatusCode.BadRequest, "Invalid token")
    }
}

private suspend fun isValidToken(token: UUID, userId: UUID): Boolean {
    val tokenRow = TokensController.select(token)
    return tokenRow != null && tokenRow.userId == userId
}

fun getIdFromRequest(call: ApplicationCall): UUID? {
    val idStr = call.parameters["id"]
    return idStr?.let { UUID.fromString(it) }
}

fun getTokenFromHeaders(call: ApplicationCall): UUID? {
    val tokenStr = call.request.headers["Bearer-Authorization"]
    return tokenStr?.let { UUID.fromString(it) }
}