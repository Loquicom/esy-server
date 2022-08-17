package com.loqui.esy.data.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.properties"])
abstract class MapperTest<E, D> {

    protected abstract fun mapEntity(dto: D): E

    protected abstract fun mapEntity(dto: List<D>): List<E>

    protected abstract fun mapDTO(entity: E): D

    protected abstract fun mapDTO(entity: List<E>): List<D>

    protected abstract fun makeEntity(indice: Int = 0): E

    protected fun makeListEntity(nb: Int = 8): List<E> {
        val list = ArrayList<E>()
        for (i in 0..nb) {
            list.add(makeEntity(i))
        }
        return list
    }

    protected abstract fun makeDTO(indice: Int = 0): D

    protected fun makeListDTO(nb: Int = 8): List<D> {
        val list = ArrayList<D>()
        for (i in 0..nb) {
            list.add(makeDTO(i))
        }
        return list
    }

    @Test
    fun toEntityTest() {
        val dto = makeDTO()
        val expected = makeEntity()

        val result = mapEntity(dto)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun toEntityListTest() {
        val dto = makeListDTO()
        val expected = makeListEntity()

        val result = mapEntity(dto)

        assertThat(result.size).isEqualTo(expected.size)
        for (e: E in result) {
            assertThat(expected.contains(e)).isTrue
        }
    }

    @Test
    fun toDTOTest() {
        val entity = makeEntity()
        val expected = makeDTO()

        val result = mapDTO(entity)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun toDTOListTest() {
        val entity = makeListEntity()
        val expected = makeListDTO()

        val result = mapDTO(entity)

        assertThat(result.size).isEqualTo(expected.size)
        for (d: D in result) {
            assertThat(expected.contains(d)).isTrue
        }
    }

}
