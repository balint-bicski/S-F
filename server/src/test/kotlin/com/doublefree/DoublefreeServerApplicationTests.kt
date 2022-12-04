package com.doublefree

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class DoublefreeServerApplicationTests {
	@Autowired
	lateinit var mockMvc: MockMvc

	@Test
	fun contextLoads() {
	}

	//Without login
	@Test
	fun caffListWithoutAuthentication() {
		mockMvc
			.perform(get("/api/caff-files"))
			.andExpect(status().isOk)
			.andExpect(content().json("[]"))
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
