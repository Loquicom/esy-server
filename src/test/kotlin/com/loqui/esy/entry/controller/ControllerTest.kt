package com.loqui.esy.entry.controller

import com.loqui.esy.maker.user.JWT_TOKEN
import com.loqui.esy.maker.user.makeUserDTO
import com.loqui.esy.utils.JWTUtils
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = ["classpath:application-test.properties"])
abstract class ControllerTest {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Value("\${esy.api.root-path}")
    protected lateinit var rootPath: String

    @MockBean
    protected lateinit var jwtUtils: JWTUtils

    fun mockAuthenticationFilter() {
        val userDto = makeUserDTO()
        Mockito.`when`(jwtUtils.getUser(JWT_TOKEN)).thenReturn(userDto)
    }

    fun authHeader(): HttpHeaders {
        mockAuthenticationFilter()
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $JWT_TOKEN")
        return headers
    }

}
