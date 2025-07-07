package com.crocus.server.service

import com.crocus.server.entity.entities.Member
import com.crocus.server.entity.entities.Story
import com.crocus.server.mapper.MemberRepositoryImp
import com.crocus.server.mapper.StoryRepositoryImp
import com.crocus.server.service.base.BaseService
import com.crocus.server.utils.database.DataSource
import com.crocus.server.utils.database.SourceName
import com.crocus.server.utils.response.Response
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigInteger

@Service
@DataSource(SourceName.MAIN)
class StoryServiceImp(
    private val repository: StoryRepositoryImp,
    private val adminRepository: MemberRepositoryImp,
) : BaseService() {

    suspend fun recommend(id: BigInteger, size: Int): List<Story> {
        return repository.recommend(id, size)
    }

    @Transactional
    suspend fun insert(member: Member, story: Story): Response {
        val query = member.uuid?.let { adminRepository.queryByUUID(it) }
            ?: return Response.DO_NOT_CHANGE_DATA
        if (query.email != member.email || query.account != member.account) {
            return Response.DO_NOT_CHANGE_DATA
        }
        val storyApply = story.apply {
            adminId = query.id
        }.apply() as Story
        return repository.insert(storyApply)
    }

}