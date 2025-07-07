package com.crucos.web.service

import com.crocus.server.entity.entities.CommonQuestion
import com.crocus.server.service.QuestionServiceImp
import com.crucos.web.SpringContext
import java.math.BigInteger

class CommonQuestionWebService {
    private val commonQuestionService = SpringContext.server.getBean(QuestionServiceImp::class.java)

    suspend fun recommend(id: BigInteger?, size: Int): List<CommonQuestion> {
//        val radom = id ?: (1..100).random().toBigInteger()
        return commonQuestionService.recommend()
    }

}