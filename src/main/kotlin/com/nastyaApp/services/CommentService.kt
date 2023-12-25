package com.nastyaApp.services

import com.nastyaApp.controllers.CommentController
import com.nastyaApp.controllers.ComsController
import com.nastyaApp.mappers.toCommentResponse
import com.nastyaApp.mappers.toNewCommentDTO
import com.nastyaApp.models.CreateCommentRequest
import com.nastyaApp.utils.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

object CommentService {

    suspend fun createComment(call: ApplicationCall) {
        apiCatch(call) {
            val userId = getUserIdFromRequest(call)
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token, userId) {
                val request = call.receive<CreateCommentRequest>()
                val newCommentDTO = request.toNewCommentDTO()

                val newCommentId = CommentController.insertComment(newCommentDTO)
                val commentDTO = CommentController.selectCommentById(newCommentId)

                val response = commentDTO?.toCommentResponse()
                if (response != null) {
                    call.respond(HttpStatusCode.Created, response)
                }
            }
        }
    }

    suspend fun deleteCommentByUser(call: ApplicationCall) {
        apiCatch(call) {
            val userId = getUserIdFromRequest(call)
            val token = getUserTokenFromHeaders(call)
            val commentId = getCommentIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Comment id not found")

            authHeaderHandle(call, token, userId) {
                val commentDTO = CommentController.selectCommentById(commentId)
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "Comment not found")

                val comDTO = ComsController.selectComById(commentDTO.comId)
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "Com not found")

                if (commentDTO.authorId != userId && comDTO.authorId != userId) {
                    return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "No rights")
                }

                CommentController.deleteCommentById(commentId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun deleteCommentByAdmin(call: ApplicationCall) {
        apiCatch(call) {
            val adminId = getAdminIdFromRequest(call)
            val token = getAdminTokenFromHeaders(call)
            val commentId = getCommentIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Comment id not found")

            adminHeaderHandle(call, token, adminId) {
                CommentController.deleteCommentById(commentId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

}