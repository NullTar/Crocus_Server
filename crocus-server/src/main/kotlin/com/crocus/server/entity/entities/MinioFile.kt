package com.crocus.server.entity.entities

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.crocus.server.entity.base.Universal
import com.crocus.server.entity.base.UniversalDate
import java.io.InputStream
import java.math.BigInteger
import java.time.LocalDateTime


@TableName("t_minIO_files")
class MinioFile : UniversalDate, Universal {

    @TableId(type = IdType.AUTO)
    override var id: BigInteger? = null

    @TableField(value = "file_uuid")
    override var uuid: String? = null

    @TableField(value = "file_name")
    var name: String? = null

    @TableField(exist = false)
    var fileName: String? = null

    @TableField(value = "file_type")
    var type: String? = null

    @TableField(value = "file_size")
    var size: Long? = null

    @TableField(value = "file_last_modified_time")
    var lastModified: BigInteger? = null

    @TableField(value = "file_bucket")
    var bucket: String? = null

    @TableField(value = "file_object")
    var `object`: String? = null

    @TableField("file_upload_time")
    override val createTime: LocalDateTime? = null

    @TableField("file_delete_time")
    override var deleteTime: LocalDateTime? = null

    @TableField(exist = false)
    var url: String? = null

    @TableField(exist = false)
    var content: InputStream? = null

    @TableField(value = "file_uploader_id")
    var uploaderId: BigInteger? = null

    @TableField(value = "file_description")
    var description: String? = null

    @TableField(value = "file_modifier_id")
    var modifierId: BigInteger? = null

    // 无用字段
    @TableField(exist = false)
    override var modifyTime: LocalDateTime? = null

}