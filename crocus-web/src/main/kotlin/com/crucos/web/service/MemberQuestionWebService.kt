package com.crucos.web.service

import com.crocus.server.service.QuestionServiceImp
import com.crucos.web.SpringContext

class MemberQuestionWebService {

    private val commonQuestionService = SpringContext.server.getBean(QuestionServiceImp::class.java)

}