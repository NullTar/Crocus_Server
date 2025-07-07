package com.crucos.web.config

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cachingheaders.*

/** 请求头的全局配置 */
fun Application.headerCaching() {
    install(CachingHeaders) {
        options { _, content ->
            when (content.contentType?.withoutParameters()) {
                ContentType.Text.Any -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 1200))
                ContentType.Application.Any -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 120))
                else -> {
                    null
                }
            }
        }
    }
}