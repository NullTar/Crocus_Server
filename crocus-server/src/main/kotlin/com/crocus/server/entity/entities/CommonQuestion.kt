package com.crocus.server.entity.entities

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.crocus.server.entity.base.Universal
import com.crocus.server.entity.base.UniversalDate
import com.crocus.server.enum.Age
import com.crocus.server.enum.Gender
import java.math.BigInteger
import java.time.LocalDateTime

@TableName("t_common_questions")
class CommonQuestion : Universal,UniversalDate {

    @TableId(type = IdType.AUTO)
    override var id: BigInteger? = null

    @TableField(value = "question_uuid")
    override var uuid: String? = null

    @TableField(value = "question_title")
    val title: String? = null

    @TableField(value = "question_answered")
    val answer: String? = null

    @TableField(value = "question_language_code")
    var languageCode: String? = null

    @TableField(value = "target_age")
    val targetAge: Age? = null

    @TableField(value = "target_gender")
    val targetGender: Gender? = null

    @TableField("question_create_time")
    override var createTime: LocalDateTime? = null

    @TableField("question_modify_time")
    override var modifyTime: LocalDateTime? = null

    @TableField("question_delete_time")
    override var deleteTime: LocalDateTime? = null

    @TableField(value = "question_hits")
    val hit: Int? = null

    @TableField(value = "question_group_id")
    var groupId: String? = null

    @TableField(value = "admin_id")
    var adminId: BigInteger? = null
}