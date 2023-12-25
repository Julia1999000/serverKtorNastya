package com.nastyaApp.routing

import com.nastyaApp.services.LikesService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.configureLikesRouting() {
    route("likes") {

        post("/set/{user_id}/{com_id}") {
            LikesService.setLike(call)
        }

        delete("/remove/{user_id}/{com_id}") {
            LikesService.deleteLike(call)
        }

    }
}