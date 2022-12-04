package com.doublefree

import com.doublefree.caff.Caff
import com.doublefree.caff.CaffRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.OffsetDateTime

@SpringBootTest
@AutoConfigureMockMvc
class DoublefreeServerApplicationTests {
	@Autowired
	lateinit var mockMvc: MockMvc

	@Autowired
	lateinit var caffRepository: CaffRepository

	@OptIn(ExperimentalCoroutinesApi::class)
	private fun insertSampleCaff(): Long {
		val id = caffRepository.save(Caff(
			id = 0, creator = "Creator", uploader = "Uploader", createdDate = OffsetDateTime.now(),
			ciffCount = 2, size = 2_000_000, title = "CAFF title"
		)).id!!

		return id
	}

	@Test
	fun contextLoads() { }

	//
	//Without login
	//
	@Test
	fun caffListWithoutAuthentication() {
		caffRepository.deleteAll()

		mockMvc
			.perform(get("/api/caff-files"))
			.andExpect(status().isOk)
			.andExpect(content().json("[]"))
	}

	@Test
	fun caffDetailsWithoutAuthentication(){
		val id = insertSampleCaff()

		mockMvc
		.perform(get("/api/caff-files/$id"))
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	}

	@Test
	fun uploadCaffWithoutAuthentication(){
		mockMvc
			.perform(post("/api/caff-files")
					.param("title", "test title")
					.content(ByteArray(20000)))
			.andExpect(status().is4xxClientError)
	}

	@Test
	fun purchaseCaffWithoutAuthentication(){
		val id = insertSampleCaff()

		mockMvc
			.perform(post("/api/caff-files/$id/purchase"))
			.andExpect(status().isUnauthorized)
	}

	@Test
	fun downloadCaffWithoutAuthentication(){
		val id = insertSampleCaff()

		mockMvc
			.perform(get("/api/caff-files/$id/download"))
			.andExpect(status().isUnauthorized)
	}

	@Test
	fun deleteCaffWithoutAuthentication(){
		val id = insertSampleCaff()

		mockMvc
			.perform(delete("/api/caff-files/$id"))
			.andExpect(status().isUnauthorized)
	}

	@Test
	fun changeTitleWithoutAuthentication(){
		val id = insertSampleCaff()

		mockMvc
			.perform(put("/api/caff-files/$id")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
			.andExpect(status().isUnauthorized)
	}

	@Test
	fun loadCommentsWithoutAuthentication() {
		val id = insertSampleCaff()

		mockMvc
			.perform(get("/api/caff-files/$id/comments"))
			.andExpect(status().isOk)
			.andExpect(content().json("[]"))
	}

	@Test
	fun commentWithoutAuthentication(){
		val id = insertSampleCaff()

		mockMvc
			.perform(post("/api/caff-files/$id/comments")
				.contentType(MediaType.APPLICATION_JSON)
				.content(""))
			.andExpect(status().is4xxClientError)
	}

	@Test
	fun deleteCommentWithoutAuthentication(){
		val id = insertSampleCaff()

		mockMvc
			.perform(delete("/api/caff-files/$id/comments/{commentId}", 1))
			.andExpect(status().isUnauthorized)
	}

	//
	//With login as user
	//

	@Test
	@WithMockUser(username = "hellothere", password = "password", roles = ["USER"])
	fun caffListAsRegularUser() {
		caffRepository.deleteAll()

		mockMvc
			.perform(get("/api/caff-files"))
			.andExpect(status().isOk)
			.andExpect(content().json("[]"))
	}

	@Test
	@WithMockUser(username = "hellothere", password = "password", roles = ["USER"])
	fun caffDetailsAsRegularUser(){
		val id = insertSampleCaff()

		mockMvc
			.perform(get("/api/caff-files/$id"))
			.andExpect(status().isOk)
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	}

	@Test
	@WithMockUser(username = "hellothere", password = "password", roles = ["USER"])
	fun deleteCaffAsRegularUser(){
		val id = insertSampleCaff()

		mockMvc
			.perform(delete("/api/caff-files/$id"))
			.andExpect(status().isForbidden)
	}

	@Test
	@WithMockUser(username = "hellothere", password = "password", roles = ["USER"])
	fun changeTitleAsRegularUser(){
		val id = insertSampleCaff()

		mockMvc
			.perform(put("/api/caff-files/$id")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
			.andExpect(status().isForbidden)
	}

	@Test
	@WithMockUser(username = "hellothere", password = "password", roles = ["USER"])
	fun loadCommentsAsRegularUser() {
		val id = insertSampleCaff()

		mockMvc
			.perform(get("/api/caff-files/$id/comments"))
			.andExpect(status().isOk)
			.andExpect(content().json("[]"))
	}

	@Test
	@WithMockUser(username = "hellothere", password = "password", roles = ["USER"])
	fun deleteCommentAsRegularUser(){
		val id = insertSampleCaff()

		mockMvc
			.perform(delete("/api/caff-files/$id/comments/{commentId}", 1))
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
			.andExpect(status().is4xxClientError)
	}
}
