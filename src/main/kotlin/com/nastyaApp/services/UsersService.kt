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

            val userId = UsersController.insertUser(anonymDTO)
            val userDTO = UsersController.selectUserById(userId)
            val token = UserTokensController.insertUserToken(userId)

            userDTO?.toIdentityUserResponse(token)?.let { response ->
                call.respond(HttpStatusCode.Created, response)
            }
        }
    }

    suspend fun loginUser(call: ApplicationCall) {
        apiCatch(call) {
            val request = call.receive<LoginRequest>()
            val userDTO = UsersController.selectUserByLogin(request.login)
                ?: return@apiCatch call.respond(HttpStatusCode.NotFound, "Login not found")

            if (request.password != userDTO.password) {
                return@apiCatch call.respond(HttpStatusCode.OK, "Invalid password")
            }

            val token = UserTokensController.insertUserToken(userDTO.id)
            val response = userDTO.toIdentityUserResponse(token)
            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun getAllUsers(call: ApplicationCall) {
        // TODO pagination
        apiCatch(call) {
            val response = UsersController.selectAllUsers("").map { it.toShortUserResponse() }
            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun getFullUserById(call: ApplicationCall) {
        apiCatch(call) {
            val userId = getUserIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "User id not found")

            val userDTO = UsersController.selectUserById(userId)
                ?: return@apiCatch call.respond(HttpStatusCode.NotFound, "User not found")

            val listComs = ComsController.selectPublishedComsByAuthorId(userId).map { com ->
                val countLikers = LikesController.selectCountAllLikesByComId(com.id)
                val countComments = CommentController.selectCountAllCommentsByComId(com.id)
                com.toShortComResponse(countLikers, countComments)
            }
            val listBoards = BoardController.selectAllPublicBoardsByUserId(userId).map {
                it.toBoardResponse()
            }
            val response = userDTO.toFullUserResponse(listComs, listBoards)
            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun updateUserInfoById(call: ApplicationCall) {
        apiCatch(call) {
            val userId = getUserIdFromRequest(call)
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token, userId) {
                val request = call.receive<UpdateUserInfoRequest>()
                val newUserDTO = request.toNewUserDTO()

                UsersController.updateUserById(userId!!, newUserDTO)
                UsersController.selectUserById(userId)?.toShortUserResponse()?.let {
                    call.respond(HttpStatusCode.OK, it)
                }
            }
        }
    }

    suspend fun updateUserSecretInfoById(call: ApplicationCall) {
        apiCatch(call) {
            val userId = getUserIdFromRequest(call)
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token, userId) {
                val request = call.receive<UpdateSecretInfoRequest>()
                val newUserDTO = request.toNewUserDTO()

                UsersController.updateUserById(userId!!, newUserDTO)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun delUserById(call: ApplicationCall) {
        apiCatch(call) {
            val userId = getUserIdFromRequest(call)
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token, userId) {
                UsersController.deleteUserById(userId!!)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun unloginUser(call: ApplicationCall) {
        apiCatch(call) {
            val userId = getUserIdFromRequest(call)
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token, userId) {
                UserTokensController.deleteUserToken(token!!)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun delAvatar(call: ApplicationCall) {
        apiCatch(call) {
            val userId = getUserIdFromRequest(call)
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token, userId) {
                val userRow = UsersController.selectUserById(userId!!)

                userRow?.avatarId?.let { avatarId ->
                    FilesController.deleteImageById(avatarId)
                }
                call.respond(HttpStatusCode.OK)
            }
        }
    }

}