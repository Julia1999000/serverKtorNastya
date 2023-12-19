package com.nastyaApp.controllers

import com.nastyaApp.controllers.ComsController.ComsTable
import com.nastyaApp.controllers.UsersController.UsersTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.UUID

object BoardController {

    object BoardsTable : UUIDTable(name = "boards") {
        val name: Column<String> = text("name")
        val ownerId: Column<EntityID<UUID>> = reference("owner_id", UsersTable.id, ReferenceOption.CASCADE)
    }

    object BoardsToComsTable : UUIDTable(name = "boards_to_coms") {
        val boardId: Column<EntityID<UUID>> = reference("board_id", BoardsTable.id, ReferenceOption.CASCADE)
        val comId: Column<EntityID<UUID>> = reference("com_id", ComsTable.id, ReferenceOption.CASCADE)
    }

}