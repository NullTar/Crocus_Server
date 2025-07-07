package com.crocus.server.mapper

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.crocus.server.entity.base.Universal
import com.crocus.server.entity.base.UniversalDate
import com.crocus.server.mapper.inter.BaseRepository
import com.crocus.server.mapper.inter.EasyMapper
import com.crocus.server.utils.exception.E
import com.crocus.server.utils.response.Response
import java.math.BigInteger
import java.time.LocalDateTime

abstract class BaseRepositoryImp<T : Universal, M : EasyMapper<T>>
    (protected val mapper: M) : BaseRepository<T> {

    abstract fun buildDeleteWrapper(uuid: String, adminId: BigInteger): KtUpdateWrapper<T>
    abstract fun buildQueryByUUIDWrapper(uuid: String): KtQueryWrapper<T>

    open fun buildRecommendWrapper(size: Int): KtQueryWrapper<T> {
        throw UnsupportedOperationException("This repository does not support recommendations.")
    }

    open fun buildRecommendFirstWrapper(id: BigInteger): KtQueryWrapper<T> {
        throw UnsupportedOperationException("This repository does not support recommendations.")
    }

    open fun buildRecommendSecondWrapper(id: BigInteger): KtQueryWrapper<T> {
        throw UnsupportedOperationException("This repository does not support recommendations.")
    }

    /**
     * TODO 部分数据需要更新 elas 索引
     */
    override suspend fun insert(entity: T): Response {
        val result = mapper.insert(entity)
        return if (result == 1) return Response.INSERT_SUCCESS
        else Response.INSERT_FAIL
    }

    override suspend fun insertList(list: List<T>): Response {
        if (list.isEmpty()) return Response.INSERT_FAIL
        val result = mapper.insertBatchSomeColumn(list)
        return if (result != list.size) Response.INSERT_FAIL
        else Response.INSERT_SUCCESS
    }

    override suspend fun update(entity: T): Response {
        entity as UniversalDate
        entity.modifyTime = LocalDateTime.now()
        val result = mapper.updateById(entity)
        return if (result != 1) Response.UPDATE_FAIL
        else Response.UPDATE_SUCCESS
    }


    override suspend fun delete(uuid: String, adminId: BigInteger): Response {
        val wrapper = buildDeleteWrapper(uuid, adminId)
        val result = mapper.update(wrapper)
        return if (result != 1) Response.DELETE_FAIL
        else Response.DELETE_SUCCESS
    }

    override suspend fun queryByUUID(uuid: String): T {
        val queryWrapper = buildQueryByUUIDWrapper(uuid)
        return mapper.selectOne(queryWrapper) ?: throw E(Response.QUERY_FAIL)
    }

    override suspend fun queryByID(id: BigInteger): T {
        return mapper.selectById(id) ?: throw E(Response.QUERY_FAIL)
    }

    override suspend fun queryToPage(now: Long, size: Long): List<T> {
        val result = mapper.selectPage(Page(now, size), null).records
        if (result.isEmpty()) throw E(Response.QUERY_FAIL)
        return result
    }

    override suspend fun queryAll(): List<T> {
        val result = mapper.selectList(null)
        if (result.isEmpty()) throw E(Response.QUERY_FAIL)
        return result
    }

    override suspend fun queryByBatchIds(ids: Collection<BigInteger>): List<T> {
        return mapper.selectBatchIds(ids)
    }

    override suspend fun recommend(id: BigInteger, size: Int): List<T> {
        val result = buildRecommendFirstWrapper(id)
            .last("LIMIT $size")
            .let { mapper.selectList(it) }
        if (result.size < size) {
            val queryAgain = buildRecommendSecondWrapper(id)
                .last("LIMIT ${size - result.size}")
                .let { mapper.selectList(it) }
            return result + queryAgain
        }
        return result
    }

    override suspend fun recommend(size: Int): List<T> {
        return mapper.selectList(buildRecommendWrapper(size))
    }

}