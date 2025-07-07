package com.crocus.server.utils.database
import com.alibaba.druid.pool.DruidDataSource
import com.alibaba.druid.support.http.StatViewServlet
import com.alibaba.druid.support.http.WebStatFilter
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component


/**
 * druid的监控配置
 */
@Configuration
@Component
class DruidConfig {

    // 配置 Druid 监控管理后台的Servlet；
    @Bean
    @ConditionalOnClass(DruidDataSource::class)
    fun statViewServlet(): ServletRegistrationBean<*> {
        // 这些参数可以在 http.StatViewServlet 的父类 ResourceServlet 中找到
        val initParams: MutableMap<String, String> = HashMap()
        initParams["loginUsername"] = "Silck"
        initParams["loginPassword"] = "JunSilck@732613"
        // allow：Druid 后台允许谁可以访问。默认就是允许所有访问。
        initParams["allow"] = "127.0.0.1"
        // 后面参数为空则所有人都能访问，一个具体的ip或ip段
        // deny 禁止访问
        // initParams["deny"] = "192.168.10.132";
        val bean = ServletRegistrationBean(StatViewServlet(), true, "/crocus/druid/*")
        bean.initParameters = initParams
        return bean
    }

    /** 配置一个web监控的filter */
    @Bean
    @ConditionalOnClass(DruidDataSource::class)
    fun webStatFilter(): FilterRegistrationBean<*> {
        val initParams: MutableMap<String, String> = HashMap()
        initParams["exclusions"] = "*.js,*.icon*.gif,*.jpg,*.png,*.css,*.ico,/crocus/druid/*"
        val bean = FilterRegistrationBean(WebStatFilter())
        bean.initParameters = initParams
        bean.urlPatterns = mutableListOf("/*")
        return bean
    }
}

