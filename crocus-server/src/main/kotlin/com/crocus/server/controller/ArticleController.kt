package com.crocus.server.controller

import com.crocus.server.entity.entities.Article
import com.crocus.server.entity.entities.MinioFile
import com.crocus.server.entity.function.detect
import com.crocus.server.entity.parameter.token_prefix
import com.crocus.server.enum.Age
import com.crocus.server.enum.Gender
import com.crocus.server.enum.Role
import com.crocus.server.service.ArticleServiceImp
import com.crocus.server.service.inter.AccountService
import com.crocus.server.utils.response.Response
import com.crocus.server.utils.response.ResponseDTO
import com.crocus.server.utils.response.toDTO
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.math.BigInteger
import kotlin.text.toBigInteger


@RestController
@RequestMapping("/crocus/manager/article")
class ArticleController(private val articleService: ArticleServiceImp, private val accountService: AccountService) {

    @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun upload(
        @RequestHeader(HttpHeaders.AUTHORIZATION) tokenRaw: String,
        @RequestPart("title") title: String,
        @RequestPart("languageCode") languageCode: String,
        @RequestPart("targetAge") targetAge: String,
        @RequestPart("targetGender") targetGender: String,
        @RequestPart("lastModified") lastModified: String,
        @RequestPart("file") file: MultipartFile
    ): ResponseDTO<*> {
        val token = tokenRaw.removePrefix(token_prefix)
        val member = articleService.jwt.parsingJwt(token)
        member.detect(setOf(Role.Admin, Role.SuperAdmin))?.let { return toDTO(it) }
        val article = Article().apply {
            this.title = title
            this.languageCode = languageCode
            this.targetAge = Age.valueOf(targetAge)
            this.targetGender = Gender.valueOf(targetGender)
        }

        val minioFile = MinioFile().apply {
            this.name = title
            this.fileName = file.originalFilename
            this.type = file.contentType
            this.size = file.size
            this.lastModified = lastModified.toBigInteger()
            this.content = file.inputStream

        }
        val result = articleService.insert(member, article, minioFile)
        return toDTO(result)
    }

    @PostMapping("/query")
    suspend fun query(
        @RequestHeader(HttpHeaders.AUTHORIZATION) tokenRaw: String,
        @RequestParam(required = false) id: BigInteger?,
        @RequestParam(required = false) uuid: String?,
    ): ResponseDTO<*> {
        val token = tokenRaw.removePrefix(token_prefix)
        val member = articleService.jwt.parsingJwt(token)
        member.detect(setOf(Role.Admin, Role.SuperAdmin))?.let { return toDTO(it) }
        val result = id?.let { articleService.queryByID(id) }
            ?: uuid?.let { articleService.queryByUUID(uuid) }
            ?: return toDTO(Response.ERROR_PARAMETER)
        return toDTO(data = result)
    }


}