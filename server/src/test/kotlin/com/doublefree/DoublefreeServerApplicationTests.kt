package com.doublefree

import com.doublefree.authentication.Role
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class DoublefreeServerApplicationTests {
	@Autowired
	lateinit var mockMvc: MockMvc

	@Test
	fun contextLoads() { }

	//
	//Without login
	//
	@Test
	fun caffListWithoutAuthentication() {
		mockMvc
			.perform(get("/api/caff-files"))
			.andExpect(status().isOk)
			.andExpect(content().json("[]"))
	}

	@Test
	fun caffDetailsWithoutAuthentication(){
		//TODO valid caff
		mockMvc
		.perform(get("/api/caff-files/{id}", 1))
			.andExpect(status().isOk)
			.andExpect(content().json("[]"))
	}

	@Test
	fun uploadCaffWithoutAuthentication(){
		mockMvc
			.perform(post("/api/caff-files")
					.param("title", "test title")
					.content(ByteArray(20000)))
			.andExpect(status().isUnauthorized)
	}

	@Test
	fun purchaseCaffWithoutAuthentication(){
		mockMvc
			.perform(post("/api/caff-files/{id}/purchase", 1))
			.andExpect(status().isUnauthorized)
	}

	@Test
	fun downloadCaffWithoutAuthentication(){
		mockMvc
			.perform(get("/api/caff-files/{id}/download", 1))
			.andExpect(status().isUnauthorized)
	}

	@Test
	fun deleteCaffWithoutAuthentication(){
		mockMvc
			.perform(delete("/api/caff-files/{id}", 1))
			.andExpect(status().isUnauthorized)
	}

	@Test
	fun changeTitleWithoutAuthentication(){
		//TODO valid CAFF
		mockMvc
			.perform(put("/api/caff-files/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
			.andExpect(status().isUnauthorized)
	}

	@Test
	fun loadCommentsWithoutAuthentication() {
		//TODO valid CAFF + valid comments
		mockMvc
			.perform(get("/api/caff-files/{id}/comments", 1))
			.andExpect(status().isOk)
			.andExpect(content().json("[]"))
	}

	@Test
	fun commentWithoutAuthentication(){
		mockMvc
			.perform(post("/api/caff-files/{id}/comments", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(""))
			.andExpect(status().isUnauthorized)
	}

	@Test
	fun deleteCommentWithoutAuthentication(){
		mockMvc
			.perform(delete("/api/caff-files/{id}/comments/{commentId}", 1, 1))
			.andExpect(status().isUnauthorized)
	}

	//
	//With login as user
	//

	@Test
	@WithMockUser(username = "hellothere", password = "password", roles = ["USER"])
	fun caffListAsRegularUser() {
		mockMvc
			.perform(get("/api/caff-files"))
			.andExpect(status().isOk)
			.andExpect(content().json("[]"))
	}

	@Test
	@WithMockUser(username = "hellothere", password = "password", roles = ["USER"])
	fun caffDetailsAsRegularUser(){
		//TODO valid caff
		mockMvc
			.perform(get("/api/caff-files/{id}", 1))
			.andExpect(status().isOk)
			.andExpect(content().json("[]"))
	}

	@Test
	@WithMockUser(username = "hellothere", password = "password", roles = ["USER"])
	fun uploadCaffAsRegularUser(){
		//TODO valid caff
		mockMvc
			.perform(post("/api/caff-files")
				.param("title", "test title")
				.content(ByteArray(20000)))
			.andExpect(status().isCreated)
	}

	@Test
	@WithMockUser(username = "hellothere", password = "password", roles = ["USER"])
	fun purchaseCaffAsRegularUser(){
		mockMvc
			.perform(post("/api/caff-files/{id}/purchase", 1))
			.andExpect(status().isCreated)
	}

	@Test
	@WithMockUser(username = "hellothere", password = "password", roles = ["USER"])
	fun downloadCaffAsRegularUser(){
		//TODO valid caff
		mockMvc
			.perform(get("/api/caff-files/{id}/download", 1))
			.andExpect(status().isOk)
	}

	@Test
	@WithMockUser(username = "hellothere", password = "password", roles = ["USER"])
	fun deleteCaffAsRegularUser(){
		mockMvc
			.perform(delete("/api/caff-files/{id}", 1))
			.andExpect(status().isForbidden)
	}

	@Test
	@WithMockUser(username = "hellothere", password = "password", roles = ["USER"])
	fun changeTitleAsRegularUser(){
		//TODO valid CAFF
		mockMvc
			.perform(put("/api/caff-files/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
			.andExpect(status().isForbidden)
	}

	@Test
	@WithMockUser(username = "hellothere", password = "password", roles = ["USER"])
	fun loadCommentsAsRegularUser() {
		//TODO valid CAFF + valid comments
		mockMvc
			.perform(get("/api/caff-files/{id}/comments", 1))
			.andExpect(status().isOk)
			.andExpect(content().json("[]"))
	}

	@Test
	@WithMockUser(username = "hellothere", password = "password", roles = ["USER"])
	fun commentAsRegularUser(){
		mockMvc
			.perform(post("/api/caff-files/{id}/comments", 1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(""))
			.andExpect(status().isCreated)
	}

	@Test
	@WithMockUser(username = "hellothere", password = "password", roles = ["USER"])
	fun deleteCommentAsRegularUser(){
		mockMvc
			.perform(delete("/api/caff-files/{id}/comments/{commentId}", 1, 1))
			.andExpect(status().isForbidden)
	}

	//
	//With login as admin
	//

	//
	//Non-existing entities
	//

	@Test
	fun getNonExistingCaffDetails(){
		mockMvc
			.perform(get("/api/caff-files/{id}", 10))
			.andExpect(status().isNotFound)
	}

	@Test
	fun uploadAsNonExistingUser(){
		throw NotImplementedError()
	}

	@Test
	fun commentAsNonExistingUser(){
		throw NotImplementedError()
	}

	@Test
	@WithMockUser(username = "general_kenobi", password = "password", roles = ["ADMINISTRATOR"])
	fun updateTitleOnNonExistingCaff(){
		throw NotImplementedError()
	}

	//
	//Malformed entities
	//
	@Test
	@WithMockUser(username = "hellothere", password = "password", roles = ["USER"])
	fun uploadInvalidCaff(){
		mockMvc
			.perform(post("/api/caff-files")
				.param("title", "test title")
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.content(ByteArray(20000)))
			.andExpect(status().isUnprocessableEntity)
	}

	@Test
	fun createEmptyComment(){
		throw NotImplementedError()
	}
}
