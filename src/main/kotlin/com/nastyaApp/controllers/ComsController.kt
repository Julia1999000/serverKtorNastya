package com.nastyaApp.controllers

import com.nastyaApp.controllers.FilesController.ImagesTable
import com.nastyaApp.controllers.UsersController.UsersTable
import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import java.util.UUID

object ComsController {

    object ComsTable : UUIDTable(name = "coms") {
        val description: Column<String> = text("description")
        val imageId: Column<EntityID<UUID>?> = reference("image_id", ImagesTable.id, ReferenceOption.SET_NULL).nullable()
        val createDate: Column<LocalDateTime> = datetime("create_date").defaultExpression(CurrentDateTime)
        val authorId: Column<EntityID<UUID>> = reference("author_id", UsersTable.id, ReferenceOption.CASCADE)
    }

    object LikersToComsTable : UUIDTable(name = "likers_to_coms") {
        val comId: Column<EntityID<UUID>> = reference("com_id", ComsTable.id, ReferenceOption.CASCADE)
        val userId: Column<EntityID<UUID>> = reference("user_id", UsersTable.id, ReferenceOption.CASCADE)
    }
}