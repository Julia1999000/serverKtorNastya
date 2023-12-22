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

        put("/updateInfo/{user_id}") {
            UsersService.updateUserInfoById(call)
        }

        put("/updateSecretInfo/{user_id}") {
            UsersService.updateUserSecretInfoById(call)
        }

        delete("/del/{user_id}") {
            UsersService.delUserById(call)
        }

        get("/getAll") {
            UsersService.getAllUsers(call)
        }

        delete("/unlogin/{user_id}") {
            UsersService.unloginUser(call)
        }

        delete("/delAvatar/{user_id}") {
            UsersService.delAvatar(call)
        }
    }
}