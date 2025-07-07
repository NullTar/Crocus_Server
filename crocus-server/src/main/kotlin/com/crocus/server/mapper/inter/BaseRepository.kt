package com.crocus.server.mapper.inter

import com.crocus.server.utils.response.Response
import java.math.BigInteger

interface BaseRepository<T> {

    // ********************** 插入 **********************
    suspend fun insert(entity: T): Response
    suspend fun insertList(list: List<T>): Response

    // ********************** 修改 **********************
    suspend fun update(entity: T): Response

    // ********************** 删除 **********************
    suspend fun delete(uuid: String, adminId: BigInteger): Response

    // ********************** 查询 **********************
    suspend fun queryByUUID(uuid: String): T
    suspend fun queryByID(id: BigInteger): T
    suspend fun queryToPage(now: Long, size: Long): List<T>
    suspend fun queryAll(): List<T>
    suspend fun queryByBatchIds(ids: Collection<BigInteger>): List<T>

    suspend fun recommend(id: BigInteger, size: Int): List<T>
    suspend fun recommend(size: Int): List<T>

}