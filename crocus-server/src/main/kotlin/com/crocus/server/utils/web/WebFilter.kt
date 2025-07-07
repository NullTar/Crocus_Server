package com.crocus.server.utils.web

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebFilter : WebMvcConfigurer {

    private val allowedPaths = setOf(
        //druid
        "/druid/login.html",
        "/favicon.ico",
        "/error",
    )

    override fun addInterceptors(registry: InterceptorRegistry) {
        // 拦截所有路径
        registry.addInterceptor(Interceptor(allowedPaths))
            .addPathPatterns("/**")
    }

}