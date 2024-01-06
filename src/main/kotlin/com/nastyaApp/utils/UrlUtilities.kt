package com.nastyaApp.utils

import java.util.UUID


fun generateImagUrl(host: String, port:Int, id: UUID?): String? {
    return id?.let {
        "http://${host}:${port}/file/images/open/${it}"
    }
}