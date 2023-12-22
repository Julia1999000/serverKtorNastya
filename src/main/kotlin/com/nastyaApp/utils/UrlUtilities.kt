package com.nastyaApp.utils

import java.util.UUID


fun generateImagUrl(id: UUID?): String? {
    return id?.let {
        "http://127.0.0.1:8080/file/images/open/${it}"
    }
}