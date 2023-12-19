package com.nastyaApp.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val database = Database.connect("jdbc:postgresql://localhost:5432/nastyaapp", // TODO nameDB
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "2514" // TODO password
    )

    transaction {
        addLogger(StdOutSqlLogger)
    }
}