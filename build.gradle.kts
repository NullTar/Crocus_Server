import com.crocus.*
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.kotlin.plugin.spring") version "2.1.0"
    id("org.springframework.boot") version "2.6.13"
    id("io.spring.dependency-management") version "1.1.7"
}


allprojects {
    repositories {
        mavenLocal()
        mavenHuawei()
        mavenAlibaba()
        google()
        mavenCentral()
    }
}


// 所有子项目的配置
subprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    group = projectGroup
    version = projectVersion

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter") {
            exclude(group = "com.squareup.okhttp3", module = "okhttp")
        }
        implementation("com.squareup.okhttp3:okhttp:$okhttp")

        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutines")

        implementation("io.github.microutils:kotlin-logging:$kotlinLogging")

        implementation("com.baomidou:mybatis-plus-boot-starter:$mybatisPlus")

    }

    java {
        sourceCompatibility = sourceVersion
        targetCompatibility = targetVersion
    }

    kotlin {
        compilerOptions {
            apiVersion.set(KotlinVersion.KOTLIN_2_1)
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }

    tasks.test {
        useJUnitPlatform()
    }

}



