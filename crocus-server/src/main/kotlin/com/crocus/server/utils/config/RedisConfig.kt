package com.crocus.server.utils.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
class RedisConfig(private val objectMapper: ObjectMapper) {

    @Bean
    fun redisTemplate(factory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>().apply {
            connectionFactory = factory
            keySerializer = StringRedisSerializer()
            valueSerializer = GenericJackson2JsonRedisSerializer(objectMapper)
            hashKeySerializer = StringRedisSerializer()
            hashValueSerializer = GenericJackson2JsonRedisSerializer(objectMapper)
            setEnableTransactionSupport(true)
        }
        redisTemplate.afterPropertiesSet()
        return redisTemplate
    }

    @Bean
    fun stringSerializer(redisTemplate: RedisTemplate<String, Any>): RedisSerializer<String> {
        return redisTemplate.stringSerializer
    }

    @Bean
    fun anyDeserializer(): RedisSerializer<Any> {
        return GenericJackson2JsonRedisSerializer(objectMapper)
    }

}
