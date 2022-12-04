package com.doublefree.util

import lombok.experimental.UtilityClass
import org.springframework.core.io.InputStreamResource
import java.io.InputStream.nullInputStream
import java.nio.file.Files
import java.nio.file.Paths

@UtilityClass
class FileUtil {

    companion object {
        fun getResource(path: String): InputStreamResource =
            InputStreamResource(Files.newInputStream(Paths.get(path)) ?: nullInputStream())

        fun getBytes(path: String): ByteArray? = Files.readAllBytes(Paths.get(path))
    }

}
