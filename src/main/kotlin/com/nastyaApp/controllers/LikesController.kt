package com.nastyaApp.controllers

import com.nastyaApp.controllers.ComsController.ComsTable
import com.nastyaApp.controllers.UsersController.UsersTable
import com.nastyaApp.models.LikeTableRowDTO
import com.nastyaApp.utils.dbQuery
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

object LikesController {
    object LikersToComsTable : UUIDTable(name = "likers_to_coms") {
        val comId: Column<EntityID<UUID>> = reference("com_id", ComsTable.id, ReferenceOption.CASCADE)
        val userId: Column<EntityID<UUID>> = reference("user_id", UsersTable.id, ReferenceOption.CASCADE)
    }

    suspend fun insertLike(userId: UUID, comId: UUID): UUID {
        return dbQuery {
            LikersToComsTable.insertAndGetId {
                it[this.comId] = comId
                it[this.userId] = userId
            }.value
        }
    }

    suspend fun selectLikeById(likeId: UUID): LikeTableRowDTO? {
        return dbQuery {
            LikersToComsTable.select { LikersToComsTable.id eq likeId }.singleOrNull()?.toLikeTableRowDTO()
        }
    }

    suspend fun selectLikeByUserIdAndComId(userId: UUID, comId: UUID): LikeTableRowDTO? {
        return dbQuery {
            LikersToComsTable.select { (LikersToComsTable.comId eq comId) and (LikersToComsTable.userId eq userId) }
                .singleOrNull()?.toLikeTableRowDTO()
        }
    }

    suspend fun deleteLikeById(likeId: UUID) {
        return dbQuery {
            LikersToComsTable.deleteWhere { this.id eq likeId }
        }
    }

    suspend fun selectCountAllLikesByComId(comId: UUID): Int {
        return dbQuery {
            LikersToComsTable.select { LikersToComsTable.comId eq comId }.count().toInt()
        }
    }

    suspend fun selectAllLikesByComId(comId: UUID): List<LikeTableRowDTO> {
        return dbQuery {
            LikersToComsTable.select { LikersToComsTable.comId eq comId }.map { it.toLikeTableRowDTO() }
        }
    }

    private fun ResultRow.toLikeTableRowDTO(): LikeTableRowDTO {
        return LikeTableRowDTO(
            id = this[LikersToComsTable.id].value,
            likerId = this[LikersToComsTable.userId].value,
            comId = this[LikersToComsTable.comId].value
        )
    }
}