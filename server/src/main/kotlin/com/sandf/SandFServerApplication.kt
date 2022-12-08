package com.sandf

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SandFServerApplication
fun main(args: Array<String>) {
	runApplication<SandFServerApplication>(*args)
}
