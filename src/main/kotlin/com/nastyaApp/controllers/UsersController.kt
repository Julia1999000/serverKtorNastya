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

    suspend fun insertUser(user: AnonymDTO): UUID {
        return dbQuery {
            UsersTable.insertAndGetId {
                it[this.name] = user.name
                it[this.login] = user.login
                it[this.password] = user.password
            }.value
        }
    }

    suspend fun selectUserByLogin(login: String): UserTableRowDTO? {
        return dbQuery {
            UsersTable.select { UsersTable.login eq login }.singleOrNull()?.toUserTableRowDTO()
        }
    }

    suspend fun selectUserById(userId: UUID): UserTableRowDTO? {
        return dbQuery {
            UsersTable.select { UsersTable.id eq userId }.singleOrNull()?.toUserTableRowDTO()
        }
    }

    suspend fun updateUserById(userId: UUID, newUser: NewUserDTO) {
        dbQuery {
            UsersTable.update({ UsersTable.id eq userId }) {
                newUser.name?.let { newName -> it[this.name] = newName }
                newUser.login?.let { newLogin -> it[this.login] = newLogin }
                newUser.password?.let { newPassword -> it[this.password] = newPassword }
                newUser.avatarId?.let { newAvatarId -> it[this.avatarId] = newAvatarId }
            }
        }
    }

    //TODO pagination
    suspend fun selectAllUsers(offsetId: String?): List<UserTableRowDTO> {
        return dbQuery {
            UsersTable.selectAll().limit(USERS_LIMIT).map { it.toUserTableRowDTO() }
        }
    }

    suspend fun deleteUserById(userId: UUID) {
        dbQuery {
            UsersTable.deleteWhere { this.id eq userId }
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

