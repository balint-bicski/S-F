package com.sandf.event

import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findByEventIdOrderByCreatedDate(eventId: Long): List<Comment>
    fun deleteAllByEventId(eventId: Long)
}
