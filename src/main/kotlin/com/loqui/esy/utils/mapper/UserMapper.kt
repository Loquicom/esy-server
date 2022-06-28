package com.loqui.esy.utils.mapper.user

import com.loqui.esy.repository.entity.User
import com.loqui.esy.service.dto.UserDTO

fun toDTO(entity: User): UserDTO {
    return UserDTO(entity.id, entity.login, entity.email)
}

fun toDTO(entity: List<User>): List<UserDTO> {
    return entity.map { elt -> toDTO(elt) }
}

fun toEntity(dto: UserDTO): User {
    return User(dto.id, dto.login, "from-dto", dto.email)
}

fun toEntity(dto: List<UserDTO>): List<User> {
    return dto.map { elt -> toEntity(elt) }
}
