package com.nastyaApp.routing

import com.nastyaApp.services.AdminsService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.configureAdminsRouting() {
    route("admins") {
        post("/registration") {
            AdminsService.regAdmin(call)
        }

        post("/login") {
            AdminsService.loginAdmin(call)
        }

        get("/getInfo/{admin_id}") {
            AdminsService.getAdminById(call)
        }

        put("/updateInfo") {
            AdminsService.updateAminInfo(call)
        }

        put("/updateSecretInfo") {
            AdminsService.updateAdminSecretInfo(call)
        }

        delete("/delete") {
            AdminsService.delAdmin(call)
        }

        get("/getAll") {
            AdminsService.getAllAdmins(call)
        }

        delete("/unlogin") {
            AdminsService.unloginAdmin(call)
        }

        delete("/delAvatar") {
            AdminsService.delAvatar(call)
        }

    }
}