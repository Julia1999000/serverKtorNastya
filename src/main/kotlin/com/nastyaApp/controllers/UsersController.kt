package com.nastyaApp.controllers

import com.nastyaApp.controllers.FilesController.ImagesTable
import com.nastyaApp.models.*
import com.nastyaApp.utils.dbQuery
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

object UsersController {

    object UsersTable : UUIDTable(name = "users") {
        val name: Column<String> = text("name")
        val login: Column<String> = text("login").uniqueIndex()
        val password: Column<String> = text("password")
        val avatarId: Column<EntityID<UUID>?> = reference("avatar_id", ImagesTable.id, ReferenceOption.SET_NULL).nullable()
    }

    private const val USERS_LIMIT = 20 // TODO for pagination

    suspend fun insert(user: AnonymDTO): UUID {
        return dbQuery {
            UsersTable.insertAndGetId {
                it[this.name] = user.name
                it[this.login] = user.login
                it[this.password] = user.password
            }.value
        }
    }

    suspend fun selectByLogin(login: String): UserTableRowDTO? {
        return dbQuery {
            UsersTable.select { UsersTable.login eq login }.singleOrNull()?.toUserTableRowDTO()
        }
    }

    suspend fun selectById(id: UUID): UserTableRowDTO? {
        return dbQuery {
            UsersTable.select { UsersTable.id eq id }.singleOrNull()?.toUserTableRowDTO()
        }
    }

    suspend fun updateById(id: UUID, newUser: NewUserDTO) {
        dbQuery {
            UsersTable.update({ UsersTable.id eq id }) {
                newUser.name?.let { newName -> it[this.name] = newName }
                newUser.login?.let { newLogin -> it[this.login] = newLogin }
                newUser.password?.let { newPassword -> it[this.password] = newPassword }
                newUser.avatarId?.let { newAvatarId -> it[this.avatarId] = newAvatarId }
            }
        }
    }

    //TODO pagination
    suspend fun selectAll(offsetId: String?): List<UserTableRowDTO> {
        return dbQuery {
            UsersTable.selectAll().limit(USERS_LIMIT).map { it.toUserTableRowDTO() }
        }
    }

    suspend fun deleteById(id: UUID) {
        dbQuery {
            UsersTable.deleteWhere { this.id eq id }
        }
    }

    private fun ResultRow.toUserTableRowDTO(): UserTableRowDTO {
        return UserTableRowDTO(
            id = this[UsersTable.id].value,
            name = this[UsersTable.name],
            login = this[UsersTable.login],
            password = this[UsersTable.password],
            avatarId = this[UsersTable.avatarId]?.value
        )
    }

}

