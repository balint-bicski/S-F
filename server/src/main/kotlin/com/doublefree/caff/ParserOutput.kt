package com.doublefree.caff

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class ParserOutput(
    @field:JsonProperty("success") val success: String = "no",
    @field:JsonProperty("reason") val reason: String? = "not called",
    @field:JsonProperty("data") val data: ParserSuccessData? = null,
)

data class ParserSuccessData(
    @field:JsonProperty("ciff_count", required = true) val ciffCount: Int,
    @field:JsonProperty("credits", required = true) val credits: CaffCredits,
    @field:JsonProperty("frames", required = true) val frames: List<CaffFrame>,
    @field:JsonProperty("preview_created", required = true) val previewCreated: String,
    @field:JsonProperty("preview_location") val previewLocation: String = "",
)

data class CaffCredits(
    @field:JsonProperty("creator", required = true) val creator: String,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @field:JsonProperty("date", required = true) val date: LocalDateTime,
)

data class CaffFrame(
    @field:JsonProperty("image", required = true) val image: Ciff,
    @field:JsonProperty("duration", required = true) val duration: Long,
)

data class Ciff(
    @field:JsonProperty("width", required = true) val width: Int,
    @field:JsonProperty("height", required = true) val height: Int,
    @field:JsonProperty("caption", required = true) val caption: String,
    @field:JsonProperty("tags", required = true) val tags: List<String>,
    @field:JsonProperty("content_size", required = true) val contentSize: Long,
)
