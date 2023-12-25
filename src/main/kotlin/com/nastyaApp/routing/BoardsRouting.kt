package com.nastyaApp.routing

import com.nastyaApp.services.BoardsService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.configureBoardsRouting() {
    route("boards") {

        post("/create/{user_id}") {
            BoardsService.createBoard(call)
        }

        delete("/delete/{user_id}/{board_id}") {
            BoardsService.deleteBoard(call)
        }

        put("/update/{user_id}/{board_id}") {
            BoardsService.updateBoard(call)
        }

        post("/addCom/{user_id}") {
            BoardsService.addComToBoard(call)
        }

        delete("/removeCom/{user_id}/{board_id}/{com_id}") {
            BoardsService.deleteComFromBoard(call)
        }

    }
}