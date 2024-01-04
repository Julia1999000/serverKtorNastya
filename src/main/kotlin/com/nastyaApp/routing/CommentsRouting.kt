package com.nastyaApp.routing

import com.nastyaApp.services.CommentService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.configureCommentsRouting() {
    route("comments") {

        post("/create") {
            CommentService.createComment(call)
        }

        delete("/deleteByUser/{comment_id}") {
            CommentService.deleteCommentByUser(call)
        }

        delete("/deleteByAdmin/{comment_id}") {
            CommentService.deleteCommentByAdmin(call)
        }

        get("/getAllByCom/{com_id}") {
            CommentService.getAllCommentsByCom(call)
        }
    }
}