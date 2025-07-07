package com.crocus.server.entity.entities

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.crocus.server.entity.base.Universal
import java.math.BigInteger
import java.time.LocalDateTime


@TableName("t_submissions")
class Feedback:Universal {

    @TableId(type = IdType.AUTO)
    override var id: BigInteger? = null

    @TableField(value = "submission_uuid")
    override var uuid: String? = null

    @TableField(value = "submission_type")
    var type:String? = null

    @TableField(value = "submission_status")
    var status:String? = null

    @TableField(value = "submission_title")
    var title:String? = null

    @TableField(value = "submission_description")
    val description:String? = null

    @TableField("submission_create_time")
    var createTime: LocalDateTime? = null

    @TableField("submission_update_time")
    var updateTime: LocalDateTime? = null

    @TableField(value = "submission_review_notes")
    var reviewNotes: String? = null

    @TableField(value = "related_id")
    var relatedId: String? = null

    @TableField(value = "user_id")
    var userId: BigInteger? = null

    @TableField(value = "admin_id")
    var adminId: BigInteger? = null
}