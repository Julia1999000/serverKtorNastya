package com.nastyaApp.mappers

import com.nastyaApp.models.*
import com.nastyaApp.utils.generateImagUrl
import java.util.UUID

fun AdminTableRowDTO.toIdentityAdminResponse(token: UUID): IdentityAdminResponse {
    return IdentityAdminResponse(
        id = this.id,
        name = this.name,
        avatarUrl = generateImagUrl(this.avatarId),
        token = token
    )
}

fun AdminTableRowDTO.toAdminInfoResponse(): AdminInfoResponse {
    return AdminInfoResponse(
        id = this.id,
        name = this.name,
        avatarUrl = generateImagUrl(this.avatarId)
    )
}

fun UpdateAdminInfoRequest.toNewAdminDTO(): NewAdminDTO {
    return NewAdminDTO(
        name = this.name,
        avatarId = this.avatarId
    )
}

fun UpdateSecretInfoRequest.toNewAdminDTO(): NewAdminDTO {
    return NewAdminDTO(
        login = this.login,
        password = this.password
    )
}