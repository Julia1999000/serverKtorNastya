package com.nastyaApp.services

import com.nastyaApp.controllers.*
import com.nastyaApp.mappers.toAdminInfoResponse
import com.nastyaApp.mappers.toAnonymDTO
import com.nastyaApp.mappers.toIdentityAdminResponse
import com.nastyaApp.mappers.toNewAdminDTO
import com.nastyaApp.models.*
import com.nastyaApp.utils.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

object AdminsService {

    suspend fun regAdmin(call: ApplicationCall) {
        apiCatch(call) {
            val request = call.receive<RegistrationRequest>()
            val anonymDTO = request.toAnonymDTO()

            val adminId = AdminsController.insert(anonymDTO)
            val adminDTO = AdminsController.selectById(adminId)
            val token = AdminTokensController.insert(adminId)

            adminDTO?.toIdentityAdminResponse(token)?.let { response ->
                call.respond(HttpStatusCode.Created, response)
            }
        }
    }

    suspend fun loginAdmin(call: ApplicationCall) {
        apiCatch(call) {
            val request = call.receive<LoginRequest>()
            val adminDTO = AdminsController.selectByLogin(request.login)
                ?: return@apiCatch call.respond(HttpStatusCode.NotFound, "Login not found")

            if (request.password != adminDTO.password) {
                return@apiCatch call.respond(HttpStatusCode.OK, "Invalid password")
            }

            val token = AdminTokensController.insert(adminDTO.id)
            val response = adminDTO.toIdentityAdminResponse(token)
            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun getAllAdmins(call: ApplicationCall) {
        apiCatch(call) {
            val response = AdminsController.selectAll().map { it.toAdminInfoResponse() }
            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun getAdminById(call: ApplicationCall) {
        apiCatch(call) {
            val id = getAdminIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.NotFound, "Admin id not found")

            val adminDTO = AdminsController.selectById(id)
                ?: return@apiCatch call.respond(HttpStatusCode.NotFound, "Admin not found")

            val response = adminDTO.toAdminInfoResponse()
            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun updateAminInfoById(call: ApplicationCall) {
        apiCatch(call) {
            val id = getAdminIdFromRequest(call)
            val token = getAdminTokenFromHeaders(call)

            adminHeaderHandle(call, token, id) {
                val request = call.receive<UpdateAdminInfoRequest>()
                val newAdminDTO = request.toNewAdminDTO()

                AdminsController.updateById(id!!, newAdminDTO)
                AdminsController.selectById(id)?.toAdminInfoResponse()?.let {
                    call.respond(HttpStatusCode.OK, it)
                }
            }
        }
    }

    suspend fun updateAdminSecretInfoById(call: ApplicationCall) {
        apiCatch(call) {
            val id = getAdminIdFromRequest(call)
            val token = getAdminTokenFromHeaders(call)

            adminHeaderHandle(call, token, id) {
                val request = call.receive<UpdateSecretInfoRequest>()
                val newAdminDTO = request.toNewAdminDTO()

                AdminsController.updateById(id!!, newAdminDTO)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun delAdminById(call: ApplicationCall) {
        apiCatch(call) {
            val id = getAdminIdFromRequest(call)
            val token = getAdminTokenFromHeaders(call)

            adminHeaderHandle(call, token, id) {
                AdminsController.deleteById(id!!)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun unloginAdmin(call: ApplicationCall) {
        apiCatch(call) {
            val id = getAdminIdFromRequest(call)
            val token = getAdminTokenFromHeaders(call)

            adminHeaderHandle(call, token, id) {
                AdminTokensController.delete(token!!)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun delAvatar(call: ApplicationCall) {
        apiCatch(call) {
            val id = getAdminIdFromRequest(call)
            val token = getAdminTokenFromHeaders(call)

            adminHeaderHandle(call, token, id) {
                val adminRow = AdminsController.selectById(id!!)

                adminRow?.avatarId?.let { avatarId ->
                    FilesController.deleteImageById(avatarId)
                }
                call.respond(HttpStatusCode.OK)
            }
        }
    }

}