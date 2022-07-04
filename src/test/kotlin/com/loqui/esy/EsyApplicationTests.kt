package com.loqui.esy

import com.loqui.esy.entry.controller.RootController
import com.loqui.esy.entry.controller.UserController
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.properties"])
class EsyApplicationTests(
    @Autowired private val rootController: RootController,
    @Autowired private val userController: UserController
) {

    @Test
    fun controllerContextLoads() {
        Assertions.assertThat(rootController).isNotNull
        Assertions.assertThat(userController).isNotNull
    }

}
