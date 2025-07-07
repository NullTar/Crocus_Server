package com.crocus.server.entity.entities

import com.baomidou.mybatisplus.annotation.FieldStrategy
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.crocus.server.entity.base.Universal
import com.crocus.server.entity.base.UniversalAccount
import com.crocus.server.enum.Age
import com.crocus.server.enum.Gender
import com.crocus.server.enum.Role
import com.crocus.server.utils.encrypt.UUIDGenerator
import org.apache.ibatis.type.EnumTypeHandler
import java.math.BigInteger
import java.time.LocalDateTime

@TableName("t_members")
class Member : Universal, UniversalAccount {

    @TableId(type = IdType.AUTO)
    override var id: BigInteger? = null

    @TableField(value = "member_uuid")
    override var uuid: String? = null

    @TableField(value = "member_salt")
    override var salt: String? = null

    @TableField("member_role", typeHandler = EnumTypeHandler::class)
    override var role: Role? = null

    @TableField(value = "member_account")
    override var account: String? = null

    @TableField(value = "member_password")
    override var password: String? = null

    @TableField("member_authenticator")
    override var authenticator: String? = null

    @TableField(value = "member_age")
    var age: Age? = null

    @TableField(value = "member_gender", typeHandler = EnumTypeHandler::class)
    var gender: Gender? = null

    @TableField("member_email")
    override var email: String? = null

    @TableField("member_create_time")
    override var createTime: LocalDateTime? = null

    @TableField("member_modify_time")
    override var modifyTime: LocalDateTime? = null

    @TableField("member_delete_time")
    override var deleteTime: LocalDateTime? = null

    /**
     * 1: 未禁用 默认
     * 2: 已禁用
     */
    @TableField("member_isBanned")
    override var disabled: Int? = null

    @TableField("member_last_login_time")
    var lastLoginTime: LocalDateTime? = null

    @TableField("admin_id")
    var adminId: BigInteger? = null

    // 验证码
    @TableField(exist = false)
    override var code: String? = null

    fun applyCode() {
        code = UUIDGenerator.generateCode()
    }

    /**
     * 调用此方法前要先赋值 password
     * 设置 UUID
     * 设置 盐
     * 加密密码
     * 设置禁用默认值
     */
    override fun apply(): Member {
        super<UniversalAccount>.apply()
        disabled = 1
        return this
    }

}