package com.loqui.esy.utils.mapper.user

import com.loqui.esy.data.entity.User
import com.loqui.esy.data.dto.UserDTO
import com.loqui.esy.utils.PASSWORD_FOR_DTO

fun toDTO(entity: User): UserDTO {
    return UserDTO(entity.id, entity.login, entity.email, entity.role.split(","))
}

fun toDTO(entity: List<User>): List<UserDTO> {
    return entity.map { elt -> toDTO(elt) }
}

fun toEntity(dto: UserDTO): User {
    return User(dto.id, dto.login, PASSWORD_FOR_DTO, dto.email, dto.role.joinToString { "," }, true)
}

fun toEntity(dto: List<UserDTO>): List<User> {
    return dto.map { elt -> toEntity(elt) }
}
