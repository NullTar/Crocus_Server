package com.crocus.server.service

import com.crocus.server.entity.entities.CommonQuestion
import com.crocus.server.entity.entities.Member
import com.crocus.server.entity.parameter.redis_question_recommend_key
import com.crocus.server.mapper.CommonQuestionRepositoryImp
import com.crocus.server.mapper.MemberRepositoryImp
import com.crocus.server.service.base.BaseService
import com.crocus.server.utils.database.DataSource
import com.crocus.server.utils.database.SourceName
import com.crocus.server.utils.response.Response
import com.crocus.server.utils.type.typeRef
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@DataSource(SourceName.MAIN)
class QuestionServiceImp(
    private val questionRepository: CommonQuestionRepositoryImp,
    private val adminRepository: MemberRepositoryImp,
) : BaseService() {

    suspend fun recommend(): List<CommonQuestion> {
        val fetch = redis.fetch(redis_question_recommend_key, ref = typeRef<List<CommonQuestion>>())
        fetch?.let { return it }
        return questionRepository.recommend(80).also {
            redis.saveWithTransaction(redis_question_recommend_key, it, null)
        }
    }

    suspend fun queryList(now: Long, size: Long): List<CommonQuestion> = questionRepository.queryToPage(now, size)

    suspend fun queryByUUID(uuid: String): CommonQuestion = questionRepository.queryByUUID(uuid)

    @Transactional
    suspend fun insert(member: Member, question: CommonQuestion): Response {
        val query = member.uuid?.let { adminRepository.queryByUUID(it) }
            ?: return Response.DO_NOT_CHANGE_DATA
        if (query.email != member.email || query.account != member.account) {
            return Response.DO_NOT_CHANGE_DATA
        }
        question.apply()
        question.adminId = query.id
        return runCatching {
            questionRepository.insert(question)
            Response.INSERT_SUCCESS
        }.getOrElse {
            Response.INSERT_FAIL
        }
    }
}