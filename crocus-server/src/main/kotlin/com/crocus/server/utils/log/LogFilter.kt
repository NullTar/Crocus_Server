package com.crocus.server.utils.log

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

/**
 * 命令控制台 Log 拦截
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
class LogFilter : Filter<ILoggingEvent>() {

    private val toFilterList: List<String> = listOf(
        "Java", "Loading", "Refreshing", "Identified", "Autowiring", "Creating", "ThemeSource",
        "Successfully", "availability", "AspectJ ", "archive", "Detected", "Writing", "Use localhost address",
        "document", "service", "Servlet", "Initializing","Completed", "StdOutImpl",
        "WebApplicationContext", "filters", "servlets", "configured", "definitions", "MacBook",
        "DataSource", "CONDITIONS", "inited", "codes", "Connected", "Completed", "initialized",
        "Starting beans", "ProtocolHandler", "Bean with name", "Autodetecting", "Using",
        "Registering", "registered", "Handler", "Controller", "GET", "Unregistering", "Exiting",
        "resourceHandlerMapping", "Found", "shut", "Shutting", "Stopping", "stop",
        "resolve [simple type", "resolve class", "declared on", "resolve java", "mapperLocations",
        "Trying to load", "Persistence", "Setting custom", "development mode", "Logging Provider",
        "Hibernate Validator", "Get network interface info", "machineId", "Adding converter from class",
        "Serializer for", "Copier for", "DDL start", "DDL end", "MacOS", "Moving from UNINITIALIZED to AVAILABLE",
        "Decode done, empty stack: false", "Services...", "kotlin.Unit",
    )

    /** @param event 日志事件 */
    override fun decide(event: ILoggingEvent): FilterReply {

        if (event.level == Level.ERROR) {
            return FilterReply.ACCEPT
        }

        for (text in toFilterList) {
            if (event.formattedMessage.contains(text)) {
                return FilterReply.DENY
            }
        }

        return FilterReply.NEUTRAL
    }

}