package com.crocus.server.utils.response

import java.io.Serializable

/**
 * @constructor T  数据
 * @property state http 状态
 * @property info type of Response, Code 系统码 和 Message 消息
 * @property data type of T 数据
 */
open class ResponseDTO<T> : Serializable {
    val state: Int = Response.SUCCESS.code
    var info: DeResponse? = null
    var data: T? = null
}

open class DeResponse : Serializable {
    var code: Int? = null
    var message: String? = null
    var tip: String? = null
}