package com.nastyaApp.controllers

import com.nastyaApp.controllers.ComsController.ComsTable
import com.nastyaApp.controllers.UsersController.UsersTable
import com.nastyaApp.models.*
import com.nastyaApp.utils.dbQuery
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.UUID

object BoardController {

    private const val PUBLIC_STATUS_BOARD = "PUBLIC"
    private const val PRIVATE_STATUS_BOARD = "PRIVATE"

    private val LIST_STATUSES = listOf(PUBLIC_STATUS_BOARD, PRIVATE_STATUS_BOARD)

    object BoardStatusesTable : IdTable<String>("board_statuses") {
        override val id: Column<EntityID<String>> = varchar("id", 10).entityId()
        override val primaryKey = PrimaryKey(id)
    }

    object BoardsTable : UUIDTable(name = "boards") {
        val name: Column<String> = text("name")
        val ownerId: Column<EntityID<UUID>> = reference("owner_id", UsersTable.id, ReferenceOption.CASCADE)
        val statusId: Column<EntityID<String>> = reference("status_id", BoardStatusesTable.id, ReferenceOption.RESTRICT)
    }

    object BoardsToComsTable : UUIDTable(name = "boards_to_coms") {
        val boardId: Column<EntityID<UUID>> = reference("board_id", BoardsTable.id, ReferenceOption.CASCADE)
        val comId: Column<EntityID<UUID>> = reference("com_id", ComsTable.id, ReferenceOption.CASCADE)
    }

    suspend fun insertBoard(board: NewBoardDTO): UUID? {
        return dbQuery {
            selectBoardStatusId(board.status)?.let { statusId ->
                BoardsTable.insertAndGetId {
                    it[this.name] = board.name
                    it[this.ownerId] = board.ownerId
                    it[this.statusId] = statusId
                }.value
            }
        }
    }

    suspend fun insertBoardStatuses() {
        return dbQuery {
            LIST_STATUSES.forEach { boardStatus ->
                BoardStatusesTable.insert {
                    it[this.id] = boardStatus
                }
            }
        }
    }

    suspend fun isEmptyBoardStatusesTable(): Boolean {
        return dbQuery {
            BoardStatusesTable.selectAll().empty()
        }
    }

    suspend fun selectBoardStatusId(status: String): EntityID<String>? {
        return dbQuery {
            BoardStatusesTable.select { BoardStatusesTable.id eq status }.singleOrNull()?.get(BoardStatusesTable.id)
        }
    }

    suspend fun selectBoardById(boardId: UUID): BoardTableRowDTO? {
        return dbQuery {
            BoardsTable.select { BoardsTable.id eq boardId }.singleOrNull()?.toBoardTableRowDTO()
        }
    }

    suspend fun deleteBoardById(boardId: UUID) {
        return dbQuery {
            BoardsTable.deleteWhere { this.id eq boardId }
        }
    }

    suspend fun selectAllPublicBoardsByUserId(userId: UUID): List<BoardTableRowDTO> {
        return dbQuery {
            selectBoardStatusId(PUBLIC_STATUS_BOARD)?.let { statusId ->
                BoardsTable.select { (BoardsTable.ownerId eq userId) and (BoardsTable.statusId eq statusId) }
            }?.map { it.toBoardTableRowDTO() } ?: listOf()
        }
    }

    suspend fun selectAllBoardsByUserId(userId: UUID): List<BoardTableRowDTO> {
        return dbQuery {
            BoardsTable.select { BoardsTable.ownerId eq userId }.map { it.toBoardTableRowDTO() }
        }
    }

    suspend fun updateBoardById(boardId: UUID, board: BoardForUpdateDTO) {
        return dbQuery {
            val newStatusId = board.status?.let { selectBoardStatusId(it) }

            BoardsTable.update({ BoardsTable.id eq boardId }) {
                board.name?.let { newName -> it[this.name] = newName }
                newStatusId?.let { newStatusId -> it[this.statusId] = newStatusId }
            }
        }
    }

    suspend fun insertBoardToCom(boardId: UUID, comId: UUID): UUID {
        return dbQuery {
            BoardsToComsTable.insertAndGetId {
                it[this.boardId] = boardId
                it[this.comId] = comId
            }.value
        }
    }

    suspend fun deleteBoardToCom(boardId: UUID, comId: UUID) {
        return dbQuery {
            BoardsToComsTable.deleteWhere { (this.comId eq comId) and (this.boardId eq boardId) }
        }
    }

    suspend fun selectBoardToComById(boardToComId: UUID): BoardToComTableRowDTO? {
        return dbQuery {
            BoardsToComsTable.select { BoardsToComsTable.id eq boardToComId }.singleOrNull()?.toBoardToComTableRowDTO()
        }
    }

    suspend fun selectAllComByBoard(boardId: UUID): List<BoardToComTableRowDTO> {
        return dbQuery {
            BoardsToComsTable.select { BoardsToComsTable.boardId eq boardId }.map { it.toBoardToComTableRowDTO() }
        }
    }

    private fun ResultRow.toBoardTableRowDTO(): BoardTableRowDTO {
        return BoardTableRowDTO(
            id = this[BoardsTable.id].value,
            name = this[BoardsTable.name],
            ownerId = this[BoardsTable.ownerId].value,
            status = this[BoardsTable.statusId].value
        )
    }

    private fun ResultRow.toBoardToComTableRowDTO(): BoardToComTableRowDTO {
        return BoardToComTableRowDTO(
            id = this[BoardsToComsTable.id].value,
            comId = this[BoardsToComsTable.comId].value,
            boardId = this[BoardsToComsTable.boardId].value
        )
    }

}