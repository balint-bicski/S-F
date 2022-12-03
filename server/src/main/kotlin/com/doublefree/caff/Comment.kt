package com.doublefree.caff

import com.doublefree.api.model.CommentDto
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@Table(name="comments")
class Comment (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doublefree_id_sequence")
    val Id: Long? = null,

    @Column(nullable = false)
    val caffId: Long,

    @Column(nullable = false)
    val creator: String,

    @Column(nullable = false)
    val createdDate: OffsetDateTime,

    @Column(nullable = false)
    val content: String,
){
    fun toDto() = CommentDto(
        Id,
        creator,
        createdDate,
        content
    )
}