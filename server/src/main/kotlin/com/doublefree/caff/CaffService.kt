package com.doublefree.caff

import com.doublefree.api.model.*
import eu.jrie.jetbrains.kotlinshell.shell.shell
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.core.io.FileUrlResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import java.io.IOException

class CaffService(private val caffRepository: CaffRepository) {

    fun getComments(caffId: Long): List<CommentDto> {
        throw NotImplementedError()
    }

    fun deleteComment(commentId: Long) {
        throw NotImplementedError()
    }

    fun createComment(caffId: Long, body: String): IdResponseDto {
        throw NotImplementedError()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun deleteCaffFile(id: Long) {
        //TODO permission check
        caffRepository.deleteById(id)
        shell {
            file("uploads/raw/$id.caff").delete()
            file("uploads/prev/$id.png").delete()
        }
    }

    fun downloadCaffFile(id: Long): Resource {
        //TODO purchase check, don't use tainted Id
        return FileUrlResource("uploads/raw/$id.caff")
    }

    fun purchaseCaffFile(id: Long): PurchaseTokenDto {
        throw NotImplementedError()
    }

    fun getCaffDetails(id: Long): CaffDto {
        return caffRepository.findById(id).get().toDto()
    }

    fun searchByTitle(title: String) : List<CaffSummaryDto> {
        return caffRepository.findByTitle(title).map { it.toSummary() }
    }

    fun updateTitle(id: Long, title: String) {
        //TODO permission check
        val found = caffRepository.findById(id).get()
        found.title = title
        caffRepository.save(found)
    }

    fun create(caffFileDto : CaffFileDto) : IdResponseDto {
        //TODO login check
        val id = processIncomingCaff(
            caffFileDto.file.inputStream.readAllBytes(),
            caffFileDto.title,
            "TODO: uploader"
        )
        return IdResponseDto(id)
    }

    private fun insertCaffIntoDB(parserOutput: String, title: String, uploader: String) : Long? {
        //TODO: somebody grab a Json deserializer
        val parsedOutput = ParserOutput()
        if (parsedOutput.success == "yes") {
            if (parsedOutput.data?.preview_created == "yes") {
                val caff = Caff(
                    null,
                    parsedOutput.data.credits.creator,
                    uploader,
                    parsedOutput.data.credits.date,
                    parsedOutput.data.ciff_count,
                    title
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
                pipeline { "parser/caff_parser temp.caff temp.png".process() pipe parserOutput }

                //evaluate result
                caffId = insertCaffIntoDB(parserOutput.toString(), title, uploader)

                //move to
                if (caffId != null) {
                    file("temp.caff").renameTo(file("uploads/raw/$caffId.caff"))
                    file("temp.png").renameTo(file("uploads/prev/$caffId.caff"))
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
                file("temp.png").delete()
            }
        }
    }
}