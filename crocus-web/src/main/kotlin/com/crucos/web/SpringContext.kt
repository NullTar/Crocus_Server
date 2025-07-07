package com.crucos.web

import com.crocus.server.ServerApplication
import com.crocus.server.utils.time.timeRecordingNow
import mu.KotlinLogging
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext


/**
 * Springboot的上下文配置
 * MARK: DONE
 */
class SpringContext {

    companion object {
        private val logger = KotlinLogging.logger(SpringContext::class.java.name)
        val server: ConfigurableApplicationContext by lazy {
            logger.info { "Spring Boot Server!!! 元神启动: ${timeRecordingNow()}" }
            SpringApplication(ServerApplication::class.java).run(*arrayOf<String>())
        }
    }
}