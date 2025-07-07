package com.crocus.server.mapper

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import com.crocus.server.entity.entities.Story
import com.crocus.server.mapper.inter.StoryMapper
import org.springframework.stereotype.Repository
import java.math.BigInteger
import java.time.LocalDateTime

@Repository
class StoryRepositoryImp(mapper: StoryMapper) :
    BaseRepositoryImp<Story, StoryMapper>(mapper) {
    override fun buildDeleteWrapper(uuid: String, adminId: BigInteger): KtUpdateWrapper<Story> {
        return KtUpdateWrapper(Story::class.java)
            .eq(Story::uuid, uuid)
            .set(Story::adminId, adminId)
            .set(Story::deleteTime, LocalDateTime.now())
    }

    override fun buildQueryByUUIDWrapper(uuid: String): KtQueryWrapper<Story> {
        return KtQueryWrapper(Story::class.java)
            .eq(Story::uuid, uuid)
    }

    override fun buildRecommendFirstWrapper(id: BigInteger): KtQueryWrapper<Story> {
        return KtQueryWrapper(Story::class.java)
            .ge(Story::id, id)
            .orderByAsc(Story::id)
    }

    override fun buildRecommendSecondWrapper(id: BigInteger): KtQueryWrapper<Story> {
        return KtQueryWrapper(Story::class.java)
            .lt(Story::id, id)
            .orderByAsc(Story::id)
    }

}