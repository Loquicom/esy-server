package com.loqui.esy.entry.controller

import com.loqui.esy.maker.toJSON
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
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.security.InvalidParameterException

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

    protected abstract val path: String

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

    fun get(url: String = ""): CallMockMVC {
        return CallMockMVC(this, MockMvcRequestBuilders.get(getPath(url)))
    }

    fun post(url: String = ""): CallMockMVC {
        return CallMockMVC(this, MockMvcRequestBuilders.post(getPath(url)))
    }

    fun put(url: String = ""): CallMockMVC {
        return CallMockMVC(this, MockMvcRequestBuilders.put(getPath(url)))
    }

    fun delete(url: String = ""): CallMockMVC {
        return CallMockMVC(this, MockMvcRequestBuilders.delete(getPath(url)))
    }

    private fun getPath(url: String): String {
        return "$rootPath/$path/$url"
    }

    class CallMockMVC(
        private val controller: ControllerTest,
        private val builder: MockHttpServletRequestBuilder,
    ) {

        private lateinit var resultActions: ResultActions

        fun header(name: String, vararg value: Any): CallMockMVC {
            checkIfIsSend()
            builder.header(name, *value)
            return this
        }

        fun headers(headers: HttpHeaders): CallMockMVC {
            checkIfIsSend()
            builder.headers(headers)
            return this
        }

        fun authHeader(): CallMockMVC {
            return headers(controller.authHeader())
        }

        fun content(content: Any): CallMockMVC {
            builder.content(toJSON(content)).contentType(MediaType.APPLICATION_JSON)
            return this
        }

        fun expect(data: Any): CallMockMVC {
            val action = getResultAction()
            action.andExpect(content().json(toJSON(data)))
            return this
        }

        fun expectString(data: String): CallMockMVC {
            val action = getResultAction()
            action.andExpect(content().string(data))
            return this
        }

        fun status(httpStatus: HttpStatus): CallMockMVC {
            val status = ResultHttpStatus.fromHttpStatus(httpStatus)
                ?: throw InvalidParameterException("Unable to converse HttpStatus to ResultHttpStatus")
            return status(status)
        }

        fun status(resultStatus: ResultHttpStatus): CallMockMVC {
            val action = getResultAction()
            action.andExpect(resultStatus.status)
            return this
        }

        fun status(status: ResultMatcher): CallMockMVC {
            val action = getResultAction()
            action.andExpect(status)
            return this
        }

        private fun isSend(): Boolean {
            return this::resultActions.isInitialized
        }

        private fun checkIfIsSend() {
            if (isSend()) {
                throw IllegalStateException("Unable to edit, request already send")
            }
        }

        private fun getResultAction(): ResultActions {
            if (!isSend()) {
                resultActions = controller.mockMvc.perform(builder)
            }
            return resultActions
        }

    }

    enum class ResultHttpStatus(val code: String, val status: ResultMatcher) {
        Informational("1XX", status().is1xxInformational),
        Successful("2XX", status().is2xxSuccessful),
        Redirection("3XX", status().is3xxRedirection),
        ClientError("4XX", status().is4xxClientError),
        ServerError("5XX", status().is5xxServerError),
        OK("200", status().isOk),
        CREATED("201", status().isCreated),
        ACCEPTED("202", status().isAccepted),
        NO_CONTENT("204", status().isNoContent),
        MULTIPLE_CHOICES("300", status().isMultipleChoices),
        MOVED_PERMANENTLY("301", status().isMovedPermanently),
        FOUND("302", status().isFound),
        BAD_REQUEST("400", status().isBadRequest),
        UNAUTHORIZED("401", status().isUnauthorized),
        PAYMENT_REQUIRED("402", status().isPaymentRequired),
        FORBIDDEN("403", status().isForbidden),
        NOT_FOUND("404", status().isNotFound),
        METHOD_NOT_ALLOWED("405", status().isMethodNotAllowed),
        INTERNAL_SERVER_ERROR("500", status().isInternalServerError),
        NOT_IMPLEMENTED("501", status().isNotImplemented),
        BAD_GATEWAY("502", status().isBadGateway),
        SERVICE_UNAVAILABLE("503", status().isServiceUnavailable);

        companion object {
            fun fromHttpStatus(status: HttpStatus): ResultHttpStatus? {
                val code = status.value()
                val list = ResultHttpStatus.values()
                return list.find { elt -> elt.code == "$code" }
            }
        }
    }

}
