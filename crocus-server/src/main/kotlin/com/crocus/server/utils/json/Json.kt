package com.crocus.server.utils.json

import com.crocus.server.utils.exception.E
import com.crocus.server.utils.response.Response
import com.crocus.server.utils.response.ResponseDTO
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.io.File
import java.io.InputStream
import java.io.Reader
import java.net.URL
import javax.annotation.PostConstruct

@Component
class Json(private val mapper: ObjectMapper) {
    private val logger = KotlinLogging.logger(this.javaClass.name)

    @PostConstruct
    fun init() {
        objectMapper = mapper
        mapper.registeredModuleIds.forEach {
            logger.info("ObjectMapper register module: $it")
        }
    }

    companion object {

        private lateinit var objectMapper: ObjectMapper

        fun toJson(obj: Any): String {
            return objectMapper.writeValueAsString(obj)
        }

        fun <T> fromJson(json: Any, clazz: Class<T>): T {
            return when (json) {
                is ByteArray -> objectMapper.readValue(json, clazz)
                // 必须要判断 redis 取出来的数据会包含 Map 比如 List<Map<K,V>>
                is List<*>, is Map<*, *>, is Set<*>, is Array<*>, is ResponseDTO<*>, is Collection<*>, is Iterable<*> -> {
                    val jsonString = objectMapper.writeValueAsString(json)
                    objectMapper.readValue(jsonString, clazz)
                }

                is String -> objectMapper.readValue(json, clazz)
                else -> {
                    toParser(json).use { parser ->
                        return objectMapper.readValue(parser, clazz)
                    }
                }
            }
        }

        fun <T> fromJson(json: Any, ref: TypeReference<T>): T {
            return when (json) {
                is String -> objectMapper.readValue(json, ref)
                is ByteArray -> objectMapper.readValue(json, ref)
                is List<*>, is Map<*, *>, is Set<*>, is Array<*>, is Collection<*>, is Iterable<*> -> {
                    val jsonString = toJson(json)
                    objectMapper.readValue(jsonString, ref)
                }

                else -> {
                    toParser(json).use { parser ->
                        return objectMapper.readValue(parser, ref)
                    }
                }
            }
        }

        fun <T> fromJson(json: Any, type: JavaType): T {
            return when (json) {
                is String -> objectMapper.readValue(json, type)
                is ByteArray -> objectMapper.readValue(json, type)
                is List<*>, is Map<*, *>, is Set<*>, is Array<*>, is ResponseDTO<*>, is Collection<*>, is Iterable<*> -> {
                    val jsonString = objectMapper.writeValueAsString(json)
                    objectMapper.readValue(jsonString, type)
                }

                else -> {
                    toParser(json).use { parser ->
                        return objectMapper.readValue(parser, type)
                    }
                }
            }
        }

        private fun toParser(json: Any): JsonParser {
            return when (json) {
                is JsonParser -> json
                is File -> objectMapper.factory.createParser(json)
                is Reader -> objectMapper.factory.createParser(json)
                is URL -> objectMapper.factory.createParser(json)
                is InputStream -> objectMapper.factory.createParser(json)
                else -> throw E(Response.JSON_DESERIALIZATION_ERROR)
            }
        }

    }
}


