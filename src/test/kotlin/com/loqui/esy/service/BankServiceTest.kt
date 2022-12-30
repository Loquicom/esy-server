package com.loqui.esy.service

import com.loqui.esy.data.wrapper.EsyError
import com.loqui.esy.exception.EsyNotFoundException
import com.loqui.esy.exception.EsyUnauthorizedException
import com.loqui.esy.maker.bank.ID
import com.loqui.esy.maker.bank.makeBank
import com.loqui.esy.maker.bank.makeBankRequest
import com.loqui.esy.maker.toUUID
import com.loqui.esy.maker.user.makeUser
import com.loqui.esy.repository.BankRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import java.util.*


class BankServiceTest : ServiceTest() {

    object BankServiceTestConstant {
        const val USER_NOT_FOUND = "User not found"
        const val BANK_ID_MAIN = "2648635e-68e0-4785-a92e-452f0d2e9556"
    }

    private lateinit var mockedStaticUUID: MockedStatic<UUID>

    @Autowired
    private lateinit var service: BankService

    @MockBean
    private lateinit var bankRepository: BankRepository

    @MockBean
    private lateinit var userService: UserService

    @BeforeAll
    fun beforeAll() {
        mockedStaticUUID = Mockito.mockStatic(UUID::class.java)
        mockedStaticUUID.`when`<UUID>(UUID::randomUUID).thenReturn(toUUID(ID))
    }

    @AfterAll
    fun afterAll() {
        mockedStaticUUID.close()
    }

    @Test
    fun contextLoad() {
        assertThat(service).isNotNull
    }

    @Test
    fun getSuccessTest() {
        val id = toUUID(ID)
        val user = makeUser()
        val bank = makeBank()

        Mockito.`when`(userService.getConnected()).thenReturn(user)
        Mockito.`when`(bankRepository.findById(id)).thenReturn(Optional.of(bank))

        val result = service.get(id)
        assertThat(result).isEqualTo(bank)
    }

    @Test
    fun getUnauthorizedFailTest() {
        val id = toUUID(ID)
        val user = makeUser(ID)
        val bank = makeBank()

        Mockito.`when`(userService.getConnected()).thenReturn(user)
        Mockito.`when`(bankRepository.findById(id)).thenReturn(Optional.of(bank))

        val ex = assertThrows<EsyUnauthorizedException> { service.get(id) }
        assertThat(ex.message).isEqualTo(EsyError.UNAUTHORIZED.message)
        assertThat(ex.status).isEqualTo(EsyError.UNAUTHORIZED.status)
        assertThat(ex.code).isEqualTo(EsyError.UNAUTHORIZED.code)
        assertThat(ex.trace()).isEmpty()
    }

    @Test
    fun getNotFoundFailTest() {
        val id = toUUID(ID)
        val user = makeUser()

        Mockito.`when`(userService.getConnected()).thenReturn(user)
        Mockito.`when`(bankRepository.findById(id)).thenReturn(Optional.empty())

        val ex = assertThrows<EsyNotFoundException> { service.get(id) }
        assertThat(ex.status).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(ex.trace()).isEmpty()
    }

    @Test
    fun addNoMainSuccessTest() {
        val request = makeBankRequest()
        val user = makeUser()
        val expected = makeBank()

        Mockito.`when`(userService.get(request.user)).thenReturn(user)
        Mockito.`when`(bankRepository.findByUser_IdAndMain(expected.user.id)).thenReturn(Collections.emptyList())
        Mockito.`when`(bankRepository.save(expected)).thenReturn(expected)

        val result = service.add(request)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun addNewMainSuccessTest() {
        val request = makeBankRequest()
        val user = makeUser()
        val expected = makeBank()
        val bankMain = makeBank(BankServiceTestConstant.BANK_ID_MAIN, 2, true)
        val bankNoMain = makeBank(BankServiceTestConstant.BANK_ID_MAIN, 2, false)

        Mockito.`when`(userService.get(request.user)).thenReturn(user)
        Mockito.`when`(bankRepository.findByUser_IdAndMain(expected.user.id)).thenReturn(Collections.singletonList(bankMain))
        Mockito.`when`(bankRepository.save(bankNoMain)).thenReturn(bankNoMain)
        Mockito.`when`(bankRepository.save(expected)).thenReturn(expected)

        val result = service.add(request)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun addFailTest() {
        val request = makeBankRequest()

        Mockito.`when`(userService.get(request.user)).thenThrow(EsyNotFoundException(BankServiceTestConstant.USER_NOT_FOUND))

        val ex = assertThrows<EsyNotFoundException> { service.add(request) }
        assertThat(ex.message).isEqualTo(BankServiceTestConstant.USER_NOT_FOUND)
        assertThat(ex.status).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(ex.trace()).isEmpty()
    }

    @Test
    fun updateNoMainSuccessTest() {
        val request = makeBankRequest(1)
        val id = toUUID(ID)
        val bank = makeBank()
        val expected = makeBank(1)

        Mockito.`when`(bankRepository.findById(id)).thenReturn(Optional.of(bank))
        Mockito.`when`(bankRepository.findByUser_IdAndMain(expected.user.id)).thenReturn(Collections.emptyList())
        Mockito.`when`(bankRepository.save(expected)).thenReturn(expected)

        val result = service.update(id, request)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun updateNewMainSuccessTest() {
        val request = makeBankRequest(1)
        val id = toUUID(ID)
        val bank = makeBank()
        val expected = makeBank(1)
        val bankMain = makeBank(BankServiceTestConstant.BANK_ID_MAIN, 2, true)
        val bankNoMain = makeBank(BankServiceTestConstant.BANK_ID_MAIN, 2, false)

        Mockito.`when`(bankRepository.findById(id)).thenReturn(Optional.of(bank))
        Mockito.`when`(bankRepository.findByUser_IdAndMain(expected.user.id)).thenReturn(Collections.singletonList(bankMain))
        Mockito.`when`(bankRepository.save(bankNoMain)).thenReturn(bankNoMain)
        Mockito.`when`(bankRepository.save(expected)).thenReturn(expected)

        val result = service.update(id, request)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun updateFailTest() {
        val request = makeBankRequest()
        val id = toUUID(ID)

        Mockito.`when`(bankRepository.findById(id)).thenReturn(Optional.empty())

        val ex = assertThrows<EsyNotFoundException> { service.update(id, request) }
        assertThat(ex.status).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(ex.trace()).isEmpty()
    }

    @Test
    fun deleteSuccessTest() {
        val id = toUUID(ID)

        service.delete(id)
    }

}
