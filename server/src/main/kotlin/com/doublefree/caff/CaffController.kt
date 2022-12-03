package com.doublefree.caff

import com.doublefree.api.CaffFileApi
import com.doublefree.api.model.*
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RestController

@RestController
class CaffController(
    private val caffService: CaffService
) : CaffFileApi {

    @PreAuthorize("hasAuthority(T(com.doublefree.api.model.Authority).UPLOAD_CAFF)")
    override fun createCaffFile(title: String, file: Resource): ResponseEntity<IdResponseDto> {
        val response = caffService.create(title, file)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PreAuthorize("hasAuthority(T(com.doublefree.api.model.Authority).WRITE_NOTE)")
    override fun createComment(id: Long, body: String): ResponseEntity<IdResponseDto> {
        return ResponseEntity.ok(caffService.createComment(id, body))
    }

    @PreAuthorize("hasAuthority(T(com.doublefree.api.model.Authority).DELETE_CAFF)")
    override fun deleteCaffFile(id: Long): ResponseEntity<Unit> {
        return ResponseEntity.ok(caffService.deleteCaffFile(id))
    }

    @PreAuthorize("hasAuthority(T(com.doublefree.api.model.Authority).DELETE_NOTE)")
    override fun deleteComment(id: Long, commentId: Long): ResponseEntity<Unit> {
        return ResponseEntity.ok(caffService.deleteComment(id))
    }

    @PreAuthorize("hasAuthority(T(com.doublefree.api.model.Authority).DOWNLOAD_CAFF)")
    override fun downloadCaffFile(id: Long, token: String): ResponseEntity<Resource> {
        return ResponseEntity.ok(caffService.downloadCaffFile(id))
    }

    override fun getCaffFile(id: Long): ResponseEntity<CaffDto> {
        return ResponseEntity.ok(caffService.getCaffDetails(id))
    }

    override fun getComments(id: Long): ResponseEntity<List<CommentDto>> {
        return ResponseEntity.ok(caffService.getComments(id))
    }

    @PreAuthorize("hasAuthority(T(com.doublefree.api.model.Authority).PAYMENT)")
    override fun purchaseCaffFile(id: Long): ResponseEntity<PurchaseTokenDto> {
        return ResponseEntity.ok(caffService.purchaseCaffFile(id))
    }

    override fun searchCaffFile(title: String?): ResponseEntity<List<CaffSummaryDto>> {
        return ResponseEntity.ok(caffService.searchByTitle(title))
    }

    @PreAuthorize("hasAuthority(T(com.doublefree.api.model.Authority).MODIFY_CAFF)")
    override fun updateCaffFile(id: Long, body: String): ResponseEntity<Unit> {
        return ResponseEntity.ok(caffService.updateTitle(id, body))
    }

}
