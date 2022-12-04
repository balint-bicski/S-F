package com.doublefree

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class DoublefreeServerApplicationTests @Autowired constructor(
	val client: WebTestClient
) {

	@Test
	fun contextLoads() {
	}

	//Without login
	@Test
	fun caffListWithoutAuthentication() {
		client
			.get().uri("/api/caff-files")
			.exchange()
			.expectStatus().isOk
			.expectBody().json("[]")
	}

	@Test
	fun uploadCaffWithoutAuthentication(){

	}

	@Test
	fun purchaseCaffWithoutAuthentication(){

	}
	@Test
	fun downloadCaffWithoutAuthentication(){

	}

	@Test
	fun commentWithoutAuthentication(){

	}

	@Test
	fun deleteCommentWithoutAuthentication(){

	}

	//With login as user

	//With login as admin

	//Non-existing entities

	@Test
	fun getNonExistingCaffDetails(){

	}

	@Test
	fun uploadAsNonExistingUser(){

	}

	@Test
	fun commentAsNonExistingUser(){

	}

	//Malformed entities
	@Test
	fun uploadInvalidCaff(){

	}

	@Test
	fun createEmptyComment(){

	}
}
