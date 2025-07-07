package com.crocus.server.mapper

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import com.crocus.server.entity.entities.Article
import com.crocus.server.entity.entities.CommonQuestion
import com.crocus.server.mapper.inter.CommonQuestionMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigInteger
import java.time.LocalDateTime


@Repository
class CommonQuestionRepositoryImp(mapper: CommonQuestionMapper) :
    BaseRepositoryImp<CommonQuestion, CommonQuestionMapper>(mapper) {

    override fun buildDeleteWrapper(uuid: String, adminId: BigInteger): KtUpdateWrapper<CommonQuestion> {
        return KtUpdateWrapper(CommonQuestion::class.java)
            .eq(CommonQuestion::uuid, uuid)
            .set(CommonQuestion::adminId, adminId)
            .set(CommonQuestion::deleteTime, LocalDateTime.now())
    }

    override fun buildQueryByUUIDWrapper(uuid: String): KtQueryWrapper<CommonQuestion> {
        return KtQueryWrapper(CommonQuestion::class.java)
            .eq(CommonQuestion::uuid, uuid)
    }

    override fun buildRecommendFirstWrapper(id: BigInteger): KtQueryWrapper<CommonQuestion> {
        return KtQueryWrapper(CommonQuestion::class.java)
            .ge(CommonQuestion::id, id)
            .orderByAsc(CommonQuestion::id)
    }

    override fun buildRecommendSecondWrapper(id: BigInteger): KtQueryWrapper<CommonQuestion> {
        return KtQueryWrapper(CommonQuestion::class.java)
            .lt(CommonQuestion::id, id)
            .orderByAsc(CommonQuestion::id)
    }

    override fun buildRecommendWrapper(size: Int): KtQueryWrapper<CommonQuestion> {
        return KtQueryWrapper(CommonQuestion::class.java)
            .orderByDesc(CommonQuestion::hit)
            .last("LIMIT $size")
    }

    suspend fun syncList(): List<CommonQuestion> {
        val wrapper = KtQueryWrapper(CommonQuestion::class.java)
            .select(CommonQuestion::uuid, CommonQuestion::title)
        return mapper.selectList(wrapper)
    }

}