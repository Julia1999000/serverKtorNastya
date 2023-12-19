package com.nastyaApp.models

import com.nastyaApp.services.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID


//----------- Request -----------

@Serializable
data class RegistrationRequest(
    val name: String,
    val login: String,
    val password: String
)

@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)

@Serializable
data class UpdateUserInfoRequest(
    val name: String? = null,
    @Serializable(with = UUIDSerializer::class) val avatarId: UUID? = null
)

@Serializable
data class UpdateSecretInfoRequest(
    val login: String,
    val password: String
)

//----------- Response -----------

@Serializable
data class IdentityUserResponse(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    val avatarUrl: String? = null,
    @Serializable(with = UUIDSerializer::class) val token: UUID
)

@Serializable
data class ShortUserResponse(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    val avatarUrl: String? = null
)

@Serializable
data class FullUserResponse(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    val avatarUrl: String? = null,
    val coms: List<Com>,
    val boards: List<Board>
)

//----------- DTO -----------

@Serializable
data class UserTableRowDTO (
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    val login: String,
    val password: String,
    @Serializable(with = UUIDSerializer::class) val avatarId: UUID? = null
)

@Serializable
data class AnonymDTO (
    val name: String,
    val login: String,
    val password: String
)

@Serializable
data class NewUserDTO (
    val name: String? = null,
    val login: String? = null,
    val password: String? = null,
    @Serializable(with = UUIDSerializer::class) val avatarId: UUID? = null
)