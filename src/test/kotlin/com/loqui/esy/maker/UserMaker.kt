package com.loqui.esy.maker

import com.loqui.esy.data.dto.UserDTO
import com.loqui.esy.data.entity.User
import com.loqui.esy.data.request.LoginRequest
import com.loqui.esy.data.request.RegisterRequest
import com.loqui.esy.data.view.LoginView
import java.util.*

const val ID = "122b04c8-7235-4ec4-b10c-9ee2ad2617be"
const val LOGIN = "test-login"
const val EMAIL = "test@email.com"
const val PASSWORD = "test-password"
const val PASSWORD_ENCODED = "\$2a\$12\$4dEDgh0zJQ62x.QZmZ9.m.rFS5TWFiJAlU5Mm4ZuwXCliHgaFlw02"
const val ROLE = "USER"
const val JWT_TOKEN = "test-jwt-token"

fun makeUser(i: Int? = null): User {
    return makeUser(UUID.randomUUID(), i)
}

fun makeUser(uuid: String, i: Int? = null): User {
    return makeUser(UUID.fromString(uuid), i)
}

fun makeUser(uuid: UUID, i: Int? = null): User {
    val prefix = getPrefix(i)
    return User(
        uuid,
        prefix + LOGIN,
        prefix + PASSWORD,
        prefix + EMAIL,
        ROLE
    )
}

fun makeUserDTO(i: Int? = null): UserDTO {
    return makeUserDTO(UUID.randomUUID(), i)
}

fun makeUserDTO(uuid: String, i: Int? = null): UserDTO {
    return makeUserDTO(UUID.fromString(uuid), i)
}

fun makeUserDTO(uuid: UUID, i: Int? = null): UserDTO {
    val prefix = getPrefix(i)
    return UserDTO(
        uuid,
        prefix + LOGIN,
        prefix + EMAIL,
        Collections.singletonList(ROLE),
    )
}

fun convertUser(user: User): UserDTO {
    return UserDTO(
        user.id,
        user.login,
        user.email,
        user.role.split(",")
    )
}

fun convertUser(user: UserDTO): User {
    return User(
        user.id,
        user.login,
        PASSWORD,
        user.email,
        user.role.joinToString { "," }
    )
}

fun makeRegisterRequest(i: Int? = null): RegisterRequest {
    val prefix = getPrefix(i)
    return RegisterRequest(
        prefix + LOGIN,
        prefix + PASSWORD,
        prefix + EMAIL
    )
}

fun makeLoginRequest(i: Int? = null): LoginRequest {
    val prefix = getPrefix(i)
    return LoginRequest(
        LOGIN + prefix,
        prefix + PASSWORD,
    )
}

fun makeLoginView(i: Int? = null): LoginView {
    val prefix = getPrefix(i)
    return LoginView(
        prefix + JWT_TOKEN
    )
}
