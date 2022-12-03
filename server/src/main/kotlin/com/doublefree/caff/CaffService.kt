package com.doublefree.caff

import com.doublefree.api.model.*
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import eu.jrie.jetbrains.kotlinshell.shell.shell
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.core.io.FileUrlResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.io.IOException
import java.time.OffsetDateTime

@Service
class CaffService(
    private val caffRepository: CaffRepository,
    private val commentRepository: CommentRepository,
    private val purchaseTokenRepository: PurchaseTokenRepository,
) {

    fun getComments(caffId: Long): List<CommentDto> {
        return commentRepository.findByCaffId(caffId).map { it.toDto() }
    }

    fun deleteComment(commentId: Long) {
        commentRepository.deleteById(commentId)
    }

    fun createComment(caffId: Long, userName: String, body: String): IdResponseDto {
        return IdResponseDto(
            commentRepository.save(
                Comment(
                    0,
                    caffId,
                    userName,
                    OffsetDateTime.now(),
                    body
                )
            ).Id
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun deleteCaffFile(id: Long) {
        caffRepository.deleteById(id)
        shell {
            file("uploads/raw/$id.caff").delete()
            file("uploads/prev/$id.bmp").delete()
        }
    }

    fun downloadCaffFile(caffId: Long, userId: Long): Resource {
        //TODO cursed feature
        val skipAuthorization = true //for testing only

        val token = purchaseTokenRepository.findByCaffIdAndUserId(caffId, userId).firstOrNull()
        if(token != null || skipAuthorization) {
            return FileUrlResource("uploads/raw/$caffId.caff")
        }
        throw Exception("Not authorized to download")
    }

    fun purchaseCaffFile(caffId: Long, userId: Long): PurchaseTokenDto {
        val token = PurchaseToken(
            0,
            OffsetDateTime.now(),
            userId,
            caffId
        )
        val created = purchaseTokenRepository.save(token)
        if(created.Id != null) {
            return PurchaseTokenDto(token.created.toString())
        }
        throw java.lang.IllegalStateException("Couldn't create purchase token. This is bad.")
    }

    fun getCaffDetails(id: Long): CaffDto {
        return caffRepository.findById(id).get().toDto()
    }

    fun searchByTitle(title: String?) : List<CaffSummaryDto> {
        return caffRepository.findByTitle(title ?: "").map { it.toSummary() }
    }

    fun updateTitle(id: Long, title: String) {
        val found = caffRepository.findById(id).get()
        found.title = title
        caffRepository.save(found)
    }

    fun create(caffFileDto : CaffFileDto, uploader: String) : IdResponseDto {
        val id = processIncomingCaff(
            caffFileDto.file.inputStream.readAllBytes(),
            caffFileDto.title,
            uploader,
        )
        return IdResponseDto(id)
    }

    private fun insertCaffIntoDB(parserOutput: String, title: String, uploader: String, fileSize: Int) : Long? {
        val parsedOutput = ObjectMapper().readValue(parserOutput, ParserOutput::class.java)
        if (parsedOutput.success == "yes") {
            if (parsedOutput.data?.preview_created == "yes") {
                val caff = Caff(
                    null,
                    parsedOutput.data.credits.creator,
                    uploader,
                    parsedOutput.data.credits.date,
                    parsedOutput.data.ciff_count,
                    fileSize,
                    title,
                )
                return caffRepository.save(caff).Id
            }
            else {
                throw IllegalStateException("Parse successful but review not created. Aborting upload.")
            }
        }
        else {
            print("Couldn't process uploaded CAFF: ")
            println(parsedOutput.reason)
            return null
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun processIncomingCaff(data: ByteArray, title: String, uploader: String): Long? {
        var caffId : Long? = null;
        try {
            shell {
                //create temp file
                file("temp.caff").writeBytes(data)

                //invoke parser
                val parserOutput = StringBuilder()
                pipeline { "parser/caff_parser temp.caff temp.bmp".process() pipe parserOutput }

                //evaluate result
                caffId = insertCaffIntoDB(parserOutput.toString(), title, uploader, data.size)

                //move to
                if (caffId != null) {
                    file("temp.caff").renameTo(file("uploads/raw/$caffId.caff"))
                    file("temp.bmp").renameTo(file("uploads/prev/$caffId.bmp"))
                }
            }
            return caffId
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        catch (ex: IllegalStateException) {
            ex.printStackTrace()
            return null
        } finally {
            shell {
                file("temp.caff").delete()
                file("temp.bmp").delete()
            }
        }
    }
}