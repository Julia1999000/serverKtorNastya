package com.nastyaApp.models

import com.nastyaApp.services.serializers.UUIDSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import java.util.*

//----------- Request -----------
@Serializable
data class ComRequest(
    val description: String,
    @Serializable(with = UUIDSerializer::class) val imageId: UUID? = null,
)

//----------- Response -----------
@Serializable
data class ShortComResponse(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val description: String,
    val imageUrl: String? = null,
    val createDate: Long,
    @Serializable(with = UUIDSerializer::class) val authorId: UUID,
    val countLikers: Int,
    val countComments: Int,
    val status: String
)

@Serializable
data class FullComResponse(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val description: String,
    val imageUrl: String? = null,
    val createDate: Long,
    @Serializable(with = UUIDSerializer::class) val authorId: UUID,
    val likers: List<ShortUserResponse>,
    val comments: List<CommentResponse>,
    val status: String
)

//----------- DTO -----------
@Serializable
data class ComTableRowDTO(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val description: String,
    @Serializable(with = UUIDSerializer::class) val imageId: UUID? = null,
    val createDate: Instant,
    @Serializable(with = UUIDSerializer::class) val authorId: UUID,
    val status: String
)

@Serializable
data class NewComDTO(
    val description: String,
    @Serializable(with = UUIDSerializer::class) val imageId: UUID? = null,
    val createDate: Instant,
    @Serializable(with = UUIDSerializer::class) val authorId: UUID
)

