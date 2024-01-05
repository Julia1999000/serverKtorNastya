package com.nastyaApp

import com.nastyaApp.controllers.UserTokensController
import com.nastyaApp.controllers.UsersController
import com.nastyaApp.models.IdentityUserResponse
import com.nastyaApp.models.LoginRequest
import com.nastyaApp.models.RegistrationRequest
import com.nastyaApp.plugins.configureDatabases
import com.nastyaApp.plugins.configureRouting
import com.nastyaApp.plugins.configureSerialization
import com.nastyaApp.utils.randomString
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.testing.*
import java.text.DateFormat
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class UsersTest {

    val userRegRequest1 = RegistrationRequest(
        name = randomString(10),
        login = randomString(10),
        password = randomString(10)
    )
    val userRegRequest2 = RegistrationRequest(
        name = randomString(10),
        login = randomString(10),
        password = randomString(10)
    )

    @Test
    fun userControlTest() = testApplication {
        application {
            configureDatabases()
            configureRouting()
            configureSerialization()
        }
        val client = createClient {
            install(ContentNegotiation) {
                gson {
                    setDateFormat(DateFormat.LONG)
                    setPrettyPrinting()
                }
            }
        }

        val checkUser1 = UsersController.selectUserByLogin(userRegRequest1.login)
        assertNull(checkUser1)

        val checkUser2 = UsersController.selectUserByLogin(userRegRequest1.login)
        assertNull(checkUser2)

        // ------------ /users/registration

        val userRegDTO1 = client.post("/users/registration") {
            contentType(ContentType.Application.Json)
            setBody(userRegRequest1)
        }.let { response ->

            val responseBody = response.body<IdentityUserResponse>()
            val userDTO = UsersController.selectUserById(responseBody.id)
            val userToken = UserTokensController.selectUserToken(responseBody.token)

            assertEquals(HttpStatusCode.Created, response.status)
            assertNotNull(responseBody)
            assertNotNull(userDTO)
            assertNotNull(userToken)
            assertEquals(userRegRequest1.name, responseBody.name)
            assertEquals(userRegRequest1.name, userDTO.name)
            assertEquals(userRegRequest1.login, userDTO.login)
            assertEquals(userRegRequest1.password, userDTO.password)
            assertEquals(userToken.userId, responseBody.id)
            assertEquals(userToken.token, responseBody.token)

            return@let userDTO
        }

        client.post("/users/registration") {
            contentType(ContentType.Application.Json)
            setBody(userRegRequest1)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
        }

        // ------------ /users/login

        loginInvalidUser(client)
        loginInvalidPassword(client)
        loginInvalidLogin(client)



        val userLoginDTO1 = client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(userRegRequest1.login, userRegRequest1.password))
        }.let { response ->

            val responseBody = response.body<IdentityUserResponse>()
            val userDTO = UsersController.selectUserById(responseBody.id)
            val userToken = UserTokensController.selectUserToken(responseBody.token)

            assertEquals(HttpStatusCode.OK, response.status)
            assertNotNull(userDTO)
            assertNotNull(userToken)
            assertEquals(userRegRequest1.name, userDTO.name)
            assertEquals(userToken.userId, userDTO.id)
            assertEquals(userRegRequest1.login, userDTO.login)
            assertEquals(userRegRequest1.password, userDTO.password)

            return@let userDTO
        }












        UsersController.deleteUserById(userRegDTO1.id)

    }

    private suspend fun loginInvalidUser(client: HttpClient) {
        client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(randomString(10), randomString(10)))
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    private suspend fun loginInvalidLogin(client: HttpClient) {
        client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(randomString(10), userRegRequest1.password))
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    private suspend fun loginInvalidPassword(client: HttpClient) {
        client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(userRegRequest1.login, randomString(10)))
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
        }
    }

}