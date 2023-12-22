package com.nastyaApp.services

import com.nastyaApp.controllers.CommentController
import com.nastyaApp.controllers.ComsController
import com.nastyaApp.mappers.toCommentResponse
import com.nastyaApp.mappers.toFullComResponse
import com.nastyaApp.mappers.toNewComDTO
import com.nastyaApp.mappers.toShortComResponse
import com.nastyaApp.models.*
import com.nastyaApp.utils.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

object ComsService {
    suspend fun createComByUser(call: ApplicationCall) {
        apiCatch(call) {
            val id = getUserIdFromRequest(call)
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token, id) {
                val request = call.receive<ComRequest>()
                val newComDTO = request.toNewComDTO(id!!)

                val comId = ComsController.insert(newComDTO)
                val comDTO = comId?.let { ComsController.selectById(it) }

                val response = comDTO?.toShortComResponse()
                if (response != null) {
                    call.respond(HttpStatusCode.Created, response)
                }
            }
        }
    }

    suspend fun getAllComs(call: ApplicationCall) {
        apiCatch(call) {
            val id = getUserIdFromRequest(call)
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token, id) {
                val response = ComsController.selectAllByAuthorId(id!!).map {
                    val countLikers = 0 // TODO get count Likers where comId == it.id
                    val countComments = CommentController.selectCountAllByComId(it.id)
                    it.toShortComResponse(countLikers, countComments)
                }
                call.respond(HttpStatusCode.OK, response)
            }
        }
    }

    suspend fun getPublishedComs(call: ApplicationCall) {
        apiCatch(call) {
            val id = getUserIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "User id not found")

            val response = ComsController.selectPublishedByAuthorId(id).map {
                val countLikers = 0 // TODO get count Likers where comId == it.id
                val countComments = CommentController.selectCountAllByComId(it.id)
                it.toShortComResponse(countLikers, countComments)
            }
            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun getFullInfoCom(call: ApplicationCall) {
        apiCatch(call) {
            val id = getComIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Com id not found")

            val comDTO = ComsController.selectById(id)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Com not found")

            val listLikers = listOf<ShortUserResponse>() // TODO get Likers where comId == comDTO.id
            val listComments = CommentController.selectAllByComId(comDTO.id).map { it.toCommentResponse() }
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
                ComsController.deleteById(comId)
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
                ComsController.deleteById(comId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun getAllPublishedComs(call: ApplicationCall) {
        apiCatch(call) {
            // TODO pagination
            val response = ComsController.selectAllPublished("").map {
                val countLikers = 0 // TODO get count Likers where comId == it.id
                val countComments = CommentController.selectCountAllByComId(it.id)
                it.toShortComResponse(countLikers, countComments)
            }
            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun getAllCreatedComs(call: ApplicationCall) {
        apiCatch(call) {
            val id = getAdminIdFromRequest(call)
            val token = getAdminTokenFromHeaders(call)

            adminHeaderHandle(call, token, id) {
                // TODO pagination
                val response = ComsController.selectAllCreated("").map {
                    val countLikers = 0 // TODO get count Likers where comId == it.id
                    val countComments = CommentController.selectCountAllByComId(it.id)
                    it.toShortComResponse(countLikers, countComments)
                }
                call.respond(HttpStatusCode.OK, response)
            }
        }
    }
}