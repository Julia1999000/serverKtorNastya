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
            val adminDTO = AdminsController.selectAdminById(adminId)
            val token = AdminTokensController.insertAdminToken(adminId)

            adminDTO?.toIdentityAdminResponse(
                token, generateImagUrl(call.request.host(), call.request.port(), adminDTO.avatarId)
            )?.let { response ->
                call.respond(HttpStatusCode.Created, response)
            }
        }
    }

    suspend fun loginAdmin(call: ApplicationCall) {
        apiCatch(call) {
            val request = call.receive<LoginRequest>()
            val adminDTO = AdminsController.selectAdminByLogin(request.login)
                ?: return@apiCatch call.respond(HttpStatusCode.NotFound, "Login not found")

            if (request.password != adminDTO.password) {
                return@apiCatch call.respond(HttpStatusCode.OK, "Invalid password")
            }

            val token = AdminTokensController.insertAdminToken(adminDTO.id)
            val response = adminDTO.toIdentityAdminResponse(
                token, generateImagUrl(call.request.host(), call.request.port(), adminDTO.avatarId))
            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun getAllAdmins(call: ApplicationCall) {
        apiCatch(call) {
            val response = AdminsController.selectAllAdmins().map {
                it.toAdminInfoResponse(generateImagUrl(call.request.host(), call.request.port(), it.avatarId))
            }
            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun getAdminById(call: ApplicationCall) {
        apiCatch(call) {
            val adminId = getAdminIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.NotFound, "Admin id not found")

            val adminDTO = AdminsController.selectAdminById(adminId)
                ?: return@apiCatch call.respond(HttpStatusCode.NotFound, "Admin not found")

            val response = adminDTO.toAdminInfoResponse(
                generateImagUrl(call.request.host(), call.request.port(), adminDTO.avatarId))
            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun updateAminInfo(call: ApplicationCall) {
        apiCatch(call) {
            val token = getAdminTokenFromHeaders(call)

            adminHeaderHandle(call, token) {
                val adminId = AdminTokensController.selectAdminToken(token!!)?.adminId
                    ?: return@adminHeaderHandle call.respond(HttpStatusCode.BadRequest, "Admin not found")

                val request = call.receive<UpdateAdminInfoRequest>()
                val newAdminDTO = request.toNewAdminDTO()

                val oldAvatarId = AdminsController.selectAdminById(adminId)?.avatarId
                if (newAdminDTO.avatarId != null && oldAvatarId != null) {
                    FilesController.deleteImageById(oldAvatarId)
                }

                AdminsController.updateAdminById(adminId, newAdminDTO)
                val adminDTO = AdminsController.selectAdminById(adminId)
                adminDTO?.toAdminInfoResponse(
                    generateImagUrl(call.request.host(), call.request.port(), adminDTO.avatarId)
                )?.let {
                    call.respond(HttpStatusCode.OK, it)
                }
            }
        }
    }

    suspend fun updateAdminSecretInfo(call: ApplicationCall) {
        apiCatch(call) {
            val token = getAdminTokenFromHeaders(call)

            adminHeaderHandle(call, token) {
                val adminId = AdminTokensController.selectAdminToken(token!!)?.adminId
                    ?: return@adminHeaderHandle call.respond(HttpStatusCode.BadRequest, "Admin not found")

                val request = call.receive<UpdateSecretInfoRequest>()
                val newAdminDTO = request.toNewAdminDTO()

                AdminsController.updateAdminById(adminId, newAdminDTO)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun delAdmin(call: ApplicationCall) {
        apiCatch(call) {
            val token = getAdminTokenFromHeaders(call)

            adminHeaderHandle(call, token) {
                val adminId = AdminTokensController.selectAdminToken(token!!)?.adminId
                    ?: return@adminHeaderHandle call.respond(HttpStatusCode.BadRequest, "Admin not found")

                AdminsController.deleteAdminById(adminId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun unloginAdmin(call: ApplicationCall) {
        apiCatch(call) {
            val token = getAdminTokenFromHeaders(call)

            adminHeaderHandle(call, token) {
                AdminTokensController.deleteAdminToken(token!!)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun delAvatar(call: ApplicationCall) {
        apiCatch(call) {
            val token = getAdminTokenFromHeaders(call)

            adminHeaderHandle(call, token) {
                val adminId = AdminTokensController.selectAdminToken(token!!)?.adminId
                    ?: return@adminHeaderHandle call.respond(HttpStatusCode.BadRequest, "Admin not found")

                val adminRow = AdminsController.selectAdminById(adminId)

                adminRow?.avatarId?.let { avatarId ->
                    FilesController.deleteImageById(avatarId)
                }
                call.respond(HttpStatusCode.OK)
            }
        }
    }

}