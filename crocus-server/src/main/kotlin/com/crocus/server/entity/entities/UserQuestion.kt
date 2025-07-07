package com.crocus.server.entity.entities

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.crocus.server.entity.base.Universal
import com.crocus.server.entity.base.UniversalDate
import java.math.BigInteger
import java.time.LocalDateTime


@TableName("t_user_questions")
class UserQuestion : Universal, UniversalDate {

    @TableId(type = IdType.AUTO)
    override var id: BigInteger? = null

    @TableField(value = "question_uuid")
    override var uuid: String? = null

    @TableField(value = "question_title")
    val title: String? = null

    @TableField(value = "question_description")
    val description: String? = null

    @TableField(value = "question_content")
    val content: String? = null

    @TableField("question_create_time")
    override var createTime: LocalDateTime? = null

    @TableField("question_modify_time")
    override var modifyTime: LocalDateTime? = null

    @TableField("question_delete_time")
    override var deleteTime: LocalDateTime? = null

    @TableField("question_operation")
    var operation: String? = null

    @TableField(value = "user_id")
    var userId: BigInteger? = null

    @TableField(value = "admin_id")
    var adminId: BigInteger? = null

}