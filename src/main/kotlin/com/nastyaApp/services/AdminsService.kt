package com.nastyaApp.services

import com.nastyaApp.controllers.*
import com.nastyaApp.models.*
import com.nastyaApp.utils.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.*

object AdminsService {

    suspend fun regAdmin(call: ApplicationCall) {
        apiCatch(call) {
            val request = call.receive<RegistrationRequest>()
            val anonymDTO = AnonymDTO(request.name, request.login, request.password)

            val adminId = AdminsController.insert(anonymDTO)
            val adminDTO = AdminsController.selectById(adminId)
            val token = AdminTokensController.insert(adminId)

            val response = adminDTO?.let {
                IdentityAdminResponse(it.id, it.name, generateImagUrl(it.avatarId), token)
            }
            if (response == null) {
                call.respond(HttpStatusCode.BadRequest, "The created admin was not found")
            } else {
                call.respond(HttpStatusCode.Created, response)
            }
        }
    }

    suspend fun loginAdmin(call: ApplicationCall) {
        apiCatch(call) {
            val request = call.receive<LoginRequest>()
            val adminDTO = AdminsController.selectByLogin(request.login)
            if (adminDTO != null) {
                if (request.password == adminDTO.password) {
                    val token = AdminTokensController.insert(adminDTO.id)
                    val response = IdentityAdminResponse(adminDTO.id, adminDTO.name, generateImagUrl(adminDTO.avatarId), token)
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    call.respond(HttpStatusCode.OK, "Invalid password")
                }
            } else {
                call.respond(HttpStatusCode.NotFound, "Login not found")
            }
        }
    }

    suspend fun getAllAdmins(call: ApplicationCall) {
        apiCatch(call) {
            val response = AdminsController.selectAll().map {
                AdminInfoResponse(it.id, it.name, generateImagUrl(it.avatarId))
            }
            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun getAdminById(call: ApplicationCall) {
        apiCatch(call) {
            val id = UUID.fromString(call.parameters["id"])
            val adminDTO = AdminsController.selectById(id)
            if (adminDTO != null) {
                val response = AdminInfoResponse(adminDTO.id, adminDTO.name, generateImagUrl(adminDTO.avatarId))
                call.respond(HttpStatusCode.OK, response)
            } else {
                call.respond(HttpStatusCode.NotFound, "Admin not found")
            }
        }
    }

    suspend fun updateAminInfoById(call: ApplicationCall) {
        apiCatch(call) {
            val id = getIdFromRequest(call)
            val token = getAdminTokenFromHeaders(call)

            adminHeaderHandle(call, token, id) {
                val request = call.receive<UpdateAdminInfoRequest>()

                val newAdminDTO = NewAdminDTO(name = request.name, avatarId = request.avatarId)
                AdminsController.updateById(id!!, newAdminDTO)

                AdminsController.selectById(id)?.let {
                    AdminInfoResponse(it.id, it.name, generateImagUrl(it.avatarId))
                }?.let {
                    call.respond(HttpStatusCode.OK, it)
                }
            }
        }
    }

    suspend fun updateAdminSecretInfoById(call: ApplicationCall) {
        apiCatch(call) {
            val id = getIdFromRequest(call)
            val token = getAdminTokenFromHeaders(call)

            adminHeaderHandle(call, token, id) {
                val request = call.receive<UpdateSecretInfoRequest>()
                val newAdminDTO = NewAdminDTO(login = request.login, password = request.password)

                AdminsController.updateById(id!!, newAdminDTO)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun delAdminById(call: ApplicationCall) {
        apiCatch(call) {
            val id = getIdFromRequest(call)
            val token = getAdminTokenFromHeaders(call)

            adminHeaderHandle(call, token, id) {
                AdminsController.deleteById(id!!)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun unloginAdmin(call: ApplicationCall) {
        apiCatch(call) {
            val id = getIdFromRequest(call)
            val token = getAdminTokenFromHeaders(call)

            adminHeaderHandle(call, token, id) {
                AdminTokensController.delete(token!!)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun delAvatar(call: ApplicationCall) {
        apiCatch(call) {
            val id = getIdFromRequest(call)
            val token = getAdminTokenFromHeaders(call)

            adminHeaderHandle(call, token, id) {
                val adminRow = AdminsController.selectById(id!!)

                adminRow?.avatarId?.let { avatarId ->
                    FilesController.deleteImageById(avatarId)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }

}