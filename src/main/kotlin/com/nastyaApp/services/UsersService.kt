package com.nastyaApp.services

import com.nastyaApp.controllers.UsersController
import com.nastyaApp.models.*
import com.nastyaApp.utils.apiCatch
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.*

object UsersService {

    suspend fun regUser(call: ApplicationCall) {
        apiCatch(call) {
            val regRequest = call.receive<RegistrationRequest>()
            val anonymDTO = AnonymDTO(regRequest.name, regRequest.login, regRequest.password)
            // TODO repeat check login
            val userId = UsersController.insert(anonymDTO)
            val userDTO = UsersController.selectById(userId)
            //TODO create token
            val identityUserResponse = IdentityUserResponse(userDTO?.id ?: "", userDTO?.name ?: "", "")
            call.respond(HttpStatusCode.Created, identityUserResponse)
        }
    }

    suspend fun login(call: ApplicationCall) {
        apiCatch(call) {
            val secretUserInfo = call.receive<SecretUserInfo>()
            val userDTO = UsersController.selectByLogin(secretUserInfo.login)
            if (userDTO != null) {
                if (secretUserInfo.password == userDTO.password) {
                    //TODO create token
                    val identityUserResponse = IdentityUserResponse(userDTO.id, userDTO.name, "")
                    call.respond(HttpStatusCode.OK, identityUserResponse)
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
            val listShortUserResponse = UsersController.selectAllUsers("").map {
                ShortUserInfo(it.id, it.name)
            }
            call.respond(HttpStatusCode.OK, listShortUserResponse)
        }
    }

    suspend fun getFullUserById(call: ApplicationCall) {
        apiCatch(call) {
            val id = UUID.fromString(call.parameters["id"])
            val userDTO = UsersController.selectById(id)
            if (userDTO != null) {
                // TODO get user's Coms and Boards
                val listComs = listOf<Com>()
                val listBoards = listOf<Board>()
                val fullUserResponse = FullUserInfo(
                    userDTO.id,
                    userDTO.name,
                    listComs,
                    listBoards
                )
                call.respond(HttpStatusCode.OK, fullUserResponse)
            } else {
                call.respond(HttpStatusCode.NotFound, "User not found")
            }
        }
    }

    suspend fun updateUserInfoById(call: ApplicationCall) { //TODO id in rout and in body
        apiCatch(call) {
            val id = UUID.fromString(call.parameters["id"])
            val user = call.receive<ShortUserInfo>()
            val newUserIfo = NewUserIfo(id = user.id, name = user.name)
            UsersController.updateById(id, newUserIfo)
            call.respond(HttpStatusCode.OK)
        }
    }

    suspend fun updateUserSecretInfoById(call: ApplicationCall) {
        apiCatch(call) {
            val id = UUID.fromString(call.parameters["id"])
            val secretUserInfo = call.receive<SecretUserInfo>()
            val newUserIfo = NewUserIfo(id = id.toString(), login = secretUserInfo.login, password = secretUserInfo.password)
            UsersController.updateById(id, newUserIfo)
            call.respond(HttpStatusCode.OK)
        }
    }

    suspend fun delUserById(call: ApplicationCall) {
        apiCatch(call) {
            val id = UUID.fromString(call.parameters["id"])
            UsersController.deleteById(id)
            call.respond(HttpStatusCode.OK)
        }
    }

}