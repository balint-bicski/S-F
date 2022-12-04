package com.doublefree.caff

import com.doublefree.api.model.*
import com.doublefree.user.UserService
import com.doublefree.util.FileUtil.Companion.getResource
import com.fasterxml.jackson.databind.ObjectMapper
import eu.jrie.jetbrains.kotlinshell.shell.shell
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.time.ZoneOffset
import javax.transaction.Transactional

@Service
class CaffService(
    private val caffRepository: CaffRepository,
    private val commentRepository: CommentRepository,
    private val purchaseTokenRepository: PurchaseTokenRepository,
    private val userService: UserService,
    private val objectMapper: ObjectMapper
) {

    fun getComments(caffId: Long): List<CommentDto> {
        return commentRepository.findByCaffIdOrderByCreatedDate(caffId).map { it.toDto() }
    }

    fun deleteComment(commentId: Long) {
        commentRepository.deleteById(commentId)
    }

    fun createComment(caffId: Long, body: String): IdResponseDto {
        if (body.isEmpty()) throw IllegalArgumentException("Comment cannot be empty")
        if (!caffRepository.existsById(caffId)) throw IllegalArgumentException("No such CAFF exists")
        return IdResponseDto(
            commentRepository.save(
                Comment(
                    caffId = caffId, creator = userService.currentUser().email, content = body
                )
            ).id
        )
    }

    @Transactional
    @OptIn(ExperimentalCoroutinesApi::class)
    fun deleteCaffFile(id: Long) {
        commentRepository.deleteAllByCaffId(id)
        purchaseTokenRepository.deleteAllByCaffId(id)
        caffRepository.deleteById(id)
        shell {
            file("uploads/raw/$id.caff").delete()
            file("uploads/prev/$id.bmp").delete()
        }
    }

    fun downloadCaffFile(caffId: Long): InputStreamResource {
        val userId = userService.currentUser().id!!
        val token = purchaseTokenRepository.findByCaffIdAndUserId(caffId, userId).firstOrNull()
        if (token != null) {
            return getResource("uploads/raw/$caffId.caff")
        }
        throw Exception("Not authorized to download")
    }

    fun purchaseCaffFile(caffId: Long): PurchaseTokenDto {
        val userId = userService.currentUser().id!!
        val token = purchaseTokenRepository.findByCaffIdAndUserId(caffId, userId).firstOrNull()
        if (token != null) {
            return token.toDto()
        }
        val created = purchaseTokenRepository.save(PurchaseToken(caffId = caffId, userId = userId))
        return PurchaseTokenDto(token = created.token.toString())
    }

    fun getCaffDetails(id: Long): CaffDto? {
        return caffRepository.findByIdOrNull(id)?.toDto()
    }

    fun searchByTitle(title: String?): List<CaffSummaryDto> {
        return caffRepository.findByTitleContainingIgnoreCase(title).map { it.toSummary() }
    }

    fun updateTitle(id: Long, title: String) {
        val found = caffRepository.findById(id).orElseThrow { NoSuchElementException("No CAFF found with id: $id") }
        found.title = title
        caffRepository.save(found)
    }

    @Transactional
    fun create(title: String, file: Resource): IdResponseDto {
        val id = processIncomingCaff(
            file.inputStream.readAllBytes(),
            title,
            userService.currentUser().email,
        )
        return IdResponseDto(id)
    }

    private fun insertCaffIntoDB(parserOutput: String, title: String, uploader: String, fileSize: Int): Long? {
        val parsedOutput = objectMapper.readValue(parserOutput, ParserOutput::class.java)
        if (parsedOutput.success == "yes") {
            if (parsedOutput.data?.previewCreated == "yes") {
                val caff = Caff(
                    null,
                    parsedOutput.data.credits.creator,
                    uploader,
                    parsedOutput.data.credits.date.atOffset(ZoneOffset.UTC),
                    parsedOutput.data.ciffCount,
                    fileSize,
                    title,
                )
                return caffRepository.save(caff).id
            } else {
                throw IllegalStateException("Parse successful but review not created. Aborting upload.")
            }
        } else {
            print("Couldn't process uploaded CAFF: ")
            println(parsedOutput.reason)
            return null
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun processIncomingCaff(data: ByteArray, title: String, uploader: String): Long? {
        var caffId: Long? = null
        try {
            shell {
                //check if folder exists, create if no
                File("uploads/raw").mkdirs()
                File("uploads/prev").mkdirs()

                //create temp file
                file("uploads/temp.caff").writeBytes(data)

                //invoke parser
                val parserOutput = StringBuilder()
                pipeline { "parser/caff_parser uploads/temp.caff uploads/temp.bmp".process() pipe parserOutput }

                //evaluate result
                caffId = insertCaffIntoDB(parserOutput.toString(), title, uploader, data.size)

                //move to
                if (caffId != null) {
                    file("uploads/temp.caff").renameTo(file("uploads/raw/$caffId.caff"))
                    file("uploads/temp.bmp").renameTo(file("uploads/prev/$caffId.bmp"))
                }
            }
            return caffId
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
            return null
        } finally {
            shell {
                file("uploads/temp.caff").delete()
                file("uploads/temp.bmp").delete()
            }
        }
    }
}
