package com.loqui.esy.entry.controller

import com.loqui.esy.maker.makeUserDTO
import com.loqui.esy.utils.JWTUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = ["classpath:application-test.properties"])
abstract class ControllerTest {

    @Autowired
    protected lateinit var jwtUtil: JWTUtil

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Value("\${esy.api.root-path}")
    protected lateinit var rootPath: String

    fun authHeader(): HttpHeaders {
        val userDTO = makeUserDTO()
        val token = jwtUtil.generate(userDTO)

        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer $token")
        return headers
    }

}
