package com.nastyaApp.mappers

import com.nastyaApp.models.CommentResponse
import com.nastyaApp.models.CommentTableRowDTO
import com.nastyaApp.models.CreateCommentRequest
import com.nastyaApp.models.NewCommentDTO
import kotlinx.datetime.Clock

fun CreateCommentRequest.toNewCommentDTO(): NewCommentDTO {
    return NewCommentDTO(
        authorId = this.authorId,
        comId = this.comId,
        text = this.text,
        createdDate = Clock.System.now()
    )
}

fun CommentTableRowDTO.toCommentResponse(authorAvatarUrl: String?, authorName: String): CommentResponse {
    return CommentResponse(
        id = this.id,
        authorId = this.authorId,
        comId = this.comId,
        text = this.text,
        createdDate = this.createdDate.epochSeconds,
        authorAvatarUrl = authorAvatarUrl,
        authorName = authorName
    )
}