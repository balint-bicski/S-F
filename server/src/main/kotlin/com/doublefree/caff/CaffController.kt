package com.doublefree.caff

import com.doublefree.api.CaffFileApi
import com.doublefree.api.model.*
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class CaffController(
    private val caffService: CaffService
) : CaffFileApi {
    override fun createCaffFile(caffFileDto: CaffFileDto): ResponseEntity<IdResponseDto> {
        val response = caffService.create(caffFileDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    override fun createComment(id: Long, body: String): ResponseEntity<IdResponseDto> {
        return super.createComment(id, body)
    }

    override fun deleteCaffFile(id: Long): ResponseEntity<Unit> {
        return super.deleteCaffFile(id)
    }

    override fun deleteComment(id: Long, commentId: Long): ResponseEntity<Unit> {
        return super.deleteComment(id, commentId)
    }

    override fun downloadCaffFile(id: Long): ResponseEntity<Resource> {
        return super.downloadCaffFile(id)
    }

    override fun getCaffFile(id: Long, token: String): ResponseEntity<CaffDto> {
        return super.getCaffFile(id, token)
    }

    override fun getComments(id: Long): ResponseEntity<List<CommentDto>> {
        return super.getComments(id)
    }

    override fun purchaseCaffFile(id: Long): ResponseEntity<PurchaseTokenDto> {
        return super.purchaseCaffFile(id)
    }

    override fun searchCaffFile(title: String?): ResponseEntity<List<CaffSummaryDto>> {
        return super.searchCaffFile(title)
    }

    override fun updateCaffFile(id: Long, body: String): ResponseEntity<Unit> {
        return super.updateCaffFile(id, body)
    }

}