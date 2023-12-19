package com.nastyaApp.controllers

import com.nastyaApp.models.FileImageTableRowDTO
import com.nastyaApp.utils.dbQuery
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

object FilesController {
    object FileTypesTable : IdTable<String>("file_types") {
        override val id: Column<EntityID<String>> = varchar("id", 6).entityId()
        override val primaryKey = PrimaryKey(id)
    }

    object ImagesTable : UUIDTable(name = "images") {
        val file: Column<ByteArray> = binary("file")
        val typeId: Column<EntityID<String>> = reference("type_id", FileTypesTable.id, ReferenceOption.CASCADE)
    }

    suspend fun insertImage(fileBytes: ByteArray, type: String): UUID? {
        return dbQuery {
            selectFileTypeId(type)?.let { typeId ->
                ImagesTable.insertAndGetId {
                    it[this.file] = fileBytes
                    it[this.typeId] = typeId
                }.value
            }
        }
    }

    suspend fun selectImageById(imageId: UUID): FileImageTableRowDTO? {
        return dbQuery {
            ImagesTable.select { ImagesTable.id eq imageId }.singleOrNull()?.toFileImageDTO()
        }
    }

    private suspend fun selectFileTypeId(type: String): EntityID<String>? {
        return dbQuery {
            FileTypesTable.select { FileTypesTable.id eq type }.singleOrNull()?.get(FileTypesTable.id)
        }
    }

    suspend fun deleteImageById(imageId: UUID) {
        dbQuery {
            ImagesTable.deleteWhere { this.id eq imageId }
        }
    }

    private fun ResultRow.toFileImageDTO(): FileImageTableRowDTO {
        return FileImageTableRowDTO(
            id = this[ImagesTable.id].value,
            fileBytes = this[ImagesTable.file],
            type = this[ImagesTable.typeId].value
        )
    }

}