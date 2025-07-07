package com.crocus.server.utils.database

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import javax.sql.DataSource

object DataSourceContextHolder {
    private val contextHolder = ThreadLocal<String>()
    fun setDataSource(ds: String) = contextHolder.set(ds)
    fun getDataSource(): String = contextHolder.get() ?: SourceName.MAIN
    fun clear() = contextHolder.remove()
}

/** 动态数据源配置 */
open class DynamicDataSource() : AbstractRoutingDataSource() {

    constructor(defaultDataSource: DataSource, targetSource: Map<String, DataSource>) : this() {
        super.setDefaultTargetDataSource(defaultDataSource)
        super.setTargetDataSources(targetSource.toMap())
        super.afterPropertiesSet()
    }

    override fun determineCurrentLookupKey(): String = DataSourceContextHolder.getDataSource()
}

