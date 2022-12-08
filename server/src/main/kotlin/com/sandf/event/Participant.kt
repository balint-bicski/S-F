package com.sandf.event

import com.sandf.api.model.ParticipantDto
import javax.persistence.*

@Entity
@Table(name = "participant")
data class Participant(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sandf_id_sequence")
    val id: Long? = null,

    @Column(nullable = false)
    val creator: String?,

    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    val eventId: Long,
) {
    fun toDto() = ParticipantDto(
        id,
        creator,
        userId
    )
}
