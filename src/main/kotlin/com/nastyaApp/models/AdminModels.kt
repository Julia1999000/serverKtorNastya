package com.nastyaApp.models

import com.nastyaApp.services.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

//----------- Request -----------
@Serializable
data class UpdateAdminInfoRequest(
    val name: String? = null,
    @Serializable(with = UUIDSerializer::class) val avatarId: UUID? = null
)

//----------- Response -----------
@Serializable
data class IdentityAdminResponse(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    val avatarUrl: String? = null,
    @Serializable(with = UUIDSerializer::class) val token: UUID
)

@Serializable
data class AdminInfoResponse(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    val avatarUrl: String? = null
)

//----------- DTO -----------

@Serializable
data class AdminTableRowDTO (
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    val login: String,
    val password: String,
    @Serializable(with = UUIDSerializer::class) val avatarId: UUID? = null
)

@Serializable
data class NewAdminDTO (
    val name: String? = null,
    val login: String? = null,
    val password: String? = null,
    @Serializable(with = UUIDSerializer::class) val avatarId: UUID? = null
)