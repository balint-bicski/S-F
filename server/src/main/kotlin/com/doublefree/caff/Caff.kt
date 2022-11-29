package com.doublefree.caff

import com.doublefree.api.model.CaffDto
import com.doublefree.api.model.CaffSummaryDto
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.core.io.FileUrlResource
import javax.persistence.*

@Entity
@Table(name="caff_metadata")
class Caff(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doublefree_id_sequence")
    val Id: Long? = null,

    @Column(nullable = false)
    val creator: String,

    @Column(nullable = false)
    val uploader: String,

    @Column(nullable = false)
    val createdDate: java.time.OffsetDateTime,

    @Column(nullable = false)
    val ciffCount: Int,

    @Column(nullable = false)
    var title: String,

    ) {

    fun toSummary(): CaffSummaryDto {
        return CaffSummaryDto(
            title,
            FileUrlResource("uploads/prev/$Id.png"),
            Id
        )
    }

    fun toDto(): CaffDto {
        return CaffDto(
            creator,
            Id,
            uploader,
            createdDate,
            ciffCount,
            11111, //TODO add size property
            title,
            FileUrlResource("uploads/prev/$Id.png")
        )
    }
}