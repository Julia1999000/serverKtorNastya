package com.nastyaApp.services

import com.nastyaApp.controllers.LikesController
import com.nastyaApp.mappers.toLikeResponse
import com.nastyaApp.utils.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

object LikesService {

    suspend fun setLike(call: ApplicationCall) {
        apiCatch(call) {
            val userId = getUserIdFromRequest(call)
            val token = getUserTokenFromHeaders(call)
            val comId = getComIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Com id not found")

            authHeaderHandle(call, token, userId) {
                val likeId = LikesController.insertLike(userId!!, comId)
                val likeDTO = LikesController.selectLikeById(likeId)

                val response = likeDTO?.toLikeResponse()
                if (response != null) {
                    call.respond(HttpStatusCode.Created, response)
                }
            }
        }
    }

    suspend fun deleteLike(call: ApplicationCall) {
        apiCatch(call) {
            val userId = getUserIdFromRequest(call)
            val token = getUserTokenFromHeaders(call)
            val comId = getComIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Com id not found")

            authHeaderHandle(call, token, userId) {
                val likeDTO = LikesController.selectLikeByUserIdAndComId(userId!!, comId)
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "Like not found")

                LikesController.deleteLikeById(likeDTO.id)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

}