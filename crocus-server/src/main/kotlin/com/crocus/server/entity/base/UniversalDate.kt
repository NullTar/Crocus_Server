package com.crocus.server.entity.base

import java.io.Serializable
import java.time.LocalDateTime


interface UniversalDate : Serializable {

    val createTime: LocalDateTime?
    var modifyTime: LocalDateTime?
    var deleteTime: LocalDateTime?

}