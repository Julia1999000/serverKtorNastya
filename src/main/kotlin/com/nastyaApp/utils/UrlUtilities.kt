package com.nastyaApp.utils

import java.util.UUID


fun generateImagUrl(id: UUID?): String? {
    return id?.let {
        "http://${System.getenv("SERVER_HOST")}:${System.getenv("SERVER_PORT")}/file/images/open/${it}"
    }
}