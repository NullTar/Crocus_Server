package com.crucos.web

import com.crucos.web.config.*
import com.crucos.web.utils.SSL
import com.crocus.server.utils.time.timeRecordingNow
import com.crucos.web.route.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mu.KotlinLogging


// 配置启动类
fun main() {
    embeddedServer(Netty, engineEnvironment(), configure = {
        requestQueueLimit = 16
        connectionGroupSize = 8
        // 可自动根据系统设置线程数
        workerGroupSize = 8
        shutdownGracePeriod = 2000
        shutdownTimeout = 3000
        maxInitialLineLength = 2048
        maxHeaderSize = 1024
        maxChunkSize = 42
    }).start(wait = true)
}

private fun engineEnvironment(): ApplicationEngineEnvironment {

    val logger = KotlinLogging.logger(ApplicationEngineEnvironment::class.java.name + ".engineEnvironment")

    return applicationEngineEnvironment {
        // 调用初始化方法
        val ssl = initialize()
        // 全局模块配置
        module { module() }
        // TODO 启用热更新会遇到SpringBoot Bean无法找到的问题，待处理
        // developmentMode = true 启用开发者模式，热更新

        // 业务服务器
        connector {
            host = "127.0.0.1"
            port = 9099
            // 单独模块配置
            module {
                common()
            }
        }
        // 用户服务器
        connector {
            host = "127.0.0.1"
            port = 8848
            module {
                user()
            }
        }
        // 配置 SSL 证书 TODO 上线后待处理
        sslConnector(
            keyStore = ssl.keyStore,
            keyAlias = "sslAlias",
            keyStorePassword = { "732613".toCharArray() },
            privateKeyPassword = { "JunSilck@732613".toCharArray() }) {
            port = 8443
            keyStorePath = ssl.keyStoreFile
            logger.info { "SSL 连接结束: ${timeRecordingNow()}" }
        }
    }
}

/**
 * 初始化的方法
 */
private fun initialize(): SSL {
    // 初始化 Spring Boot 上下文
    CoroutineScope(Dispatchers.Default).launch { SpringContext.server }
    // 初始化SSL
    return SSL()
}

/**
 * 配置模块的方法
 */
private fun Application.module() {
    // 路由拦截w
    globalRoute()
    // 状态页面
    statusPages()
    // Api 调用日志
    callLogger()
    // 接口限制
    limit()
    // 请求头配置
    headerCaching()
    // 时区协商
    contentNegotiation()
}


/**
 * @author junsilck
 * @date 2024/5/27 18:10
 */

fun Application.user() {
    /** 请求地址router配置 */
    routing {
        userRouting()
    }
}

/**
 * @author junsilck
 * @date 2024/5/27 18:07
 * @description 公共模块设置
 */
fun Application.common() {
    /** 请求地址router配置 */
    routing {
        articleRouting()
        storyRouting()
        bookRouting()
        question()
        announcementRouting()
    }
    // 初始化公共热数据到缓存
    CoroutineScope(Dispatchers.IO).launch {
    }
}








