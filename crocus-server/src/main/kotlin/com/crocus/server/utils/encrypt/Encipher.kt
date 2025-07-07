package com.crocus.server.utils.encrypt

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component


/**
 * @author junsilck
 * @date 2024/5/28 21:34
 * 密码工具类 实现加盐加密 / 加盐解密
 */
@Component
class Encipher {

    private val crypt = BCryptPasswordEncoder()

    /**
     * @param password 原始密码
     * @return String 加密后的密码
     * @date 2024/5/28 21:55
     * 外部需要校验!!!
     */
    fun encrypt(password: String): String = crypt.encode(password)

    /**
     * @param password 原始密码
     * @param encrypt 加密后的密码
     * @return Boolean 匹配返回 True 不匹配返回 false
     * @date 2024/5/28 21:55
     */
    fun decrypt(password: String?, encrypt: String?): Boolean {
        if (password.isNullOrBlank() || password.length < 8 || encrypt.isNullOrBlank()) return false
        return crypt.matches(password, encrypt)
    }
}

