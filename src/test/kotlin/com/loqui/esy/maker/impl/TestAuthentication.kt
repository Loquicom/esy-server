package com.loqui.esy.maker.user.impl

import com.loqui.esy.maker.user.ID
import com.loqui.esy.maker.user.LOGIN
import com.loqui.esy.maker.user.makeUser
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class TestAuthentication(
    private val principalType: String = "user"
) : Authentication {

    override fun getName(): String {
        TODO("Not yet implemented")
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        TODO("Not yet implemented")
    }

    override fun getCredentials(): Any {
        TODO("Not yet implemented")
    }

    override fun getDetails(): Any {
        TODO("Not yet implemented")
    }

    override fun getPrincipal(): Any {
        return when (principalType) {
            "string" -> LOGIN
            else -> makeUser(ID)
        }
    }

    override fun isAuthenticated(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        TODO("Not yet implemented")
    }
}
