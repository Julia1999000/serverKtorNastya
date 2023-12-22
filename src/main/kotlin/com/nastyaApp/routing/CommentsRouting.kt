package com.nastyaApp.routing

import com.nastyaApp.services.CommentService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.configureCommentsRouting() {
    route("comments") {

        post("/create/{user_id}") {
            CommentService.createComment(call)
        }

        delete("/deleteByUser/{user_id}/{comment_id}") {
            CommentService.deleteCommentByUser(call)
        }

        delete("/deleteByAdmin/{admin_id}/{comment_id}") {
            CommentService.deleteCommentByAdmin(call)
        }

    }
}