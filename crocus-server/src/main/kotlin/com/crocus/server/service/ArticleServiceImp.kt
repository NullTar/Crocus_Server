package com.crocus.server.service

import com.crocus.server.entity.entities.Article
import com.crocus.server.entity.entities.Member
import com.crocus.server.entity.entities.MinioFile
import com.crocus.server.entity.parameter.hot_key
import com.crocus.server.entity.parameter.prefix_article_path
import com.crocus.server.mapper.ArticleRepositoryImp
import com.crocus.server.mapper.MemberRepositoryImp
import com.crocus.server.mapper.MinioRepositoryImp
import com.crocus.server.service.base.BaseService
import com.crocus.server.utils.database.DataSource
import com.crocus.server.utils.database.SourceName
import com.crocus.server.utils.exception.E
import com.crocus.server.utils.files.MinioHandler
import com.crocus.server.utils.response.Response
import com.crocus.server.utils.response.response
import com.crocus.server.utils.time.FormatTime
import com.crocus.server.utils.type.typeRef
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigInteger


@Service
@DataSource(SourceName.MAIN)
class ArticleServiceImp(
    private val articleRepository: ArticleRepositoryImp,
    private val adminRepository: MemberRepositoryImp,
    private val minioRepository: MinioRepositoryImp,
    private val minioHandler: MinioHandler,
    @Value("\${minio.bucket-name}") private val bucketName: String
) : BaseService() {

    suspend fun queryByHot(): String {
        val fetch = redis.fetch(hot_key, ref = typeRef<List<Article>>())
        fetch?.let { return response(it) }
        val randomInt = (1..100).random().toBigInteger()
        val query = articleRepository.recommend(randomInt, 80)
        return response(query).also {
            redis.saveWithTransaction(hot_key, query, 24 * 60)
        }
    }

    suspend fun recommend(id: BigInteger, size: Int): List<Article> {
        val articleData = articleRepository.recommend(id, size)
        val fileIds = articleData.mapNotNull { it.minioId }
        val fileInfoMap = minioRepository
            .queryByBatchIds(fileIds)
            .associateBy { it.id }

        val articles = articleData.map { article ->
            val fileObject = fileInfoMap[article.minioId]?.`object`
            article.url = fileObject?.let { minioHandler.getUrl(it) }
            article.adminId = null
            article.minioId = null
            return@map article
        }
        return articles

    }

    suspend fun queryByID(id: BigInteger): Article = articleRepository.queryByID(id)

    suspend fun queryByUUID(uuid: String): Article {
        val query = articleRepository.queryByUUID(uuid)
        val file = query.minioId?.let { minioRepository.queryByID(it) }
            ?: throw E(Response.QUERY_FAIL)
        val url = file.`object`?.let { minioHandler.getUrl(it) }
        query.url = url
        return query
    }

    suspend fun queryList(now: Long, size: Long): List<Article> = articleRepository.queryToPage(now, size)

    @Transactional
    suspend fun insert(member: Member, article: Article, file: MinioFile): Response {
        val query = member.uuid?.let { adminRepository.queryByUUID(it) }
            ?: return Response.DO_NOT_CHANGE_DATA
        if (query.email != member.email || query.account != member.account) {
            return Response.DO_NOT_CHANGE_DATA
        }
        val articleApply = article.apply() as Article
        val fileApply = file.apply() as MinioFile
        val objectName = "${prefix_article_path}_${FormatTime.getYMD()}/${file.fileName}"
        fileApply.apply {
            bucket = bucketName
            `object` = objectName
            uploaderId = query.id
        }
        return runCatching {
            minioHandler.uploadObject(fileApply)
            val result = minioRepository.queryByObject(objectName)
            if (result == null) {
                minioRepository.insert(fileApply)
            }
            articleApply.apply {
                adminId = query.id
                minioId = result?.id ?: fileApply.id
            }
            articleRepository.insert(articleApply)
            Response.UPLOAD_SUCCESS
        }.getOrElse {
            minioHandler.deleteObject(objectName)
            minioRepository.delete(fileApply.uuid!!, query.id!!)
            articleRepository.delete(articleApply.uuid!!, query.id!!)
            Response.UPLOAD_FAILED
        }
    }

    suspend fun queryAll(): List<Article> = articleRepository.queryAll()
}