package com.nastyaApp.services

import com.nastyaApp.controllers.*
import com.nastyaApp.mappers.*
import com.nastyaApp.models.*
import com.nastyaApp.utils.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

object UsersService {

    suspend fun regUser(call: ApplicationCall) {
        apiCatch(call) {
            val request = call.receive<RegistrationRequest>()
            val anonymDTO = request.toAnonymDTO()

            val userId = UsersController.insert(anonymDTO)
            val userDTO = UsersController.selectById(userId)
            val token = UserTokensController.insert(userId)

            userDTO?.toIdentityUserResponse(token)?.let { response ->
                call.respond(HttpStatusCode.Created, response)
            }
        }
    }

    suspend fun loginUser(call: ApplicationCall) {
        apiCatch(call) {
            val request = call.receive<LoginRequest>()
            val userDTO = UsersController.selectByLogin(request.login)
                ?: return@apiCatch call.respond(HttpStatusCode.NotFound, "Login not found")

            if (request.password != userDTO.password) {
                return@apiCatch call.respond(HttpStatusCode.OK, "Invalid password")
            }

            val token = UserTokensController.insert(userDTO.id)
            val response = userDTO.toIdentityUserResponse(token)
            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun getAllUsers(call: ApplicationCall) {
        // TODO pagination
        apiCatch(call) {
            val response = UsersController.selectAll("").map { it.toShortUserResponse() }
            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun getFullUserById(call: ApplicationCall) {
        apiCatch(call) {
            val id = getUserIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "User id not found")

            val userDTO = UsersController.selectById(id)
                ?: return@apiCatch call.respond(HttpStatusCode.NotFound, "User not found")

            val listComs = ComsController.selectPublishedByAuthorId(id).map {
                val countLikers = LikesController.selectCountAllByComId(it.id)
                val countComments = CommentController.selectCountAllByComId(it.id)
                it.toShortComResponse(countLikers, countComments)
            }
            val listBoards = listOf<Board>() // TODO get user's Boards
            val response = userDTO.toFullUserResponse(listComs, listBoards)
            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun updateUserInfoById(call: ApplicationCall) {
        apiCatch(call) {
            val id = getUserIdFromRequest(call)
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token, id) {
                val request = call.receive<UpdateUserInfoRequest>()
                val newUserDTO = request.toNewUserDTO()

                UsersController.updateById(id!!, newUserDTO)
                UsersController.selectById(id)?.toShortUserResponse()?.let {
                    call.respond(HttpStatusCode.OK, it)
                }
            }
        }
    }

    suspend fun updateUserSecretInfoById(call: ApplicationCall) {
        apiCatch(call) {
            val id = getUserIdFromRequest(call)
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token, id) {
                val request = call.receive<UpdateSecretInfoRequest>()
                val newUserDTO = request.toNewUserDTO()

                UsersController.updateById(id!!, newUserDTO)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun delUserById(call: ApplicationCall) {
        apiCatch(call) {
            val id = getUserIdFromRequest(call)
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token, id) {
                UsersController.deleteById(id!!)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun unloginUser(call: ApplicationCall) {
        apiCatch(call) {
            val id = getUserIdFromRequest(call)
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token, id) {
                UserTokensController.delete(token!!)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun delAvatar(call: ApplicationCall) {
        apiCatch(call) {
            val id = getUserIdFromRequest(call)
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token, id) {
                val userRow = UsersController.selectById(id!!)

                userRow?.avatarId?.let { avatarId ->
                    FilesController.deleteImageById(avatarId)
                }
                call.respond(HttpStatusCode.OK)
            }
        }
    }

}