package com.nastyaApp.mappers

import com.nastyaApp.models.*
import java.util.UUID

fun RegistrationRequest.toAnonymDTO(): AnonymDTO {
    return AnonymDTO(
        name = this.name,
        login = this.login,
        password = this.password
    )
}

fun UserTableRowDTO.toIdentityUserResponse(token: UUID, avatarUrl: String?): IdentityUserResponse {
    return IdentityUserResponse(
        id = this.id,
        name = this.name,
        avatarUrl = avatarUrl,
        token = token
    )
}

fun UserTableRowDTO.toShortUserResponse(avatarUrl: String?): ShortUserResponse {
    return ShortUserResponse(
        id = this.id,
        name = this.name,
        avatarUrl = avatarUrl
    )
}

fun UserTableRowDTO.toFullUserResponse(avatarUrl: String?, listComs: List<ShortComResponse>, listBoards: List<BoardResponse>): FullUserResponse {
    return FullUserResponse(
        id = this.id,
        name = this.name,
        avatarUrl = avatarUrl,
        publishComs = listComs,
        publicBoards = listBoards
    )
}

fun UpdateUserInfoRequest.toNewUserDTO(): NewUserDTO {
    return NewUserDTO(
        name = this.name,
        avatarId = this.avatarId
    )
}

fun UpdateSecretInfoRequest.toNewUserDTO(): NewUserDTO {
    return NewUserDTO(
        login = this.login,
        password = this.password
    )
}