import com.crocus.ehcache
import com.crocus.ktor

dependencies {
    implementation(project(":crocus-server"))

    implementation("io.ktor:ktor-server-netty:$ktor")
    // 请求次数限制插件
    implementation("io.ktor:ktor-server-rate-limit:$ktor")
    // 状态页面配置插件
    implementation("io.ktor:ktor-server-status-pages:$ktor")
    // 多重接受请求插件
    implementation("io.ktor:ktor-server-double-receive:$ktor")
    // 请求头缓存设置插件
    implementation("io.ktor:ktor-server-caching-headers:$ktor")
    // 请求日志
    implementation("io.ktor:ktor-server-call-logging:$ktor")
    // 请求获取数据
    implementation("io.ktor:ktor-server-content-negotiation:$ktor")
    // 跨域插件
    // implementation("io.ktor:ktor-server-cors:${ktor}")
    // Gson
    implementation("io.ktor:ktor-serialization-gson:$ktor")

    implementation("io.ktor:ktor-network-tls-certificates:$ktor")

    implementation("org.ehcache:ehcache:$ehcache")


}


