package com.nastyaApp.models

import java.io.File
import java.util.*

class FileImageTableRowDTO(
    val id: UUID,
    val fileBytes: ByteArray,
    val type: String
) {

    fun getFailName(): String {
        return "${id}.${type}"
    }

    fun getFile(): File {
        val file = File(getFailName())
        file.writeBytes(fileBytes)
        return file
    }
}