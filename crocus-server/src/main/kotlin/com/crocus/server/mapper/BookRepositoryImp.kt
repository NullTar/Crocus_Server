package com.crocus.server.mapper

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import com.crocus.server.entity.entities.Article
import com.crocus.server.entity.entities.Book
import com.crocus.server.mapper.inter.BookMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigInteger
import java.time.LocalDateTime

@Repository
class BookRepositoryImp(mapper: BookMapper) :
    BaseRepositoryImp<Book, BookMapper>(mapper) {

    override fun buildDeleteWrapper(uuid: String, adminId: BigInteger): KtUpdateWrapper<Book> {
        return KtUpdateWrapper(Book::class.java)
            .eq(Book::uuid, uuid)
            .set(Book::adminId, adminId)
            .set(Book::deleteTime, LocalDateTime.now())
    }

    override fun buildQueryByUUIDWrapper(uuid: String): KtQueryWrapper<Book> {
        return KtQueryWrapper(Book::class.java)
            .eq(Book::uuid, uuid)
    }

    override fun buildRecommendFirstWrapper(id: BigInteger): KtQueryWrapper<Book> {
        return KtQueryWrapper(Book::class.java)
            .ge(Book::id, id)
            .orderByAsc(Book::id)
    }

    override fun buildRecommendSecondWrapper(id: BigInteger): KtQueryWrapper<Book> {
        return KtQueryWrapper(Book::class.java)
            .lt(Book::id, id)
            .orderByAsc(Book::id)
    }

    suspend fun syncList(): List<Book> {
        val wrapper = KtQueryWrapper(Book::class.java)
            .select(Book::uuid, Book::cover, Book::name)
            .isNull(Book::deleteTime)
            .isNotNull(Book::cover)
        return mapper.selectList(wrapper)
    }

}
