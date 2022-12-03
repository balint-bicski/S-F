package com.doublefree.caff;

import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findByCaffId(caffId: Long): List<Comment>
}