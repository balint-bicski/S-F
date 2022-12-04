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
        return try {
            val response = caffService.create(title, file)
            if (response.id == null) {
                ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build()
            } else {
                ResponseEntity.status(HttpStatus.CREATED).body(response)
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    @PreAuthorize("hasAuthority(T(com.doublefree.api.model.Authority).WRITE_NOTE)")
    override fun createComment(id: Long, body: String): ResponseEntity<IdResponseDto> {
        return try {
            val response = caffService.createComment(id, body)
            ResponseEntity.status(HttpStatus.CREATED).body(response)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @PreAuthorize("hasAuthority(T(com.doublefree.api.model.Authority).DELETE_CAFF)")
    override fun deleteCaffFile(id: Long): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(caffService.deleteCaffFile(id))
    }

    @PreAuthorize("hasAuthority(T(com.doublefree.api.model.Authority).DELETE_NOTE)")
    override fun deleteComment(id: Long, commentId: Long): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(caffService.deleteComment(commentId))
    }

    @PreAuthorize("hasAuthority(T(com.doublefree.api.model.Authority).DOWNLOAD_CAFF)")
    override fun downloadCaffFile(id: Long, token: String): ResponseEntity<Resource> {
        return try {
            val resource = caffService.downloadCaffFile(id)
            ResponseEntity.ok(resource)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    override fun getCaffFile(id: Long): ResponseEntity<CaffDto> {
        val caff = caffService.getCaffDetails(id) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        return ResponseEntity.ok(caff)
    }

    override fun getComments(id: Long): ResponseEntity<List<CommentDto>> {
        return ResponseEntity.ok(caffService.getComments(id))
    }

    @PreAuthorize("hasAuthority(T(com.doublefree.api.model.Authority).PAYMENT)")
    override fun purchaseCaffFile(id: Long): ResponseEntity<PurchaseTokenDto> {
        return try {
            ResponseEntity.ok(caffService.purchaseCaffFile(id))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    override fun searchCaffFile(title: String?): ResponseEntity<List<CaffSummaryDto>> {
        return ResponseEntity.ok(caffService.searchByTitle(title))
    }

    @PreAuthorize("hasAuthority(T(com.doublefree.api.model.Authority).MODIFY_CAFF)")
    override fun updateCaffFile(id: Long, body: String): ResponseEntity<Unit> {
        return try {
            val response = caffService.updateTitle(id, body)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }
}
