package com.nastyaApp.controllers

import com.nastyaApp.controllers.FilesController.ImagesTable
import com.nastyaApp.models.AdminTableRowDTO
import com.nastyaApp.models.AnonymDTO
import com.nastyaApp.models.NewAdminDTO
import com.nastyaApp.utils.dbQuery
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

object AdminsController {
    object AdminsTable : UUIDTable(name = "admins") {
        val name: Column<String> = text("name")
        val login: Column<String> = text("login").uniqueIndex()
        val password: Column<String> = text("password")
        val avatarId: Column<EntityID<UUID>?> = reference("avatar_id", ImagesTable.id, ReferenceOption.SET_NULL).nullable()
    }

    suspend fun insert(user: AnonymDTO): UUID {
        return dbQuery {
            AdminsTable.insertAndGetId {
                it[this.name] = user.name
                it[this.login] = user.login
                it[this.password] = user.password
            }.value
        }
    }

    suspend fun selectAdminById(adminId: UUID): AdminTableRowDTO? {
        return dbQuery {
            AdminsTable.select { AdminsTable.id eq adminId }.singleOrNull()?.toAdminTableRowDTO()
        }
    }

    suspend fun selectAdminByLogin(login: String): AdminTableRowDTO? {
        return dbQuery {
            AdminsTable.select { AdminsTable.login eq login }.singleOrNull()?.toAdminTableRowDTO()
        }
    }

    suspend fun updateAdminById(adminId: UUID, newAdmin: NewAdminDTO) {
        dbQuery {
            AdminsTable.update({ AdminsTable.id eq adminId }) {
                newAdmin.name?.let { newName -> it[this.name] = newName }
                newAdmin.login?.let { newLogin -> it[this.login] = newLogin }
                newAdmin.password?.let { newPassword -> it[this.password] = newPassword }
                newAdmin.avatarId?.let { newAvatarId -> it[this.avatarId] = newAvatarId }
            }
        }
    }

    suspend fun selectAllAdmins(): List<AdminTableRowDTO> {
        return dbQuery {
            AdminsTable.selectAll().map { it.toAdminTableRowDTO() }
        }
    }

    suspend fun deleteAdminById(adminId: UUID) {
        dbQuery {
            AdminsTable.deleteWhere { this.id eq adminId }
        }
    }


    private fun ResultRow.toAdminTableRowDTO(): AdminTableRowDTO {
        return AdminTableRowDTO(
            id = this[AdminsTable.id].value,
            name = this[AdminsTable.name],
            login = this[AdminsTable.login],
            password = this[AdminsTable.password],
            avatarId = this[AdminsTable.avatarId]?.value
        )
    }
}