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

    private const val COM_STATUS_CREATED = "CREATED"
    private const val COM_STATUS_PUBLISHED = "PUBLISHED"
    private const val COM_STATUS_CHECKABLE = "CHECKABLE"
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

    suspend fun insert(com: NewComDTO): UUID? {
        return dbQuery {
            selectComStatusId(COM_STATUS_CREATED)?.let { statusId ->
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

    suspend fun selectById(id: UUID): ComTableRowDTO? {
        return dbQuery {
            ComsTable.select { ComsTable.id eq id }.singleOrNull()?.toComTableRowDTO()
        }
    }

    suspend fun selectAllByAuthorId(authorId: UUID): List<ComTableRowDTO> {
        return dbQuery {
            ComsTable.select { ComsTable.authorId eq authorId }.map {
                it.toComTableRowDTO()
            }
        }
    }

    suspend fun selectPublishedByAuthorId(authorId: UUID): List<ComTableRowDTO> {
        return dbQuery {
            selectComStatusId(COM_STATUS_PUBLISHED)?.let { statusId ->
                ComsTable.select { (ComsTable.authorId eq authorId) and (ComsTable.statusId eq statusId) }
            }?.map { it.toComTableRowDTO() } ?: listOf()
        }
    }

    suspend fun setStatusPublished(id: UUID) {
        setStatus(id, COM_STATUS_PUBLISHED)
    }

    suspend fun setStatusCheckable(id: UUID) {
        setStatus(id, COM_STATUS_CHECKABLE)
    }

    private suspend fun setStatus(id: UUID, status: String) {
        return dbQuery {
            selectComStatusId(status)?.let { statusId ->
                ComsTable.update({ ComsTable.id eq id }) {
                    it[this.statusId] = statusId
                }
            }
        }
    }

    suspend fun deleteById(id: UUID) {
        return dbQuery {
            ComsTable.deleteWhere { this.id eq id }
        }
    }

    suspend fun selectAllCreated(offsetId: String?): List<ComTableRowDTO> { //TODO pagination
        return selectAllByStatus(COM_STATUS_CREATED)
    }

    suspend fun selectAllPublished(offsetId: String?): List<ComTableRowDTO> { //TODO pagination
        return selectAllByStatus(COM_STATUS_PUBLISHED)
    }

    private suspend fun selectAllByStatus(status: String): List<ComTableRowDTO> {
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