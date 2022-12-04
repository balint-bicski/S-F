package com.doublefree.caff

import com.doublefree.user.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.*
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.time.OffsetDateTime
import java.util.*
import kotlin.NoSuchElementException

class CaffServiceTests {
    // Mocked dependencies
    private val caffRepository: CaffRepository = mockk()
    private val commentRepository: CommentRepository = mockk()
    private val purchaseTokenRepository: PurchaseTokenRepository = mockk()
    private val userService: UserService = mockk()
    private val objectMapper: ObjectMapper = mockk()

    private val service = CaffService(caffRepository, commentRepository, purchaseTokenRepository, userService, objectMapper)

    private val caff = Caff(
        id = 0, creator = "Creator", uploader = "Uploader", createdDate = OffsetDateTime.now(),
        ciffCount = 2, size = 2_000_000, title = "CAFF title"
    )

    private val comment = Comment(
        id = 0, caffId = 0, creator = "Creator", createdDate = OffsetDateTime.now(), content = "Sample comment"
    )

    @Test
    fun getComments_empty_with_no_result() {
        every { commentRepository.findByCaffIdOrderByCreatedDate(any()) } returns listOf()

        val result = service.getComments(0)

        verify { commentRepository.findByCaffIdOrderByCreatedDate(0) }
        assert(result.isEmpty())
    }

    @Test
    fun getComments_correct_when_result() {
        every { commentRepository.findByCaffIdOrderByCreatedDate(any()) } returns listOf(comment)

        val result = service.getComments(0)

        verify { commentRepository.findByCaffIdOrderByCreatedDate(0) }
        assert(result.size == 1)
        assert(result.first().content == comment.content)
        assert(result.first().creator == comment.creator)
    }

    @Test
    fun getComments_orders_by_createdDate() {
        every { commentRepository.findByCaffIdOrderByCreatedDate(any()) } returns listOf(
            comment, comment.copy(content = "Earlier", createdDate = OffsetDateTime.MIN)
        ).reversed()

        val result = service.getComments(0)

        verify { commentRepository.findByCaffIdOrderByCreatedDate(0) }
        assert(result.size == 2)
        assert(result[0].content == "Earlier")
        assert(result[1].content == comment.content)
    }

    @Test
    fun deleteComment_deletes_the_correct_comment() {
        every { commentRepository.deleteById(any()) } just runs

        service.deleteComment(0)

        verify { commentRepository.deleteById(0) }
    }

    @Test
    fun createComment_fails_with_nonexistent_caff_id() {
        every { caffRepository.existsById(any()) } returns false

        assertThrows<IllegalArgumentException> { service.createComment(320, "content") }
    }

    @Test
    fun createComment_fails_with_empty_body() {
        every { caffRepository.existsById(any()) } returns true

        assertThrows<IllegalArgumentException> { service.createComment(0, "") }
    }

    @Test
    fun createComment_correct_when_arguments_correct() {
        every { caffRepository.existsById(any())} returns true
        every { commentRepository.save(any()) } returns comment

        assertDoesNotThrow { service.createComment(0, "content") }

        verify { commentRepository.save(any()) }
    }

    @Test
    fun searchByTitle_empty_with_no_result() {
        every { caffRepository.findByTitleContainingIgnoreCase(any()) } returns listOf()

        val result = service.searchByTitle("")

        verify { caffRepository.findByTitleContainingIgnoreCase("") }
        assert(result.isEmpty())
    }

    @Test
    fun searchByTitle_correct_when_result() {
        every { caffRepository.findByTitleContainingIgnoreCase(any()) } returns listOf(caff)

        val result = service.searchByTitle("")

        verify { caffRepository.findByTitleContainingIgnoreCase("") }
        assert(result.size == 1)
        assert(result.first().id == caff.id)
        assert(result.first().title == caff.title)
    }

    @Test
    fun getCaffDetails_correct_with_no_result() {
        every { caffRepository.findById(any()) } returns Optional.empty()

        assertThrows<NoSuchElementException> { service.getCaffDetails(0) }

        verify { caffRepository.findById(0) }
    }

    @Test
    fun getCaffDetails_correct_when_result() {
        every { caffRepository.findById(any()) } returns Optional.of(caff)

        val result = assertDoesNotThrow { service.getCaffDetails(0) }

        verify { caffRepository.findById(0) }
        assert(result?.id == caff.id)
        assert(result?.creator == caff.creator)
    }

    @Test
    fun updateTitle_correct_with_no_result() {
        every { caffRepository.findById(any()) } returns Optional.empty()

        assertThrows<NoSuchElementException> { service.updateTitle(0, "Title") }

        verify { caffRepository.findById(0) }
    }
}
