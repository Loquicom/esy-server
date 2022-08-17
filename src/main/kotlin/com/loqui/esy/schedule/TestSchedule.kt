package com.loqui.esy.schedule

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class TestSchedule {

    private val log = LoggerFactory.getLogger(TestSchedule::class.java)

    @Scheduled(cron = "1 * * * * *")
    fun test() {
        log.error("Coucou je suis une tache plannifiée")
    }

}
