package com.crocus.server.mapper

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import com.crocus.server.entity.entities.Announcement
import com.crocus.server.entity.entities.CommonQuestion
import com.crocus.server.mapper.inter.AnnouncementMapper
import com.crocus.server.utils.exception.E
import com.crocus.server.utils.response.Response
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigInteger
import java.time.LocalDateTime

@Repository
class AnnouncementRepositoryImp(mapper: AnnouncementMapper) :
    BaseRepositoryImp<Announcement, AnnouncementMapper>(mapper) {

    override fun buildDeleteWrapper(uuid: String, adminId: BigInteger): KtUpdateWrapper<Announcement> {
        return KtUpdateWrapper(Announcement::class.java)
            .eq(Announcement::uuid, uuid)
            .set(Announcement::adminId, adminId)
            .set(Announcement::deleteTime, LocalDateTime.now())
    }

    override fun buildQueryByUUIDWrapper(uuid: String): KtQueryWrapper<Announcement> {
        return KtQueryWrapper(Announcement::class.java)
            .eq(Announcement::uuid, uuid)
    }

    override fun buildRecommendFirstWrapper(id: BigInteger): KtQueryWrapper<Announcement> {
        return KtQueryWrapper(Announcement::class.java)
            .ge(Announcement::id, id)
            .orderByAsc(Announcement::id)
    }

    override fun buildRecommendSecondWrapper(id: BigInteger): KtQueryWrapper<Announcement> {
        return KtQueryWrapper(Announcement::class.java)
            .lt(Announcement::id, id)
            .orderByAsc(Announcement::id)
    }

    /**
     * 获取最新
     * MARK: DONE
     */
    suspend fun getNew(): List<Announcement> {
        val tenDaysAgo = LocalDateTime.now().minusDays(10)
        val queryWrapper = KtQueryWrapper(Announcement::class.java)
            // 发布时间 >= 10天前
            .ge(Announcement::createTime, tenDaysAgo)
            // 按发布时间倒序排列
            .orderByDesc(Announcement::createTime)
        val result = mapper.selectList(queryWrapper)
        if (result == null) throw E(Response.NOT_FOUND)
        else return result
    }

    suspend fun syncList(): List<Announcement> {
        val wrapper = KtQueryWrapper(Announcement::class.java)
            .select(Announcement::uuid, Announcement::title)
        return mapper.selectList(wrapper)
    }

}