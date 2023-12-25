package com.nastyaApp.mappers

import com.nastyaApp.models.LikeResponse
import com.nastyaApp.models.LikeTableRowDTO

fun LikeTableRowDTO.toLikeResponse(): LikeResponse {
    return LikeResponse(
        id = this.id,
        likerId = this.likerId,
        comId = this.comId
    )
}