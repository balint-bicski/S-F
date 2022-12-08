package com.sandf.event

import com.sandf.api.model.EventDto
import com.sandf.api.model.EventSummaryDto
import javax.persistence.*

@Entity
@Table(name = "event_metadata")
class Event(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sandf_id_sequence")
    val id: Long? = null,

    @Column(nullable = false)
    val creator: String,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = true)
    var desc: String,

    @Column(nullable = false)
    var time: String,

    @Column(nullable = false)
    var wp: String,

    ) {

    fun toSummary(): EventSummaryDto {
        return EventSummaryDto(title, id, wp)
    }

    fun toDto(): EventDto {
        return EventDto(
             id, creator, title, desc, time, wp,
        )
    }
}
