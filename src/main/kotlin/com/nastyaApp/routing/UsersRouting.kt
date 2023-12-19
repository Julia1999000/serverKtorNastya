package com.nastyaApp.routing

import com.nastyaApp.services.UsersService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.configureUsersRouting() {
    route("users") {

        post("/registration") {
            UsersService.regUser(call)
        }

        post("/login") {
            UsersService.login(call)
        }

        get("/getInfo/{id}") {
            UsersService.getFullUserById(call)
        }

        put("/updateInfo/{id}") {
            UsersService.updateUserInfoById(call)
        }

        put("/updateSecretInfo/{id}") {
            UsersService.updateUserSecretInfoById(call)
        }

        delete("/del/{id}") {
            UsersService.delUserById(call)
        }

        get("/getAll") {
            UsersService.getAllUsers(call)
        }

        delete("/unlog/{id}") {
            UsersService.unlog(call)
        }

        delete("/delAvatar/{id}") {
            UsersService.delAvatar(call)
        }
    }
}