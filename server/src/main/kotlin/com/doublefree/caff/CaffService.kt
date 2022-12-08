package com.doublefree.caff

import com.doublefree.api.model.*
import com.doublefree.user.UserService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class CaffService(
    private val caffRepository: CaffRepository,
    private val commentRepository: CommentRepository,
    private val participantRepository: ParticipantRepository,
    private val purchaseTokenRepository: PurchaseTokenRepository,
    private val userService: UserService,
) {

    fun getComments(caffId: Long): List<CommentDto> {
        return commentRepository.findByCaffIdOrderByCreatedDate(caffId).map { it.toDto() }
    }

    fun getParticipants(caffId: Long): List<ParticipantDto> {
        return participantRepository.findByCaffId(caffId).map { it.toDto() }
    }

    fun deleteComment(commentId: Long) {
        commentRepository.deleteById(commentId)
    }

    fun deleteParticipant(id: Long) {
        participantRepository.deleteById(id)
    }

    fun deleteParticipantbyUserId(caffId: Long, userId: Long) {
        val participantToDelete = participantRepository.findByCaffId(caffId).find { p -> p.userId == userId }
        if (participantToDelete != null) {
            participantRepository.deleteById(participantToDelete.id!!)
        }
    }


    fun createComment(caffId: Long, body: String): IdResponseDto {
        if (body.isEmpty()) throw IllegalArgumentException("Comment cannot be empty")
        if (!caffRepository.existsById(caffId)) throw IllegalArgumentException("No such event exists")
        return IdResponseDto(
            commentRepository.save(
                Comment(
                    caffId = caffId, creator = userService.currentUser().email, content = body
                )
            ).id
        )
    }

    fun createParticipant(caffId: Long, userId: Long): IdResponseDto {
        if (!caffRepository.existsById(caffId)) throw IllegalArgumentException("No such event exists")
        return IdResponseDto(
            participantRepository.save(
                Participant(
                    caffId = caffId, creator = userService.currentUser().email, userId = userId
                )
            ).id
        )
    }

    @Transactional
    @OptIn(ExperimentalCoroutinesApi::class)
    fun deleteCaffFile(id: Long) {
        commentRepository.deleteAllByCaffId(id)
        purchaseTokenRepository.deleteAllByCaffId(id)
        participantRepository.deleteAllByCaffId(id)
        caffRepository.deleteById(id)
    }

    fun purchaseCaffFile(caffId: Long): PurchaseTokenDto {
        val userId = userService.currentUser().id!!
        val token = purchaseTokenRepository.findByCaffIdAndUserId(caffId, userId).firstOrNull()
        if (token != null) {
            return token.toDto()
        }
        val created = purchaseTokenRepository.save(PurchaseToken(caffId = caffId, userId = userId))
        return PurchaseTokenDto(token = created.token.toString())
    }

    fun getCaffDetails(id: Long): CaffDto? {
        return caffRepository.findByIdOrNull(id)?.toDto()
    }

    fun searchByTitle(title: String?): List<CaffSummaryDto> {
        return caffRepository.findByTitleContainingIgnoreCase(title).map { it.toSummary() }
    }

    fun updateTitle(id: Long, title: String, desc: String, time: String, wp: String) {
        val found = caffRepository.findById(id).orElseThrow { NoSuchElementException("No event found with id: $id") }
        found.title = title
        found.desc = desc
        found.time = time
        found.wp = wp
        caffRepository.save(found)
    }

    @Transactional
    fun create(title: String, desc: String, time: String, wp: String): IdResponseDto {
        val caff = Caff(
            null,
            userService.currentUser().email,
            title,
            desc,
            time,
            wp
        )

        val id = caffRepository.save(caff).id
        return IdResponseDto(id)
    }
}
