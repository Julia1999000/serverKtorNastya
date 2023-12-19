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

        get("/getInfo/{id}") {
            AdminsService.getAdminById(call)
        }

        put("/updateInfo/{id}") {
            AdminsService.updateAminInfoById(call)
        }

        put("/updateSecretInfo/{id}") {
            AdminsService.updateAdminSecretInfoById(call)
        }

        delete("/del/{id}") {
            AdminsService.delAdminById(call)
        }

        get("/getAll") {
            AdminsService.getAllAdmins(call)
        }

        delete("/unlogin/{id}") {
            AdminsService.unloginAdmin(call)
        }

        delete("/delAvatar/{id}") {
            AdminsService.delAvatar(call)
        }

    }
}