package com.doublefree.caff

import com.doublefree.api.CaffFileApi
import com.doublefree.api.model.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class CaffController(
    private val caffService: CaffService
) : CaffFileApi {

    @PreAuthorize("hasAuthority(T(com.doublefree.api.model.Authority).UPLOAD_CAFF)")
    override fun createCaff(title: String, desc: String, time: String, wp: String): ResponseEntity<IdResponseDto> {
        return try {
            val response = caffService.create(title, desc, time, wp)
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

    @PreAuthorize("hasAuthority(T(com.doublefree.api.model.Authority).WRITE_NOTE)")
    override fun addParticipant(
        @PathVariable(value = "caffId") caffId: Long,
        @PathVariable(value = "userId") userId: Long
    ): ResponseEntity<IdResponseDto> {
        return try {
            val response = caffService.createParticipant(caffId, userId)
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

    @PreAuthorize("hasAuthority(T(com.doublefree.api.model.Authority).DELETE_NOTE)")
    override fun deleteParticipant(id: Long): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(caffService.deleteParticipant(id))
    }

    override fun deleteParticipantbyUserId(id: Long, userId: Long): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(caffService.deleteParticipantbyUserId(id, userId))
    }

    override fun getCaffFile(id: Long): ResponseEntity<CaffDto> {
        val caff = caffService.getCaffDetails(id) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        return ResponseEntity.ok(caff)
    }

    override fun getComments(id: Long): ResponseEntity<List<CommentDto>> {
        return ResponseEntity.ok(caffService.getComments(id))
    }

    override fun getParticipants(id: Long): ResponseEntity<List<ParticipantDto>> {
        return ResponseEntity.ok(caffService.getParticipants(id))
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
    override fun updateCaffFile(id: Long, title: String, desc: String, time: String, wp: String): ResponseEntity<Unit> {
        return try {
            val response = caffService.updateTitle(id, title, desc, time, wp)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }
}
