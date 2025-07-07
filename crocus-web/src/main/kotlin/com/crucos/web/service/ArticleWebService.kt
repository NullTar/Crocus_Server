package com.crucos.web.service

import com.crocus.server.entity.entities.Article
import com.crocus.server.entity.entities.Book
import com.crocus.server.entity.entities.MinioFile
import com.crocus.server.service.ArticleServiceImp
import com.crocus.server.utils.response.Response
import com.crocus.server.utils.response.response
import com.crucos.web.SpringContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigInteger


class ArticleWebService {

    private val articleService = SpringContext.server.getBean(ArticleServiceImp::class.java)

    // TODO 缓存
    suspend fun queryByHot(): String = articleService.queryByHot()

    suspend fun recommend(id: BigInteger?, size: Int): List<Article> {
        val random = id ?: (1..100).random().toBigInteger()
        return articleService.recommend(random, size)
    }

    suspend fun queryByUUID(uuid: String): Article = articleService.queryByUUID(uuid)

    suspend fun queryList(now: Long, size: Long): String = response(articleService.queryList(now, size))

}

