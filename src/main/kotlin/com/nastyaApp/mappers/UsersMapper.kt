package com.nastyaApp.mappers

import com.nastyaApp.models.*
import com.nastyaApp.utils.generateImagUrl
import java.util.UUID

fun RegistrationRequest.toAnonymDTO(): AnonymDTO {
    return AnonymDTO(
        name = this.name,
        login = this.login,
        password = this.password
    )
}

fun UserTableRowDTO.toIdentityUserResponse(token: UUID): IdentityUserResponse {
    return IdentityUserResponse(
        id = this.id,
        name = this.name,
        avatarUrl = generateImagUrl(this.avatarId),
        token = token
    )
}

fun UserTableRowDTO.toShortUserResponse(): ShortUserResponse {
    return ShortUserResponse(
        id = this.id,
        name = this.name,
        avatarUrl = generateImagUrl(this.avatarId)
    )
}

fun UserTableRowDTO.toFullUserResponse(listComs: List<ShortComResponse>, listBoards: List<Board>): FullUserResponse {
    return FullUserResponse(
        id = this.id,
        name = this.name,
        avatarUrl = generateImagUrl(this.avatarId),
        coms = listComs,
        boards = listBoards
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