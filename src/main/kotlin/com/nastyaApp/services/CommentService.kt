package com.nastyaApp.services

import com.nastyaApp.controllers.CommentController
import com.nastyaApp.controllers.ComsController
import com.nastyaApp.controllers.UserTokensController
import com.nastyaApp.controllers.UsersController
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
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token) {
                val userId = UserTokensController.selectUserToken(token!!)?.userId
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "User not found")

                val request = call.receive<CreateCommentRequest>()
                val newCommentDTO = request.toNewCommentDTO()

                val newCommentId = CommentController.insertComment(newCommentDTO)
                val commentDTO = CommentController.selectCommentById(newCommentId)

                val userDTO = UsersController.selectUserById(userId)
                val response = userDTO?.let {
                    commentDTO?.toCommentResponse(generateImagUrl(call.request.host(), call.request.port(), it.avatarId), it.name)
                }
                if (response != null) {
                    call.respond(HttpStatusCode.Created, response)
                }
            }
        }
    }

    suspend fun deleteCommentByUser(call: ApplicationCall) {
        apiCatch(call) {
            val token = getUserTokenFromHeaders(call)
            val commentId = getCommentIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Comment id not found")

            authHeaderHandle(call, token) {
                val userId = UserTokensController.selectUserToken(token!!)?.userId
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "User not found")

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
            val token = getAdminTokenFromHeaders(call)
            val commentId = getCommentIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Comment id not found")

            adminHeaderHandle(call, token) {
                CommentController.deleteCommentById(commentId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun getAllCommentsByCom(call: ApplicationCall) {
        apiCatch(call) {
            val comId = getComIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Com id not found")

            val response = CommentController.selectAllCommentsByComId(comId).mapNotNull { commentDTO ->
                UsersController.selectUserById(commentDTO.authorId)?.let { authorDTO ->
                    commentDTO.toCommentResponse(
                        generateImagUrl(call.request.host(), call.request.port(), authorDTO.avatarId), authorDTO.name)
                }
            }
            call.respond(HttpStatusCode.OK, response)
        }
    }

}