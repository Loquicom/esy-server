package com.loqui.esy.maker.impl

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext

class TestContext : SecurityContext {

    override fun getAuthentication(): Authentication {
        return TestAuthentication("string")
    }

    override fun setAuthentication(authentication: Authentication?) {
        TODO("Not yet implemented")
    }
}
