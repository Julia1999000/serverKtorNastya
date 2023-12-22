package com.nastyaApp.models

import com.nastyaApp.services.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class CommentResponse(
    @Serializable(with = UUIDSerializer::class) val id: UUID
)