package com.crocus.server.utils.time

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


/**
 * @author junsilck
 * @date 2024/5/28 10:01
 */
fun timeRecordingNow(): String {
    return FormatTime.now()
}

class FormatTime {

    companion object {


        fun now(): String = formatTimestamp(null, null)

        fun getYMD ():String = formatTimestamp(null, "yyyy-MM-dd")

        /**
         * @param timestamp 时间戳
         * @return 返回处理时间戳后的字符串
         */
        fun formatTimestamp(timestamp: Long?, pattern: String? = null): String {
            val instant = when (timestamp) {
                null -> Instant.now()
                else -> Instant.ofEpochMilli(timestamp)
            }
            val formatter = when (pattern) {
                null -> DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
                else -> DateTimeFormatter.ofPattern(pattern)
            }
            val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            return localDateTime.format(formatter)
        }

        fun stringToDate(time: String): LocalDateTime {
            val formatter = when (time.length) {
                // 例如：2024-05-06
                10 -> DateTimeFormatter.ofPattern("yyyy-MM-dd")
                // 例如：2024-05-06 14:23
                16 -> DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                // 例如：2024-05-06 14:23:59
                19 -> DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                // 例如：2024-05-06 14:23:59.123
                23 -> DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                // 例如：2024-05-06 14:23:59.123111
                26 -> DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
                else -> throw IllegalArgumentException("不支持的时间格式: $time")
            }

            return LocalDateTime.parse(time, formatter)
        }
    }
}