package com.crocus.server.mapper

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import com.crocus.server.entity.entities.Article
import com.crocus.server.mapper.inter.ArticleMapper
import org.springframework.stereotype.Repository
import java.math.BigInteger
import java.time.LocalDateTime


@Repository
class ArticleRepositoryImp(mapper: ArticleMapper) : BaseRepositoryImp<Article, ArticleMapper>(mapper) {

    override fun buildDeleteWrapper(uuid: String, adminId: BigInteger): KtUpdateWrapper<Article> {
        return KtUpdateWrapper(Article::class.java).eq(Article::uuid, uuid).set(Article::adminId, adminId)
            .set(Article::deleteTime, LocalDateTime.now())
    }

    override fun buildQueryByUUIDWrapper(uuid: String): KtQueryWrapper<Article> {
        return KtQueryWrapper(Article::class.java).eq(Article::uuid, uuid)
    }

    override fun buildRecommendFirstWrapper(id: BigInteger): KtQueryWrapper<Article> {
        return KtQueryWrapper(Article::class.java).ge(Article::id, id).orderByAsc(Article::id)
    }

    override fun buildRecommendSecondWrapper(id: BigInteger): KtQueryWrapper<Article> {
        return KtQueryWrapper(Article::class.java).lt(Article::id, id).orderByAsc(Article::id)
    }

    suspend fun syncList(): List<Article> {
        val wrapper = KtQueryWrapper(Article::class.java)
            .select(Article::uuid, Article::minioId, Article::title)
            .isNull(Article::deleteTime)
            .isNotNull(Article::minioId)
        return mapper.selectList(wrapper)
    }

}