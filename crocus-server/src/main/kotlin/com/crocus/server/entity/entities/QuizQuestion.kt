package com.crocus.server.entity.entities

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.crocus.server.entity.base.Universal
import com.crocus.server.entity.base.UniversalDate
import java.math.BigInteger
import java.time.LocalDateTime

@TableName("t_quizzes_questions")
class QuizQuestion : Universal, UniversalDate {

    @TableId(type = IdType.AUTO)
    override var id: BigInteger? = null

    @TableField(value = "question_uuid")
    override var uuid: String? = null

    @TableField(value = "question_text")
    var text: String? = null

    @TableField(value = "quizzes_supplement")
    var supplement: String? = null

    @TableField(value = "question_type")
    var type: String? = null

    @TableField(value = "question_options")
    var option: String? = null

    @TableField(value = "question_correct_answer")
    var correctAnswer: String? = null

    @TableField(value = "question_score")
    var score: Int? = null

    @TableField(value = "question_create_time")
    override var createTime: LocalDateTime? = null

    @TableField(value = "question_update_time")
    override var modifyTime: LocalDateTime? = null

    @TableField(value = "email_delete_time")
    override var deleteTime: LocalDateTime? = null

    @TableField(value = "question_quiz_id")
    var quizId: BigInteger? = null
}