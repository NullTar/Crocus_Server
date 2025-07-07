package com.crocus.server.service

import com.crocus.server.entity.parameter.announcement_type
import com.crocus.server.entity.parameter.article_type
import com.crocus.server.entity.parameter.book_type
import com.crocus.server.entity.parameter.question_type
import com.crocus.server.entity.search.SearchContent
import com.crocus.server.mapper.AnnouncementRepositoryImp
import com.crocus.server.mapper.ArticleRepositoryImp
import com.crocus.server.mapper.BookRepositoryImp
import com.crocus.server.mapper.CommonQuestionRepositoryImp
import com.crocus.server.mapper.MinioRepositoryImp
import com.crocus.server.mapper.inter.SearchContentRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class ElasSyncService(
    private val articleRepository: ArticleRepositoryImp,
    private val bookRepository: BookRepositoryImp,
    private val questionRepository: CommonQuestionRepositoryImp,
    private val announcementRepository: AnnouncementRepositoryImp,
    private val minioRepository: MinioRepositoryImp,
    private val searchContentRepository: SearchContentRepository,
) {

    private val logger = KotlinLogging.logger(this::javaClass.name)

    suspend fun syncArticlesToEs() {
        val articles = articleRepository.syncList()
        val ids = articles.mapNotNull { it.minioId }.toSet()
        val files = minioRepository.syncList(ids)
            .associateBy { it.id }
        val filtered = articles.filter { files.containsKey(it.minioId) }
        filtered.forEach { article ->
            val minioFile = files[article.minioId]
            val uuid = article.uuid
            val key = article.title
            val linkUUID = minioFile?.uuid
            if (!uuid.isNullOrBlank() && !key.isNullOrBlank() && !linkUUID.isNullOrBlank()) {
                val exists = searchContentRepository.existsById(uuid)
                if (!exists) {
                    val doc = SearchContent(uuid, key, linkUUID, article_type)
                    searchContentRepository.save(doc)
                    logger.info { "Inserted $article_type to article with uuid=$uuid" }
                }

            }
        }
    }

    suspend fun syncBooksToEs() {
        val books = bookRepository.syncList()
        val ids = books.mapNotNull { it.cover }.toSet()
        val files = minioRepository.syncList(ids)
            .associateBy { it.id }
        val filtered = books.filter { files.containsKey(it.cover) }
        filtered.forEach { book ->
            val minioFile = files[book.cover]
            val uuid = book.uuid
            val key = book.name
            val linkUUID = minioFile?.uuid
            if (!uuid.isNullOrBlank() && !key.isNullOrBlank() && !linkUUID.isNullOrBlank()) {
                val exists = searchContentRepository.existsById(uuid)
                if (!exists) {
                    val doc = SearchContent(uuid, key, linkUUID, book_type)
                    searchContentRepository.save(doc)
                    logger.info { "Inserted $book_type to Elasticsearch with uuid=$uuid" }
                }
            }
        }
    }

    suspend fun syncAnnouncementsToEs() {
        announcementRepository.syncList().forEach { announcement ->
            val uuid = announcement.uuid
            val key = announcement.title
            if (!uuid.isNullOrBlank() && !key.isNullOrBlank()) {
                val exists = searchContentRepository.existsById(uuid)
                if (!exists) {
                    val doc = SearchContent(uuid, key, type = announcement_type)
                    searchContentRepository.save(doc)
                    logger.info { "Inserted $announcement_type to Elasticsearch with uuid=$uuid" }
                }
            }
        }
    }

    suspend fun syncQuestionsToEs() {
        questionRepository.syncList().forEach { question ->
            val uuid = question.uuid
            val key = question.title
            if (!uuid.isNullOrBlank() && !key.isNullOrBlank()) {
                val exists = searchContentRepository.existsById(uuid)
                if (!exists) {
                    val doc = SearchContent(uuid, key, type = question_type)
                    searchContentRepository.save(doc)
                    logger.info { "Inserted $question_type to Elasticsearch with uuid=$uuid" }
                }
            }
        }
    }

}
