package com.crocus.server.service

import com.crocus.server.entity.search.SearchContent
import com.crocus.server.utils.elastic.EsHelper
import com.crocus.server.utils.exception.E
import com.crocus.server.utils.response.Response
import org.springframework.data.domain.PageRequest
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate
import org.springframework.stereotype.Service


@Service
class SearchServiceImp(val elasticsearch: ElasticsearchRestTemplate) {

    suspend fun searchByKeywordLike(keyword: String, now: Int?, size: Int?): List<SearchContent> {
        val page = PageRequest.of(now ?: 0, size ?: 10)
        val query = EsHelper.buildFuzzyQuery(keyword, SearchContent::key.name, page = page)
        val searchHits = elasticsearch.search(query, SearchContent::class.java)
        return searchHits.map { it.content }.toList()
    }

    suspend fun searchByKeywordLike(keyword: String, type: String, now: Int?, size: Int?): List<SearchContent> {
        val page = PageRequest.of(now ?: 0, size ?: 10)
        val query = EsHelper.buildFuzzyQueryWithFilter(
            keyword, type, filterField = SearchContent::type.name, SearchContent::key.name, page = page
        )
        val searchHits = elasticsearch.search(query, SearchContent::class.java)
        return searchHits.map { it.content }.toList()
    }

    suspend fun searchByKeyword(keyword: String): List<SearchContent> {
        val query = EsHelper.buildExactMatch(keyword, SearchContent::key.name)
        val searchHits = elasticsearch.search(query, SearchContent::class.java)
        return searchHits.map { it.content }.toList()
    }

    suspend fun searchBySpecific(keyword: String): List<SearchContent> {
        val query = EsHelper.buildTermQuery(keyword, SearchContent::key.name)
        val searchHits = elasticsearch.search(query, SearchContent::class.java)
        return searchHits.map { it.content }.toList()
    }


}