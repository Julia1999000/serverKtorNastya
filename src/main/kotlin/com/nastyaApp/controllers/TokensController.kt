package com.nastyaApp.controllers

import com.nastyaApp.controllers.UsersController.UsersTable
import com.nastyaApp.models.TokenTableRowModel
import com.nastyaApp.utils.dbQuery
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.UUID

object TokensController {
    object TokensTable : UUIDTable(name = "tokens", columnName = "token") {
        val userId: Column<EntityID<UUID>> = reference("user_id", UsersTable.id, ReferenceOption.CASCADE)
    }

    suspend fun insert(userId: UUID): UUID {
        return dbQuery {
            TokensTable.insertAndGetId {
                it[this.userId] = userId
            }.value
        }
    }

    suspend fun select(token: UUID): TokenTableRowModel? {
        return dbQuery {
            TokensTable.select { TokensTable.id eq token }.singleOrNull()?.toTokenTableRowModel()
        }
    }

    suspend fun delete(token: UUID) {
        dbQuery {
            TokensTable.deleteWhere { this.id eq token }
        }
    }

    private fun ResultRow.toTokenTableRowModel(): TokenTableRowModel {
        return TokenTableRowModel(
            token = this[TokensTable.id].value,
            userId = this[TokensTable.userId].value
        )
    }
}
