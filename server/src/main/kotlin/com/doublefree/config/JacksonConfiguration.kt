package com.doublefree.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.ByteArrayHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

@Configuration
class JacksonConfiguration {

    @Bean
    fun mapper(): ObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    @Bean
    fun mappingJackson2HttpMessageConverter(): MappingJackson2HttpMessageConverter =
        MappingJackson2HttpMessageConverter(mapper())

    @Bean
    fun byteArrayHttpMessageConverter(): ByteArrayHttpMessageConverter = ByteArrayHttpMessageConverter()

}
