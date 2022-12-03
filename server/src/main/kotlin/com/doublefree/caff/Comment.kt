package com.doublefree.caff

import com.doublefree.api.model.CommentDto
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@Table(name = "comment")
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doublefree_id_sequence")
    val id: Long? = null,

    @Column(nullable = false)
    val creator: String,

    @Column(nullable = false)
    val createdDate: OffsetDateTime = OffsetDateTime.now(),

    @Column(nullable = false)
    val content: String,

    @Column(nullable = false)
    val caffId: Long,
) {
    fun toDto() = CommentDto(
        id,
        creator,
        createdDate,
        content
    )
}
