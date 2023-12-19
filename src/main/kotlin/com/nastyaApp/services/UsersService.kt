package com.nastyaApp.services

import com.nastyaApp.controllers.FilesController
import com.nastyaApp.controllers.TokensController
import com.nastyaApp.controllers.UsersController
import com.nastyaApp.models.*
import com.nastyaApp.utils.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.*

object UsersService {

    suspend fun regUser(call: ApplicationCall) {
        apiCatch(call) {
            val request = call.receive<RegistrationRequest>()
            val anonymDTO = AnonymDTO(request.name, request.login, request.password)

            val userId = UsersController.insert(anonymDTO)
            val userDTO = UsersController.selectById(userId)
            val token = TokensController.insert(userId)

            val response = userDTO?.let {
                IdentityUserResponse(it.id, it.name, generateImagUrl(it.avatarId), token)
            }

            if (response == null) {
                call.respond(HttpStatusCode.BadRequest, "The created user was not found")
            } else {
                call.respond(HttpStatusCode.Created, response)
            }
        }
    }

    suspend fun login(call: ApplicationCall) {
        apiCatch(call) {
            val request = call.receive<LoginRequest>()
            val userDTO = UsersController.selectByLogin(request.login)
            if (userDTO != null) {
                if (request.password == userDTO.password) {
                    val token = TokensController.insert(userDTO.id)
                    val response = IdentityUserResponse(userDTO.id, userDTO.name,  generateImagUrl(userDTO.avatarId), token)
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    call.respond(HttpStatusCode.OK, "Invalid password")
                }
            } else {
                call.respond(HttpStatusCode.NotFound, "Login not found")
            }
        }
    }

    suspend fun getAllUsers(call: ApplicationCall) {
        // TODO pagination
        apiCatch(call) {
            val response = UsersController.selectAllUsers("").map {
                ShortUserResponse(it.id, it.name, generateImagUrl(it.avatarId))
            }
            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun getFullUserById(call: ApplicationCall) {
        apiCatch(call) {
            val id = UUID.fromString(call.parameters["id"])
            val userDTO = UsersController.selectById(id)
            if (userDTO != null) {
                val listComs = listOf<Com>() // TODO get user's Coms
                val listBoards = listOf<Board>() // TODO get user's Boards
                val response = FullUserResponse(
                    userDTO.id,
                    userDTO.name,
                    generateImagUrl(userDTO.avatarId),
                    listComs,
                    listBoards
                )
                call.respond(HttpStatusCode.OK, response)
            } else {
                call.respond(HttpStatusCode.NotFound, "User not found")
            }
        }
    }

    suspend fun updateUserInfoById(call: ApplicationCall) {
        apiCatch(call) {
            val id = UUID.fromString(call.parameters["id"])
            val token = getTokenFromHeaders(call)

            authHeaderHandle(call, token, id) {
                val request = call.receive<UpdateUserInfoRequest>()

                val newUserDTO = NewUserDTO(name = request.name, avatarId = request.avatarId)
                UsersController.updateById(id, newUserDTO)

                UsersController.selectById(id)?.let {
                    ShortUserResponse(it.id, it.name, generateImagUrl(it.avatarId))
                }?.let {
                    call.respond(HttpStatusCode.OK, it)
                }
            }
        }
    }

    suspend fun updateUserSecretInfoById(call: ApplicationCall) {
        apiCatch(call) {
            val id = UUID.fromString(call.parameters["id"])
            val token = getTokenFromHeaders(call)

            authHeaderHandle(call, token, id) {
                val request = call.receive<UpdateSecretInfoRequest>()
                val newUserDTO = NewUserDTO(login = request.login, password = request.password)

                UsersController.updateById(id, newUserDTO)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun delUserById(call: ApplicationCall) {
        apiCatch(call) {
            val id = UUID.fromString(call.parameters["id"])
            val token = getTokenFromHeaders(call)

            authHeaderHandle(call, token, id) {
                UsersController.deleteById(id)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun unlog(call: ApplicationCall) {
        apiCatch(call) {
            val id = UUID.fromString(call.parameters["id"])
            val token = getTokenFromHeaders(call)

            authHeaderHandle(call, token, id) {
                TokensController.delete(token!!)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun delAvatar(call: ApplicationCall) {
        apiCatch(call) {
            val id = getIdFromRequest(call)
            val token = getTokenFromHeaders(call)

            authHeaderHandle(call, token, id) {
                val userRow = UsersController.selectById(id!!)

                userRow?.avatarId?.let { avatarId ->
                    FilesController.deleteImageById(avatarId)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}