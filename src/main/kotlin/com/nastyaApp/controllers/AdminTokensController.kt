package com.nastyaApp.controllers

import com.nastyaApp.controllers.AdminsController.AdminsTable
import com.nastyaApp.models.AdminTokenTableRowModel
import com.nastyaApp.utils.dbQuery
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

object AdminTokensController {
    object AdminTokensTable : UUIDTable(name = "admin_tokens", columnName = "token") {
        val adminId: Column<EntityID<UUID>> = reference("admin_id", AdminsTable.id, ReferenceOption.CASCADE)
    }

    suspend fun insertAdminToken(adminId: UUID): UUID {
        return dbQuery {
            AdminTokensTable.insertAndGetId {
                it[this.adminId] = adminId
            }.value
        }
    }

    suspend fun selectAdminToken(token: UUID): AdminTokenTableRowModel? {
        return dbQuery {
            AdminTokensTable.select { AdminTokensTable.id eq token }.singleOrNull()?.toTokenTableRowModel()
        }
    }

    suspend fun deleteAdminToken(token: UUID) {
        dbQuery {
            AdminTokensTable.deleteWhere { this.id eq token }
        }
    }

    private fun ResultRow.toTokenTableRowModel(): AdminTokenTableRowModel {
        return AdminTokenTableRowModel(
            token = this[AdminTokensTable.id].value,
            adminId = this[AdminTokensTable.adminId].value
        )
    }

}