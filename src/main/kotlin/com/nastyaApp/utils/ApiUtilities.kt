package com.nastyaApp.utils

import com.nastyaApp.controllers.AdminTokensController
import com.nastyaApp.controllers.UserTokensController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import java.util.*

suspend fun authHeaderHandle(call: ApplicationCall, token: UUID?, runValidBlock: suspend () -> Unit) {
    if (isValidUserToken(token)) {
        runValidBlock()
    } else {
        call.respond(HttpStatusCode.BadRequest, "Invalid token or not found")
    }
}

private suspend fun isValidUserToken(token: UUID?): Boolean {
    val tokenRow = token?.let { UserTokensController.selectUserToken(it) }
    return tokenRow != null
}

suspend fun adminHeaderHandle(call: ApplicationCall, token: UUID?, runValidBlock: suspend () -> Unit) {
    if (isValidAdminToken(token)) {
        runValidBlock()
    } else {
        call.respond(HttpStatusCode.BadRequest, "Invalid token or not found")
    }
}

private suspend fun isValidAdminToken(token: UUID?): Boolean {
    val tokenRow = token?.let { AdminTokensController.selectAdminToken(it) }
    return tokenRow != null
}

fun getUserIdFromRequest(call: ApplicationCall): UUID? {
    val idStr = call.parameters["user_id"]
    return idStr?.let { UUID.fromString(it) }
}

fun getAdminIdFromRequest(call: ApplicationCall): UUID? {
    val idStr = call.parameters["admin_id"]
    return idStr?.let { UUID.fromString(it) }
}

fun getComIdFromRequest(call: ApplicationCall): UUID? {
    val idStr = call.parameters["com_id"]
    return idStr?.let { UUID.fromString(it) }
}

fun getFileIdFromRequest(call: ApplicationCall): UUID? {
    val idStr = call.parameters["file_id"]
    return idStr?.let { UUID.fromString(it) }
}

fun getCommentIdFromRequest(call: ApplicationCall): UUID? {
    val idStr = call.parameters["comment_id"]
    return idStr?.let { UUID.fromString(it) }
}

fun getBoardIdFromRequest(call: ApplicationCall): UUID? {
    val idStr = call.parameters["board_id"]
    return idStr?.let { UUID.fromString(it) }
}

fun getUserTokenFromHeaders(call: ApplicationCall): UUID? {
    val tokenStr = call.request.headers["Bearer-Authorization"]
    return tokenStr?.let { UUID.fromString(it) }
}

fun getAdminTokenFromHeaders(call: ApplicationCall): UUID? {
    val tokenStr = call.request.headers["Bearer-Administration"]
    return tokenStr?.let { UUID.fromString(it) }
}