package com.nastyaApp.controllers

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column

object TokensController {

    private object TokensTable : UUIDTable(name = "tokens") {
        // TODO
    }

}
