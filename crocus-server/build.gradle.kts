import com.crocus.*

dependencies {

    implementation("commons-codec:commons-codec:$codec")

    api("io.jsonwebtoken:jjwt-api:$jwt")
    implementation("io.jsonwebtoken:jjwt-impl:$jwt")
    implementation("io.jsonwebtoken:jjwt-jackson:$jwt")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("io.minio:minio:$minio")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")

    implementation("dev.turingcomplete:kotlin-onetimepassword:$auth")

    implementation("com.alibaba:druid-spring-boot-starter:$druidVersion")

    implementation("com.mysql:mysql-connector-j")

    implementation("javax.servlet:javax.servlet-api")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // Controller 支持协程挂起
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation(platform("org.junit:junit-bom:$junitBom"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}