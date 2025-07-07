package com.crocus.server.mapper.inter

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.crocus.server.entity.entities.*
import org.apache.ibatis.annotations.Mapper

interface EasyMapper<T> : BaseMapper<T> {
    fun insertBatchSomeColumn(list: List<T>): Int
}

@Mapper
interface AnnouncementMapper : EasyMapper<Announcement>

@Mapper
interface ArticleMapper : EasyMapper<Article>

@Mapper
interface StoryMapper: EasyMapper<Story>

@Mapper
interface MinioMapper: EasyMapper<MinioFile>

@Mapper
interface BookMapper : EasyMapper<Book>

@Mapper
interface UserQuestionMapper : EasyMapper<UserQuestion>

@Mapper
interface CommonQuestionMapper : EasyMapper<CommonQuestion>

@Mapper
interface MemberMapper : EasyMapper<Member>

