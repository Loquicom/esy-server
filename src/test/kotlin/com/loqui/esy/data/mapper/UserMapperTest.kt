package com.loqui.esy.data.mapper

import com.loqui.esy.data.dto.UserDTO
import com.loqui.esy.data.entity.User
import com.loqui.esy.maker.ID
import com.loqui.esy.maker.makeUser
import com.loqui.esy.maker.makeUserDTO
import com.loqui.esy.utils.mapper.user.toDTO
import com.loqui.esy.utils.mapper.user.toEntity

class UserMapperTest : MapperTest<User, UserDTO>() {

    override fun mapEntity(dto: UserDTO): User {
        return toEntity(dto)
    }

    override fun mapEntity(dto: List<UserDTO>): List<User> {
        return toEntity(dto)
    }

    override fun mapDTO(entity: User): UserDTO {
        return toDTO(entity)
    }

    override fun mapDTO(entity: List<User>): List<UserDTO> {
        return toDTO(entity)
    }

    override fun makeEntity(indice: Int): User {
        return makeUser(ID, indice)
    }

    override fun makeDTO(indice: Int): UserDTO {
        return makeUserDTO(ID, indice)
    }
}
