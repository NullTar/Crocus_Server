package com.crocus.server.controller

import com.crocus.server.entity.entities.Book
import com.crocus.server.entity.entities.MinioFile
import com.crocus.server.entity.function.detect
import com.crocus.server.entity.parameter.token_prefix
import com.crocus.server.enum.Age
import com.crocus.server.enum.Gender
import com.crocus.server.enum.Role
import com.crocus.server.service.BookServiceImp
import com.crocus.server.utils.response.ResponseDTO
import com.crocus.server.utils.response.toDTO
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/crocus/manager/book")
class BookController(private val bookServiceImp: BookServiceImp) {

    @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun upload(
        @RequestHeader(HttpHeaders.AUTHORIZATION) tokenRaw: String,
        @RequestPart("name") name: String,
        @RequestPart("author") author: String,
        @RequestPart("isbn") isbn: String,
        @RequestPart("targetAge") targetAge: String,
        @RequestPart("targetGender") targetGender: String,
        @RequestPart("description") description: String,
        @RequestPart("cover") file: MultipartFile
    ): ResponseDTO<*> {
        val token = tokenRaw.removePrefix(token_prefix)
        val member = bookServiceImp.jwt.parsingJwt(token)
        member.detect(setOf(Role.Admin, Role.SuperAdmin))?.let { return toDTO(it) }
        val book = Book().apply {
            this.name = name
            this.author = author
            this.isbn = isbn
            this.targetAge = Age.valueOf(targetAge)
            this.targetGender = Gender.valueOf(targetGender)
            this.description = description
        }

        val minioFile = MinioFile().apply {
            this.name = name
            this.fileName = file.originalFilename
            this.type = file.contentType
            this.size = file.size
            this.content = file.inputStream
        }
        val result = bookServiceImp.insert(member, book, minioFile)
        return toDTO(result)
    }

    @PostMapping("/modifyInfo")
    suspend fun modifyInfo(
        @RequestHeader(HttpHeaders.AUTHORIZATION) tokenRaw: String,
        @RequestBody book: Book
    ): ResponseDTO<*> {
        val token = tokenRaw.removePrefix(token_prefix)
        val member = bookServiceImp.jwt.parsingJwt(token)
        member.detect(setOf(Role.Admin, Role.SuperAdmin))?.let { return toDTO(it) }
        return toDTO(bookServiceImp.modifyInfo(member, book))
    }


    @PostMapping("/modifyCover")
    suspend fun modifyCover(
        @RequestHeader(HttpHeaders.AUTHORIZATION) tokenRaw: String,
        @RequestPart("uuid") uuid: String,
        @RequestPart("name") name: String,
        @RequestPart("cover") file: MultipartFile
    ): ResponseDTO<*> {
        val token = tokenRaw.removePrefix(token_prefix)
        val member = bookServiceImp.jwt.parsingJwt(token)
        member.detect(setOf(Role.Admin, Role.SuperAdmin))?.let { return toDTO(it) }
        val minioFile = MinioFile().apply {
            this.name = name
            this.fileName = file.originalFilename
            this.type = file.contentType
            this.size = file.size
            this.content = file.inputStream
        }
        return toDTO(bookServiceImp.modifyCover(member, uuid, minioFile))
    }

}