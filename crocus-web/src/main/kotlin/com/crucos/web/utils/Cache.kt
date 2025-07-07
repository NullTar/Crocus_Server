package com.crucos.web.utils

import org.ehcache.Cache
import org.ehcache.CacheManager
import org.ehcache.config.builders.CacheConfigurationBuilder
import org.ehcache.config.builders.CacheManagerBuilder
import org.ehcache.config.builders.ResourcePoolsBuilder


/** 这个单例是调用ehCache的实例 */
object Cache {

    // 懒加载 初始化defaultCacheManager
    private val defaultCacheManager by lazy { createCacheManager("crocus-web") }

    /**
     * 外部调用这个方法,返回的是defaultCacheManager的Cache
     *
     * @return Cache<String,String> 返回一个Cache
     */
    fun getCache(): Cache<String, String> {
        return defaultCacheManager.getCache("crocus-web", String::class.java, String::class.java)
    }

    /**
     * 新建一个CacheManager并获取其Cache 正常情况下只需要一个
     * 此方法流程 创建一个CacheManager，通过.getCache获取其Cache实例
     * 此方法可以做一次性二级缓存，普通缓存建议使用getCache方法获取默认的Cache
     *
     * @return Cache<Any, Any>
     */
    fun newCacheWithNewManager(s: String): Cache<Any, Any> {
        return createCacheManager(s).getCache(s, Any::class.java, Any::class.java)
    }

    /**
     * 这个方法为一个通用模板
     *
     * @return 返回一个CacheManager的实例
     */
    private fun createCacheManager(s: String): CacheManager {

        val cacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(
            String::class.java, String::class.java,
            ResourcePoolsBuilder.heap(10)
        ).build()

        val cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
            .withCache(s, cacheConfiguration)
        return cacheManager.build(true)
    }
}