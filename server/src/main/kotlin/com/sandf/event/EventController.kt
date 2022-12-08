package com.sandf.event

import com.sandf.api.EventApi
import com.sandf.api.model.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class EventController(
    private val eventService: EventService
) : EventApi {

    @PreAuthorize("hasAuthority(T(com.sandf.api.model.Authority).CREATE_EVENT)")
    override fun createEvent(title: String, desc: String, time: String, wp: String): ResponseEntity<IdResponseDto> {
        return try {
            val response = eventService.create(title, desc, time, wp)
            if (response.id == null) {
                ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build()
            } else {
                ResponseEntity.status(HttpStatus.CREATED).body(response)
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    @PreAuthorize("hasAuthority(T(com.sandf.api.model.Authority).WRITE_NOTE)")
    override fun createComment(id: Long, body: String): ResponseEntity<IdResponseDto> {
        return try {
            val response = eventService.createComment(id, body)
            ResponseEntity.status(HttpStatus.CREATED).body(response)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @PreAuthorize("hasAuthority(T(com.sandf.api.model.Authority).WRITE_NOTE)")
    override fun addParticipant(
        @PathVariable(value = "eventId") eventId: Long,
        @PathVariable(value = "userId") userId: Long
    ): ResponseEntity<IdResponseDto> {
        return try {
            val response = eventService.createParticipant(eventId, userId)
            ResponseEntity.status(HttpStatus.CREATED).body(response)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @PreAuthorize("hasAuthority(T(com.sandf.api.model.Authority).DELETE_EVENT)")
    override fun deleteEvent(id: Long): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(eventService.deleteEvent(id))
    }

    @PreAuthorize("hasAuthority(T(com.sandf.api.model.Authority).DELETE_NOTE)")
    override fun deleteComment(id: Long, commentId: Long): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(eventService.deleteComment(commentId))
    }

    @PreAuthorize("hasAuthority(T(com.sandf.api.model.Authority).DELETE_NOTE)")
    override fun deleteParticipant(id: Long): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(eventService.deleteParticipant(id))
    }

    override fun deleteParticipantbyUserId(id: Long, userId: Long): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(eventService.deleteParticipantbyUserId(id, userId))
    }

    override fun getEvent(id: Long): ResponseEntity<EventDto> {
        val event = eventService.getEventDetails(id) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        return ResponseEntity.ok(event)
    }

    override fun getComments(id: Long): ResponseEntity<List<CommentDto>> {
        return ResponseEntity.ok(eventService.getComments(id))
    }

    override fun getParticipants(id: Long): ResponseEntity<List<ParticipantDto>> {
        return ResponseEntity.ok(eventService.getParticipants(id))
    }

    override fun searchEvent(title: String?): ResponseEntity<List<EventSummaryDto>> {
        return ResponseEntity.ok(eventService.searchByTitle(title))
    }

    @PreAuthorize("hasAuthority(T(com.sandf.api.model.Authority).MODIFY_EVENT)")
    override fun updateEvent(id: Long, title: String, desc: String, time: String, wp: String): ResponseEntity<Unit> {
        return try {
            val response = eventService.updateTitle(id, title, desc, time, wp)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }
}
