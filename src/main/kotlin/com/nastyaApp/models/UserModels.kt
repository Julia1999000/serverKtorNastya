package com.nastyaApp.models

import kotlinx.serialization.Serializable

// TODO add avatar img

//----------- Request -----------

@Serializable
data class RegistrationRequest(
    val name: String,
    val login: String,
    val password: String
)

//----------- Response -----------

@Serializable
data class IdentityUserResponse(
    val id: String,
    val name: String,
    val token: String
)

//----------- DTO -----------

@Serializable
data class UserDTO (
    val id: String,
    val name: String,
    val login: String,
    val password: String
)

@Serializable
data class AnonymDTO (
    val name: String,
    val login: String,
    val password: String
)

@Serializable
data class NewUserIfo (
    val id: String,
    val name: String? = null,
    val login: String? = null,
    val password: String? = null
)

//-----------------------------
@Serializable
data class FullUserInfo(
    val id: String,
    val name: String,
    val coms: List<Com>,
    val boards: List<Board>
)

@Serializable
data class ShortUserInfo(
    val id: String,
    val name: String
)

@Serializable
data class SecretUserInfo(
    val login: String,
    val password: String
)
