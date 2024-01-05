package com.nastyaApp.services

import com.nastyaApp.controllers.*
import com.nastyaApp.mappers.*
import com.nastyaApp.models.*
import com.nastyaApp.utils.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

object ComsService {
    suspend fun createComByUser(call: ApplicationCall) {
        apiCatch(call) {
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token) {
                val userId = UserTokensController.selectUserToken(token!!)?.userId
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "User not found")

                val request = call.receive<ComRequest>()
                val newComDTO = request.toNewComDTO(userId)

                val comId = ComsController.insertCom(newComDTO)
                val comDTO = comId?.let { ComsController.selectComById(it) }

                val response = comDTO?.toShortComResponse()
                if (response != null) {
                    call.respond(HttpStatusCode.Created, response)
                }
            }
        }
    }

    suspend fun getAllUserComs(call: ApplicationCall) {
        apiCatch(call) {
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token) {
                val userId = UserTokensController.selectUserToken(token!!)?.userId
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "User not found")

                val response = ComsController.selectAllComsByAuthorId(userId).map { com ->
                    val countLikers = LikesController.selectCountAllLikesByComId(com.id)
                    val countComments = CommentController.selectCountAllCommentsByComId(com.id)
                    com.toShortComResponse(countLikers, countComments)
                }
                call.respond(HttpStatusCode.OK, response)
            }
        }
    }

    suspend fun getPublishedComsByUser(call: ApplicationCall) {
        apiCatch(call) {
            val userId = getUserIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "User id not found")

            val response = ComsController.selectPublishedComsByAuthorId(userId).map { com ->
                val countLikers = LikesController.selectCountAllLikesByComId(com.id)
                val countComments = CommentController.selectCountAllCommentsByComId(com.id)
                com.toShortComResponse(countLikers, countComments)
            }
            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun getFullInfoCom(call: ApplicationCall) {
        apiCatch(call) {
            val comId = getComIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Com id not found")

            val comDTO = ComsController.selectComById(comId)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Com not found")

            val listLikers = LikesController.selectAllLikesByComId(comDTO.id).mapNotNull { like ->
                UsersController.selectUserById(like.likerId)?.toShortUserResponse()
            }

            val authorDTO = UsersController.selectUserById(comDTO.authorId)

            val listComments = CommentController.selectAllCommentsByComId(comDTO.id).mapNotNull { commentDTO ->
                authorDTO?.let { commentDTO.toCommentResponse(it.avatarId, it.name) }
            }

            val response = comDTO.toFullComResponse(listLikers, listComments)
            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun publishComByAdmin(call: ApplicationCall) {
        apiCatch(call) {
            val token = getAdminTokenFromHeaders(call)
            val comId = getComIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Com id not found")

            adminHeaderHandle(call, token) {
                ComsController.setStatusPublished(comId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun checkComByAdmin(call: ApplicationCall) {
        apiCatch(call) {
            val token = getAdminTokenFromHeaders(call)
            val comId = getComIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Com id not found")

            adminHeaderHandle(call, token) {
                ComsController.setStatusCheckable(comId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun deleteComByAdmin(call: ApplicationCall) {
        apiCatch(call) {
            val token = getAdminTokenFromHeaders(call)
            val comId = getComIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Com id not found")

            adminHeaderHandle(call, token) {
                ComsController.selectComById(comId)?.imageId?.let {
                    FilesController.deleteImageById(it)
                }
                ComsController.deleteComById(comId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun deleteComByUser(call: ApplicationCall) {
        apiCatch(call) {
            val token = getAdminTokenFromHeaders(call)
            val comId = getComIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Com id not found")

            authHeaderHandle(call, token) {
                val userId = UserTokensController.selectUserToken(token!!)?.userId
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "User not found")

                val comDTO = ComsController.selectComById(comId)
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "Com not found")

                if (comDTO.authorId != userId) {
                    return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "No rights")
                }

                ComsController.selectComById(comId)?.imageId?.let {
                    FilesController.deleteImageById(it)
                }
                ComsController.deleteComById(comId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun getAllPublishedComs(call: ApplicationCall) {
        apiCatch(call) {
            // TODO pagination
            val response = ComsController.selectAllComsPublished("").map { com ->
                val countLikers = LikesController.selectCountAllLikesByComId(com.id)
                val countComments = CommentController.selectCountAllCommentsByComId(com.id)
                com.toShortComResponse(countLikers, countComments)
            }
            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun getAllCreatedComsByAdmin(call: ApplicationCall) {
        apiCatch(call) {
            val token = getAdminTokenFromHeaders(call)

            adminHeaderHandle(call, token) {
                // TODO pagination
                val response = ComsController.selectAllComsCreated("").map { com ->
                    val countLikers = LikesController.selectCountAllLikesByComId(com.id)
                    val countComments = CommentController.selectCountAllCommentsByComId(com.id)
                    com.toShortComResponse(countLikers, countComments)
                }
                call.respond(HttpStatusCode.OK, response)
            }
        }
    }
}