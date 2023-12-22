package com.nastyaApp.utils

import com.nastyaApp.controllers.AdminTokensController
import com.nastyaApp.controllers.UserTokensController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import java.util.*

suspend fun authHeaderHandle(call: ApplicationCall, token: UUID?, userId: UUID?, runValidBlock: suspend () -> Unit) {
    token ?: return call.respond(HttpStatusCode.BadRequest, "Token not found")
    userId ?: return call.respond(HttpStatusCode.BadRequest, "User id not found")

    if (isValidUserToken(token, userId)) {
        runValidBlock()
    } else {
        call.respond(HttpStatusCode.BadRequest, "Invalid token")
    }
}

private suspend fun isValidUserToken(token: UUID, userId: UUID): Boolean {
    val tokenRow = UserTokensController.select(token)
    return tokenRow != null && tokenRow.userId == userId
}

suspend fun adminHeaderHandle(call: ApplicationCall, token: UUID?, adminId: UUID?, runValidBlock: suspend () -> Unit) {
    token ?: return call.respond(HttpStatusCode.BadRequest, "Token not found")
    adminId ?: return call.respond(HttpStatusCode.BadRequest, "Admin id not found")

    if (isValidAdminToken(token, adminId)) {
        runValidBlock()
    } else {
        call.respond(HttpStatusCode.BadRequest, "Invalid token")
    }
}

private suspend fun isValidAdminToken(token: UUID, adminId: UUID): Boolean {
    val tokenRow = AdminTokensController.select(token)
    return tokenRow != null && tokenRow.adminId == adminId
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

fun getUserTokenFromHeaders(call: ApplicationCall): UUID? {
    val tokenStr = call.request.headers["Bearer-Authorization"]
    return tokenStr?.let { UUID.fromString(it) }
}

fun getAdminTokenFromHeaders(call: ApplicationCall): UUID? {
    val tokenStr = call.request.headers["Bearer-Administration"]
    return tokenStr?.let { UUID.fromString(it) }
}