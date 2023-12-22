package com.nastyaApp.services

import com.nastyaApp.controllers.CommentController
import com.nastyaApp.controllers.ComsController
import com.nastyaApp.mappers.toCommentResponse
import com.nastyaApp.mappers.toNewCommentDTO
import com.nastyaApp.models.CommentRequest
import com.nastyaApp.utils.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

object CommentService {

    suspend fun createComment(call: ApplicationCall) {
        apiCatch(call) {
            val id = getUserIdFromRequest(call)
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token, id) {
                val request = call.receive<CommentRequest>()
                val newCommentDTO = request.toNewCommentDTO()

                val newCommentId = CommentController.insert(newCommentDTO)
                val commentDTO = CommentController.selectById(newCommentId)

                val response = commentDTO?.toCommentResponse()
                if (response != null) {
                    call.respond(HttpStatusCode.Created, response)
                }
            }
        }
    }

    suspend fun deleteCommentByUser(call: ApplicationCall) {
        apiCatch(call) {
            val id = getUserIdFromRequest(call)
            val token = getUserTokenFromHeaders(call)
            val commentId = getCommentIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Comment id not found")

            authHeaderHandle(call, token, id) {
                val commentDTO = CommentController.selectById(commentId)
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "Comment not found")

                val comDTO = ComsController.selectById(commentDTO.comId)
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "Com not found")

                if (commentDTO.authorId != id && comDTO.authorId != id) {
                    return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "No rights")
                }

                CommentController.deleteById(commentId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun deleteCommentByAdmin(call: ApplicationCall) {
        apiCatch(call) {
            val id = getAdminIdFromRequest(call)
            val token = getAdminTokenFromHeaders(call)
            val commentId = getCommentIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Comment id not found")

            adminHeaderHandle(call, token, id) {
                CommentController.deleteById(commentId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

}