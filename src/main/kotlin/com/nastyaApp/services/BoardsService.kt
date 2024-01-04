package com.nastyaApp.services

import com.nastyaApp.controllers.*
import com.nastyaApp.mappers.*
import com.nastyaApp.models.ComToBoardRequest
import com.nastyaApp.models.CreateBoardRequest
import com.nastyaApp.models.UpdateBoardRequest
import com.nastyaApp.utils.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

object BoardsService {

    suspend fun createBoard(call: ApplicationCall) {
        apiCatch(call) {
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token) {
                val request = call.receive<CreateBoardRequest>()

                val userId = UserTokensController.selectUserToken(token!!)?.userId
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "User not found")

                BoardController.selectBoardStatusId(request.status.uppercase())
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "Board status not found")

                val newBoardDTO = request.toNewBoardDTO(userId)
                val boardId = BoardController.insertBoard(newBoardDTO)
                val boardDTO = boardId?.let { BoardController.selectBoardById(it) }

                val response = boardDTO?.toBoardResponse()
                if (response != null) {
                    call.respond(HttpStatusCode.Created, response)
                }
            }
        }
    }

    suspend fun deleteBoard(call: ApplicationCall) {
        apiCatch(call) {
            val token = getUserTokenFromHeaders(call)
            val boardId = getBoardIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Board id not found")

            authHeaderHandle(call, token) {
                val userId = UserTokensController.selectUserToken(token!!)?.userId
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "User not found")

                val boardDTO = BoardController.selectBoardById(boardId)
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "Board not found")

                if (boardDTO.ownerId != userId) {
                    return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "No rights")
                }

                BoardController.deleteBoardById(boardId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun updateBoard(call: ApplicationCall) {
        apiCatch(call) {
            val token = getUserTokenFromHeaders(call)
            val boardId = getBoardIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Board id not found")

            authHeaderHandle(call, token) {
                val userId = UserTokensController.selectUserToken(token!!)?.userId
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "User not found")

                val boardDTO = BoardController.selectBoardById(boardId)
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "Board not found")

                if (boardDTO.ownerId != userId) {
                    return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "No rights")
                }

                val request = call.receive<UpdateBoardRequest>()
                val boardForUpdateDTO = request.toBoardForUpdateDTO()

                request.status?.let {
                    BoardController.selectBoardStatusId(it.uppercase())
                        ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "Board status not found")
                }

                BoardController.updateBoardById(boardId, boardForUpdateDTO)
                BoardController.selectBoardById(boardId)?.toBoardResponse()?.let {
                    call.respond(HttpStatusCode.OK, it)
                }
            }
        }
    }

    suspend fun addComToBoard(call: ApplicationCall) {
        apiCatch(call) {
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token) {
                val request = call.receive<ComToBoardRequest>()

                val userId = UserTokensController.selectUserToken(token!!)?.userId
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "User not found")

                val boardDTO = BoardController.selectBoardById(request.boardId)
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "Board not found")

                if (boardDTO.ownerId != userId) {
                    return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "No rights")
                }

                val comToBoardId = BoardController.insertBoardToCom(request.boardId, request.comId)
                BoardController.selectBoardToComById(comToBoardId)?.toBoardToComResponse()?.let {
                    call.respond(HttpStatusCode.OK, it)
                }
            }
        }
    }

    suspend fun deleteComFromBoard(call: ApplicationCall) {
        apiCatch(call) {
            val token = getUserTokenFromHeaders(call)
            val boardId = getBoardIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Board id not found")
            val comId = getComIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Com id not found")

            authHeaderHandle(call, token) {
                val userId = UserTokensController.selectUserToken(token!!)?.userId
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "User not found")

                val boardDTO = BoardController.selectBoardById(boardId)
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "Board not found")

                if (boardDTO.ownerId != userId) {
                    return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "No rights")
                }

                BoardController.deleteBoardToCom(boardId, comId)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    suspend fun getAllComsByBoadr(call: ApplicationCall) {
        apiCatch(call) {
            val boardId = getBoardIdFromRequest(call)
                ?: return@apiCatch call.respond(HttpStatusCode.BadRequest, "Board id not found")

            val response = BoardController.selectAllComByBoard(boardId).mapNotNull {
                val likersCount = LikesController.selectCountAllLikesByComId(it.comId)
                val commentsCount = CommentController.selectCountAllCommentsByComId(it.comId)
                ComsController.selectComById(it.comId)?.toShortComResponse(likersCount, commentsCount)
            }

            call.respond(HttpStatusCode.OK, response)
        }
    }

    suspend fun getAllBoardsBuUser(call: ApplicationCall) {
        apiCatch(call) {
            val token = getUserTokenFromHeaders(call)

            authHeaderHandle(call, token) {
                val userId = UserTokensController.selectUserToken(token!!)?.userId
                    ?: return@authHeaderHandle call.respond(HttpStatusCode.BadRequest, "User not found")

                val response = BoardController.selectAllBoardsByUserId(userId)
                call.respond(HttpStatusCode.OK, response)
            }
        }
    }
}