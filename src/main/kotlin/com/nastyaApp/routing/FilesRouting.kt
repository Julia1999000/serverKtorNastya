package com.nastyaApp.routing

import com.nastyaApp.services.FilesService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.configureFilesRouting() {
    route("file") {
        post("/images/upload") {
            FilesService.uploadFile(call)
        }

        get("/images/download/{id}") {
            FilesService.downloadFileById(call)
        }

        get("/images/open/{id}") {
            FilesService.openFileById(call)
        }
    }
}