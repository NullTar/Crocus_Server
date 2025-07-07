
package com.crocus

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository

fun RepositoryHandler.mavenAlibaba(): MavenArtifactRepository = maven {
    setUrl("https://maven.aliyun.com/repository/public/")
}

fun RepositoryHandler.mavenHuawei(): MavenArtifactRepository = maven {
    setUrl("https://mirrors.huaweicloud.com/repository/maven/")
}