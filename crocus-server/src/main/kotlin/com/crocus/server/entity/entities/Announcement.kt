package com.crocus.server.entity.entities

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.crocus.server.entity.base.Universal
import com.crocus.server.entity.base.UniversalDate
import com.crocus.server.enum.Role
import java.io.Serializable
import java.math.BigInteger
import java.time.LocalDateTime


@TableName("t_announcements")
class Announcement : Universal, UniversalDate {

    @TableId(type = IdType.AUTO)
    override var id: BigInteger? = null

    @TableField(value = "announcement_uuid")
    override var uuid: String? = null

    @TableField(value = "announcement_title")
    var title: String? = null

    @TableField(value = "announcement_content")
    var content: String? = null

    @TableField(value = "announcement_type")
    var type: String? = null

    @TableField(value = "announcement_status")
    var status: String? = null

    @TableField("announcement_publish_time")
    override var createTime: LocalDateTime? = null

    @TableField("announcement_modify_time")
    override var modifyTime: LocalDateTime? = null

    @TableField("announcement_archive_time")
    override var deleteTime: LocalDateTime? = null


    @TableField(value = "admin_id")
    var adminId: BigInteger? = null
}