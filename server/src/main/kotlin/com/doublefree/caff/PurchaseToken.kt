package com.doublefree.caff

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "purchase_tokens")
class PurchaseToken(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doublefree_id_sequence")
    val Id: Long? = null,

    @Column(nullable = false)
    val token: String,

    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    val caffId: Long,
) {
}