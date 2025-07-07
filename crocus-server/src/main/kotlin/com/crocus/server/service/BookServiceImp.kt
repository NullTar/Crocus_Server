package com.crocus.server.service

import com.crocus.server.entity.entities.Book
import com.crocus.server.entity.entities.Member
import com.crocus.server.entity.entities.MinioFile
import com.crocus.server.entity.parameter.prefix_book_path
import com.crocus.server.mapper.BookRepositoryImp
import com.crocus.server.mapper.MemberRepositoryImp
import com.crocus.server.mapper.MinioRepositoryImp
import com.crocus.server.service.base.BaseService
import com.crocus.server.utils.database.DataSource
import com.crocus.server.utils.database.SourceName
import com.crocus.server.utils.exception.E
import com.crocus.server.utils.files.MinioHandler
import com.crocus.server.utils.response.Response
import com.crocus.server.utils.time.FormatTime
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigInteger

@Service
@DataSource(SourceName.MAIN)
class BookServiceImp(
    private val bookRepository: BookRepositoryImp,
    private val adminRepository: MemberRepositoryImp,
    private val minioRepository: MinioRepositoryImp,
    private val minioHandler: MinioHandler,
    @Value("\${minio.bucket-name}") private val bucketName: String
) : BaseService() {

    private val logger = KotlinLogging.logger(this.javaClass.name)

    suspend fun recommend(id: BigInteger, size: Int): List<Book> {
        val bookData = bookRepository.recommend(id, size)
        val coverIds = bookData.mapNotNull { it.cover }
        val fileInfoMap = minioRepository
            .queryByBatchIds(coverIds)
            .associateBy { it.id }

        val books = bookData.map { book ->
            val fileObject = fileInfoMap[book.cover]?.`object`
            book.url = fileObject?.let { minioHandler.getUrl(it) }
            book.adminId = null
            book.cover = null
            return@map book
        }
        return books
    }

    suspend fun queryList(now: Long, size: Long): List<Book> = bookRepository.queryToPage(now, size)

    suspend fun queryByUUID(uuid: String): Book {
        val query = bookRepository.queryByUUID(uuid)
        val file = query.cover?.let { minioRepository.queryByID(it) }
            ?: throw E(Response.QUERY_FAIL)
        val url = file.`object`?.let { minioHandler.getUrl(it) }
        query.url = url
        return query
    }

    @Transactional
    suspend fun insert(member: Member, book: Book, file: MinioFile): Response {
        val query = member.uuid?.let { adminRepository.queryByUUID(it) } ?: return Response.DO_NOT_CHANGE_DATA
        if (query.email != member.email || query.account != member.account) {
            return Response.DO_NOT_CHANGE_DATA
        }
        val bookApply = book.apply() as Book
        val fileApply = file.apply() as MinioFile
        val objectName = "${prefix_book_path}_${FormatTime.getYMD()}/${file.fileName}"
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
            bookApply.apply {
                adminId = query.id
                cover = result?.id ?: fileApply.id
            }
            bookRepository.insert(bookApply)
            Response.UPLOAD_SUCCESS
        }.getOrElse {
            val adminId = query.id ?: throw it
            minioHandler.deleteObject(objectName)
            logger.info { "Error deleting $objectName" }
            fileApply.uuid?.let { uuid -> minioRepository.delete(uuid, adminId) }
            bookApply.uuid?.let { uuid -> bookRepository.delete(uuid, adminId) }
            Response.UPLOAD_FAILED
        }
    }


    suspend fun modifyInfo(member: Member, book: Book): Response {
        val query = member.uuid?.let { adminRepository.queryByUUID(it) }
            ?: return Response.DO_NOT_CHANGE_DATA
        if (query.email != member.email || query.account != member.account) {
            return Response.DO_NOT_CHANGE_DATA
        }
        val queryBook = book.uuid?.let { bookRepository.queryByUUID(it) }
            ?: return Response.DO_NOT_CHANGE_DATA
        queryBook.adminId = query.id
        book.name?.let { queryBook.name = it }
        book.author?.let { queryBook.author = it }
        book.targetGender?.let { queryBook.targetGender = it }
        book.targetAge?.let { queryBook.targetAge = it }
        book.isbn?.let { queryBook.isbn = it }
        book.description?.let { queryBook.description = it }
        return bookRepository.update(queryBook)
    }

    suspend fun modifyCover(member: Member, uuid: String, file: MinioFile): Response {
        val query = member.uuid?.let { adminRepository.queryByUUID(it) }
            ?: return Response.DO_NOT_CHANGE_DATA
        if (query.email != member.email || query.account != member.account) {
            return Response.DO_NOT_CHANGE_DATA
        }
        val queryBook = bookRepository.queryByUUID(uuid)
        val fileApply = file.apply() as MinioFile
        val objectName = "${prefix_book_path}_${FormatTime.getYMD()}/${file.fileName}"
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
            queryBook.apply {
                adminId = query.id
                cover = result?.id ?: fileApply.id
            }
            bookRepository.update(queryBook)
            Response.UPLOAD_SUCCESS
        }.getOrElse {
            val adminId = query.id ?: throw it
            minioHandler.deleteObject(objectName)
            logger.info { "Error deleting $objectName" }
            fileApply.uuid?.let { uuid -> minioRepository.delete(uuid, adminId) }
            Response.UPLOAD_FAILED
        }

    }

}