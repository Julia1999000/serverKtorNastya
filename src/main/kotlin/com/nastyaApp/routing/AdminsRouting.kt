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

        put("/updateInfo/{admin_id}") {
            AdminsService.updateAminInfoById(call)
        }

        put("/updateSecretInfo/{admin_id}") {
            AdminsService.updateAdminSecretInfoById(call)
        }

        delete("/del/{admin_id}") {
            AdminsService.delAdminById(call)
        }

        get("/getAll") {
            AdminsService.getAllAdmins(call)
        }

        delete("/unlogin/{admin_id}") {
            AdminsService.unloginAdmin(call)
        }

        delete("/delAvatar/{admin_id}") {
            AdminsService.delAvatar(call)
        }

    }
}