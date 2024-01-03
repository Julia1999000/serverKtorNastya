package com.nastyaApp.plugins

import com.nastyaApp.controllers.*
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val database = Database.connect(
        url = System.getenv("DB_URL"),
        driver = "org.postgresql.Driver",
        user = System.getenv("DB_USER"),
        password = System.getenv("DB_PASSWORD")
    )

    transaction {
        addLogger(StdOutSqlLogger)

        SchemaUtils.create(
            FilesController.FileTypesTable,
            FilesController.ImagesTable,
            UsersController.UsersTable,
            UserTokensController.UserTokensTable,
            AdminsController.AdminsTable,
            AdminTokensController.AdminTokensTable,
            ComsController.ComStatusesTable,
            ComsController.ComsTable,
            CommentController.CommentsTable,
            LikesController.LikersToComsTable,
            BoardController.BoardStatusesTable,
            BoardController.BoardsTable,
            BoardController.BoardsToComsTable
        )
    }
}