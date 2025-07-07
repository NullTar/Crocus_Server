package com.crocus.server.entity.function

import com.crocus.server.entity.entities.Member

/**
 * 传出操作
 * 清理隐私数据
 * 返回用户状态
 */
fun Member.filter(): Member {
    this.id = null
    // uuid
    this.salt = null
    // role
    // account
    this.password = null
    this.authenticator = null
    // age
    // gender
    // email
    // createTime
    // modifyTime
    // deleteTime
    this.disabled = null
    // lastLoginTime
    this.adminId = null
    // 不用 @JsonIgnore 会有奇怪的问题
    this.code = null

    return this
}






