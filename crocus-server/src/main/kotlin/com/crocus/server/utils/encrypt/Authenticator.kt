package com.crocus.server.utils.encrypt

import com.crocus.server.entity.entities.Member
import dev.turingcomplete.kotlinonetimepassword.GoogleAuthenticator
import dev.turingcomplete.kotlinonetimepassword.OtpAuthUriBuilder
import org.apache.commons.codec.binary.Base32


/**
 * 必须先调用apply 才能使用此方法
 * @return String 生成 totpURL 类似 otpauth://totp/Crocus:$账户名?issuer=Crocus&digits=6&secret= $加密字符串
 */
fun Member.generateUrl(): String {
    val organization = "Crocus"
    // 转为字节数组
    val saltByteArray = this.salt!!.toByteArray(Charsets.UTF_8)
    val passwordByteArray = this.password!!.toByteArray(Charsets.UTF_8)
    // 拼接 密码、用户盐
    val plainSecret = passwordByteArray + saltByteArray
    // ByteArray 的范围必须为5的倍数
    val baseEncodedSecret = Base32().encodeToString(
        plainSecret +
                GoogleAuthenticator
                    .createRandomSecretAsByteArray()
                    .copyOfRange(0, 5 - (plainSecret.size % 5))
    )
    return OtpAuthUriBuilder.forTotp(baseEncodedSecret.toByteArray())
        .label(this.account!!, organization)
        .issuer(organization)
        .digits(6)
        .buildToString()
}

/**
 * 需要裁剪 secret= 后面的值
 */
fun extractSecret(totpUrl: String): String? {
    return totpUrl.split("secret=").getOrNull(1)?.split("&")?.get(0)
}
