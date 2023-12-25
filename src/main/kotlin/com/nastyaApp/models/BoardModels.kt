package com.nastyaApp.models

import com.nastyaApp.services.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

//----------- Request -----------
@Serializable
data class CreateBoardRequest(
    val name: String,
    val status: String
)

@Serializable
data class UpdateBoardRequest(
    val name: String? = null,
    val status: String? = null
)

@Serializable
data class ComToBoardRequest(
    @Serializable(with = UUIDSerializer::class) val comId: UUID,
    @Serializable(with = UUIDSerializer::class) val boardId: UUID,
)

//----------- Response -----------
@Serializable
data class BoardResponse(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    @Serializable(with = UUIDSerializer::class) val ownerId: UUID,
    val status: String
)

@Serializable
data class BoardToComResponse(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val comId: UUID,
    @Serializable(with = UUIDSerializer::class) val boardId: UUID,
)

//----------- DTO -----------
@Serializable
data class NewBoardDTO(
    val name: String,
    @Serializable(with = UUIDSerializer::class) val ownerId: UUID,
    val status: String
)

@Serializable
data class BoardForUpdateDTO(
    val name: String? = null,
    val status: String? = null
)

@Serializable
data class BoardTableRowDTO(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    @Serializable(with = UUIDSerializer::class) val ownerId: UUID,
    val status: String
)

@Serializable
data class BoardToComTableRowDTO(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val comId: UUID,
    @Serializable(with = UUIDSerializer::class) val boardId: UUID,
)