package com.crocus.server.utils.database

import mu.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
class DataSourceAspect {

    private val logger = KotlinLogging.logger(this::class.java.simpleName)

    @Around("@annotation(dataSourceAnnotation) || @within(dataSourceAnnotation)")
    fun around(point: ProceedingJoinPoint, dataSourceAnnotation: DataSource): Any {
        val value = dataSourceAnnotation.value.ifEmpty { SourceName.MAIN }

        val signature = point.signature as MethodSignature
        val method = signature.method
        val dataClass = Class.forName(signature.declaringTypeName)

        val dsMethod = method.getAnnotation(DataSource::class.java)
        val dsClass = dataClass.getAnnotation(DataSource::class.java)
        when {
            dsMethod != null -> {
                DataSourceContextHolder.setDataSource(dsMethod.value)
                logger.info { "调用服务 & 设置数据源为(Method): ${dsMethod.value}" }
            }

            dsClass != null -> {
                DataSourceContextHolder.setDataSource(dsClass.value)
                logger.info { "调用服务 & 设置数据源为(Class): ${dsClass.value}" }
            }

            else -> {
                DataSourceContextHolder.setDataSource(value)
                logger.info { "设置默认数据源: $value" }
            }
        }

        try {
            return point.proceed()
        } finally {
            DataSourceContextHolder.clear()
        }

    }
}