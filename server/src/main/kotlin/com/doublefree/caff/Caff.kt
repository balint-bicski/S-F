package com.doublefree.caff

import com.doublefree.api.model.CaffDto
import com.doublefree.api.model.CaffSummaryDto
import com.doublefree.util.FileUtil.Companion.getBytes
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@Table(name = "caff_metadata")
class Caff(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doublefree_id_sequence")
    val id: Long? = null,

    @Column(nullable = false)
    val creator: String,

    @Column(nullable = false)
    val uploader: String,

    @Column(nullable = false)
    val createdDate: OffsetDateTime,

    @Column(nullable = false)
    val ciffCount: Int,

    @Column(nullable = false)
    val size: Int,

    @Column(nullable = false)
    var title: String,

    ) {

    fun toSummary(): CaffSummaryDto {
        return CaffSummaryDto(title, id, getBytes("/uploads/prev/$id.bmp"))
    }

    fun toDto(): CaffDto {
        return CaffDto(
            creator, id, uploader, createdDate, ciffCount, size, title, getBytes("/uploads/prev/$id.caff")
        )
    }
}
