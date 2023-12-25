package com.nastyaApp.mappers

import com.nastyaApp.models.*
import java.util.*

fun CreateBoardRequest.toNewBoardDTO(ownerId: UUID): NewBoardDTO {
    return NewBoardDTO(
        name = this.name,
        ownerId = ownerId,
        status = this.status.uppercase()
    )
}

fun BoardTableRowDTO.toBoardResponse(): BoardResponse {
    return BoardResponse(
        id = this.id,
        name = this.name,
        ownerId = this.ownerId,
        status = this.status
    )
}

fun UpdateBoardRequest.toBoardForUpdateDTO(): BoardForUpdateDTO {
    return BoardForUpdateDTO(
        name = this.name,
        status = this.status
    )
}

fun BoardToComTableRowDTO.toBoardToComResponse(): BoardToComResponse {
    return BoardToComResponse(
        id = this.id,
        comId = this.comId,
        boardId = this.boardId
    )
}