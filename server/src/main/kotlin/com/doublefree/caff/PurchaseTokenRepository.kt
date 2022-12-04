package com.doublefree.caff

import org.springframework.data.jpa.repository.JpaRepository

interface PurchaseTokenRepository : JpaRepository<PurchaseToken, Long> {
    fun findByCaffIdAndUserId(caffId: Long, userId: Long): List<PurchaseToken>
    fun deleteAllByCaffId(caffId: Long)
}
