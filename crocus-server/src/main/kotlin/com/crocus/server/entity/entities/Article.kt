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

@TableName("t_articles")
class Article : Universal, UniversalDate {

    @TableId(type = IdType.AUTO)
    override var id: BigInteger? = null

    @TableField(value = "article_uuid")
    override var uuid: String? = null

    @TableField(value = "article_title")
    var title: String? = null

    @TableField(value = "article_language_code")
    var languageCode: String? = null

    @TableField(value = "target_age")
    var targetAge: Age? = null

    @TableField(value = "target_gender")
    var targetGender: Gender? = null

    @TableField("article_publish_time")
    override val createTime: LocalDateTime? = null

    @TableField("article_modify_time")
    override var modifyTime: LocalDateTime? = null

    @TableField("article_delete_time")
    override var deleteTime: LocalDateTime? = null

    @TableField(value = "file_id")
    var minioId: BigInteger? = null

    @TableField(exist = false)
    var url: String? = null

    @TableField(value = "admin_id")
    var adminId: BigInteger? = null

}