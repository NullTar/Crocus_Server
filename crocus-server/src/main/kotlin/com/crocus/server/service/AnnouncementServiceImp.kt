package com.crocus.server.service

import com.crocus.server.entity.entities.Announcement
import com.crocus.server.entity.entities.Book
import com.crocus.server.entity.entities.Member
import com.crocus.server.entity.function.detect
import com.crocus.server.enum.Role
import com.crocus.server.mapper.AnnouncementRepositoryImp
import com.crocus.server.mapper.MemberRepositoryImp
import com.crocus.server.service.base.BaseService
import com.crocus.server.utils.database.DataSource
import com.crocus.server.utils.database.SourceName
import com.crocus.server.utils.response.Response
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@DataSource(SourceName.MAIN)
class AnnouncementServiceImp(
    private val repository: AnnouncementRepositoryImp,
    private val memberRepository: MemberRepositoryImp
) : BaseService() {

    /**
     * 获取最新 10 天内的
     */
    suspend fun getNew(): List<Announcement> = repository.getNew()

    @Transactional
    suspend fun insert(member: Member, announcement: Announcement): Response {
        val query = member.uuid?.let { memberRepository.queryByUUID(it) }
            ?: return Response.DO_NOT_CHANGE_DATA
        announcement.adminId = query.id
        announcement.apply()
        return runCatching {
            repository.insert(announcement)
        }.fold(
            onSuccess = { Response.INSERT_SUCCESS },
            onFailure = { Response.INSERT_FAIL }
        )
    }

    @Transactional
    suspend fun update(member: Member, announcement: Announcement): Response {
        val query = member.uuid?.let { memberRepository.queryByUUID(it) }
            ?: return Response.DO_NOT_CHANGE_DATA
        announcement.apply {
            id = null
            uuid = null
            adminId = query.id
        }
        return repository.update(announcement)
    }

    @Transactional
    suspend fun delete(member: Member, uuid: String): Response {
        val query = member.uuid?.let { memberRepository.queryByUUID(it) }
            ?: return Response.DO_NOT_CHANGE_DATA
        if (query.role != Role.Admin || query.role != Role.SuperAdmin) {
            return Response.FORBIDDEN
        }
        return query.id?.let { repository.delete(uuid, it) }
            ?: Response.DELETE_FAIL
    }

    suspend fun queryByUUID(uuid: String): Announcement = repository.queryByUUID(uuid)


    suspend fun queryByUUID(member: Member, uuid: String): Announcement? {
        val query = member.uuid?.let { memberRepository.queryByUUID(it) }
            ?: return null
        if (query.role != Role.Admin || query.role != Role.SuperAdmin) {
            return null
        }
        return repository.queryByUUID(uuid)
    }

}



