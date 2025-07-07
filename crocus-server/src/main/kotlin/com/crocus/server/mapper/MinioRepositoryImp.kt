package com.crocus.server.mapper

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import com.crocus.server.entity.entities.MinioFile
import com.crocus.server.mapper.inter.MinioMapper
import com.crocus.server.utils.exception.E
import com.crocus.server.utils.response.Response
import org.springframework.stereotype.Repository
import java.math.BigInteger
import java.time.LocalDateTime

@Repository
class MinioRepositoryImp(mapper: MinioMapper) : BaseRepositoryImp<MinioFile, MinioMapper>(mapper) {
    override fun buildDeleteWrapper(uuid: String, adminId: BigInteger): KtUpdateWrapper<MinioFile> {
        return KtUpdateWrapper(MinioFile::class.java).eq(MinioFile::uuid, uuid).set(MinioFile::modifierId, adminId)
            .set(MinioFile::deleteTime, LocalDateTime.now())
    }

    override fun buildQueryByUUIDWrapper(uuid: String): KtQueryWrapper<MinioFile> {
        return KtQueryWrapper(MinioFile::class.java).eq(MinioFile::uuid, uuid)
    }

    suspend fun queryByObject(value: String): MinioFile? {
        val wrapper = KtQueryWrapper(MinioFile::class.java)
            .eq(MinioFile::`object`, value)
        return mapper.selectOne(wrapper)
    }

    suspend fun syncList(ids: Set<BigInteger>?): List<MinioFile> {
        if (ids.isNullOrEmpty()) throw E(Response.QUERY_FAIL)
        val wrapper = KtQueryWrapper(MinioFile::class.java)
            .`in`(MinioFile::id, ids)
            .select(MinioFile::uuid, MinioFile::id)
        return mapper.selectList(wrapper)
    }

}