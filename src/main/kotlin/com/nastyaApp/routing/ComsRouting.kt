package com.nastyaApp.routing

import com.nastyaApp.services.ComsService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.configureComsRouting() {
    route("coms") {

        post("/createByUser/{user_id}") {
            ComsService.createComByUser(call)
        }

        get("/getAllByUser/{user_id}") {
            ComsService.getAllComs(call)
        }

        get("/getPublishedByUser/{user_id}") {
            ComsService.getPublishedComs(call)
        }

        get("/getInfoById/{com_id}") {
            ComsService.getFullInfoCom(call)
        }

        put("/publishByAdmin/{admin_id}/{com_id}") {
            ComsService.publishComByAdmin(call)
        }

        put("/checkByAdmin/{admin_id}/{com_id}") {
            ComsService.checkComByAdmin(call)
        }

        delete("/deleteByUser/{user_id}/{com_id}") {
            ComsService.deleteComByUser(call)
        }

        delete("/deleteByAdmin/{admin_id}/{com_id}") {
            ComsService.deleteComByAdmin(call)
        }

        get("/getAllPublished") {
            ComsService.getAllPublishedComs(call)
        }

        get("/getAllCreated/{admin_id}") {
            ComsService.getAllCreatedComs(call)
        }
    }
}
