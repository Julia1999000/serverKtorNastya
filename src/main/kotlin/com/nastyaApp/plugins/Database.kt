package com.nastyaApp.plugins

import com.nastyaApp.controllers.BoardController
import com.nastyaApp.controllers.FilesController
import com.nastyaApp.controllers.UserTokensController
import com.nastyaApp.controllers.UsersController
import com.nastyaApp.controllers.AdminsController
import com.nastyaApp.controllers.ComsController
import com.nastyaApp.controllers.AdminTokensController
import com.nastyaApp.controllers.CommentController
import com.nastyaApp.controllers.LikesController
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val database = Database.connect(
        System.getenv("DB_URL"),
        driver = "org.postgresql.Driver",
        user = System.getenv("DB_USER"),
        password = System.getenv("DB_PASSWORD")
    )

    transaction {
        addLogger(StdOutSqlLogger)

    }
}