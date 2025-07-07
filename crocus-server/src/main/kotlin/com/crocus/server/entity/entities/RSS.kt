package com.crocus.server.entity.entities

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.crocus.server.entity.base.Universal
import com.crocus.server.entity.base.UniversalDate
import java.math.BigInteger
import java.time.LocalDateTime

@TableName("t_rss_feeds")
class RSS : Universal, UniversalDate {

    @TableId(type = IdType.AUTO)
    override var id: BigInteger? = null

    @TableField(value = "feed_uuid")
    override var uuid: String? = null


    @TableField(value = "feed_language_code")
    var languageCode: String? = null

    @TableField(value = "feed_description")
    val description: String? = null

    @TableField("feed_create_time")
    override var createTime: LocalDateTime? = null

    @TableField("feed_update_time")
    override var modifyTime: LocalDateTime? = null

    @TableField("feed_delete_time")
    override var deleteTime: LocalDateTime? = null

    @TableField(value = "admin_id")
    var adminId: BigInteger? = null
}