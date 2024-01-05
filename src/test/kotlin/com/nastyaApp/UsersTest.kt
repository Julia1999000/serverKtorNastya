package com.nastyaApp

import com.nastyaApp.controllers.UserTokensController
import com.nastyaApp.controllers.UsersController
import com.nastyaApp.models.IdentityUserResponse
import com.nastyaApp.models.LoginRequest
import com.nastyaApp.models.RegistrationRequest
import com.nastyaApp.models.UserTableRowDTO
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

        /*
        val checkUser1 = UsersController.selectUserByLogin(userRegRequest1.login)
        assertNull(checkUser1)

        val checkUser2 = UsersController.selectUserByLogin(userRegRequest1.login)
        assertNull(checkUser2)
        */


        // ------------ /users/registration

        val userRegDTO1 = successfulRegistration(client, userRegRequest1)
        registrationExistingUser(client, userRegRequest1)

        // ------------ /users/login

        loginInvalidUser(client)
        loginInvalidPassword(client, LoginRequest(userRegRequest1.login, randomString(10)))
        loginInvalidLogin(client, LoginRequest(randomString(10), userRegRequest1.password))
        val userLoginDTO1 = successfulLogin(client, LoginRequest(userRegRequest1.login, userRegRequest1.password))

        // ------------ /users/updateInfo

        // ------------ /users/getInfo/{user_id}
        // ------------ /users/updateSecretInfo
        // ------------ /users/getAll
        // ------------ /users/unlogin
        // ------------ /users/delAvatar
        // ------------ /users/delete

        UsersController.deleteUserById(userRegDTO1.id)
    }

    private suspend fun successfulRegistration(client: HttpClient, userRegRequest: RegistrationRequest): UserTableRowDTO {
        return client.post("/users/registration") {
            contentType(ContentType.Application.Json)
            setBody(userRegRequest)
        }.let { response ->

            val responseBody = response.body<IdentityUserResponse>()
            val userDTO = UsersController.selectUserById(responseBody.id)
            val userToken = UserTokensController.selectUserToken(responseBody.token)

            assertEquals(HttpStatusCode.Created, response.status)
            assertNotNull(responseBody)
            assertNotNull(userDTO)
            assertNotNull(userToken)
            assertEquals(userRegRequest.name, responseBody.name)
            assertEquals(userRegRequest.name, userDTO.name)
            assertEquals(userRegRequest.login, userDTO.login)
            assertEquals(userRegRequest.password, userDTO.password)
            assertEquals(userToken.userId, responseBody.id)
            assertEquals(userToken.token, responseBody.token)

            return@let userDTO
        }
    }

    private suspend fun registrationExistingUser(client: HttpClient, userRegRequest: RegistrationRequest) {
        client.post("/users/registration") {
            contentType(ContentType.Application.Json)
            setBody(userRegRequest)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
        }
    }

    private suspend fun loginInvalidUser(client: HttpClient) {
        client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(randomString(10), randomString(10)))
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    private suspend fun loginInvalidLogin(client: HttpClient, userLoginRequest: LoginRequest) {
        client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(userLoginRequest)
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    private suspend fun loginInvalidPassword(client: HttpClient, userLoginRequest: LoginRequest) {
        client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(userLoginRequest)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
        }
    }

    private suspend fun successfulLogin(client: HttpClient, userLoginRequest: LoginRequest): UserTableRowDTO  {
        return client.post("/users/login") {
            contentType(ContentType.Application.Json)
            setBody(userLoginRequest)
        }.let { response ->

            val responseBody = response.body<IdentityUserResponse>()
            val userDTO = UsersController.selectUserById(responseBody.id)
            val userToken = UserTokensController.selectUserToken(responseBody.token)

            assertEquals(HttpStatusCode.OK, response.status)
            assertNotNull(userDTO)
            assertNotNull(userToken)
            assertEquals(userToken.userId, userDTO.id)
            assertEquals(userLoginRequest.login, userDTO.login)
            assertEquals(userLoginRequest.password, userDTO.password)

            return@let userDTO
        }
    }

}