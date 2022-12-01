package com.doublefree.caff

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.OffsetDateTime

data class ParserOutput (
    @field:JsonProperty("success") val success: String = "no",
    @field:JsonProperty("reason") val reason: String? = "not called",
    @field:JsonProperty("data") val data: ParserSuccessData? = null,
)

data class ParserSuccessData (
    @field:JsonProperty("ciff_count") val ciff_count: Int,
    @field:JsonProperty("credits") val credits: CaffCredits,
    @field:JsonProperty("frames") val frames: List<CaffFrame>,
    @field:JsonProperty("preview_created") val preview_created: String,
)

data class CaffCredits (
    @field:JsonProperty("creator") val creator: String,
    @field:JsonProperty("date") val date: OffsetDateTime,
)

data class CaffFrame(
    @field:JsonProperty("image") val image: Ciff,
    @field:JsonProperty("duration") val duration: Long,
)

class Ciff(
    @field:JsonProperty("width") val width: Int,
    @field:JsonProperty("height") val height: Int,
    @field:JsonProperty("caption") val caption: String,
    @field:JsonProperty("tags") val tags: List<String>,
    @field:JsonProperty("content_size") val content_size: Long,
)