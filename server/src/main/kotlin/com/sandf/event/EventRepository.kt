package com.sandf.event

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : JpaRepository<Event, Long> {
    fun findByTitleContainingIgnoreCase(title: String?): List<Event>
}
