package com.nastyaApp.mappers

import com.nastyaApp.models.*
import com.nastyaApp.utils.generateImagUrl
import kotlinx.datetime.Clock
import java.util.UUID

fun ComTableRowDTO.toShortComResponse(countLikers: Int = 0, countComments: Int = 0): ShortComResponse {
    return ShortComResponse(
        id = this.id,
        description = this.description,
        imageUrl = generateImagUrl(this.imageId),
        createDate = this.createDate.epochSeconds,
        authorId = this.authorId,
        countLikers = countLikers,
        countComments = countComments,
        status = this.status
    )
}

fun ComRequest.toNewComDTO(authorId: UUID): NewComDTO {
    return NewComDTO(
        description = this.description,
        imageId = this.imageId,
        createDate = Clock.System.now(),
        authorId = authorId
    )
}

fun ComTableRowDTO.toFullComResponse(likers: List<ShortUserResponse>, comments: List<CommentResponse>): FullComResponse {
    return FullComResponse(
        id = this.id,
        description = this.description,
        imageUrl = generateImagUrl(this.imageId),
        createDate = this.createDate.epochSeconds,
        authorId = this.authorId,
        likers = likers,
        comments = comments,
        status = this.status
    )
}