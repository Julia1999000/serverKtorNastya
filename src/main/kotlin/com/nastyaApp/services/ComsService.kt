package com.nastyaApp.services

import com.nastyaApp.controllers.CommentController
import com.nastyaApp.controllers.ComsController
import com.nastyaApp.controllers.LikesController
import com.nastyaApp.controllers.UsersController
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
            val userId = getUserIdFromRequest(call)
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token, userId) {
                val request = call.receive<ComRequest>()
                val newComDTO = request.toNewComDTO(userId!!)

                val comId = ComsController.insertCom(newComDTO)
                val comDTO = comId?.let { ComsController.selectComById(it) }

                val response = comDTO?.toShortComResponse()
                if (response != null) {
                    call.respond(HttpStatusCode.Created, response)
                }
            }
        }
    }

    suspend fun getAllComs(call: ApplicationCall) {
        apiCatch(call) {
            val userId = getUserIdFromRequest(call)
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token, userId) {
                val response = ComsController.selectAllComsByAuthorId(userId!!).map { com ->
                    val countLikers = LikesController.selectCountAllLikesByComId(com.id)
                    val countComments = CommentController.selectCountAllCommentsByComId(com.id)
                    com.toShortComResponse(countLikers, countComments)
                }
                call.respond(HttpStatusCode.OK, response)
            }
        }
    }

    suspend fun getPublishedComs(call: ApplicationCall) {
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
            val userId = getComIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Com id not found")

            val comDTO = ComsController.selectComById(userId)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Com not found")

            val listLikers = LikesController.selectAllLikesByComId(comDTO.id).mapNotNull { like ->
                UsersController.selectUserById(like.likerId)?.toShortUserResponse()
            }
            val listComments = CommentController.selectAllCommentsByComId(comDTO.id).map { it.toCommentResponse() }
            val response = comDTO.toFullComResponse(listLikers, listComments)
            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun publishComByAdmin(call: ApplicationCall) {
        apiCatch(call) {
            val adminId = getAdminIdFromRequest(call)
            val token = getAdminTokenFromHeaders(call)
            val comId = getComIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Com id not found")

            adminHeaderHandle(call, token, adminId) {
                ComsController.setStatusPublished(comId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun checkComByAdmin(call: ApplicationCall) {
        apiCatch(call) {
            val adminId = getAdminIdFromRequest(call)
            val token = getAdminTokenFromHeaders(call)
            val comId = getComIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Com id not found")

            adminHeaderHandle(call, token, adminId) {
                ComsController.setStatusCheckable(comId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun deleteComByAdmin(call: ApplicationCall) {
        apiCatch(call) {
            val adminId = getAdminIdFromRequest(call)
            val token = getAdminTokenFromHeaders(call)
            val comId = getComIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Com id not found")

            adminHeaderHandle(call, token, adminId) {
                ComsController.deleteComById(comId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun deleteComByUser(call: ApplicationCall) {
        apiCatch(call) {
            val userId = getUserIdFromRequest(call)
            val token = getAdminTokenFromHeaders(call)
            val comId = getComIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Com id not found")

            authHeaderHandle(call, token, userId) {
                val comDTO = ComsController.selectComById(comId)
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "Com not found")

                if (comDTO.authorId != userId) {
                    return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "No rights")
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

    suspend fun getAllCreatedComs(call: ApplicationCall) {
        apiCatch(call) {
            val adminId = getAdminIdFromRequest(call)
            val token = getAdminTokenFromHeaders(call)

            adminHeaderHandle(call, token, adminId) {
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