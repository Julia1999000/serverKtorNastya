package com.nastyaApp.mappers

import com.nastyaApp.models.CommentRequest
import com.nastyaApp.models.CommentResponse
import com.nastyaApp.models.CommentTableRowDTO
import com.nastyaApp.models.NewCommentDTO
import kotlinx.datetime.Clock

fun CommentRequest.toNewCommentDTO(): NewCommentDTO {
    return NewCommentDTO(
        authorId = this.authorId,
        comId = this.comId,
        text = this.text,
        createdDate = Clock.System.now()
    )
}

fun CommentTableRowDTO.toCommentResponse(): CommentResponse {
    return CommentResponse(
        id = this.id,
        authorId = this.authorId,
        comId = this.comId,
        text = this.text,
        createdDate = this.createdDate.epochSeconds
    )
}