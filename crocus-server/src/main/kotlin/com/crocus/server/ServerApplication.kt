package com.crocus.server

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.PropertySource
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy
@ComponentScan("com.crocus")
@MapperScan("com.crocus.server.mapper")
@PropertySource("classpath:application.properties")
@SpringBootApplication(
    scanBasePackages = ["com.crocus"],
    exclude = [
        DruidDataSourceAutoConfigure::class,
        SecurityAutoConfiguration::class,
    ]
)

class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}
