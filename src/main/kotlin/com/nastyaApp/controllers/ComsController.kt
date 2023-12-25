package com.nastyaApp.controllers

import com.nastyaApp.controllers.FilesController.ImagesTable
import com.nastyaApp.controllers.UsersController.UsersTable
import com.nastyaApp.models.ComTableRowDTO
import com.nastyaApp.models.NewComDTO
import com.nastyaApp.utils.dbQuery
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.kotlin.datetime.*
import java.util.UUID

object ComsController {

    private const val CREATED_STATUS_COM = "CREATED"
    private const val PUBLISHED_STATUS_COM = "PUBLISHED"
    private const val CHECKABLE_STATUS_COM = "CHECKABLE"
    private const val COMS_LIMIT = 20 // TODO for pagination

    object ComStatusesTable : IdTable<String>("com_statuses") {
        override val id: Column<EntityID<String>> = varchar("id", 10).entityId()
        override val primaryKey = PrimaryKey(id)
    }

    object ComsTable : UUIDTable(name = "coms") {
        val description: Column<String> = text("description")
        val imageId: Column<EntityID<UUID>?> = reference("image_id", ImagesTable.id, ReferenceOption.SET_NULL).nullable()
        val createdDate: Column<Instant> = timestamp("created_date").defaultExpression(CurrentTimestamp())
        val authorId: Column<EntityID<UUID>> = reference("author_id", UsersTable.id, ReferenceOption.CASCADE)
        val statusId: Column<EntityID<String>> = reference("status_id", ComStatusesTable.id, ReferenceOption.RESTRICT)
    }

    suspend fun insertCom(com: NewComDTO): UUID? {
        return dbQuery {
            selectComStatusId(CREATED_STATUS_COM)?.let { statusId ->
                ComsTable.insertAndGetId {
                    it[this.description] = com.description
                    it[this.imageId] = com.imageId
                    it[this.createdDate] = com.createdDate
                    it[this.authorId] = com.authorId
                    it[this.statusId] = statusId
                }.value
            }
        }
    }

    private suspend fun selectComStatusId(status: String): EntityID<String>? {
        return dbQuery {
            ComStatusesTable.select { ComStatusesTable.id eq status }.singleOrNull()?.get(ComStatusesTable.id)
        }
    }

    suspend fun selectComById(comId: UUID): ComTableRowDTO? {
        return dbQuery {
            ComsTable.select { ComsTable.id eq comId }.singleOrNull()?.toComTableRowDTO()
        }
    }

    suspend fun selectAllComsByAuthorId(authorId: UUID): List<ComTableRowDTO> {
        return dbQuery {
            ComsTable.select { ComsTable.authorId eq authorId }.map {
                it.toComTableRowDTO()
            }
        }
    }

    suspend fun selectPublishedComsByAuthorId(authorId: UUID): List<ComTableRowDTO> {
        return dbQuery {
            selectComStatusId(PUBLISHED_STATUS_COM)?.let { statusId ->
                ComsTable.select { (ComsTable.authorId eq authorId) and (ComsTable.statusId eq statusId) }
            }?.map { it.toComTableRowDTO() } ?: listOf()
        }
    }

    suspend fun setStatusPublished(comId: UUID) {
        setStatus(comId, PUBLISHED_STATUS_COM)
    }

    suspend fun setStatusCheckable(comId: UUID) {
        setStatus(comId, CHECKABLE_STATUS_COM)
    }

    private suspend fun setStatus(comId: UUID, status: String) {
        return dbQuery {
            selectComStatusId(status)?.let { statusId ->
                ComsTable.update({ ComsTable.id eq comId }) {
                    it[this.statusId] = statusId
                }
            }
        }
    }

    suspend fun deleteComById(comId: UUID) {
        return dbQuery {
            ComsTable.deleteWhere { this.id eq comId }
        }
    }

    suspend fun selectAllComsCreated(offsetId: String?): List<ComTableRowDTO> { //TODO pagination
        return selectAllComsByStatus(CREATED_STATUS_COM)
    }

    suspend fun selectAllComsPublished(offsetId: String?): List<ComTableRowDTO> { //TODO pagination
        return selectAllComsByStatus(PUBLISHED_STATUS_COM)
    }

    private suspend fun selectAllComsByStatus(status: String): List<ComTableRowDTO> {
        return dbQuery {
            selectComStatusId(status)?.let { statusId ->
                ComsTable.select { ComsTable.statusId eq statusId }.limit(COMS_LIMIT).map { it.toComTableRowDTO() }
            } ?: listOf()
        }
    }

    private fun ResultRow.toComTableRowDTO(): ComTableRowDTO {
        return ComTableRowDTO(
            id = this[ComsTable.id].value,
            description = this[ComsTable.description],
            imageId = this[ComsTable.imageId]?.value,
            createdDate = this[ComsTable.createdDate],
            authorId = this[ComsTable.authorId].value,
            status = this[ComsTable.statusId].value
        )
    }
}