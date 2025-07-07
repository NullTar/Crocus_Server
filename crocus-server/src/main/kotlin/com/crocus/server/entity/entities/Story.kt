package com.crocus.server.entity.entities

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.crocus.server.entity.base.Universal
import com.crocus.server.entity.base.UniversalDate
import java.math.BigInteger
import java.time.LocalDateTime

@TableName("t_stories")
class Story : Universal, UniversalDate {

    @TableId(type = IdType.AUTO)
    override var id: BigInteger? = null

    @TableField(value = "story_uuid")
    override var uuid: String? = null

    @TableField(value = "story_title")
    var title: String? = null

    @TableField(value = "story_content")
    var content: String? = null

    @TableField("story_create_time")
    override val createTime: LocalDateTime? = null

    @TableField("story_modify_time")
    override var modifyTime: LocalDateTime? = null

    @TableField("story_delete_time")
    override var deleteTime: LocalDateTime? = null

    @TableField(value = "admin_id")
    var adminId: BigInteger? = null
}