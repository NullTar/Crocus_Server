package com.crocus.server.entity.entities

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.crocus.server.entity.base.Universal
import java.math.BigInteger

@TableName("t_email_attachments")
class Attachment: Universal {

    @TableId(type = IdType.AUTO)
    override var id: BigInteger? = null

    @TableField(value = "attachments_uuid")
    override var uuid: String? = null

    @TableField(value = "attachments_email_id")
    var emailId: BigInteger? = null

    @TableField(value = "attachments_group_id")
    var groupId: BigInteger? = null

}