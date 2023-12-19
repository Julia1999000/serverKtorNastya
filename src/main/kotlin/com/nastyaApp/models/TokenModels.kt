package com.nastyaApp.models

import com.nastyaApp.services.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TokenTableRowModel(
    @Serializable(with = UUIDSerializer::class) val token: UUID,
    @Serializable(with = UUIDSerializer::class) val userId: UUID
)