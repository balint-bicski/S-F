package com.sandf.event

import com.sandf.api.model.*
import com.sandf.user.UserService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val commentRepository: CommentRepository,
    private val participantRepository: ParticipantRepository,
    private val userService: UserService,
) {

    fun getComments(eventId: Long): List<CommentDto> {
        return commentRepository.findByEventIdOrderByCreatedDate(eventId).map { it.toDto() }
    }

    fun getParticipants(eventId: Long): List<ParticipantDto> {
        return participantRepository.findByEventId(eventId).map { it.toDto() }
    }

    fun deleteComment(commentId: Long) {
        commentRepository.deleteById(commentId)
    }

    fun deleteParticipant(id: Long) {
        participantRepository.deleteById(id)
    }

    fun deleteParticipantbyUserId(eventId: Long, userId: Long) {
        val participantToDelete = participantRepository.findByEventId(eventId).find { p -> p.userId == userId }
        if (participantToDelete != null) {
            participantRepository.deleteById(participantToDelete.id!!)
        }
    }


    fun createComment(eventId: Long, body: String): IdResponseDto {
        if (body.isEmpty()) throw IllegalArgumentException("Comment cannot be empty")
        if (!eventRepository.existsById(eventId)) throw IllegalArgumentException("No such event exists")
        return IdResponseDto(
            commentRepository.save(
                Comment(
                    eventId = eventId, creator = userService.currentUser().email, content = body
                )
            ).id
        )
    }

    fun createParticipant(eventId: Long, userId: Long): IdResponseDto {
        if (!eventRepository.existsById(eventId)) throw IllegalArgumentException("No such event exists")
        return IdResponseDto(
            participantRepository.save(
                Participant(
                    eventId = eventId, creator = userService.currentUser().email, userId = userId
                )
            ).id
        )
    }

    @Transactional
    @OptIn(ExperimentalCoroutinesApi::class)
    fun deleteEvent(id: Long) {
        commentRepository.deleteAllByEventId(id)
        participantRepository.deleteAllByEventId(id)
        eventRepository.deleteById(id)
    }

    fun getEventDetails(id: Long): EventDto? {
        return eventRepository.findByIdOrNull(id)?.toDto()
    }

    fun searchByTitle(title: String?): List<EventSummaryDto> {
        return eventRepository.findByTitleContainingIgnoreCase(title).map { it.toSummary() }
    }

    fun updateTitle(id: Long, title: String, desc: String, time: String, wp: String) {
        val found = eventRepository.findById(id).orElseThrow { NoSuchElementException("No event found with id: $id") }
        found.title = title
        found.desc = desc
        found.time = time
        found.wp = wp
        eventRepository.save(found)
    }

    @Transactional
    fun create(title: String, desc: String, time: String, wp: String): IdResponseDto {
        val event = Event(
            null,
            userService.currentUser().email,
            title,
            desc,
            time,
            wp
        )

        val id = eventRepository.save(event).id
        return IdResponseDto(id)
    }
}
