package com.crocus.server.entity.base


import com.crocus.server.utils.encrypt.UUIDGenerator
import java.io.Serializable
import java.math.BigInteger


/**
 * 通用属性
 */
interface Universal: Serializable{
    var id: BigInteger?
    var uuid: String?

    /**
     * 设置UUID
     */
    fun apply(): Universal {
        uuid = UUIDGenerator.get()
        return this
    }
}