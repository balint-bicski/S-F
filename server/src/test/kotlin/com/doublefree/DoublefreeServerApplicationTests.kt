package com.doublefree

import com.doublefree.authentication.Role
import com.doublefree.caff.*
import com.doublefree.user.User
import com.doublefree.user.UserRepository
import eu.jrie.jetbrains.kotlinshell.shell.shell
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.OffsetDateTime
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class DoublefreeServerApplicationTests {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var commentRepository: CommentRepository

    @Autowired
    lateinit var purchaseTokenRepository: PurchaseTokenRepository

    @Autowired
    lateinit var caffRepository: CaffRepository

    @Autowired
    lateinit var userRepository: UserRepository

    var userId: Long? = null

    var adminId: Long? = null

    @BeforeEach
    fun beforeEach() {
        commentRepository.deleteAll()
        purchaseTokenRepository.deleteAll()
        caffRepository.deleteAll()
        userRepository.deleteAll()

        userId = userRepository.save(User(null, "hellothere", "password", Role.USER)).id
        adminId = userRepository.save(User(null, "general_kenobi", "password", Role.ADMINISTRATOR)).id
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun insertSampleCaff(): Long {
        val id = caffRepository.save(
            Caff(
                id = 0, creator = "Creator", uploader = "Uploader", createdDate = OffsetDateTime.now(),
                ciffCount = 2, size = 2_000_000, title = "CAFF title"
            )
        ).id!!
        shell {
            file("uploads/raw/0.caff").copyTo(file("uploads/raw/$id.caff"), overwrite = true)
            file("uploads/prev/0.bmp").copyTo(file("uploads/prev/$id.bmp"), overwrite = true)
        }
        return id
    }

    @Test
    fun contextLoads() {
    }

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
    fun caffDetailsWithoutAuthentication() {
        val id = insertSampleCaff()

        mockMvc
            .perform(get("/api/caff-files/$id"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun uploadCaffWithoutAuthentication() {
        mockMvc
            .perform(
                post("/api/caff-files")
                    .param("title", "test title")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .content(ByteArray(20000))
            )
            .andExpect(status().is4xxClientError)
    }

    @Test
    fun purchaseCaffWithoutAuthentication() {
        val id = insertSampleCaff()

        mockMvc
            .perform(post("/api/caff-files/$id/purchase"))
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun downloadCaffWithoutAuthentication() {
        val id = insertSampleCaff()

        mockMvc
            .perform(
                get("/api/caff-files/$id/download")
                    .param("token", "not_valid_token")
            )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun deleteCaffWithoutAuthentication() {
        val id = insertSampleCaff()

        mockMvc
            .perform(delete("/api/caff-files/$id"))
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun changeTitleWithoutAuthentication() {
        val id = insertSampleCaff()

        mockMvc
            .perform(
                put("/api/caff-files/$id")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}")
            )
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
    fun commentWithoutAuthentication() {
        val id = insertSampleCaff()

        mockMvc
            .perform(
                post("/api/caff-files/$id/comments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("")
            )
            .andExpect(status().is4xxClientError)
    }

    @Test
    fun deleteCommentWithoutAuthentication() {
        val id = insertSampleCaff()

        mockMvc
            .perform(delete("/api/caff-files/$id/comments/{commentId}", 1))
            .andExpect(status().isUnauthorized)
    }

    //
    //With login as user
    //

    @Test
    @WithMockUser(username = "hellothere", password = "password")
    fun caffListAsRegularUser() {
        caffRepository.deleteAll()

        mockMvc
            .perform(get("/api/caff-files"))
            .andExpect(status().isOk)
            .andExpect(content().json("[]"))
    }

    @Test
    @WithMockUser(username = "hellothere", password = "password")
    fun caffDetailsAsRegularUser() {
        val id = insertSampleCaff()

        mockMvc
            .perform(get("/api/caff-files/$id"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    @WithMockUser(username = "hellothere", password = "password", authorities = ["UPLOAD_CAFF"])
    fun uploadCaffAsRegularUser() {
        mockMvc
            .perform(
                post("/api/caff-files")
                    .param("title", "test title")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .content(ByteArray(20000))
            )
            .andExpect(status().is4xxClientError)
    }

    @Test
    @WithMockUser(username = "hellothere", password = "password", authorities = ["PAYMENT"])
    fun purchaseCaffAsRegularUser() {
        val id = insertSampleCaff()

        mockMvc
            .perform(post("/api/caff-files/$id/purchase"))
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(username = "hellothere", password = "password", authorities = ["DOWNLOAD_CAFF"])
    fun downloadCaffAsRegularUser() {
        val id = insertSampleCaff()
        val token = UUID.randomUUID()
        purchaseTokenRepository.save(PurchaseToken(caffId = id, token = token, userId = userId!!))

        mockMvc
            .perform(
                get("/api/caff-files/$id/download")
                    .param("token", token.toString())
            )
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(username = "hellothere", password = "password")
    fun deleteCaffAsRegularUser() {
        val id = insertSampleCaff()

        mockMvc
            .perform(delete("/api/caff-files/$id"))
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = "hellothere", password = "password")
    fun changeTitleAsRegularUser() {
        val id = insertSampleCaff()

        mockMvc
            .perform(
                put("/api/caff-files/$id")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}")
            )
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = "hellothere", password = "password")
    fun loadCommentsAsRegularUser() {
        val id = insertSampleCaff()

        mockMvc
            .perform(get("/api/caff-files/$id/comments"))
            .andExpect(status().isOk)
            .andExpect(content().json("[]"))
    }

    @Test
    @WithMockUser(username = "hellothere", password = "password", authorities = ["WRITE_NOTE"])
    fun commentAsRegularUser() {
        val id = insertSampleCaff()

        mockMvc
            .perform(
                post("/api/caff-files/$id/comments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"content\": \"text\" }")
            )
            .andExpect(status().isCreated)
    }

    @Test
    @WithMockUser(username = "hellothere", password = "password")
    fun deleteCommentAsRegularUser() {
        val id = insertSampleCaff()

        mockMvc
            .perform(delete("/api/caff-files/$id/comments/{commentId}", 1))
            .andExpect(status().isForbidden)
    }

    //
    //With login as admin
    //

    @Test
    @WithMockUser(username = "general_kenobi", password = "password")
    fun caffListAsAdministrator() {
        caffRepository.deleteAll()

        mockMvc
            .perform(get("/api/caff-files"))
            .andExpect(status().isOk)
            .andExpect(content().json("[]"))
    }

    @Test
    @WithMockUser(username = "general_kenobi", password = "password")
    fun caffDetailsAsAdministrator() {
        val id = insertSampleCaff()

        mockMvc
            .perform(get("/api/caff-files/$id"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    @WithMockUser(username = "general_kenobi", password = "password", authorities = ["UPLOAD_CAFF"])
    fun uploadCaffAsAdministrator() {
        mockMvc
            .perform(
                post("/api/caff-files")
                    .param("title", "test title")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .content(ByteArray(20000))
            )
            .andExpect(status().is4xxClientError)
    }

    @Test
    @WithMockUser(username = "general_kenobi", password = "password", authorities = ["PAYMENT"])
    fun purchaseCaffAsAdministrator() {
        val id = insertSampleCaff()

        mockMvc
            .perform(post("/api/caff-files/$id/purchase"))
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(username = "general_kenobi", password = "password", authorities = ["DOWNLOAD_CAFF"])
    fun downloadCaffAsAdministrator() {
        val id = insertSampleCaff()
        val token = UUID.randomUUID()
        purchaseTokenRepository.save(PurchaseToken(caffId = id, token = token, userId = adminId!!))

        mockMvc
            .perform(
                get("/api/caff-files/$id/download")
                    .param("token", token.toString())
            )
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(username = "general_kenobi", password = "password", authorities = ["MODIFY_CAFF"])
    fun changeTitleAsAdministrator() {
        val id = insertSampleCaff()

        mockMvc
            .perform(
                put("/api/caff-files/$id")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}")
            )
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(username = "general_kenobi", password = "password")
    fun loadCommentsAsAdministrator() {
        val id = insertSampleCaff()

        mockMvc
            .perform(get("/api/caff-files/$id/comments"))
            .andExpect(status().isOk)
            .andExpect(content().json("[]"))
    }

    @Test
    @WithMockUser(username = "general_kenobi", password = "password", authorities = ["WRITE_NOTE"])
    fun commentAsAdministrator() {
        val id = insertSampleCaff()

        mockMvc
            .perform(
                post("/api/caff-files/$id/comments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"content\": \"text\" }")
            )
            .andExpect(status().isCreated)
    }

    @Test
    @WithMockUser(username = "general_kenobi", password = "password", authorities = ["DELETE_NOTE"])
    fun deleteCommentAsAdministrator() {
        val id = insertSampleCaff()
        val commentId = commentRepository.save(Comment(creator = "user", caffId = id, content = "test_comment")).id

        mockMvc
            .perform(delete("/api/caff-files/$id/comments/$commentId"))
            .andExpect(status().isNoContent)
    }

    //
    //Non-existing entities
    //

    @Test
    fun getNonExistingCaffDetails() {
        mockMvc
            .perform(get("/api/caff-files/{id}", 10))
            .andExpect(status().isNotFound)
    }

    //
    //Malformed entities
    //
    @Test
    @WithMockUser(username = "hellothere", password = "password", authorities = ["UPLOAD_CAFF"])
    fun uploadInvalidCaff() {
        mockMvc
            .perform(
                post("/api/caff-files")
                    .param("title", "test title")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .content(ByteArray(20000))
            )
            .andExpect(status().is4xxClientError)
    }
}
