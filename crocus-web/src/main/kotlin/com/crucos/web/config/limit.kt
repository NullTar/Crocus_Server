package com.crucos.web.config

import io.ktor.server.application.*
import io.ktor.server.plugins.ratelimit.*
import kotlin.time.Duration.Companion.minutes

/**
 * 请求限制的全局配置
 */
fun Application.limit() {
    install(RateLimit) {
        // TODO 记得放开请求限制
//        global {
//            rateLimiter(limit = 10, refillPeriod = 60.seconds)
//        }
        // user: 删除、修改、登陆、新建
        register(RateLimitName("user")) {
            rateLimiter(limit = 5, refillPeriod = 2.minutes)
//            requestKey {a ->
//                TODO("此处设置key")
////                a.request.queryParameters["key"]!!
//            }
        }
    }
}


