package com.crocus.server.entity.base

import com.crocus.server.enum.Role
import com.crocus.server.utils.encrypt.Encipher
import com.crocus.server.utils.encrypt.SaltGenerator
import java.io.Serializable


/**
 * 具有账户属性
 */
interface UniversalAccount : Universal, UniversalDate, Serializable {
    var salt: String?
    var authenticator: String?
    var account: String?
    var email: String?
    var password: String?
    var role: Role?
    val code: String?
    val disabled: Int?

    /**
     * 设置盐、设置UUID
     * 将盐和密码拼接起来加密后再赋值给密码
     */

    override fun apply(): UniversalAccount {
        super<Universal>.apply()
        salt = SaltGenerator.get()
        password = Encipher().encrypt(salt + password)
        return this
    }

}