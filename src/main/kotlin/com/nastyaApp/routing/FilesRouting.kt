package com.nastyaApp.routing

import com.nastyaApp.services.FilesService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.configureFilesRouting() {
    route("file") {
        post("/images/uploadByUser/{user_id}") {
            FilesService.uploadFileByUser(call)
        }

        post("/images/uploadByAdmin/{admin_id}") {
            FilesService.uploadFileByAdmin(call)
        }

        get("/images/download/{file_id}") {
            FilesService.downloadFileById(call)
        }

        get("/images/open/{file_id}") {
            FilesService.openFileById(call)
        }
    }
}