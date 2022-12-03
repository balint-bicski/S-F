package com.doublefree.caff

import com.doublefree.api.model.PurchaseTokenDto
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "purchase_tokens")
class PurchaseToken(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doublefree_id_sequence")
    val id: Long? = null,

    @Column(nullable = false)
    val createdDate: OffsetDateTime = OffsetDateTime.now(),

    @Column(nullable = false)
    val token: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    val caffId: Long,
) {
    fun toDto() = PurchaseTokenDto(token.toString())
}
