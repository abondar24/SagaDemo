package org.abondar.experimental.sagademo.orchestator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OrchestratorApplication {}

fun main(args: Array<String>) {
    runApplication<OrchestratorApplication>(*args)
}