package com.crocus.server.utils.web

import mu.KotlinLogging
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class Interceptor(private val allowedPaths: Set<String>) : HandlerInterceptor {

    private val logger = KotlinLogging.logger(Interceptor::class.java.name)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        val uri = request.requestURI

        // 放行在白名单中的路径
        if (allowedPaths.contains(uri)) return true

        // 放行 Controller 方法
        if (handler is HandlerMethod) return true

        // 非 controller 且不在白名单，拦截
        logger.info { "拦截非法请求: $uri" }
        response.status = HttpServletResponse.SC_NO_CONTENT
        response.sendError(response.status)
        response.flushBuffer()
        return false
    }
}