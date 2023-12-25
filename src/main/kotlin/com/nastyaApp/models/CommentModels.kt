package com.nastyaApp.models

import com.nastyaApp.services.serializers.UUIDSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class CreateCommentRequest(
    @Serializable(with = UUIDSerializer::class) val authorId: UUID,
    @Serializable(with = UUIDSerializer::class) val comId: UUID,
    val text: String,
)

@Serializable
data class CommentResponse(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val authorId: UUID,
    @Serializable(with = UUIDSerializer::class) val comId: UUID,
    val text: String,
    val createdDate: Long,
)

@Serializable
data class NewCommentDTO(
    @Serializable(with = UUIDSerializer::class) val authorId: UUID,
    @Serializable(with = UUIDSerializer::class) val comId: UUID,
    val text: String,
    val createdDate: Instant,
)

@Serializable
data class CommentTableRowDTO(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val authorId: UUID,
    @Serializable(with = UUIDSerializer::class) val comId: UUID,
    val text: String,
    val createdDate: Instant,
)


