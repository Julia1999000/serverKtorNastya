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
            UsersService.loginUser(call)
        }

        get("/getInfo/{user_id}") {
            UsersService.getFullUserById(call)
        }

        put("/updateInfo") {
            UsersService.updateUserInfo(call)
        }

        put("/updateSecretInfo") {
            UsersService.updateUserSecretInfo(call)
        }

        delete("/delete") {
            UsersService.delUser(call)
        }

        get("/getAll") {
            UsersService.getAllUsers(call)
        }

        delete("/unlogin") {
            UsersService.unloginUser(call)
        }

        delete("/delAvatar") {
            UsersService.delAvatar(call)
        }
    }
}