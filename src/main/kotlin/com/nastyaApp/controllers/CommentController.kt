package com.nastyaApp.controllers

import com.nastyaApp.controllers.ComsController.ComsTable
import com.nastyaApp.controllers.UsersController.UsersTable
import com.nastyaApp.models.CommentTableRowDTO
import com.nastyaApp.models.NewCommentDTO
import com.nastyaApp.utils.dbQuery
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object CommentController {

    object CommentsTable : UUIDTable("comments") {
        val text: Column<String> = text("text")
        val authorId: Column<EntityID<UUID>> = reference("author_id", UsersTable.id, ReferenceOption.CASCADE)
        val comId: Column<EntityID<UUID>> = reference("com_id", ComsTable.id, ReferenceOption.CASCADE)
        val createdDate: Column<Instant> = timestamp("created_date").defaultExpression(CurrentTimestamp())
    }

    suspend fun insert(comment: NewCommentDTO): UUID {
        return dbQuery {
            CommentsTable.insertAndGetId {
                it[this.text] = comment.text
                it[this.authorId] = comment.authorId
                it[this.comId] = comment.comId
                it[this.createdDate] = comment.createdDate
            }.value
        }
    }

    suspend fun deleteById(id: UUID) {
        return dbQuery {
            CommentsTable.deleteWhere { this.id eq id }
        }
    }

    suspend fun selectById(id: UUID): CommentTableRowDTO? {
        return dbQuery {
            CommentsTable.select { CommentsTable.id eq id }.singleOrNull()?.toCommentTableRowDTO()
        }
    }

    suspend fun selectAllByComId(id: UUID): List<CommentTableRowDTO> {
        return dbQuery {
            CommentsTable.select { CommentsTable.comId eq id }
                .orderBy(CommentsTable.createdDate to SortOrder.DESC)
                .map { it.toCommentTableRowDTO() }
        }
    }

    suspend fun selectCountAllByComId(id: UUID): Int {
        return dbQuery {
            CommentsTable.select { CommentsTable.comId eq id }.count().toInt()
        }
    }

    private fun ResultRow.toCommentTableRowDTO(): CommentTableRowDTO {
        return CommentTableRowDTO(
            id = this[CommentsTable.id].value,
            authorId = this[CommentsTable.authorId].value,
            comId = this[CommentsTable.comId].value,
            text = this[CommentsTable.text],
            createdDate = this[CommentsTable.createdDate]
        )
    }
}