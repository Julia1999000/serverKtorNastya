package com.nastyaApp.controllers

import com.nastyaApp.controllers.ComsController.ComsTable
import com.nastyaApp.controllers.UsersController.UsersTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.*

object LikersController {
    object LikersToComsTable : UUIDTable(name = "likers_to_coms") {
        val comId: Column<EntityID<UUID>> = reference("com_id", ComsTable.id, ReferenceOption.CASCADE)
        val userId: Column<EntityID<UUID>> = reference("user_id", UsersTable.id, ReferenceOption.CASCADE)
    }
}