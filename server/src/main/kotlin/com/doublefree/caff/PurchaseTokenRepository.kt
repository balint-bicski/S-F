package com.doublefree.caff;

import org.springframework.data.jpa.repository.JpaRepository

interface PurchaseTokenRepository : JpaRepository<PurchaseToken, Long> {
    fun findByToken(token: String): PurchaseToken
}