package com.crocus.server.controller

import com.crocus.server.service.SearchServiceImp
import com.crocus.server.utils.response.ResponseDTO
import com.crocus.server.utils.response.toDTO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/search")
class SearchController(val searchService: SearchServiceImp) {

    @GetMapping("/like")
    suspend fun searchLike(
        @RequestParam keyword: String,
        @RequestParam(required = false) now: Int?,
        @RequestParam(required = false) size: Int?,
        @RequestParam(required = false) type: String?,
    ): ResponseDTO<*> {
        return type?.let { toDTO(data = searchService.searchByKeywordLike(keyword, type, now, size)) }
            ?: toDTO(data = searchService.searchByKeywordLike(keyword, now, size))
    }

    @GetMapping("/keyword")
    suspend fun searchKeyword(@RequestParam keyword: String): ResponseDTO<*> {
        return toDTO(data = searchService.searchByKeyword(keyword))
    }

    @GetMapping("/specific")
    suspend fun searchSpecific(@RequestParam keyword: String): ResponseDTO<*> {
        return toDTO(data = searchService.searchBySpecific(keyword))
    }


}