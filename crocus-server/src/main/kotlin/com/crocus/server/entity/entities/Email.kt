package com.crocus.server.entity.entities

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.crocus.server.entity.base.Universal
import java.math.BigInteger
import java.time.LocalDateTime

@TableName("t_email_logs")
class Email: Universal {

    @TableId(type = IdType.AUTO)
    override var id: BigInteger? = null

    @TableField(value = "email_uuid")
    override var uuid: String? = null

    @TableField(value = "email_recipient")
    var recipient: String? = null

    @TableField(value = "email_subject")
    var subject: String? = null

    @TableField(value = "email_content")
    var content: String? = null

    @TableField(value = "email_status")
    var status: String? = null

    @TableField(value = "email_error_message")
    var errorMessage: String? = null

    @TableField(value = "email_create_time")
    var createTime: LocalDateTime? = null

    @TableField(value = "email_send_time")
    var sendTime: LocalDateTime? = null

    @TableField(value = "email_retry_count")
    var retryCount: Int? = null

    @TableField(value = "admin_id")
    var adminId:BigInteger? = null
}