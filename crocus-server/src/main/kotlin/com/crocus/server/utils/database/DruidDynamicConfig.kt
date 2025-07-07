package com.crocus.server.utils.database

import com.alibaba.druid.pool.DruidDataSource
import javax.sql.DataSource

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate


/**
 * 配置类
 * 多数据源
 */
@Configuration
class DruidDynamicConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.first")
    fun firstDataSource(): DataSource = DruidDataSource()

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.second")
    fun secondDataSource(): DataSource = DruidDataSource()

    @Bean
    @Primary
    fun dataSource(firstDataSource: DataSource, secondDataSource: DataSource): DynamicDataSource {
        val targetDataSource = HashMap<String, DataSource>()
        targetDataSource[SourceName.MAIN] = firstDataSource
        targetDataSource[SourceName.SECONDARY] = secondDataSource
        return DynamicDataSource(firstDataSource, targetDataSource)
    }

    @Bean
    fun jdbcTemplate(ds: DataSource): JdbcTemplate = JdbcTemplate(ds)

}