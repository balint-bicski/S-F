package com.doublefree.caff

import org.springframework.data.jpa.repository.JpaRepository

interface ParticipantRepository : JpaRepository<Participant, Long> {
    fun findByCaffId(caffId: Long): List<Participant>
    fun deleteAllByCaffId(caffId: Long)
}
