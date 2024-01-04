package com.nastyaApp.routing

import com.nastyaApp.services.BoardsService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.configureBoardsRouting() {
    route("boards") {

        post("/create") {
            BoardsService.createBoard(call)
        }

        delete("/delete/{board_id}") {
            BoardsService.deleteBoard(call)
        }

        put("/update/{board_id}") {
            BoardsService.updateBoard(call)
        }

        post("/addCom") {
            BoardsService.addComToBoard(call)
        }

        delete("/removeCom/{board_id}/{com_id}") {
            BoardsService.deleteComFromBoard(call)
        }

        get("/getAllComsByBoard/{board_id}") {
            BoardsService.getAllComsByBoadr(call)
        }

        get("/getAllBoardsByUser") {
            BoardsService.getAllBoardsBuUser(call)
        }

    }
}