package com.doublefree.caff

import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findByCaffIdOrderByCreatedDate(caffId: Long): List<Comment>
    fun deleteAllByCaffId(caffId: Long)
}
