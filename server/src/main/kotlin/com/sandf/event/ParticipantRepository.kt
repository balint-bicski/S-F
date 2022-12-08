package com.sandf.event

import org.springframework.data.jpa.repository.JpaRepository

interface ParticipantRepository : JpaRepository<Participant, Long> {
    fun findByEventId(eventId: Long): List<Participant>
    fun deleteAllByEventId(eventId: Long)
}
