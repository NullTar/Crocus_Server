package com.crucos.web.config

import com.crocus.server.utils.time.timeRecordingNow
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import io.ktor.util.*
import org.slf4j.event.Level

/**
 * @author junsilck
 * @date 2024/5/28 10:33
 * 请求日志记录和过滤
 */
fun Application.callLogger() {

    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
        format { call ->
            """
----------------------请求开始----------------------
URI: ${call.request.uri}
Method: ${call.request.httpMethod.value}
Status: ${call.response.status()}
User-Agent: ${call.request.headers["User-Agent"]}
Headers: ${call.request.headers.toMap()}
QueryParams: ${call.request.queryParameters.toMap()}
Where: ${call.request.local}
Time: ${timeRecordingNow()}
----------------------请求结束----------------------
""".trimIndent()
        }
    }
}
