package com.crocus.server.entity.entities

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.crocus.server.entity.base.Universal
import com.crocus.server.entity.base.UniversalDate
import java.math.BigInteger
import java.time.LocalDateTime


@TableName("t_quizzes")
class Quiz : Universal, UniversalDate {

    @TableId(type = IdType.AUTO)
    override var id: BigInteger? = null

    @TableField(value = "quiz_uuid")
    override var uuid: String? = null

    @TableField(value = "quiz_title")
    var title: String? = null

    @TableField(value = "quiz_difficulty")
    var difficulty: String? = null

    @TableField(value = "quiz_description")
    var description: String? = null

    @TableField(value = "quiz_time_limit")
    var timeLimit: Long? = null

    @TableField("quiz_create_time")
    override var createTime: LocalDateTime? = null

    @TableField("quiz_modify_time")
    override var modifyTime: LocalDateTime? = null

    @TableField("quiz_delete_time")
    override var deleteTime: LocalDateTime? = null

    @TableField(value = "target_age")
    var targetAge: String? = null

    @TableField(value = "target_gender")
    var targetGender: String? = null

    @TableField(value = "quiz_admin_id")
    var adminId: BigInteger? = null
}