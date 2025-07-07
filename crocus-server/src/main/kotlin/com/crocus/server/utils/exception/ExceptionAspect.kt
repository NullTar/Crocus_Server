package com.crocus.server.utils.exception

import com.crocus.server.utils.time.timeRecordingNow
import com.fasterxml.jackson.databind.JsonMappingException
import mu.KotlinLogging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
class ExceptionAspect {

    private val logger = KotlinLogging.logger(ExceptionAspect::class.java.name)

    @Pointcut("execution(* com.crocus..*(..))")
    fun anyMethod() = Unit

    /**
     * @param joinPoint 抛出异常的方法
     * @param e 异常处理类
     * @method 处理切入点和拦截报错的信息
     */
    @AfterThrowing(pointcut = "anyMethod()", throwing = "e")
    fun newException(joinPoint: JoinPoint, e: Exception) {
        val sourceMethod = joinPoint.signature.toShortString()
        val time = timeRecordingNow()
        val topFrame = e.stackTrace.firstOrNull()
        val location = topFrame?.let {
            "${it.className}.${it.methodName}(${it.fileName}:${it.lineNumber})"
        } ?: "未知位置"
        val baseInfo = buildString {
            appendLine("地址: $sourceMethod")
            appendLine("时间: $time")
            appendLine("位置: $location")
        }

        val jsonError = if (e is JsonMappingException) {
            val fieldPath = e.path.joinToString(" -> ") { it.fieldName ?: "[index:${it.index}]" }
            buildString {
                appendLine("字段路径: $fieldPath")
                appendLine("错误描述: ${e.originalMessage}")
            }
        } else ""

        val text = buildString {
            if (e is E) appendLine("代码: ${e.code}")
            append(baseInfo)
            appendLine("消息: ${e.message}")
            appendLine("原因: ${e.cause}")
            append(jsonError)
            appendLine("StackTrace: ${e.stackTraceToString()}")
        }
        logger.error(text)
    }
}