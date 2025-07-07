package com.crocus.server.utils.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Configuration
class ObjectMapperConfig {

    @Bean
    fun objectMapper(): Jackson2ObjectMapperBuilderCustomizer {
        return Jackson2ObjectMapperBuilderCustomizer { builder ->
            val javaTimeModule = JavaTimeModule()
            val formatterLocal = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formatterZone = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss E XXX")

            javaTimeModule.addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(formatterLocal))
            javaTimeModule.addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(formatterLocal))
            javaTimeModule.addSerializer(ZonedDateTime::class.java, ZonedDateTimeSerializer(formatterZone))

            val kotlinModule = KotlinModule.Builder()
                .configure(KotlinFeature.NullIsSameAsDefault, true)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build()

            builder.modules(javaTimeModule, kotlinModule)

            builder.serializationInclusion(JsonInclude.Include.NON_NULL)
            builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        }
    }

}