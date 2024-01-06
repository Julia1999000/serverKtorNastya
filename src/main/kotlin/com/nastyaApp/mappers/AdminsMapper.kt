package com.nastyaApp.mappers

import com.nastyaApp.models.*
import java.util.UUID

fun AdminTableRowDTO.toIdentityAdminResponse(token: UUID, avatarUrl: String?): IdentityAdminResponse {
    return IdentityAdminResponse(
        id = this.id,
        name = this.name,
        avatarUrl = avatarUrl,
        token = token
    )
}

fun AdminTableRowDTO.toAdminInfoResponse(avatarUrl: String?): AdminInfoResponse {
    return AdminInfoResponse(
        id = this.id,
        name = this.name,
        avatarUrl = avatarUrl
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