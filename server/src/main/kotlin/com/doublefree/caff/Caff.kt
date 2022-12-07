package com.doublefree.caff

import com.doublefree.api.model.CaffDto
import com.doublefree.api.model.CaffSummaryDto
import javax.persistence.*

@Entity
@Table(name = "caff_metadata")
class Caff(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doublefree_id_sequence")
    val id: Long? = null,

    @Column(nullable = false)
    val uploader: String,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = true)
    var desc: String,

    @Column(nullable = false)
    var time: String,

    @Column(nullable = false)
    var wp: String,

    ) {

    fun toSummary(): CaffSummaryDto {
        return CaffSummaryDto(title, id, wp)
    }

    fun toDto(): CaffDto {
        return CaffDto(
             id, uploader, title, desc, time, wp,
        )
    }
}
