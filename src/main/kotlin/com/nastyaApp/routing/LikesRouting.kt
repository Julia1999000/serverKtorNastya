package com.nastyaApp.routing

import com.nastyaApp.services.LikesService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.configureLikesRouting() {
    route("likes") {

        post("/set/{com_id}") {
            LikesService.setLike(call)
        }

        delete("/remove/{com_id}") {
            LikesService.deleteLike(call)
        }

        get("/getAllByCom/{com_id}") {
            LikesService.gelAllLikersByCom(call)
        }

    }
}