package com.crocus.server.utils.database

/**
 * 自定义注解
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class DataSource(val value: String = SourceName.MAIN)



object SourceName {
    const val MAIN = "main"
    const val SECONDARY = "secondary"
}

