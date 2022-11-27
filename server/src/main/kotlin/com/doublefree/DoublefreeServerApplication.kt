package com.doublefree

import eu.jrie.jetbrains.kotlinshell.shell.shell
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.IOException

@SpringBootApplication
class DoublefreeServerApplication

fun insertCaffIntoDB(parserOutput: String) : Long? {
	//TODO process JSON, insert entity, return Id
	println()
	println(parserOutput)
	println()
	return 1;
}
@OptIn(ExperimentalCoroutinesApi::class)
fun processIncomingCaff(data: ByteArray): Boolean {
	return try {
		shell {
			//create temp file
			file("temp.caff").writeBytes(data)

			//invoke parser
			val parserOutput = StringBuilder()
			pipeline { "parser/caff_parser temp.caff temp.png".process() pipe parserOutput }

			//evaluate result
			val caffId = insertCaffIntoDB(parserOutput.toString())

			//move to
			if (caffId != null) {
				file("temp.caff").renameTo(file("uploads/raw/$caffId.caff"))
				file("temp.png").renameTo(file("uploads/prev/$caffId.caff"))
			}
		}
		true
	} catch (ex: IOException) {
		ex.printStackTrace()
		false
	} finally {
		shell {
			file("temp.caff").delete()
			file("temp.png").delete()
		}
	}
}

fun main(args: Array<String>) {
	//val result = processIncomingCaff(FileUrlResource("sample.caff").inputStream.readAllBytes())
	//println(result)
	runApplication<DoublefreeServerApplication>(*args)
}
