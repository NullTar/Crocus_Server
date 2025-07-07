package com.crocus.server.mapper

import com.crocus.server.utils.exception.E
import com.crocus.server.utils.json.Json
import com.crocus.server.utils.response.Response
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class RedisDao @Autowired constructor(
    private val redis: RedisTemplate<String, Any>,
    private val keySerializer: RedisSerializer<String>,
    private val valueSerializer: RedisSerializer<Any>
) {

    /**
     * 拿数据
     * @param key String
     * @return T
     */
    fun <T> fetch(key: String?, clazz: Class<T>? = null, ref: TypeReference<T>? = null, type: JavaType? = null): T? {
        if (key.isNullOrBlank()) throw E(Response.FUNCTION_PARAMETER_ERROR)
        if (clazz == null && ref == null && type == null) throw E(Response.FUNCTION_PARAMETER_ERROR)
        val value = redis.opsForValue().get(key)
            ?: return null
        return clazz?.let {
            Json.fromJson(value, it)
        } ?: ref?.let {
            Json.fromJson(value, it)
        } ?: type?.let {
            Json.fromJson<T>(value, it)
        }
    }

    /**
     * 存数据 存在就跳过
     * @param key String
     * @param value Any
     * @param duration 分钟 default: 10 * 60
     * @param overwrite 覆盖 default: true
     * @sample
     * - key value 存数据 默认 10 * 60 可覆盖
     * - key value duration = null 永不过期 可覆盖
     * - key value null false 永不过期 不可覆盖
     */
    fun saveWithTransaction(key: String?, value: Any?, duration: Long? = 10 * 60, overwrite: Boolean = true) {
        if (key.isNullOrBlank() || value == null) {
            throw E(Response.INSERT_FAIL)
        }
        val serializedKey = keySerializer.serialize(key) ?: throw E(Response.JSON_SERIALIZATION_ERROR)
        val serializedValue = valueSerializer.serialize(value) ?: throw E(Response.JSON_SERIALIZATION_ERROR)
        redis.execute { connection ->
            connection.multi() // 开启事务
            runCatching {
                if (overwrite) {
                    // 如果允许覆盖，直接设置值
                    connection.set(serializedKey, serializedValue)
                } else {
                    // 如果不允许覆盖，使用 setIfAbsent
                    connection.setNX(serializedKey, serializedValue)
                }
                duration?.let { connection.expire(key.toByteArray(), duration) }
                // 提交事务
                connection.exec()
            }.getOrElse {
                connection.discard()
                throw E(Response.QUERY_FAIL)
            }
        }
    }

    /**
     * 设置过期时间
     */
    fun setTime(key: String?, duration: Long) {
        if (key.isNullOrBlank()) {
            return
        }
        redis.execute { connection ->
            connection.multi()
            // 设置过期时间
            connection.expire(key.toByteArray(), duration)
            connection.exec()
        }
    }

    /**
     * 获取过期时间
     */
    fun expireTime(key: String?): Long {
        if (key.isNullOrBlank()) return 0
        val ops = redis.opsForValue()
        return ops.operations.getExpire(key, TimeUnit.SECONDS) ?: 0
    }

    /**
     * 删数据
     * @param key String
     * @return Boolean
     */
    fun delete(key: String?) {
        if (key.isNullOrBlank()) return
        val ops = redis.opsForValue()
        ops.getAndDelete(key)
    }

    /**
     * 根据keys删数据
     * @param key Collection
     * @return Long
     */
    fun deleteWithKeys(key: Collection<String>?) {
        if (key.isNullOrEmpty()) {
            return
        }
        val ops = redis.opsForValue()
        key.forEach {
            ops.getAndDelete(it)
        }
    }

}