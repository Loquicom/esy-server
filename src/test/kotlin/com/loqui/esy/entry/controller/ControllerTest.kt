package com.loqui.esy.entry.controller

import com.loqui.esy.maker.JWT_TOKEN
import com.loqui.esy.maker.makeUserDTO
import com.loqui.esy.utils.JWTUtil
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
    protected lateinit var jwtUtil: JWTUtil

    fun mockAuthenticationFilter() {
        val userDto = makeUserDTO()
        Mockito.`when`(jwtUtil.getUser(JWT_TOKEN)).thenReturn(userDto)
    }

    fun authHeader(): HttpHeaders {
        mockAuthenticationFilter()
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $JWT_TOKEN")
        return headers
    }

}
