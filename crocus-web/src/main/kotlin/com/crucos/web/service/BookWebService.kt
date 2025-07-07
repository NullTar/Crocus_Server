package com.crucos.web.service

import com.crocus.server.entity.entities.Book
import com.crocus.server.service.BookServiceImp
import com.crocus.server.utils.response.response
import com.crucos.web.SpringContext
import java.math.BigInteger

class BookWebService {

    private val bookService = SpringContext.server.getBean(BookServiceImp::class.java)

    suspend fun recommend(id: BigInteger?, size: Int): List<Book> {
        val random = id ?: (1..100).random().toBigInteger()
        return bookService.recommend(random, size)
    }

    suspend fun queryList(now: Long, size: Long): List<Book> =bookService.queryList(now, size)

    suspend fun queryByUUID(uuid: String): Book = bookService.queryByUUID(uuid)


}