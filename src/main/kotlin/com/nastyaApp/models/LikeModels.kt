package com.nastyaApp.models

import com.nastyaApp.services.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class LikeResponse(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val likerId: UUID,
    @Serializable(with = UUIDSerializer::class) val comId: UUID
)

@Serializable
data class LikeTableRowDTO(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val likerId: UUID,
    @Serializable(with = UUIDSerializer::class) val comId: UUID
)