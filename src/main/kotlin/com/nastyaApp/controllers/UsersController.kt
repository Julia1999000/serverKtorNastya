package com.nastyaApp.controllers

import com.nastyaApp.models.*
import com.nastyaApp.utils.dbQuery
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

object UsersController {

    //TODO add avatar img
    private object UsersTable : UUIDTable(name = "users") {
        val name: Column<String> = varchar("name", 10)
        val login: Column<String> = varchar("login", 10)
        val password: Column<String> = varchar("password", 10)
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

    suspend fun selectByLogin(login: String): UserDTO? {
        return dbQuery {
            UsersTable.select { UsersTable.login eq login }.singleOrNull()?.toUserDTO()
        }
    }

    suspend fun selectById(id: UUID): UserDTO? {
        return dbQuery {
            UsersTable.select { UsersTable.id eq id }.singleOrNull()?.toUserDTO()
        }
    }

    suspend fun updateById(id: UUID, newUser: NewUserIfo) {
        dbQuery {
            UsersTable.update({ UsersTable.id eq id }) {
                newUser.name?.let { newName -> it[this.name] = newName }
                newUser.login?.let { newLogin -> it[this.login] = newLogin }
                newUser.password?.let { newPassword -> it[this.password] = newPassword }
            }
        }
    }

    //TODO pagination
    suspend fun selectAllUsers(offsetId: String?): List<UserDTO> {
        return dbQuery {
            UsersTable.selectAll()
                .limit(USERS_LIMIT)
                .map {
                    it.toUserDTO()
                }
        }
    }

    suspend fun deleteById(id: UUID) {
        dbQuery {
            UsersTable.deleteWhere { this.id eq id }
        }
    }

    private fun ResultRow.toUserDTO(): UserDTO {
        return UserDTO(
            id = this[UsersTable.id].value.toString(),
            name = this[UsersTable.name],
            login = this[UsersTable.login],
            password = this[UsersTable.password]
        )
    }

}

