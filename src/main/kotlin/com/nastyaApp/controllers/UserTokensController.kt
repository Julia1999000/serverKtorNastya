package com.nastyaApp.controllers

import com.nastyaApp.controllers.UsersController.UsersTable
import com.nastyaApp.models.UserTokenTableRowModel
import com.nastyaApp.utils.dbQuery
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.UUID

object UserTokensController {
    object UserTokensTable : UUIDTable(name = "user_tokens", columnName = "token") {
        val userId: Column<EntityID<UUID>> = reference("user_id", UsersTable.id, ReferenceOption.CASCADE)
    }

    suspend fun insert(userId: UUID): UUID {
        return dbQuery {
            UserTokensTable.insertAndGetId {
                it[this.userId] = userId
            }.value
        }
    }

    suspend fun select(token: UUID): UserTokenTableRowModel? {
        return dbQuery {
            UserTokensTable.select { UserTokensTable.id eq token }.singleOrNull()?.toTokenTableRowModel()
        }
    }

    suspend fun delete(token: UUID) {
        dbQuery {
            UserTokensTable.deleteWhere { this.id eq token }
        }
    }

    private fun ResultRow.toTokenTableRowModel(): UserTokenTableRowModel {
        return UserTokenTableRowModel(
            token = this[UserTokensTable.id].value,
            userId = this[UserTokensTable.userId].value
        )
    }
}
