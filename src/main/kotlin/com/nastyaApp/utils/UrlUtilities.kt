package com.nastyaApp.utils

import java.util.UUID

fun generateImagUrl(id: String?): String? {
    return "url $id" //TODO
}

fun generateImagUrl(id: UUID?): String? {
    return "url ${id.toString()}" //TODO
}