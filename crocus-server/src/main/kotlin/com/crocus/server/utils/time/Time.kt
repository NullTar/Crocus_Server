package com.crocus.server.utils.time

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author junsilck
 * @date 2024/5/28 10:01
 */

fun timeRecording(): String {
    return FormatTime.formatTimestamp(System.currentTimeMillis())
}

fun stringToDate(time: String): LocalDateTime {
    val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val parse = LocalDate.parse(time, pattern)
    return parse.atStartOfDay()
}