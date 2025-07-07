package com.crocus.server.utils.config

import com.baomidou.mybatisplus.core.injector.AbstractMethod
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector
import com.baomidou.mybatisplus.core.metadata.TableInfo
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order


@Configuration
class MybatisPlusConfig {

    @Bean
    fun defaultInterceptor(): MybatisPlusInterceptor {
        val interceptor = MybatisPlusInterceptor()
        interceptor.addInnerInterceptor(PaginationInnerInterceptor())
        return interceptor
    }

    @Bean
    fun easySqlInjector(): EasySqlInjector = EasySqlInjector()
}



open class EasySqlInjector : DefaultSqlInjector() {
    override fun getMethodList(
        configuration: org.apache.ibatis.session.Configuration,
        clazz: Class<*>,
        tableInfo: TableInfo
    ): List<AbstractMethod> {
        val methodList = super.getMethodList(configuration, clazz, tableInfo)
        methodList += InsertBatchSomeColumn()
        return methodList
    }
}