package com.crocus.server.utils.permission

import com.crocus.server.entity.entities.Member
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.jackson.io.JacksonDeserializer
import io.jsonwebtoken.jackson.io.JacksonSerializer
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class JWT(private val objectMapper: ObjectMapper) {

    @Value("\${spring.jwt.secret}")
    private lateinit var secret: String

    @Value("\${spring.jwt.expiration}")
    private var expiration: Long = 0

    /**
     * 创建一个 Jwt
     * @param member Member
     */
    fun generateJwt(member: Member): String {
        val jwtBuilder = Jwts.builder()
            .header().add("typ", "JWT").and()
            .issuer("Crocus")
            .subject(member.account)
            .issuedAt(Date())
            .expiration(Date(Date().time + expiration))
            .claim("data", member)
            .signWith(Keys.hmacShaKeyFor(secret.toByteArray()))
            .json(JacksonSerializer(objectMapper))
        return jwtBuilder.compact()
    }

    /**
     * 解码一个 Jwt
     * @param jwt String 类型的数据
     */
    fun parsingJwt(jwt: String): Member {
        val parser = Jwts.parser()
            .requireIssuer("Crocus")
            .verifyWith(Keys.hmacShaKeyFor(secret.toByteArray()))
            .json(JacksonDeserializer(objectMapper))
            .build()
        val parsed = parser.parse(jwt)
        val payload = parsed.payload as Map<*, *>
        return objectMapper.convertValue(payload["data"], Member::class.java)
    }

}