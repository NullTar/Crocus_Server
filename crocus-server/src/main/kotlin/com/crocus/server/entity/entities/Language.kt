package com.crocus.server.entity.entities

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.math.BigInteger


@TableName("t_languages")
class Language {

    @TableId(type = IdType.AUTO)
    var id: BigInteger? = null

    @TableField(value = "language_code")
    var languageCode: String? = null

    @TableField(value = "language_name")
    var languageName: String? = null

    @TableField(value = "admin_id")
    var adminId: BigInteger? = null
}