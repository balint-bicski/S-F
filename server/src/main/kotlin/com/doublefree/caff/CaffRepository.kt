package com.doublefree.caff

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CaffRepository : JpaRepository<Caff, Long> {
    fun findByTitle(title: String): List<Caff>
}