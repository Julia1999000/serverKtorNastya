package com.nastyaApp.services

import com.nastyaApp.controllers.FilesController
import com.nastyaApp.utils.apiCatch
import com.nastyaApp.utils.getIdFromRequest
import com.nastyaApp.utils.getTokenFromHeaders
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

object FilesService {
    private val ACCEPTED_IMAGE_TYPES = listOf("PNG", "JPG", "JPEG")

    suspend fun uploadFile(call: ApplicationCall) {
        apiCatch(call) {
            getTokenFromHeaders(call) ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Token not found")

            val partsList = call.receiveMultipart().readAllParts()
            if (partsList.size > 1) {
                return@apiCatch call.respond(HttpStatusCode.BadRequest, "A lot of data")
            }
            partsList.getOrNull(0)?.let { part ->
                if (part.name != "file" || part !is PartData.FileItem) {
                    return@apiCatch call.respond(HttpStatusCode.BadRequest, "Invalid key name or type")
                }

                val fileType = part.contentType?.contentSubtype?.uppercase()
                if (fileType !in ACCEPTED_IMAGE_TYPES) {
                    return@apiCatch call.respond(HttpStatusCode.BadRequest, "Invalid file type")
                }

                val fileBytes = part.streamProvider().readBytes()
                fileType?.let { FilesController.insertImage(fileBytes, it) }?.let { id ->
                    call.respond(HttpStatusCode.Created, id)
                }
            }
        }
    }

    suspend fun downloadFileById(call: ApplicationCall) {
        apiCatch(call) {
            val id = getIdFromRequest(call)
            val fileImageDTO = id?.let { FilesController.selectImageById(id) }

            if (fileImageDTO == null) {
                call.respond(HttpStatusCode.NotFound, "File not found")
            } else {
                call.response.header(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Attachment.withParameter(
                        ContentDisposition.Parameters.FileName, fileImageDTO.getFailName()
                    ).toString()
                )

                fileImageDTO.getFile().let {
                    call.respondFile(it)
                    it.delete()
                }
            }
        }
    }

    suspend fun openFileById(call: ApplicationCall) {
        apiCatch(call) {
            val id = getIdFromRequest(call)
            val fileImageDTO = id?.let { FilesController.selectImageById(id) }

            if (fileImageDTO == null) {
                call.respond(HttpStatusCode.NotFound, "File not found")
            } else {
                call.response.header(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Inline.withParameter(
                        ContentDisposition.Parameters.FileName, fileImageDTO.getFailName()
                    ).toString()
                )

                fileImageDTO.getFile().let {
                    call.respondFile(it)
                    it.delete()
                }
            }
        }
    }

}