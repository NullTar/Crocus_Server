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


@TableName("t_books")
class Book : Universal, UniversalDate {

    @TableId(type = IdType.AUTO)
    override var id: BigInteger? = null

    @TableField(value = "book_uuid")
    override var uuid: String? = null

    @TableField(value = "book_name")
    var name: String? = null

    @TableField(value = "book_author")
    var author: String? = null

    @TableField(value = "book_cover_object")
    var cover: BigInteger? = null

    @TableField(exist = false)
    var url: String? = null

    @TableField(value = "book_description")
    var description: String? = null

    @TableField(value = "book_isbn")
    var isbn: String? = null

    @TableField(value = "target_age")
    var targetAge: Age? = null

    @TableField(value = "target_gender")
    var targetGender: Gender? = null

    @TableField("book_create_time")
    override var createTime: LocalDateTime? = null

    @TableField("book_modify_time")
    override var modifyTime: LocalDateTime? = null

    @TableField("book_delete_time")
    override var deleteTime: LocalDateTime? = null

    @TableField(value = "admin_id")
    var adminId: BigInteger? = null

}