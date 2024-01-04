package com.nastyaApp.routing

import com.nastyaApp.services.ComsService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.configureComsRouting() {
    route("coms") {

        post("/createByUser") {
            ComsService.createComByUser(call)
        }

        get("/getAllByUser") {
            ComsService.getAllUserComs(call)
        }

        get("/getPublishedByUser/{user_id}") {
            ComsService.getPublishedComsByUser(call)
        }

        get("/getInfoById/{com_id}") {
            ComsService.getFullInfoCom(call)
        }

        put("/publishByAdmin/{com_id}") {
            ComsService.publishComByAdmin(call)
        }

        put("/checkByAdmin/{com_id}") {
            ComsService.checkComByAdmin(call)
        }

        delete("/deleteByUser/{com_id}") {
            ComsService.deleteComByUser(call)
        }

        delete("/deleteByAdmin/{com_id}") {
            ComsService.deleteComByAdmin(call)
        }

        get("/getAllPublished") {
            ComsService.getAllPublishedComs(call)
        }

        get("/getAllCreatedByAdmin") {
            ComsService.getAllCreatedComsByAdmin(call)
        }

    }
}
