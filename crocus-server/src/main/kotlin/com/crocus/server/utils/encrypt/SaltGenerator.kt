package com.crocus.server.utils.encrypt

import java.security.SecureRandom

/**
 * @author junsilck
 * @date 2024/5/28 21:25 pepper 生成指定长度的随机盐值的十六进制字符串表示的外层盐
 */
class SaltGenerator {
    companion object {
        fun get(): String {
            val saltBytes = ByteArray(8)
            val secureRandom = SecureRandom()
            secureRandom.nextBytes(saltBytes)
            return saltBytes.joinToString("") { "%02X".format(it.toInt() and 0xFF) }
        }
    }
}
