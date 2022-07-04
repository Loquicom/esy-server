package com.loqui.esy.maker

import com.loqui.esy.data.dto.UserDTO
import com.loqui.esy.data.entity.User
import com.loqui.esy.data.request.LoginRequest
import com.loqui.esy.data.request.RegisterRequest
import com.loqui.esy.data.view.LoginView
import com.loqui.esy.data.view.ResponseView
import com.loqui.esy.data.wrapper.ResultWrapper
import java.util.*

fun makeUser(i: Int? = null): User {
    val prefix = getPrefix(i)
    return User(
        UUID.randomUUID(),
        "test-login$prefix",
        "test-password$prefix",
        "test$prefix@email.com",
        "USER"
    )
}

fun makeUserDTO(i: Int? = null): UserDTO {
    val prefix = getPrefix(i)
    return UserDTO(
        UUID.randomUUID(),
        "test-login$prefix",
        "test$prefix@email.com",
        Collections.singletonList("USER"),
    )
}

fun makeRegisterRequest(i: Int? = null): RegisterRequest {
    val prefix = getPrefix(i)
    return RegisterRequest(
        "test-user$prefix",
        "test-password$prefix",
        "test$prefix@email.com"
    )
}

fun makeLoginRequest(i: Int? = null): LoginRequest {
    val prefix = getPrefix(i)
    return LoginRequest(
        "test-login$prefix",
        "test-password$prefix"
    )
}

fun makeLoginView(i: Int? = null): LoginView {
    val prefix = getPrefix(i)
    return LoginView(
        "test-jwt-token$prefix"
    )
}

fun makeWrappedLoginView(i: Int? = null): ResultWrapper<LoginView> {
    return wrapper(makeLoginView(i))
}

fun toLoginView(result: ResponseView<Map<String, Any>>): LoginView {
    if (!result.success) {
        // TODO throw error
    }
    return LoginView(
        result.data["token"] as String
    )
}
