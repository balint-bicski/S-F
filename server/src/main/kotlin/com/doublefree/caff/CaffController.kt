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
        val response = caffService.create(caffFileDto, "TODO uploader") //TODO uploader
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    override fun createComment(id: Long, body: String): ResponseEntity<IdResponseDto> {
        //TODO yadda-yadda
        val userName: String = "NoOne"; //TODO userName !
        return ResponseEntity.ok(caffService.createComment(id, userName, body))
    }

    override fun deleteCaffFile(id: Long): ResponseEntity<Unit> {
        //TODO yadda-yadda
        return ResponseEntity.ok(caffService.deleteCaffFile(id))
    }

    override fun deleteComment(id: Long, commentId: Long): ResponseEntity<Unit> {
        //TODO yadda-yadda
        return ResponseEntity.ok(caffService.deleteComment(id))
    }

    override fun downloadCaffFile(id: Long): ResponseEntity<Resource> {
        //TODO yadda-yadda
        //TODO cursed feature, tread cautiously
        return ResponseEntity.ok(caffService.downloadCaffFile(id, 0))
    }

    override fun getCaffFile(id: Long, token: String): ResponseEntity<CaffDto> {
        //TODO yadda-yadda
        return ResponseEntity.ok(caffService.getCaffDetails(id))
    }

    override fun getComments(id: Long): ResponseEntity<List<CommentDto>> {
        //TODO yadda-yadda
        return ResponseEntity.ok(caffService.getComments(id))
    }

    override fun purchaseCaffFile(id: Long): ResponseEntity<PurchaseTokenDto> {
        //TODO yadda-yadda
        val userId: Long = -1; //TODO userId !
        return ResponseEntity.ok(caffService.purchaseCaffFile(id, userId))
    }

    override fun searchCaffFile(title: String?): ResponseEntity<List<CaffSummaryDto>> {
        //TODO yadda-yadda
        return ResponseEntity.ok(caffService.searchByTitle(title))
    }

    override fun updateCaffFile(id: Long, body: String): ResponseEntity<Unit> {
        //TODO yadda-yadda
        return ResponseEntity.ok(caffService.updateTitle(id, body))
    }

}