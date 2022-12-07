package com.doublefree.caff

import com.doublefree.api.model.ParticipantDto
import javax.persistence.*

@Entity
@Table(name = "participant")
data class Participant(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doublefree_id_sequence")
    val id: Long? = null,

    @Column(nullable = false)
    val creator: String?,

    @Column(nullable = false)
    val participantId: Long,

    @Column(nullable = false)
    val caffId: Long,
) {
    fun toDto() = ParticipantDto(
        id,
        creator,
    )
}
